package com.github.robining.helper.net.cookie;

import com.github.robining.config.Config;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 自动管理Cookies
 */
public class CookieJarManager implements CookieJar {
    private static CookieJarManager cookieJarManager = new CookieJarManager();

    public static CookieJarManager getInstance() {
        return cookieJarManager;
    }

    private final PersistentCookieStore cookieStore = PersistentCookieStore.getInstance(Config.getInstance().provideContext());

    public PersistentCookieStore getCookieStore() {
        return cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }
}