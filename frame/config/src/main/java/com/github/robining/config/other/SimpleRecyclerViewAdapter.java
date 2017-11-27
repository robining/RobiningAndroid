package com.github.robining.config.other;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017\11\15 0015.
 */

public abstract class SimpleRecyclerViewAdapter<K> extends BaseRecyclerViewAdapter<K, ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent.getContext(), provideItemView(parent, viewType));
    }

    protected abstract View provideItemView(ViewGroup parent, int viewType);
}
