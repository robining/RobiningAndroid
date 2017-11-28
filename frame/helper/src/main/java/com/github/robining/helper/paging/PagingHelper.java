package com.github.robining.helper.paging;

import com.github.robining.config.interfaces.ui.loading.ILoadingHelper;
import com.github.robining.config.interfaces.ui.refresh.IRefreshLayout;
import com.github.robining.helper.ILifeCycleProvider;
import com.github.robining.helper.paging.callback.ICallback;
import com.github.robining.helper.paging.request.IPagingRequest;
import com.github.robining.helper.paging.request.PagingRequestFactory;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/9/20.
 * Email:496349136@qq.com
 */

public class PagingHelper<T> implements IRefreshLayout.OnRefreshLoadMoreListener, IPagingProvider {
    private Builder builder;
    private ICallback<T> callback;
    private IPagingRequest request;
    private boolean haveMoreData = false;
    private int curPageIndex;//当前页码
    private boolean haveData;//是否已有数据
    private ILoadingHelper.OnRetryListener retryListener = new ILoadingHelper.OnRetryListener() {
        @Override
        public void onRetry(ILoadingHelper loadingHelper) {//错误重试监听器
            startLoading();
        }
    };

    private PagingHelper(Builder builder, ICallback<T> callback) {
        this.builder = builder;
        this.callback = callback;
        this.request = PagingRequestFactory.create(this, callback);
        init();
    }

    public void init() {
        this.curPageIndex = builder.getStartPagerNumber() - 1;
        haveMoreData = false;
        haveData = false;

        if (builder.getRefreshLayout() != null) {
            builder.getRefreshLayout().setRefreshLoadMoreListener(this);
            builder.getRefreshLayout().setLoadMoreEnable(builder.isLoadMoreEnable());
            builder.getRefreshLayout().setRefreshEnable(builder.isRefreshEnable());
        }
    }

    /**
     * 开始刷新（显示下拉刷新状态）
     */
    public void startRefresh() {
        if (builder.getRefreshLayout() != null && builder.isRefreshEnable()) {
            builder.getRefreshLayout().startRefresh();
        }else{
            startLoading();
        }
    }

    /**
     * 开始加载(显示加载中状态)
     */
    public void startLoading() {
        requestData(builder.getStartPagerNumber(), builder.getPageSize());
    }

    /**
     * 请求数据
     *
     * @param pageIndex 页码
     * @param pageSize  每页最大项数
     */
    protected void requestData(final int pageIndex, final int pageSize) {
        if (request != null) {
            request.request(pageIndex, pageSize);
        }
    }

    /**
     * 刷新回调
     */
    @Override
    public void onRefresh() {
        startLoading();
    }

    /**
     * 加载更多回调
     */
    @Override
    public void onLoadMore() {
        requestData(curPageIndex + 1, builder.getPageSize());
    }

    @Override
    public boolean haveMoreData() {
        return haveMoreData;
    }

    @Override
    public boolean isEnableLoadMore() {
        return getBuilder().isLoadMoreEnable();
    }

    @Override
    public boolean haveData() {
        return haveData;
    }

    @Override
    public ILoadingHelper provideLoadingHelper() {
        return builder.getLoadingHelper();
    }

    @Override
    public IRefreshLayout provideRefreshLayout() {
        return builder.getRefreshLayout();
    }

    public Builder getBuilder() {
        return builder;
    }

    public ICallback<T> getCallback() {
        return callback;
    }

    public boolean isHaveMoreData() {
        return haveMoreData;
    }

    public PagingHelper setHaveMoreData(boolean haveMoreData) {
        this.haveMoreData = haveMoreData;
        if (builder.getRefreshLayout() != null) {
            //            builder.getRefreshLayout().setLoadMoreEnable(haveMoreData && builder.isLoadMoreEnable());
            builder.getRefreshLayout().loadedAll(!haveMoreData);
        }
        return this;
    }

    public PagingHelper setCurPageIndex(int curPageIndex) {
        this.curPageIndex = curPageIndex;
        return this;
    }

    public PagingHelper setHaveData(boolean haveData) {
        this.haveData = haveData;
        return this;
    }

    public int getCurPageIndex() {
        return curPageIndex;
    }

    public boolean isHaveData() {
        return haveData;
    }

    public ILoadingHelper.OnRetryListener getRetryListener() {
        return retryListener;
    }

    public static class Builder {
        private int startPagerNumber = 1;
        private int pageSize = 15;
        private IRefreshLayout refreshLayout;
        private ILoadingHelper loadingHelper;
        private ILifeCycleProvider lifeCycleProvider;
        private boolean refreshEnable = true;
        private boolean loadMoreEnable = true;

        public int getStartPagerNumber() {
            return startPagerNumber;
        }

        public Builder setStartPagerNumber(int startPagerNumber) {
            this.startPagerNumber = startPagerNumber;
            return this;
        }

        public int getPageSize() {
            return pageSize;
        }

        public Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public IRefreshLayout getRefreshLayout() {
            return refreshLayout;
        }

        public Builder setRefreshLayout(IRefreshLayout refreshLayout) {
            this.refreshLayout = refreshLayout;
            return this;
        }

        public ILoadingHelper getLoadingHelper() {
            return loadingHelper;
        }

        public Builder setLoadingHelper(ILoadingHelper loadingHelper) {
            this.loadingHelper = loadingHelper;
            return this;
        }

        public ILifeCycleProvider getLifeCycleProvider() {
            return lifeCycleProvider;
        }

        public Builder setLifeCycleProvider(ILifeCycleProvider lifeCycleProvider) {
            this.lifeCycleProvider = lifeCycleProvider;
            return this;
        }

        public boolean isRefreshEnable() {
            return refreshEnable;
        }

        public Builder setRefreshEnable(boolean refreshEnable) {
            this.refreshEnable = refreshEnable;
            return this;
        }

        public boolean isLoadMoreEnable() {
            return loadMoreEnable;
        }

        public Builder setLoadMoreEnable(boolean loadMoreEnable) {
            this.loadMoreEnable = loadMoreEnable;
            return this;
        }

        public <T> PagingHelper<T> build(ICallback<T> callback) {
            return new PagingHelper<>(this, callback);
        }
    }
}
