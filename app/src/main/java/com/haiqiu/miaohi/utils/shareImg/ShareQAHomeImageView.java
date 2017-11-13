package com.haiqiu.miaohi.utils.shareImg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.OtherQAData;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.view.MyCircleView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 分享映答主页view
 * Created by ningl on 16/12/24.
 */

public class ShareQAHomeImageView extends AbsShareImg {

    private MyCircleView mcv_qahome_head;
    private ImageView iv_qahome_vip;
    private TextView tv_qahome_name;
    private TextView tv_qahome_describe;
    private TextView tv_qahome_authentic;
    private LinearLayout ll_rl_qahome_top;
    private LinearLayout ll_rl_qahome_bottom;
    private TextView tv_shareqahome_circuseed;
    private TextView tv_shareqahome_answered;
    private TextView tv_shareqahome_income;
    private ImageView iv_qahome_qrcode;
    private TextView tv_qahome_tip;
    private TextView tv_askto;

    public ShareQAHomeImageView(Context context) {
        super(context);
    }

    public ShareQAHomeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShareQAHomeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 生成图片
     *
     * @param otherQAData
     */
    public void genderImage(OtherQAData otherQAData, SHARE_MEDIA platform) {
        if(platform == SHARE_MEDIA.QZONE){
            if(onLoadFinish != null) onLoadFinish.onFinish(R.mipmap.ic_launcher);
            return;
        }
        if(otherQAData == null) return;
        View view = View.inflate(getContext(), R.layout.share_qahome_img, null);
        initView(view);
        addView(view);
        iv_qahome_vip.setVisibility(otherQAData.getUser_type()>10?VISIBLE:GONE);
        tv_qahome_name.setText(otherQAData.getUser_name());
        tv_shareqahome_income.setText(CommonUtil.formatPrice(otherQAData.getIncome_amount()));
        tv_qahome_describe.setText(otherQAData.getVip_note());
        tv_qahome_authentic.setText(otherQAData.getUser_note());

        tv_shareqahome_answered.setText(otherQAData.getAnswered_amount());
        tv_shareqahome_circuseed.setText(otherQAData.getObserved_amount());

        Drawable genderDrawable = null;
        if(otherQAData.getUser_gender() == 1){
            genderDrawable = getContext().getResources().getDrawable(R.drawable.gender_man);
            tv_askto.setText("向他提问");
            if(platform == SHARE_MEDIA.SINA){
                tv_qahome_tip.setText("扫描二维码向他提问");
            } else {
                tv_qahome_tip.setText("长按二维码向他提问");
            }
        } else {
            genderDrawable = getContext().getResources().getDrawable(R.drawable.gender_women);
            tv_askto.setText("向她提问");
            if(platform == SHARE_MEDIA.SINA){
                tv_qahome_tip.setText("扫描二维码向她提问");
            } else {
                tv_qahome_tip.setText("长按二维码向她提问");
            }
        }
        genderDrawable.setBounds(0, 0, genderDrawable.getMinimumWidth(), genderDrawable.getMinimumHeight());
        tv_qahome_name.setCompoundDrawables(null, null, genderDrawable, null);

        //设置二维码
        iv_qahome_qrcode.setImageBitmap(getQRBitmap(otherQAData.getShare_link_address()));
        ImageLoader.getInstance().displayImage(otherQAData.getUser_portrait(), mcv_qahome_head, DisplayOptionsUtils.getHeaderDefaultImageOptions(), new SimpleImageLoadingListener(){

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
        ll_rl_qahome_top = (LinearLayout) view.findViewById(R.id.ll_rl_qahome_top);
        ll_rl_qahome_bottom = (LinearLayout) view.findViewById(R.id.ll_rl_qahome_bottom);
        mcv_qahome_head = (MyCircleView) view.findViewById(R.id.mcv_qahome_head);
        iv_qahome_vip = (ImageView) view.findViewById(R.id.iv_qahome_vip);
        tv_qahome_name = (TextView) view.findViewById(R.id.tv_qahome_name);
        tv_qahome_describe = (TextView) view.findViewById(R.id.tv_qahome_describe);
        tv_qahome_authentic = (TextView) view.findViewById(R.id.tv_qahome_authentic);
        tv_shareqahome_circuseed = (TextView) view.findViewById(R.id.tv_shareqahome_circuseed);
        tv_shareqahome_answered = (TextView) view.findViewById(R.id.tv_shareqahome_answered);
        tv_shareqahome_income = (TextView) view.findViewById(R.id.tv_shareqahome_income);
        iv_qahome_qrcode = (ImageView) view.findViewById(R.id.iv_qahome_qrcode);
        tv_qahome_tip = (TextView) view.findViewById(R.id.tv_qahome_tip);
        tv_askto = (TextView) view.findViewById(R.id.tv_askto);
    }

    /**
     * 获取最后生成的图片地址
     */
    private void genderImgPath(){
        genderPath(ll_rl_qahome_top, ll_rl_qahome_bottom, new ViewToImageUtil.OnImageSavedCallback() {
            @Override
            public void onFinishCallback(String path) {
                onLoadFinish.onFinish(path);
            }
        });
    }
}
