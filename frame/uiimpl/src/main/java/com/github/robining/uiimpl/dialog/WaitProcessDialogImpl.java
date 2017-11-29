package com.github.robining.uiimpl.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.github.robining.config.interfaces.ui.dialog.WaitProcessDialog;
import com.github.robining.uiimpl.R;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/6/28.
 * Email:496349136@qq.com
 */

public class WaitProcessDialogImpl extends WaitProcessDialog {
    private AppCompatTextView viewMessage;

    public WaitProcessDialogImpl(@NonNull Context context) {
        super(context);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.dialog_wait_process);
        viewMessage = (AppCompatTextView) findViewById(R.id.tv_message);
        if (viewMessage != null) {
            viewMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void setMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            viewMessage.setVisibility(View.GONE);
        } else {
            viewMessage.setText(message);
            viewMessage.setVisibility(View.VISIBLE);
        }
    }
}
