package com.github.robining.config.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017\11\13 0013.
 */

public class MiddleLineTextView extends AppCompatTextView {
    public MiddleLineTextView(Context context) {
        super(context);
    }

    public MiddleLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiddleLineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        super.onDraw(canvas);
    }
}
