package com.haiqiu.miaohi.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.EmojiAdapter;
import com.haiqiu.miaohi.adapter.EmojiDialogAdapter;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.EmojiData;
import com.haiqiu.miaohi.utils.KeyboardWatcher;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.SoftKeyboardStateHelper;
import com.haiqiu.miaohi.utils.ToastUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.view.EmojiDialog;
import com.haiqiu.miaohi.view.NoteEditText;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * emoji键盘
 * Created by ningl on 2016/7/7.
 */
public class KeyBoardView extends LinearLayout implements KeyboardWatcher.OnKeyboardToggleListener, View.OnClickListener {

    private SoftKeyboardStateHelper mKeyboardHelper;
    private LinearLayout ll_keyboardlayout;
    private NoteEditText et;
    private Context context;
    private CheckBox cb_emoji_btn;
    private ImageView iv_invitefriend;
    public TextView tv_videodetail_send;
    //    private LinearLayout ll_outside;
//    private View outsideView;
    private boolean isSoftShow;//是否显示软键盘

    private ViewPager vp;
    private List<RecyclerView> recyclerViews;
    private List<Integer> emojis;
    private static final int DEFAULTCOUNT = 20;
    private EmojiDialogAdapter emojiDialogAdapter;
    private OnEmojiClickListener onEmojiClickListener;
    private OnEmojiDoListener onEmojiDoListener;
    private View view;
    private int height;
    private LinearLayout ll_emoji_dot;
    private List<ImageView> dots;
    private int currentIndex = 0;
    private int keyHeight;

    int currentCount;

    private int softKeyboardHeight;
    private OnKeboardListener onKeboardListener;
    private boolean isEmojiBtn;

    public KeyboardWatcher keyboardWatcher;

    private RelativeLayout contentView;
    private OnStartCommentListener onStartCommentListener;
    private boolean isHasRootView;

    @Override
    public void onKeyboardShown(int keyboardSize) {
        if(!isHasRootView) return;
        softKeyboardHeight = keyboardSize;
        hideLayout();
        isSoftShow = true;
        setCnState();
        tv_videodetail_send.setText("发送");
        if(et.getText().toString().trim().length()>0){
            tv_videodetail_send.setBackgroundResource(R.drawable.corner_blue_shape);
        } else {
            tv_videodetail_send.setBackgroundResource(R.drawable.corner_gray_shape);
        }
        if (null != onKeboardListener && !isEmojiBtn) {
            onKeboardListener.onOpen();
        }
        isEmojiBtn = false;
    }

    @Override
    public void onKeyboardClosed() {
        if(!isHasRootView) return;
        isSoftShow = false;
        tv_videodetail_send.setBackgroundResource(R.drawable.bg_gift_corner_6_selector);
        tv_videodetail_send.setTextColor(context.getResources().getColor(R.color.white));

        softKeyboardHeight = 0;

        if (null != onKeboardListener && !isEmojiBtn) {
            onKeboardListener.onClose();
        }

        if (!TextUtils.isEmpty(et.getText().toString().trim())) {
            tv_videodetail_send.setText("发送");
        }

        isEmojiBtn = false;

    }

    public interface OnKeboardListener {
        void onOpen();

        void onClose();
    }

    public void setOnKeyboardListener(OnKeboardListener onKeboardListener) {
        this.onKeboardListener = onKeboardListener;
    }

    public interface OnEmojiClickListener {
        void onEmojiClick(int emoji);
    }

    public void setOnEmojiClickListener(OnEmojiClickListener onEmojiClickListener) {
        this.onEmojiClickListener = onEmojiClickListener;
    }

    public interface OnEmojiDoListener {
        /**
         * @好友
         */
        void onAtFriend();

        /**
         * 发送评论
         */
        void onSendComment();

        /**
         * 输入框输入后
         */
        void onTextChangeAfter(Editable s);

        /**
         * 输入框输入后
         * @param s
         * @param start
         * @param count
         * @param after
         */
        void onTextChangeBefore(CharSequence s, int start, int count, int after);

    }

    public void setOnEmojiDoListener(OnEmojiDoListener onEmojiDoListener) {
        this.onEmojiDoListener = onEmojiDoListener;
    }

