package com.github.robining.config.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.github.robining.config.utils.ResizeDrawableUtil;

/**
 * Created by Administrator on 2017\11\10 0010.
 */

public class ResizeDrawableTextView extends AppCompatTextView {
    private int[][] drawableSize;

    public ResizeDrawableTextView(Context context) {
        super(context);
        init(null);
    }

    public ResizeDrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ResizeDrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
