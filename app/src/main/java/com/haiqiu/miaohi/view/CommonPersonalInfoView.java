package com.haiqiu.miaohi.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.CommonPersonInfo;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;

/**
 * 用户信息view
 * Created by ningl on 16/12/1.
 */
public class CommonPersonalInfoView extends LinearLayout {
    private Context context;
    private MyCircleView mcv_commompersonalinfo_head;
    private TextView tv_commonpersonalinfo_nodescribename;
    private TextView tv_commonpersonalinfo_name;
    private TextView tv_commonpersonalinfo_describe;
    private ImageView iv_commonpersonalinfo_vip;
    private TextView tv_commonpersonalinfo_time;
    private TextView tv_commonpersonalinfo_attention;
    private ImageView iv_deletevideoandimg;
    private TextView tv_commonpersonalinfo_nodescribetime;
    private ImageView iv_commonpersonalinfo_gender;
    private ImageView iv_commonpersonalinfo_qaauthotic;
    private ImageView iv_nodescribe_gender;
    private ImageView iv_nodescribe_qaauthotic;
    private boolean isAttention;
    private int contentType;
    private OnAttentionListener onAttentionListener;
    private OnClickHeadListerner onClickHeadListerner;
    private OnClickDeleteListener onClickDeleteListener;
    private OnClickListener onClickListener;

    public CommonPersonalInfoView(Context context) {
        this(context, null);
    }

    public CommonPersonalInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonPersonalInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        View.inflate(context, R.layout.commonpersonalinfo, this);
        this.context=context;
        mcv_commompersonalinfo_head = (MyCircleView) findViewById(R.id.mcv_commompersonalinfo_head);
        tv_commonpersonalinfo_nodescribename = (TextView) findViewById(R.id.tv_commonpersonalinfo_nodescribename);
        tv_commonpersonalinfo_name = (TextView) findViewById(R.id.tv_commonpersonalinfo_name);
        tv_commonpersonalinfo_describe = (TextView) findViewById(R.id.tv_commonpersonalinfo_describe);
        tv_commonpersonalinfo_time = (TextView) findViewById(R.id.tv_commonpersonalinfo_time);
        iv_commonpersonalinfo_vip = (ImageView) findViewById(R.id.iv_commonpersonalinfo_vip);
        tv_commonpersonalinfo_attention = (TextView) findViewById(R.id.tv_commonpersonalinfo_attention);
        iv_deletevideoandimg = (ImageView) findViewById(R.id.iv_deletevideoandimg);
        tv_commonpersonalinfo_nodescribetime = (TextView) findViewById(R.id.tv_commonpersonalinfo_nodescribetime);
        iv_commonpersonalinfo_gender = (ImageView) findViewById(R.id.iv_commonpersonalinfo_gender);
        iv_commonpersonalinfo_qaauthotic = (ImageView) findViewById(R.id.iv_commonpersonalinfo_qaauthotic);
        iv_nodescribe_gender = (ImageView) findViewById(R.id.iv_nodescribe_gender);
        iv_nodescribe_qaauthotic = (ImageView) findViewById(R.id.iv_nodescribe_qaauthotic);
        SetClickStateUtil.getInstance().setStateListener(iv_deletevideoandimg);

