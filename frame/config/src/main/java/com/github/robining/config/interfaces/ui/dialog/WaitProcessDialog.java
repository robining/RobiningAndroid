package com.github.robining.config.interfaces.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * 功能描述:加载进度等待对话框
 * Created by LuoHaifeng on 2017/6/28.
 * Email:496349136@qq.com
 */

public abstract class WaitProcessDialog extends BaseDialog {
    public WaitProcessDialog(@NonNull Context context) {
        super(context);
    }

    public abstract void setMessage(String message);

    public void setMessage(@StringRes int message) {
        setMessage(getContext().getString(message));
    }
}
