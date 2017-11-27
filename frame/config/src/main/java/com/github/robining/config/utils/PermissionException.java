package com.github.robining.config.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * 功能描述:当获取权限失败时抛出的异常
 * Created by LuoHaifeng on 2017/5/23.
 * Email:496349136@qq.com
 */

public class PermissionException extends Exception {
    public PermissionException() {
    }

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public PermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
