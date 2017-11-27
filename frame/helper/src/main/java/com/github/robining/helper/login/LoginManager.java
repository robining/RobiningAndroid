package com.github.robining.helper.login;

import com.github.robining.config.utils.SPUtils;
import com.github.robining.helper.login.event.LoginEvent;
import com.github.robining.helper.login.event.UserChangedEvent;
import com.github.robining.helper.login.event.LogoutEvent;
import com.github.robining.helper.login.event.UpdateUserProfileEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/7/6.
 * Email:496349136@qq.com
 */

public class LoginManager implements ILoginManager {
    private static final String LAST_LOGIN_USER = "LAST_LOGIN_USER";//保存登录用户名的key
    private static final String LAST_LOGIN_RESULT = "LAST_LOGIN_RESULT";//保存登录结果的key
    private SPUtils spUtils;//SharedPreferences工具类

    private LoginManager() {
        spUtils = SPUtils.getInstance();
    }

    public synchronized static LoginManager getInstance() {
        return new LoginManager();
    }

    @Override
    public void saveLastLoginUserName(String username) {
        spUtils.put(LAST_LOGIN_USER, username);
    }

    @Override
    public String getLastLoginUserName() {
        return spUtils.getString(LAST_LOGIN_USER);
    }

    @Override
    public void saveLastLoginResult(Serializable obj) {
        spUtils.put(LAST_LOGIN_RESULT, obj);
    }

    @Override
    public void signIn(Serializable loginResult) {
        saveLastLoginResult(loginResult);
        EventBus.getDefault().post(new LoginEvent());
        EventBus.getDefault().post(new UserChangedEvent());
    }

    @Override
    public void updateProfile(Serializable newUserInfo) {
        saveLastLoginResult(newUserInfo);
        EventBus.getDefault().post(new UpdateUserProfileEvent());
        EventBus.getDefault().post(new UserChangedEvent());
    }

    @Override
    public void signOff() {
        saveLastLoginResult(null);
        EventBus.getDefault().post(new LogoutEvent());
        EventBus.getDefault().post(new UserChangedEvent());
    }

    @Override
    public <T> T getLastLoginResult() {
        return spUtils.getSerializable(LAST_LOGIN_RESULT, null);
    }
}
