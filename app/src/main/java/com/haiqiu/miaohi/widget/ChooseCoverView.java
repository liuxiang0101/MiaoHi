package com.haiqiu.miaohi.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhandalin on 2016-07-11 18:08.
 * 说明:选择封面的view
 */
public class ChooseCoverView extends RelativeLayout {
    public static final int IMAGE_NUM = 30;//容纳的图片数量

    private Context context;
    private LinearLayout ll_image_content;
    private ImageView dragImageView;
    private List<String> imagePathList;
    private int imageHeight;//背景图片的高
    private int imageWidth;//背景图片的宽

    private int screenWidth;
    private ImageLoader imageLoader;
    private boolean isFirst = true;
    private int height;//控件的高
    private OnItemSelectedLickListener onItemSelectedLickListener;

    public ChooseCoverView(Context context) {
        this(context, null);
    }

    public ChooseCoverView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChooseCoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        screenWidth = DensityUtil.getScreenWidth(context);
        imageLoader = ImageLoader.getInstance();
        init();
    }

    private void init() {
        //默认图片尺寸
        imageHeight = DensityUtil.dip2px(context, 50);
        imageWidth = screenWidth / IMAGE_NUM;
        //实际尺寸

        setGravity(Gravity.CENTER_VERTICAL);
        setClickable(true);
        height = imageHeight + DensityUtil.dip2px(context, 6);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (null == layoutParams) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        } else {
            layoutParams.height = height;//外层控件比背景图片大6dp
        }
        setLayoutParams(layoutParams);

        ll_image_content = new LinearLayout(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, imageHeight);
        ll_image_content.setLayoutParams(params);
        params.addRule(RelativeLayout.CENTER_VERTICAL, getId());
        addView(ll_image_content);

        dragImageView = new ImageView(context);
        int padding = DensityUtil.dip2px(context, 2);
        dragImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LayoutParams imageParams = new LayoutParams(height, height);
        dragImageView.setPadding(padding, padding, padding, padding);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            dragImageView.setCropToPadding(true);
        }
        dragImageView.setLayoutParams(imageParams);
        imageParams.addRule(RelativeLayout.CENTER_VERTICAL, getId());
        addView(dragImageView);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        super.onInterceptTouchEvent(ev);
        return true;
    }

    public void setData(List<String> imagePath) {
        this.imagePathList = imagePath;
        ll_image_content.removeAllViews();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(imageWidth, imageHeight);
        for (int i = 0; i < imagePath.size(); i++) {
            String path = imagePath.get(i);
            if (i == 0) {
                imageLoader.displayImage("file://" + path, dragImageView);
            }
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(layoutParams);
            imageLoader.displayImage("file://" + path, imageView);
            ll_image_content.addView(imageView);
        }
    }

    public void addData(String imagePath) {
        if (null == imagePathList) imagePathList = new ArrayList<>();
        imagePathList.add(imagePath);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(imageWidth, imageHeight);
        if (isFirst) {
            imageLoader.displayImage("file://" + imagePath, dragImageView);
            isFirst = false;
            dragImageView.setBackgroundColor(Color.WHITE);
        }
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(layoutParams);
        imageLoader.displayImage("file://" + imagePath, imageView);
        ll_image_content.addView(imageView);
    }

    float targetX;
    int position;
    int lastPosition;
    float offset = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        offset = 1.0f * position / IMAGE_NUM * height / 2;
//        MHLogUtil.d("KKKK", "offset=" + offset + "--position=" + position + "---imageHeight=" + imageHeight);
        position = (int) ((event.getX() + offset) / imageWidth);
        targetX = event.getX() - height / 2;

        if (targetX < 0) targetX = 0;
        if (targetX > screenWidth - height) targetX = screenWidth - height;

        dragImageView.setX(targetX);
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
//                int temp = position * imageWidth;
//                if (temp > screenWidth - imageWidth) temp = screenWidth - imageWidth;
//                dragImageView.setX(temp);
                break;
        }
        if (position != lastPosition) {
            lastPosition = position;
            if (null != imagePathList && imagePathList.size() > position) {
                String path = imagePathList.get(position);
                imageLoader.displayImage("file://" + path, dragImageView, DisplayOptionsUtils.getSilenceDisplayBuilder());
                if (null != onItemSelectedLickListener)
                    onItemSelectedLickListener.onItemSelected(position);
            }
        }
//        super.onTouchEvent(event);
        return true;
    }

    public interface OnItemSelectedLickListener {
        void onItemSelected(int position);
    }

    public void setOnItemSelectedLickListener(OnItemSelectedLickListener onItemSelectedLickListener) {
        this.onItemSelectedLickListener = onItemSelectedLickListener;
    }
}
