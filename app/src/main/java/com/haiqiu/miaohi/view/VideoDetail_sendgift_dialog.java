package com.haiqiu.miaohi.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.VideoDetailDialog_sendGgiftAdapter;
import com.haiqiu.miaohi.bean.SendGiftBean;
import com.haiqiu.miaohi.bean.VideoDetailBean;
import com.haiqiu.miaohi.bean.VideoDetailSendGiftResponse;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频详情发送礼物对话框
 * Created by ningl on 2016/6/24.
 */
public class VideoDetail_sendgift_dialog extends Dialog implements View.OnClickListener, VideoDetailDialog_sendGgiftAdapter.OnItemClickListener {

    private TextView tv_dialog_sendgifttitle;
    private ImageView iv_dialog_sendgiftcancle;
    private ImageView iv_dialog_sendgift;
    private ImageView iv_dialog_sendgiftrighttarrow;
    private ImageView iv_dialog_sendgiftleftarrow;
    private RelativeLayout tv_dialog_sendgiftvpout;
    private RecyclerView rv_dialog_sendgift;
    private TextView tv_dialog_sendgiftcount;
    private TextView tv_dialog_sendgifterror;
    private TextView tv_dialog_sendgift;
    private List<SendGiftBean> sendGiftBeans;
    private VideoDetailDialog_sendGgiftAdapter adapter;
    private List<Integer> selectList;//选中项记录
    private int currentSelectIndex;//当前选中项
    private int lastSelectIndex;//上一次选中项
    private VideoDetailBean videoDetailBean;//视频相关信息
    private Activity context;
    private LinearLayoutManager lm;
    private ImageView videodetail_sendgift_sun;
    private ImageView iv_videodetail_sendgift_fail;
    private AnimationDrawable animationDrawable;
    private Animation anim;
    private ISendGiftSuccessListener iSendGiftSuccessListener;

    public VideoDetail_sendgift_dialog(Activity context) {
        super(context, R.style.MiaoHiDialog);
        this.context = context;
        setContentView(R.layout.dialog_sendgift);
        initView();
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = ScreenUtils.getScreenSize(context).x;
        lp.height = ScreenUtils.getScreenSize(context).y;
        win.setAttributes(lp);
    }

    public void init(List<SendGiftBean> sendGiftBeans, VideoDetailBean videoDetailBean) {
        this.sendGiftBeans = sendGiftBeans;
        this.videoDetailBean = videoDetailBean;
        selectList = new ArrayList<>();
        setSelectList(sendGiftBeans.size());
        setSelcetedItem(0);
        adapter = new VideoDetailDialog_sendGgiftAdapter(context, sendGiftBeans, selectList);
        lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_dialog_sendgift.setLayoutManager(lm);
        rv_dialog_sendgift.setAdapter(adapter);
        setSendData(0);
        adapter.setOnItemClickListener(this);
        tv_dialog_sendgift.setText("发送");
        videodetail_sendgift_sun.setVisibility(View.GONE);
        iv_videodetail_sendgift_fail.setVisibility(View.GONE);
        tv_dialog_sendgifterror.setVisibility(View.GONE);
        show();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_dialog_sendgifttitle = (TextView) this.findViewById(R.id.tv_dialog_sendgifttitle);
        iv_dialog_sendgiftcancle = (ImageView) this.findViewById(R.id.iv_dialog_sendgiftcancle);
        iv_dialog_sendgift = (ImageView) this.findViewById(R.id.iv_dialog_sendgift);
        iv_dialog_sendgiftrighttarrow = (ImageView) this.findViewById(R.id.iv_dialog_sendgiftrighttarrow);
        iv_dialog_sendgiftleftarrow = (ImageView) this.findViewById(R.id.iv_dialog_sendgiftleftarrow);
        tv_dialog_sendgiftvpout = (RelativeLayout) this.findViewById(R.id.tv_dialog_sendgiftvpout);
        rv_dialog_sendgift = (RecyclerView) this.findViewById(R.id.rv_dialog_sendgift);
        tv_dialog_sendgiftcount = (TextView) this.findViewById(R.id.tv_dialog_sendgiftcount);
        tv_dialog_sendgifterror = (TextView) this.findViewById(R.id.tv_dialog_sendgifterror);
        tv_dialog_sendgift = (TextView) this.findViewById(R.id.tv_dialog_sendgift);
        videodetail_sendgift_sun = (ImageView) this.findViewById(R.id.videodetail_sendgift_sun);
        iv_videodetail_sendgift_fail = (ImageView) this.findViewById(R.id.iv_videodetail_sendgift_fail);
        iv_dialog_sendgiftcancle.setOnClickListener(this);
        tv_dialog_sendgift.setOnClickListener(this);
        iv_dialog_sendgiftleftarrow.setOnClickListener(this);
        iv_dialog_sendgiftrighttarrow.setOnClickListener(this);
    }

