package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.MyBaseAdapter;
import com.haiqiu.miaohi.bean.RechargingRecordItem;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DateUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;

/**
 * 通用充值交易记录adapter
 * Created by hackest on 16/8/17.
 */
public class CommonRechargingRecordAdapter extends MyBaseAdapter<RechargingRecordItem> {
    public CommonRechargingRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        RechargingRecordItem bean = myList.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_recharging_record, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //字体加粗
        TextPaint tp = holder.item_record_centent_name_text.getPaint();
        tp.setFakeBoldText(true);

        if (!MHStringUtils.isEmpty(bean.getDeposit_title()))
            holder.item_record_centent_name_text.setText(bean.getDeposit_title());
        holder.item_time_text.setText(DateUtil.milliseconds2String(bean.getDeposit_time()));
        holder.money_text.setText("¥" + CommonUtil.formatPrice4Point(bean.getDeposit_price()));
        //充值记录中是否有优惠金额
        if (!MHStringUtils.isEmpty(bean.getAdditional_hi_coin_text())) {
            holder.item_discount.setVisibility(View.VISIBLE);
            holder.item_discount.setText(bean.getAdditional_hi_coin_text());
        } else {
            holder.item_discount.setVisibility(View.GONE);
        }
        //是否有充值状态显示
        if (!MHStringUtils.isEmpty(bean.getDeposit_status_view())) {
            holder.item_record_state_text.setVisibility(View.VISIBLE);
            holder.item_record_state_text.setText(bean.getDeposit_status_view());
            if (bean.getDeposit_status_view().contains("失败"))
                holder.item_record_state_text.setTextColor(context.getResources().getColor(R.color.red_text));
            else
                holder.item_record_state_text.setTextColor(context.getResources().getColor(R.color.common_blue2));
        } else {
            holder.item_record_state_text.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView item_record_centent_name_text;
        public TextView item_record_state_text;
        public TextView item_time_text;
        public TextView money_text;
        public TextView item_discount;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.item_record_centent_name_text = (TextView) rootView.findViewById(R.id.item_record_centent_name_text);
            this.item_record_state_text = (TextView) rootView.findViewById(R.id.item_record_state_text);
            this.item_time_text = (TextView) rootView.findViewById(R.id.item_time_text);
            this.money_text = (TextView) rootView.findViewById(R.id.money_text);
            this.item_discount = (TextView) rootView.findViewById(R.id.item_discount);
        }

    }
}
