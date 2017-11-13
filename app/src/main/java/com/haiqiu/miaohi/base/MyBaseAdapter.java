package com.haiqiu.miaohi.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的BaseAdapter进行数据封装
 *
 * @author Mikee
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> myList = new ArrayList<T>();

    public MyBaseAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public List<T> getAdapterData() {
        return myList;
    }

    // 封装加数据的方法--1条 多条
    public void appendData(T t, boolean isClearOld) {
        if (t == null)
            return;
        if (isClearOld)
            myList.clear();
        myList.add(t);
    }

    public void appendData(List<T> data, boolean isClearOld) {
        if (data == null)
            return;
        if (isClearOld)
            myList.clear();
        myList.addAll(data);
    }

    // 封装加数据的方法--1条 多条 在顶部添加
    public void appendDataTop(T t, boolean isClearOld) {
        if (t == null)
            return;
        if (isClearOld)
            myList.clear();
        myList.add(0, t);
    }

    public void appendDataTop(List<T> data, boolean isClearOld) {
        if (data == null)
            return;
        if (isClearOld)
            myList.clear();
        myList.addAll(0, data);
    }

    public void update() {
        this.notifyDataSetChanged();
    }

    public void clear() {
        myList.clear();
    }

    public int getCount() {
        if (myList == null)
            return 0;
        return myList.size();
    }

    public T getItem(int position) {
        if (myList == null)
            return null;
        if (position > myList.size())
            return null;
        return myList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return getMyView(position, convertView, parent);
    }

    public abstract View getMyView(int position, View convertView, ViewGroup parent);

}
