package com.haiqiu.miaohi.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.Layout;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.CommonPersonInfo;
import com.haiqiu.miaohi.activity.InterlocutionDetailsActivity;
import com.haiqiu.miaohi.activity.MaybeInterestToPeopleActivity;
import com.haiqiu.miaohi.activity.OnLookersListAvtivity;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.activity.VideoAndImgActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.Attention;
import com.haiqiu.miaohi.bean.Comment;
import com.haiqiu.miaohi.bean.MayBeInterest;
import com.haiqiu.miaohi.bean.ShareVideoAndImgInfo;
import com.haiqiu.miaohi.bean.TextInfo;
import com.haiqiu.miaohi.bean.VideoAndImg;
import com.haiqiu.miaohi.bean.VideoDetailUserCommentBean;
import com.haiqiu.miaohi.bean.VideoExtraInfo;
import com.haiqiu.miaohi.bean.VideoInfo;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.response.HomeFoundResponse;
import com.haiqiu.miaohi.umeng.IUMShareResultListener;
import com.haiqiu.miaohi.umeng.UmengShare;
import com.haiqiu.miaohi.utils.AbstractTextUtil;
import com.haiqiu.miaohi.utils.AnimateFirstDisplayListener;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.DraftUtil;
import com.haiqiu.miaohi.utils.MHContentSyncUtil;
import com.haiqiu.miaohi.utils.MHStateSyncUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.NoDoubleClickUtils;
import com.haiqiu.miaohi.utils.PraisedUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.TextUtil;
import com.haiqiu.miaohi.utils.TimeFormatUtils;
import com.haiqiu.miaohi.utils.ToastUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.utils.shareImg.AbsShareImg;
import com.haiqiu.miaohi.utils.shareImg.SharePersonalHomeImgView;
import com.haiqiu.miaohi.utils.shareImg.ShareQADetailView;
import com.haiqiu.miaohi.utils.shareImg.ShareVideoAndImgView2;
import com.haiqiu.miaohi.utils.upload.UploadService;
import com.haiqiu.miaohi.view.CommonPersonalInfoView;
import com.haiqiu.miaohi.view.ShareDialog;
import com.haiqiu.miaohi.view.picturezoom.OnShowPicture;
import com.haiqiu.miaohi.widget.HeaderAdapterInterface;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 关注页适配器
 * Created by ningl on 16/12/2.
 */
public class AttentionStickyHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements HeaderAdapterInterface {

    private Context context;
    private ArrayList<Attention> data;
    private KeyBoardView.OnStartCommentListener onStartCommentListener;
    private OnShowPicture onShowPicture;
    private OnAttentionDo onAttentionDo;
    public final int VIDEO = 1;
    public final int IMG = 2;
    public final int MAYBEINTEREST = 0;
    public final int QA = 3;
    private int lastPraisePosition;
    public int lastPlayPosition = -1;
    private MyMediaPlayer mediaPlayer;
    public VideoView lastVideoView;
    private ShareVideoAndImgView2 sviv_attention;
    private ShareQADetailView sqdv_attention;
    private LinkedHashMap<String, VideoUploadInfo> uploadTempMap;
    private final int screenWidth;
    private AttentionMaybeInterestAdapter maybeInterestAdapter;
    private boolean isRefresh;


    public AttentionStickyHeaderAdapter(Context context, ArrayList<Attention> data, MyMediaPlayer mediaPlayer, KeyBoardView.OnStartCommentListener onStartCommentListener, OnAttentionDo onAttentionDo) {
        this.context = context;
        this.data = data;
        this.onStartCommentListener = onStartCommentListener;
        this.onAttentionDo = onAttentionDo;
        this.mediaPlayer = mediaPlayer;
        screenWidth = ScreenUtils.getScreenWidth(context);
        setHasStableIds(true);
    }

    public void setRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case IMG:
                holder = new ImgViewHolder(View.inflate(context, R.layout.item_content_img, null));
                break;
            case MAYBEINTEREST:
                holder = new MaybeInterestViewHolder(View.inflate(context, R.layout.item_attention_maybeinterest, null));
                break;
            case QA:
                holder = new QaViewHolder(View.inflate(context, R.layout.item_attention_homeqa, null));
                break;
            default:
                holder = new VideoViewHolder(View.inflate(context, R.layout.item_content_video, null));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIDEO:
                TCAgent.onEvent(context, "视频展现" + ConstantsValue.android);
                if (lastPlayPosition == position) {
                    if (!mediaPlayer.isPlaying()) {
                        handleNomalVideo(position, (VideoViewHolder) holder);
                    }
                } else {
                    handleNomalVideo(position, (VideoViewHolder) holder);
                }
                break;
            case IMG:
                TCAgent.onEvent(context, "图片展现" + ConstantsValue.android);

