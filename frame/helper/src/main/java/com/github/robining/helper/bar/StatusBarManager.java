package com.github.robining.helper.bar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
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
    private Integer statusBarColor = null;//状态栏颜色(顶部)
    private Integer navigationBarColor = null;//导航栏颜色(底部或右边)
    private boolean statusBarPadding = true;//是否预留状态栏空间
    private boolean navigationBarPadding = true;//是否预留导航栏颜色
    private boolean darkStatusTextColor = true;//状态栏文字颜色
    private int defaultStatusBarColor = Color.BLACK;
    private int defaultNavigationBarColor = Color.BLACK;

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
                int uiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                if (!statusBarPadding) {
                    uiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                }
                if (!navigationBarPadding) {
                    uiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                }
                window.getDecorView().setSystemUiVisibility(uiVisibility);

                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (!statusBarPadding) {
                    window.setStatusBarColor(Color.TRANSPARENT);
                } else {
                    window.setStatusBarColor(statusBarColor == null ? defaultStatusBarColor : statusBarColor);
                }

                if (!navigationBarPadding) {
                    window.setNavigationBarColor(Color.TRANSPARENT);
                } else {
                    window.setNavigationBarColor(navigationBarColor == null ? defaultNavigationBarColor : navigationBarColor);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//设置状态栏和导航栏的透明
                SystemStatusManager systemStatusManager = new SystemStatusManager(activity);//必须设置在addFlags之后
                systemStatusManager.setStatusBarTintEnabled(true);
                systemStatusManager.setStatusBarTintColor(statusBarColor == null ? defaultStatusBarColor : statusBarColor);
                systemStatusManager.setNavigationBarTintEnabled(true);
                systemStatusManager.setNavigationBarTintColor(navigationBarColor == null ? defaultNavigationBarColor : navigationBarColor);
            }
        }

        //设置状态栏文字颜色
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

    /**
     * 是否处于竖屏状态
     *
     * @param context
     * @return
     */
    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

}
