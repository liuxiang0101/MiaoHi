package com.haiqiu.miaohi.widget;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.haiqiu.miaohi.adapter.CarouselAdapter;
import com.haiqiu.miaohi.bean.DiscoveryBannerObj;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.view.NoScrollViewPager;

import java.util.List;

/**
 * Created by zhandalin on 2016-07-04 16:12.
 * 在Activity中的对应方法中调用onResume,onVideoPause
 * 嵌套在Fragment中的话就调用setUserVisibleHint(要注意是不是hide,show机制)
 */
public class CarouselImageView extends RelativeLayout {
    private final int NEXT_PAGE = 78;
    private final long delayMillis = 5000;
    private final Context context;
    private NoScrollViewPager view_pager;
    private HomePageIndicator pageIndicator;
    private List<DiscoveryBannerObj> carousel_data;
    private boolean isInited;
    private boolean isMoreThanOne;
    private CarouselAdapter carouselAdapter;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case NEXT_PAGE:
                    handler.removeMessages(NEXT_PAGE);
                    if ((!isInited) && isMoreThanOne) {
                        break;
                    }
                    view_pager.setCurrentItem(view_pager.getCurrentItem() + 1, true);
                    if (view_pager.getCurrentItem() + 20 > Integer.MAX_VALUE) {//保证极端情况下还能运行
                        view_pager.setCurrentItem(view_pager.getCurrentItem() - 30 * carousel_data.size(), true);
                    }
                    handler.sendEmptyMessageDelayed(NEXT_PAGE, delayMillis);
                    break;
            }
            return false;
        }
    });

    public CarouselImageView(Context context) {
        this(context, null);
    }

    public CarouselImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(context);
//        initData();
    }

    private void init(Context context) {
        //适配尺寸
        Point screenSize = ScreenUtils.getScreenSize(context);
        int height = 392 * screenSize.x / 750;
        AbsListView.LayoutParams params = (AbsListView.LayoutParams) getLayoutParams();
        if (params == null) {
            params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
        } else {
            params.height = height;
        }
        setLayoutParams(params);

        //轮播的图片
        view_pager = new NoScrollViewPager(context);
        view_pager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(view_pager);

        //页面指示器
        pageIndicator = new HomePageIndicator(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.bottomMargin = DensityUtil.dip2px(context, 12);
        addView(pageIndicator, layoutParams);

        view_pager.addOnPageChangeListener(pageIndicator);
    }


    /**
     * 加载轮播图图片
     */
    public void initData(List<DiscoveryBannerObj> list) {
//        List<BannerResult> banner_result = new ArrayList<>();
//        for(DiscoveryBannerObj obj:list){
//            BannerResult result=new BannerResult();
//            result.setBanner_uri(obj.getBanner_uri());
//            banner_result.add(result);
//        }
        pageIndicator.setVisibility(View.VISIBLE);
        if (null != list) {
            carousel_data = list;
            handler.removeMessages(NEXT_PAGE);
            if (list.size() <= 1) {
                view_pager.setNoScroll(true);
            } else {
                view_pager.setNoScroll(false);
            }
//            if (null == carouselAdapter) {
                carouselAdapter = new CarouselAdapter(context, list);
                view_pager.setAdapter(carouselAdapter);
                view_pager.setCurrentItem(40 * carousel_data.size(), true);
//            } else {
//                carouselAdapter.changeList(carousel_data);
//            }
            pageIndicator.setPageCount(carousel_data.size());
            if (carousel_data.size() > 1) {
                isInited = true;
                isMoreThanOne = true;
                handler.sendEmptyMessageDelayed(NEXT_PAGE, delayMillis);
            } else {
                isMoreThanOne = false;
                pageIndicator.setVisibility(View.INVISIBLE);
            }
        }
    }


    public void onResume() {
        if (isInited && null != handler) {
            handler.sendEmptyMessageDelayed(NEXT_PAGE, delayMillis);
        }
    }

    public void onPause() {
        if (isInited && null != handler) {
            handler.removeMessages(NEXT_PAGE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null == carousel_data || carousel_data.size() < 2) return super.dispatchTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeMessages(NEXT_PAGE);
                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(NEXT_PAGE, delayMillis);
                break;
            case MotionEvent.ACTION_CANCEL:
                handler.sendEmptyMessageDelayed(NEXT_PAGE, delayMillis);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void refresh() {

    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isInited && null != handler) {
            if (isVisibleToUser) {
                handler.sendEmptyMessageDelayed(NEXT_PAGE, delayMillis);
            } else {
                handler.removeMessages(NEXT_PAGE);
            }
        }
    }

    public void onHiddenChanged(boolean hidden) {
        if (isInited && null != handler) {
            if (hidden) {
                handler.removeMessages(NEXT_PAGE);
            } else {
                handler.sendEmptyMessageDelayed(NEXT_PAGE, delayMillis);
            }
        }
    }

}
