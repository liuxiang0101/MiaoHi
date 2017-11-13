package com.haiqiu.miaohi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.activity.VideoRecorderActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.Comment;
import com.haiqiu.miaohi.bean.ShareVideoAndImgInfo;
import com.haiqiu.miaohi.bean.TextInfo;
import com.haiqiu.miaohi.bean.UserWork;
import com.haiqiu.miaohi.bean.VideoDetailUserCommentBean;
import com.haiqiu.miaohi.bean.VideoExtraInfo;
import com.haiqiu.miaohi.bean.VideoInfo;
import com.haiqiu.miaohi.fragment.MineFragment;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.receiver.DeleteVideoAndImgAsyncEvent;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.umeng.IUMShareResultListener;
import com.haiqiu.miaohi.umeng.ShareImg;
import com.haiqiu.miaohi.umeng.UmengShare;
import com.haiqiu.miaohi.utils.AbstractTextUtil;
import com.haiqiu.miaohi.utils.AnimateFirstDisplayListener;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHContentSyncUtil;
import com.haiqiu.miaohi.utils.MHStateSyncUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.NoDoubleClickUtils;
import com.haiqiu.miaohi.utils.PraisedUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.TextUtil;
import com.haiqiu.miaohi.utils.TimeFormatUtils;
import com.haiqiu.miaohi.utils.shareImg.SharePersonalHomeImgView;
import com.haiqiu.miaohi.utils.shareImg.ShareVideoAndImgView2;
import com.haiqiu.miaohi.view.ShareDialog;
import com.haiqiu.miaohi.view.picturezoom.OnShowPicture;
import com.haiqiu.miaohi.widget.KeyBoardView;
import com.haiqiu.miaohi.widget.PraiseImageView;
import com.haiqiu.miaohi.widget.ShareLayout;
import com.haiqiu.miaohi.widget.mediaplayer.BaseVideoView;
import com.haiqiu.miaohi.widget.mediaplayer.MyMediaPlayer;
import com.haiqiu.miaohi.widget.mediaplayer.VideoView;
import com.haiqiu.miaohi.widget.mediaplayer.VideoViewWrap;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的列表适配器
 * Created by ningl on 16/12/5.
 */
public class MineListAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<UserWork> data;
    private KeyBoardView.OnStartCommentListener onStartCommentListener;
    private String uploadUserName;
    private ShareVideoAndImgView2 svaiv_personalhome;
    private static final int EMPTY_OTHER = -1;  //其他人的个人主页
    private static final int EMPTY_MINE = 0;    //我的个人主页
    private static final int VIDEO = 1;         //视频
    private static final int IMG = 2;           //图片
    private static final int GRIDIMG = 3;       //网格图片

    private static final int GRID = 0;          //网格布局
    private static final int LIST = 1;          //列表布局
    private int listStyle;                      //列表样式(网格或者列表)
    private MyMediaPlayer mediaPlayer;
    private VideoView lastVideoView;
    private int lastPlayPosition = -1;
    private int lastPraisePosition;
    private OnSkipVideoAndImgDetail onSkipVideoAndImgDetail;
    private OnShowPicture onShowPicture;
    private boolean isMyself;
    private Fragment fragment;
    private IDeleteAll iDeleteAll;
    private String headerUrl;

    public MineListAdapter(Fragment fragment, ArrayList<UserWork> data, int listStyle, MyMediaPlayer mediaPlayer, KeyBoardView.OnStartCommentListener onStartCommentListener) {
        this.context = fragment.getActivity();
        this.fragment = fragment;
        this.data = data;
        this.listStyle = listStyle;
        this.mediaPlayer = mediaPlayer;
        this.onStartCommentListener = onStartCommentListener;
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case EMPTY_OTHER://空数据页
            case EMPTY_MINE:
                //开启你的第一次秒嗨之旅
                ((MineFragment) fragment).setEnptyView(true);
                return new EmptyViewHolder(View.inflate(context, R.layout.item_mineempty, null));
            case GRIDIMG://网格布局
                ((MineFragment) fragment).setEnptyView(false);
                return new GridViewHolder(View.inflate(context, R.layout.item_minelist, null));
            case VIDEO://视频
                ((MineFragment) fragment).setEnptyView(false);
                return new VideoViewHolder(View.inflate(context, R.layout.item_content_video, null));
            case IMG://图片
                ((MineFragment) fragment).setEnptyView(false);
                return new ImgViewHolder(View.inflate(context, R.layout.item_content_img, null));
        }
        return null;
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        switch (type) {
            case EMPTY_OTHER://空数据页
            case EMPTY_MINE:
                EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
                LinearLayout.LayoutParams emptyLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                emptyLp.width = ScreenUtils.getScreenWidth(context);
                emptyViewHolder.itemView.setLayoutParams(emptyLp);
                if (!data.isEmpty() && data.get(0).isLoading()) {
                    emptyViewHolder.tv_mineempty.setVisibility(View.GONE);
                    emptyViewHolder.tv_minestartmiaohi.setVisibility(View.GONE);
                } else {
                    if (isMyself) {
                        emptyViewHolder.tv_mineempty.setVisibility(View.GONE);
                        emptyViewHolder.tv_minestartmiaohi.setVisibility(View.VISIBLE);
                    } else {
                        emptyViewHolder.tv_mineempty.setVisibility(View.VISIBLE);
                        emptyViewHolder.tv_minestartmiaohi.setVisibility(View.GONE);
                    }
                }
                emptyViewHolder.tv_minestartmiaohi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!((BaseActivity) context).isLogin(false)) return;
                        TCAgent.onEvent(context, "拍摄总点击数" + ConstantsValue.android);
                        if ("x86".equalsIgnoreCase(Build.CPU_ABI)) {
                            ((BaseActivity) context).showToastAtCenter("您的手机暂不支持");
                            return;
                        }
                        Intent intent = new Intent(context, VideoRecorderActivity.class);
                        context.startActivity(intent);
                        ((BaseActivity) context).overridePendingTransition(0, 0);
                        ((BaseActivity) context).overridePendingTransition(R.anim.slide_bottom_out, 0);
                    }
                });
                break;
            case GRIDIMG://网格布局
                GridViewHolder gridHolder = (GridViewHolder) holder;
                final UserWork userWork = data.get(position);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.width = (ScreenUtils.getScreenWidth(context) - 2) / 3;
                lp.height = (ScreenUtils.getScreenWidth(context) - 2) / 3;
                gridHolder.iv_minelistimg.setLayoutParams(lp);
