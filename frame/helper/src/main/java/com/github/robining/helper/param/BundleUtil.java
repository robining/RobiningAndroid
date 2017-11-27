package com.github.robining.helper.param;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;

import java.io.Serializable;

/**
 * Bundle 存放Object类型,让其自动选择存放方式
 * Created by LuoHaifeng on 2017/3/8.
 */

public class BundleUtil {
    /***
     * 从指定的Bundle对象中取值,并自动匹配类型
     * @param key key
     * @param from 从什么地方取值
     * @param <T> 目标类型
     * @return Bundle中与key对应的值
     * @throws Exception 取值异常
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValueFromBundle(String key, Bundle from) throws Exception {
        try {
            return (T) from.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage() + " at " + key, e.getCause());
        }
    }

    /**
     * 将指定值存放到Bundle对象中
     *
     * @param key    存放的key
     * @param value  存放的值
     * @param target 存放的目标Bundle
     * @throws Exception 存放过程中的异常,例如不支持指定类型的存放
     */
    public static void putObjectToBundle(String key, Object value, Bundle target) throws Exception {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (value instanceof Size) {
                target.putSize(key, (Size) value);
                return;
            } else if (value instanceof SizeF) {
                target.putSizeF(key, (SizeF) value);
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (value instanceof IBinder) {
                target.putBinder(key, (IBinder) value);
                return;
            }
        }

        if (value == null) {
            if (target.containsKey(key)) {
                target.remove(key);
            }
            return;
        }

        if (value instanceof Byte) {
            target.putByte(key, (Byte) value);
            return;
        } else if (value instanceof Character) {
            target.putChar(key, (Character) value);
            return;
        } else if (value instanceof Short) {
            target.putShort(key, (Short) value);
            return;
        } else if (value instanceof Float) {
            target.putFloat(key, (Float) value);
            return;
        } else if (value instanceof CharSequence) {
            target.putCharSequence(key, (CharSequence) value);
            return;
        } else if (value instanceof Bundle) {
            target.putBundle(key, (Bundle) value);
            return;
        } else if (value instanceof Parcelable) {
            target.putParcelable(key, (Parcelable) value);
            return;
        } else if (value instanceof Parcelable[]) {
            target.putParcelableArray(key, (Parcelable[]) value);
            return;
        } else if (value instanceof byte[]) {
            target.putByteArray(key, (byte[]) value);
            return;
        } else if (value instanceof short[]) {
            target.putShortArray(key, (short[]) value);
            return;
        } else if (value instanceof char[]) {
            target.putCharArray(key, (char[]) value);
            return;
        } else if (value instanceof float[]) {
            target.putFloatArray(key, (float[]) value);
            return;
        } else if (value instanceof CharSequence[]) {
            target.putCharSequenceArray(key, (CharSequence[]) value);
            return;
        } else if (value instanceof Serializable) {
            target.putSerializable(key, (Serializable) value);
            return;
        }

        throw new Exception("not support this type:" + key + "=" + value);
    }

    /**
     * 将指定值存放到Bundle对象中(对null值的支持性更好)
     * @param key    存放的key
     * @param value  存放的值
     * @param target 存放的目标Bundle
     * @throws Exception 存放过程中的异常,例如不支持指定类型的存放
     *
     * 目前还未完善
     */
    @Deprecated
    public static void putObjectToBundleV2(String key, Object value, Bundle target, Class<?> type) throws Exception {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (type.isAssignableFrom(Size.class)) {
                target.putSize(key, (Size) value);
                return;
            } else if (type.isAssignableFrom(SizeF.class)) {
                target.putSizeF(key, (SizeF) value);
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (type.isAssignableFrom(IBinder.class)) {
                target.putBinder(key, (IBinder) value);
                return;
            }
        }

        if (value == null) {
            if (target.containsKey(key)) {
                target.remove(key);
            }
            return;
        }

        if (type.isAssignableFrom(Byte.class)) {
            target.putByte(key, (Byte) value);
            return;
        } else if (type.isAssignableFrom(Character.class)) {
            target.putChar(key, (Character) value);
            return;
        } else if (type.isAssignableFrom(Short.class)) {
            target.putShort(key, (Short) value);
            return;
        } else if (type.isAssignableFrom(Float.class)) {
            target.putFloat(key, (Float) value);
            return;
        } else if (type.isAssignableFrom(CharSequence.class)) {
            target.putCharSequence(key, (CharSequence) value);
            return;
        } else if (type.isAssignableFrom(Bundle.class)) {
            target.putBundle(key, (Bundle) value);
            return;
        } else if (type.isAssignableFrom(Parcelable.class)) {
            target.putParcelable(key, (Parcelable) value);
            return;
        } else if (type.isAssignableFrom(Parcelable[].class)) {
            target.putParcelableArray(key, (Parcelable[]) value);
            return;
        } else if (type.isAssignableFrom(byte[].class)) {
            target.putByteArray(key, (byte[]) value);
            return;
        } else if (type.isAssignableFrom(short[].class)) {
            target.putShortArray(key, (short[]) value);
            return;
        } else if (type.isAssignableFrom(char[].class)) {
            target.putCharArray(key, (char[]) value);
            return;
        } else if (type.isAssignableFrom(float[].class)) {
            target.putFloatArray(key, (float[]) value);
            return;
        } else if (type.isAssignableFrom(CharSequence[].class)) {
            target.putCharSequenceArray(key, (CharSequence[]) value);
            return;
        } else if (type.isAssignableFrom(Serializable.class)) {
            target.putSerializable(key, (Serializable) value);
            return;
        }
        throw new Exception("not support this type:" + key + "=" + value + "  the type name is:" + type);
    }
}
