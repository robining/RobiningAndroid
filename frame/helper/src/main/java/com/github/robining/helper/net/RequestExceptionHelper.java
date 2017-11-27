package com.github.robining.helper.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ParseException;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.github.robining.config.Config;
import com.github.robining.config.interfaces.ui.loading.ILoadingHelper;
import com.github.robining.helper.R;
import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import retrofit2.HttpException;

/**
 * 功能描述:提供统一的错误处理内容
 * Created by LuoHaifeng on 2017/4/13.
 * Email:496349136@qq.com
 */

public class RequestExceptionHelper {
    private static HashMap<String, String> customExceptions = new HashMap<>();

    public static void unregisterCustomException(Class<? extends Throwable> exClass) {
        String key = exClass.getName();
        if (customExceptions.containsKey(key)) {
            customExceptions.remove(key);
        }
    }

    public static void registCustomException(Class<? extends Throwable> exClass, String msg) {
        String key = exClass.getName();
        customExceptions.put(key, msg);
    }

    public static ILoadingHelper.State getLayoutStateByThrowable(Throwable ex) {
        if (ex instanceof ConnectException || ex instanceof SocketTimeoutException || ex instanceof UnknownHostException || ex instanceof SocketException) {
            if (!isConnected(Config.getInstance().provideContext())) {
                //如果网络未链接显示
                return ILoadingHelper.State.NO_NETWORK;
            }
        }

        return ILoadingHelper.State.ERROR;
    }

    public static boolean isConnected(Context context) {
        boolean flag;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        flag = manager != null && manager.getActiveNetworkInfo() != null;
        return flag;
    }

    public static String getErrorMessage(Context context, Throwable ex, @StringRes int unknownErrorMsg) {
        if (ex == null) {
            return context.getResources().getString(unknownErrorMsg);
        }

        ex.printStackTrace();

        if (customExceptions.containsKey(ex.getClass().getName())) {
            String message = customExceptions.get(ex.getClass().getName());
            if (TextUtils.isEmpty(message)) {
                message = ex.getMessage();
            }
            return message;
        }

        if (ex instanceof HttpException || ex instanceof ConnectException || ex instanceof SocketTimeoutException || ex instanceof UnknownHostException || ex instanceof SocketException) {
            return context.getResources().getString(R.string.network_error);
        } else if (ex instanceof CodeException) {
            CodeException cex = (CodeException) ex;
            String msg = cex.getMsg();
            return msg == null ? context.getResources().getString(R.string.network_response_code_error_default) : msg;
        } else if (ex instanceof MalformedJsonException) {
            return context.getResources().getString(R.string.network_response_code_error_default);
        } else if (ex instanceof JsonParseException
                || ex instanceof JSONException
                || ex instanceof ParseException) {
            return context.getResources().getString(R.string.network_response_parse_error);
        } else {
            return context.getResources().getString(unknownErrorMsg);
        }
    }

    public static String getErrorMessage(Throwable ex, @StringRes int unknownErrorMsg) {
        return getErrorMessage(Config.getInstance().provideContext(), ex, unknownErrorMsg);
    }

    public static String getErrorMessage(Context context, Throwable ex) {
        return getErrorMessage(context, ex, R.string.network_unknown_error);
    }

    public static String getErrorMessage(Throwable ex) {
        return getErrorMessage(Config.getInstance().provideContext(), ex);
    }

    public static void tip(Throwable ex) {
        Config.getInstance().getUiProvider().applyToast().error(getErrorMessage(ex));
    }
}
