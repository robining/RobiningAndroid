package com.github.robining.helper.paging.callback;

import io.reactivex.Observable;

public interface IPagingCallback<T> extends ICallback<T> {
        Observable<T> onRequestData(int pageIndex, int pageSize);

        void onBindData(int pageIndex, int pageSize, T data);
    }