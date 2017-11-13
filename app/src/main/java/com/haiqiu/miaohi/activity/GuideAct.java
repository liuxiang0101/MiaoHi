package com.haiqiu.miaohi.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.MHApplication;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.widget.viewpagerindicator.CirclePageIndicator;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * 新手引导页面
 *
 * @author Kevin
 * @date 2015-10-17
 */
public class GuideAct extends BaseActivity {
    private ViewPager mViewPager;
    private CirclePageIndicator mIndicator;
    private ImageView iv_experienceImmediately;
    private ArrayList<ImageView> mImageViewList;    // imageView集合
    private int[] mImageIds;                        // 引导页图片id数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 必须在setContentView之前调用
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_guide);

        initView();
        initData();
        initVp();

        //是否首次进入新版的APP，为true时才进入此页面
        SpUtils.put(ConstantsValue.Sp.FIRST_INTO_NEW_VERSION_APP, false);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        iv_experienceImmediately = (ImageView) findViewById(R.id.tv_experienceimmediately);
        iv_experienceImmediately.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtils.put(ConstantsValue.Sp.LAST_VERSION_CODE, MHApplication.versionCode);
                startActivity(new Intent(GuideAct.this, MainActivity.class));
                finish();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mImageViewList = new ArrayList<>();
        mImageIds = new int[]{R.drawable.guide1, R.drawable.guide2, R.drawable.guide3};
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setImageBitmap(readBitMap(GuideAct.this, mImageIds[i]));
//            ImageLoader.getInstance().displayImage(mImageIds[i],view);
            mImageViewList.add(view);
        }
    }

    /**
     * 初始化viewpager设置
     */
    private void initVp() {
        mViewPager.setAdapter(new GuideAdapter());// 设置数据
        mViewPager.setOffscreenPageLimit(2);
        mIndicator.setViewPager(mViewPager);

        final float density = getResources().getDisplayMetrics().density;
        mIndicator.setBackgroundColor(Color.argb(0, 255, 255, 255));
        mIndicator.setSpacing(DensityUtil.dip2px(context, 10));
        mIndicator.setRadius(3 * density);
        mIndicator.setPageColor(ContextCompat.getColor(context, R.color.transparent));
        mIndicator.setFillColor(0xff00a0e9);
        mIndicator.setStrokeColor(0xff00a0e9);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//                if (position == (mImageIds.length - 1) && positionOffsetPixels == 0) {
//                    mIndicator.setVisibility(View.GONE);
//                    tv_experienceImmediately.setVisibility(View.VISIBLE);
//                    setAlphaAnim(tv_experienceImmediately);
//                } else {
//                    mIndicator.setVisibility(View.VISIBLE);
//                    tv_experienceImmediately.setVisibility(View.GONE);
//                }

                if (position == 1) {
                    iv_experienceImmediately.setVisibility(View.VISIBLE);
                    iv_experienceImmediately.setTranslationY(
                            mViewPager.getBottom() * (1 - positionOffset));
                    mIndicator.setAlpha(1 - positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
//                if (position == mImageIds.length - 1) {
//                    mIndicator.setVisibility(View.GONE);
//                    tv_experienceImmediately.setVisibility(View.VISIBLE);
//                } else {
//                    mIndicator.setVisibility(View.VISIBLE);
//                    tv_experienceImmediately.setVisibility(View.GONE);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * viewpager适配器
     */
    private class GuideAdapter extends PagerAdapter {
        // item的个数
        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // 初始化item布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        // 销毁item
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public void setAlphaAnim(View view) {
        Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(800);
        view.startAnimation(alphaAnimation);
    }
}
