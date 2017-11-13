package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.FFmpegPreviewActivity;
import com.haiqiu.miaohi.activity.ImageCropActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.ChooseMediaBean;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by zhandalin on 2016-06-27 11:24.
 * 说明:
 */
public class ChooseVideoAdapter extends RecyclerView.Adapter<ChooseVideoAdapter.ChooseVideoViewHolder> {
    private BaseActivity context;
    private List<ChooseMediaBean> dataList;
    private final ImageLoader imageLoader;
    private final int width;
    private VideoUploadInfo videoUploadInfo;
    private static final String TAG = "ChooseVideoAdapter";

    public ChooseVideoAdapter(Context context, List<ChooseMediaBean> dataList) {
        this.context = (BaseActivity) context;
        this.dataList = dataList;
        imageLoader = ImageLoader.getInstance();
        width = (ScreenUtils.getScreenSize(context).x - context.getResources().getDimensionPixelSize(R.dimen.video_choose_spacing) * 2) / 3;
    }

    @Override
    public ChooseVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChooseVideoViewHolder(View.inflate(context, R.layout.item_choose_video, null));
    }

    @Override
    public void onBindViewHolder(ChooseVideoViewHolder holder, int position) {
        ChooseMediaBean mediaBean = dataList.get(position);
        imageLoader.displayImage(mediaBean.getVideoThumbnailUri(), holder.iv_preview_image, DisplayOptionsUtils.getDefaultMinRectImageOptions());
        if (mediaBean.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            holder.iv_video_flag.setVisibility(View.VISIBLE);
        } else {
            holder.iv_video_flag.setVisibility(View.GONE);
        }
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setInfo(VideoUploadInfo videoUploadInfo) {
        this.videoUploadInfo = videoUploadInfo;
    }

    public class ChooseVideoViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_preview_image;
        private final ImageView iv_video_flag;
        private int position;

        public ChooseVideoViewHolder(View itemView) {
            super(itemView);
            iv_preview_image = (ImageView) itemView.findViewById(R.id.iv_preview_image);
            iv_video_flag = (ImageView) itemView.findViewById(R.id.iv_video_flag);
            ViewGroup.LayoutParams layoutParams = iv_preview_image.getLayoutParams();
            layoutParams.height = layoutParams.width = width;
            iv_preview_image.setLayoutParams(layoutParams);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null == videoUploadInfo) videoUploadInfo = new VideoUploadInfo();
                    ChooseMediaBean chooseMediaBean = dataList.get(position);
                    if (null == chooseMediaBean) return;

                    if (chooseMediaBean.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                        videoUploadInfo.setVideoDuration(chooseMediaBean.getDuration());
                        videoUploadInfo.setVideoType(VideoUploadInfo.VIDEO_TYPE_LOCAL);
                        videoUploadInfo.setVideoPath(chooseMediaBean.getMediaPath());
                        videoUploadInfo.setVideoSrcPath(videoUploadInfo.getVideoPath());
                        videoUploadInfo.setVideoSrcWidth(chooseMediaBean.getMediaWidth());
                        videoUploadInfo.setVideoWidth(chooseMediaBean.getMediaWidth());
                        videoUploadInfo.setVideoHeight(chooseMediaBean.getMediaHeight());
                        videoUploadInfo.setMediaType(VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO);

                        if (chooseMediaBean.isFail()) {
//                        MHLogUtil.d(TAG, "选择的是超高清视频");
                            context.showToastAtCenter(chooseMediaBean.getFailMessage());
                            return;
                        }
                        if (chooseMediaBean.getMediaSize() > 1024) {
                            context.showToastAtCenter("您选择视频太大，暂不支持上传");
                            return;
                        }
                        if (chooseMediaBean.getDuration() < 3 * 1000) {
                            context.showToastAtCenter("您选择视频不足3秒，不支持上传");
                            return;
                        }

                        Intent intent = new Intent(context, FFmpegPreviewActivity.class);
                        intent.putExtra("videoUploadInfo", videoUploadInfo);
                        context.startActivity(intent);
                    } else {
                        videoUploadInfo.setPictureHeight(chooseMediaBean.getMediaHeight());
                        videoUploadInfo.setPictureWidth(chooseMediaBean.getMediaWidth());
                        videoUploadInfo.setPicturePath(chooseMediaBean.getMediaPath());
                        videoUploadInfo.setPictureSrcPath(chooseMediaBean.getMediaPath());
                        videoUploadInfo.setMediaType(VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE);
                        Intent intent = new Intent(context, ImageCropActivity.class);
                        intent.putExtra("videoUploadInfo", videoUploadInfo);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public void addData(List<ChooseMediaBean> data) {
        if (null == data || data.size() == 0) return;
        dataList.addAll(data);
        notifyDataSetChanged();
    }

}
