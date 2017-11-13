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
import com.haiqiu.miaohi.bean.MsgQaData;
import com.haiqiu.miaohi.bean.PushedMsgResult;
import com.haiqiu.miaohi.response.MsgQaResponse;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by LiuXiang on 2016/12/20.
 */
public class MessageQaAdapter extends BaseAdapter {
    private Context context;
    private List<PushedMsgResult> list;
    private List<MsgQaResponse> listMsg;
    private ImageLoader imageLoader;

    public MessageQaAdapter(Context context, List<PushedMsgResult> list, List<MsgQaResponse> listMsg) {
        imageLoader = ImageLoader.getInstance();
        this.context = context;
        this.list = list;
        this.listMsg = listMsg;
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
        ViewHolder vh;
//        //解析加密字符串
//        String content = list.get(position).getMsg_content();
//        content = Base64Util.getFromBase64(content);
//        //获取加密字符内的bean类
//        MsgQaResponse response = gson.fromJson(content, MsgQaResponse.class);
        String commond = listMsg.get(position).getBase().getCommand();
        final MsgQaData data = listMsg.get(position).getData();
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_message_qa, null);
            vh = new ViewHolder();
            vh.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            vh.iv_video = (ImageView) convertView.findViewById(R.id.iv_video);
            vh.iv_vip_mark = (ImageView) convertView.findViewById(R.id.iv_vip_mark);
            vh.iv_is_video = (ImageView) convertView.findViewById(R.id.iv_is_video);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.tv_prompt_language = (TextView) convertView.findViewById(R.id.tv_prompt_language);
            vh.tv_question = (TextView) convertView.findViewById(R.id.tv_question);
            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        //用户头像
        imageLoader.displayImage(data.getSender_portrait_uri(), vh.iv_head);
        //是否vip
        vh.iv_vip_mark.setVisibility(data.getSender_type() > 10 ? View.VISIBLE : View.INVISIBLE);
        //映答视频封面
        if (MHStringUtils.isEmpty(data.getVideo_cover_uri())) {
            vh.iv_video.setVisibility(View.GONE);
            vh.iv_is_video.setVisibility(View.GONE);
        } else {
            vh.iv_video.setVisibility(View.VISIBLE);
            vh.iv_is_video.setVisibility(View.VISIBLE);
            imageLoader.displayImage(data.getVideo_cover_uri(), vh.iv_video);
        }
        //用户名
        vh.tv_name.setText(data.getSender_name());
        //提问内容
        vh.tv_question.setText(data.getQuestion_text());
        //消息时间
        if (!MHStringUtils.isEmpty(list.get(position).getSend_time_text()))
            vh.tv_time.setText(list.get(position).getSend_time_text());
        //映答消息类型
        vh.tv_prompt_language.setTextColor(context.getResources().getColor(R.color.color_c4));
        switch (commond) {
            case "pmsgvipreceivequestion":
                vh.tv_prompt_language.setText("向你提了一个问题");
                vh.tv_prompt_language.setTextColor(context.getResources().getColor(R.color.red_text));
                break;
            case "pmsgvipansweruser":
            case "pmsgvipanswervip":
                vh.tv_prompt_language.setText("回答了你提问的映答");
                break;
            case "pmsgobservevideo":
                vh.tv_prompt_language.setText("围观了你的映答");
                break;
            default:
                vh.tv_prompt_language.setText("这个映答消息类型还未定义");
                break;
        }
        vh.iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PersonalHomeActivity.class)
                        .putExtra("userId", MHStringUtils.isEmpty(data.getSender_id()) ? "error" : data.getSender_id()));
            }
        });

        return convertView;
    }

    private class ViewHolder {
        ImageView iv_head;
        ImageView iv_video;
        ImageView iv_vip_mark;
        ImageView iv_is_video;
        TextView tv_name;
        TextView tv_prompt_language;
        TextView tv_question;
        TextView tv_time;
    }
}
