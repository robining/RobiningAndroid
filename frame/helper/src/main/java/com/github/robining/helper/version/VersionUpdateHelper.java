package com.github.robining.helper.version;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import com.github.robining.config.Config;
import com.github.robining.config.interfaces.ui.dialog.AlertDialog;
import com.github.robining.config.interfaces.ui.dialog.ProgressDialog;
import com.github.robining.config.utils.SPUtils;
import com.github.robining.config.utils.SystemUtil;
import com.github.robining.helper.ILifeCycleProvider;
import com.github.robining.helper.net.TransformerProvider;
import com.github.robining.helper.net.progress.ProgressEntity;
import com.github.robining.helper.net.progress.ProgressInterceptor;
import com.github.robining.helper.net.progress.ProgressListener;
import com.github.robining.helper.net.progress.ProgressListenerPool;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static io.reactivex.Observable.create;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/26.
 * Email:496349136@qq.com
 */

public class VersionUpdateHelper {
    private static final String LAST_VERSION_INFO = "LAST_VERSION_INFO";
    private static SPUtils spUtils = SPUtils.getInstance("version_manager");

    /**
     * 获取最后一次获取到的版本更新内容
     *
     * @return
     */
    public static IVersionEntity getLastVersionInfo() {
        return spUtils.getSerializable(LAST_VERSION_INFO, null);
    }

    /**
     * 设置最新一次获取到的版本信息
     *
     * @param entity 版本内容
     */
    public static void setLastVersionInfo(IVersionEntity entity) {
        spUtils.put(LAST_VERSION_INFO, entity);
    }

    private static VersionStyleProvider versionStyleProvider = new VersionStyleProvider() {
        @Override
        public ProgressDialog provideProgressDialog(Context context) {
            return Config.getInstance().getUiProvider().applyProgressDialog(context);
        }

        @Override
        public AlertDialog provideFoundNewVersionDialog(Context context) {
            return Config.getInstance().getUiProvider().applyAlertDialog(context);
        }
    };
    private static QueryVersionInfoObservableProvider queryVersionInfoObservableProvider;//版本更新请求提供器

    public static QueryVersionInfoObservableProvider getQueryVersionInfoObservableProvider() {
        return queryVersionInfoObservableProvider;
    }

    public static void setQueryVersionInfoObservableProvider(QueryVersionInfoObservableProvider queryVersionInfoObservableProvider) {
        VersionUpdateHelper.queryVersionInfoObservableProvider = queryVersionInfoObservableProvider;
    }

    public static VersionStyleProvider getVersionStyleProvider() {
        return versionStyleProvider;
    }

    public static void setVersionStyleProvider(VersionStyleProvider versionStyleProvider) {
        VersionUpdateHelper.versionStyleProvider = versionStyleProvider;
    }

