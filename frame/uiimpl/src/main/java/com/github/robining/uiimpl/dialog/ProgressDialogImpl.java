package com.github.robining.uiimpl.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.robining.config.interfaces.ui.dialog.ProgressDialog;
import com.github.robining.uiimpl.R;

import java.math.BigDecimal;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/7/18.
 * Email:496349136@qq.com
 */

public class ProgressDialogImpl extends ProgressDialog {
    private TextView titleView;
    private TextView progressTextView;
    private ProgressBar progressBar;

    public ProgressDialogImpl(Context context) {
        super(context, R.style.ThemeDialog);
        setContentView(R.layout.dialog_progress);
        titleView = (TextView) findViewById(R.id.textTitle);
        progressTextView = (TextView) findViewById(R.id.tv_progress);
        progressBar = (ProgressBar) findViewById(R.id.pb_progress_bar);
    }


    @Override
    public void setTitle(CharSequence title) {
        titleView.setText(title);
        titleView.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setProgress(int progress) {
        progressBar.setProgress(progress);
        updateProgress();
    }

    @Override
    public void setMax(int max) {
        progressBar.setMax(max);
        updateProgress();
    }

    private void updateProgress() {
        if (progressBar.getMax() == 0) {
            progressBar.setIndeterminate(true);
        } else {
            progressBar.setIndeterminate(false);
            float percent = progressBar.getProgress() / (float) progressBar.getMax();
            BigDecimal bigDecimal = new BigDecimal(percent).multiply(new BigDecimal(100));
            progressTextView.setText(bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP) + "%");
        }
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
