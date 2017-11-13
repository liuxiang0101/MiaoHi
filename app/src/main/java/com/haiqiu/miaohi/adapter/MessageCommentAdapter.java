package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.activity.VideoAndImgActivity;
import com.haiqiu.miaohi.bean.PushMsgCommentMsgData;
import com.haiqiu.miaohi.bean.PushMsgCommentMsgObj;
import com.haiqiu.miaohi.bean.PushedMsgResult;
import com.haiqiu.miaohi.bean.VideoAndImg;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miaohi on 2016/4/20.
 * 消息页面  评论
 */
public class MessageCommentAdapter extends BaseAdapter {
    private Context context;
    private List<PushedMsgResult> list;
    private List<PushMsgCommentMsgObj> list_msg;
    private ImageLoader imageLoader;

    public MessageCommentAdapter(Context context, List<PushedMsgResult> list, List<PushMsgCommentMsgObj> list_msg) {
        this.context = context;
        this.list = list;
        this.list_msg = list_msg;
        this.imageLoader = ImageLoader.getInstance();
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
        final PushMsgCommentMsgData data;
        data = list_msg.get(position).getData();
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.list_item_message_comment, null);
            vh.ivHead = (ImageView) convertView.findViewById(R.id.iv_message_comment_head);
            vh.ivVideo = (ImageView) convertView.findViewById(R.id.iv_message_comment_video);
            vh.iv_is_video = (ImageView) convertView.findViewById(R.id.iv_is_video);
            vh.ivVip = (ImageView) convertView.findViewById(R.id.iv_isvip);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_message_comment_name);
            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_message_comment_time);
            vh.tv_msg = (TextView) convertView.findViewById(R.id.tv_message_comment_msg);
            vh.tv_message_comment_type = (TextView) convertView.findViewById(R.id.tv_message_comment_type);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        convertView.setTag(R.string.isSwipe, false);
        if (data == null) {
            vh.tv_name.setText(null);
            vh.tv_time.setText(null);
            vh.tv_message_comment_type.setText(null);
            vh.iv_is_video.setVisibility(View.INVISIBLE);
            vh.tv_msg.setText(null);
            vh.tv_message_comment_type.setText(null);
            vh.tv_msg.setText(null);
            vh.ivVip.setVisibility(View.INVISIBLE);
            imageLoader.displayImage(null, vh.ivHead);
            imageLoader.displayImage(null, vh.ivVideo, DisplayOptionsUtils.getCornerImageOptions(DensityUtil.dip2px(context, 5)));
            vh.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            return convertView;
        }

        if (!MHStringUtils.isEmpty(data.getComment_user_name()))
            vh.tv_name.setText(data.getComment_user_name());
        if (!MHStringUtils.isEmpty(list.get(position).getSend_time_text()))
            vh.tv_time.setText(list.get(position).getSend_time_text());
        vh.iv_is_video.setVisibility(data.getRoot_type() == 2 ? View.VISIBLE : View.INVISIBLE);
        if (!MHStringUtils.isEmpty(data.getRoot_id()) && !MHStringUtils.isEmpty(data.getCommented_id())) {
            if (data.getRoot_id().equals(data.getCommented_id())) {
                vh.tv_message_comment_type.setText(data.getRoot_type() == 2 ? "评论了你的视频" : "评论了你的图片");
                vh.tv_msg.setText("“" + data.getComment_text() + "”");
            } else {
                vh.tv_message_comment_type.setText("回复了你的评论");
                vh.tv_msg.setText("“" + data.getComment_text() + "”");
            }
        }
        vh.ivVip.setVisibility(data.getComment_user_type() > 10 ? View.VISIBLE : View.INVISIBLE);
        imageLoader.displayImage(data.getComment_user_portrait_uri(), vh.ivHead);
        imageLoader.displayImage(data.getRoot_cover_uri(), vh.ivVideo, DisplayOptionsUtils.getCornerImageOptions(DensityUtil.dip2px(context, 5)));

        vh.ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PersonalHomeActivity.class);
                intent.putExtra("userId", MHStringUtils.isEmpty(data.getComment_user_id()) ? "error" : data.getComment_user_id());
                context.startActivity(intent);
            }
        });
//        vh.ivVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, VideoDetailActivity.class);
//                intent.putExtra("video_id", data.getRoot_id());
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }

    public void intoVideoDetail(PushMsgCommentMsgData data) {
        if (data == null) return;
//        PushMsgCommentMsgData data = gson.fromJson(Base64Util.getFromBase64(string), PushMsgCommentMsgObj.class).getData();
        Intent intent = new Intent(context, VideoAndImgActivity.class);
        ArrayList<VideoAndImg> bean = new ArrayList<>();
        VideoAndImg obj = new VideoAndImg();
        obj.setElement_type(data.getRoot_type() == 2 ? 1 : 2);
        obj.setContent_type(data.getRoot_type() == 2 ? 1 : 2);
        obj.setPhoto_id(data.getRoot_id());
        obj.setVideo_id(data.getRoot_id());
        bean.add(obj);
        intent.putParcelableArrayListExtra("data", bean)
                .putExtra("isNeedBack",true)
                .putExtra("currentIndex", 0)
                .putExtra("userId", data.getRoot_id())
                .putExtra("pageIndex", 0)
                .putExtra("command", ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS);
        context.startActivity(intent);
    }

    private class ViewHolder {
        ImageView ivHead;
        ImageView ivVideo;
        ImageView iv_is_video;
        ImageView ivVip;
        TextView tv_name;
        TextView tv_time;
        TextView tv_msg;
        TextView tv_message_comment_type;
    }
}
