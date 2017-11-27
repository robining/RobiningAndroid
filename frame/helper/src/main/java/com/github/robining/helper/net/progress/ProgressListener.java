package com.github.robining.helper.net.progress;

import android.widget.ProgressBar;

import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 进度监听器
 */
public class ProgressListener {
    private String id;
    private Flowable<ProgressEntity> listener;

    public String getId() {
        return id;
    }

    public ProgressListener setId(String id) {
        this.id = id;
        return this;
    }

    public Flowable<ProgressEntity> getListener() {
        return listener;
    }


    public Flowable<ProgressEntity> getListener(final WeakReference<ProgressBar> progressBarWeakReference) {
        return listener.
                doOnNext(new Consumer<ProgressEntity>() {
                    @Override
                    public void accept(@NonNull ProgressEntity progressEntity) throws Exception {
                        ProgressBar progressBar = progressBarWeakReference.get();
                        if (progressBar != null) {
                            progressBar.setMax(100);
                            if (progressEntity.isRequest()) {
                                progressBar.setProgress((int) progressEntity.getProgressPercent());
                            } else {
                                progressBar.setSecondaryProgress((int) progressEntity.getProgressPercent());
                            }
                        }
                    }
                });
    }

    public Flowable<ProgressEntity> getListener(@android.support.annotation.NonNull final ProgressWidget progressWidget) {
        return listener.
                doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        progressWidget.onStart();
                    }
                }).
                doOnNext(new Consumer<ProgressEntity>() {
                    @Override
                    public void accept(@NonNull ProgressEntity progressEntity) throws Exception {
                        progressWidget.onUpdateProgress(progressEntity.isRequest(), progressEntity.getProgress(), progressEntity.getTotal(), progressEntity.getProgressPercent());
                    }
                })
                .doOnCancel(new Action() {
                    @Override
                    public void run() throws Exception {
                        progressWidget.onCanceled();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        progressWidget.onError(throwable);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        progressWidget.onCompleted();
                    }
                });
    }

    public ProgressListener setListener(Flowable<ProgressEntity> listener) {
        this.listener = listener;
        return this;
    }
}