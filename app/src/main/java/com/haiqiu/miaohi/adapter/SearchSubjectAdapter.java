package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.SearchAllPageResult;
import com.haiqiu.miaohi.utils.DensityUtil;

import java.util.List;

/**
 * Created by zhandalin on 2016-06-23 17:40.
 * 说明:录制视频,参加专题专用
 */
public class SearchSubjectAdapter extends BaseAdapter {

    private Context context;
    private List<SearchAllPageResult> dataList;

    public SearchSubjectAdapter(Context context, List<SearchAllPageResult> page_result) {
        this.context = context;
        this.dataList = page_result;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (null == convertView) {
            textView = new TextView(context);
            textView.setHeight(DensityUtil.dip2px(context, 60));
            textView.setPadding(DensityUtil.dip2px(context, 10), 0, 0, 0);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setBackgroundResource(R.drawable.common_enter_selector);
            textView.setTextColor(context.getResources().getColor(R.color.color_666));
            convertView = textView;
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }
        SearchAllPageResult pageResult = dataList.get(position);
        textView.setText("#" + pageResult.getActivity_name() + "#");
        return convertView;
    }


}
