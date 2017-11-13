package com.haiqiu.miaohi.widget.decal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.haiqiu.miaohi.R;


/**
 * Created by zhandalin on 2016-12-26 15:40.
 * 说明: 贴纸View,基于github上面的项目改的,增加了很多实用的功能,使得适合我们的项目
 */
public class DecalView extends LinearLayout {
    private final Context context;
    public MyImageView mImageView;
    private MyImageView mScaleRotateView;
    private MyImageView mDeleteView;
    private float _1dp;
    private boolean mCenterInParent = true;
    private Drawable mPushImageDrawable, mDeleteImageDrawable;
    private float mImageHeight, mImageWidth, mPushImageHeight, mPushImageWidth, mDeleteImageHeight, mDeleteImageWidth;
    public int mLeft = 0, mTop = 0;
    private boolean mIconVisibility = true;
    /**
     * callback interface to be invoked when the delete icon has clicked
     */
    private OnDecalDeleteIconClickListener mOnDecalDeleteIconClickListener;

    /**
     * callback interface to be invoked when the image iv_content has clicked
     */
    private OnImageViewClickListener mOnImageViewClickListener;
    private Point imageMinRect;
    private Point imageMaxRect;
    private View contentView;
    private OnDecalUpdateListener onDecalUpdateListener;


    public DecalView(Context context) {
        this(context, null, 0);
    }

