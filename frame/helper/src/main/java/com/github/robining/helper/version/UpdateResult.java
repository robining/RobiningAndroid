package com.github.robining.helper.version;

import java.io.Serializable;

/**
 * 功能描述:版本更新操作结果
 * Created by LuoHaifeng on 2017/5/26.
 * Email:496349136@qq.com
 */

public class UpdateResult implements Serializable {
    public enum Result {
        RESULT_NOT_NEED_UPDATE,
        RESULT_CANCELED_UPDATE,
        RESULT_UPDATE_SUCCESS,
        RESULT_UPDATE_FAILED
    }

    private IVersionEntity updateEntity;
    private Result result;

    public IVersionEntity getUpdateEntity() {
        return updateEntity;
    }

    public UpdateResult setUpdateEntity(IVersionEntity updateEntity) {
        this.updateEntity = updateEntity;
        return this;
    }

    public Result getResult() {
        return result;
    }

    public UpdateResult setResult(Result result) {
        this.result = result;
        return this;
    }
}
