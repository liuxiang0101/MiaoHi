package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.widget.OnItemClickListener;

/**
 * Created by zhandalin on 2016-12-05 16:45.
 * 说明:分镜表示Adapter
 */
public class StoryboardAdapter extends RecyclerView.Adapter<StoryboardAdapter.ViewHolder> {

    private Context context;
    private int storyboardNum;
    private OnItemClickListener onItemClickListener;
    private int lastSelectPosition;

    public StoryboardAdapter(Context context, int storyboardNum) {
        this.context = context;
        if (storyboardNum == 0) storyboardNum = 1;
        this.storyboardNum = storyboardNum;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, R.layout.item_storyboard, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setSelected(position == lastSelectPosition);
        holder.tv_index.setText("L" + (position + 1));
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return storyboardNum;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_index;
        private int position;
        private View itemView;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_index = (TextView) itemView.findViewById(R.id.tv_index);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastSelectPosition == position) return;

                    lastSelectPosition = position;
                    notifyDataSetChanged();
                    if (null != onItemClickListener) onItemClickListener.OnItemClick(v, position);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
