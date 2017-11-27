package com.github.robining.helper.bar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/8/3.
 * Email:496349136@qq.com
 */

public class StatusBarManager {
    private Activity activity;//设置状态栏的目标
    private int statusBarColor = Color.BLACK;//状态栏颜色(顶部)
    private int navigationBarColor = Color.BLACK;//导航栏颜色(底部或右边)
    private boolean statusBarPadding = true;//是否预留状态栏空间
    private boolean navigationBarPadding = true;//是否预留导航栏颜色
    private boolean darkStatusTextColor = true;//状态栏文字颜色

    public StatusBarManager(@NonNull Activity activity) {
        this.activity = activity;
    }

    public int getStatusBarColor() {
        return statusBarColor;
    }

    public StatusBarManager setStatusBarColor(int statusBarColor) {
        this.statusBarColor = statusBarColor;
        return this;
    }

    public int getNavigationBarColor() {
        return navigationBarColor;
    }

    public StatusBarManager setNavigationBarColor(int navigationBarColor) {
        this.navigationBarColor = navigationBarColor;
        return this;
    }

    public boolean isStatusBarPadding() {
        return statusBarPadding;
    }

    public StatusBarManager setStatusBarPadding(boolean statusBarPadding) {
        this.statusBarPadding = statusBarPadding;
        return this;
    }

    public boolean isNavigationBarPadding() {
        return navigationBarPadding;
    }

    public StatusBarManager setNavigationBarPadding(boolean navigationBarPadding) {
        this.navigationBarPadding = navigationBarPadding;
        return this;
    }

    public boolean isDarkStatusTextColor() {
        return darkStatusTextColor;
    }

    public StatusBarManager setDarkStatusTextColor(boolean darkStatusTextColor) {
        this.darkStatusTextColor = darkStatusTextColor;
        return this;
    }

    public StatusBarManager commit() {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                );//取消状态栏和导航栏的透明
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusBarColor);
                window.setNavigationBarColor(navigationBarColor);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//设置状态栏和导航栏的透明
                SystemStatusManager systemStatusManager = new SystemStatusManager(activity);//必须设置在addFlags之后
                systemStatusManager.setStatusBarTintEnabled(true);
                systemStatusManager.setStatusBarTintColor(statusBarColor);
                systemStatusManager.setNavigationBarTintEnabled(true);
                systemStatusManager.setNavigationBarTintColor(navigationBarColor);
            }

            ViewGroup contentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
            View childView = null;
            if (contentView != null) {
                childView = contentView.getChildAt(0);
            }

            if (childView != null) {
                childView.setFitsSystemWindows(false);
                int paddingLeft = contentView.getPaddingLeft();
                int paddingTop = contentView.getPaddingTop();
                int paddingRight = contentView.getPaddingRight();
                int paddingBottom = contentView.getPaddingBottom();
                SystemStatusManager systemStatusManager = new SystemStatusManager(activity);
                if (statusBarPadding) {
                    paddingTop += systemStatusManager.getConfig().getStatusBarHeight();
                }
                if (systemStatusManager.getConfig().hasNavigtionBar()) {
                    if (systemStatusManager.getConfig().isNavigationAtBottom() && navigationBarPadding) {
                        paddingBottom += systemStatusManager.getConfig().getNavigationBarHeight();
                    } else {//不在底部便在右边
                        paddingRight += systemStatusManager.getConfig().getNavigationBarWidth();
                    }
                }

                if (!isFullScreen(activity)) {
                    contentView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                } else {
                    if (!systemStatusManager.getConfig().isNavigationAtBottom()) {
                        contentView.setPadding(contentView.getPaddingLeft(), contentView.getPaddingTop(), contentView.getPaddingRight() + paddingRight, contentView.getPaddingBottom());
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setMStatusBarLightMode(window, darkStatusTextColor);
        }

        setMIUIStatusBarLightMode(window, darkStatusTextColor);
        setFlymeStatusBarLightMode(window, darkStatusTextColor);
        return this;
    }

    private boolean setFlymeStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception ignored) {

            }
        }
        return result;
    }

    private boolean setMIUIStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception ignored) {

            }
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setMStatusBarLightMode(final Window window, boolean dark) {
        int flags = window.getDecorView().getSystemUiVisibility();
        window.getDecorView().setSystemUiVisibility(dark ? (flags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) : (flags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
    }

    private boolean isFullScreen(Activity activity) {
        int flag = activity.getWindow().getAttributes().flags;
        return (flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }
}
