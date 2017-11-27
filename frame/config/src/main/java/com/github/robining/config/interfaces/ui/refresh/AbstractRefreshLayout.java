package com.github.robining.config.interfaces.ui.refresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 功能描述:上下拉刷新布局
 * Created by LuoHaifeng on 2017/5/3.
 * Email:496349136@qq.com
 */

public abstract class AbstractRefreshLayout extends FrameLayout implements IRefreshLayout {
    protected IRefreshLayout realRefreshLayout;

    public AbstractRefreshLayout(Context context) {
        super(context);
        init();
    }

    public AbstractRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AbstractRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        realRefreshLayout = provideRealRefreshLayout();//初始化代理对象
        post(new Runnable() {
            @Override
            public void run() {
                //将当前对象的子控件移动到代理对象
                if (getChildCount() > 0) {
                    View view = getChildAt(0);
                    removeAllViews();
                    ((ViewGroup) realRefreshLayout).addView(view);
                    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    addView((View) realRefreshLayout, lp);
                    realRefreshLayout.onProxyCompleted();
                }
            }
        });
    }

    protected abstract IRefreshLayout provideRealRefreshLayout();

    protected IRefreshLayout getIRefreshLayout() {
        return realRefreshLayout;
    }

    @Override
    public void startRefresh() {
        getIRefreshLayout().startRefresh();
    }

    @Override
    public void setRefreshEnable(boolean enable) {
        getIRefreshLayout().setRefreshEnable(enable);
    }

    @Override
    public void setLoadMoreEnable(boolean enable) {
        getIRefreshLayout().setLoadMoreEnable(enable);
    }

    @Override
    public void refreshCompleted() {
        getIRefreshLayout().refreshCompleted();
    }

    @Override
    public void loadMoreCompleted() {
        getIRefreshLayout().loadMoreCompleted();
    }

    @Override
    public void loadedAll(boolean isLoadedAll) {
        getIRefreshLayout().loadedAll(isLoadedAll);
    }

    @Override
    public void setRefreshLoadMoreListener(OnRefreshLoadMoreListener listener) {
        getIRefreshLayout().setRefreshLoadMoreListener(listener);
    }

}
