package com.github.robining.uiimpl;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;

import com.github.robining.config.interfaces.ui.IUIProvider;
import com.github.robining.config.interfaces.ui.dialog.AlertDialog;
import com.github.robining.config.interfaces.ui.dialog.ProgressDialog;
import com.github.robining.config.interfaces.ui.dialog.WaitProcessDialog;
import com.github.robining.config.interfaces.ui.loading.ILoadingHelper;
import com.github.robining.config.interfaces.ui.refresh.IRefreshLayout;
import com.github.robining.config.interfaces.ui.toast.Toast;
import com.github.robining.config.interfaces.ui.toolbar.IToolbar;
import com.github.robining.uiimpl.dialog.AlterDialogImpl;
import com.github.robining.uiimpl.dialog.ProgressDialogImpl;
import com.github.robining.uiimpl.dialog.WaitProcessDialogImpl;
import com.github.robining.uiimpl.loading.LoadingHelperImpl;
import com.github.robining.uiimpl.refresh.ProxySmartRefreshLayout;
import com.github.robining.uiimpl.toast.ToastImpl;
import com.github.robining.uiimpl.toolbar.ToolbarImpl;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/6/28.
 * Email:496349136@qq.com
 */

public class UIProvider implements IUIProvider {
    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context);
            }
        });

        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(context).setPrimaryColor(Color.TRANSPARENT).setSpinnerStyle(SpinnerStyle.Translate).setDrawableArrowSize(20);
            }
        });
    }

    @Override
    public WaitProcessDialog applyWaitProcessDialog(Context context) {
        return new WaitProcessDialogImpl(context);
    }

    @Override
    public AlertDialog applyAlertDialog(Context context) {
        return new AlterDialogImpl(context);
    }

    @Override
    public ProgressDialog applyProgressDialog(Context context) {
        return new ProgressDialogImpl(context);
    }

    @Override
    public Toast applyToast() {
        return new ToastImpl();
    }

    @Override
    public IRefreshLayout applyRefreshLayout(Context context) {
        return new ProxySmartRefreshLayout(context);
    }

    @Override
    public ILoadingHelper applyLoadingHelper(View view) {
        return LoadingHelperImpl.with(view);
    }

    @Override
    public IToolbar provideToolbar(Context context) {
        return new ToolbarImpl(context);
    }
}
