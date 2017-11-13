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
import com.haiqiu.miaohi.bean.PushMsgReceiveZanData;
import com.haiqiu.miaohi.bean.PushMsgReceiveZanObj;
import com.haiqiu.miaohi.bean.PushedMsgResult;
import com.haiqiu.miaohi.bean.ServerResponseBaseInfo;
import com.haiqiu.miaohi.bean.VideoAndImg;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.HomeFoundResponse;
import com.haiqiu.miaohi.utils.MHStateSyncUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by miaohi on 2016/6/16.
 * 消息-通知-适配器
 */
public class MessageNoticeAdapter extends BaseAdapter {
    private Context context;
    private List<PushedMsgResult> list;
    private List<PushMsgReceiveZanObj> list_msg;
    private ImageLoader imageLoader;
    private HashMap<String, Boolean> hashMap;

    public MessageNoticeAdapter(Context context, List list, List<PushMsgReceiveZanObj> list_msg, HashMap<String, Boolean> hashMap) {
        this.context = context;
        this.list = list;
        this.list_msg = list_msg;
        this.hashMap = hashMap;
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
        ViewHolder viewHolder;
        final PushMsgReceiveZanData data = list_msg.get(position).getData();
        ServerResponseBaseInfo base = list_msg.get(position).getBase();
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.list_item_message_receive_zan, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_message_comment_name);
            viewHolder.tvAttention = (TextView) convertView.findViewById(R.id.tv_attention);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_message_comment_time);
            viewHolder.tvType = (TextView) convertView.findViewById(R.id.tv_message_comment_type);
            viewHolder.ivHead = (ImageView) convertView.findViewById(R.id.iv_message_comment_head);
            viewHolder.ivVideo = (ImageView) convertView.findViewById(R.id.iv_message_comment_video);
            viewHolder.ivVip = (ImageView) convertView.findViewById(R.id.iv_isvip);
            viewHolder.iv_video_mark = (ImageView) convertView.findViewById(R.id.iv_video_mark);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        convertView.setTag(R.string.isSwipe, false);

        //判断消息类型
        viewHolder.tvAttention.setVisibility(View.INVISIBLE);
        viewHolder.ivVideo.setVisibility(View.INVISIBLE);
        viewHolder.iv_video_mark.setVisibility(View.INVISIBLE);
        MHStateSyncUtil.State syncState = MHStateSyncUtil.getSyncState(data.getSender_id());
        if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != syncState) {
            hashMap.put(data.getSender_id(), MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == syncState);
        }
        switch (base.getCommand()) {
            case "pmsgreceiveattention":                            //收到关注
                viewHolder.tvAttention.setVisibility(View.VISIBLE);
                viewHolder.tvType.setText("关注你啦~");
                if (hashMap.get(data.getSender_id()) != null) {
                    if (hashMap.get(data.getSender_id())) {
                        viewHolder.tvAttention.setText("已关注");
                        viewHolder.tvAttention.setTextColor(context.getResources().getColor(R.color.color_c4));
                        viewHolder.tvAttention.setBackgroundResource(R.drawable.tag_bg);
                    } else {
                        viewHolder.tvAttention.setText("关注");
                        viewHolder.tvAttention.setTextColor(context.getResources().getColor(R.color.fontblue));
                        viewHolder.tvAttention.setBackgroundResource(R.drawable.shape_attention_blue_selector);
                    }
                    viewHolder.tvAttention.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tvAttention.setVisibility(View.INVISIBLE);
                }
                viewHolder.tvAttention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (hashMap.get(data.getSender_id())) {
                            CommonDialog commonDialog = new CommonDialog(context);
                            commonDialog.setLeftButtonMsg("取消");
                            commonDialog.setRightButtonMsg("确定");
                            commonDialog.setContentMsg("确定要取消关注吗？");
                            commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                                @Override
                                public void onRightButtonOnClick() {
                                    changeAttentionState(data.getSender_id(), !hashMap.get(data.getSender_id()) + "", v);
                                }
                            });
                            commonDialog.show();
                        } else {
                            changeAttentionState(data.getSender_id(), !hashMap.get(data.getSender_id()) + "", v);
                        }
                    }
                });
                break;
            case "pmsgreceivepraisevideo":
                if (MHStringUtils.isEmpty(data.getPhoto_id())) {    //收到点赞-视频
                    viewHolder.ivVideo.setVisibility(View.VISIBLE);
                    viewHolder.iv_video_mark.setVisibility(View.VISIBLE);
                    viewHolder.tvType.setText(context.getString(R.string.tv_give_fabulous_to_your_video));
                    imageLoader.displayImage(data.getVideo_cover_uri(), viewHolder.ivVideo/*, DisplayOptionsUtils.getCornerImageOptions(DensityUtil.dip2px(context, 5))*/);
                } else {                                            //收到点赞-图片
                    viewHolder.ivVideo.setVisibility(View.VISIBLE);
                    viewHolder.tvType.setText("赞了你的图片");
                    if (MHStringUtils.isEmpty(data.getPhoto_thumb_uri()))
                        imageLoader.displayImage(data.getPhoto_uri(), viewHolder.ivVideo);
                    else
                        imageLoader.displayImage(data.getPhoto_thumb_uri(), viewHolder.ivVideo);
                }
                break;
        }

        if (!MHStringUtils.isEmpty(data.getSender_name()))
            viewHolder.tvName.setText(data.getSender_name());
        else
            viewHolder.tvName.setText("未知错误");
        if (!MHStringUtils.isEmpty(list.get(position).getSend_time_text()))
            viewHolder.tvTime.setText(list.get(position).getSend_time_text());
        else
            viewHolder.tvName.setText("time error");

        viewHolder.ivVip.setVisibility(data.getSender_type() > 10 ? View.VISIBLE : View.INVISIBLE);
        imageLoader.displayImage(data.getSender_portrait_uri(), viewHolder.ivHead);

        //点击头像跳转
        viewHolder.ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PersonalHomeActivity.class);
                intent.putExtra("userId", MHStringUtils.isEmpty(data.getSender_id()) ? "error" : data.getSender_id());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    //实体类，复用时用到
    private class ViewHolder {
        TextView tvName;
        TextView tvTime;
        TextView tvAttention;
        TextView tvType;
        ImageView ivHead;
        ImageView ivVideo;
        ImageView ivVip;
        ImageView iv_video_mark;
    }

    private void changeAttentionState(final String userId, final String targetAttentionState, final View view) {
        if (MHStringUtils.isEmpty(userId) || MHStringUtils.isEmpty(targetAttentionState)) return;
        final TextView textView = (TextView) view;
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("action_mark", targetAttentionState);
        requestParams.addParams("user_id", userId);
        textView.setEnabled(false);
        textView.setText(null);
        MHHttpClient.getInstance().post(HomeFoundResponse.class, context, ConstantsValue.Url.ATTENTIONDO, requestParams, new MHHttpHandler<HomeFoundResponse>() {
            @Override
            public void onSuccess(HomeFoundResponse response) {
                MHStateSyncUtil.pushSyncEvent(context, userId, Boolean.parseBoolean(targetAttentionState));
                hashMap.put(userId, Boolean.parseBoolean(targetAttentionState));
                resetState(textView, Boolean.parseBoolean(targetAttentionState));
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(String content) {
                resetState(textView, !Boolean.parseBoolean(targetAttentionState));
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                resetState(textView, !Boolean.parseBoolean(targetAttentionState));
            }
        });
    }

    private void resetState(TextView view, boolean state) {
        view.setEnabled(true);
        if (state) {
            view.setText("已关注");
            view.setTextColor(context.getResources().getColor(R.color.color_c4));
            view.setBackgroundResource(R.drawable.tag_bg);
        } else {
            view.setText("关注");
            view.setTextColor(context.getResources().getColor(R.color.fontblue));
            view.setBackgroundResource(R.drawable.shape_attention_blue_selector);
        }
    }

    //点击列表条目进入详情
    public void intoDetail(PushMsgReceiveZanObj obj) {
        PushMsgReceiveZanData data = obj.getData();
        ServerResponseBaseInfo base = obj.getBase();
        Intent intent = null;
        ArrayList<VideoAndImg> bean;
        VideoAndImg viObj;
        switch (base.getCommand()) {
            case "pmsgreceiveattention":                            //关注消息
                intent = new Intent(context, PersonalHomeActivity.class);
                intent.putExtra("userId", data.getSender_id());
                break;
            case "pmsgreceivepraisevideo":                          //点赞消息
                intent = new Intent(context, VideoAndImgActivity.class);
                bean = new ArrayList<>();
                viObj = new VideoAndImg();
                if (MHStringUtils.isEmpty(data.getPhoto_id())) {    //视频点赞
                    viObj.setElement_type(1);
                    viObj.setContent_type(1);
                    viObj.setPhoto_id("");
                    viObj.setVideo_id(data.getVideo_id());

                } else {                                            //图片点赞
                    viObj.setElement_type(2);
                    viObj.setContent_type(2);
                    viObj.setPhoto_id(data.getPhoto_id());
                    viObj.setVideo_id("");
                }
                bean.add(viObj);
                intent.putParcelableArrayListExtra("data", bean)
                        .putExtra("isNeedBack", true)
                        .putExtra("currentIndex", 0)
                        .putExtra("userId", data.getVideo_id())
                        .putExtra("pageIndex", 0)
                        .putExtra("command", ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS);
                break;
            default:
                break;
        }
        if (intent != null)
            context.startActivity(intent);
    }
}