//                gridHolder.iv_minelistimg.setImageResource(R.color.color_f1);
                String imgurl = null;
                if (userWork.getContent_type() == VIDEO) {
                    imgurl = userWork.getVideo_cover_uri();
                } else {
                    imgurl = userWork.getPhoto_thumb_url();
                }
                ImageLoader.getInstance().displayImage(imgurl, gridHolder.iv_minelistimg, DisplayOptionsUtils.getDefaultMinRectImageOptions(), new AnimateFirstDisplayListener(gridHolder.iv_minelistimg));
                if (userWork.getContent_type() == VIDEO) {
                    gridHolder.iv_minelistisvideo.setVisibility(View.VISIBLE);
                } else if (userWork.getContent_type() == IMG) {
                    gridHolder.iv_minelistisvideo.setVisibility(View.GONE);
                }
                gridHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSkipVideoAndImgDetail != null)
                            onSkipVideoAndImgDetail.onSkip(position);
//                        context.startActivity(new Intent(context, VideoAndImgActivity.class)
//                        .putParcelableArrayListExtra("data", data)
//                        .putExtra("currentIndex", position));
                    }
                });
                break;
            case VIDEO://视频
                final UserWork videoUserWork = data.get(position);
                final VideoViewHolder videoHolder = (VideoViewHolder) holder;
                LinearLayout.LayoutParams videoLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                videoLp.width = ScreenUtils.getScreenWidth(context);
                videoHolder.itemView.setLayoutParams(videoLp);
                //设置播放器尺寸
                double rat = 0;
