package com.github.robining.helper.version;

import java.io.Serializable;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/26.
 * Email:496349136@qq.com
 */

public interface IVersionEntity extends Serializable {
    /**
     * @return 获取新版本版本号
     */
    int _getVersionCode_();
    /**
     * @return 获取新版本版本名称
     */
    String _getVersionName_();
    /**
     * @return 获取新版本更新内容
     */
    String _getUpdateContent_();
    /**
     * @return 是否强制更新
     */
    boolean _isForceUpdate_();
    /**
     * @return 新版本apk文件下载地址
     */
    String _getApkDownloadUrl_();
}
