package com.github.robining.config.interfaces.ui.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/6/28.
 * Email:496349136@qq.com
 */

public abstract class BaseDialog extends AppCompatDialog {
    public BaseDialog(Context context) {
        super(context);
        init();
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    protected void init(){}
}
