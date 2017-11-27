package com.github.robining.config.other;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2017\11\15 0015.
 */

public abstract class BaseRecyclerViewAdapter<K, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    private List<K> data = new ArrayList<>();

    public List<K> getData() {
        return data;
    }

    public void addAll(Collection<K> datas) {
        data.addAll(datas);
    }

    public BaseRecyclerViewAdapter setData(List<K> data) {
        this.data = data;
        return this;
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }
}
