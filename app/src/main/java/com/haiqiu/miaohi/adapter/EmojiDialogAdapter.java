package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * emojiDialog适配器
 * Created by ningl on 2016/7/5.
 */
public class EmojiDialogAdapter extends PagerAdapter {

    private Context context;
    private List<RecyclerView> recyclerViews;

    public EmojiDialogAdapter(Context context, List<RecyclerView> recyclerViews) {
        this.context = context;
        this.recyclerViews = recyclerViews;
    }

    @Override
    public int getCount() {
        return recyclerViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(recyclerViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(recyclerViews.get(position));
        return recyclerViews.get(position);
    }
}
