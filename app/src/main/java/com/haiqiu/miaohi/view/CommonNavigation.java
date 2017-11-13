package com.haiqiu.miaohi.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;


/**
 * Created by zhandalin on 2015/8/9.
 * 说明:统一的导航栏, 该类支持自定义左,中,右的布局,当需要添加分享按钮什么的自行替换
 * 需要修改这个类的时候,需告知,这个类整个工程都用的,建议只由一个人来维护
 */
public class CommonNavigation extends LinearLayout {
    private Context context = null;
    private TextView bar_title;
    private TextView bar_right_text;
    private TextView bar_left_text;
    private String jumpClassName;
    private LinearLayout leftLayoutView, titleLayoutView, rightLayoutView;

    private OnRightLayoutClick onRightLayoutClick;
    private OnLeftLayoutClick onLeftLayoutClick;
    private OnTitleLayoutClick onTitleLayoutClick;
    private ImageView iv_navigation_bg;
    private ImageView bottom_line;
    private View iv_back;

    public CommonNavigation(Context context) {
        this(context, null);
    }

    public CommonNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        initAttribute(attrs);
        initListener();
    }

    private void initView() {
        View view = View.inflate(context, R.layout.navigation_layout, null);
        iv_navigation_bg = (ImageView) view.findViewById(R.id.iv_navigation_bg);

        leftLayoutView = (LinearLayout) view.findViewById(R.id.leftLayoutView);
        bar_left_text = ((TextView) view.findViewById(R.id.bar_left_text));
        iv_back = view.findViewById(R.id.iv_back);

        titleLayoutView = (LinearLayout) view.findViewById(R.id.titleLayoutView);
        bar_title = ((TextView) view.findViewById(R.id.bar_title));

        rightLayoutView = (LinearLayout) view.findViewById(R.id.rightLayoutView);
        bar_right_text = ((TextView) view.findViewById(R.id.bar_right_text));

        bottom_line = (ImageView) view.findViewById(R.id.bottom_line);

        addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(R.dimen.navigation_height)));
    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonNavigation);
        String rightText = typedArray.getString(R.styleable.CommonNavigation_rightText);
        if (!MHStringUtils.isEmpty(rightText)) {
            bar_right_text.setText(rightText);
        }
        String title = typedArray.getString(R.styleable.CommonNavigation_titleText);
        if (!MHStringUtils.isEmpty(title)) {
            bar_title.setText(title);
        }
        String leftText = typedArray.getString(R.styleable.CommonNavigation_leftText);
        if (!MHStringUtils.isEmpty(leftText)) {
            bar_left_text.setVisibility(VISIBLE);
            bar_left_text.setText(leftText);
        }

        jumpClassName = typedArray.getString(R.styleable.CommonNavigation_jumpClass);
        typedArray.recycle();
    }


    public void setTitle(String str) {
        bar_title.setVisibility(VISIBLE);
        bar_title.setText(str);
    }

    public TextView getBar_title() {
        return bar_title;
    }

    public void setRightText(String rightStr) {
        bar_right_text.setText(rightStr);
        bar_right_text.setVisibility(View.VISIBLE);
    }

    public void setRightTextColor(int color) {
        bar_right_text.setTextColor(color);
    }

    public String getRightText() {
        return bar_right_text.getText().toString();
    }

    public TextView getRightTextView() {
        return bar_right_text;
    }

    public void setLeftText(String rightStr) {
        bar_left_text.setText(rightStr);
        bar_left_text.setVisibility(View.VISIBLE);
    }

    public void setLeftIcon(int resId) {
        ((ImageView) iv_back).setImageResource(resId);
    }

    public String getLeftText() {
        return bar_left_text.getText().toString();
    }

    public void showLeftLayout() {
        leftLayoutView.setVisibility(VISIBLE);
    }

    public void hideLeftLayout() {
        leftLayoutView.setVisibility(GONE);
    }


    public void showTitleLayout() {
        titleLayoutView.setVisibility(VISIBLE);
    }

    public void hideTitleLayout() {
        titleLayoutView.setVisibility(GONE);
    }


    public void showRightLayout() {
        rightLayoutView.setVisibility(VISIBLE);
    }

    public void hideRightLayout() {
        rightLayoutView.setVisibility(GONE);
    }

    public void hideLeftArrow() {
        iv_back.setVisibility(GONE);
        LinearLayout.LayoutParams layoutParams = (LayoutParams) bar_left_text.getLayoutParams();
        layoutParams.leftMargin = DensityUtil.dip2px(context, 20);
        bar_left_text.setLayoutParams(layoutParams);
    }

    private void initListener() {
        leftLayoutView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setAlpha(1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setAlpha(0.5f);
                }
                return false;
            }
        });
        leftLayoutView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLeftLayoutClick != null) {
                    onLeftLayoutClick.onClick(v);
                    return;
                }
                if (!MHStringUtils.isEmpty(jumpClassName)) {
                    Intent intent = new Intent();
                    intent.setClassName(context.getPackageName(), "com.haiqiu.miaohi.act." + jumpClassName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
                ((BaseActivity) context).finish();
                ((BaseActivity) context).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        });
        rightLayoutView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setAlpha(1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setAlpha(0.5f);
                }
                return false;
            }
        });
        rightLayoutView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRightLayoutClick != null) {
                    onRightLayoutClick.onClick(v);
                }
            }
        });

        titleLayoutView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onTitleLayoutClick) {
                    onTitleLayoutClick.onClick(v);
                }
            }
        });

    }

    /**
     * 设置导航栏的背景透明度
     *
     * @param alpha 取值0到1
     */
    public void setNavigationBackgroundAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        iv_navigation_bg.setAlpha(alpha);
        bottom_line.setAlpha(alpha);
    }

    public void setNavigationBackground(int resource_id) {
        iv_navigation_bg.setBackgroundResource(resource_id);
    }

    public void setNavigationBackgroundColor(int resource_id) {
        iv_navigation_bg.setBackgroundColor(resource_id);
    }

    public void setNavigationtImage(int resource_id) {
        iv_navigation_bg.setImageResource(resource_id);
    }

    public void setTitleTextColor(int color) {
        bar_title.setTextColor(color);
    }

    /**
     * @param color 导航栏下面分割线的颜色
     */
    public void setBottomLineColor(int color) {
        bottom_line.setBackgroundColor(color);
    }


    /**
     * @param view 要替换的左边View
     */
    public void setLeftLayoutView(View view) {
        leftLayoutView.removeAllViews();
        leftLayoutView.addView(view);
    }

    /**
     * @param view 要替换的标题View
     */
    public void setTitleLayoutView(View view) {
        titleLayoutView.removeAllViews();
        titleLayoutView.addView(view);
    }

    /**
     * @param view 要替换的右边布局
     */
    public void setRightLayoutView(View view) {
        rightLayoutView.removeAllViews();
        rightLayoutView.addView(view);
    }

    /**
     * 隐藏底部的线
     */
    public void hideBottomLine() {
        bottom_line.setVisibility(GONE);
    }

    /**
     * 显示底部的线
     */
    public void showBottomLine() {
        bottom_line.setVisibility(VISIBLE);
    }

    public interface OnLeftLayoutClick {
        void onClick(View v);
    }

    public void setOnLeftLayoutClickListener(OnLeftLayoutClick onLeftLayoutClick) {
        this.onLeftLayoutClick = onLeftLayoutClick;
    }


    public interface OnTitleLayoutClick {
        void onClick(View v);
    }

    public void setOnTitleLayoutClickListener(OnTitleLayoutClick onTitleLayoutClick) {
        this.onTitleLayoutClick = onTitleLayoutClick;
    }

    public interface OnRightLayoutClick {
        void onClick(View v);
    }

    public void setOnRightLayoutClickListener(OnRightLayoutClick onRightLayoutClick) {
        this.onRightLayoutClick = onRightLayoutClick;
    }

    public LinearLayout getLeftLayoutView() {
        return leftLayoutView;
    }

    public LinearLayout getTitleLayoutView() {
        return titleLayoutView;
    }

    public LinearLayout getRightLayoutView() {
        return rightLayoutView;
    }
}
