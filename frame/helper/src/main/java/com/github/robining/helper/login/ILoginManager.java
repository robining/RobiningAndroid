package com.github.robining.helper.login;

import java.io.Serializable;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/7/6.
 * Email:496349136@qq.com
 */

public interface ILoginManager {
    /**
     * 保存最后一次登录的用户名
     *
     * @param username 用户名
     */
    void saveLastLoginUserName(String username);

    /**
     * @return 获取最后一次登录的用户名
     */
    String getLastLoginUserName();

    /**
     * 保存最后一次登录的结果
     *
     * @param obj 登录结果
     */
    void saveLastLoginResult(Serializable obj);

    /**
     * 登录
     * @param loginResult
     */
    void signIn(Serializable loginResult);

    /**
     * 更新用户信息
     * @param newUserInfo 新用户信息
     */
    void updateProfile(Serializable newUserInfo);

    /**
     * 登出
     */
    void signOff();

    /**
     * 获取最后一次登录的结果
     *
     * @param <T> 登录结果的类型
     * @return 登录结果
     */
    <T> T getLastLoginResult();
}