    /**
     * 发送礼物
     */
    private void sendGift() {
        //礼物数为0不执行
        if (TextUtils.isEmpty(sendGiftBeans.get(currentSelectIndex).getRemain_count())
                || TextUtils.equals("null", sendGiftBeans.get(currentSelectIndex).getRemain_count())
                || Integer.parseInt(sendGiftBeans.get(currentSelectIndex).getRemain_count()) == 0)
            return;
        tv_dialog_sendgift.setText("发送中...");
        stopAnimation();
        tv_dialog_sendgifterror.setVisibility(View.GONE);
        videodetail_sendgift_sun.setVisibility(View.GONE);
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("gift_id", sendGiftBeans.get(currentSelectIndex).getGift_id());
        requestParams.addParams("video_id", videoDetailBean.getVideo_id());
        requestParams.addParams("user_id", videoDetailBean.getUpload_user_id());
        MHHttpClient.getInstance().post(VideoDetailSendGiftResponse.class, ConstantsValue.Url.SENDGIFT, requestParams, new MHHttpHandler<VideoDetailSendGiftResponse>() {
            @Override
            public void onSuccess(VideoDetailSendGiftResponse response) {
//                VideoDetail_sendgift_dialog.this.dismiss();
//                ToastUtils.showToastAtCenter(context, "发送礼物成功");
                if (iSendGiftSuccessListener != null) iSendGiftSuccessListener.onSendGiftSuccess();
                tv_dialog_sendgift.setText("已发送，再发一个");
                videodetail_sendgift_sun.setVisibility(View.VISIBLE);
                iv_videodetail_sendgift_fail.setVisibility(View.GONE);
                tv_dialog_sendgifterror.setVisibility(View.GONE);
                startAnimation();
                Intent intent = new Intent(ConstantsValue.IntentFilterAction.VIDEO_GIFT_COUNT_ACTION);
                intent.putExtra(ConstantsValue.IntentFilterAction.VIDEO_ID_KEY, videoDetailBean.getVideo_id());
                context.sendBroadcast(intent);
                tv_dialog_sendgift.setEnabled(true);
            }

            @Override
            public void onFailure(String content) {
                tv_dialog_sendgifterror.setVisibility(View.VISIBLE);
                tv_dialog_sendgift.setText("重新发送");
                iv_videodetail_sendgift_fail.setVisibility(View.VISIBLE);
                tv_dialog_sendgift.setEnabled(true);
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                tv_dialog_sendgifterror.setVisibility(View.VISIBLE);
                tv_dialog_sendgift.setText("重新发送");
                iv_videodetail_sendgift_fail.setVisibility(View.VISIBLE);
                tv_dialog_sendgift.setEnabled(true);
            }
        });
    }

    /**
     * 设置选中选中项记录集合
     *
     * @param size 新增加数量
     */
    private void setSelectList(int size) {
        for (int i = 0; i < size; i++) {
            selectList.add(0);
        }
    }

    /**
     * 设置选中项并更新
     *
     * @param selectItemIndex 选中position
     */
    private void setSelcetedItem(int selectItemIndex) {
        lastSelectIndex = currentSelectIndex;
        currentSelectIndex = selectItemIndex;
        selectList.set(lastSelectIndex, 0);
        selectList.set(selectItemIndex, 1);
        ImageLoader.getInstance().displayImage(sendGiftBeans.get(selectItemIndex).getIcon_uri(), iv_dialog_sendgift, DisplayOptionsUtils.getSilenceDisplayBuilder());
    }

