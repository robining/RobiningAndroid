package com.github.robining.config.utils.proxy;

import android.content.Intent;

import java.io.Serializable;

/**
 * 功能描述:请求结果类
 * Created by LuoHaifeng on 2017/5/31.
 * Email:496349136@qq.com
 */

public class ProxyResultBean implements Serializable {
    private int resultCode;//响应码
    private Intent data;//响应数据

    public int getResultCode() {
        return resultCode;
    }

    public ProxyResultBean setResultCode(int resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public Intent getData() {
        return data;
    }

    public ProxyResultBean setData(Intent data) {
        this.data = data;
        return this;
    }
}
