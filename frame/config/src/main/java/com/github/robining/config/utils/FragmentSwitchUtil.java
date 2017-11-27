package com.github.robining.config.utils;

import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2017\11\17 0017.
 */

public class FragmentSwitchUtil {
    private FragmentManager fragmentManager;
    private Fragment currentVisibleFragment = null;//当前显示的Fragment
    @IdRes
    private int containerResId;

    public static final int MODE_HIDE_SHOW = 1;
    public static final int MODE_REPLACE = 2;

    @IntDef({MODE_HIDE_SHOW, MODE_REPLACE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {

    }

    public FragmentSwitchUtil(FragmentManager fragmentManager, int containerResId) {
        this.fragmentManager = fragmentManager;
        this.containerResId = containerResId;
    }

    /**
     * 切换fragment
     * @param fragment 切换至的目标
     * @param mode 切换模式
     */
    public void switchFragment(Fragment fragment, @Mode int mode) {
        switch (mode) {
            case MODE_HIDE_SHOW:
                switchFragmentHideShow(fragment);
                break;
            case MODE_REPLACE:
                switchFragmentReplace(fragment);
                break;
            default:
                break;
        }
    }

    /**
     * 以替换的方式切换fragment
     *
     * @param fragment
     */
    private void switchFragmentReplace(Fragment fragment) {
        if (currentVisibleFragment == fragment || fragment == null) {
            return;
        }

        fragmentManager.beginTransaction().replace(containerResId, fragment).commit();
    }

    /**
     * 以显示隐藏的方式切换fragment
     *
     * @param fragment
     */
    private void switchFragmentHideShow(Fragment fragment) {
        if (currentVisibleFragment == fragment || fragment == null) {
            return;
        }

        FragmentTransaction transaction = this.fragmentManager.beginTransaction();
        if (currentVisibleFragment != null) {//如果当前有显示内容,先隐藏
            transaction.hide(currentVisibleFragment);
        }

        this.currentVisibleFragment = fragment;
        if (!fragment.isAdded()) {//判断制定的Fragment是否已经添加到Manager中,若没有,则添加
            transaction.add(containerResId, fragment);
        }

        transaction.show(fragment).commit();//显示并提交
    }
}
