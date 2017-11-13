package com.haiqiu.miaohi.utils.shareImg;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.ShareVideoAndImgInfo;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.RoundedBackgroundSpan;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.TimeFormatUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 应答详情分享view
 * Created by ningl on 16/12/24.
 */

public class ShareQADetailView extends AbsShareImg {

    private TextView tv_shareqadetail_question;
    private LinearLayout ll_qadetail_top;
    private TextView tv_shareqadetail_name;
    private TextView tv_shareqadetail_time;
    private ImageView iv_shareqadetail_qrcode;
    private LinearLayout ll_qadetail_bottom;
    private ImageView iv_qadetail_content;
    private LinearLayout ll_shareqadetail_outside;
    private RelativeLayout rl_shareqadetail_imgsize;
    private TextView tv_qadetail_tip;
    private TextView tv_qadetailtype_tip;

    public ShareQADetailView(Context context) {
        super(context);
    }

    public ShareQADetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShareQADetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void genderImage(ShareVideoAndImgInfo videoAndImgInfo, SHARE_MEDIA platform) {
        if(platform == SHARE_MEDIA.QZONE){
            if(onLoadFinish != null) onLoadFinish.onFinish(R.mipmap.ic_launcher);
            return;
        }
        if(videoAndImgInfo == null) return;
        View view = View.inflate(getContext(), R.layout.share_qadetail_img, null);
        initView(view);
        addView(view);
        iv_shareqadetail_qrcode.setImageBitmap(getQRBitmap(videoAndImgInfo.getQaCode_str()));
        tv_shareqadetail_question.setText(getQuestionNote(videoAndImgInfo.getNote()));
        tv_shareqadetail_name.setText(videoAndImgInfo.getName());
        if(videoAndImgInfo.getAnswerTime() == 0){
            tv_shareqadetail_time.setText("于1970/01/01回答");
        } else {
            tv_shareqadetail_time.setText("于"+TimeFormatUtils.formatYMD(videoAndImgInfo.getAnswerTime())+"回答");
        }
        if(platform == SHARE_MEDIA.SINA){
            tv_qadetail_tip.setText("扫描二维码可围观映答");
            tv_qadetailtype_tip.setText("扫描二维码可围观映答");
        } else {
            tv_qadetail_tip.setText("长按二维码可围观映答");
            tv_qadetailtype_tip.setText("长按二维码可围观映答");
        }
        LinearLayout.LayoutParams outsideLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int width = ScreenUtils.getScreenWidth(getContext());
        outsideLp.width = width;
        outsideLp.height = width - DensityUtil.dip2px(getContext(), 30);
        ll_shareqadetail_outside.setLayoutParams(outsideLp);

        //设置图片尺寸
        LinearLayout.LayoutParams contentLP = (LayoutParams) rl_shareqadetail_imgsize.getLayoutParams();
        double rat;
        double standardSize = ScreenUtils.getScreenWidth(getContext()) - DensityUtil.dip2px(getContext(), 30);
        contentLP.height = (int)standardSize;
        rl_shareqadetail_imgsize.setLayoutParams(contentLP);


        RelativeLayout.LayoutParams imgLp = (RelativeLayout.LayoutParams) iv_qadetail_content.getLayoutParams();
        if(videoAndImgInfo.getHeight()==0.0
                ||videoAndImgInfo.getWidth() == 0.0
                ||videoAndImgInfo.getWidth()==videoAndImgInfo.getHeight()){
            rat = 1;
            imgLp.width = (int)standardSize;
            imgLp.height = (int)standardSize;
        } else if(videoAndImgInfo.getWidth()>=videoAndImgInfo.getHeight()){
            //宽大于高
            rat = videoAndImgInfo.getHeight()/videoAndImgInfo.getWidth();
            imgLp.width = (int)standardSize;
            imgLp.height = (int)(standardSize*rat);
        } else if(videoAndImgInfo.getWidth()<videoAndImgInfo.getHeight()){
            //高大于宽
            rat = videoAndImgInfo.getWidth()/videoAndImgInfo.getHeight();
            imgLp.width = (int) (standardSize*rat);
            imgLp.height = (int) standardSize;
        }
        iv_qadetail_content.setLayoutParams(imgLp);

        ImageLoader.getInstance().displayImage(videoAndImgInfo.getImgUrl(), iv_qadetail_content ,new SimpleImageLoadingListener(){
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                genderImgPath();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                genderImgPath();
            }
        });
    }

    private void initView(View view) {
        tv_shareqadetail_question = (TextView) view.findViewById(R.id.tv_shareqadetail_question);
        ll_qadetail_top = (LinearLayout) view.findViewById(R.id.ll_qadetail_top);
        tv_shareqadetail_name = (TextView) view.findViewById(R.id.tv_shareqadetail_name);
        tv_shareqadetail_time = (TextView) view.findViewById(R.id.tv_shareqadetail_time);
        iv_shareqadetail_qrcode = (ImageView) view.findViewById(R.id.iv_shareqadetail_qrcode);
        ll_qadetail_bottom = (LinearLayout) view.findViewById(R.id.ll_qadetail_bottom);
        iv_qadetail_content = (ImageView) view.findViewById(R.id.iv_qadetail_content);
        ll_shareqadetail_outside = (LinearLayout) view.findViewById(R.id.ll_shareqadetail_outside);
        rl_shareqadetail_imgsize = (RelativeLayout) view.findViewById(R.id.rl_shareqadetail_imgsize);
        tv_qadetail_tip = (TextView) view.findViewById(R.id.tv_qadetail_tip);
        tv_qadetailtype_tip = (TextView) view.findViewById(R.id.tv_qadetailtype_tip);
    }

    /**
     * 获取最后生成的图片地址
     */
    private void genderImgPath(){
        genderPath(ll_qadetail_top, ll_qadetail_bottom, new ViewToImageUtil.OnImageSavedCallback() {
            @Override
            public void onFinishCallback(String path) {
                onLoadFinish.onFinish(path);
            }
        });
    }

    /**
     * 获取映答问题
     * @param question
     * @return
     */
    private SpannableStringBuilder getQuestionNote(String question){
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        RoundedBackgroundSpan span = new RoundedBackgroundSpan(ContextCompat.getColor(getContext(), R.color.color_1b), ContextCompat.getColor(getContext(), R.color.white));
        stringBuilder.append(" 映答 ");
        stringBuilder.setSpan(span, stringBuilder.length() - 4, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append(" "+question);
        return stringBuilder;
    }
}
