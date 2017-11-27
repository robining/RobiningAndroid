package com.github.robining.helper.paging.request;

import android.support.annotation.Nullable;

import com.github.robining.helper.paging.PagingHelper;
import com.github.robining.helper.paging.callback.ICallback;
import com.github.robining.helper.paging.callback.IPagingCallback;
import com.github.robining.helper.paging.callback.IPagingListCallback;
import com.github.robining.helper.paging.callback.IRefreshCallback;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/9/22.
 * Email:496349136@qq.com
 */

public class PagingRequestFactory {
    @Nullable
    public static <T> IPagingRequest create(PagingHelper<T> pagingHelper, ICallback<T> callback) {
        if (callback instanceof IPagingCallback) {
            return new PagingRequest<>(pagingHelper, (IPagingCallback<T>) callback);
        } else if (callback instanceof IRefreshCallback) {
            return new RefreshRequest<>(pagingHelper, (IRefreshCallback<T>) callback);
        } else if (callback instanceof IPagingListCallback) {
            return new PagingListRequest<>(pagingHelper, (IPagingListCallback<T, ?>) callback);
        }

        return null;
    }
}