        tv_commonpersonalinfo_attention.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_commonpersonalinfo_attention.isSelected()){
                    final CommonDialog commonDialog = new CommonDialog(getContext());
                    commonDialog.setContentMsg("确定要取消关注吗？");
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
                            if(onAttentionListener!=null)
                                onAttentionListener.onAttention();
                            commonDialog.dismiss();
                            if(contentType==1)
                                TCAgent.onEvent(context,"视频已关注"+ConstantsValue.android);
                            else if(contentType==2)
                                TCAgent.onEvent(context,"图片已关注"+ConstantsValue.android);
                            else if(contentType==3)
                                TCAgent.onEvent(context,"映答已关注"+ConstantsValue.android);
                        }
                    });
                    commonDialog.show();
                } else {
                    if(onAttentionListener!=null) {
                        onAttentionListener.onAttention();
                        if(contentType==1)
                            TCAgent.onEvent(context,"视频关注"+ConstantsValue.android);
                        else if(contentType==2)
                            TCAgent.onEvent(context,"图片关注"+ConstantsValue.android);
                        else if(contentType==3)
                            TCAgent.onEvent(context,"映答关注"+ConstantsValue.android);
                    }
                }



            }
        });
        iv_deletevideoandimg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickDeleteListener!=null) {
                    onClickDeleteListener.onClickDelete();
                    if(contentType==1)
                        TCAgent.onEvent(context,"视频更多"+ConstantsValue.android);
                    else if(contentType==2)
                        TCAgent.onEvent(context,"图片更多"+ConstantsValue.android);
                    else if(contentType==3)
                        TCAgent.onEvent(context,"映答更多"+ConstantsValue.android);
                }
            }
        });
    }

    /**
     * 设置用户信息
     * @param info
     */
    public void setUserInfo(final CommonPersonInfo info){
        if(info == null) return;
        contentType=info.getContentType();
        if(MHStringUtils.isEmpty(info.getDescribe())){//只有用户名 无描述
            tv_commonpersonalinfo_name.setVisibility(GONE);
            tv_commonpersonalinfo_time.setVisibility(GONE);
            tv_commonpersonalinfo_describe.setVisibility(GONE);
            tv_commonpersonalinfo_nodescribetime.setVisibility(VISIBLE);
            tv_commonpersonalinfo_nodescribename.setVisibility(VISIBLE);
            tv_commonpersonalinfo_nodescribename.setText(info.getName());
            iv_nodescribe_gender.setVisibility(info.isShownGender()?VISIBLE:GONE);
            iv_nodescribe_qaauthotic.setVisibility(info.isShownQA()?VISIBLE:GONE);
            iv_nodescribe_gender.setImageResource(info.getGender()==1?R.drawable.gender_man:R.drawable.gender_women);
            if(MHStringUtils.isEmpty(info.getTime())){
                tv_commonpersonalinfo_nodescribetime.setVisibility(View.GONE);
            } else {
                tv_commonpersonalinfo_nodescribetime.setVisibility(View.VISIBLE);
            }
        } else {//用户名+描述
            tv_commonpersonalinfo_name.setVisibility(VISIBLE);
            tv_commonpersonalinfo_describe.setVisibility(VISIBLE);
            tv_commonpersonalinfo_nodescribename.setVisibility(GONE);
            tv_commonpersonalinfo_nodescribetime.setVisibility(View.GONE);
            iv_commonpersonalinfo_gender.setVisibility(info.isShownGender()?VISIBLE:GONE);
            iv_commonpersonalinfo_qaauthotic.setVisibility(info.isShownQA()?VISIBLE:GONE);
            iv_commonpersonalinfo_gender.setImageResource(info.getGender()==1?R.drawable.gender_man:R.drawable.gender_women);
            if(MHStringUtils.isEmpty(info.getTime())){
                tv_commonpersonalinfo_time.setVisibility(View.GONE);
            } else {
                tv_commonpersonalinfo_time.setVisibility(View.VISIBLE);
            }
        }
        tv_commonpersonalinfo_nodescribetime.setText(info.getTime());
        tv_commonpersonalinfo_time.setText(info.getTime());
        if(info.isVip()) iv_commonpersonalinfo_vip.setVisibility(VISIBLE);
        else iv_commonpersonalinfo_vip.setVisibility(GONE);
        if(info.getHeadUri()!=null){
            String headerUri = null;
            if(info.getHeadUri()!=null&&info.getHeadUri().contains("?")){
                headerUri = info.getHeadUri() + "&" + ConstantsValue.Other.HEADER_PARAM;
            } else {
                headerUri = info.getHeadUri() + "?" + ConstantsValue.Other.HEADER_PARAM;
            }
            ImageLoader.getInstance().displayImage(headerUri, mcv_commompersonalinfo_head, DisplayOptionsUtils.getSilenceDisplayBuilder());
        }
        onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(getContext() instanceof BaseActivity){
                        if(((BaseActivity)getContext()).isLogin(false)){
                            if(onClickHeadListerner!=null) onClickHeadListerner.onClickHead();
                            else context.startActivity(new Intent(context, PersonalHomeActivity.class)
                                    .putExtra("userId",info.getUserId()));
                            if(info.getContentType()==1)
                                TCAgent.onEvent(context,"视频头像"+ConstantsValue.android);
                            else if(info.getContentType()==2)
                                TCAgent.onEvent(context,"图片头像"+ConstantsValue.android);
                            else if(info.getContentType()==3)
                                TCAgent.onEvent(context,"映答头像"+ConstantsValue.android);
                        }
                    }
            }
        };

        mcv_commompersonalinfo_head.setOnClickListener(onClickListener);
        tv_commonpersonalinfo_name.setOnClickListener(onClickListener);
        tv_commonpersonalinfo_nodescribename.setOnClickListener(onClickListener);
        tv_commonpersonalinfo_describe.setOnClickListener(onClickListener);
        tv_commonpersonalinfo_nodescribename.setText(info.getName());
        tv_commonpersonalinfo_name.setText(info.getName());
        tv_commonpersonalinfo_describe.setText(info.getDescribe());
        tv_commonpersonalinfo_time.setText(info.getTime());
        tv_commonpersonalinfo_nodescribetime.setText(info.getTime());
    }

    /**
     * 设置右侧view
     */
    public void setRightView(View view){
        addView(view);
    }

    /**
     * 切换关注状态
     * @param isAttention
     */
    public void setAttention(boolean isAttention){
        if(isAttention){//已关注
            tv_commonpersonalinfo_attention.setTextColor(Color.parseColor("#c4c4c4"));
            tv_commonpersonalinfo_attention.setText("已关注");
        } else {//未关注
            tv_commonpersonalinfo_attention.setTextColor(Color.parseColor("#00a2ff"));
            tv_commonpersonalinfo_attention.setText("关注");
        }
        tv_commonpersonalinfo_attention.setSelected(isAttention);
    }

    /**
     * 是否显示关注按钮
     * @param isShowAttention
     */
    public void isShowAttentionBtn(boolean isShowAttention){
        tv_commonpersonalinfo_attention.setVisibility(isShowAttention?VISIBLE:GONE);
    }

    /**
     * 是否显示删除按钮
     * @param isShowDelete
     */
    public void isShowDelete(boolean isShowDelete){
        iv_deletevideoandimg.setVisibility(isShowDelete?VISIBLE:GONE);
    }

    /**
     * 设置字体
     */
    public void setText(String text){
        tv_commonpersonalinfo_attention.setText(text);
    }

    /**
     *是否显示性别
     * @param isShowGender
     */
    public void setGenderEnable(boolean isShowGender){
        iv_commonpersonalinfo_gender.setVisibility(isShowGender?VISIBLE:GONE);
    }

    /**
     * 设置性别
     * @param gender 1：男 2：女
     */
    public void setGneder(int gender){
        iv_commonpersonalinfo_gender.setImageResource(gender==1?R.drawable.gender_man:R.drawable.gender_women);
    }

    /**
     * 是否显示应答权限
     * @param isShowQAAuthotic
     */
    public void setQAAutotic(boolean isShowQAAuthotic){
//        iv_commonpersonalinfo_qaauthotic.setVisibility(isShowQAAuthotic?VISIBLE:GONE);
    }

