package com.haiqiu.miaohi.utils.shareImg;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.ShareVideoAndImgInfo;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.view.MyCircleView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 分享视频和图片新样式
 * Created by ningl on 17/3/1.
 */

public class ShareVideoAndImgView2 extends ShareVideoAndImgView {

    private final int VIDEO = 1;
    private final int IMG = 2;
    private ImageView iv_shareimg;
    private MyCircleView cv_shareheader;
    private RelativeLayout rl_shareimgwrap;
    private LinearLayout ll_sharevideoandimg_top2;
    private TextView tv_sharename;
    private ImageView iv_sharevideoandimg_qrcode2;
    private TextView tv_skiptip;
    private LinearLayout ll_sharevideoandimg_bottom2;
    private ImageView iv_sharevideoandimgplaybtn;
    private boolean isHeaderComplete;
    private boolean isImgComplete;

    public ShareVideoAndImgView2(Context context) {
        super(context);
    }

    public ShareVideoAndImgView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShareVideoAndImgView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void genderImage(final ShareVideoAndImgInfo videoAndImgInfo, final SHARE_MEDIA platform) {
        isHeaderComplete = false;
        isImgComplete = false;
        if (platform == SHARE_MEDIA.QZONE
                ||platform == SHARE_MEDIA.SINA) {
            if (onLoadFinish != null) onLoadFinish.onFinish(R.mipmap.ic_launcher);
            return;
        }
        if (videoAndImgInfo == null) return;
        View view = View.inflate(getContext(), R.layout.share_videoandimg_img2, null);
        initView(view);
        if (getChildCount() > 0) {
            removeAllViews();
        }
        addView(view);
        if(platform == SHARE_MEDIA.SINA){
            if(videoAndImgInfo.getType() == IMG){
                tv_skiptip.setText("扫描二维码 查看详细内容");
            } else {
                tv_skiptip.setText("扫描二维码播放视频");
            }
        } else {
            if(videoAndImgInfo.getType() == IMG){
                tv_skiptip.setText("长按识别二维码 查看详细内容");
            } else {
                tv_skiptip.setText("长按识别二维码播放视频");
            }
        }
        iv_sharevideoandimg_qrcode2.setImageBitmap(getQRBitmap(videoAndImgInfo.getQaCode_str()));
        tv_sharename.setText(videoAndImgInfo.getName());
        setSize(videoAndImgInfo);
        if(videoAndImgInfo.getType() == VIDEO) {
            iv_sharevideoandimgplaybtn.setVisibility(VISIBLE);
        }
        String imgUrl = null;
        if(videoAndImgInfo.getImgUrl().contains("?")){
            imgUrl = videoAndImgInfo.getImgUrl()+ "/imageView2/0/w/320/h/320";
        } else {
            imgUrl = videoAndImgInfo.getImgUrl()+ "?imageView2/0/w/320/h/320";
        }
        String headerUrl = null;
        if(videoAndImgInfo.getHeaderUrl().contains("?")){
            headerUrl = videoAndImgInfo.getHeaderUrl()+ "/imageView2/0/w/50/h/50";
        } else {
            headerUrl = videoAndImgInfo.getHeaderUrl()+ "?imageView2/0/w/50/h/50";
        }

        /**
         * 加载头像
         */
        ImageLoader.getInstance().displayImage(headerUrl, cv_shareheader, new SimpleImageLoadingListener(){
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                isHeaderComplete = true;
                if(isImgComplete){
                    genderPath();
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                isHeaderComplete = true;
                if (isImgComplete) {
                    genderPath();
                }
            }
        });
        /**
         * 加载图片
         */
        ImageLoader.getInstance().displayImage(imgUrl, iv_shareimg, new SimpleImageLoadingListener(){
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                isImgComplete = true;
                if(isHeaderComplete){
                    genderPath();
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                isImgComplete = true;
                if(isHeaderComplete){
                    genderPath();
                }
            }
        });


    }

    private void initView(View rootView) {
        iv_shareimg = (ImageView) rootView.findViewById(R.id.iv_shareimg);
        cv_shareheader = (MyCircleView) rootView.findViewById(R.id.cv_shareheader);
        rl_shareimgwrap = (RelativeLayout) rootView.findViewById(R.id.rl_shareimgwrap);
        ll_sharevideoandimg_top2 = (LinearLayout) rootView.findViewById(R.id.ll_sharevideoandimg_top2);iv_shareimg = (ImageView) rootView.findViewById(R.id.iv_shareimg);
        tv_sharename = (TextView) rootView.findViewById(R.id.tv_sharename);
        iv_sharevideoandimg_qrcode2 = (ImageView) rootView.findViewById(R.id.iv_sharevideoandimg_qrcode2);
        tv_skiptip = (TextView) rootView.findViewById(R.id.tv_skiptip);
        ll_sharevideoandimg_bottom2 = (LinearLayout) rootView.findViewById(R.id.ll_sharevideoandimg_bottom2);
        iv_sharevideoandimgplaybtn = (ImageView) rootView.findViewById(R.id.iv_sharevideoandimgplaybtn);
    }

    /**
     * 设置图片尺寸
     * @param videoAndImgInfo
     */
    private void setSize(ShareVideoAndImgInfo videoAndImgInfo){
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) iv_shareimg.getLayoutParams();
        double mWidth = ScreenUtils.getScreenWidth(getContext()) - DensityUtil.dip2px(getContext(), 46);
        if(videoAndImgInfo.getWidth() ==0
                || videoAndImgInfo.getHeight() == 0
                || videoAndImgInfo.getWidth()/videoAndImgInfo.getHeight()>=1){
            lp.height = (int) mWidth;
        } else if(videoAndImgInfo.getWidth()/videoAndImgInfo.getHeight()<1){
            lp.height = (int) (mWidth*(videoAndImgInfo.getWidth()/videoAndImgInfo.getHeight()));
        }
        iv_shareimg.setLayoutParams(lp);
    }


    public void genderPath() {
        genderPath(ll_sharevideoandimg_top2, ll_sharevideoandimg_bottom2, new ViewToImageUtil.OnImageSavedCallback() {
            @Override
            public void onFinishCallback(String path) {
                onLoadFinish.onFinish(path);
            }
        });
    }

}