    /**
     * 通过已知的版本信息进行版本更新
     *
     * @param activity          页面
     * @param lifeCycleProvider 生命周期控制器
     * @param updateEntity      版本信息
     */
    public static void update(final Activity activity, final ILifeCycleProvider lifeCycleProvider, final IVersionEntity updateEntity) {
        Observable.just(updateEntity)
                .compose(VersionUpdateHelper.provideUpdateTransformer(activity, lifeCycleProvider))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateResult>() {
                    @Override
                    public void accept(@NonNull UpdateResult updateResult) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * @param activity          页面
     * @param lifeCycleProvider 生命周期控制器
     * @return 版本更新进度控制器（包括对话框操作）
     */
    public static ObservableTransformer<IVersionEntity, UpdateResult> provideUpdateTransformer(final Activity activity, final ILifeCycleProvider lifeCycleProvider) {
        return new ObservableTransformer<IVersionEntity, UpdateResult>() {
            @Override
            public ObservableSource<UpdateResult> apply(Observable<IVersionEntity> upstream) {
                return upstream
                        .compose(TransformerProvider.<IVersionEntity>provideSchedulers(lifeCycleProvider))
                        .flatMap(new Function<IVersionEntity, ObservableSource<UpdateResult>>() {
                            @Override
                            public ObservableSource<UpdateResult> apply(@NonNull final IVersionEntity iVersionEntity) throws Exception {
                                PackageManager packageManager = activity.getPackageManager();
                                PackageInfo packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
                                if (iVersionEntity._getVersionCode_() > packageInfo.versionCode) {
                                    return create(new ObservableOnSubscribe<UpdateResult>() {
                                        @Override
                                        public void subscribe(ObservableEmitter<UpdateResult> e) throws Exception {
                                            VersionUpdateHelper.showUpdateDialog(activity, lifeCycleProvider, iVersionEntity, e);
                                        }
                                    });
                                } else {
                                    return Observable.just(new UpdateResult().setUpdateEntity(iVersionEntity).setResult(UpdateResult.Result.RESULT_NOT_NEED_UPDATE));
                                }
                            }
                        });
            }
        };
    }

    /**
     * 显示发现新版本对话框
     */
    public static void showUpdateDialog(final Activity activity, final ILifeCycleProvider lifeCycleProvider, final IVersionEntity updateEntity, final ObservableEmitter<UpdateResult> e) {
        final AlertDialog dialog = versionStyleProvider.provideFoundNewVersionDialog(activity);
        dialog.setTitle("发现新版本:" + updateEntity._getVersionName_());
        dialog.setContent(updateEntity._getUpdateContent_());
        dialog.setPositiveButton("立即更新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDownloadDialog(activity, lifeCycleProvider, updateEntity, e);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(!updateEntity._isForceUpdate_());
        dialog.setCanceledOnTouchOutside(false);
        if (updateEntity._isForceUpdate_()) {
            dialog.setNegativeButton(null, null);
        } else {
            dialog.setNegativeButton("忽略", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    e.onNext(new UpdateResult().setUpdateEntity(updateEntity).setResult(UpdateResult.Result.RESULT_CANCELED_UPDATE));
                    e.onComplete();
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    /**
     * 显示下载进度对话框
     **/
    public static void showDownloadDialog(final Activity activity, final ILifeCycleProvider lifeCycleProvider, final IVersionEntity updateEntity, final ObservableEmitter<UpdateResult> e) {
        final ProgressDialog dialog = versionStyleProvider.provideProgressDialog(activity);
        dialog.setTitle("正在更新");
        dialog.setMax(100);
        dialog.setProgress(0);
        dialog.setCancelable(!updateEntity._isForceUpdate_());
        dialog.show();
        ProgressListener listener = ProgressListenerPool.getInstance().registerListener(lifeCycleProvider);
        listener.getListener()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ProgressEntity>() {
                    @Override
                    public void accept(@NonNull ProgressEntity progressEntity) throws Exception {
                        if (!progressEntity.isRequest()) {
                            dialog.setProgress((int) ((double) progressEntity.getProgress() / progressEntity.getTotal() * 100));
                            if (progressEntity.isCompleted()) {
                                dialog.setProgress(100);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Config.getInstance().getUiProvider().applyToast().error("progress error");
                    }
                });

        final Disposable disposable = provideDownloadTask(listener.getId(), updateEntity._getApkDownloadUrl_(), lifeCycleProvider)
                .flatMap(new Function<File, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(@NonNull File file) throws Exception {
                        return SystemUtil.requestInstallApk(activity, file.getAbsolutePath(), Config.getInstance().getFileProviderAuthority());
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        dialog.setProgress(100);
                        e.onNext(new UpdateResult().setUpdateEntity(updateEntity).setResult(UpdateResult.Result.RESULT_UPDATE_SUCCESS));
                        e.onComplete();
                        //                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        e.onError(throwable);
                        //                        dialog.dismiss();
                    }
                });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!disposable.isDisposed()) {
                    disposable.dispose();
                }
                e.onNext(new UpdateResult().setUpdateEntity(updateEntity).setResult(UpdateResult.Result.RESULT_CANCELED_UPDATE));
                e.onComplete();
            }
        });
    }

    /**
     * 提供下载任务
     */
    private static Observable<File> provideDownloadTask(final String progressListenerId, final String url, ILifeCycleProvider lifeCycleProvider) {
        Observable<ResponseBody> download = Observable.create(new ObservableOnSubscribe<ResponseBody>() {
            @Override
            public void subscribe(final ObservableEmitter<ResponseBody> e) throws Exception {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new ProgressInterceptor())
                        .build();
                HttpUrl httpUrl = HttpUrl.parse(url);
                httpUrl = httpUrl.newBuilder().addQueryParameter(ProgressInterceptor.LISTENER_ID_KEY, progressListenerId).build();
                okHttpClient.newCall(new Request.Builder().url(httpUrl).get().build()).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException ex) {
                        e.onError(ex);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        e.onNext(response.body());
                        e.onComplete();
                    }
                });
            }
        });

        return download.compose(TransformerProvider.provideDownloadTransformer(lifeCycleProvider));
    }
}