//                if (videoUserWork.getWidth() == 0) rat = 1;
//                else {
//                    rat = videoUserWork.getHeight() / videoUserWork.getWidth();
//                }
                if (videoUserWork.getWidth() == 0) rat = 1;
                else if (videoUserWork.getWidth() > videoUserWork.getHeight()
                        || (videoUserWork.getHeight() * (3d / 4d)) <= videoUserWork.getWidth()) {
                    rat = videoUserWork.getHeight() / videoUserWork.getWidth();
                } else if ((videoUserWork.getHeight() * (3d / 4d)) > videoUserWork.getWidth()) {
                    rat = 4d / 3d;
                }
                RelativeLayout.LayoutParams videoViewLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                videoLp.width = ScreenUtils.getScreenWidth(context);
                videoViewLp.height = (int) (ScreenUtils.getScreenWidth(context) * rat);
                videoHolder.vvw_content_video.setLayoutParams(videoViewLp);
                ImageLoader.getInstance().displayImage(videoUserWork.getVideo_cover_uri(), videoHolder.videoView.getPreviewImageView()
                        , DisplayOptionsUtils.getDefaultMaxRectImageOptions(), new AnimateFirstDisplayListener(videoHolder.videoView.getPreviewImageView())); //视频封面
                videoHolder.position = position;
                videoHolder.tv_attentioncontent_describe.setText(videoUserWork.getVideo_note());
                videoHolder.tv_attentioncontent_describe.setText(TextUtil.getInstance().handlerString(videoUserWork.getVideo_note(), videoUserWork.getNotify_user_result(), new AbstractTextUtil() {
                    @Override
                    public void onClickSpan(TextInfo textInfo) {
                        context.startActivity(new Intent(context, PersonalHomeActivity.class)
                                .putExtra("userId", textInfo.getTarget()));
                    }
                }));
                videoHolder.videoView.reset(position);
                List<Comment> videoComments = videoUserWork.getComment_list();
                if (videoComments.isEmpty()) {
                    videoHolder.ll_attentioncontent_conent.setVisibility(View.GONE);
                } else {
                    //与同步池中的数据比较
                    List<VideoDetailUserCommentBean> list = MHContentSyncUtil.
                            getContentSync(videoUserWork.getVideo_id());
                    if (list != null && list.size() > videoComments.size()) {
                        int targetNum = list.size();
                        videoComments.clear();
                        if (targetNum > 0) {
                            if (targetNum > 3) targetNum = 3;
                            for (int a = 0; a < targetNum; a++) {
                                Comment comment = new Comment();
                                comment.setComment_id(list.get(a).getComment_id());
                                comment.setComment_text(list.get(a).getComment_text());
                                comment.setComment_user_id(list.get(a).getComment_user_id());
                                comment.setComment_user_name(list.get(a).getComment_user_name());
                                comment.setNotify_user_result(list.get(a).getNotify_user_result());
                                videoComments.add(comment);
                            }
                        }
                    }
                    videoHolder.ll_attentioncontent_conent.setVisibility(videoComments.size() > 0 ? View.VISIBLE : View.GONE);
                    //为底部的三条评论赋值
                    for (int i = 0; i < videoHolder.tv_commonts.size(); i++) {
                        if (i + 1 > videoComments.size()) {
                            videoHolder.tv_commonts.get(i).setVisibility(View.GONE);
                        } else {
                            videoHolder.ll_attentioncontent_conent.setVisibility(View.VISIBLE);
                            videoHolder.tv_commonts.get(i).setVisibility(View.VISIBLE);
                            String text = videoComments.get(i).getComment_user_name() + "    " + videoComments.get(i).getComment_text();
                            videoHolder.tv_commonts.get(i).setMovementMethod(LinkMovementMethod.getInstance());
                            videoHolder.tv_commonts.get(i).setText(TextUtil.getInstance().getCommontSpan(videoComments.get(i), new AbstractTextUtil() {
                                @Override
                                public void onClickSpan(TextInfo textInfo) {
                                    context.startActivity(new Intent(context, PersonalHomeActivity.class)
                                            .putExtra("userId", textInfo.getTarget()));
                                }
                            }));
                        }
                    }
                }

                if (videoComments.size() >= 3 && videoUserWork.getComments_count() > 3) {
                    videoHolder.tv_attentioncontent_commenttotalcount.setVisibility(View.VISIBLE);
                } else {
                    videoHolder.tv_attentioncontent_commenttotalcount.setVisibility(View.GONE);
                }
                videoHolder.tv_attentioncontent_commenttotalcount.setText("所有" + videoUserWork.getComments_count() + "条评论");
                if (videoUserWork.getPraiseSrcCount() == 0) {
                    videoHolder.tv_attentioncontent_praisecount.setVisibility(View.GONE);
                } else {
                    videoHolder.tv_attentioncontent_praisecount.setVisibility(View.VISIBLE);
                }
                videoHolder.tv_attentioncontent_praisecount.setText(videoUserWork.getPraise_count());

                videoHolder.tv_attentioncontent_commenttotalcount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSkipVideoAndImgDetail != null)
                            onSkipVideoAndImgDetail.onSkip(position);
                    }
                });
                MHStateSyncUtil.State attentionState = MHStateSyncUtil.getSyncState(videoUserWork.getVideo_id());
                if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != attentionState) {
                    videoUserWork.setPraise_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == attentionState);
                }

                videoHolder.iv_attentcontent_praise.setSelected(videoUserWork.isPraise_state());
                //点击空白区域
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSkipVideoAndImgDetail != null)
                            onSkipVideoAndImgDetail.onSkip(position);
                    }
                });
                videoHolder.iv_attentcontent_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareVideoAndImg(((BaseActivity) context), position);
                    }
                });
                videoHolder.videoView.setPraiseState(videoUserWork.isPraising());
                videoHolder.tv_attentcontent_time.setText(videoUserWork.getUpload_time_text());
                videoHolder.videoView.setVideoControlListener(new BaseVideoView.VideoControlListener() {
                    @Override
                    public void videoViewState(BaseVideoView.VideoViewState videoViewState, int extra) {
                        switch (videoViewState) {
                            case ON_START_PLAY_CLICK:
                                if (null != lastVideoView)
                                    lastVideoView.reset(position);
                                lastVideoView = videoHolder.videoView;
                                lastPlayPosition = position;
                                VideoInfo videoInfo = new VideoInfo();
                                videoInfo.setVideo_id(videoUserWork.getVideo_id());
                                videoInfo.setVideo_uri(videoUserWork.getVideo_uri());
                                videoInfo.setVideo_state(videoUserWork.getVideo_state());
                                videoHolder.videoView.startPlay(mediaPlayer, videoInfo);
                                break;
                            case ON_DOUBLE_CLICK:
                                if (null != data.get(position) && !data.get(position).isPraise_state())
                                    videoPrise(position, videoHolder.iv_attentcontent_praise, videoHolder.tv_attentioncontent_praisecount, videoHolder.videoView);
                                break;
                            case ON_VIDEO_COMPLETE:
                                videoHolder.videoView.setPraiseState(data.get(position).isPraise_state());
                                break;
                        }
                    }
                });
                videoHolder.videoView.setOnPraiseListener(new VideoView.OnPraiseListener() {
                    @Override
                    public void onPraiseClick(View view, boolean isPraise) {
                        videoPrise(position, videoHolder.iv_attentcontent_praise, videoHolder.tv_attentioncontent_praisecount, videoHolder.videoView);
                    }
                });

                VideoExtraInfo videoExtraInfo = new VideoExtraInfo();
                videoExtraInfo.videoId = videoUserWork.getVideo_id();
                videoExtraInfo.playNum = videoUserWork.getPlay_total();
                videoExtraInfo.videoDuration = videoUserWork.getDuration_second();
                videoExtraInfo.setVideoType(VideoExtraInfo.VideoType.VIDEO_TYPE_COMMON);
                videoExtraInfo.subjectName = videoUserWork.getActivity_name();
                videoExtraInfo.subjectUri = videoUserWork.getActivity_uri();
                videoExtraInfo.position = position;
                videoHolder.videoView.setVideoExtraInfo(videoExtraInfo);
                break;
            case IMG://图片
                final UserWork imgUserWork = data.get(position);
                final ImgViewHolder imgHolder = (ImgViewHolder) holder;
                imgHolder.position = position;
                LinearLayout.LayoutParams imgLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imgLp.width = ScreenUtils.getScreenWidth(context);
                imgHolder.itemView.setLayoutParams(imgLp);
                //设置图片尺寸
                double imgRat = 0;
                if (imgUserWork.getWidth() == 0) imgRat = 1;
                else {
                    imgRat = imgUserWork.getHeight() / imgUserWork.getWidth();
                }
                RelativeLayout.LayoutParams imageViewLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageViewLp.width = ScreenUtils.getScreenWidth(context);
                imageViewLp.height = (int) (ScreenUtils.getScreenWidth(context) * imgRat);
                imgHolder.iv_content_img.setLayoutParams(imageViewLp);
                ImageLoader.getInstance().displayImage(imgUserWork.getPhoto_uri(), imgHolder.iv_content_img.getImageView()
                        , DisplayOptionsUtils.getDefaultMaxRectImageOptions(), new AnimateFirstDisplayListener(imgHolder.iv_content_img.getImageView()));
                imgHolder.tv_attentioncontent_describe.setText(TextUtil.getInstance().handlerString(imgUserWork.getPhoto_note(), imgUserWork.getNotify_user_result(), new AbstractTextUtil() {
                    @Override
                    public void onClickSpan(TextInfo textInfo) {
                        context.startActivity(new Intent(context, PersonalHomeActivity.class)
                                .putExtra("userId", textInfo.getTarget()));
                    }
                }));
                List<Comment> comments = imgUserWork.getComment_list();
                if (comments.isEmpty())
                    imgHolder.ll_attentioncontent_conent.setVisibility(View.GONE);
                else {
                    //与同步池中的数据比较
                    List<VideoDetailUserCommentBean> list = MHContentSyncUtil.
                            getContentSync(imgUserWork.getPhoto_id());
                    if (list != null && list.size() > comments.size()) {
                        int targetNum = list.size();
                        comments.clear();
                        if (targetNum > 0) {
                            if (targetNum > 3) targetNum = 3;
                            for (int a = 0; a < targetNum; a++) {
                                Comment comment = new Comment();
                                comment.setComment_id(list.get(a).getComment_id());
                                comment.setComment_text(list.get(a).getComment_text());
                                comment.setComment_user_id(list.get(a).getComment_user_id());
                                comment.setComment_user_name(list.get(a).getComment_user_name());
                                comment.setNotify_user_result(list.get(a).getNotify_user_result());
                                comments.add(comment);
                            }
                        }
                    }
                    imgHolder.ll_attentioncontent_conent.setVisibility(comments.size() > 0 ? View.VISIBLE : View.GONE);

                    for (int i = 0; i < imgHolder.tv_commonts.size(); i++) {
                        if (i + 1 > comments.size()) {
                            imgHolder.tv_commonts.get(i).setVisibility(View.GONE);
                        } else {
                            imgHolder.ll_attentioncontent_conent.setVisibility(View.VISIBLE);
                            imgHolder.tv_commonts.get(i).setVisibility(View.VISIBLE);
                            String text = comments.get(i).getComment_user_name() + context.getResources().getString(R.string.space) + comments.get(i).getComment_text();
                            imgHolder.tv_commonts.get(i).setText(TextUtil.getInstance().getCommontSpan(comments.get(i), new AbstractTextUtil() {
                                @Override
                                public void onClickSpan(TextInfo textInfo) {
                                    context.startActivity(new Intent(context, PersonalHomeActivity.class)
                                            .putExtra("userId", textInfo.getTarget()));
                                }
                            }));
                        }
                    }
                }

                if (comments.size() == 3 && imgUserWork.getComments_count() > 3) {
                    imgHolder.tv_attentioncontent_commenttotalcount.setVisibility(View.VISIBLE);
                } else {
                    imgHolder.tv_attentioncontent_commenttotalcount.setVisibility(View.GONE);
                }
                imgHolder.tv_attentioncontent_commenttotalcount.setText("所有" + imgUserWork.getComments_count() + "条评论");
                if (imgUserWork.getPraiseSrcCount() == 0) {
                    imgHolder.tv_attentioncontent_praisecount.setVisibility(View.GONE);
                } else {
                    imgHolder.tv_attentioncontent_praisecount.setVisibility(View.VISIBLE);
                }
                imgHolder.tv_attentioncontent_praisecount.setText(imgUserWork.getPraise_count());
                imgHolder.tv_attentcontent_time.setText(imgUserWork.getUpload_time_text());
                imgHolder.tv_attentioncontent_commenttotalcount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        context.startActivity(new Intent(context, VideoAndImgActivity.class)
//                                .putParcelableArrayListExtra("data", data)
//                                .putExtra("currentIndex", position));
                        if (onSkipVideoAndImgDetail != null)
                            onSkipVideoAndImgDetail.onSkip(position);
                    }
                });
                MHStateSyncUtil.State attentionStateP = MHStateSyncUtil.getSyncState(imgUserWork.getPhoto_id());
                if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != attentionStateP) {
                    imgUserWork.setPraise_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == attentionStateP);
                }
                imgHolder.iv_attentcontent_praise.setSelected(imgUserWork.isPraise_state());
                //点击图片
                imgHolder.iv_content_img.setOnPraiseImageViewClick(new PraiseImageView.PraiseImageViewClickListener() {
                    @Override
                    public void onSingleTap(View view) {
                        if (onShowPicture != null)
                            onShowPicture.onShowPicture(imgUserWork.getPhoto_uri());
                    }

                    @Override
                    public void onDoubleTap(View view) {
                        if (!imgUserWork.isPraise_state()) {
                            videoPrise(position, imgHolder.iv_attentcontent_praise, imgHolder.tv_attentioncontent_praisecount, null);
                        }
                    }
                });
                //点击空白区域
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSkipVideoAndImgDetail != null)
                            onSkipVideoAndImgDetail.onSkip(position);
                    }
                });
                imgHolder.iv_attentcontent_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareVideoAndImg(((BaseActivity) context), position);
                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public long getItemId(int position) {
        if (null != data && data.size() > position)
            return data.get(position).hashCode();
        return super.getItemId(position);
    }


    /**
     * 网格
     */
    public class GridViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_minelistimg;
        public ImageView iv_minelistisvideo;

        public GridViewHolder(View itemView) {
            super(itemView);
            iv_minelistimg = (ImageView) itemView.findViewById(R.id.iv_minelistimg);
            iv_minelistisvideo = (ImageView) itemView.findViewById(R.id.iv_minelistisvideo);
        }
    }

    /**
     * 空数据
     */
    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_minestartmiaohi;
        private TextView tv_mineempty;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            tv_minestartmiaohi = (TextView) itemView.findViewById(R.id.tv_minestartmiaohi);
            tv_mineempty = (TextView) itemView.findViewById(R.id.tv_mineempty);
        }
    }

    /**
     * 视频
     */
    private class VideoViewHolder extends RecyclerView.ViewHolder {
        private VideoViewWrap vvw_content_video;
        private ImageView iv_attentcontent_praise;
        private ImageView iv_attentcontent_comment;
        private ImageView iv_attentcontent_share;
        private TextView tv_attentcontent_time;
        private TextView tv_attentioncontent_describe;
        private TextView tv_attentioncontent_praisecount;
        private LinearLayout ll_attentioncontent_conent;
        private TextView tv_attentioncontent_commenttotalcount;
        private TextView tv_commont1, tv_commont2, tv_commont3;
        private List<TextView> tv_commonts;
        private VideoView videoView;
        private int position;

        public VideoViewHolder(View itemView) {
            super(itemView);
            iv_attentcontent_praise = (ImageView) itemView.findViewById(R.id.iv_attentcontent_praise);
            iv_attentcontent_comment = (ImageView) itemView.findViewById(R.id.iv_attentcontent_comment);
            iv_attentcontent_share = (ImageView) itemView.findViewById(R.id.iv_attentcontent_share);
            tv_attentcontent_time = (TextView) itemView.findViewById(R.id.tv_attentcontent_time);
            tv_attentioncontent_describe = (TextView) itemView.findViewById(R.id.tv_attentioncontent_describe);
            tv_attentioncontent_praisecount = (TextView) itemView.findViewById(R.id.tv_attentioncontent_praisecount);
            tv_attentioncontent_commenttotalcount = (TextView) itemView.findViewById(R.id.tv_attentioncontent_commenttotalcount);
            ll_attentioncontent_conent = (LinearLayout) itemView.findViewById(R.id.ll_attentioncontent_conent);
            vvw_content_video = (VideoViewWrap) itemView.findViewById(R.id.vvw_content_video);
            tv_commont1 = (TextView) itemView.findViewById(R.id.tv_commont1);
            tv_commont2 = (TextView) itemView.findViewById(R.id.tv_commont2);
            tv_commont3 = (TextView) itemView.findViewById(R.id.tv_commont3);
            tv_commonts = new ArrayList<>();
            tv_commonts.add(tv_commont1);
            tv_commonts.add(tv_commont2);
            tv_commonts.add(tv_commont3);
            videoView = (VideoView) vvw_content_video.getVideoView();
            final UserWork pageResult = data.get(position);
            iv_attentcontent_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoPrise(position, iv_attentcontent_praise, tv_attentioncontent_praisecount, videoView);
                    if (!iv_attentcontent_praise.isSelected())
                        PraisedUtil.showPop(iv_attentcontent_praise, context, true, iv_attentcontent_praise.getWidth());
                }
            });

            iv_attentcontent_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onStartCommentListener != null)
                        onStartCommentListener.onStartCommont(position);
                }
            });
        }
    }

    /**
     * 图片
     */
    private class ImgViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_attentcontent_praise;
        private ImageView iv_attentcontent_comment;
        private ImageView iv_attentcontent_share;
        private TextView tv_attentcontent_time;
        private TextView tv_attentioncontent_describe;
        private TextView tv_attentioncontent_praisecount;
        private LinearLayout ll_attentioncontent_conent;
        private TextView tv_attentioncontent_commenttotalcount;
        private TextView tv_commont1, tv_commont2, tv_commont3;
        private List<TextView> tv_commonts;
        private PraiseImageView iv_content_img;
        private int position;

        public ImgViewHolder(View itemView) {
            super(itemView);
            iv_attentcontent_praise = (ImageView) itemView.findViewById(R.id.iv_attentcontent_praise);
            iv_attentcontent_comment = (ImageView) itemView.findViewById(R.id.iv_attentcontent_comment);
            iv_attentcontent_share = (ImageView) itemView.findViewById(R.id.iv_attentcontent_share);
            tv_attentcontent_time = (TextView) itemView.findViewById(R.id.tv_attentcontent_time);
            tv_attentioncontent_describe = (TextView) itemView.findViewById(R.id.tv_attentioncontent_describe);
            tv_attentioncontent_praisecount = (TextView) itemView.findViewById(R.id.tv_attentioncontent_praisecount);
            tv_attentioncontent_commenttotalcount = (TextView) itemView.findViewById(R.id.tv_attentioncontent_commenttotalcount);
            ll_attentioncontent_conent = (LinearLayout) itemView.findViewById(R.id.ll_attentioncontent_conent);
            iv_content_img = (PraiseImageView) itemView.findViewById(R.id.iv_content_img);
            tv_commont1 = (TextView) itemView.findViewById(R.id.tv_commont1);
            tv_commont2 = (TextView) itemView.findViewById(R.id.tv_commont2);
            tv_commont3 = (TextView) itemView.findViewById(R.id.tv_commont3);
            tv_commonts = new ArrayList<>();
            tv_commonts.add(tv_commont1);
            tv_commonts.add(tv_commont2);
            tv_commonts.add(tv_commont3);

            iv_attentcontent_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastPlayPosition = position;
                    videoPrise(position, iv_attentcontent_praise, tv_attentioncontent_praisecount, null);
                    if (!iv_attentcontent_praise.isSelected())
                        PraisedUtil.showPop(iv_attentcontent_praise, context, true, iv_attentcontent_praise.getWidth());

                }
            });
            iv_attentcontent_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onStartCommentListener != null)
                        onStartCommentListener.onStartCommont(position);
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        UserWork userWork = data.get(position);
        if (userWork.getContent_type() == EMPTY_MINE) return EMPTY_MINE;
        if (userWork.getContent_type() == EMPTY_OTHER) return EMPTY_OTHER;
        if (listStyle == GRID) {
            return GRIDIMG;
        } else if (listStyle == LIST) {
            if (userWork.getContent_type() == VIDEO) return VIDEO;
            else if (userWork.getContent_type() == IMG) return IMG;
        }
        return super.getItemViewType(position);
    }

    /**
     * 设置类型
     *
     * @param listStype
     */
    public void setListStyle(int listStype) {
        this.listStyle = listStype;
    }

    public int getLastPlayPosition() {
        return lastPlayPosition;
    }

    /**
     * 点赞
     */
    public void videoPrise(final int position, final ImageView btn, final TextView tv_prise_count, final VideoView videoView) {
        final UserWork userWork = data.get(position);
        MHRequestParams params = new MHRequestParams();
        params.addParams("action_mark", !userWork.isPraise_state() + "");
        String url = null;
        if (userWork.getContent_type() == VIDEO) {
            url = ConstantsValue.Url.PRAISE_VIDEODO;
            params.addParams("video_id", userWork.getVideo_id());
        } else if (userWork.getContent_type() == IMG) {
            url = ConstantsValue.Url.PRAISEPHOTODO;
            params.addParams("photo_id", userWork.getPhoto_id());
        }
        lastPraisePosition = position;
        btn.setEnabled(false);
        userWork.setPraising(true);
        MHHttpClient.getInstance().post(BaseResponse.class, context, url, params, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                NoDoubleClickUtils.setEnable(btn);

                userWork.setPraise_state(!userWork.isPraise_state());
                userWork.setPraise_count(userWork.isPraise_state() ? userWork.getPraiseSrcCount() + 1 : userWork.getPraiseSrcCount() - 1);
                if (userWork.getPraiseSrcCount() == 0) {
                    tv_prise_count.setVisibility(View.GONE);
                } else {
                    tv_prise_count.setVisibility(View.VISIBLE);
                }
                if (lastPraisePosition == position) {//防止网络请求慢导致数据不同步
                    tv_prise_count.setText(userWork.getPraise_count());
                    btn.setEnabled(true);
                    btn.setSelected(userWork.isPraise_state());
                    if (userWork.isPraise_state()) {
                        btn.setImageResource(R.drawable.praise);
                    } else {
                        btn.setImageResource(R.drawable.nopraise);
                    }
                    btn.setSelected(userWork.isPraise_state());
                } else {
                    notifyDataSetChanged();
                }
                if (userWork.getContent_type() == VIDEO) {
                    MHStateSyncUtil.pushSyncEvent(context, userWork.getVideo_id(), userWork.isPraise_state());
                } else if (userWork.getContent_type() == IMG) {
                    MHStateSyncUtil.pushSyncEvent(context, userWork.getPhoto_id(), userWork.isPraise_state());
                }
                if (videoView != null) videoView.setPraiseState(userWork.isPraise_state());
            }

            @Override
            public void onFailure(String content) {
                userWork.setPraising(false);
                btn.setEnabled(true);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                userWork.setPraising(false);
                btn.setEnabled(true);
            }
        });
    }


    public interface OnSkipVideoAndImgDetail {
        void onSkip(int position);
    }

    public void setOnSkipVideoAndImgDetailListener(OnSkipVideoAndImgDetail onSkipVideoAndImgDetail) {
        this.onSkipVideoAndImgDetail = onSkipVideoAndImgDetail;
    }

    public void setOnClickPictureListener(OnShowPicture onShowPicture) {
        this.onShowPicture = onShowPicture;
    }

    /**
     * 设置分享所需的view和上传人昵称
     *
     * @param name
     * @param svaiv_personalhome
     */
    public void setShareData(String name, String headerUrl, ShareVideoAndImgView2 svaiv_personalhome) {
        uploadUserName = name;
        this.headerUrl = headerUrl;
        this.svaiv_personalhome = svaiv_personalhome;
    }

    /**
     * 分享视频或者图片
     *
     * @param activity
     * @param position
     */
    public void shareVideoAndImg(final Activity activity, final int position) {
        final UserWork userWork = data.get(position);
        final ShareDialog shareDialog = new ShareDialog((BaseActivity) activity);
        shareDialog.setData();
        shareDialog.setShareLable(ShareDialog.IMG);
        if (MHStringUtils.isEmpty(userWork.getShare_link_address())) {
            shareDialog.setShareLink(ConstantsValue.Shared.APPDOWNLOAD);
        } else {
            shareDialog.setShareLink(userWork.getShare_link_address());
        }
        ShareImg shareImg = null;
        if (userWork.getContent_type() == VIDEO) {
            shareImg = new ShareImg(((BaseActivity) context), userWork.getVideo_id(), "", "", "", "");
            if (isMyself) {
                //是自己
                shareImg.setDeleteBtnType(ShareLayout.VIDEO_DELETE);
            } else {
                shareImg.setDeleteBtnType(ShareLayout.VIDEO_REPORT);
            }
        } else {
            shareImg = new ShareImg(((BaseActivity) context), userWork.getPhoto_id(), "", "", "", "");
            if (isMyself) {
                //是自己
                shareImg.setDeleteBtnType(ShareLayout.IMG_DELETE);
            } else {
                shareImg.setDeleteBtnType(ShareLayout.IMG_REPORT);
            }
        }
        shareImg.setShowDelete(true);
        shareDialog.setShareInfo(shareImg);
        shareDialog.setOnShareImgPath(new ShareLayout.OnShareImgPath() {
            @Override
            public void getimgPath(final SHARE_MEDIA platform) {
                shareDialog.dismiss();
                ((BaseActivity) context).showLoading();
                ShareVideoAndImgInfo videoAndImgInfo = new ShareVideoAndImgInfo();
                videoAndImgInfo.setHeight(userWork.getHeight());
                videoAndImgInfo.setWidth(userWork.getWidth());
                videoAndImgInfo.setJoinTime(TimeFormatUtils.formatYMD(userWork.getUpload_time()));
                videoAndImgInfo.setName(uploadUserName);
                videoAndImgInfo.setType(userWork.getContent_type());
                videoAndImgInfo.setHeaderUrl(headerUrl);
                if (MHStringUtils.isEmpty(userWork.getShare_link_address()))
                    videoAndImgInfo.setQaCode_str(ConstantsValue.Shared.APPDOWNLOAD);
                else videoAndImgInfo.setQaCode_str(userWork.getShare_link_address());
                if (userWork.getContent_type() == VIDEO) {
                    //视频
                    videoAndImgInfo.setImgUrl(userWork.getVideo_cover_uri());
                    videoAndImgInfo.setNote(userWork.getVideo_note());
                    svaiv_personalhome.setOnLoadFinishListener(new SharePersonalHomeImgView.OnLoadFinish() {
                        @Override
                        public void onFinish(Object path) {
                            UmengShare.sharedIMG(activity, platform, path, userWork.getShare_link_address(), userWork.getVideo_note(), new IUMShareResultListener((BaseActivity) context));
                        }
                    });
                } else {
                    //图片
                    videoAndImgInfo.setImgUrl(userWork.getPhoto_uri());
                    videoAndImgInfo.setNote(userWork.getPhoto_note());
                    svaiv_personalhome.setOnLoadFinishListener(new SharePersonalHomeImgView.OnLoadFinish() {
                        @Override
                        public void onFinish(Object path) {
                            UmengShare.sharedIMG(activity, platform, path, userWork.getShare_link_address(), userWork.getPhoto_note(), new IUMShareResultListener((BaseActivity) context));
                        }
                    });
                }
                svaiv_personalhome.genderImage(videoAndImgInfo, platform);
            }
        });
        //删除自己的作品或者举报别人的作品
        shareDialog.setDeleteOrReportListener(new ShareLayout.OnDeleteOrReportListener() {
            @Override
            public void onDeleteOrReport(int type) {
                if (type == ShareLayout.IMG_DELETE
                        || type == ShareLayout.VIDEO_DELETE) {
                    shareDialog.dismiss();
                    //处理视频/图片删除同步
                    if (data.size() == 1) {
                        if (iDeleteAll != null) iDeleteAll.onDelete();
//                        data.get(position).setContent_type(0);
//                        data.get(position).setLoading(false);
//                        notifyDataSetChanged();
                    }
                    //删除操作
                    DeleteVideoAndImgAsyncEvent event = new DeleteVideoAndImgAsyncEvent();
                    if (userWork.getElement_type() == IMG) {
                        event.setContentType(IMG);
                        event.setTargetId(userWork.getPhoto_id());
                        event.setFromType(ConstantsValue.Other.DELETEVIDEOANDIMG_FROMAPERSONALHOME);
                    } else if (userWork.getElement_type() == VIDEO) {
                        event.setContentType(VIDEO);
                        event.setTargetId(userWork.getVideo_id());
                        event.setFromType(ConstantsValue.Other.DELETEVIDEOANDIMG_FROMAPERSONALHOME);
                    }
                    EventBus.getDefault().post(event);
                }
            }
        });
    }

    /**
     * 是否是当前用户自己
     *
     * @param isMyself
     */
    public void setMyself(boolean isMyself) {
        this.isMyself = isMyself;
    }

    public interface IDeleteAll {
        void onDelete();
    }

    public void setDeleteAllListener(IDeleteAll iDeleteAll) {
        this.iDeleteAll = iDeleteAll;
    }

    public void remove(int position) {
        if (null != data && data.size() > position) {
            data.remove(position);

//            data.remove(position);
//            notifyItemRemoved(position);
//            if (position != (data.size())) { // 如果移除的是最后一个，忽略
//                notifyItemRangeChanged(position, this.data.size() - position);
//            }
        }
        if (null != mediaPlayer && lastPlayPosition == position) {
            mediaPlayer.reset();
        }
    }

}
