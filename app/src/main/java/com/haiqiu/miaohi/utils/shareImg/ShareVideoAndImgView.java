package com.haiqiu.miaohi.utils.shareImg;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.ShareVideoAndImgInfo;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 分享视频和图片view
 * Created by ningl on 16/12/24.
 */

public class ShareVideoAndImgView extends AbsShareImg {

    private ImageView iv_sharecontent;
    private RelativeLayout rl_sharecontentlayout;
    private LinearLayout ll_sharevideoandimg_top;
    private TextView tv_sharevideoandimg_describe;
    private ImageView iv_sharevideoandimg_qrcode;
    private LinearLayout ll_sharevideoandimg_bottom;
    private FrameLayout fl_sharevideoandimg_top;
    private TextView tv_sharevideoandimg_name;
    private TextView tv_sharevideoandimg_time;
    private LinearLayout ll_sharevideo;
    private TextView tv_videoandimg_tip;
    private TextView tv_videoandimgtype_tip;
    private final int VIDEO = 1;
    private final int IMG = 2;

    public ShareVideoAndImgView(Context context) {
        super(context);
    }

    public ShareVideoAndImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShareVideoAndImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void genderImage(final ShareVideoAndImgInfo videoAndImgInfo, SHARE_MEDIA platform) {
        if(platform == SHARE_MEDIA.QZONE){
            if(onLoadFinish != null) onLoadFinish.onFinish(R.mipmap.ic_launcher);
            return;
        }
        if(videoAndImgInfo == null) return;
        View view = View.inflate(getContext(), R.layout.share_videoandimg_img, null);
        initView(view);
        if(getChildCount()>0) {
            removeAllViews();
        }
        addView(view);
        ll_sharevideo.setVisibility(videoAndImgInfo.getType() == VIDEO?VISIBLE:GONE);
        iv_sharevideoandimg_qrcode.setImageBitmap(getQRBitmap(videoAndImgInfo.getQaCode_str()));
        tv_sharevideoandimg_describe.setText(videoAndImgInfo.getNote());
        tv_sharevideoandimg_name.setText(videoAndImgInfo.getName());
        tv_sharevideoandimg_time.setText("于"+videoAndImgInfo.getJoinTime());
        if(platform == SHARE_MEDIA.SINA){
            if(videoAndImgInfo.getType() == IMG){
                tv_videoandimg_tip.setText("扫描二维码查看更多");
                tv_videoandimgtype_tip.setText("扫描二维码查看更多");
            } else {
                tv_videoandimg_tip.setText("扫描二维码可播放");
                tv_videoandimgtype_tip.setText("扫描二维码可播放");
            }
        } else {
            if(videoAndImgInfo.getType() == IMG){
                tv_videoandimg_tip.setText("长按二维码查看更多");
                tv_videoandimgtype_tip.setText("长按二维码查看更多");
            } else {
                tv_videoandimg_tip.setText("长按二维码可播放");
                tv_videoandimgtype_tip.setText("长按二维码可播放");
            }
        }
//        LinearLayout.LayoutParams lp1 = (LayoutParams) rl_sharecontentlayout.getLayoutParams();
//        lp1.height = ScreenUtils.getScreenWidth(getContext()) - DensityUtil.dip2px(getContext(), 30);
//        rl_sharecontentlayout.setLayoutParams(lp1);

        //设置图片尺寸
        RelativeLayout.LayoutParams contentLP = (RelativeLayout.LayoutParams) iv_sharecontent.getLayoutParams();
        double rat;
        double standardSize = ScreenUtils.getScreenWidth(getContext()) - DensityUtil.dip2px(getContext(), 30);
        if(videoAndImgInfo.getHeight() == 0
                ||videoAndImgInfo.getWidth() == 0){
            MHLogUtil.i("videoAndImgInfo.getHeight()");
            rat = 1;
            contentLP.width = (int)standardSize;
            contentLP.height = (int)standardSize;
        } else if(videoAndImgInfo.getWidth()>=videoAndImgInfo.getHeight()){
            //宽大于高
            rat = videoAndImgInfo.getHeight()/videoAndImgInfo.getWidth();
            contentLP.width = (int)standardSize;
            contentLP.height = (int)(standardSize*rat);
        } else if(videoAndImgInfo.getWidth()<videoAndImgInfo.getHeight()){
            //高大于宽
            rat = videoAndImgInfo.getWidth()/videoAndImgInfo.getHeight();
            contentLP.width = (int) (standardSize*rat);
            contentLP.height = (int) standardSize;
        }
        iv_sharecontent.setLayoutParams(contentLP);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ll_sharevideoandimg_top.getLayoutParams();
        int height = ScreenUtils.getScreenWidth(getContext()) + DensityUtil.dip2px(getContext(), 40);
        lp.height = height;
        ll_sharevideoandimg_top.setLayoutParams(lp);
        String url = null;
        if(videoAndImgInfo.getImgUrl()!=null){
            if(videoAndImgInfo.getImgUrl().contains("?")){
                url = videoAndImgInfo.getImgUrl()+ "/imageView2/0/w/320/h/320";
            } else {
                url = videoAndImgInfo.getImgUrl()+ "?imageView2/0/w/320/h/320";
            }
        } else {
            genderImgPath();
            return;
        }
        ImageLoader.getInstance().displayImage(url, iv_sharecontent, DisplayOptionsUtils.getDefaultMaxRectImageOptions(), new SimpleImageLoadingListener(){
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
        iv_sharecontent = (ImageView) view.findViewById(R.id.iv_sharecontent);
        rl_sharecontentlayout = (RelativeLayout) view.findViewById(R.id.rl_sharecontentlayout);
        ll_sharevideoandimg_top = (LinearLayout) view.findViewById(R.id.ll_sharevideoandimg_top);
        tv_sharevideoandimg_describe = (TextView) view.findViewById(R.id.tv_sharevideoandimg_describe);
        iv_sharevideoandimg_qrcode = (ImageView) view.findViewById(R.id.iv_sharevideoandimg_qrcode);
        ll_sharevideoandimg_bottom = (LinearLayout) view.findViewById(R.id.ll_sharevideoandimg_bottom);
        fl_sharevideoandimg_top = (FrameLayout) view.findViewById(R.id.fl_sharevideoandimg_top);
        tv_sharevideoandimg_name = (TextView) view.findViewById(R.id.tv_sharevideoandimg_name);
        tv_sharevideoandimg_time = (TextView) view.findViewById(R.id.tv_sharevideoandimg_time);
        ll_sharevideo = (LinearLayout) view.findViewById(R.id.ll_sharevideo);
        tv_videoandimg_tip = (TextView) view.findViewById(R.id.tv_videoandimg_tip);
        tv_videoandimgtype_tip = (TextView) view.findViewById(R.id.tv_videoandimgtype_tip);
    }

    /**
     * 获取最后生成的图片地址
     */
    private void genderImgPath(){
        genderPath(fl_sharevideoandimg_top, ll_sharevideoandimg_bottom, new ViewToImageUtil.OnImageSavedCallback() {
            @Override
            public void onFinishCallback(String path) {
                onLoadFinish.onFinish(path);
            }
        });
    }
}