    /**
     * 根据选项设置数据
     */
    public void setSendData(int index) {
        tv_dialog_sendgifttitle.setText(sendGiftBeans.get(index).getGift_name());
        if (!TextUtils.isEmpty(sendGiftBeans.get(index).getRemain_count())
                && !TextUtils.equals("null", sendGiftBeans.get(index).getRemain_count())) {
            if (Integer.parseInt(sendGiftBeans.get(index).getRemain_count()) == -1) {
                tv_dialog_sendgiftcount.setText("此礼物还剩无限个");
                tv_dialog_sendgifterror.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(sendGiftBeans.get(index).getRemain_count())
                    && !TextUtils.equals("null", sendGiftBeans.get(index).getRemain_count())
                    && Integer.parseInt(sendGiftBeans.get(index).getRemain_count()) == 0) {
                tv_dialog_sendgifterror.setVisibility(View.VISIBLE);
                tv_dialog_sendgiftcount.setText("此礼物还剩" + MHStringUtils.countFormat(sendGiftBeans.get(index).getRemain_count()) + "个");
                tv_dialog_sendgifterror.setText("礼物数为0");
            } else {
                tv_dialog_sendgiftcount.setText("此礼物还剩" + MHStringUtils.countFormat(sendGiftBeans.get(index).getRemain_count()) + "个");
                tv_dialog_sendgifterror.setVisibility(View.GONE);
            }
        }
        ImageLoader.getInstance().displayImage(sendGiftBeans.get(index).getIcon_uri(), iv_dialog_sendgift, DisplayOptionsUtils.getSilenceDisplayBuilder());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dialog_sendgiftcancle://取消
                cancel();
                break;
            case R.id.tv_dialog_sendgift://送礼物
                tv_dialog_sendgift.setEnabled(false);
                sendGift();
                break;
            case R.id.iv_dialog_sendgiftleftarrow://上一个礼物
                if (currentSelectIndex != 0) {
                    setSendData(currentSelectIndex - 1);
                    setSelcetedItem(currentSelectIndex - 1);
                    if (currentSelectIndex - 1 < lm.findFirstVisibleItemPosition() || currentSelectIndex - 1 > lm.findLastVisibleItemPosition()) {
                        lm.scrollToPosition(currentSelectIndex - 1);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.iv_dialog_sendgiftrighttarrow://下一个礼物
                if (currentSelectIndex != sendGiftBeans.size() - 1) {
                    setSendData(currentSelectIndex + 1);
                    setSelcetedItem(currentSelectIndex + 1);
                    if (currentSelectIndex + 1 < lm.findFirstVisibleItemPosition() || currentSelectIndex + 1 > lm.findLastVisibleItemPosition()) {
                        lm.scrollToPosition(currentSelectIndex + 1);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;

        }
    }

    /**
     * 启动动画
     */
    private void startAnimation() {
        if (anim == null) {
            anim = AnimationUtils.loadAnimation(context, R.anim.sendgift_sunroate);
            anim.setInterpolator(new LinearInterpolator());
        }
        videodetail_sendgift_sun.setAnimation(anim);
        anim.start();
//        ObjectAnimator.ofFloat(videodetail_sendgift_sun, "rotation", 0f, 360f)
//        .setDuration(5000)
//        .start();
//        if (animationDrawable == null) {
//            animationDrawable = (AnimationDrawable) context.getResources().getDrawable(R.drawable.anim_sendgiftsun);
//        }
//        videodetail_sendgift_sun.setBackgroundDrawable(animationDrawable);
//        animationDrawable.start();
    }

    private void stopAnimation() {
        if (animationDrawable != null) {
            videodetail_sendgift_sun.clearAnimation();
        }
    }

    @Override
    public void onItemClick(int position) {
        setSendData(position);
        setSelcetedItem(position);
        adapter.notifyDataSetChanged();
    }

    public interface ISendGiftSuccessListener {
        void onSendGiftSuccess();
    }

    public void setOnSendGiftSuccessListener(ISendGiftSuccessListener iSendGiftSuccessListener) {
        this.iSendGiftSuccessListener = iSendGiftSuccessListener;
    }
}
