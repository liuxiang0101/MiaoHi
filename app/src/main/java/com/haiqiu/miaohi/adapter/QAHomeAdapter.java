package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.CommonPersonInfo;
import com.haiqiu.miaohi.activity.InterlocutionDetailsActivity;
import com.haiqiu.miaohi.bean.OthterQAInfo;
import com.haiqiu.miaohi.utils.TimeFormatUtils;
import com.haiqiu.miaohi.view.CommonPersonalInfoView;

import java.util.List;

/**
 * 映答主页适配器
 * Created by ningl on 16/12/8.
 */
public class QAHomeAdapter extends BaseAdapter {

    private Context context;
    private List<OthterQAInfo> data;
    private static final int CLICKPLAY = 0;     //点击播放
    private static final int LIMITTIMEFREE = 1; //限时免费
    private static final int PAYCIRCUSEE = 2;   //xx元围观

    public QAHomeAdapter(Context context, List<OthterQAInfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
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
            convertView = View.inflate(context, R.layout.item_qahomeqainfo, null);
            holder.cpiv_itemqahomeinfo = (CommonPersonalInfoView) convertView.findViewById(R.id.cpiv_itemqahomeinfo);
            holder.tv_qa_recommenddescribe = (TextView) convertView.findViewById(R.id.tv_qa_recommenddescribe);
            holder.tv_qarecommendcircusee_count = (TextView) convertView.findViewById(R.id.tv_qarecommendcircusee_count);
            holder.iv_timelimitfree = (ImageView) convertView.findViewById(R.id.iv_timelimitfree);
            holder.tv_qahomeetail_remaintime = (TextView) convertView.findViewById(R.id.tv_qahomeetail_remaintime);
            holder.ll_value_look = (LinearLayout) convertView.findViewById(R.id.ll_value_look);
            holder.rl_qahomeitembtn = (RelativeLayout) convertView.findViewById(R.id.rl_qahomeitembtn);
            holder.tv_qahomeitembtntext = (TextView) convertView.findViewById(R.id.tv_qahomeitembtntext);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OthterQAInfo othterQAInfo = data.get(position);
        CommonPersonInfo info = new CommonPersonInfo();
        info.setName_nodescribe(othterQAInfo.getAnswer_user_name());
        info.setTime(othterQAInfo.getAnswer_time_text());
        info.setUserType(othterQAInfo.getAnswer_user_type());
        info.setHeadUri(othterQAInfo.getAnswer_user_portrait());
        info.setUserId(othterQAInfo.getAnswer_user_id());
        info.setName(othterQAInfo.getAnswer_user_name());
        holder.cpiv_itemqahomeinfo.setUserInfo(info);
        holder.tv_qa_recommenddescribe.setText(othterQAInfo.getQuestion_content());
        holder.tv_qarecommendcircusee_count.setText(othterQAInfo.getObserve_count() + "");
        holder.tv_qahomeitembtntext.setText(othterQAInfo.getObserve_price_text());
        switch (getQAStat(othterQAInfo)) {
            case CLICKPLAY://点击播放
                holder.tv_qahomeetail_remaintime.setVisibility(View.GONE);
                holder.rl_qahomeitembtn.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
                break;

            case LIMITTIMEFREE://限时免费
                //限时免费的情况只有在剩余时间小于两个小时的时候才显示倒计时
                if (othterQAInfo.getTime_remain() / 3600000 <= 1) {
                    holder.tv_qahomeetail_remaintime.setVisibility(View.VISIBLE);
                    holder.tv_qahomeetail_remaintime.setText("还剩 " + TimeFormatUtils.getCountDownFormat((int) othterQAInfo.getTime_remain()));
                } else {
                    holder.tv_qahomeetail_remaintime.setVisibility(View.GONE);
                }
                holder.rl_qahomeitembtn.setBackgroundResource(R.drawable.shape_limit_free_radius_red_selector);
                break;

            case PAYCIRCUSEE://xx元围观
                holder.tv_qahomeetail_remaintime.setVisibility(View.GONE);
                holder.rl_qahomeitembtn.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
                holder.tv_qahomeitembtntext.setText(othterQAInfo.getObserve_price() / 100 + "元围观");
                break;
        }
        holder.rl_qahomeitembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, InterlocutionDetailsActivity.class)
                        .putExtra("question_id", othterQAInfo.getQuestion_id()));
            }
        });
        return convertView;
    }


    public class ViewHolder {
        private CommonPersonalInfoView cpiv_itemqahomeinfo;
        private TextView tv_qa_recommenddescribe;
        private TextView tv_qarecommendcircusee_count;
        private ImageView iv_timelimitfree;
        private TextView tv_qahomeetail_remaintime;
        private LinearLayout ll_value_look;
        private RelativeLayout rl_qahomeitembtn;
        private TextView tv_qahomeitembtntext;
    }

    /**
     * @param othterQAInfo
     * @return 0:点击播放 1：限时免费 2：xx元围观（倒计时）
     */
    private int getQAStat(OthterQAInfo othterQAInfo) {
        if (othterQAInfo.is_question_owner()) return CLICKPLAY;
        if (othterQAInfo.getTemporary_free()) return LIMITTIMEFREE;
        else return PAYCIRCUSEE;
    }
}
