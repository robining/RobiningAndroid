package com.github.robining.config.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

import com.github.robining.config.utils.ResizeDrawableUtil;

/**
 * Created by Administrator on 2017\11\9 0009.
 */

public class ResizeDrawableRadioButton extends AppCompatRadioButton {
    private int[][] drawableSize;

    public ResizeDrawableRadioButton(Context context) {
        super(context);
        init(null);
    }

    public ResizeDrawableRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ResizeDrawableRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        drawableSize = ResizeDrawableUtil.getValues(getContext(), attributeSet);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ResizeDrawableUtil.resizeDrawableSize(this, drawableSize);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
