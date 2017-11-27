package com.github.robining.uiimpl.toast;

import android.content.Context;

import com.github.robining.config.interfaces.ui.toast.Toast;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/6/28.
 * Email:496349136@qq.com
 */

public class ToastImpl extends Toast {
    @Override
    public void info(Context context, String message) {
        ToastUtil.toast(context, message);
    }

    @Override
    public void warn(Context context, String message) {
        ToastUtil.toast(context, message);
    }

    @Override
    public void error(Context context, String message) {
        ToastUtil.toast(context, message);
    }

    @Override
    public void success(Context context, String message) {
        ToastUtil.toast(context, message);
    }
}
