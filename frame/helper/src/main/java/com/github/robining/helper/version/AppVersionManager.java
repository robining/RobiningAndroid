package com.github.robining.helper.version;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.github.robining.config.Config;
import com.github.robining.config.SimpleActivityLifecycleCallbacks;
import com.github.robining.helper.version.event.FoundNewVersionEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/26.
 * Email:496349136@qq.com
 */

public class AppVersionManager {
    public static AppVersionManager appVersionManager = new AppVersionManager();
    private QueryVersionInfoObservableProvider queryVersionInfoObservableProvider;
    private IVersionEntity lastVersionEntity;
    private SimpleActivityLifecycleCallbacks activityLifecycleCallbacks = new SimpleActivityLifecycleCallbacks() {
        @Override
        public void onActivityResumed(Activity activity) {
            super.onActivityResumed(activity);
            if (lastVersionEntity != null) {
                //更新
                IVersionEntity entity = lastVersionEntity;
                lastVersionEntity = null;
                showUpdateInfo(activity, entity);
            }
        }
    };

    private AppVersionManager() {
    }

    public static AppVersionManager getInstance() {
        return appVersionManager;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFoundVersionInfo(FoundNewVersionEvent event) {
        appVersionManager.onFoundVersionInfo(event.getUpdateEntity());
    }

    private AppVersionManager onFoundVersionInfo(IVersionEntity entity) {
        Activity topActivity = Config.getInstance().getTopActivity();
        if (topActivity != null) {
            appVersionManager.showUpdateInfo(topActivity, entity);
        } else {
            appVersionManager.waitForUpdate(entity);
        }

        return this;
    }

    public AppVersionManager init(QueryVersionInfoObservableProvider provider) {
        this.queryVersionInfoObservableProvider = provider;
        return this;
    }

    public AppVersionManager start(){
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        ((Application) (Config.getInstance().provideContext())).startService(new Intent(Config.getInstance().provideContext(), VersionManageService.class));
        return this;
    }

    private AppVersionManager waitForUpdate(IVersionEntity versionEntity) {
        this.lastVersionEntity = versionEntity;
        ((Application) (Config.getInstance().provideContext())).registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        return this;
    }

    private AppVersionManager showUpdateInfo(Activity activity, IVersionEntity versionEntity) {
        ((Application) (Config.getInstance().provideContext())).unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        VersionUpdateHelper.update(activity, null, versionEntity);
        return this;
    }

    public QueryVersionInfoObservableProvider getQueryVersionInfoObservableProvider() {
        return queryVersionInfoObservableProvider;
    }
}
