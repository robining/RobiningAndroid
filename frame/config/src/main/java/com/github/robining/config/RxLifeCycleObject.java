package com.github.robining.config;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.os.Bundle;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by fzroa on 2017/10/10.
 */

public class RxLifeCycleObject {
    private HashMap<Activity,BehaviorSubject<ActivityEvent>> activityBehaviorSubjectHashMap = new HashMap<>();
    public RxLifeCycleObject(final Application application) {
        application.registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks(){
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                super.onActivityCreated(activity, bundle);
                BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
            }
        });
    }
}
