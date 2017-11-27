package com.github.robining.config.proxy;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.github.robining.config.Config;
import com.github.robining.config.interfaces.ui.refresh.AbstractRefreshLayout;
import com.github.robining.config.interfaces.ui.refresh.IRefreshLayout;

public class ProxyRefreshLayout extends AbstractRefreshLayout {
    public ProxyRefreshLayout(Context context) {
        super(context);
    }

    public ProxyRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProxyRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected IRefreshLayout provideRealRefreshLayout() {
        return Config.getInstance().getUiProvider().applyRefreshLayout(getContext());
    }

    @Override
    public void onProxyCompleted() {

    }
}