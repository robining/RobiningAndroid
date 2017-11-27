package com.github.robining.helper.net.progress;

import okhttp3.HttpUrl;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/7/17.
 * Email:496349136@qq.com
 */

public class ProgressListenerHelper {
    public static String addListenerToUrl(String urlStr, String id) {
        HttpUrl url = HttpUrl.parse(urlStr);
        return url.newBuilder().addQueryParameter(ProgressInterceptor.LISTENER_ID_KEY, id)
                .build().toString();
    }
}
