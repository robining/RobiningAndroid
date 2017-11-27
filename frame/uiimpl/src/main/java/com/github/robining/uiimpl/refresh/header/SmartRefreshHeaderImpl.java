package com.github.robining.uiimpl.refresh.header;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.github.robining.uiimpl.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/7/25.
 * Email:496349136@qq.com
 */

public class SmartRefreshHeaderImpl implements RefreshHeader {
    private ImageView ivImage;
    private Context context;
    private View view;

    public SmartRefreshHeaderImpl(Context context) {
        this.context = context;

        view = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header, null);
        ivImage = view.findViewById(R.id.iv_image);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
    }

    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {
    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {
    }

    @Override
    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {

    }

    @NonNull
    @Override
    public View getView() {
        return view;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Scale;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {

    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        return 0;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case PullDownToRefresh:
                ivImage.setImageResource(R.mipmap.pulling);
                break;
            case ReleaseToRefresh:
                ivImage.setImageResource(R.mipmap.release);
                break;
            case Refreshing:
                ivImage.setImageResource(R.mipmap.refreshing);
                break;
            case RefreshFinish:
            case PullDownCanceled:
                ivImage.setImageDrawable(null);
                break;
            default:
                break;
        }
    }
}
