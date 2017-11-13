package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.bean.SendPraiseBean;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by ningl on 2016/6/30.
 */
public class SendPraiseAdapter extends BaseAdapter {
    private Context context;
    private List<SendPraiseBean> page_result;

    public SendPraiseAdapter(Context context, List<SendPraiseBean> page_result) {
        this.context = context;
        this.page_result = page_result;
    }

    @Override
    public int getCount() {
        return page_result.size();
    }

    @Override
    public Object getItem(int position) {
        return page_result.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_senpraise, null);
            holder.iv_itempraise_header = (ImageView) convertView.findViewById(R.id.iv_itempraise_header);
            holder.tv_itempraise_name = (TextView) convertView.findViewById(R.id.tv_itempraise_name);
            holder.tv_itempraise_time = (TextView) convertView.findViewById(R.id.tv_itempraise_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SendPraiseBean sendPraiseBean = page_result.get(position);
        ImageLoader.getInstance().displayImage(sendPraiseBean.getVideo_cover_uri(), holder.iv_itempraise_header, DisplayOptionsUtils.getCornerImageOptions(DensityUtil.dip2px(context, 5)));
        holder.tv_itempraise_name.setText(sendPraiseBean.getUser_name());
        holder.tv_itempraise_time.setText(sendPraiseBean.getSend_time_text());
        holder.tv_itempraise_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PersonalHomeActivity.class);
                intent.putExtra("userId", sendPraiseBean.getUser_id());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView iv_itempraise_header;
        TextView tv_itempraise_time;
        TextView tv_itempraise_name;
    }
}
