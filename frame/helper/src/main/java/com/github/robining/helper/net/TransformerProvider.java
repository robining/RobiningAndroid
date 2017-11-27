package com.github.robining.helper.net;

import android.support.annotation.Nullable;

import com.github.robining.config.Config;
import com.github.robining.config.interfaces.ui.loading.ILoadingHelper;
import com.github.robining.config.utils.file.FileUtil;
import com.github.robining.helper.ILifeCycleProvider;
import com.github.robining.helper.paging.ILoadingProvider;
import com.github.robining.helper.paging.IPagingProvider;
import com.github.robining.helper.paging.IRefreshProvider;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/4/13.
 * Email:496349136@qq.com
 */

public class TransformerProvider {
    public static <T, K> ObservableTransformer<T, K> convertTo() {
        return new ObservableTransformer<T, K>() {
            @Override
            public ObservableSource<K> apply(Observable<T> upstream) {
                return upstream.map(new Function<T, K>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public K apply(@NonNull T t) throws Exception {
                        return (K) t;
                    }
                });
            }
        };
    }


    /**
     * 提供最基础的线程切换,订阅在子线程,响应在主线程
     *
     * @return
     */
    public static <T> ObservableTransformer<T, T> provideSchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> provideSchedulers(final ILifeCycleProvider lifeCycleProvider) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                if (lifeCycleProvider != null) {
                    upstream = upstream.compose(lifeCycleProvider.<T>bindLifecycle());
                }

                return upstream
                        .compose(TransformerProvider.<T>provideSchedulers());
            }
        };
    }

    /**
     * 提供统一的错误处理
     *
     * @return
     */
    public static <T> ObservableTransformer<T, T> provideErrorHandler() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                                Config.getInstance().getUiProvider().applyToast().error(RequestExceptionHelper.getErrorMessage(throwable));
                            }
                        });
            }
        };
    }

    /***
     * 提供刷新的逻辑响应处理
     *
     * @param lifeCycle      提供生命周期
     * @param refreshProvider 提供刷新数据
     * @param retryListener  提供重试接口
     * @return
     */
    public static <T> ObservableTransformer<T, T> provideRefreshTransformer(final ILifeCycleProvider lifeCycle, final IRefreshProvider refreshProvider, final ILoadingHelper.OnRetryListener retryListener) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                if (!refreshProvider.haveData()) {
                                    refreshProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                                }
                            }
                        })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                if (!refreshProvider.haveData()) {//没有数据切换到错误布局
                                    refreshProvider.provideLoadingHelper().setRetryListener(retryListener).showState(RequestExceptionHelper.getLayoutStateByThrowable(throwable));
                                }
                            }
                        })
                        .doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (refreshProvider.haveData()) {//有数据显示正常布局
                                    refreshProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                                } else {//没有数据切换至空布局
                                    refreshProvider.provideLoadingHelper().setRetryListener(retryListener).showEmpty();
                                }
                            }
                        })
                        .doAfterTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                refreshProvider.provideRefreshLayout().refreshCompleted();
                                refreshProvider.provideRefreshLayout().loadMoreCompleted();
                            }
                        });
            }
        };
    }

    /***
     * 提供分页的逻辑响应处理
     *
     * @param lifeCycle      提供生命周期
     * @param pagingProvider 提供分页操作数据
     * @param retryListener  提供重试接口
     * @return
     */
    public static <T> ObservableTransformer<T, T> providePagingTransformer(final ILifeCycleProvider lifeCycle, final IPagingProvider pagingProvider, final ILoadingHelper.OnRetryListener retryListener) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                if (!pagingProvider.haveData()) {//第一页并且没有数据才替换加载中布局
                                    pagingProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                                }
                            }
                        })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                if (!pagingProvider.haveData()) {//没有数据切换到错误布局
                                    pagingProvider.provideLoadingHelper().setRetryListener(retryListener).showState(RequestExceptionHelper.getLayoutStateByThrowable(throwable));
                                }
                            }
                        })
                        .doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (pagingProvider.haveData()) {//有数据显示正常布局
                                    pagingProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                                } else {//没有数据切换至空布局
                                    pagingProvider.provideLoadingHelper().setRetryListener(retryListener).showEmpty();
                                }
                            }
                        })
                        .doAfterTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                pagingProvider.provideRefreshLayout().refreshCompleted();
                                pagingProvider.provideRefreshLayout().loadMoreCompleted();

                                if (pagingProvider.haveMoreData()) {
                                    pagingProvider.provideRefreshLayout().setLoadMoreEnable(true);
                                } else {
                                    pagingProvider.provideRefreshLayout().setLoadMoreEnable(false);
                                }
                            }
                        });
            }
        };
    }

    /***
     * 开始加载 显示加载中
     * 结束加载 成功  显示正常布局
     * 失败  显示失败布局
     *
     * @param lifeCycle       提供生命周期
     * @param loadingProvider 提供分页操作数据
     * @param retryListener   提供重试接口
     * @return
     */
    public static <T> ObservableTransformer<T, T> provideLoadingTransformer(final ILifeCycleProvider lifeCycle, final ILoadingProvider loadingProvider, final ILoadingHelper.OnRetryListener retryListener) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                            }
                        })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showState(RequestExceptionHelper.getLayoutStateByThrowable(throwable));
                            }
                        })
                        .doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                            }
                        });
            }
        };
    }

    public static <T> ObservableTransformer<T, T> provideLoadingEmptyTransformer(final ILifeCycleProvider lifeCycle, final ILoadingProvider loadingProvider, final ILoadingHelper.OnRetryListener retryListener) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                            }
                        })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showState(RequestExceptionHelper.getLayoutStateByThrowable(throwable));
                            }
                        })
                        .doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (loadingProvider.haveData()) {
                                    loadingProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                                } else {
                                    loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showEmpty();
                                }
                            }
                        });
            }
        };
    }

    public static <T> ObservableTransformer<T, T> provideBackgroundLoadingTransformer(final ILifeCycleProvider lifeCycle, final ILoadingProvider loadingProvider, final ILoadingHelper.OnRetryListener retryListener) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                if (!loadingProvider.haveData()) {
                                    loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                                }
                            }
                        })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                if (!loadingProvider.haveData()) {
                                    loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showState(RequestExceptionHelper.getLayoutStateByThrowable(throwable));
                                }
                            }
                        })
                        .doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                            }
                        });
            }
        };
    }


    public static ObservableTransformer<ResponseBody, File> provideDownloadTransformer(final ILifeCycleProvider lifeCycle) {
        return provideDownloadTransformer(lifeCycle, null);
    }

    public static ObservableTransformer<ResponseBody, File> provideDownloadTransformer(final ILifeCycleProvider lifeCycle, @Nullable final String path) {
        return new ObservableTransformer<ResponseBody, File>() {
            @Override
            public ObservableSource<File> apply(Observable<ResponseBody> upstream) {
                return upstream
                        .compose(TransformerProvider.<ResponseBody>provideSchedulers(lifeCycle))
                        .compose(TransformerProvider.<ResponseBody>provideErrorHandler())
                        .observeOn(Schedulers.io())
                        .flatMap(new Function<ResponseBody, ObservableSource<File>>() {
                            @Override
                            public ObservableSource<File> apply(@NonNull final ResponseBody responseBody) throws Exception {
                                return Observable.create(new ObservableOnSubscribe<File>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<File> e) throws Exception {
                                        File file;
                                        if (path == null) {
                                            file = FileUtil.getRandomFile();
                                        } else {
                                            file = new File(path);
                                        }

                                        FileUtil.writeFile(responseBody.byteStream(), file, true);
                                        e.onNext(file);
                                        e.onComplete();
                                    }
                                });
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
