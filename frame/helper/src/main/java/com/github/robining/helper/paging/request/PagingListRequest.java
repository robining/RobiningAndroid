package com.github.robining.helper.paging.request;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.github.robining.config.other.BaseRecyclerViewAdapter;
import com.github.robining.helper.paging.PagingHelper;
import com.github.robining.helper.paging.callback.IPagingListCallback;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017\11\15 0015.
 */

public class PagingListRequest<T, V extends RecyclerView.ViewHolder> extends AbstractPagingRequest<List<T>, IPagingListCallback<T, V>> {
    private IPagingListCallback<T, V> callback;
    private BaseRecyclerViewAdapter<T, V> adapter = new BaseRecyclerViewAdapter<T, V>() {
        @Override
        public V onCreateViewHolder(ViewGroup parent, int viewType) {
            return callback.provideViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(V holder, int position) {
            callback.onBindItemData(this, position, holder, getData().get(position));
        }

        @Override
        public int getItemViewType(int position) {
            return callback.provideViewType(position);
        }
    };

    public PagingListRequest(PagingHelper pagingHelper, IPagingListCallback<T, V> callback) {
        super(pagingHelper, callback);
        this.callback = callback;
        this.callback.bindAdapter(adapter);
    }

    @Override
    protected Observable<List<T>> getRealRequest(IPagingListCallback<T, V> callback, int pageIndex, int pageSize) {
        return callback.onRequestData(pageIndex, pageSize);
    }

    @Override
    protected void onBindData(PagingHelper pagingHelper, IPagingListCallback<T, V> callback, int pageIndex, int pageSize, List<T> data) {
        if (pageIndex == pagingHelper.getBuilder().getStartPagerNumber()) {
            adapter.getData().clear();
        }

        adapter.getData().addAll(data);
        adapter.notifyDataSetChanged();
    }
}
