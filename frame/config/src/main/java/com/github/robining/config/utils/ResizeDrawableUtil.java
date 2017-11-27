package com.github.robining.config.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.github.robining.config.R;

/**
 * Created by Administrator on 2017\11\9 0009.
 */

public class ResizeDrawableUtil {
    /**
     * 改变DrawableSize
     *
     * @param textView 需要改变Drawable大小的控件
     * @param sizes    大小 int[][]:[0][0]代表left的宽,[0][1]代表left的高;[1][0]代表top的宽,[1][1]代表top的高...
     */
    public static void resizeDrawableSize(TextView textView, int[][] sizes) {
        Drawable[] drawables = textView.getCompoundDrawables();
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 23, textView.getResources().getDisplayMetrics());
        for (int i = 0; i < drawables.length; i++) {
            if (drawables[i] != null) {
                int width = sizes[i][0];
                int height = sizes[i][1];
                if (width != -1 && height != -1) {
                    drawables[i].setBounds(0, 0, width, height);
                }
            }
        }

        textView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    /**
     * 通过自定义属性获取四个方向的drawable宽高
     *
     * @param context
     * @param attrs
     * @return
     */
    public static int[][] getValues(Context context, AttributeSet attrs) {
        int[][] values = new int[4][2];
        if (context == null || attrs == null) {
            initArray(values, -1);
            return values;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ResizeDrawableAttrs);
        //drawableSize
        int drawableSize = typedArray.getDimensionPixelSize(R.styleable.ResizeDrawableAttrs_drawableSize, -1);
        initArray(values, drawableSize);

        //drawable-direction-size
        int drawableLeftSize = typedArray.getDimensionPixelSize(R.styleable.ResizeDrawableAttrs_drawableLeftSize, values[0][0]);
        int drawableTopSize = typedArray.getDimensionPixelSize(R.styleable.ResizeDrawableAttrs_drawableTopSize, values[1][0]);
        int drawableRightSize = typedArray.getDimensionPixelSize(R.styleable.ResizeDrawableAttrs_drawableRightSize, values[2][0]);
        int drawableBottomSize = typedArray.getDimensionPixelSize(R.styleable.ResizeDrawableAttrs_drawableBottomSize, values[3][0]);

        values[0][0] = values[0][1] = drawableLeftSize;
        values[1][0] = values[1][1] = drawableTopSize;
        values[2][0] = values[2][1] = drawableRightSize;
        values[3][0] = values[3][1] = drawableBottomSize;

        //drawable w,h size
        int drawableLeftWidth = typedArray.getDimensionPixelSize(R.styleable.ResizeDrawableAttrs_drawableLeftWidth, values[0][0]);
        int drawableLeftHeight = typedArray.getDimensionPixelSize(R.styleable.ResizeDrawableAttrs_drawableLeftHeight, values[0][1]);
        int drawableTopWidth = typedArray.getDimensionPixelSize(R.styleable.ResizeDrawableAttrs_drawableTopWidth, values[1][0]);
        int drawableTopHeight = typedArray.getDimensionPixelSize(R.styleable.ResizeDrawableAttrs_drawableTopHeight, values[1][1]);
        int drawableRightWidth = typedArray.getDimensionPixelSize(R.styleable.ResizeDrawableAttrs_drawableRightWidth, values[2][0]);
        int drawableRightHeight = typedArray.getDimensionPixelSize(R.styleable.ResizeDrawableAttrs_drawableRightHeight, values[2][1]);
        int drawableBottomWidth = typedArray.getDimensionPixelSize(R.styleable.ResizeDrawableAttrs_drawableBottomWidth, values[3][0]);
        int drawableBottomHeight = typedArray.getDimensionPixelSize(R.styleable.ResizeDrawableAttrs_drawableBottomHeight, values[3][1]);
        values[0][0] = drawableLeftWidth;
        values[0][1] = drawableLeftHeight;
        values[1][0] = drawableTopWidth;
        values[1][1] = drawableTopHeight;
        values[2][0] = drawableRightWidth;
        values[2][1] = drawableRightHeight;
        values[3][0] = drawableBottomWidth;
        values[3][1] = drawableBottomHeight;

        typedArray.recycle();

        return values;
    }

    private static void initArray(int[][] target, int value) {
        for (int i = 0; i < target.length; i++) {
            for (int j = 0; j < target[i].length; j++) {
                target[i][j] = value;
            }
        }
    }
}
