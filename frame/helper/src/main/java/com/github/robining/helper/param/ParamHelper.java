package com.github.robining.helper.param;

import android.os.Bundle;

import java.lang.reflect.Field;

/**
 * 主要做自动取值和自动存储
 * 在onCreate和onSaveInstance处调用
 * Created by LuoHaifeng on 2017/3/8.
 */

public class ParamHelper {
    /***
     * 获取指定target的所有字段,如果有@SaveState注解的,那么自动为其取值
     * @param target 需要注入的对象
     * @param froms 提供内容的Bundle列表
     */
    public static void injectValue(Object target, Bundle... froms) {
        try {
            Field[] fields = target.getClass().getDeclaredFields();
            for (Field field : fields) {
                Param param = field.getAnnotation(Param.class);
                if (param != null) {
                    field.setAccessible(true);
                    String key = param.key();
                    Object value = getValueFromBundles(key, field.get(target), froms);
                    field.set(target, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 将指定对象的所有包含@SaveState注解的字段,进行存储到指定Bundle容器
     * @param target 需要解析的对象
     * @param outState 存储容器
     */
    public static void saveValues(Object target, Bundle outState) {
        try {
            Field[] fields = target.getClass().getDeclaredFields();
            for (Field field : fields) {
                Param param = field.getAnnotation(Param.class);
                if (param != null) {
                    field.setAccessible(true);
                    String key = param.key();
                    Object value = field.get(target);
                    BundleUtil.putObjectToBundle(key, value, outState);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 按照列表顺序依次查找key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValueFromBundles(String key, T defaultValue, Bundle... froms) {
        Object value = null;
        for (Bundle from : froms) {
            if (from != null && from.containsKey(key)) {
                value = from.get(key);
                break;
            }
        }

        if (value != null) {
            return (T) value;
        }
        return defaultValue;
    }
}
