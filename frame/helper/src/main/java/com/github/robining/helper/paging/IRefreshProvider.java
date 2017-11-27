package com.github.robining.helper.paging;

import com.github.robining.config.interfaces.ui.refresh.IRefreshLayout;

/**
 * 功能描述:上下拉内容提供接口
 * Created by LuoHaifeng on 2017/4/21.
 * Email:496349136@qq.com
 */

public interface IRefreshProvider extends ILoadingProvider {
    /**
     * 提供刷新布局
     */
    IRefreshLayout provideRefreshLayout();
}