                handleImg(position, (ImgViewHolder) holder);
                break;
            case MAYBEINTEREST:
                handleMaybeInsterestPeople(position, (MaybeInterestViewHolder) holder);
                break;
            case QA:
                TCAgent.onEvent(context, "映答展现" + ConstantsValue.android);
                if (lastPlayPosition == position) {
                    if (!mediaPlayer.isPlaying()) {
                        handleQaVideo(position, (QaViewHolder) holder);
                    }
                } else {
                    handleQaVideo(position, (QaViewHolder) holder);
                }
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int position) {
        View inflate = View.inflate(context, R.layout.item_attentionheader, null);
        inflate.setLayoutParams(new ViewGroup.LayoutParams(screenWidth, context.getResources().getDimensionPixelSize(R.dimen.person_header_height)));
        return new HeaderViewHolder(inflate);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            final Attention attention = data.get(position);
            setHeaderData(headerHolder.cpiv_attentionheader, attention, position);
        }
    }

    @Override
    public int getHeaderId(int position) {
        if (null == data || data.size() <= position)
            return -1;
        Attention attention = data.get(position);
        String user_id = null;
        if (null != attention) {
            user_id = attention.getUser_id();
            if (attention.getElement_type() == QA) {
                user_id = attention.getAnswer_user_id();
            }
        }
        if (null == user_id) return -1;

        return user_id.hashCode();
    }

    @Override
    public long getItemId(int position) {
        if (null != data && data.size() > position)
            return data.get(position).hashCode();
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getElement_type();
    }


    public void remove(int position) {
        if (null != data && data.size() > position) {
            data.remove(position);
            notifyDataSetChanged();

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

    private void setHeaderData(final CommonPersonalInfoView personalInfoView, final Attention attention, final int position) {
        if (null == personalInfoView || null == attention) return;
        //可能感兴趣的人头部不显示
        if (attention.getElement_type() == MAYBEINTEREST) {
            personalInfoView.setVisibility(View.GONE);
        } else {
            personalInfoView.setVisibility(View.VISIBLE);
        }
        //可能感兴趣的人item头部不做处理
        if (attention.getElement_type() != MAYBEINTEREST) {
            //不是自己显示关注按钮否则不显示
            if (!TextUtils.equals(UserInfoUtil.getUserId(context), attention.getUser_id())) {
                personalInfoView.isShowAttentionBtn(true);
            } else {
                personalInfoView.isShowAttentionBtn(false);
            }
            CommonPersonInfo personInfo = new CommonPersonInfo();
            switch (attention.getElement_type()) {
                case VIDEO:
                    personInfo.setContentType(1);
                case IMG://视频和图片头部处理
                    personInfo.setContentType(2);
                    personInfo.setName(attention.getUser_name());
                    personInfo.setName_nodescribe(attention.getUser_name());
                    personInfo.setUserType(attention.getUser_type());
                    personInfo.setHeadUri(attention.getPortrait_uri());
                    personInfo.setUserId(attention.getUser_id());

                    break;
                case QA://映答头部处理
                    personInfo.setContentType(3);
                    personInfo.setName(attention.getAnswer_user_name());
                    personInfo.setName_nodescribe(attention.getAnswer_user_name());
                    personInfo.setUserType(attention.getAnswer_user_type());
                    personInfo.setHeadUri(attention.getAnswer_user_portrait());
                    personInfo.setUserId(attention.getAnswer_user_id());
                    break;
            }
            personInfo.setDescribe(attention.getUser_authentic());
            personalInfoView.setUserInfo(personInfo);

            MHStateSyncUtil.State state = MHStateSyncUtil.getSyncState(attention.getElement_type() == QA ? attention.getAnswer_user_id() : attention.getUser_id());
            if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND == state) {
                personalInfoView.setAttention(attention.isAttention_state());
            } else {
                attention.setAttention_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == state);
                personalInfoView.setAttention(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == state);
            }
            personalInfoView.isShowDelete(true);
            //处理关注
            personalInfoView.setOnAttentionListener(new CommonPersonalInfoView.OnAttentionListener() {
                @Override
                public void onAttention() {
                    attentionOrCancle(personalInfoView, attention);
                }
            });
            //删除
            personalInfoView.setOnClickDeleteListener(new CommonPersonalInfoView.OnClickDeleteListener() {
                @Override
                public void onClickDelete() {
                    if (isUploadOver(attention)) {
                        if (onAttentionDo != null) onAttentionDo.onDelete(position);
                    }
                }
            });
        }
    }


    /**
     * 处理可能感兴趣的人数据
     *
     * @param psition
     * @param holder
     */
    public void handleMaybeInsterestPeople(final int psition, final MaybeInterestViewHolder holder) {
        final Attention mayBeInterestAttention = data.get(psition);
        final List<MayBeInterest> maybeInterest = mayBeInterestAttention.getAttention_user_list();
        if (maybeInterestAdapter == null || isRefresh) {
            maybeInterestAdapter = new AttentionMaybeInterestAdapter(context, maybeInterest);
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.rv_attention_mabeinterest.setLayoutManager(llm);
            holder.rv_attention_mabeinterest.setAdapter(maybeInterestAdapter);
            isRefresh = false;
        } else {
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.rv_attention_mabeinterest.setLayoutManager(llm);
            holder.rv_attention_mabeinterest.setAdapter(maybeInterestAdapter);
        }
        holder.tv_attention_maybeinterestfindmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MaybeInterestToPeopleActivity.class));
                TCAgent.onEvent(context, "关注推人发现更多" + ConstantsValue.android);
            }
        });
        maybeInterestAdapter.setOnRemoveAllInterestPeopleListener(new AttentionMaybeInterestAdapter.OnRemoveAllInterestPeople() {
            @Override
            public void onRemove() {
                data.remove(psition);
                notifyDataSetChanged();
            }
        });
        TCAgent.onEvent(context, "关注推人展示" + ConstantsValue.android);
        if (maybeInterestAdapter == null) {
        }
    }

    public void notifyDataSetChangedBySelf() {
        super.notifyDataSetChanged();
        notifyDataSetChanged();
    }

    /**
     * 处理视频数据
     *
     * @param position
     * @param holder
     */
    public void handleNomalVideo(final int position, final VideoViewHolder holder) {
        final Attention videoAttention = data.get(position);
        boolean isComeFromUpload = isExitUploading(videoAttention.getVideoSrcPath());
        holder.position = position;
        setHeaderData(holder.video_attentionheader, videoAttention, position);

        boolean isVideoUpload = false;
        if (videoAttention.isShowItemMarsk()) {
            //数据是否来自于上传 来自上传需特殊处理
            isVideoUpload = handleVideoUpload(position, holder);

            holder.iv_attentionshare_wechatcircle.setOnClickListener(new ShareClick(videoAttention, ((Activity) context), SHARE_MEDIA.WEIXIN_CIRCLE));
            holder.iv_attentionshare_wechat.setOnClickListener(new ShareClick(videoAttention, ((Activity) context), SHARE_MEDIA.WEIXIN));
            holder.iv_attentionshare_qq.setOnClickListener(new ShareClick(videoAttention, ((Activity) context), SHARE_MEDIA.QQ));
            holder.iv_attentionshare_qqzone.setOnClickListener(new ShareClick(videoAttention, ((Activity) context), SHARE_MEDIA.QZONE));
            holder.iv_attentionshare_sina.setOnClickListener(new ShareClick(videoAttention, ((Activity) context), SHARE_MEDIA.SINA));
            holder.iv_attentionshare_copylink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(videoAttention.getShare_link_address());
                    ToastUtils.showToastAtCenter(context, "已经复制到剪切板");
                    videoAttention.setShowItemMarsk(false);
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.rl_itemqa_on.setVisibility(View.GONE);
            holder.rl_itemattention_share.setVisibility(View.GONE);
            holder.rl_itemqa_fail.setVisibility(View.GONE);
        }

        final VideoView videoView = (VideoView) holder.vvw_content_video.getVideoView();
        if (!isVideoUpload)
            videoView.reset(position);
        else
            videoView.reset(-1);
        //(height/width) 高和宽的比例
        double rat = 0;
//        if (videoAttention.getWidth() == 0) rat = 1;
//        else {
//            rat = videoAttention.getHeight() / videoAttention.getWidth();
//        }
        if (videoAttention.getWidth() == 0) rat = 1;
        else if (videoAttention.getWidth() > videoAttention.getHeight()
                || (videoAttention.getHeight() * (3d / 4d)) <= videoAttention.getWidth()) {
            rat = videoAttention.getHeight() / videoAttention.getWidth();
        } else if ((videoAttention.getHeight() * (3d / 4d)) > videoAttention.getWidth()) {
            rat = 4d / 3d;
        }
        ViewGroup.LayoutParams layoutParams = holder.rl_video_container.getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = (int) (screenWidth * rat);
        holder.rl_video_container.setLayoutParams(layoutParams);

        MHStateSyncUtil.State syncState = MHStateSyncUtil.getSyncState(videoAttention.getVideo_id());
        if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != syncState) {
            videoAttention.setPraise_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == syncState);
        }

        if (videoAttention.getPraiseSrcCount() == 0) {
            holder.tv_attentioncontent_praisecount.setVisibility(View.GONE);
        } else {
            holder.tv_attentioncontent_praisecount.setVisibility(View.VISIBLE);
        }
        holder.tv_attentioncontent_praisecount.setText(videoAttention.getPraise_count());
        holder.tv_attentcontent_time.setText(videoAttention.getUpload_time_text());
        holder.tv_attentioncontent_describe.setOnTouchListener(new LinkOnTouckListener());
        holder.tv_attentioncontent_describe.setText(TextUtil.getInstance().handlerString(videoAttention.getVideo_note(), videoAttention.getNotify_user_result(), new AbstractTextUtil() {
            @Override
            public void onClickSpan(TextInfo textInfo) {
                context.startActivity(new Intent(context, PersonalHomeActivity.class)
                        .putExtra("userId", textInfo.getTarget()));
            }
        }));
        //添加评论数据
        List<Comment> videoComments = videoAttention.getComment_list();
        if (videoComments != null) {
            //与同步池中的数据比较
            List<VideoDetailUserCommentBean> list = MHContentSyncUtil.
                    getContentSync(videoAttention.getVideo_id());
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
            holder.ll_attentioncontent_conent.setVisibility(videoComments.size() > 0 ? View.VISIBLE : View.GONE);
            for (int i = 0; i < holder.tv_commonts.size(); i++) {
                if (i + 1 > videoComments.size()) {
                    holder.tv_commonts.get(i).setVisibility(View.GONE);
                } else {
                    holder.ll_attentioncontent_conent.setVisibility(View.VISIBLE);
                    holder.tv_commonts.get(i).setVisibility(View.VISIBLE);
                    String text = videoComments.get(i).getComment_user_name() + "  " + videoComments.get(i).getComment_text();
                    holder.tv_commonts.get(i).setText(TextUtil.getInstance().getCommontSpan(videoComments.get(i), new AbstractTextUtil() {
                        @Override
                        public void onClickSpan(TextInfo textInfo) {
                            context.startActivity(new Intent(context, PersonalHomeActivity.class)
                                    .putExtra("userId", textInfo.getTarget()));
                        }
                    }));
                }
            }
            if (videoComments.size() >= 3 && videoAttention.getComments_count() > 3) {
                holder.tv_attentioncontent_commenttotalcount.setVisibility(View.VISIBLE);
            } else {
                holder.tv_attentioncontent_commenttotalcount.setVisibility(View.GONE);
            }
        } else {
            videoAttention.setComment_list(new ArrayList<Comment>());
            holder.ll_attentioncontent_conent.setVisibility(View.GONE);
        }
        holder.tv_attentioncontent_commenttotalcount.setText("所有" + videoAttention.getComments_count() + "条评论");
        holder.tv_attentioncontent_praisecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, OnLookersListAvtivity.class)
                        .putExtra("type", videoAttention.getElement_type() + "")
                        .putExtra("video_id", videoAttention.getVideo_id())
                        .putExtra("photo_id", videoAttention.getPhoto_id()));
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUploadOver(videoAttention)) {
                    ArrayList<VideoAndImg> videoAndImgs = new ArrayList<VideoAndImg>();
                    videoAndImgs.add(videoAttention);
                    context.startActivity(new Intent(context, VideoAndImgActivity.class)
                            .putParcelableArrayListExtra("data", videoAndImgs)
                            .putExtra("isFromCommentList", true));
                }
            }
        };
        holder.tv_attentioncontent_commenttotalcount.setOnClickListener(onClickListener);
        holder.ll_attentioncontent_conent.setOnClickListener(onClickListener);
        holder.tv_commont1.setOnClickListener(onClickListener);
        holder.tv_commont2.setOnClickListener(onClickListener);
        holder.tv_commont3.setOnClickListener(onClickListener);
