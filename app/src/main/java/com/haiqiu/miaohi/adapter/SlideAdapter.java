package com.haiqiu.miaohi.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.PicturePublishActivity;
import com.haiqiu.miaohi.activity.VideoPublishActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.Notify_user_result;
import com.haiqiu.miaohi.bean.UserInfo;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.receiver.UploadProgressEvent;
import com.haiqiu.miaohi.response.UserInfoResponse1;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.TimeFormatUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.utils.upload.UploadListUtil;
import com.haiqiu.miaohi.utils.upload.UploadService;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class SlideAdapter extends BaseAdapter {
    private BaseActivity context;
    private List<VideoUploadInfo> dataList;
    private OnDraftDeleteListener onDraftDeleteListener;

    public SlideAdapter(BaseActivity context, List<VideoUploadInfo> dataList) {
        this.context = context;
        this.dataList = dataList;
        List<VideoUploadInfo> queue;
        //进入草稿箱前判断上传队列中上传的任务是否在草稿箱中存在
        // 如果存在则显示上传
        queue = UploadListUtil.getInstance().getQueue();
        if (null != queue) {
            for (VideoUploadInfo videoUploadInfo : this.dataList) {
                for (int i = 0; i < queue.size(); i++) {
                    videoUploadInfo.setUploadState(0);
                    if (videoUploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
                        //视频
                        if (TextUtils.equals(videoUploadInfo.getVideoSrcPath(), queue.get(i).getVideoSrcPath())) {
                            videoUploadInfo.setUploadState(UploadProgressEvent.UPLOAD_PRE);
                        }
                    } else if (videoUploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE) {
                        //图片
                        if (TextUtils.equals(videoUploadInfo.getPictureSrcPath(), queue.get(i).getPictureSrcPath())) {
                            videoUploadInfo.setUploadState(UploadProgressEvent.UPLOAD_PRE);
                        }
                    }
                }
            }
        } else {
            for (VideoUploadInfo videoUploadInfo : this.dataList) {
                videoUploadInfo.setUploadState(0);
            }
        }
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_mine_drafts, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final VideoUploadInfo videoUploadInfo = dataList.get(position);
        //判断草稿箱存入类型判断是否显示上传失败
        if (videoUploadInfo.isUploadFail()) {
            holder.tv_updatefail.setVisibility(View.VISIBLE);
        } else {
            holder.tv_updatefail.setVisibility(View.GONE);
        }
        if (videoUploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
            if (MHStringUtils.isEmpty(videoUploadInfo.getQuestionId())) {
                holder.tv_updatefail.setText("视频上传失败");
            } else {
                holder.tv_updatefail.setText("映答上传失败");
            }
        } else if (videoUploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE) {
            holder.tv_updatefail.setText("图片上传失败");
        } else {
            holder.tv_updatefail.setText("上传失败");
        }

        if (videoUploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
            //视频
            ImageLoader.getInstance().displayImage("file:///" + videoUploadInfo.getVideoPreviewImagePath(), holder.iv);
        } else if (videoUploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE) {
            //图片
            ImageLoader.getInstance().displayImage("file:///" + videoUploadInfo.getPicturePath(), holder.iv);
        }
        //映答的视频是过期剩余时间
        if (!MHStringUtils.isEmpty(videoUploadInfo.getQuestionId())) {//映答拍摄
            holder.videoTime.setText(MHStringUtils.getDifferenceTime3(videoUploadInfo.getQuestionEndTime(), TimeFormatUtils.getCurrentTimeMillis_CH()) + " 后此视频自动消失");
        } else {
            holder.videoTime.setText(TimeFormatUtils.formatTime(videoUploadInfo.getSaveTime()));
        }
        holder.tv_drafts_btn.setEnabled(true);

        holder.tv_drafts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoUploadInfo.setFromInfo(VideoUploadInfo.FROM_DRAFTS);
                if (CommonUtil.isNetworkAvailable(context)) {
                    if (videoUploadInfo.isUploadFail()) {
                        //上传失败重新上传
                        MHLogUtil.i("草稿箱点击重新上传按钮");
                        holder.tv_drafts_btn.setText("0%");
                        holder.tv_drafts_btn.setEnabled(false);
                        videoUploadInfo.setUploadState(UploadProgressEvent.UPLOAD_PRE);
                        Intent it = new Intent(context, UploadService.class);
                        it.putExtra("uploadTask", videoUploadInfo);
                        it.putExtra("uploadType", VideoUploadInfo.FROM_DRAFTS);
//                        context.startService(it);
                        getUserInfo(it, holder, videoUploadInfo);
                    } else if (videoUploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
                        Intent intent = new Intent(context, VideoPublishActivity.class);
                        intent.putExtra("videoUploadInfo", videoUploadInfo);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, PicturePublishActivity.class);
                        intent.putExtra("videoUploadInfo", videoUploadInfo);
                        context.startActivity(intent);
                    }
                } else {
                    context.showToastAtCenter(ConstantsValue.Other.NETWORK_ERROR_TIP_MSG);
                }
            }
        });
        setItemStyle(videoUploadInfo, holder);
        handleUpload(videoUploadInfo, holder);
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(videoUploadInfo.isUploading()) return false;
                final CommonDialog commonDialog = new CommonDialog(context);
                commonDialog.setContentMsg("确定要删除该草稿吗？");
                commonDialog.setRightButtonMsg("确定");
                commonDialog.setLeftButtonMsg("取消");
                commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
                    @Override
                    public void onLeftButtonOnClick() {
                        commonDialog.dismiss();
                    }
                });
                commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                    @Override
                    public void onRightButtonOnClick() {
                        // 删除
                        if(onDraftDeleteListener != null){
                            onDraftDeleteListener.onDraftDelete(position);
                        }
                    }
                });
                commonDialog.show();
                return false;
            }
        });
        return convertView;
    }

    /**
     * 处理上传
     */
    private void handleUpload(VideoUploadInfo draftsBean, ViewHolder holder) {
        //适合两个场景 1、从草稿箱直接上传
        //            2、拍摄后存入草稿箱 然后继续上传
        if (draftsBean != null
                && (draftsBean.getFromInfo() == VideoUploadInfo.FROM_DRAFTS
                || draftsBean.getFromInfo() == VideoUploadInfo.FROM_COMMON
                || draftsBean.getFromInfo() == VideoUploadInfo.FROM_ASK_AND_ANSWER)) {
            switch (draftsBean.getUploadState()) {
                case VideoUploadInfo.UPLOAD_PRE:
                    holder.tv_drafts_btn.setText("0%");
                    holder.tv_drafts_btn.setEnabled(false);
                    draftsBean.setUploading(true);
                    break;
                case VideoUploadInfo.UPLOAD_PROGRESS:
                    holder.tv_drafts_btn.setText((draftsBean.getProsess()) + "%");
                    holder.tv_drafts_btn.setEnabled(false);
                    draftsBean.setUploading(true);
                    break;
                case VideoUploadInfo.UPLOAD_SUCCESS:
                    holder.tv_drafts_btn.setText("100%");
                    holder.tv_drafts_btn.setEnabled(true);
                    draftsBean.setUploading(true);
                    break;
                case VideoUploadInfo.UPLOAD_FAILE:
                    holder.tv_drafts_btn.setText("重新上传");
                    holder.tv_drafts_btn.setEnabled(true);
                    draftsBean.setUploading(false);
                    break;
            }

        }
    }

    /**
     * 设置item样式
     */
    private void setItemStyle(VideoUploadInfo videoUploadInfo, ViewHolder holder) {
        if (videoUploadInfo.isUploadFail()) {
            holder.tv_drafts_btn.setText("重新上传");
        } else {
            holder.tv_drafts_btn.setText("编辑");
        }
        if (videoUploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
            if (MHStringUtils.isEmpty(videoUploadInfo.getQuestionId())) {
                holder.videoName.setText("我的视频");
            } else {
                holder.videoName.setText("我的映答");
            }
        } else if (videoUploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE) {
            holder.videoName.setText("我的图片");
        } else {
            holder.tv_updatefail.setText("我的草稿");
        }
    }

    private class ViewHolder {
        private TextView videoName;
        private TextView videoTime;
        private ImageView iv;
        private TextView tv_updatefail;
        private TextView tv_drafts_btn;

        public ViewHolder(View contentView) {
            videoName = (TextView) contentView.findViewById(R.id.tv_mine_drafts_vedio);
            videoTime = (TextView) contentView.findViewById(R.id.tv_mine_drafts_time);
            iv = (ImageView) contentView.findViewById(R.id.iv_mine_drafts_img);
            tv_updatefail = (TextView) contentView.findViewById(R.id.tv_updatefail);
            tv_drafts_btn = (TextView) contentView.findViewById(R.id.tv_drafts_btn);
        }
    }


    /**
     * 获取人信息
     */
    public void getUserInfo(final Intent it, final ViewHolder holder, final VideoUploadInfo videoUploadInfo) {
        MHRequestParams params = new MHRequestParams();
        params.addParams("user_id", UserInfoUtil.getUserId(context));
        //TODO UserInfoResponse1 改名
        MHHttpClient.getInstance().post(UserInfoResponse1.class, context, ConstantsValue.Url.GET_USERINFO, params, new MHHttpHandler<UserInfoResponse1>() {
            @Override
            public void onSuccess(UserInfoResponse1 response) {
                UserInfo userInfo = response.getData();
                videoUploadInfo.setUserInfo(userInfo);
                videoUploadInfo.setNotify_user_results(getNotifyUserResult(videoUploadInfo));
                it.putExtra("uploadTask", videoUploadInfo);
                context.startService(it);
            }

            @Override
            public void onFailure(String content) {
//                ToastUtils.showToastAtCenter(context, "获取用户失败");
                holder.tv_drafts_btn.setEnabled(true);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
//                ToastUtils.showToastAtCenter(context, "获取用户失败");
                holder.tv_drafts_btn.setEnabled(true);
            }
        });
    }

    /**
     * 获取@好友集合
     *
     * @return
     */
    public ArrayList<Notify_user_result> getNotifyUserResult(VideoUploadInfo uploadInfo) {
        ArrayList<Notify_user_result> notify_user_results = new ArrayList<>();
        if (uploadInfo == null) return notify_user_results;
        List<String> atUserName = uploadInfo.getNotifyUserName();
        List<String> atUserId = uploadInfo.getNotifyUserId();
        if (atUserName == null) return notify_user_results;
        if (atUserId == null) return notify_user_results;
        for (int i = 0; i < atUserId.size(); i++) {
            Notify_user_result notify_user_result = new Notify_user_result();
            notify_user_result.setNotify_user_name(atUserName.get(i).replace("@", ""));
            notify_user_result.setNotify_user_id(atUserId.get(i));
            notify_user_results.add(notify_user_result);
        }
        return notify_user_results;
    }

    public interface OnDraftDeleteListener{
        void onDraftDelete(int position);
    }

    public void setOnDraftDeleteListener(OnDraftDeleteListener onDraftDeleteListener){
        this.onDraftDeleteListener = onDraftDeleteListener;
    }

}
