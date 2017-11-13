package com.haiqiu.miaohi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.UserGroupInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * 竖向选择器   字母索引
 */
public class LetterSelectorView extends View {

    private OnItemClickListener mOnItemClickListener;
    private List<UserGroupInfo> allGroup = new ArrayList<>();
    int choose = -1;
    Paint paint = new Paint();
    boolean showBkg = false;
    private PopupWindow mPopupWindow;
    private TextView mPopupText;
    private Handler handler = new Handler();
    private int fontColor;
    private int selectedFontColor;
    private int pixelSize;
    private static final float textSizeMultiple = 1.3f;//每个字占用的高度相对于字本身大小的倍数


    public LetterSelectorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public LetterSelectorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSelectorView(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        fontColor = context.getResources().getColor(R.color.color_1d);
        selectedFontColor = context.getResources().getColor(R.color.red);
        pixelSize = getResources().getDimensionPixelSize(R.dimen.bladeview_fontsize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(Color.parseColor("#AAAAAA"));
        }
        if (allGroup.size() == 0) return;

        int height = getHeight();
        int width = getWidth();
        float textHeight = pixelSize * textSizeMultiple * allGroup.size();

        int offset = (int) (height - textHeight) / 2;
        for (int i = 0; i < allGroup.size(); i++) {
            //竖向选择器中字母默认显示的颜色
            paint.setColor(fontColor);
//			paint.setTypeface(Typeface.DEFAULT_BOLD);	//加粗
            paint.setTextSize(pixelSize);//设置字体的大小
            paint.setFakeBoldText(true);
            paint.setAntiAlias(true);
            if (i == choose) {
                //竖向选择器中选中字母的颜色
                paint.setColor(selectedFontColor);
            }
            //字居中显示
            float xPos = (width - paint.measureText(allGroup.get(i).getGroupName())) / 2;
            float yPos = pixelSize * textSizeMultiple * (i + 1) + offset;
            canvas.drawText(allGroup.get(i).getGroupName(), xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        //用户点击选中的字母
        int choiceLetter = 0;
        if (allGroup.size() > 0) {
            int height = getHeight();
            float textHeight = pixelSize * textSizeMultiple * allGroup.size();
            int offset = (int) (height - textHeight) / 2;

            choiceLetter = (int) ((y - offset) / (pixelSize * textSizeMultiple));
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //如果此处设置为true，意为当竖向选择器被点击(包括滑动状态)时，显示背景颜色
//                showBkg = true;
                if (oldChoose != choiceLetter) {
                    if (choiceLetter >= 0 && choiceLetter < allGroup.size()) {    //让第一个字母响应点击事件
                        performItemClicked(choiceLetter);
                        choose = choiceLetter;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != choiceLetter) {
                    if (choiceLetter >= 0 && choiceLetter < allGroup.size()) {    //让第一个字母响应点击事件
                        performItemClicked(choiceLetter);
                        choose = choiceLetter;
                        /*
                        invalidate:
                         Invalidate[ɪn'vælɪdeɪt] the whole view. If the view is visible,
                         onDraw(android.graphics.Canvas) will be called at some point in
                         the future. This must be called from a UI thread. To call from a non-UI thread,
                         call postInvalidate().
                         */
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                dismissPopup();
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 弹出显示   字母索引
     */
    private void showPopup(String content) {
        if (mPopupWindow == null) {

            handler.removeCallbacks(dismissRunnable);
            mPopupText = new TextView(getContext());
            mPopupText.setBackgroundColor(Color.GRAY);
            mPopupText.setTextColor(Color.WHITE);
            mPopupText.setTextSize(getResources().getDimensionPixelSize(R.dimen.bladeview_popup_fontsize));
            mPopupText.setBackgroundResource(R.drawable.shape_corner_rectangle);
            mPopupText.setGravity(Gravity.CENTER_HORIZONTAL
                    | Gravity.CENTER_VERTICAL);

            int height = getResources().getDimensionPixelSize(R.dimen.bladeview_popup_height);

            mPopupWindow = new PopupWindow(mPopupText, height, height);
        }
        mPopupText.setText(content);
        if (mPopupWindow.isShowing()) {
            mPopupWindow.update();
        } else {
            mPopupWindow.showAtLocation(getRootView(),
                    Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        }
    }

    public void setAllGroup(List<UserGroupInfo> allGroup) {
        if (null == allGroup || allGroup.size() == 0) return;
        this.allGroup = allGroup;
        postInvalidate();
    }

    private void dismissPopup() {
        handler.postDelayed(dismissRunnable, 800);
    }

    Runnable dismissRunnable = new Runnable() {

        @Override
        public void run() {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        }
    };

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void performItemClicked(int item) {
        if (mOnItemClickListener != null) {
            String content = allGroup.get(item).getGroupName();
            mOnItemClickListener.onItemClick(content, item);
            showPopup(content);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String content, int position);
    }

}
