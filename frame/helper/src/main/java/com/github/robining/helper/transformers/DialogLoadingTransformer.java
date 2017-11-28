package com.github.robining.helper.transformers;

import android.content.DialogInterface;

import com.github.robining.config.Config;
import com.github.robining.config.interfaces.context.IContextProvider;
import com.github.robining.config.interfaces.ui.dialog.WaitProcessDialog;
import com.github.robining.helper.ILifeCycleProvider;
import com.github.robining.helper.net.TransformerProvider;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017\11\16 0016.
 */

public class DialogLoadingTransformer<T> implements ObservableTransformer<T, T> {
    private ILifeCycleProvider lifeCycle;
    private IContextProvider activityContextProvider;

    public DialogLoadingTransformer(ILifeCycleProvider lifeCycle, IContextProvider activityContextProvider) {
        this.lifeCycle = lifeCycle;
        this.activityContextProvider = activityContextProvider;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        final WaitProcessDialog progressDialog = Config.getInstance().getUiProvider().applyWaitProcessDialog(activityContextProvider.provideContext());
        return upstream
                .compose(lifeCycle.<T>bindLifecycle())
                .compose(TransformerProvider.<T>provideSchedulers())
                .compose(TransformerProvider.<T>provideErrorHandler())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull final Disposable disposable) throws Exception {
                        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                if (!disposable.isDisposed())
                                    disposable.dispose();
                            }
                        });
                        progressDialog.show();
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        progressDialog.dismiss();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        progressDialog.dismiss();
                    }
                });
    }
}