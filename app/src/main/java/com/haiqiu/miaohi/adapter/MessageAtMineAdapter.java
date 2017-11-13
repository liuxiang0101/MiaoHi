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
import com.haiqiu.miaohi.bean.PushMsgNewFriendData;
import com.haiqiu.miaohi.bean.PushMsgNewFriendObj;
import com.haiqiu.miaohi.bean.PushedMsgResult;
import com.haiqiu.miaohi.bean.VideoAndImg;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miaohi on 2016/6/20.
 */
public class MessageAtMineAdapter extends BaseAdapter {
    private Context context;
    private List<PushedMsgResult> list;
    private List<PushMsgNewFriendObj> list_msg;
    private ImageLoader imageLoader;

    public MessageAtMineAdapter(Context context, List<PushedMsgResult> list, List<PushMsgNewFriendObj> list_msg) {
        this.context = context;
        this.list = list;
        this.list_msg = list_msg;
        imageLoader = ImageLoader.getInstance();
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
        final PushMsgNewFriendData data = list_msg.get(position).getData();
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.list_item_message_at_mine, null);
            vh.ivHead = (ImageView) convertView.findViewById(R.id.iv_message_comment_head);
            vh.ivVideo = (ImageView) convertView.findViewById(R.id.iv_message_comment_video);
            vh.iv_is_video = (ImageView) convertView.findViewById(R.id.iv_is_video);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_message_comment_name);
            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_message_comment_time);
            vh.tv_msg_type = (TextView) convertView.findViewById(R.id.tv_msg_type);
            vh.ivVip = (ImageView) convertView.findViewById(R.id.iv_isvip);

            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        convertView.setTag(R.string.isSwipe, false);
        if (!MHStringUtils.isEmpty(data.getSender_name()))
            vh.tv_name.setText(data.getSender_name());
        if (!MHStringUtils.isEmpty(list.get(position).getSend_time_text()))
            vh.tv_time.setText(list.get(position).getSend_time_text());

        vh.ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PersonalHomeActivity.class);
                intent.putExtra("userId", MHStringUtils.isEmpty(data.getSender_id())?"error":data.getSender_id());
                context.startActivity(intent);
            }
        });
        vh.tv_msg_type.setText(MHStringUtils.isEmpty(data.getComment_id()) ? "在发布中@了你" : "在评论中@了你");
        vh.ivVip.setVisibility(data.getSender_type() > 10 ? View.VISIBLE : View.INVISIBLE);
        vh.iv_is_video.setVisibility(MHStringUtils.isEmpty(data.getVideo_id()) ? View.INVISIBLE : View.VISIBLE);
        imageLoader.displayImage(data.getSender_portrait_uri(), vh.ivHead);
        //加载视频封面或图片
        if (MHStringUtils.isEmpty(data.getVideo_id()))
            imageLoader.displayImage(MHStringUtils.isEmpty(
                    data.getPhoto_thumb_uri()) ? data.getPhoto_uri() : data.getPhoto_thumb_uri(),
                    vh.ivVideo);
        else
            imageLoader.displayImage(data.getVideo_cover_uri(), vh.ivVideo);
        return convertView;
    }

    public void intoVideoDetail(PushMsgNewFriendData data) {
        Intent intent = new Intent(context, VideoAndImgActivity.class);
        ArrayList<VideoAndImg> bean = new ArrayList<>();
        VideoAndImg obj = new VideoAndImg();
        if (MHStringUtils.isEmpty(data.getVideo_id())) {
            obj.setElement_type(2);
            obj.setContent_type(2);
            obj.setPhoto_id(data.getPhoto_id());
        } else {
            obj.setElement_type(1);
            obj.setContent_type(1);
            obj.setVideo_id(data.getVideo_id());
        }
        bean.add(obj);
        intent.putParcelableArrayListExtra("data", bean)
                .putExtra("isNeedBack", true)
                .putExtra("currentIndex", 0)
                .putExtra("userId", data.getVideo_id())
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
        TextView tv_msg_type;
    }
}
