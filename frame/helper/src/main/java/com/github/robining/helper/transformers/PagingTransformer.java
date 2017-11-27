package com.github.robining.helper.transformers;

import com.github.robining.config.interfaces.ui.loading.ILoadingHelper;
import com.github.robining.helper.net.RequestExceptionHelper;
import com.github.robining.helper.net.TransformerProvider;
import com.github.robining.helper.ILifeCycleProvider;
import com.github.robining.helper.paging.IPagingProvider;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 功能描述:分页逻辑处理
 * Created by LuoHaifeng on 2017/9/20.
 * Email:496349136@qq.com
 */

public class PagingTransformer<T> implements ObservableTransformer<T, T> {
    private ILifeCycleProvider lifeCycleProvider;
    private IPagingProvider pagingProvider;
    private ILoadingHelper.OnRetryListener onRetryListener;

    public PagingTransformer(ILifeCycleProvider lifeCycleProvider, IPagingProvider pagingProvider, ILoadingHelper.OnRetryListener onRetryListener) {
        this.lifeCycleProvider = lifeCycleProvider;
        this.pagingProvider = pagingProvider;
        this.onRetryListener = onRetryListener;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        if (lifeCycleProvider != null) {
            upstream = upstream.compose(lifeCycleProvider.<T>bindLifecycle());
        }

        return upstream
                .compose(TransformerProvider.<T>provideSchedulers())
                .compose(TransformerProvider.<T>provideErrorHandler())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        if (!pagingProvider.haveData() && pagingProvider.provideLoadingHelper() != null) {//第一页并且没有数据才替换加载中布局
                            ILoadingHelper loadingHelper = pagingProvider.provideLoadingHelper().setRetryListener(onRetryListener);
                            loadingHelper.showLoading();
                        }
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (!pagingProvider.haveData() && pagingProvider.provideLoadingHelper() != null) {//没有数据切换到错误布局
                            pagingProvider.provideLoadingHelper().setRetryListener(onRetryListener).showState(RequestExceptionHelper.getLayoutStateByThrowable(throwable));
                        }
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (pagingProvider.provideLoadingHelper() != null && pagingProvider.provideLoadingHelper().getState() == ILoadingHelper.State.LOADING) {
                            if (pagingProvider.haveData()) {//有数据显示正常布局
                                pagingProvider.provideLoadingHelper().setRetryListener(onRetryListener).restore();
                            } else {//没有数据切换至空布局
                                pagingProvider.provideLoadingHelper().setRetryListener(onRetryListener).showEmpty();
                            }
                        }

                        if (pagingProvider.provideRefreshLayout() != null) {
                            pagingProvider.provideRefreshLayout().refreshCompleted();
                            pagingProvider.provideRefreshLayout().loadMoreCompleted();
                            pagingProvider.provideRefreshLayout().setLoadMoreEnable(pagingProvider.haveMoreData() && pagingProvider.isEnableLoadMore());
                        }
                    }
                });
    }
}
