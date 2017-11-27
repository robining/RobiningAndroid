package com.github.robining.helper.paging.callback;

import io.reactivex.Observable;

public interface IRefreshCallback<T> extends ICallback<T> {
    Observable<T> onRequestData();

    void onBindData(T data);
}