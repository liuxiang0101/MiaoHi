package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.PushVo;
import com.haiqiu.miaohi.bean.PushedMsgResult;
import com.haiqiu.miaohi.response.MsgContentResponse;
import com.haiqiu.miaohi.utils.Base64Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by miaohi on 2016/4/20.
 */
public class MessageSystemAdapter extends BaseAdapter {
    private Context context;
    private Gson gson;
    private List<PushedMsgResult> list;

    public MessageSystemAdapter(Context context, List<PushedMsgResult> list) {
        this.list = list;
        this.context = context;
        gson = new Gson();
    }

    @Override
    public int getCount() {
        return list.size();
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
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.list_item_message_system, null);
            vh.tvTitle = (TextView) convertView.findViewById(R.id.tv_message_system_name);
            vh.tvContent = (TextView) convertView.findViewById(R.id.tv_message_system_content);
            vh.tvTime = (TextView) convertView.findViewById(R.id.tv_message_system_time);
            vh.iv_rightarrow = (ImageView) convertView.findViewById(R.id.iv_rightarrow);
            vh.rl = (RelativeLayout) convertView.findViewById(R.id.rl);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        convertView.setTag(R.string.isSwipe, false);
        String msgContent = Base64Util.getFromBase64(list.get(position).getMsg_content());

        PushVo vo;
        String msgType = "0";
        String objectId = null;
        try {
            vo = gson.fromJson(msgContent, MsgContentResponse.class).getData();
        } catch (Exception e) {
            vo = null;
        }
        if (vo != null) {
            msgType = vo.getObjectType() == null ? "0" : vo.getObjectType();
            objectId = vo.getObjectId();
            vh.tvContent.setText(vo.getSystemContent());
        } else {
            vh.tvContent.setText(msgContent);
        }
        String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(list.get(position).getSend_time())));
        vh.tvTime.setText(list.get(position).getSend_time_text());
//        clickToDetail(vh.rl, msgType, objectId, vo.getObjectNote());
        switch (msgType) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "6":
                vh.iv_rightarrow.setVisibility(View.VISIBLE);
                break;
            default:
                vh.iv_rightarrow.setVisibility(View.INVISIBLE);
                break;
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        TextView tvTime;
        RelativeLayout rl;
        ImageView iv_rightarrow;
    }

//    private void clickToDetail(RelativeLayout rl, final String msgType, final String objectId, final String title) {
//        rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                if (msgType.equals("1")) {
//                    intent.setClass(context, VideoAndImgActivity.class);
//                    intent.putExtra("video_id", objectId);
//                } else if (msgType.equals("2")) {
//                    intent.setClass(context, ActiveDetailActivity.class);
//                    intent.putExtra("activity_name", title);
//                    intent.putExtra("activityId", objectId);
//                } else if (msgType.equals("3")) {
//                    intent.setClass(context, InterlocutionDetailsActivity.class);
//                    intent.putExtra("question_id", objectId);
//                } else if (msgType.equals("4")) {
//                    intent.setClass(context, PersonalHomeActivity.class);
//                    intent.putExtra("userId", objectId);
//                } else {
//                    return;
//                }
//                context.startActivity(intent);
//            }
//        });
//    }
}
