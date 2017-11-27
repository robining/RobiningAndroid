package com.github.robining.config.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.github.robining.config.R;

/**
 * Created by Administrator on 2017\11\13 0013.
 */

public class RatioHeightImageView extends AppCompatImageView {
    private float ratio = Float.NaN;

    public RatioHeightImageView(Context context) {
        super(context);
        init(null);
    }

    public RatioHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RatioHeightImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.RatioAttrs);
        ratio = typedArray.getFloat(R.styleable.RatioAttrs_ratio, Float.NaN);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (ratio != Float.NaN) {
            setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth() * ratio));
        }
    }
}
