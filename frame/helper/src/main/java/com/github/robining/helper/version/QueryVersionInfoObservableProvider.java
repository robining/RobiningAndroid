package com.github.robining.helper.version;

import io.reactivex.Observable;

/**
 * 功能描述:检测版本更新请求提供器
 * Created by LuoHaifeng on 2017/8/14.
 * Email:496349136@qq.com
 */

public interface QueryVersionInfoObservableProvider {
    Observable<IVersionEntity> provideVersionInfoObservable();
}
