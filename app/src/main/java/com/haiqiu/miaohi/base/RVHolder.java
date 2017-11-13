package com.haiqiu.miaohi.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RVHolder 通用写法
 *
 * @author Mikee
 */
public class RVHolder extends RecyclerView.ViewHolder {


    private ViewHolder viewHolder;

    public RVHolder(View itemView) {
        super(itemView);
        viewHolder = ViewHolder.getViewHolder(itemView);
    }


    public ViewHolder getViewHolder() {
        return viewHolder;
    }


    /**
     * eg:adapter
     */
/*    public class DemoRVAdapter extends AutoRVAdapter {
        public RecyclerAdapter(Context context, List<?> list) {
            super(context, list);
        }

        @Override
        public int onCreateViewLayoutID(int viewType) {
            return R.layout.item;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Entity item=(Entity) list.get(position);
            vh.getTextView(R.id.name).setText(item.getName());
            vh.getTextView(R.id.age).setText(item.getAge());
            vh.setText(R.id.height,item.getHeight());
        }
    }*/
}
