package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.InterlocutionDetailsActivity;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.bean.TradingRecordItem;
import com.haiqiu.miaohi.utils.DateUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;

import java.util.List;

/**
 * Created by LiuXiang on 2016/11/1.
 */
public class TradingRecordAdapter extends BaseAdapter {
    private Context context;
    private List<TradingRecordItem> list;
    private final int VIDEO = 1;

    public TradingRecordAdapter(Context context, List<TradingRecordItem> list) {
        this.context = context;
        this.list = list;
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
        final TradingRecordItem item = list.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_trading_record1, null);
            viewHolder.iv_record_type = (ImageView) convertView.findViewById(R.id.iv_record_type);
            viewHolder.tv_left = (TextView) convertView.findViewById(R.id.tv_left);
//            viewHolder.tv_center = (TextView) convertView.findViewById(R.id.tv_center);
//            viewHolder.tv_right = (TextView) convertView.findViewById(R.id.tv_right);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_record_money = (TextView) convertView.findViewById(R.id.tv_record_money);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (item.getTransaction_type()) {
            case 10:
                viewHolder.iv_record_type.setImageResource(R.drawable.trading_record_observe_icon);
                break;
            case 20:
                viewHolder.iv_record_type.setImageResource(R.drawable.trading_record_answer_icon);
                break;
            case 30:
                viewHolder.iv_record_type.setImageResource(R.drawable.trading_record_ask_icon);
                break;
            case 40:
                viewHolder.iv_record_type.setImageResource(R.drawable.trading_record_gift_icon);
                break;
            case 50:
                viewHolder.iv_record_type.setImageResource(R.drawable.trading_record_refund_icon);
                break;
        }
        if (!MHStringUtils.isEmpty(item.getTransaction_content())) {

            String text = item.getTransaction_content();
            String text1 = text.substring(0, text.indexOf("<"));
            String text2 = text.substring(text.indexOf("<") + 1, text.indexOf(">"));
            String text3 = text.substring(text.indexOf(">") + 1, text.length());

            String string;
            int lenth1;
            int lenth2;
            if (!MHStringUtils.isEmpty(text1)) {
                string = text1 + " " + text2 + " " + text3;
                lenth1 = text1.length() + 1;
                lenth2 = (text1 + text2).length() + 2;
            } else {
                lenth1 = 0;
                lenth2 = text2.length() + 1;
                string = text2 + " " + text3;
            }

            SpannableString spannableString;
//            if (string.contains(text1)) {
                spannableString = new SpannableString(string);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), lenth1, lenth2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), lenth1, lenth2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            } else {
//                spannableString = new SpannableString("");
//            }

                viewHolder.tv_left.setText(spannableString);
//            viewHolder.tv_center.setText(text2 + " ");
//            viewHolder.tv_right.setText(text3);
        }

        viewHolder.tv_time.setText(DateUtil.milliseconds2String(item.getTransaction_time()));
        viewHolder.tv_record_money.setText(item.getHi_coin_view());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch (item.getTransaction_type()) {
                    case 10:
                        intent = new Intent(context, InterlocutionDetailsActivity.class);
                        intent.putExtra("question_id", item.getObject_id());
                        break;
                    case 20:
                        intent = new Intent(context, InterlocutionDetailsActivity.class);
                        intent.putExtra("question_id", item.getObject_id());
                        break;
                    case 30:
                        intent = new Intent(context, PersonalHomeActivity.class);
                        intent.putExtra("userId", item.getObject_id());
                        break;
                    case 40:
//                        intent = new Intent(context, VideoAndImgActivity.class);
//                        ArrayList<VideoAndImg> list = new ArrayList<>();
//                        VideoAndImg videoAndImg = new VideoAndImg();
//                        videoAndImg.setContent_type(VIDEO);
//                        videoAndImg.setVideo_id(item.getObject_id());
//                        list.add(videoAndImg);
//                        context.startActivity(new Intent(context, VideoAndImgActivity.class)
//                                .putParcelableArrayListExtra("data", list));
                        break;
                    case 50:
                        intent = new Intent(context, PersonalHomeActivity.class);
                        intent.putExtra("userId", item.getObject_id());
                        break;
                }
                if (intent != null) context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView iv_record_type;
        TextView tv_left;
        TextView tv_center;
        TextView tv_right;
        TextView tv_time;
        TextView tv_record_money;
    }
}