    public KeyBoardView(Context context) {
        super(context);
        init(context);
    }

    public KeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View root = View.inflate(context, R.layout.view_keyboard, null);
        keyHeight = ScreenUtils.getScreenHeight(context)/3;
        this.addView(root);
        this.initWigdet();
        initParams(context);
        emojiDialogAdapter = new EmojiDialogAdapter(context, recyclerViews);
        vp.setAdapter(emojiDialogAdapter);
        root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initWigdet() {
        et = (NoteEditText) findViewById(R.id.et_videodetail_input);
        ll_keyboardlayout = (LinearLayout) findViewById(R.id.ll_keyboardlayout);
        cb_emoji_btn = (CheckBox) findViewById(R.id.iv_emoji_btn);
        tv_videodetail_send = (TextView) findViewById(R.id.tv_videodetail_send);
        iv_invitefriend = (ImageView) findViewById(R.id.iv_invitefriend);
        tv_videodetail_send.setBackgroundResource(R.drawable.bg_gift_corner_6_selector);
        tv_videodetail_send.setTextColor(context.getResources().getColor(R.color.white));
        tv_videodetail_send.setOnClickListener(this);
        iv_invitefriend.setOnClickListener(this);
        //        ll_outside.setOnClickListener(this);
        et.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLayout();
                et.requestFocus();
                et.setCursorVisible(true);
                TCAgent.onEvent(context, "视频详情-输入框" + ConstantsValue.android);
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                MHLogUtil.i("视频评论字数beforeTextChanged", s.length() + "");
                if(onEmojiDoListener!=null) onEmojiDoListener.onTextChangeBefore(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MHLogUtil.i("视频评论字数onTextChanged", s.length() + "");
            }

            @Override
            public void afterTextChanged(Editable s) {
                MHLogUtil.i("视频评论字数afterTextChanged", s.length() + "");
                if (s.length() > 500) {
                    et.setText(et.getText().toString().substring(0, 500));
                    ToastUtils.showToastAtCenter(context, "最多可输入500字");
                }
                if(s.length()>0){
                    tv_videodetail_send.setEnabled(true);
                    tv_videodetail_send.setBackgroundResource(R.drawable.bg_gift_corner_6_selector);
                } else {
                    tv_videodetail_send.setBackgroundResource(R.drawable.corner_gray_shape);
                }
                tv_videodetail_send.setText("发送");
                if(onEmojiDoListener!=null) onEmojiDoListener.onTextChangeAfter(s);
            }
        });
        cb_emoji_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TCAgent.onEvent(context, "视频详情-表情" + ConstantsValue.android);
                isEmojiBtn = true;
                if (isShow()) {
                    hideLayout();
                    cb_emoji_btn.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showKeyboard(context);
                        }
                    }, 100);
                } else {
                    showLayout();
//                    hideKeyboard(context);
//                    ll_keyboardlayout.setVisibility(View.VISIBLE);
//                    tv_videodetail_send.setText("发送");
//                    setOutsideViewState();
//                    et.requestFocus();
                }
            }
        });

        vp = (ViewPager) findViewById(R.id.vp_emoji);
        ll_emoji_dot = (LinearLayout) findViewById(R.id.ll_emoji_dot);
        dots = new ArrayList<>();
        for (int i = 0; i < getPageCount(); i++) {
            if (i == 0) {
                ImageView dot = new ImageView(context);
                dot.setImageResource(R.drawable.svg_dot_selected);
                dots.add(dot);
                ll_emoji_dot.addView(dot);
            } else {
                ImageView dot = new ImageView(context);
                dot.setPadding(DensityUtil.dip2px(context, 15), 0, 0, 0);
                dot.setImageResource(R.drawable.svg_dot_noselect);
                dots.add(dot);
                ll_emoji_dot.addView(dot);
            }
        }
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void hideLayout() {
        if (isShow()) {
            if (cb_emoji_btn.isChecked()) {
                cb_emoji_btn.setChecked(false);
            } else {
                cb_emoji_btn.setChecked(true);
            }
        }
        ll_keyboardlayout.setVisibility(View.GONE);
        if (null != onKeboardListener && isEmojiBtn) {
            onKeboardListener.onClose();
        }

        isEmojiBtn = false;
    }

    public void showLayout() {
        hideKeyboard(this.context);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                isEmojiBtn = true;
                if(et.getText().length()==0){
                    et.requestFocus();
                    et.setFocusable(true);
                    et.setSelection(0);
                }
                ll_keyboardlayout.setVisibility(View.VISIBLE);
                setVisibility(VISIBLE);
                tv_videodetail_send.setText("发送");
                if(et.getText().toString().trim().length()>0){
                    tv_videodetail_send.setBackgroundResource(R.drawable.bg_gift_corner_6_selector);
                } else {
                    tv_videodetail_send.setBackgroundResource(R.drawable.corner_gray_shape);
                }
                if (null != onKeboardListener) {
                    onKeboardListener.onOpen();
                }
            }
        }, 200);
    }

    private void initData() {
//        keyboardWatcher = new KeyboardWatcher((Activity) context);
//        keyboardWatcher.setListener(this);

    }

    private boolean isShow() {
        return ll_keyboardlayout.getVisibility() == View.VISIBLE;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initData();
    }


    public void initParams(Context activity) {
        recyclerViews = new ArrayList<>();
        emojis = new ArrayList<>();
        for (int i = 0; i < EmojiData.DATA.length; i++) {
            emojis.add(EmojiData.DATA[i]);
        }
        for (int i = 0; i < getPageCount(); i++) {
            recyclerViews.add(newView(context, i));
        }
    }

    public void hidenSoft4RotateScreen() {
        if (isSoftShow)
            hideKeyboard(context);
    }

    public int getSoftKeyboardHeight() {
        return softKeyboardHeight;
    }

    /**
     * 创建view
     *
     * @param activity
     * @return
     */
    public RecyclerView newView(Context activity, int pageIndex) {
        RecyclerView rv = new RecyclerView(activity);
        rv.setLayoutManager(new GridLayoutManager(activity, 7));
        rv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        rv.addItemDecoration(new SpaceItemDecoration(activity));
        int startPosition = pageIndex * DEFAULTCOUNT;
        int endPosition = (pageIndex + 1) * DEFAULTCOUNT;
        if (endPosition >= emojis.size()) {
            endPosition = emojis.size() - 1;
        }
        List<Integer> ls = new ArrayList<>();
        ls.clear();
        ls.addAll(emojis.subList(startPosition, endPosition));
        if (ls.size() == DEFAULTCOUNT) {
            ls.add(-1);
        } else {
            int count = ls.size();
            ls.add(-1);
            for (int i = 0; i < DEFAULTCOUNT - count; i++) {
                ls.add(0);
            }
        }

        EmojiAdapter adapter = new EmojiAdapter(context, ls, new EmojiDialog.OnEmojiClickListener() {
            @Override
            public void onEmojiClick(int emoji) {
                handleEdit(emoji);
            }
        });
        rv.setAdapter(adapter);
        return rv;
    }

    /**
     * 获取页数
     *
     * @return
     */
    public int getPageCount() {
        return EmojiData.DATA.length % DEFAULTCOUNT == 0 ? EmojiData.DATA.length / DEFAULTCOUNT : EmojiData.DATA.length / DEFAULTCOUNT + 1;
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
    }

    /**
     * 显示软键盘
     */
    public static void showKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(activity.getCurrentFocus()
                    .getWindowToken(), 0);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public NoteEditText getEt() {
        return et;
    }

    public void handleEdit(int emoji) {
        Editable edit = et.getEditableText();
        if (emoji != -1) {
            if(et.getSelectionStart()==-1){
                edit.insert(0, getEmojiStringByUnicode(emoji));
            } else {
                edit.insert(et.getSelectionStart(), getEmojiStringByUnicode(emoji));
            }
        } else {
            int index = et.getSelectionStart();
            if (index != 0) {
                int start = index - 1;
                int end = index;
                if (containsEmoji(et.getText().toString().substring(start, end))) {
                    edit.delete(index - 2, end);
                } else {
                    edit.delete(start, end);
                }

            }
        }
    }

    /**
     * 设置选中的点
     *
     * @param index 当前选中的位置
     */
    private void setSelectDot(int index) {
        dots.get(index).setImageResource(R.drawable.svg_dot_selected);
        dots.get(currentIndex).setImageResource(R.drawable.svg_dot_noselect);
        currentIndex = index;
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private Context context;

        public SpaceItemDecoration(Context context) {
            this.context = context;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.top = DensityUtil.dip2px(context, 22);
            outRect.bottom = 0;
        }
    }

    /**
     * 判断是否是emoji
     *
     * @param source
     * @return
     */
    public boolean containsEmoji(String source) {
        if (TextUtils.isEmpty(source)) {
            return false;
        }
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }
        return false;
    }

    private boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    private String getEmojiStringByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public void reset() {
        hideLayout();
        hideKeyboard(context);
        setCnState();
    }


    public void setCnState() {
        cb_emoji_btn.setChecked(false);
    }

    /**
     * 发送按钮是否可点击
     *
     * @param enable
     */
    public void setKeyboardEnable(boolean enable) {
        tv_videodetail_send.setEnabled(enable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_invitefriend://@好友
                if (onEmojiDoListener != null)
                    onEmojiDoListener.onAtFriend();
                break;
            case R.id.tv_videodetail_send://发送评论
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (!UserInfoUtil.isLogin()) {
                            UserInfoUtil.goLogin(context);
                        } else {
                            if (onEmojiDoListener != null) {
                                if(et.getText().length()==0){
//                                    ToastUtils.showToastAtCenter(context, "评论内容不能为空");
                                } else {
                                    onEmojiDoListener.onSendComment();
                                }
                            }
                        }
                    }
                });
                break;
        }
    }


    /**
     * 设置按钮名称
     */
    public void setGiftText() {
        tv_videodetail_send.setText("礼物");
    }

    /**
     * 绑定内容view
     * @param contentView
     */
    public void bindContentView(RelativeLayout contentView){
        this.contentView = contentView;
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private void lockContentHeight() {
        if(contentView==null) return;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentView.getLayoutParams();
        params.height = contentView.getHeight();
        params.weight = 0.0F;
    }

    /**
     * 释放被锁定的内容高度
     */
    private void unlockContentHeightDelayed() {
        if(contentView==null) return;
        et.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) contentView.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }

    public interface OnStartCommentListener {
        void onStartCommont(int postion);
    }

    public void setOnStartCommentListener(OnStartCommentListener onStartCommentListener){
        this.onStartCommentListener = onStartCommentListener;
    }

    /**
     * 键盘全部关闭
     */
    public void closeAllBoard(){
        hideKeyboard(getContext());
        hideLayout();
        setVisibility(GONE);
    }

    /**
     * 设置根布局
     * @param view
     */
    public void setRootView(View view){
        isHasRootView = true;
        view.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
                    //打开软键盘
                    hideLayout();
                    isSoftShow = true;
                    setCnState();
                    tv_videodetail_send.setText("发送");
                    if(et.getText().toString().trim().length()>0){
                        tv_videodetail_send.setBackgroundResource(R.drawable.corner_blue_shape);
                    } else {
                        tv_videodetail_send.setBackgroundResource(R.drawable.corner_gray_shape);
                    }
                    if (null != onKeboardListener && !isEmojiBtn) {
                        onKeboardListener.onOpen();
                    }
                    isEmojiBtn = false;

                }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
                    //关闭软键盘
                    isSoftShow = false;
                    tv_videodetail_send.setBackgroundResource(R.drawable.bg_gift_corner_6_selector);
                    tv_videodetail_send.setTextColor(context.getResources().getColor(R.color.white));

                    softKeyboardHeight = 0;

                    if (null != onKeboardListener && !isEmojiBtn) {
                        onKeboardListener.onClose();
                    }

                    if (!TextUtils.isEmpty(et.getText().toString().trim())) {
                        tv_videodetail_send.setText("发送");
                    }

                    isEmojiBtn = false;
                }
            }
        });
    }

}
