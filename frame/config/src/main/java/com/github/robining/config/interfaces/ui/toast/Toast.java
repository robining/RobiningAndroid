package com.github.robining.config.interfaces.ui.toast;

import android.content.Context;
import android.support.annotation.StringRes;

import com.github.robining.config.Config;

/**
 * 功能描述:Toast的样式定义接口
 * Created by LuoHaifeng on 2017/5/4.
 * Email:496349136@qq.com
 */

public abstract class Toast {
    /**
     * 弹出提示消息
     *
     * @param message 消息内容
     */
    public void info(String message) {
        info(Config.getInstance().provideContext(), message);
    }

    public void info(@StringRes int message) {
        info(Config.getInstance().provideContext().getResources().getString(message));
    }

    public void info(Context context, @StringRes int message) {
        info(context, context.getResources().getString(message));
    }

    public abstract void info(Context context, String message);

    /**
     * 弹出警告消息
     *
     * @param message 消息内容
     */
    public void warn(String message) {
        warn(Config.getInstance().provideContext(), message);
    }
    public void warn(@StringRes int message) {
        warn(Config.getInstance().provideContext().getResources().getString(message));
    }

    public void warn(Context context, @StringRes int message) {
        warn(context, context.getResources().getString(message));
    }
    public abstract void warn(Context context, String message);

    /**
     * 弹出错误消息
     *
     * @param message 消息内容
     */
    public void error(String message) {
        error(Config.getInstance().provideContext(), message);
    }
    public void error(@StringRes int message) {
        error(Config.getInstance().provideContext().getResources().getString(message));
    }

    public void error(Context context, @StringRes int message) {
        error(context, context.getResources().getString(message));
    }
    public abstract void error(Context context, String message);

    /**
     * 弹出成功消息
     *
     * @param message 消息内容
     */
    public abstract void success(Context context, String message);
    public void success(@StringRes int message) {
        success(Config.getInstance().provideContext().getResources().getString(message));
    }

    public void success(Context context, @StringRes int message) {
        success(context, context.getResources().getString(message));
    }
    public void success(String message) {
        success(Config.getInstance().provideContext(), message);
    }
}
