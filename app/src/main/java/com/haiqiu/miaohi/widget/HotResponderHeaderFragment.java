package com.haiqiu.miaohi.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.SportsListActivity;
import com.haiqiu.miaohi.base.BaseFragment;

/**
 * Created by LiuXiang on 2016/12/7.
 */
public class HotResponderHeaderFragment extends BaseFragment {
    GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        gridView = new GridView(context);
        gridView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        gridView.setNumColumns(7);
        gridView.setVerticalScrollBarEnabled(false);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(context, SportsListActivity.class));
            }
        });
        initGv();
        return gridView;
    }

    private void initGv() {
        gridView.setAdapter(new GvAdapter());
    }

    class GvAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 7;

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(context, R.layout.item_hot_responder_header, null);
            return convertView;
        }
    }
}
