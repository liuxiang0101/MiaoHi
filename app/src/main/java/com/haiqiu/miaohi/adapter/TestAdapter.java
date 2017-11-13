package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhandalin 2015年09月15日 20:14.
 * 说明:
 */
public class TestAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> list;

    public TestAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list.add("测试数据" + i);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView = new TextView(context);
        textView.setText(list.get(position));
        return textView;
    }

    public void addData(List<String> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }
}
