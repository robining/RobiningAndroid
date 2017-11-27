package com.github.robining.helper.version;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.github.robining.helper.version.event.FoundNewVersionEvent;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 功能描述:版本更新服务
 * Created by LuoHaifeng on 2017/8/11.
 * Email:496349136@qq.com
 */

public class VersionManageService extends IntentService {
    public VersionManageService() {
        super("VersionManageService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (AppVersionManager.getInstance().getQueryVersionInfoObservableProvider() != null) {
            //检测版本更新
            Observable<IVersionEntity> observable = AppVersionManager.getInstance().getQueryVersionInfoObservableProvider().provideVersionInfoObservable();
            if (observable != null) {
                observable.subscribe(new Consumer<IVersionEntity>() {
                    @Override
                    public void accept(@NonNull IVersionEntity updateEntity) throws Exception {
                        //保存版本信息
                        VersionUpdateHelper.setLastVersionInfo(updateEntity);
                        //通知有更新
                        EventBus.getDefault().post(new FoundNewVersionEvent(updateEntity));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
            }
        }
    }
}
