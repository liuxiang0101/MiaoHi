package com.haiqiu.miaohi.widget;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by zhandalin on 2017-02-16 15:31.
 * 说明:
 */
public interface HeaderAdapterInterface {
    RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int position);

    void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position);

    int getHeaderId(int position);
}
