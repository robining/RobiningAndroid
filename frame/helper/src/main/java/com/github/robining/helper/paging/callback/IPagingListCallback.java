package com.github.robining.helper.paging.callback;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.github.robining.config.other.BaseRecyclerViewAdapter;

import java.util.List;

import io.reactivex.Observable;

public interface IPagingListCallback<T, V extends RecyclerView.ViewHolder> extends ICallback<List<T>> {
    void bindAdapter(BaseRecyclerViewAdapter<T, V> adapter);

    V provideViewHolder(ViewGroup parent, int viewType);

    int provideViewType(int position);

    void onBindItemData(BaseRecyclerViewAdapter<T, V> adapter, int position, V holder, T data);

    Observable<List<T>> onRequestData(int pageIndex, int pageSize);
}