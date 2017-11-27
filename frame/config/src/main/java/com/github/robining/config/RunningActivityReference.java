package com.github.robining.config;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by fzroa on 2017/10/10.
 */

public class RunningActivityReference extends WeakReference<Activity> {
    public RunningActivityReference(Activity referent) {
        super(referent);
    }

    @Override
    public boolean equals(Object obj) {
        return this.get() != null && obj instanceof WeakReference && ((WeakReference) obj).get() != null && this.get().equals(((WeakReference) obj).get());
    }
}
