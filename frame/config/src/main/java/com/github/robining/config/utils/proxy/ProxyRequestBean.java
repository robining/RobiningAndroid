package com.github.robining.config.utils.proxy;

import java.io.Serializable;

import io.reactivex.ObservableEmitter;

/**
 * 功能描述:请求类
 * Created by LuoHaifeng on 2017/5/31.
 * Email:496349136@qq.com
 */

public class ProxyRequestBean implements Serializable {
    private int requestCode;//请求码
    private ObservableEmitter<ProxyResultBean> emitter;//请求结果发射器

    public int getRequestCode() {
        return requestCode;
    }

    public ProxyRequestBean setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public ObservableEmitter<ProxyResultBean> getEmitter() {
        return emitter;
    }

    public ProxyRequestBean setEmitter(ObservableEmitter<ProxyResultBean> emitter) {
        this.emitter = emitter;
        return this;
    }
}
