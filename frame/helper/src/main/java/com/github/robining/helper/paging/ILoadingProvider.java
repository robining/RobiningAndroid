package com.github.robining.helper.paging;

import com.github.robining.config.interfaces.ui.loading.ILoadingHelper;

/**
 * 功能描述:Loading
 * Created by LuoHaifeng on 2017/4/13.
 * Email:496349136@qq.com
 */

public interface ILoadingProvider {
    /**
     * @return 当前页面是否已有数据
     */
    boolean haveData();

    /***
     * @return 提供加载工具, 方便布局切换
     */
    ILoadingHelper provideLoadingHelper();
}
