package com.haiqiu.miaohi.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.DecalInfo;
import com.haiqiu.miaohi.bean.FilterInfo;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.fragment.DecalFragment;
import com.haiqiu.miaohi.fragment.FilterFragment;
import com.haiqiu.miaohi.utils.BitmapUtil;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.SquareProgressView;
import com.haiqiu.miaohi.widget.decal.DecalView;
import com.haiqiu.miaohi.widget.tablayout.CommonTabLayout;
import com.haiqiu.miaohi.widget.tablayout.CustomTabEntity;
import com.haiqiu.miaohi.widget.tablayout.OnTabSelectListener;
import com.haiqiu.miaohi.widget.tablayout.TabEntity;

import org.wysaid.nativePort.CGENativeLibrary;
import org.wysaid.texUtils.TextureRenderer;
import org.wysaid.view.ImageGLSurfaceView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zhandalin on 2016-12-01 11:05.
 * 说明:图片预览
 */
public class PicturePreviewActivity extends BaseActivity implements View.OnClickListener, FilterFragment.OnFilterSelectListener, DecalFragment.OnDecalSelectedListener, OnTabSelectListener {
    private static final int MSG_WHAT_UPDATE_PROGRESS = 50;

    private VideoUploadInfo videoUploadInfo;
    private ImageGLSurfaceView gl_image_view;
    private ImageView iv_rotate;
    private CommonTabLayout tab_layout;
    private Bitmap srcBitmap;
    private int currentPage;
    private DecalView decal_container;
    private Dialog progressDialog;
    private SquareProgressView square_progress_view;
    private ArrayList<BaseFragment> fragments;
    private volatile float progress;
    private FragmentManager fragmentManager;
    private FilterInfo lastFilterInfo;
    private boolean isEnd;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_UPDATE_PROGRESS:
                    if (null != square_progress_view) {
                        square_progress_view.setProgress(progress / 100);
                        tv_handle_progress.setText(String.format("%.1f", progress) + "%");
                    }
                    break;
            }
            return false;
        }
    });
    private TextView tv_handle_progress;
    private Random random;
    private DecalInfo lastDecalInfo;
    private String decalParentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_preview);
        videoUploadInfo = getIntent().getParcelableExtra("videoUploadInfo");
        if (null == videoUploadInfo || null == videoUploadInfo.getPictureSrcPath()) {
            showToastAtCenter("处理出错啦");
            finish();
        }
        CGENativeLibrary.setLoadImageCallback(loadImageCallback, null);
        decalParentPath = context.getFilesDir() + "/" + ConstantsValue.VideoEdit.PASTER_DIR_NAME + "/";
        initView();
    }


    private void initView() {

        gl_image_view = (ImageGLSurfaceView) findViewById(R.id.gl_image_view);
//        gl_image_view.setClearColor(0.113f, 0.113f, 0.113f, 1);
        iv_rotate = (ImageView) findViewById(R.id.iv_rotate);

        if (videoUploadInfo.getPictureHeight() != 0) {
            float v = 1.0f * videoUploadInfo.getPictureWidth() / videoUploadInfo.getPictureHeight();
            float v1 = 16.0f / 9;
            if (v - 0.1 < v1 && v + 0.1 > v1)//防止误差
                iv_rotate.setVisibility(View.GONE);
        }

        tab_layout = (CommonTabLayout) findViewById(R.id.tab_layout);

        srcBitmap = BitmapUtil.loadBitmap(context, videoUploadInfo.getPictureSrcPath());
        gl_image_view.setDisplayMode(ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FIT);
        gl_image_view.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
            @Override
            public void surfaceCreated() {
                gl_image_view.setImageBitmap(srcBitmap);
                iv_rotate.setOnClickListener(PicturePreviewActivity.this);
            }
        });
        decal_container = (DecalView) findViewById(R.id.decal_container);
        gl_image_view.setOnViewportCalcCompleteListener(new ImageGLSurfaceView.OnViewportCalcCompleteListener() {
            @Override
            public void onViewportCalcComplete(final TextureRenderer.Viewport viewport) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) decal_container.getLayoutParams();
                        layoutParams.topMargin = viewport.y;
                        layoutParams.leftMargin = viewport.x;
                        layoutParams.height = viewport.height;
                        layoutParams.width = viewport.width;
                        decal_container.setLayoutParams(layoutParams);
                    }
                });
            }
        });

        final CommonNavigation navigation = (CommonNavigation) findViewById(R.id.navigation);
        navigation.setNavigationBackground(R.color.transparent_30);
        navigation.setOnRightLayoutClickListener(new CommonNavigation.OnRightLayoutClick() {
            @Override
            public void onClick(View v) {
                showHandleProgressDialog();
            }
        });

        fragments = new ArrayList<>();
        FilterFragment filterFragment = new FilterFragment();
        filterFragment.setOnFilterSelectListener(PicturePreviewActivity.this);
        filterFragment.setImagePath(videoUploadInfo.getPicturePath());
        fragments.add(filterFragment);

        final DecalFragment decalFragment = new DecalFragment();
        decalFragment.setOnDecalSelectedListener(PicturePreviewActivity.this);
        fragments.add(decalFragment);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.fragment_container, filterFragment);
        ft.add(R.id.fragment_container, decalFragment);
        ft.hide(decalFragment);
        ft.commit();

        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        mTabEntities.add(new TabEntity("滤镜", 0, 0));
        mTabEntities.add(new TabEntity("贴纸", 0, 0));
        tab_layout.setTabData(mTabEntities);
        tab_layout.setOnTabSelectListener(this);

        decal_container.setOnDecalDeleteIconClickListener(new DecalView.OnDecalDeleteIconClickListener() {
            @Override
            public void onDeleteIconClick(DecalView singleFingerView) {
                decalFragment.setCurrentSelected(null);
                lastDecalInfo = null;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_rotate:
                srcBitmap = BitmapUtil.rotateBitmap(srcBitmap, 90);
                gl_image_view.setImageBitmap(srcBitmap);
                if (null != lastFilterInfo) {
                    gl_image_view.setFilterWithConfig(lastFilterInfo.getFilter_param());
                    gl_image_view.setFilterIntensity(lastFilterInfo.getIntensity());
                }

                break;

        }
    }

    public void showHandleProgressDialog() {
        if (!isLogin(false)) return;
        isEnd = false;

        progressDialog = new Dialog(context, R.style.Dialog_loading);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        View view = View.inflate(context, R.layout.dialog_handle_progress_layout, null);
        tv_handle_progress = (TextView) view.findViewById(R.id.tv_handle_progress);
        square_progress_view = (SquareProgressView) view.findViewById(R.id.square_progress_view);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) square_progress_view.getLayoutParams();

        int height = (ScreenUtils.getScreenWidth(context) - layoutParams.rightMargin - layoutParams.leftMargin) * srcBitmap.getHeight() / srcBitmap.getWidth();
        int maxHeight = ScreenUtils.getScreenHeight(context) - DensityUtil.dip2px(context, 160);
        layoutParams.height = height;
        if (height > maxHeight) {
            layoutParams.height = maxHeight;
            layoutParams.width = srcBitmap.getWidth() * maxHeight / srcBitmap.getHeight();
        }
        square_progress_view.setLayoutParams(layoutParams);

        square_progress_view.setImageBitmap(srcBitmap);
        square_progress_view.setProgressColor(Color.WHITE);

        TextView tv_handle_info = (TextView) view.findViewById(R.id.tv_handle_info);
        tv_handle_info.setText("正在加工图片...");
        view.findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isEnd = true;
                progressDialog.dismiss();
            }
        });

        progressDialog.setContentView(view, new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(context), ViewGroup.LayoutParams.MATCH_PARENT));
        progressDialog.show();

        handleImage();
    }

    private void threadSleep() {
        handler.sendEmptyMessage(MSG_WHAT_UPDATE_PROGRESS);
        try {
            if (null == random)
                random = new Random(System.currentTimeMillis());
            Thread.sleep(100 + random.nextInt(300));
        } catch (InterruptedException e) {
            MHLogUtil.e(TAG,e);
        }
    }

    private void handleImage() {
        progress = 10;
        final Bitmap resultBitmap = decal_container.getResultBitmap();
        handler.sendEmptyMessage(MSG_WHAT_UPDATE_PROGRESS);
        gl_image_view.getResultBitmap(new ImageGLSurfaceView.QueryResultBitmapCallback() {
            @Override
            public void get(final Bitmap bitmap) {//该bitmap是已经滤过镜了
                progress = 25;
                handler.sendEmptyMessage(MSG_WHAT_UPDATE_PROGRESS);
                if (null == bitmap) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToastAtCenter("处理出错啦");
                        }
                    });
                    return;
                }
                progress = 45;
                threadSleep();
                if (null != resultBitmap) {
                    Bitmap result = BitmapUtil.scaleBitmap(resultBitmap, bitmap.getWidth(), bitmap.getHeight());
                    progress = 65;
                    threadSleep();
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawBitmap(result, 0, 0, new Paint());

                    progress = 80;
                    threadSleep();
                }
                progress = 90;
                threadSleep();

                String path = videoUploadInfo.getFilesParent() + "/filter_" + System.currentTimeMillis() + ".jpg";
                BitmapUtil.saveBitmapToSDcard(bitmap, path);

                progress = 96;
                threadSleep();

                videoUploadInfo.setPicturePath(path);
                videoUploadInfo.setPictureWidth(bitmap.getWidth());
                videoUploadInfo.setPictureHeight(bitmap.getHeight());
                videoUploadInfo.setMediaType(VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE);

                progress = 100;
                handler.sendEmptyMessage(MSG_WHAT_UPDATE_PROGRESS);

                Intent intent = new Intent(context, PicturePublishActivity.class);
                intent.putExtra("videoUploadInfo", videoUploadInfo);
                if (!isDestroyed && !isEnd)
                    startActivity(intent);
                if (null != progressDialog && progressDialog.isShowing()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        handler.removeMessages(MSG_WHAT_UPDATE_PROGRESS);
        MHLogUtil.d(TAG, "onDestroy");
    }

    @Override
    public void onDecalSelectedListener(DecalInfo decalInfo) {
        if (lastDecalInfo == decalInfo) return;

        lastDecalInfo = decalInfo;
        decal_container.removeAllViews();
        if (!MHStringUtils.isEmpty(decalInfo.getSticker_uri())) {
            if (decalInfo.getType() == DecalInfo.FROM_LOCAL) {
                Bitmap bitmap = BitmapFactory.decodeFile(decalParentPath + decalInfo.getSticker_uri());
                if (null != bitmap) {
                    decal_container.addDecal(bitmap);
                }
            } else {
                decal_container.addDecal(BitmapUtil.loadBitmap(context, decalInfo.getSticker_uri()));
            }
        }
    }


    @Override
    public void onFilterSelect(FilterInfo filterInfo) {
        if (null == filterInfo) return;
        if (null != gl_image_view) {
            lastFilterInfo = filterInfo;
            gl_image_view.setFilterWithConfig(filterInfo.getFilter_param());
            gl_image_view.setFilterIntensity(filterInfo.getIntensity());
        }
    }


    @Override
    public boolean onTabSelect(int position) {
        if (null == fragmentManager)
            fragmentManager = getSupportFragmentManager();

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.show(fragments.get(position)).hide(fragments.get(currentPage)).commit();
        currentPage = position;

        return false;
    }

    @Override
    public void onTabReselect(int position) {

    }

    private CGENativeLibrary.LoadImageCallback loadImageCallback = new CGENativeLibrary.LoadImageCallback() {

        //注意， 这里回传的name不包含任何路径名， 仅为具体的图片文件名如 1.jpg
        @Override
        public Bitmap loadImage(String name, Object arg) {

            MHLogUtil.i(TAG, "正在加载图片: " + name);
            AssetManager am = getAssets();
            InputStream is;
            try {
                is = am.open(name);
            } catch (IOException e) {
                MHLogUtil.e(TAG, "Can not open file " + name);
                return null;
            }
            return BitmapFactory.decodeStream(is);
        }

        @Override
        public void loadImageOK(Bitmap bmp, Object arg) {
            MHLogUtil.i(TAG, "加载图片完毕， 可以自行选择 recycle or cache");

            //loadImage结束之后可以马上recycle
            //唯一不需要马上recycle的应用场景为 多个不同的滤镜都使用到相同的bitmap
            //那么可以选择缓存起来。
            bmp.recycle();
        }
    };
}