//    /**
//     * 是否显示上传时间
//     * @param isShowUploadTime
//     */
//    public void setUploadTimeEnable(boolean isShowUploadTime){
//        tv_commonpersonalinfo_time.setVisibility(isShowUploadTime?VISIBLE:GONE);
//        tv_commonpersonalinfo_nodescribetime.setVisibility(isShowUploadTime?VISIBLE:GONE);
//    }

    /**
     * 点击关注
     */
    public interface OnAttentionListener{
        void onAttention();
    }

    public void setOnAttentionListener(OnAttentionListener onAttentionListener){
        this.onAttentionListener = onAttentionListener;
    }

    /**
     * 点击头部
     */
    public interface OnClickHeadListerner{
        void onClickHead();
    }

//    public void setOnClickHeadListerner(OnClickHeadListerner onClickHeadListerner){
//        this.onClickHeadListerner = onClickHeadListerner;
//    }

    /**
     * 点击删除按钮
     */
    public interface OnClickDeleteListener{
        void onClickDelete();
    }

    public void setOnClickDeleteListener(OnClickDeleteListener onClickDeleteListener){
        this.onClickDeleteListener = onClickDeleteListener;
    }

//    /**
//     * 设置右侧view(圆角)
//     */
//    public void setRightView(CommonPersonalInfo_RightViewStyle style){
//        TextView tv = new TextView(getContext());
//        tv.setGravity(Gravity.CENTER);
//        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, style.getTextSize());
//        getBgDrawable(style);
//    }
//
//    /**
//     * 获取drawable
//     * @param style
//     */
//    public void getBgDrawable(CommonPersonalInfo_RightViewStyle style){
//        GradientDrawable drawable = new GradientDrawable();
//        drawable.setCornerRadius(style.getRadius());
//        drawable.setStroke(style.getStrokeWidth(), style.getStrokeColor());
//        drawable.setSize(style.getWith(), style.getHeight());
//    }
}