    public DecalView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DecalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        this._1dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
        this.parseAttr(context, attrs);
    }

    private void addScaleMoveView() {
        removeAllViews();
        contentView = View.inflate(context, R.layout.decal_image_view, null);
        addView(contentView, -1, -1);
        mScaleRotateView = (MyImageView) contentView.findViewById(R.id.push_view);
        mImageView = (MyImageView) contentView.findViewById(R.id.iv_content);
        mImageView.setTag(R.id.single_finger_view_scale, (float) 1.0);
        mDeleteView = (MyImageView) contentView.findViewById(R.id.delete_view);

        //最小为1/2
//        imageMinRect = new Point(mImageWidth / 0.7f, mImageHeight / 0.7f);
        imageMinRect = new Point(mImageWidth / 2, mImageHeight / 2);
        //最大为3倍
//        imageMaxRect = new Point(mImageWidth * 1.73, mImageHeight * 1.73);
        imageMaxRect = new Point(mImageWidth * 3, mImageHeight * 3);
        mScaleRotateView.setOnTouchListener(new PushBtnTouchListener(mImageView, mDeleteView, imageMinRect, imageMaxRect));

        mDeleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDecalImage();
            }
        });
        if (getWidth() > 0 && getHeight() > 0) {
            mImageView.setOnTouchListener(new ViewOnTouchListener(mScaleRotateView, mDeleteView, getWidth(), getHeight()));
            setViewToAttr(getWidth(), getHeight());
        }
        mImageView.setOnDecalUpdateListener(new OnDecalUpdateListener() {
            @Override
            public void onDecalUpdate() {
                if (null != onDecalUpdateListener) onDecalUpdateListener.onDecalUpdate();
            }
        });
        mScaleRotateView.setOnDecalUpdateListener(new OnDecalUpdateListener() {
            @Override
            public void onDecalUpdate() {
                if (null != onDecalUpdateListener) onDecalUpdateListener.onDecalUpdate();
            }
        });
        mDeleteView.setOnDecalUpdateListener(new OnDecalUpdateListener() {
            @Override
            public void onDecalUpdate() {
                if (null != onDecalUpdateListener) onDecalUpdateListener.onDecalUpdate();
            }
        });
    }

    public void showIconAndBorder() {
        if (null != mDeleteView) {
            mDeleteView.setVisibility(View.VISIBLE);
        }
        if (null != mScaleRotateView) {
            mScaleRotateView.setVisibility(View.VISIBLE);
        }
        if (null != mImageView) {
            mImageView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.decal_image_border));
        }
        mIconVisibility = true;
    }

    private void parseAttr(Context context, AttributeSet attrs) {
        if (null == attrs) return;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DecalView);
        if (a != null) {
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.DecalView_centerInParent) {
                    this.mCenterInParent = a.getBoolean(attr, false);
                } else if (attr == R.styleable.DecalView_image_height) {
                    this.mImageHeight = a.getDimension(attr, 200 * _1dp);
                } else if (attr == R.styleable.DecalView_image_width) {
                    this.mImageWidth = a.getDimension(attr, 200 * _1dp);
                } else if (attr == R.styleable.DecalView_push_image) {
                    this.mPushImageDrawable = a.getDrawable(attr);
                } else if (attr == R.styleable.DecalView_push_image_width) {
                    this.mPushImageWidth = a.getDimension(attr, 50 * _1dp);
                } else if (attr == R.styleable.DecalView_push_image_height) {
                    this.mPushImageHeight = a.getDimension(attr, 50 * _1dp);
                } else if (attr == R.styleable.DecalView_left) {
                    this.mLeft = (int) a.getDimension(attr, 0 * _1dp);
                } else if (attr == R.styleable.DecalView_top) {
                    this.mTop = (int) a.getDimension(attr, 0 * _1dp);
                } else if (attr == R.styleable.DecalView_delete_image) {
                    this.mDeleteImageDrawable = a.getDrawable(attr);
                } else if (attr == R.styleable.DecalView_delete_image_width) {
                    this.mDeleteImageWidth = a.getDimension(attr, 50 * _1dp);
                } else if (attr == R.styleable.DecalView_delete_image_height) {
                    this.mDeleteImageHeight = a.getDimension(attr, 50 * _1dp);
                }
            }
        }
    }

    private void setViewToAttr(int pWidth, int pHeight) {
        if (pWidth <= 0 || pHeight <= 0) return;
        if (null == mScaleRotateView || null == mDeleteView || null == mImageView) return;

        if (null != mPushImageDrawable) {
            this.mScaleRotateView.setImageDrawable(mPushImageDrawable);
        }
        if (null != mDeleteImageDrawable) {
            this.mDeleteView.setImageDrawable(mDeleteImageDrawable);
        }
        FrameLayout.LayoutParams viewLP = (FrameLayout.LayoutParams) this.mImageView.getLayoutParams();
        viewLP.width = (int) mImageWidth;
        viewLP.height = (int) mImageHeight;
        int left = 0, top = 0;
        if (mCenterInParent) {
            left = pWidth / 2 - viewLP.width / 2;
            top = pHeight / 2 - viewLP.height / 2;
        } else {
            if (mLeft > 0) left = mLeft;
            if (mTop > 0) top = mTop;
        }
        viewLP.leftMargin = left;
        viewLP.topMargin = top;
        this.mImageView.setLayoutParams(viewLP);

        FrameLayout.LayoutParams pushViewLP = (FrameLayout.LayoutParams) mScaleRotateView.getLayoutParams();
        pushViewLP.width = (int) mPushImageWidth;
        pushViewLP.height = (int) mPushImageHeight;
        pushViewLP.leftMargin = (int) (viewLP.leftMargin + mImageWidth - mPushImageWidth / 2);
        pushViewLP.topMargin = (int) (viewLP.topMargin + mImageHeight - mPushImageHeight / 2);
        mScaleRotateView.setLayoutParams(pushViewLP);

        FrameLayout.LayoutParams deleteViewLP = (FrameLayout.LayoutParams) mDeleteView.getLayoutParams();
        deleteViewLP.width = (int) mDeleteImageWidth;
        deleteViewLP.height = (int) mDeleteImageHeight;
        deleteViewLP.leftMargin = (int) (viewLP.leftMargin - mDeleteImageWidth / 2);
        deleteViewLP.topMargin = (int) (viewLP.topMargin - mDeleteImageHeight / 2);
        mDeleteView.setLayoutParams(deleteViewLP);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setParamsForView(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (null != mImageView)
            mImageView.setOnTouchListener(new ViewOnTouchListener(mScaleRotateView, mDeleteView, w, h));
    }

    private boolean hasSetParamsForView = false;

    private void setParamsForView(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (null != layoutParams && !hasSetParamsForView) {
            hasSetParamsForView = true;
            int width;
            if ((getLayoutParams().width == LayoutParams.MATCH_PARENT)) {
                width = MeasureSpec.getSize(widthMeasureSpec);
            } else {
                width = getLayoutParams().width;
            }
            int height;
            if ((getLayoutParams().height == LayoutParams.MATCH_PARENT)) {
                height = MeasureSpec.getSize(heightMeasureSpec);
            } else {
                height = getLayoutParams().height;
            }
            setViewToAttr(width, height);
        }
    }

    /**
     * Interface definition for a callback to be invoked when the delete icon has clicked.
     */
    public interface OnDecalDeleteIconClickListener {
        /***
         * callback method to be invoked when the delete icon has clicked
         */
        void onDeleteIconClick(DecalView singleFingerView);
    }

    /**
     * Interface definition for a callback to be invoked when the image iv_content has clicked
     */
    public interface OnImageViewClickListener {
        /**
         * callback method to be invoked when the image iv_content has clicked
         */
        void onImageViewClick(DecalView singleFingerView);
    }

    /**
     * call to delete the icon
     */
    public void deleteDecalImage() {
        //if the delete icon clicked then notify listener if there is one
        removeAllViews();
        if (mOnDecalDeleteIconClickListener != null) {
            mOnDecalDeleteIconClickListener.onDeleteIconClick(this);
        }
    }

    public void setOnDecalDeleteIconClickListener(OnDecalDeleteIconClickListener onDecalDeleteIconClickListener) {
        this.mOnDecalDeleteIconClickListener = onDecalDeleteIconClickListener;
    }

    /**
     * call to change the index of imageview
     */
    public void changeViewIndexToTop() {
        //if the iv_content clicked then notify listener if there is one
        if (mOnImageViewClickListener != null) {
            mOnImageViewClickListener.onImageViewClick(this);
        }
    }

    /**
     * @param bitmap 要展示的贴纸
     */
    public void addDecal(Bitmap bitmap) {
        if (null == bitmap || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) return;
        addScaleMoveView();
        mImageView.setImageBitmap(bitmap);
    }

    /**
     * @return 最终带贴纸的bitmap
     */
    public Bitmap getResultBitmap() {
        if (getChildCount() <= 0) return null;
        mScaleRotateView.setVisibility(INVISIBLE);
        mDeleteView.setVisibility(INVISIBLE);

        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap b1 = getDrawingCache();
        Bitmap resultBitmap = Bitmap.createBitmap(b1, 0, 0, getWidth(), getHeight());
        destroyDrawingCache();

        mScaleRotateView.setVisibility(VISIBLE);
        mDeleteView.setVisibility(VISIBLE);
        return resultBitmap;
    }

    /**
     * @return 得到当前的贴纸View
     */
    public View getCurrentDecalView() {
        return contentView;
    }

    /**
     * 恢复之前的贴纸状态
     *
     * @param contentView 之前的某个贴纸View
     */
    public void restoreDecalView(View contentView) {
        removeAllViews();
        if (null == contentView) return;

        this.contentView = contentView;
        addView(contentView, -1, -1);
        mScaleRotateView = (MyImageView) contentView.findViewById(R.id.push_view);
        mImageView = (MyImageView) contentView.findViewById(R.id.iv_content);
        mImageView.setTag(R.id.single_finger_view_scale, (float) 1.0);
        mDeleteView = (MyImageView) contentView.findViewById(R.id.delete_view);

        mScaleRotateView.setOnTouchListener(new PushBtnTouchListener(mImageView, mDeleteView, imageMinRect, imageMaxRect));

        mDeleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDecalImage();
            }
        });
        if (getWidth() > 0 && getHeight() > 0) {
            mImageView.setOnTouchListener(new ViewOnTouchListener(mScaleRotateView, mDeleteView, getWidth(), getHeight()));
        }
    }


    public void setOnDecalUpdateListener(OnDecalUpdateListener onDecalUpdateListener) {
        this.onDecalUpdateListener = onDecalUpdateListener;
    }
}