//        String
        //处理上传过程中无网络图片则加载本地图片
        if (MHStringUtils.isEmpty(videoAttention.getVideo_cover_uri())) {
            //无网络图片
            ImageLoader.getInstance().displayImage("file://" + videoAttention.getLocalCoverPath(), videoView.getPreviewImageView(), new AnimateFirstDisplayListener(videoView.getPreviewImageView()));
        } else {
            //有网络图片
            ImageLoader.getInstance().displayImage(videoAttention.getVideo_cover_uri(), videoView.getPreviewImageView(), DisplayOptionsUtils.getDefaultMaxRectImageOptions(), new AnimateFirstDisplayListener(videoView.getPreviewImageView())); //视频封面
        }
        holder.iv_attentcontent_praise.setSelected(videoAttention.isPraise_state());
        holder.videoView.setPraiseState(videoAttention.isPraise_state());

        VideoExtraInfo videoExtraInfo = new VideoExtraInfo();
        videoExtraInfo.videoId = videoAttention.getVideo_id();
        videoExtraInfo.playNum = videoAttention.getPlay_total();
        videoExtraInfo.videoDuration = videoAttention.getDuration_second();
        videoExtraInfo.setVideoType(VideoExtraInfo.VideoType.VIDEO_TYPE_COMMON);
        videoExtraInfo.subjectName = videoAttention.getActivity_name();
        videoExtraInfo.subjectUri = videoAttention.getActivity_uri();
        videoExtraInfo.recommendStr = videoAttention.getRecommend_text();
        videoExtraInfo.position = position;
        videoView.setVideoExtraInfo(videoExtraInfo);
    }

    /**
     * 处理图片数据
     *
     * @param position
     * @param holder
     */
    public void handleImg(final int position, final ImgViewHolder holder) {
        //处理分享
        final Attention imgAttention = data.get(position);
        setHeaderData(holder.imag_attentionheader, imgAttention, position);

        holder.position = position;
        boolean isComeFromUpload = isExitUploading(imgAttention.getPhotoSrcPath());
        if (imgAttention.isShowItemMarsk()) {//数据是否来自于上传 来自上传需特殊处理
            handleImgUpload(position, holder);
            holder.iv_attentionshare_wechatcircle.setOnClickListener(new ShareClick(imgAttention, ((Activity) context), SHARE_MEDIA.WEIXIN_CIRCLE));
            holder.iv_attentionshare_wechat.setOnClickListener(new ShareClick(imgAttention, ((Activity) context), SHARE_MEDIA.WEIXIN));
            holder.iv_attentionshare_qq.setOnClickListener(new ShareClick(imgAttention, ((Activity) context), SHARE_MEDIA.QQ));
            holder.iv_attentionshare_qqzone.setOnClickListener(new ShareClick(imgAttention, ((Activity) context), SHARE_MEDIA.QZONE));
            holder.iv_attentionshare_sina.setOnClickListener(new ShareClick(imgAttention, ((Activity) context), SHARE_MEDIA.SINA));
            holder.iv_attentionshare_copylink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(imgAttention.getShare_link_address());
                    ToastUtils.showToastAtCenter(context, "已经复制到剪切板");
                    imgAttention.setShowItemMarsk(false);
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.rl_itemqa_on.setVisibility(View.GONE);
            holder.rl_itemattention_share.setVisibility(View.GONE);
            holder.rl_itemqa_fail.setVisibility(View.GONE);
        }
        holder.iv_attentcontent_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUploadOver(imgAttention)) {
                    lastPraisePosition = position;
                    if (!imgAttention.isPraise_state()) {
                        PraisedUtil.showPop(holder.iv_attentcontent_praise, context, true, holder.iv_attentcontent_praise.getWidth());
                    }
                    videoPrise(position, lastPraisePosition, holder.iv_attentcontent_praise, holder.tv_attentioncontent_praisecount);
                }
                TCAgent.onEvent(context, "图片赞" + ConstantsValue.android);
            }
        });
        holder.iv_attentcontent_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUploadOver(imgAttention)) {
                    if (onStartCommentListener != null)
                        onStartCommentListener.onStartCommont(position);
                }
            }
        });
        double rat = 0;
        if (imgAttention.getWidth() == 0) rat = 1;
        else {
            rat = imgAttention.getHeight() / imgAttention.getWidth();
        }
        RelativeLayout.LayoutParams videoLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        videoLp.width = ScreenUtils.getScreenWidth(context);
        videoLp.height = (int) (ScreenUtils.getScreenWidth(context) * rat);
        holder.iv_content_img.setLayoutParams(videoLp);
        //设置图片主题和推荐
        holder.iv_content_img.setSubject(imgAttention.getActivity_name(), imgAttention.getActivity_uri());
        holder.iv_content_img.setRecommend(imgAttention.getRecommend_text());
        //上传时无网络图片则使用本地图片
        if (imgAttention.getPhoto_uri() == null) {
            ImageLoader.getInstance().displayImage("file://" + imgAttention.getLocalPicturePath(), holder.iv_content_img.getImageView(), new AnimateFirstDisplayListener(holder.iv_content_img.getImageView()));
        } else {
            ImageLoader.getInstance().displayImage(imgAttention.getPhoto_uri(), holder.iv_content_img.getImageView(), DisplayOptionsUtils.getDefaultMaxRectImageOptions(), new AnimateFirstDisplayListener(holder.iv_content_img.getImageView()));
        }

        MHStateSyncUtil.State syncState = MHStateSyncUtil.getSyncState(imgAttention.getPhoto_id());
        if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != syncState) {
            imgAttention.setPraise_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == syncState);
        }
        holder.iv_attentcontent_praise.setSelected(imgAttention.isPraise_state());
        if (imgAttention.getPraiseSrcCount() == 0) {
            holder.tv_attentioncontent_praisecount.setVisibility(View.GONE);
        } else {
            holder.tv_attentioncontent_praisecount.setVisibility(View.VISIBLE);
        }
        holder.tv_attentioncontent_praisecount.setText(imgAttention.getPraise_count());
        holder.tv_attentcontent_time.setText(imgAttention.getUpload_time_text());
        holder.tv_attentioncontent_describe.setOnTouchListener(new LinkOnTouckListener());
        holder.tv_attentioncontent_describe.setText(TextUtil.getInstance().handlerString(imgAttention.getPhoto_note(), imgAttention.getNotify_user_result(), new AbstractTextUtil() {
            @Override
            public void onClickSpan(TextInfo textInfo) {
                if (isUploadOver(imgAttention)) {
                    context.startActivity(new Intent(context, PersonalHomeActivity.class)
                            .putExtra("userId", textInfo.getTarget()));
                }
            }
        }));
        //添加评论数据
        List<Comment> videoComments = imgAttention.getComment_list();
        if (videoComments == null) {
            videoComments = new ArrayList<>();
            imgAttention.setComment_list(videoComments);
        }
        if (videoComments != null) {
            //与同步池中的数据比较
            List<VideoDetailUserCommentBean> list = MHContentSyncUtil.
                    getContentSync(imgAttention.getPhoto_id());
            if (list != null&& list.size() > videoComments.size()) {
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

            for (int i = 0; i < holder.tv_commonts.size(); i++) {
                if (i + 1 > videoComments.size()) {
                    holder.tv_commonts.get(i).setVisibility(View.GONE);
                } else {
                    holder.ll_attentioncontent_conent.setVisibility(View.VISIBLE);
                    holder.tv_commonts.get(i).setVisibility(View.VISIBLE);
                    holder.tv_commonts.get(i).setMaxLines(2);
                    holder.tv_commonts.get(i).setEllipsize(TextUtils.TruncateAt.END);

                    String text = videoComments.get(i).getComment_user_name() + "  " + videoComments.get(i).getComment_text();
//                        holder.tv_commonts.get(i).setMovementMethod(LinkMovementMethod.getInstance());
                    holder.tv_commonts.get(i).setText(TextUtil.getInstance().getCommontSpan(videoComments.get(i), new AbstractTextUtil() {
                        @Override
                        public void onClickSpan(TextInfo textInfo) {
                            context.startActivity(new Intent(context, PersonalHomeActivity.class)
                                    .putExtra("userId", textInfo.getTarget()));
                        }
                    }));
                }
            }

        } else {
            holder.ll_attentioncontent_conent.setVisibility(View.GONE);
            imgAttention.setComment_list(new ArrayList<Comment>());
        }

        if (videoComments.size() >= 3 && imgAttention.getComments_count() > 3) {
            holder.tv_attentioncontent_commenttotalcount.setVisibility(View.VISIBLE);
        } else {
            holder.tv_attentioncontent_commenttotalcount.setVisibility(View.GONE);
        }
        holder.tv_attentioncontent_commenttotalcount.setText("所有" + imgAttention.getComments_count() + "条评论");
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUploadOver(imgAttention)) {
                    ArrayList<VideoAndImg> videoAndImgs = new ArrayList<VideoAndImg>();
                    videoAndImgs.add(imgAttention);
                    context.startActivity(new Intent(context, VideoAndImgActivity.class)
                            .putParcelableArrayListExtra("data", videoAndImgs)
                            .putExtra("isFromCommentList", true));
                }
            }
        };
        holder.tv_attentioncontent_commenttotalcount.setOnClickListener(onClickListener);
        holder.ll_attentioncontent_conent.setOnClickListener(onClickListener);
        holder.tv_commont1.setOnClickListener(onClickListener);
        holder.tv_commont2.setOnClickListener(onClickListener);
        holder.tv_commont3.setOnClickListener(onClickListener);

        //点击图片
        holder.iv_content_img.setOnPraiseImageViewClick(new PraiseImageView.PraiseImageViewClickListener() {
            @Override
            public void onSingleTap(View view) {
                if (onShowPicture != null) onShowPicture.onShowPicture(imgAttention.getPhoto_uri());
            }

            @Override
            public void onDoubleTap(View view) {
                TCAgent.onEvent(context, "图片双击赞" + ConstantsValue.android);
                if (!imgAttention.isPraise_state())
                    videoPrise(position, lastPraisePosition, holder.iv_attentcontent_praise, holder.tv_attentioncontent_praisecount);
            }
        });
        holder.rl_itemattention_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancleUploadShare(v, imgAttention);
            }
        });
        holder.iv_attentcontent_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUploadOver(imgAttention)) {
                    shareVideoAndImg((BaseActivity) context, imgAttention);
                    imgAttention.setShowItemMarsk(false);
                }
            }
        });
        holder.tv_attentioncontent_praisecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, OnLookersListAvtivity.class)
                        .putExtra("type", imgAttention.getElement_type() + "")
                        .putExtra("video_id", imgAttention.getVideo_id())
                        .putExtra("photo_id", imgAttention.getPhoto_id()));
            }
        });
    }

    /**
     * 处理视频上传
     *
     * @return 是否来自上传 true：是 false：不是
     */
    private boolean handleVideoUpload(final int position, VideoViewHolder holder) {
        final Attention attention = data.get(position);
        boolean state = false;
        if (uploadTempMap != null) {
            final VideoUploadInfo uploadInfo = uploadTempMap.get(attention.getVideoSrcPath());
            if (attention.getUploadState() != VideoUploadInfo.UPLOAD_FINISH) {
                //未完成上传
                switch (attention.getUploadState()) {
                    case VideoUploadInfo.UPLOAD_PRE://准备上传
                        holder.rl_itemqa_on.setVisibility(View.VISIBLE);
                        holder.rl_itemattention_share.setVisibility(View.GONE);
                        holder.rl_itemqa_fail.setVisibility(View.GONE);
                        holder.tv_itemqa_percent.setText("0%");
                        holder.pb_itemqa_progressbar.setProgress(0);
                        holder.videoView.setPlayButtonEnableVisible(false);
                        attention.setDuration_second(uploadInfo.getVideoDuration() / 1000);
                        state = true;
                        break;
                    case VideoUploadInfo.UPLOAD_PROGRESS://上传中（刷新进度）
                        holder.rl_itemqa_on.setVisibility(View.VISIBLE);
                        holder.rl_itemattention_share.setVisibility(View.GONE);
                        holder.rl_itemqa_fail.setVisibility(View.GONE);
                        holder.tv_itemqa_percent.setText(attention.getProgress() + "%");
                        holder.pb_itemqa_progressbar.setProgress((int) (attention.getProgress()));
                        holder.videoView.setPlayButtonEnableVisible(false);
                        attention.setDuration_second(uploadInfo.getVideoDuration() / 1000);
                        state = true;
                        break;
                    case VideoUploadInfo.UPLOAD_SUCCESS://上传成功
                        holder.rl_itemqa_on.setVisibility(View.GONE);
                        holder.rl_itemattention_share.setVisibility(View.VISIBLE);
                        holder.rl_itemqa_fail.setVisibility(View.GONE);
                        //如果上传已经成功 上传状态设置为已经上传完毕
//                        uploadInfo.setUploadState(VideoUploadInfo.UPLOAD_FINISH);
                        uploadInfo.setUploadState(VideoUploadInfo.UPLOAD_SUCCESS);
                        holder.videoView.setPlayButtonEnableVisible(true);
                        state = false;
                        break;
                    case VideoUploadInfo.UPLOAD_FAILE://上传失败
                        holder.rl_itemqa_on.setVisibility(View.GONE);
                        holder.rl_itemattention_share.setVisibility(View.GONE);
                        holder.rl_itemqa_fail.setVisibility(View.VISIBLE);
                        holder.videoView.setPlayButtonEnableVisible(false);
                        state = false;
                        break;
                    case VideoUploadInfo.UPLOAD_FINISH://上传完成
                        holder.rl_itemqa_on.setVisibility(View.GONE);
                        holder.rl_itemattention_share.setVisibility(View.GONE);
                        holder.rl_itemqa_fail.setVisibility(View.GONE);
                        holder.videoView.setPlayButtonEnableVisible(true);
                        state = false;
                        break;
                }
            }
            //处理上传过程不能点击
            holder.rl_itemqa_on.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.tv_itemqareupload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isExitInDraft = DraftUtil.isExitInDraft(uploadInfo);
                    if (isExitInDraft) {
                        //存在草稿箱
                        Intent it = new Intent(context, UploadService.class);
                        VideoUploadInfo videoUploadInfo = uploadInfo;
                        videoUploadInfo.setFromInfo(VideoUploadInfo.FROM_DRAFTS);
                        it.putExtra("uploadTask", videoUploadInfo);
                        context.startService(it);
                    } else {
                        //不存在草稿箱
                        data.remove(position);
                        ToastUtils.showToastAtCenter(context, "该视频不存在！");
                        notifyDataSetChanged();
                    }
                }
            });
        }
        return state;
    }

    /**
     * 处理图片上传
     *
     * @param position
     * @param holder
     * @return 是否来自上传 true：是 false：不是
     */
    private boolean handleImgUpload(final int position, ImgViewHolder holder) {
        final Attention attention = data.get(position);
        if (uploadTempMap != null) {
            final VideoUploadInfo uploadInfo = uploadTempMap.get(attention.getPhotoSrcPath());
            if (attention.getUploadState() != VideoUploadInfo.UPLOAD_FINISH) {
                //未完成上传
                switch (attention.getUploadState()) {
                    case VideoUploadInfo.UPLOAD_PRE://准备上传
                        holder.rl_itemqa_on.setVisibility(View.VISIBLE);
                        holder.rl_itemattention_share.setVisibility(View.GONE);
                        holder.rl_itemqa_fail.setVisibility(View.GONE);
                        holder.tv_itemqa_percent.setText("0%");
                        holder.pb_itemqa_progressbar.setProgress(0);
                        break;
                    case VideoUploadInfo.UPLOAD_PROGRESS://上传中（刷新进度）
                        holder.rl_itemqa_on.setVisibility(View.VISIBLE);
                        holder.rl_itemattention_share.setVisibility(View.GONE);
                        holder.rl_itemqa_fail.setVisibility(View.GONE);
                        holder.tv_itemqa_percent.setText(attention.getProgress() + "%");
                        holder.pb_itemqa_progressbar.setProgress((int) (uploadInfo.getProsess()));
                        break;
                    case VideoUploadInfo.UPLOAD_SUCCESS://上传成功
                        holder.rl_itemqa_on.setVisibility(View.GONE);
                        holder.rl_itemattention_share.setVisibility(View.VISIBLE);
                        holder.rl_itemqa_fail.setVisibility(View.GONE);
                        //如果上传已经成功 上传状态设置为已经上传完毕
//                        uploadInfo.setUploadState(VideoUploadInfo.UPLOAD_FINISH);
                        uploadInfo.setUploadState(VideoUploadInfo.UPLOAD_SUCCESS);
                        break;
                    case VideoUploadInfo.UPLOAD_FAILE://上传失败
                        holder.rl_itemqa_on.setVisibility(View.GONE);
                        holder.rl_itemattention_share.setVisibility(View.GONE);
                        holder.rl_itemqa_fail.setVisibility(View.VISIBLE);
                        break;
                    case VideoUploadInfo.UPLOAD_FINISH://上传完成
                        holder.rl_itemqa_on.setVisibility(View.GONE);
                        holder.rl_itemattention_share.setVisibility(View.GONE);
                        holder.rl_itemqa_fail.setVisibility(View.GONE);
                        break;
                }
            }
            //处理上传过程不能点击
            holder.rl_itemqa_on.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.tv_itemqareupload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isExitInDraft = DraftUtil.isExitInDraft(uploadInfo);
                    if (isExitInDraft) {
                        //存在草稿箱
                        Intent it = new Intent(context, UploadService.class);
                        VideoUploadInfo videoUploadInfo = uploadInfo;
                        videoUploadInfo.setFromInfo(VideoUploadInfo.FROM_DRAFTS);
                        it.putExtra("uploadTask", videoUploadInfo);
                        context.startService(it);
                    } else {
                        //不存在草稿箱
                        data.remove(position);
                        ToastUtils.showToastAtCenter(context, "该视频不存在！");
                        notifyDataSetChanged();
                    }
                }
            });

            return true;
        }
        return false;
    }

    /**
     * 处理应答视频数据
     *
     * @param position
     * @param holder
     */
    public void handleQaVideo(final int position, final QaViewHolder holder) {
        holder.position = position;
        final Attention qaAttention = data.get(position);
        setHeaderData(holder.qa_attentionheader, qaAttention, position);
        final VideoView videoView = (VideoView) holder.vvw_attentionqa.getVideoView();
        videoView.reset(position);
        //(height/width) 高和宽的比例
        double rat = 0;
        if (qaAttention.getWidth() == 0) rat = 1;
        else if (qaAttention.getWidth() > qaAttention.getHeight()
                || (qaAttention.getHeight() * (3d / 4d)) <= qaAttention.getWidth()) {
            rat = qaAttention.getHeight() / qaAttention.getWidth();
        } else if ((qaAttention.getHeight() * (3d / 4d)) > qaAttention.getWidth()) {
            rat = 4d / 3d;
        }
        ViewGroup.LayoutParams layoutParams = holder.rl_attentionqa.getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = (int) (screenWidth * rat);
        holder.rl_attentionqa.setLayoutParams(layoutParams);


        holder.tv_attention_qa_time.setText(qaAttention.getUpload_time_text());
        holder.tv_attention_qa_circuseecount.setText(qaAttention.getObserve_count());
        ImageLoader.getInstance().displayImage(qaAttention.getCover_uri(), videoView.getPreviewImageView()
                , DisplayOptionsUtils.getDefaultMaxRectImageOptions(), new AnimateFirstDisplayListener(videoView.getPreviewImageView())); //视频封面

        videoView.setVideoControlListener(new BaseVideoView.VideoControlListener() {
            @Override
            public void videoViewState(BaseVideoView.VideoViewState videoViewState, int extra) {
                switch (videoViewState) {
                    case ON_START_PLAY_CLICK:
                        VideoInfo videoInfo = new VideoInfo();
                        if (null != lastVideoView) {
                            lastVideoView.reset(position);
                            if (lastPlayPosition == position) {
                                videoInfo.setLastPlayDuration(lastVideoView.getLastPlayPosition());
                            }
                        }
                        lastVideoView = videoView;
                        lastPlayPosition = position;

                        videoInfo.setVideo_id(qaAttention.getVideo_id());
                        videoInfo.setVideo_uri(qaAttention.getVideo_url());
                        videoInfo.setVideo_state(qaAttention.getVideo_state());
                        videoView.startPlay(mediaPlayer, videoInfo);
                        break;
                    case ON_VIDEO_COMPLETE:
                        videoView.setPlayButtonEnableVisible(false);
                        if (null != holder.rl_itemattention_btn)
                            holder.rl_itemattention_btn.setVisibility(View.VISIBLE);
                        break;
                    case ON_CLOSE_AUDIO_CLICK:
                        notifyDataSetChanged();
                        break;
                }
            }
        });

        //设置推荐标签
        holder.tv_attentioinbtn_text.setText(qaAttention.getObserve_price_text());
        if (checkQaState(qaAttention) == 2) {
            //限时免费
            holder.tv_attentioinbtn_text.setText("限时免费");
            holder.rl_itemattention_btn.setBackgroundResource(R.drawable.shape_limit_free_radius_red_selector);

        } else if (checkQaState(qaAttention) == 1) {
            //点击播放
            holder.tv_attentioinbtn_text.setText("点击播放");
            holder.rl_itemattention_btn.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
        } else {
            holder.rl_itemattention_btn.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
        }
        //是否显示按钮
        videoView.setPlayButtonEnableVisible(false);
        // 播放状态不显示映答按钮
        holder.rl_itemattention_btn.setVisibility(videoView.getCurrentMediaState() == VideoView.MEDIA_STATE_IDLE ? View.VISIBLE : View.GONE);
        holder.tv_attentionqatext.setText(qaAttention.getQuestion_text());
        //点击播放按钮
        holder.rl_itemattention_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkQaState(qaAttention) == 1) {
                    //点击播放
                    qaAttention.setShowQaBtn(false);

                    VideoInfo videoInfo = new VideoInfo();
                    if (null != lastVideoView) {
                        lastVideoView.reset(position);
                        if (lastPlayPosition == position) {
                            videoInfo.setLastPlayDuration(lastVideoView.getLastPlayPosition());
                        }
                    }

                    lastVideoView = videoView;
                    lastPlayPosition = position;
                    holder.rl_itemattention_btn.setVisibility(View.GONE);
                    videoInfo.setVideo_id(qaAttention.getVideo_id());
                    videoInfo.setVideo_uri(qaAttention.getVideo_url());
                    videoInfo.setVideo_state(qaAttention.getVideo_state());
                    videoView.startPlay(mediaPlayer, videoInfo);
                } else if (checkQaState(qaAttention) == 2
                        || checkQaState(qaAttention) == 3) {
                    TCAgent.onEvent(context, "关注映答围观" + ConstantsValue.android);
//                    v.setVisibility(View.GONE);
                    //xx元围观和限时免费
                    if (onAttentionDo != null)
                        onAttentionDo.onConfirmPay(position, lastPlayPosition, videoView, lastVideoView, holder.rl_itemattention_btn);
                }
            }
        });

        //分享
        holder.iv_attention_qa_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TCAgent.onEvent(context, "关注映答分享" + ConstantsValue.android);
                shareQA(qaAttention);
            }
        });

        //点击围观
        holder.ll_attentioncircusee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InterlocutionDetailsActivity.class)
                        .putExtra("question_id", qaAttention.getQuestion_id());
                ((BaseActivity) context).startActivityForResult(intent, InterlocutionDetailsActivity.PAYRESULT);
            }
        });
        holder.tv_attentionqatext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InterlocutionDetailsActivity.class)
                        .putExtra("question_id", qaAttention.getQuestion_id());
                ((BaseActivity) context).startActivityForResult(intent, InterlocutionDetailsActivity.PAYRESULT);
                TCAgent.onEvent(context, "映答点击" + ConstantsValue.android);
            }
        });

        if (qaAttention.isPlayNow()) {
            //立即播放
            if (null != lastVideoView)
                lastVideoView.reset(position);
            lastVideoView = videoView;
            lastPlayPosition = position;
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setVideo_id(qaAttention.getVideo_id());
            videoInfo.setVideo_uri(qaAttention.getVideo_url());
            videoInfo.setVideo_state(/*videoAttention.getVideo_state()*/10);
            videoView.startPlay(mediaPlayer, videoInfo);
            holder.rl_itemattention_btn.setVisibility(View.GONE);
            qaAttention.setPlayNow(false);//关闭立即播放
            qaAttention.setShowQaBtn(false);//支付过后应答按钮不在显示
        }

        VideoExtraInfo videoExtraInfo = new VideoExtraInfo();
        videoExtraInfo.videoId = qaAttention.getVideo_id();
        videoExtraInfo.playNum = qaAttention.getPlay_total();
        videoExtraInfo.videoDuration = qaAttention.getDuration_second();
        videoExtraInfo.setVideoType(VideoExtraInfo.VideoType.VIDEO_TYPE_YD);
        videoExtraInfo.subjectName = qaAttention.getActivity_name();
        videoExtraInfo.subjectUri = qaAttention.getActivity_uri();
        videoExtraInfo.recommendStr = qaAttention.getRecommend_text();
        videoExtraInfo.position = position;
        videoView.setVideoExtraInfo(videoExtraInfo);
    }

    public void setLastPlayPosition(int lastPlayPosition) {
        this.lastPlayPosition = lastPlayPosition;
    }


    private class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_attentcontent_praise;
        private ImageView iv_attentcontent_comment;
        private ImageView iv_attentcontent_share;
        private TextView tv_attentcontent_time;
        private TextView tv_attentioncontent_describe;
        private TextView tv_attentioncontent_praisecount;
        private LinearLayout ll_attentioncontent_conent;
        private TextView tv_attentioncontent_commenttotalcount;
        private VideoViewWrap vvw_content_video;
        private VideoView videoView;
        private List<TextView> tv_commonts;
        private TextView tv_commont1;
        private TextView tv_commont2;
        private TextView tv_commont3;
        /*视频上传相关*/
        private RelativeLayout rl_itemqa_on;
        private RelativeLayout rl_itemqa_fail;
        private RelativeLayout rl_itemattention_share;
        private TextView tv_itemqa_percent;
        private ProgressBar pb_itemqa_progressbar;
        private TextView tv_itemqareupload;
        private TextView tv_itemqafail_time;
        private ImageView iv_attentionshare_wechatcircle;
        private ImageView iv_attentionshare_wechat;
        private ImageView iv_attentionshare_qqzone;
        private ImageView iv_attentionshare_qq;
        private ImageView iv_attentionshare_sina;
        private ImageView iv_attentionshare_copylink;
        private int position;
        private int lastPrisePosition;
        private View ll_video_info;
        private View rl_video_container;
        private CommonPersonalInfoView video_attentionheader;

        public VideoViewHolder(View rootView) {
            super(rootView);
            if (VideoViewHolder.this instanceof ImgViewHolder) return;
            video_attentionheader = (CommonPersonalInfoView) rootView.findViewById(R.id.video_attentionheader);
            video_attentionheader.setVisibility(View.VISIBLE);
            this.rl_video_container = rootView.findViewById(R.id.rl_video_container);
            this.iv_attentcontent_praise = (ImageView) rootView.findViewById(R.id.iv_attentcontent_praise);
            this.iv_attentcontent_comment = (ImageView) rootView.findViewById(R.id.iv_attentcontent_comment);
            this.iv_attentcontent_share = (ImageView) rootView.findViewById(R.id.iv_attentcontent_share);
            this.tv_attentcontent_time = (TextView) rootView.findViewById(R.id.tv_attentcontent_time);
            this.tv_attentioncontent_describe = (TextView) rootView.findViewById(R.id.tv_attentioncontent_describe);
            this.tv_attentioncontent_praisecount = (TextView) rootView.findViewById(R.id.tv_attentioncontent_praisecount);
            this.tv_attentioncontent_commenttotalcount = (TextView) rootView.findViewById(R.id.tv_attentioncontent_commenttotalcount);
            this.ll_attentioncontent_conent = (LinearLayout) rootView.findViewById(R.id.ll_attentioncontent_conent);
            this.vvw_content_video = (VideoViewWrap) rootView.findViewById(R.id.vvw_content_video);
//            this.ll_video_info = rootView.findViewById(R.id.ll_video_info);
            this.videoView = (VideoView) vvw_content_video.getVideoView();
            this.tv_commont1 = (TextView) rootView.findViewById(R.id.tv_commont1);
            this.tv_commont2 = (TextView) rootView.findViewById(R.id.tv_commont2);
            this.tv_commont3 = (TextView) rootView.findViewById(R.id.tv_commont3);
            this.tv_commonts = new ArrayList<>();
            this.tv_commonts.add(this.tv_commont1);
            this.tv_commonts.add(this.tv_commont2);
            this.tv_commonts.add(this.tv_commont3);
            tv_commont1.setOnTouchListener(new LinkOnTouckListener());
            tv_commont2.setOnTouchListener(new LinkOnTouckListener());
            tv_commont3.setOnTouchListener(new LinkOnTouckListener());

            this.rl_itemqa_on = (RelativeLayout) rootView.findViewById(R.id.rl_itemqa_on);
            this.rl_itemqa_fail = (RelativeLayout) rootView.findViewById(R.id.rl_itemqa_fail);
            this.rl_itemattention_share = (RelativeLayout) rootView.findViewById(R.id.rl_itemattention_share);
            this.pb_itemqa_progressbar = (ProgressBar) rootView.findViewById(R.id.pb_itemqa_progressbar);
            this.tv_itemqa_percent = (TextView) rootView.findViewById(R.id.tv_itemqa_percent);
            this.tv_itemqareupload = (TextView) rootView.findViewById(R.id.tv_itemqareupload);
            this.tv_itemqafail_time = (TextView) rootView.findViewById(R.id.tv_itemqafail_time);

            this.iv_attentionshare_wechatcircle = (ImageView) rootView.findViewById(R.id.iv_attentionshare_wechatcircle);
            this.iv_attentionshare_wechat = (ImageView) rootView.findViewById(R.id.iv_attentionshare_wechat);
            this.iv_attentionshare_qqzone = (ImageView) rootView.findViewById(R.id.iv_attentionshare_qqzone);
            this.iv_attentionshare_qq = (ImageView) rootView.findViewById(R.id.iv_attentionshare_qq);
            this.iv_attentionshare_sina = (ImageView) rootView.findViewById(R.id.iv_attentionshare_wechatcircle);
            this.iv_attentionshare_copylink = (ImageView) rootView.findViewById(R.id.iv_attentionshare_copylink);

            rootView.setOnClickListener(this);
            iv_attentcontent_praise.setOnClickListener(this);
            iv_attentcontent_comment.setOnClickListener(this);
//            ll_video_info.setOnClickListener(this);
            rl_itemattention_share.setOnClickListener(this);
            iv_attentcontent_share.setOnClickListener(this);

            videoView.setVideoControlListener(new BaseVideoView.VideoControlListener() {
                @Override
                public void videoViewState(BaseVideoView.VideoViewState videoViewState, int extra) {
                    switch (videoViewState) {
                        case ON_START_PLAY_CLICK:
                            VideoInfo videoInfo = new VideoInfo();
                            if (null != lastVideoView) {
                                lastVideoView.reset(position);
                                if (lastPlayPosition == position) {
                                    videoInfo.setLastPlayDuration(lastVideoView.getLastPlayPosition());
                                }
                            }
                            Attention attention = data.get(position);
                            if (null == attention) return;
                            lastVideoView = videoView;
                            lastPlayPosition = position;
                            videoInfo.setVideo_id(attention.getVideo_id());
                            videoInfo.setVideo_uri(attention.getVideo_uri());
                            videoInfo.setVideo_state(attention.getVideo_state());
                            videoView.startPlay(mediaPlayer, videoInfo);
                            break;
                        case ON_DOUBLE_CLICK:
                            if (null != data.get(position) && !data.get(position).isPraise_state())
                                videoPrise();
                            break;
                        case ON_VIDEO_COMPLETE:
                            videoView.setPraiseState(data.get(position).isPraise_state());
                            break;
                        case ON_CLOSE_AUDIO_CLICK:
                            notifyDataSetChanged();
                            break;
                    }
                }
            });
            videoView.setOnPraiseListener(new VideoView.OnPraiseListener() {
                @Override
                public void onPraiseClick(View view, boolean isPraise) {
                    videoPrise();
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_video_root_view:
                    if (isUploadOver(data.get(position))) {
                        ArrayList<Attention> list = new ArrayList<Attention>();
                        list.add(data.get(position));
                        context.startActivity(new Intent(context, VideoAndImgActivity.class)
                                .putParcelableArrayListExtra("data", list));
                    }
                    break;
                case R.id.iv_attentcontent_praise:
                    if (isUploadOver(data.get(position))) {
                        videoPrise();
                        if (!iv_attentcontent_praise.isSelected())
                            PraisedUtil.showPop(iv_attentcontent_praise, context, true, iv_attentcontent_praise.getWidth());
                    }
                    break;
                case R.id.iv_attentcontent_comment:
                    if (isUploadOver(data.get(position))) {
                        if (onStartCommentListener != null)
                            onStartCommentListener.onStartCommont(position);
                    }
                    break;
//                case R.id.ll_video_info:
//                    if (isUploadOver(data.get(position))) {
//                        asyncPraise(data.get(position));
//                        asyncAttention(data.get(position));
//                        ArrayList<Attention> list = new ArrayList<Attention>();
//                        list.add(data.get(position));
//                        context.startActivity(new Intent(context, VideoAndImgActivity.class)
//                                .putParcelableArrayListExtra("data", list)
//                                .putExtra("isFromCommentList", true));
//                    }
//                    break;

                case R.id.rl_itemattention_share:
                    if (null != data.get(position))
                        cancleUploadShare(v, data.get(position));
                    break;

                case R.id.iv_attentcontent_share:
                    if (isUploadOver(data.get(position))) {
                        shareVideoAndImg((BaseActivity) context, data.get(position));
                        data.get(position).setShowItemMarsk(false);
                    }
                    break;


            }
        }

        private void videoPrise() {
            TCAgent.onEvent(context, "视频赞" + ConstantsValue.android);
            final Attention pageResult = data.get(position);
            MHRequestParams params = new MHRequestParams();
            params.addParams("action_mark", !pageResult.isPraise_state() + "");
            params.addParams("video_id", pageResult.getVideo_id());
            iv_attentcontent_praise.setEnabled(false);
            lastPrisePosition = position;
            videoView.setPraiseEnable(false);
            pageResult.setPraising(true);
            MHHttpClient.getInstance().post(BaseResponse.class, context, ConstantsValue.Url.PRAISE_VIDEODO, params, new MHHttpHandler<BaseResponse>() {
                @Override
                public void onSuccess(BaseResponse response) {
                    NoDoubleClickUtils.setEnable(iv_attentcontent_praise);

                    pageResult.setPraising(false);
                    pageResult.setPraise_state(!pageResult.isPraise_state());
                    pageResult.setPraise_count(pageResult.isPraise_state() ? pageResult.getPraiseSrcCount() + 1 : pageResult.getPraiseSrcCount() - 1);
                    if (lastPrisePosition == position) {//防止网络请求慢导致数据不同步
                        tv_attentioncontent_praisecount.setText(pageResult.getPraise_count() + "");
                        tv_attentioncontent_praisecount.setVisibility(pageResult.getPraiseSrcCount() > 0 ? View.VISIBLE : View.GONE);
                        iv_attentcontent_praise.setEnabled(true);
                        videoView.setPraiseEnable(true);
                        iv_attentcontent_praise.setSelected(pageResult.isPraise_state());
                        videoView.setPraiseState(pageResult.isPraise_state());
                    }
//                    PraisedUtil.showPop(iv_prise_icon, baseActivity, pageResult.isPraise_state());
                    MHStateSyncUtil.pushSyncEvent(context, pageResult.getVideo_id(), pageResult.isPraise_state());
                }

                @Override
                public void onFailure(String content) {
                    pageResult.setPraising(false);
                    iv_attentcontent_praise.setEnabled(true);
                }

                @Override
                public void onStatusIsError(String message) {
                    super.onStatusIsError(message);
                    pageResult.setPraising(false);
                    iv_attentcontent_praise.setEnabled(true);
                }
            });
        }
    }

    class ImgViewHolder extends VideoViewHolder {
        private CommonPersonalInfoView imag_attentionheader;
        private ImageView iv_attentcontent_praise;
        private ImageView iv_attentcontent_comment;
        private ImageView iv_attentcontent_share;
        private TextView tv_attentcontent_time;
        private TextView tv_attentioncontent_describe;
        private TextView tv_attentioncontent_praisecount;
        private LinearLayout ll_attentioncontent_conent;
        private TextView tv_attentioncontent_commenttotalcount;
        private PraiseImageView iv_content_img;
        private TextView tv_videosubject;
        private ImageView iv_qa;
        private TextView tv_recommend;
        private List<TextView> tv_commonts;
        private TextView tv_commont1, tv_commont2, tv_commont3;
        /*视频上传相关*/
        private RelativeLayout rl_itemqa_on;
        private RelativeLayout rl_itemqa_fail;
        private RelativeLayout rl_itemattention_share;
        private TextView tv_itemqa_percent;
        private ProgressBar pb_itemqa_progressbar;
        private TextView tv_itemqareupload;
        private TextView tv_itemqafail_time;
        private ImageView iv_attentionshare_wechatcircle;
        private ImageView iv_attentionshare_wechat;
        private ImageView iv_attentionshare_qqzone;
        private ImageView iv_attentionshare_qq;
        private ImageView iv_attentionshare_sina;
        private ImageView iv_attentionshare_copylink;
        private int position;

        public ImgViewHolder(View convertView) {
            super(convertView);
            imag_attentionheader = (CommonPersonalInfoView) convertView.findViewById(R.id.imag_attentionheader);
            imag_attentionheader.setVisibility(View.VISIBLE);
            iv_attentcontent_praise = (ImageView) convertView.findViewById(R.id.iv_attentcontent_praise);
            iv_attentcontent_comment = (ImageView) convertView.findViewById(R.id.iv_attentcontent_comment);
            iv_attentcontent_share = (ImageView) convertView.findViewById(R.id.iv_attentcontent_share);
            tv_attentcontent_time = (TextView) convertView.findViewById(R.id.tv_attentcontent_time);
            tv_attentioncontent_describe = (TextView) convertView.findViewById(R.id.tv_attentioncontent_describe);
            tv_attentioncontent_praisecount = (TextView) convertView.findViewById(R.id.tv_attentioncontent_praisecount);
            tv_attentioncontent_commenttotalcount = (TextView) convertView.findViewById(R.id.tv_attentioncontent_commenttotalcount);
            ll_attentioncontent_conent = (LinearLayout) convertView.findViewById(R.id.ll_attentioncontent_conent);
            iv_content_img = (PraiseImageView) convertView.findViewById(R.id.iv_content_img);
            tv_videosubject = (TextView) convertView.findViewById(R.id.tv_videosubject);
            iv_qa = (ImageView) convertView.findViewById(R.id.iv_qa);
            tv_recommend = (TextView) convertView.findViewById(R.id.tv_recommend);
            tv_commont1 = (TextView) convertView.findViewById(R.id.tv_commont1);
            tv_commont2 = (TextView) convertView.findViewById(R.id.tv_commont2);
            tv_commont3 = (TextView) convertView.findViewById(R.id.tv_commont3);
            tv_commonts = new ArrayList<>();
            tv_commonts.add(tv_commont1);
            tv_commonts.add(tv_commont2);
            tv_commonts.add(tv_commont3);
            tv_commont1.setOnTouchListener(new LinkOnTouckListener());
            tv_commont2.setOnTouchListener(new LinkOnTouckListener());
            tv_commont3.setOnTouchListener(new LinkOnTouckListener());

            rl_itemqa_on = (RelativeLayout) convertView.findViewById(R.id.rl_itemqa_on);
            rl_itemqa_fail = (RelativeLayout) convertView.findViewById(R.id.rl_itemqa_fail);
            rl_itemattention_share = (RelativeLayout) convertView.findViewById(R.id.rl_itemattention_share);
            pb_itemqa_progressbar = (ProgressBar) convertView.findViewById(R.id.pb_itemqa_progressbar);
            tv_itemqa_percent = (TextView) convertView.findViewById(R.id.tv_itemqa_percent);
            tv_itemqareupload = (TextView) convertView.findViewById(R.id.tv_itemqareupload);
            tv_itemqafail_time = (TextView) convertView.findViewById(R.id.tv_itemqafail_time);

            iv_attentionshare_wechatcircle = (ImageView) convertView.findViewById(R.id.iv_attentionshare_wechatcircle);
            iv_attentionshare_wechat = (ImageView) convertView.findViewById(R.id.iv_attentionshare_wechat);
            iv_attentionshare_qqzone = (ImageView) convertView.findViewById(R.id.iv_attentionshare_qqzone);
            iv_attentionshare_qq = (ImageView) convertView.findViewById(R.id.iv_attentionshare_qq);
            iv_attentionshare_sina = (ImageView) convertView.findViewById(R.id.iv_attentionshare_wechatcircle);
            iv_attentionshare_copylink = (ImageView) convertView.findViewById(R.id.iv_attentionshare_copylink);

            //点击空白跳转
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isUploadOver(data.get(position))) {
                        ArrayList<Attention> list = new ArrayList<Attention>();
                        list.add(data.get(position));
                        context.startActivity(new Intent(context, VideoAndImgActivity.class)
                                .putParcelableArrayListExtra("data", list));
                    }
                    if (AttentionStickyHeaderAdapter.this.getItemViewType(position) == VIDEO)
                        TCAgent.onEvent(context, "视频点击" + ConstantsValue.android);
                    else if (AttentionStickyHeaderAdapter.this.getItemViewType(position) == IMG)
                        TCAgent.onEvent(context, "图片点击" + ConstantsValue.android);
                }
            });
        }
    }

    class MaybeInterestViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_attention_maybeinterestfindmore;
        private RecyclerView rv_attention_mabeinterest;

        MaybeInterestViewHolder(View itemView) {
            super(itemView);
            tv_attention_maybeinterestfindmore = (TextView) itemView.findViewById(R.id.tv_attention_maybeinterestfindmore);
            rv_attention_mabeinterest = (RecyclerView) itemView.findViewById(R.id.rv_attention_mabeinterest);
        }
    }

    class QaViewHolder extends RecyclerView.ViewHolder {
        private CommonPersonalInfoView qa_attentionheader;
        private ImageView iv_attention_qa_circuseecount;
        private TextView tv_attention_qa_circuseecount;
        private ImageView iv_attention_qa_share;
        private TextView tv_attention_qa_time;
        private VideoViewWrap vvw_attentionqa;
        private VideoView videoView;
        private RelativeLayout rl_attentionqa;
        private RelativeLayout rl_itemattention_btn;
        private TextView tv_attentioinbtn_text;
        private LinearLayout ll_attentioncircusee;
        private TextView tv_attentionqatext;
        private int position;

        public QaViewHolder(View convertView) {
            super(convertView);
            tv_attention_qa_circuseecount = (TextView) convertView.findViewById(R.id.tv_attention_qa_circuseecount);
            tv_attention_qa_time = (TextView) convertView.findViewById(R.id.tv_attention_qa_time);
            iv_attention_qa_circuseecount = (ImageView) convertView.findViewById(R.id.iv_attention_qa_circuseecount);
            iv_attention_qa_share = (ImageView) convertView.findViewById(R.id.iv_attention_qa_share);
            vvw_attentionqa = (VideoViewWrap) convertView.findViewById(R.id.vvw_attentionqa);
            rl_attentionqa = (RelativeLayout) convertView.findViewById(R.id.rl_attentionqa);
            rl_itemattention_btn = (RelativeLayout) convertView.findViewById(R.id.rl_itemattention_btn);
            tv_attentioinbtn_text = (TextView) convertView.findViewById(R.id.tv_attentioinbtn_text);
            ll_attentioncircusee = (LinearLayout) convertView.findViewById(R.id.ll_attentioncircusee);
            tv_attentionqatext = (TextView) convertView.findViewById(R.id.tv_attentionqatext);
            qa_attentionheader = (CommonPersonalInfoView) itemView.findViewById(R.id.qa_attentionheader);
            qa_attentionheader.setVisibility(View.VISIBLE);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        CommonPersonalInfoView cpiv_attentionheader;

        HeaderViewHolder(View itemView) {
            super(itemView);
            cpiv_attentionheader = (CommonPersonalInfoView) itemView.findViewById(R.id.cpiv_attentionheader);
        }
    }


    /**
     * 点赞
     */
    public void videoPrise(final int position, final int lastPrisePosition, final ImageView btn, final TextView tv_prise_count) {
        final Attention attentionInfo = data.get(position);
        String url = null;
        MHRequestParams params = new MHRequestParams();
        params.addParams("action_mark", !attentionInfo.isPraise_state() + "");
        if (attentionInfo.getElement_type() == VIDEO) {
            params.addParams("video_id", attentionInfo.getVideo_id());
            url = ConstantsValue.Url.PRAISE_VIDEODO;
        } else if (attentionInfo.getElement_type() == IMG) {
            params.addParams("photo_id", attentionInfo.getPhoto_id());
            url = ConstantsValue.Url.PRAISEPHOTODO;
        }
        btn.setEnabled(false);
        attentionInfo.setPraising(true);
        MHHttpClient.getInstance().post(BaseResponse.class, context, url, params, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                NoDoubleClickUtils.setEnable(btn);
                attentionInfo.setPraising(false);
                attentionInfo.setPraise_state(!attentionInfo.isPraise_state());
                if (attentionInfo.isPraise_state()) {
                    attentionInfo.setPraise_count(attentionInfo.getPraiseSrcCount() + 1);
                } else {
                    attentionInfo.setPraise_count(attentionInfo.getPraiseSrcCount() - 1);
                }
                tv_prise_count.setVisibility(attentionInfo.getPraiseSrcCount() == 0 ? View.GONE : View.VISIBLE);

                if (attentionInfo.getElement_type() == VIDEO) {
                    MHStateSyncUtil.pushSyncEvent(context, attentionInfo.getVideo_id(), attentionInfo.isPraise_state());
                } else if (attentionInfo.getElement_type() == IMG) {
                    MHStateSyncUtil.pushSyncEvent(context, attentionInfo.getPhoto_id(), attentionInfo.isPraise_state());
                }
                if (lastPrisePosition == position) {//防止网络请求慢导致数据不同步
                    tv_prise_count.setText(attentionInfo.getPraise_count());
                    btn.setEnabled(true);
                    btn.setSelected(attentionInfo.isPraise_state());
//                    if (attentionInfo.isPraise_state()) {
//                        btn.setImageResource(R.drawable.praise);
//                    } else {
//                        btn.setImageResource(R.drawable.nopraise);
//                    }
//                    btn.setSelected(attentionInfo.isPraise_state());
                } else {
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String content) {
                attentionInfo.setPraising(false);
                btn.setEnabled(true);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                attentionInfo.setPraising(false);
                btn.setEnabled(true);
            }
        });
    }

    /**
     * 关注
     *
     * @param cpiv
     * @param attention
     */
    public void attentionOrCancle(final CommonPersonalInfoView cpiv, final Attention attention) {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("action_mark", !attention.isAttention_state() + "");
        if (attention.getElement_type() == VIDEO || attention.getElement_type() == IMG) {
            requestParams.addParams("user_id", attention.getUser_id());
        } else if (attention.getElement_type() == QA) {
            requestParams.addParams("user_id", attention.getAnswer_user_id());
        } else {
            return;
        }
//        progress_bar.setVisibility(View.VISIBLE);
        MHHttpClient.getInstance().post(HomeFoundResponse.class, context, ConstantsValue.Url.ATTENTIONDO, requestParams, new MHHttpHandler<HomeFoundResponse>() {
            @Override
            public void onSuccess(HomeFoundResponse response) {
                attention.setAttention_state(!attention.isAttention_state());
                cpiv.setAttention(attention.isAttention_state());

                if (attention.getElement_type() == VIDEO || attention.getElement_type() == IMG) {
                    MHStateSyncUtil.pushSyncEvent(context, attention.getUser_id(), attention.isAttention_state());
                } else if (attention.getElement_type() == QA) {
                    MHStateSyncUtil.pushSyncEvent(context, attention.getAnswer_user_id(), attention.isAttention_state());
                }
                asyncCurrPageAttention(attention, attention.isAttention_state());
            }

            @Override
            public void onFailure(String content) {
//                resetState(pageResult);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
//                resetState(pageResult);
            }
        });
    }


    /**
     * 同步当前页用户的关注状态
     *
     * @param isAttention
     */
    public void asyncCurrPageAttention(Attention attention, boolean isAttention) {
        for (int i = 0; i < data.size(); i++) {
            Attention currentAttention = data.get(i);
            if (attention.getElement_type() == VIDEO || attention.getElement_type() == IMG) {
                if (currentAttention.getElement_type() == QA) {
                    if (TextUtils.equals(attention.getUser_id(), currentAttention.getAnswer_user_id())) {
                        currentAttention.setAttention_state(isAttention);
                    }
                } else {
                    if (TextUtils.equals(attention.getUser_id(), currentAttention.getUser_id())) {
                        currentAttention.setAttention_state(isAttention);
                    }
                }
                if (TextUtils.equals(attention.getUser_id(), currentAttention.getUser_id())) {
                    data.get(i).setAttention_state(isAttention);
                }
            } else if (attention.getElement_type() == QA) {
                if (currentAttention.getElement_type() == QA) {
                    if (TextUtils.equals(attention.getAnswer_user_id(), currentAttention.getAnswer_user_id())) {
                        currentAttention.setAttention_state(isAttention);
                    }
                } else {
                    if (TextUtils.equals(attention.getAnswer_user_id(), currentAttention.getUser_id())) {
                        currentAttention.setAttention_state(isAttention);
                    }
                }

            }
        }
        notifyDataSetChanged();
    }


    public interface OnAttentionDo {
        //删除自己的视频或图片
        void onDelete(int position);

        //点击应答按钮调用二次确认
        void onConfirmPay(int position, int lastPlayPosition, VideoView videoView, VideoView lastVideoView, RelativeLayout btn);
    }

    public void setOnClickPictureListener(OnShowPicture onShowPicture) {
        this.onShowPicture = onShowPicture;
    }

    /**
     * 设置是否显示推荐标签
     *
     * @param attention
     * @param tv
     */
    public void setRecommendEnable(TextView tv, Attention attention) {
        if (MHStringUtils.isEmpty(attention.getRecommend_text())) {
            //无推荐
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(attention.getRecommend_text());
        }
    }

    /**
     * 当前是否上传成功
     *
     * @param attention
     * @return
     */
    public boolean isUploadOver(Attention attention) {
        if (uploadTempMap == null) return true;
        VideoUploadInfo uploadInfo = null;
        if (attention.getElement_type() == VIDEO) {
            uploadInfo = uploadTempMap.get(attention.getVideoSrcPath());
        } else if (attention.getElement_type() == IMG) {
            uploadInfo = uploadTempMap.get(attention.getPhotoSrcPath());
        }
        if (uploadInfo == null) return true;
        if (uploadInfo.getUploadState() == VideoUploadInfo.UPLOAD_SUCCESS
                || uploadInfo.getUploadState() == VideoUploadInfo.UPLOAD_FINISH) {
            return true;
        } else {
            ToastUtils.showToastAtCenter(context, "再等等，还未发布成功");
            return false;
        }
    }

    /**
     * 查看映答状态
     *
     * @param attention
     * @return 1:点击播放 2:限时免费 3:xx围观
     */
    public int checkQaState(Attention attention) {
        if (attention.is_question_owner()) return 1;
        if (attention.isTemporary_free()) return 2;
        else return 3;
    }

    /**
     * 设置分享视频和图片的view
     *
     * @param sviv_attention
     */
    public void setShareVideoAndImgView(ShareVideoAndImgView2 sviv_attention) {
        this.sviv_attention = sviv_attention;
    }

    /**
     * 设置分享映答的view
     *
     * @param sqdv_attention
     */
    public void setShareAttentionQA(ShareQADetailView sqdv_attention) {
        this.sqdv_attention = sqdv_attention;
    }


    /**
     * 分享视频或者图片
     *
     * @param activity
     * @param attention
     */
    public void shareVideoAndImg(final Activity activity, final Attention attention) {

        final ShareDialog shareDialog = new ShareDialog((BaseActivity) activity);
        shareDialog.setData();
        shareDialog.setShareLable(ShareDialog.IMG);
        shareDialog.setShareLink(attention.getShare_link_address());

        shareDialog.setOnShareImgPath(new ShareLayout.OnShareImgPath() {
            @Override
            public void getimgPath(final SHARE_MEDIA platform) {
                shareDialog.dismiss();
                shareVideoAndIMGToPlatform(activity, platform, attention);
            }
        });
    }

    /**
     * 分享到制定平台
     *
     * @param activity
     * @param platform
     * @param attention
     */
    private void shareVideoAndIMGToPlatform(final Activity activity, final SHARE_MEDIA platform, final Attention attention) {
        ((BaseActivity) context).showLoading();
        ShareVideoAndImgInfo videoAndImgInfo = new ShareVideoAndImgInfo();
        videoAndImgInfo.setHeight(attention.getHeight());
        videoAndImgInfo.setWidth(attention.getWidth());
        videoAndImgInfo.setJoinTime(TimeFormatUtils.formatYMD(attention.getUpload_time()));
        videoAndImgInfo.setName(attention.getUser_name());
        videoAndImgInfo.setType(attention.getElement_type());
        videoAndImgInfo.setHeaderUrl(attention.getPortrait_uri());
        if (MHStringUtils.isEmpty(attention.getShare_link_address())) {
            videoAndImgInfo.setQaCode_str(ConstantsValue.Shared.APPDOWNLOAD);
        } else videoAndImgInfo.setQaCode_str(attention.getShare_link_address());
        if (attention.getElement_type() == VIDEO) {
            //视频
            videoAndImgInfo.setImgUrl(attention.getVideo_cover_uri());
            videoAndImgInfo.setNote(attention.getVideo_note());
            sviv_attention.setOnLoadFinishListener(new SharePersonalHomeImgView.OnLoadFinish() {
                @Override
                public void onFinish(Object path) {
                    UmengShare.sharedIMG(activity, platform, path, attention.getShare_link_address(), attention.getVideo_note(), new IUMShareResultListener((BaseActivity) context));
                }
            });
        } else {
            //图片
            videoAndImgInfo.setImgUrl(attention.getPhoto_uri());
            videoAndImgInfo.setNote(attention.getPhoto_note());
            sviv_attention.setOnLoadFinishListener(new SharePersonalHomeImgView.OnLoadFinish() {
                @Override
                public void onFinish(Object path) {
                    UmengShare.sharedIMG(activity, platform, path, attention.getShare_link_address(), attention.getPhoto_note(), new IUMShareResultListener((BaseActivity) context));
                }
            });
        }
        sviv_attention.genderImage(videoAndImgInfo, platform);
    }

    /**
     * 分享应答
     */
    public void shareQA(final Attention attention) {
        if (data == null) return;
        final ShareDialog shareDialog = new ShareDialog((BaseActivity) context);
        shareDialog.setData();
        shareDialog.setShareLink(attention.getShare_link_address());
        shareDialog.setShareLable(ShareDialog.IMG);
        if (MHStringUtils.isEmpty(attention.getShare_link_address())) {
            shareDialog.setShareLink(ConstantsValue.Shared.APPDOWNLOAD);
        } else {
            shareDialog.setShareLink(attention.getShare_link_address());
        }
        shareDialog.setOnShareImgPath(new ShareLayout.OnShareImgPath() {
            @Override
            public void getimgPath(final SHARE_MEDIA platform) {
                shareDialog.dismiss();
                ((BaseActivity) context).showLoading();
                ShareVideoAndImgInfo videoAndImgInfo = new ShareVideoAndImgInfo();
                videoAndImgInfo.setQaCode_str(attention.getShare_link_address());
                videoAndImgInfo.setNote(attention.getQuestion_text());
                videoAndImgInfo.setName(attention.getAnswer_user_name());
                videoAndImgInfo.setImgUrl(attention.getCover_uri());
                videoAndImgInfo.setHeight(attention.getHeight());
                videoAndImgInfo.setWidth(attention.getWidth());
                videoAndImgInfo.setAnswerTime(attention.getAnswer_time());
                sqdv_attention.genderImage(videoAndImgInfo, platform);
                sqdv_attention.setOnLoadFinishListener(new AbsShareImg.OnLoadFinish() {
                    @Override
                    public void onFinish(Object path) {
                        UmengShare.sharedIMG((BaseActivity) context, platform, path, attention.getShare_link_address(), attention.getQuestion_text(), new IUMShareResultListener((BaseActivity) context));
                    }
                });
            }
        });
    }

    /**
     * 设置正在上传的VideoUploadInfo
     */
    public void setUploadTempMap(VideoUploadInfo uploadInfo) {
        if (uploadTempMap == null) uploadTempMap = new LinkedHashMap<>();
        if (uploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
            uploadTempMap.put(uploadInfo.getVideoSrcPath(), uploadInfo);
        } else if (uploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE) {
            uploadTempMap.put(uploadInfo.getPictureSrcPath(), uploadInfo);
        }
    }

    /**
     * 该item是否正在上传
     *
     * @param path
     */
    public boolean isExitUploading(String path) {
        if (uploadTempMap != null || path == null) return false;
        if (uploadTempMap.containsKey(path)) return true;
        else return false;
    }

    public class ShareClick implements View.OnClickListener {

        private Attention attention;
        private Activity activity;
        private SHARE_MEDIA platform;

        public ShareClick(Attention attention, Activity activity, SHARE_MEDIA platform) {
            this.attention = attention;
            this.activity = activity;
            this.platform = platform;
        }

        @Override
        public void onClick(View v) {
            shareVideoAndIMGToPlatform(activity, platform, attention);
            attention.setShowItemMarsk(false);
            AttentionStickyHeaderAdapter.this.notifyDataSetChanged();
        }
    }

    public int getLastPlayPosition() {
        return lastPlayPosition;
    }

    /**
     * 防止view点击和span的click冲突
     * 防止TextView设置sapn无"..."
     */
    public class LinkOnTouckListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean ret = false;
            CharSequence text = ((TextView) v).getText();
            Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
            TextView widget = (TextView) v;
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    }
                    ret = true;
                }
            }
            return ret;
        }

    }

    /**
     * 分享蒙层点击消失动画
     *
     * @param view
     * @param attention
     */
    private void cancleUploadShare(final View view, final Attention attention) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0.8f, 0f);
        anim.setDuration(300);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                view.setAlpha(1);
                attention.setShowItemMarsk(false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

}
