package com.github.robining.config.interfaces.ui.dialog;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/8/1.
 * Email:496349136@qq.com
 */

public class AbstractOnClickListener {
    public void onCancelListener(BaseDialog dialog) {
        dialog.dismiss();
    }

    public void onConfirmListener(BaseDialog dialog) {

    }
}
