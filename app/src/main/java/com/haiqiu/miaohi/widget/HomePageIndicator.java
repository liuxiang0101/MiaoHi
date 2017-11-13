package com.haiqiu.miaohi.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhandalin on 2012/3/1.
 * 说明:页面指示器,支持最后一个指示器是一个圆形的图片
 */
public class HomePageIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {

    private Context context;
    private int pageCount;
    private int lastPosition = 0;
    private int marginLeft = 10;//dp
    private List<ImageView> pageIndicatorList = new ArrayList<>();
    private LayoutParams params;

    public HomePageIndicator(Context context) {
        this(context, null);
    }

    public HomePageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        marginLeft = DensityUtil.dip2px(context, marginLeft);
        int dip2px = DensityUtil.dip2px(context, 5);
        params = new LayoutParams(dip2px, dip2px);
        params.setMargins(marginLeft, 0, 0, 0);
    }

    public void setPageCount(int pageCount) {
        setPageCount(pageCount, 0);
    }

    /**
     * @param pageCount 页面的总数
     */
    public void setPageCount(int pageCount, int indicatorColorType) {
        this.pageCount = pageCount;
        if (pageCount <= 1) {
            return;
        }
        pageIndicatorList.clear();
        removeAllViews();

        for (int i = 0; i < pageCount; i++) {
            ImageView view = new ImageView(context);
            //设置指示器的点的颜色
            view.setImageResource(R.drawable.selector_home_page_indicator);
            if (indicatorColorType == 1)
                view.setImageResource(R.drawable.selector_home_page_indicator1);
            if (i == 0)
                addView(view);
            else
                addView(view, params);

            view.setEnabled(false);
            pageIndicatorList.add(view);
        }
        pageIndicatorList.get(lastPosition % pageCount).setEnabled(true);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (pageCount <= 1) {
            return;
        }
        pageIndicatorList.get(lastPosition % pageCount).setEnabled(false);
        pageIndicatorList.get(position % pageCount).setEnabled(true);
        lastPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
