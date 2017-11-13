package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.activity.QAHomeActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.HotResponderPageResult;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by LiuXiang on 2016/12/6.
 */
public class HotResponderAdapter extends BaseAdapter {
    private List<HotResponderPageResult> list;
    private Context context;
    private ImageLoader imageLoader;

    public HotResponderAdapter(List<HotResponderPageResult> list, Context context) {
        this.list = list;
        this.context = context;
        this.imageLoader = ImageLoader.getInstance();
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final HotResponderPageResult obj = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_hot_responder, null);
            viewHolder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            viewHolder.iv_vip = (ImageView) convertView.findViewById(R.id.iv_vip);
            viewHolder.iv_gender = (ImageView) convertView.findViewById(R.id.iv_gender);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_qa_num = (TextView) convertView.findViewById(R.id.tv_qa_num);
            viewHolder.tv_observe_num = (TextView) convertView.findViewById(R.id.tv_observe_num);
            viewHolder.bt_ask = (Button) convertView.findViewById(R.id.bt_ask);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        imageLoader.displayImage(obj.getPortrait_uri(), viewHolder.iv_head, DisplayOptionsUtils.getHeaderDefaultImageOptions());
        viewHolder.iv_vip.setVisibility(obj.getUser_type() > 10 ? View.VISIBLE : View.INVISIBLE);
        viewHolder.iv_gender.setImageResource(obj.getUser_gender() == 2 ? R.drawable.gender_women : R.drawable.gender_man);
        viewHolder.tv_name.setText(obj.getUser_name());
        viewHolder.tv_title.setText(obj.getVip_note());
        viewHolder.tv_qa_num.setText(CommonUtil.formatCount(obj.getAnswer_total()));
        viewHolder.tv_observe_num.setText(CommonUtil.formatCount(obj.getPlay_total()));

        //判断是否是自己，是则隐藏提问按钮
        if (SpUtils.getString(ConstantsValue.Sp.USER_ID).equals(obj.getUser_id()))
            viewHolder.bt_ask.setVisibility(View.INVISIBLE);
        else
            viewHolder.bt_ask.setVisibility(View.VISIBLE);
        viewHolder.bt_ask.setText(obj.getUser_gender() == 2 ? "向她提问" : "向他提问");
        //点击头像
        viewHolder.iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((BaseActivity) context).isLogin(false))
                    context.startActivity(new Intent(context, PersonalHomeActivity.class)
                            .putExtra("userId", obj.getUser_id())
                    );
            }
        });
        //点击昵称
        viewHolder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((BaseActivity) context).isLogin(false))
                    context.startActivity(new Intent(context, PersonalHomeActivity.class)
                            .putExtra("userId", obj.getUser_id())
                    );
            }
        });
        //点击头衔
        viewHolder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((BaseActivity) context).isLogin(false))
                    context.startActivity(new Intent(context, PersonalHomeActivity.class)
                            .putExtra("userId", obj.getUser_id())
                    );
            }
        });
        //点击提问，跳转映答主页
        viewHolder.bt_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != obj&&!MHStringUtils.isEmpty(obj.getUser_id())){
                    context.startActivity(new Intent(context, QAHomeActivity.class)
                            .putExtra("userId", obj.getUser_id())
                            .putExtra("userName", obj.getUser_name())
                    );
                }
            }
        });
        //点击整个条目，跳转映答主页
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != obj && !MHStringUtils.isEmpty(obj.getUser_id())){
                    context.startActivity(new Intent(context, QAHomeActivity.class)
                            .putExtra("userId", obj.getUser_id())
                            .putExtra("userName", obj.getUser_name()));
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView iv_head;
        ImageView iv_vip;
        ImageView iv_gender;
        TextView tv_name;
        TextView tv_title;
        TextView tv_qa_num;
        TextView tv_observe_num;
        Button bt_ask;
    }
}
