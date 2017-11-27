package com.github.robining.config.utils.file;

/**
 * 文件单位
 */
public enum FileSizeUnit {
    UNIT_BYTE("B"),
    UNIT_K_BYTE("KB"),
    UNIT_M_BYTE("MB"),
    UNIT_G_BYTE("GB");

    private String desc;//单位描述

    FileSizeUnit(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}