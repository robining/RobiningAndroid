package com.github.robining.uiimpl.refresh;

import android.content.Context;
import android.util.AttributeSet;

import com.github.robining.config.interfaces.ui.refresh.IRefreshLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

/**
 * 功能描述:SmartRefreshLayout转换
 * Created by LuoHaifeng on 2017/7/25.
 * Email:496349136@qq.com
 */

public class ProxySmartRefreshLayout extends SmartRefreshLayout implements IRefreshLayout {
    public ProxySmartRefreshLayout(Context context) {
        super(context);
    }

    public ProxySmartRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProxySmartRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onProxyCompleted() {
        setEnableAutoLoadmore(true);
        setEnableOverScrollDrag(false);
        setEnableOverScrollBounce(false);
        setEnableFooterFollowWhenLoadFinished(true);
    }

    @Override
    public void startRefresh() {
        super.autoRefresh();
    }

    @Override
    public void setRefreshEnable(boolean enable) {
        super.setEnableRefresh(enable);
    }

    @Override
    public void setLoadMoreEnable(boolean enable) {
        super.setEnableLoadmore(enable);
    }

    @Override
    public void refreshCompleted() {
        super.finishRefresh();
    }

    @Override
    public void loadMoreCompleted() {
        super.finishLoadmore();
    }

    @Override
    public void loadedAll(boolean isLoadedAll) {
        setLoadmoreFinished(isLoadedAll);
    }

    @Override
    public void setRefreshLoadMoreListener(final OnRefreshLoadMoreListener listener) {
        super.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                listener.onLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                listener.onRefresh();
            }
        });
    }
}
