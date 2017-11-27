package com.github.robining.helper.net.progress;

/**
 * Created by fzroa on 2017/10/9.
 */
public abstract class ProgressWidget {
    public void onStart(){}
    public void onUpdateProgress(boolean isRequest,long progress,long total,float percent){}
    public void onCompleted(){}
    public void onCanceled(){}
    public void onError(Throwable ex){}
}
