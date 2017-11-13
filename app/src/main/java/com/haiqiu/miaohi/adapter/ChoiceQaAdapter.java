package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.CommonPersonInfo;
import com.haiqiu.miaohi.activity.InterlocutionDetailsActivity;
import com.haiqiu.miaohi.bean.ChoiceQaPageResult;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.view.CommonPersonalInfoView;
import com.haiqiu.miaohi.view.TimeTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by LiuXiang on 2016/12/6.
 */
public class ChoiceQaAdapter extends BaseAdapter {
    private List<ChoiceQaPageResult> list;
    private Context context;
    private ImageLoader imageLoader;

    public ChoiceQaAdapter(List<ChoiceQaPageResult> list, Context context) {
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
        final ViewHolder viewHolder;
        final ChoiceQaPageResult obj = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_choice_qa, null);
            viewHolder.cpiv_qa_recommend = (CommonPersonalInfoView) convertView.findViewById(R.id.cpiv_qa_recommend);
            viewHolder.rl_outer_shape = (RelativeLayout) convertView.findViewById(R.id.rl_outer_shape);
            viewHolder.tv_qa_price = (TextView) convertView.findViewById(R.id.tv_qa_price);
            viewHolder.tv_qaetail_remaintime = (TimeTextView) convertView.findViewById(R.id.tv_qaetail_remaintime);
            viewHolder.tv_qa_recommenddescribe = (TextView) convertView.findViewById(R.id.tv_qa_recommenddescribe);
            viewHolder.tv_qarecommendcircusee_count = (TextView) convertView.findViewById(R.id.tv_qarecommendcircusee_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CommonPersonInfo info = new CommonPersonInfo();
        info.setHeadUri(obj.getAnswer_user_portrait());
        info.setName(obj.getAnswer_user_name());
        info.setName_nodescribe(obj.getAnswer_vip_note());
        info.setDescribe(obj.getAnswer_vip_note());
        info.setTime(obj.getAnswer_time_text() + "回答了");
        info.setUserType(obj.getAnswer_user_type());
        info.setUserId(obj.getAnswer_user_id());
        viewHolder.cpiv_qa_recommend.setUserInfo(info);
        viewHolder.tv_qa_recommenddescribe.setText(obj.getQuestion_content());
        viewHolder.tv_qarecommendcircusee_count.setText(CommonUtil.formatCount(obj.getObserve_count()));
        if (obj.isTemporary_free()) {
            viewHolder.rl_outer_shape.setBackgroundResource(R.drawable.shape_limit_free_radius_red_selector);
            viewHolder.tv_qa_price.setText("限时免费");
        } else {
            viewHolder.rl_outer_shape.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
            viewHolder.tv_qa_price.setText(obj.getObserve_price_text());
        }
        //倒计时结束，改变限时免费状态
        viewHolder.tv_qaetail_remaintime.setVisibility(View.VISIBLE);
        viewHolder.tv_qaetail_remaintime.setTimes(obj.getTime_remain());
        viewHolder.tv_qaetail_remaintime.setOnTimeMonitorListener(new TimeTextView.OnTimeMonitorListener() {
            @Override
            public void isTimeUp(boolean b) {
                viewHolder.rl_outer_shape.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
                viewHolder.tv_qa_price.setText(CommonUtil.formatPrice2(obj.getObserve_price()) + "元围观");
            }
        });
        //点击整个条目，跳转映答详情
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, InterlocutionDetailsActivity.class).putExtra("question_id", obj.getQuestion_id()));
            }
        });
        return convertView;
    }

    class ViewHolder {
        CommonPersonalInfoView cpiv_qa_recommend;
        RelativeLayout rl_outer_shape;
        TextView tv_qa_price;
        TimeTextView tv_qaetail_remaintime;
        TextView tv_qa_recommenddescribe;
        TextView tv_qarecommendcircusee_count;
    }
}
