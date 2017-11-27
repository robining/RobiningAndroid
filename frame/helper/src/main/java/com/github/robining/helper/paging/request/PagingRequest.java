package com.github.robining.helper.paging.request;

import com.github.robining.helper.paging.PagingHelper;
import com.github.robining.helper.paging.callback.IPagingCallback;

import io.reactivex.Observable;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/9/22.
 * Email:496349136@qq.com
 */

public class PagingRequest<T> extends AbstractPagingRequest<T, IPagingCallback<T>> {
    public PagingRequest(PagingHelper pagingHelper, IPagingCallback<T> callback) {
        super(pagingHelper, callback);
    }

    @Override
    protected Observable<T> getRealRequest(IPagingCallback<T> callback, int pageIndex, int pageSize) {
        return callback.onRequestData(pageIndex, pageSize);
    }

    @Override
    protected void onBindData(PagingHelper pagingHelper, IPagingCallback<T> callback, int pageIndex, int pageSize, T data) {
        callback.onBindData(pageIndex, pageSize, data);
    }
}
