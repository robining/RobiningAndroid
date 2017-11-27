package com.github.robining.uiimpl.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.robining.config.interfaces.ui.dialog.AlertDialog;
import com.github.robining.uiimpl.R;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/7/18.
 * Email:496349136@qq.com
 */

public class AlterDialogImpl extends AlertDialog {
    private TextView titleView;
    private TextView contentView;
    private TextView buttonNegativeView;
    private TextView buttonPositiveView;

    public AlterDialogImpl(Context context) {
        super(context, R.style.ThemeDialog);
        setContentView(R.layout.dialog_alert);
        titleView = (TextView) findViewById(R.id.textTitle);
        contentView = (TextView) findViewById(R.id.textContent);
        buttonNegativeView = (TextView) findViewById(R.id.buttonNegative);
        buttonPositiveView = (TextView) findViewById(R.id.buttonPositive);
    }


    @Override
    public void setTitle(CharSequence title) {
        titleView.setText(title);
        titleView.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setContent(CharSequence content) {
        contentView.setText(content);
        contentView.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setPositiveButton(CharSequence buttonText, View.OnClickListener onClickListener) {
        buttonPositiveView.setVisibility(TextUtils.isEmpty(buttonText) ? View.GONE : View.VISIBLE);
        buttonPositiveView.setText(buttonText);
        buttonPositiveView.setOnClickListener(onClickListener);
    }

    @Override
    public void setNegativeButton(CharSequence buttonText, View.OnClickListener onClickListener) {
        buttonNegativeView.setVisibility(TextUtils.isEmpty(buttonText) ? View.GONE : View.VISIBLE);
        buttonNegativeView.setText(buttonText);
        buttonNegativeView.setOnClickListener(onClickListener);
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);
    }
}
