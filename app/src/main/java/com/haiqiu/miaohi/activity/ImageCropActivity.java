package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.utils.BitmapUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.CropZoomableImageView;
import com.haiqiu.miaohi.widget.RatioView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * Created by zhandalin on 2016-12-17 10:28.
 * 说明:选择本地图片裁切
 */
public class ImageCropActivity extends BaseActivity implements View.OnClickListener {

    private CommonNavigation navigation;
    private CropZoomableImageView crop_image_view;
    private RelativeLayout rl_grid_view;
    private RatioView ratio_view_1_1;
    private RatioView ratio_view_3_4;
    private RatioView ratio_view_4_3;
    private RatioView ratio_view_16_9;
    private VideoUploadInfo videoUploadInfo;
    private RatioView lastSelectedRatioView;
    private int screenWidth;
    private View content_view;
    private String videoParentPath;
    private View ll_grid_view_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        videoUploadInfo = getIntent().getParcelableExtra("videoUploadInfo");
        if (null == videoUploadInfo) videoUploadInfo = new VideoUploadInfo();

        initView();
    }

    private void initView() {
        screenWidth = ScreenUtils.getScreenWidth(context);
        navigation = (CommonNavigation) findViewById(R.id.navigation);

        crop_image_view = (CropZoomableImageView) findViewById(R.id.crop_image_view);
        content_view = findViewById(R.id.content_view);
        ImageLoader.getInstance().displayImage("file://" + videoUploadInfo.getPictureSrcPath(), crop_image_view, DisplayOptionsUtils.getSilenceDisplayBuilder());

        ll_grid_view_parent = findViewById(R.id.ll_grid_view_parent);
        rl_grid_view = (RelativeLayout) findViewById(R.id.rl_grid_view);
        ViewGroup.LayoutParams layoutParams = rl_grid_view.getLayoutParams();
        layoutParams.height = layoutParams.width = screenWidth;

        ratio_view_1_1 = (RatioView) findViewById(R.id.ratio_view_1_1);
        ratio_view_1_1.setSelected(true);
        lastSelectedRatioView = ratio_view_1_1;
        ratio_view_3_4 = (RatioView) findViewById(R.id.ratio_view_3_4);
        ratio_view_4_3 = (RatioView) findViewById(R.id.ratio_view_4_3);
        ratio_view_16_9 = (RatioView) findViewById(R.id.ratio_view_16_9);

        ratio_view_1_1.setOnClickListener(this);
        ratio_view_3_4.setOnClickListener(this);
        ratio_view_4_3.setOnClickListener(this);
        ratio_view_16_9.setOnClickListener(this);
        navigation.setOnRightLayoutClickListener(new CommonNavigation.OnRightLayoutClick() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = crop_image_view.clip(lastSelectedRatioView.getRatioPoint());
                if (null == bitmap) {
                    showLoading("处理出错啦");
                    return;
                }
                String picPath = videoParentPath + "/pic_" + System.currentTimeMillis() + ".jpg";
                boolean result = BitmapUtil.saveBitmapToSDcard(bitmap, picPath);

                if (!result) {
                    showLoading("处理出错啦");
                    return;
                }
                Intent intent = new Intent(context, PicturePreviewActivity.class);
                videoUploadInfo.setPictureWidth(bitmap.getWidth());
                videoUploadInfo.setPictureHeight(bitmap.getHeight());
                videoUploadInfo.setPicturePath(picPath);
                videoUploadInfo.setPictureSrcPath(picPath);
                intent.putExtra("videoUploadInfo", videoUploadInfo);
                startActivity(intent);
                bitmap.recycle();
//                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (lastSelectedRatioView == v) return;
        ViewGroup.LayoutParams layoutParams = rl_grid_view.getLayoutParams();
        ViewGroup.LayoutParams contentParams = content_view.getLayoutParams();
        contentParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        contentParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        switch (v.getId()) {
            case R.id.ratio_view_1_1:
                if (null != lastSelectedRatioView)
                    lastSelectedRatioView.setSelected(false);
                ratio_view_1_1.setSelected(true);
                crop_image_view.setRatio(new Point(1, 1));
                lastSelectedRatioView = ratio_view_1_1;
                layoutParams.height = layoutParams.width = screenWidth;
                break;
            case R.id.ratio_view_3_4:
                if (null != lastSelectedRatioView)
                    lastSelectedRatioView.setSelected(false);
                ratio_view_3_4.setSelected(true);
                crop_image_view.setRatio(new Point(3, 4));
                lastSelectedRatioView = ratio_view_3_4;
                layoutParams.width = ll_grid_view_parent.getHeight() * 3 / 4;
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

                contentParams.width = ll_grid_view_parent.getHeight() * 3 / 4;
                contentParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

                break;
            case R.id.ratio_view_4_3:
                if (null != lastSelectedRatioView)
                    lastSelectedRatioView.setSelected(false);
                ratio_view_4_3.setSelected(true);
                crop_image_view.setRatio(new Point(4, 3));
                lastSelectedRatioView = ratio_view_4_3;
                layoutParams.width = screenWidth;
                layoutParams.height = screenWidth * 3 / 4;
                break;
            case R.id.ratio_view_16_9:
                if (null != lastSelectedRatioView)
                    lastSelectedRatioView.setSelected(false);
                ratio_view_16_9.setSelected(true);
                crop_image_view.setRatio(new Point(16, 9));
                lastSelectedRatioView = ratio_view_16_9;
                layoutParams.width = screenWidth;
                layoutParams.height = screenWidth * 9 / 16;
                break;
        }
        rl_grid_view.setLayoutParams(layoutParams);
        content_view.setLayoutParams(contentParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoParentPath = videoUploadInfo.getFilesParent();
        //表示视频不是我们录制的视频,或者视频地址不存在
        if (MHStringUtils.isEmpty(videoParentPath) || !videoParentPath.contains(ConstantsValue.Video.VIDEO_TEMP_PATH)) {
            videoParentPath = ConstantsValue.Video.VIDEO_TEMP_PATH + "/temp_" + System.currentTimeMillis();
            File parentFile = new File(videoParentPath);
            if (!parentFile.exists()) parentFile.mkdirs();

            videoUploadInfo.setFilesParent(videoParentPath);
            MHLogUtil.d(TAG, "不是我们平台的图片");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MHLogUtil.d(TAG, "onDestroy");
    }
}
