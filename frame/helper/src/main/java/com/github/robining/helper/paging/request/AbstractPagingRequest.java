package com.github.robining.helper.paging.request;

import com.github.robining.helper.SimpleObserver;
import com.github.robining.helper.paging.PagingHelper;
import com.github.robining.helper.paging.callback.ICallback;
import com.github.robining.helper.transformers.PagingTransformer;

import java.util.Collection;

import io.reactivex.Observable;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/9/22.
 * Email:496349136@qq.com
 */

public abstract class AbstractPagingRequest<T, C extends ICallback<T>> implements IPagingRequest {
    private PagingHelper pagingHelper;
    private C callback;

    public AbstractPagingRequest(PagingHelper pagingHelper, C callback) {
        this.pagingHelper = pagingHelper;
        this.callback = callback;
    }

    @Override
    public void request(final int pageIndex, final int pageSize) {
        getRealRequest(callback, pageIndex, pageSize)
                .compose(new PagingTransformer<T>(pagingHelper.getBuilder().getLifeCycleProvider(), pagingHelper, pagingHelper.getRetryListener()))
                .subscribe(new SimpleObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        super.onNext(t);
                        boolean thisRequestHaveData = t != null && (!(t instanceof Collection) || !((Collection) t).isEmpty());//如果是一个对象那么判断对象是否为null,如果是一个集合,判断集合是否为空
                        if (thisRequestHaveData) {
                            pagingHelper.setCurPageIndex(pageIndex);
                        }
                        pagingHelper.setHaveData(pagingHelper.haveData() || thisRequestHaveData);//已经有数据了或者本次有数据

                        if (thisRequestHaveData && t instanceof Collection) {//如果是集合 检测是否满
                            thisRequestHaveData = ((Collection) t).size() >= pageSize;
                        }
                        pagingHelper.setHaveMoreData(thisRequestHaveData);

                        onBindData(pagingHelper, callback, pageIndex, pageSize, t);
                    }
                });
    }

    protected abstract Observable<T> getRealRequest(C callback, final int pageIndex, final int pageSize);

    protected abstract void onBindData(PagingHelper pagingHelper, C callback, final int pageIndex, final int pageSize, T data);
}
