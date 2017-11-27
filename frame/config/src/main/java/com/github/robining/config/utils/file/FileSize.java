package com.github.robining.config.utils.file;

import java.io.Serializable;

/**
 * 功能描述:文件大小实体
 * Created by LuoHaifeng on 2017/5/24.
 * Email:496349136@qq.com
 */

public class FileSize implements Serializable {
    private double size;//文件大小
    private FileSizeUnit unit;//文件单位

    public double getSize() {
        return size;
    }

    public FileSize setSize(double size) {
        this.size = size;
        return this;
    }

    public FileSizeUnit getUnit() {
        return unit;
    }

    public FileSize setUnit(FileSizeUnit unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public String toString() {
        return size + "" + unit.getDesc();
    }
}
