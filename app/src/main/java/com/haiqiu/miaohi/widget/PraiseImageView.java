package com.haiqiu.miaohi.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.WebViewActivity;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.PraisedUtil;

/**
 * 点赞imageview
 * Created by ningl on 17/1/9.
 */

public class PraiseImageView extends LinearLayout implements View.OnTouchListener{

    private GestureDetector gestureDetector;
    private PraiseImageViewClickListener praiseImageViewClick;
    private ImageView iv_imageview;
    private TextView tv_praseImgesubject;
    private TextView tv_praseImgerecommend;

    public PraiseImageView(Context context) {
        this(context, null);
    }

    public PraiseImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PraiseImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void init(final Context context){
        View.inflate(context, R.layout.view_praiseimageview, this);
        iv_imageview = (ImageView) findViewById(R.id.iv_imageview);
        tv_praseImgesubject = (TextView) findViewById(R.id.tv_praseImgesubject);
        tv_praseImgerecommend = (TextView) findViewById(R.id.tv_praseImgerecommend);
        setOnTouchListener(this);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //单击
                if(praiseImageViewClick!=null) praiseImageViewClick.onSingleTap(PraiseImageView.this);
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //双击
                if(praiseImageViewClick!=null){
                    praiseImageViewClick.onDoubleTap(PraiseImageView.this);
                    PraisedUtil.showPop(iv_imageview, context, true);
                }
                return super.onDoubleTap(e);
            }
        });
    }

    public ImageView getImageView(){
        return iv_imageview;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public interface PraiseImageViewClickListener{
        //单击
        void onSingleTap(View view);
        //双击
        void onDoubleTap(View view);
    }

    public void setOnPraiseImageViewClick(PraiseImageViewClickListener praiseImageViewClick){
        this.praiseImageViewClick = praiseImageViewClick;
    }

    /**
     * 设置活动
     * @param subject 专题名称
     */
    public void setSubject(final String subject, final String uri){
        if(MHStringUtils.isEmpty(subject)
                ||MHStringUtils.isEmpty(uri)){
            tv_praseImgesubject.setVisibility(GONE);
        } else {
            tv_praseImgesubject.setVisibility(getVisibility());
            tv_praseImgesubject.setText("#"+subject+"#");
            tv_praseImgesubject.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("uri", uri);
                    intent.putExtra("title", subject);
                    getContext().startActivity(intent);
                }
            });

        }
    }

    /**
     * 设置专题
     * @param recommend 专题名称
     */
    public void setRecommend(String recommend){
        if(MHStringUtils.isEmpty(recommend)){
            tv_praseImgerecommend.setVisibility(GONE);
        } else {
            tv_praseImgerecommend.setVisibility(VISIBLE);
            tv_praseImgerecommend.setText(recommend);
        }
    }
}
