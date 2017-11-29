package com.github.robining.config.interfaces.ui.dialog;

import android.content.Context;
import android.support.annotation.StringRes;

/**
 * 功能描述:提示类对话框(确定取消)
 * Created by LuoHaifeng on 2017/7/18.
 * Email:496349136@qq.com
 */

public abstract class ProgressDialog extends BaseDialog {

    public ProgressDialog(Context context) {
        super(context);
    }

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public ProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * 设置标题
     * @param title 标题内容
     */
    public abstract void setTitle(CharSequence title);

    public void setTitle(@StringRes int title) {
        setTitle(getContext().getString(title));
    }

    public abstract void setProgress(int progress);

    public abstract void setMax(int max);
}
