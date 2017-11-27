package com.github.robining.helper.paging.request;

import com.github.robining.helper.paging.PagingHelper;
import com.github.robining.helper.paging.callback.IRefreshCallback;

import io.reactivex.Observable;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/9/22.
 * Email:496349136@qq.com
 */

public class RefreshRequest<T> extends AbstractPagingRequest<T, IRefreshCallback<T>> {
    public RefreshRequest(PagingHelper pagingHelper, IRefreshCallback<T> callback) {
        super(pagingHelper, callback);
    }

    @Override
    protected Observable<T> getRealRequest(IRefreshCallback<T> callback, int pageIndex, int pageSize) {
        return callback.onRequestData();
    }

    @Override
    protected void onBindData(PagingHelper pagingHelper, IRefreshCallback<T> callback, int pageIndex, int pageSize, T data) {
        callback.onBindData(data);
    }
}
