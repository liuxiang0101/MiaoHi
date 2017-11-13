package com.haiqiu.miaohi.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.ItemInfo;

import java.util.List;

/**
 * Created by zhandalin on 2017-02-10 18:25.
 * 说明:
 */
public class TestRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final static int TEXT = 23;
    public final static int IMAGE = 24;
    private final List<ItemInfo> list;


    public TestRecyclerAdapter(List<ItemInfo> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case IMAGE:
                View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false);
                return new ImageViewHolder(inflate);
            case TEXT:
                TextView textView = new TextView(parent.getContext());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.RED);
                textView.setTextSize(30);
                textView.setBackgroundColor(Color.BLUE);
                return new ViewHolder(textView);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case IMAGE:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                ItemInfo itemInfo = list.get(position);
                imageViewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
                imageViewHolder.tv_content.setText(itemInfo.getName() + "--position=" + position);
                imageViewHolder.position = position;
                break;
            case TEXT:
                ViewHolder viewHolder = (ViewHolder) holder;
                ItemInfo item = list.get(position);
                AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, item.getHeight());
                viewHolder.textView.setLayoutParams(layoutParams);
                viewHolder.textView.setAlpha(1);
                viewHolder.textView.setTranslationY(0);
                viewHolder.textView.setText(item.getName());
                viewHolder.position = position;
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private int position;
        private View rootView;

        ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            this.textView = (TextView) itemView;
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private int position;
        private TextView tv_content;
        private View rootView;

        ImageViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            this.imageView = (ImageView) itemView.findViewById(R.id.image_view);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    public void remove(int position) {
        this.list.remove(position);
        notifyItemRemoved(position);
        if (position != (list.size())) { // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, this.list.size() - position);
        }
    }

}