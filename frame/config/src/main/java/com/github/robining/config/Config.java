package com.github.robining.config;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.robining.config.interfaces.context.IApplicationContextProvider;
import com.github.robining.config.interfaces.ui.IUIProvider;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * 功能描述:全局配置类
 * Created by LuoHaifeng on 2017/9/20.
 * Email:496349136@qq.com
 */

public class Config implements IApplicationContextProvider {
    private WeakReference<Application> applicationWeakReference;
    private static Config instance = new Config();
    private IUIProvider uiProvider;
    private RunningActivityReference topActivity;//最顶部的Activity
    private Stack<RunningActivityReference> runningActivities = new Stack<>();//正在运行的Activity栈

    private Config() {
    }

    public static Config getInstance() {
        return instance;
    }

    public void init(Application application, IUIProvider uiProvider) {
        applicationWeakReference = new WeakReference<>(application);
        this.uiProvider = uiProvider;
        application.registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                super.onActivityCreated(activity, bundle);
                runningActivities.push(new RunningActivityReference(activity));
            }

            @Override
            public void onActivityResumed(Activity activity) {
                super.onActivityResumed(activity);
                //重新进栈 确保进栈顺序正确
                topActivity = new RunningActivityReference(activity);
                runningActivities.remove(topActivity);
                runningActivities.push(topActivity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                runningActivities.remove(new RunningActivityReference(activity));
            }
        });
    }

    /**
     * 获取App的上下文参数
     *
     * @return Application上下文
     */
    @Override
    public Context provideContext() {
        return applicationWeakReference != null ? applicationWeakReference.get() : null;
    }

    public IUIProvider getUiProvider() {
        return uiProvider;
    }

    public Stack<RunningActivityReference> getRunningActivities() {
        return runningActivities;
    }

    @Nullable
    public Activity getTopActivity() {
        return topActivity != null ? topActivity.get() : null;
    }

    public String getFileProviderAuthority() {
        return provideContext().getApplicationContext().getPackageName() + ".fileProvider";
    }
}
