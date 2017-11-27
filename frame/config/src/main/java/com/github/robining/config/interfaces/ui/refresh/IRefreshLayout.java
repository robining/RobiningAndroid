package com.github.robining.config.interfaces.ui.refresh;

/**
 * 刷新组件的基本功能
 * Created by LuoHaifeng on 2017/3/9.
 */

public interface IRefreshLayout {
    /**
     * 当代理初始化完成回调
     */
    void onProxyCompleted();

    /**
     * 开始刷新
     */
    void startRefresh();

    /**
     * 设置是否开启刷新功能
     * @param enable 开关
     */
    void setRefreshEnable(boolean enable);

    /**
     * 设置是否开启加载更多功能
     * @param enable 开关
     */
    void setLoadMoreEnable(boolean enable);

    /**
     * 刷新完成
     */
    void refreshCompleted();

    /**
     * 加载更多完成
     */
    void loadMoreCompleted();

    /**
     * 加载结束
     */
    void loadedAll(boolean isLoadedAll);

    /**
     * 设置加载监听器(回调刷新和加载更多)
     * @param listener 监听器
     */
    void setRefreshLoadMoreListener(OnRefreshLoadMoreListener listener);

    interface OnRefreshLoadMoreListener{
        void onRefresh();
        void onLoadMore();
    }
}
