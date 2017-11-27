package com.github.robining.config.proxy;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.github.robining.config.Config;
import com.github.robining.config.interfaces.ui.toolbar.AbstractToolbar;
import com.github.robining.config.interfaces.ui.toolbar.IToolbar;

/**
 * Created by Administrator on 2017\11\15 0015.
 */

public class ProxyToolbar extends AbstractToolbar {
    public ProxyToolbar(Context context) {
        super(context);
    }

    public ProxyToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProxyToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected IToolbar provideRealToolbar() {
        return Config.getInstance().getUiProvider().provideToolbar(getContext());
    }
}
