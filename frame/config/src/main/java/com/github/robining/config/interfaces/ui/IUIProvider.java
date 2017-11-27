package com.github.robining.config.interfaces.ui;

import android.content.Context;
import android.view.View;

import com.github.robining.config.interfaces.ui.dialog.AlertDialog;
import com.github.robining.config.interfaces.ui.dialog.WaitProcessDialog;
import com.github.robining.config.interfaces.ui.loading.ILoadingHelper;
import com.github.robining.config.interfaces.ui.refresh.IRefreshLayout;
import com.github.robining.config.interfaces.ui.toast.Toast;
import com.github.robining.config.interfaces.ui.toolbar.IToolbar;

/**
 * 功能描述:UI部分提供器
 * Created by LuoHaifeng on 2017/6/28.
 * Email:496349136@qq.com
 */

public interface IUIProvider {
    WaitProcessDialog applyWaitProcessDialog(Context context);

    AlertDialog applyAlertDialog(Context context);

    Toast applyToast();

    IRefreshLayout applyRefreshLayout(Context context);

    ILoadingHelper applyLoadingHelper(View view);

    IToolbar provideToolbar(Context context);
}
