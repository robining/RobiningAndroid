package com.github.robining.helper.version.event;;

import com.github.robining.helper.version.IVersionEntity;

import java.io.Serializable;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/8/11.
 * Email:496349136@qq.com
 */

public class FoundNewVersionEvent implements Serializable {
    private IVersionEntity updateEntity;

    public FoundNewVersionEvent(IVersionEntity updateEntity) {
        this.updateEntity = updateEntity;
    }

    public IVersionEntity getUpdateEntity() {
        return updateEntity;
    }

    public FoundNewVersionEvent setUpdateEntity(IVersionEntity updateEntity) {
        this.updateEntity = updateEntity;
        return this;
    }
}
