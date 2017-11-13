package com.haiqiu.miaohi.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.EmojiAdapter;
import com.haiqiu.miaohi.adapter.EmojiDialogAdapter;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.EmojiData;
import com.haiqiu.miaohi.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 表情dialog
 * Created by ningl on 2016/7/5.
 */
public class EmojiDialog extends Dialog {

    private ViewPager vp;
    private List<RecyclerView> recyclerViews;
    private List<Integer> emojis;
    private static final int DEFAULTCOUNT = 20;
    private EmojiDialogAdapter emojiDialogAdapter;
    private OnEmojiClickListener onEmojiClickListener;
    private View view;
    private int height;
    private LinearLayout ll_emoji_dot;
    private List<ImageView> dots;
    private int currentIndex = 0;

    public EmojiDialog(Activity activity) {
        super(activity, R.style.EmojiDialog);
        view = View.inflate(activity, R.layout.dialog_emoji, null);
        setContentView(view);
        initView(activity);
        initParams(activity);
        emojiDialogAdapter = new EmojiDialogAdapter(activity, recyclerViews);
        vp.setAdapter(emojiDialogAdapter);
    }

    /**
     * 初始化控件
     */
    public void initView(Activity activity){
        Window win = getWindow();
        WindowManager wm = activity.getWindowManager();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = ScreenUtils.getScreenSize(activity).x;
        win.setAttributes(lp);
        win.setGravity(Gravity.BOTTOM);
        win.setWindowAnimations(R.style.BottomInAnim);


        vp = (ViewPager) findViewById(R.id.vp_emoji);
        ll_emoji_dot = (LinearLayout) findViewById(R.id.ll_emoji_dot);
        dots = new ArrayList<>();
        for (int i=0;i<getPageCount();i++){
            if(i ==0){
                ImageView dot = new ImageView(activity);
                dot.setImageResource(R.drawable.svg_dot_selected);
                dots.add(dot);
                ll_emoji_dot.addView(dot);
            } else {
                ImageView dot = new ImageView(activity);
                dot.setPadding(DensityUtil.dip2px(activity, 15), 0, 0 ,0);
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

    public void initParams(Activity activity){
        recyclerViews = new ArrayList<>();
        emojis = new ArrayList<>();
        for(int i=0;i<EmojiData.DATA.length;i++){
            emojis.add(EmojiData.DATA[i]);
        }
        for(int i = 0;i < getPageCount(); i++){
            recyclerViews.add(newView(activity, i));
        }
    }

    /**
     * 创建view
     * @param activity
     * @return
     */
    public RecyclerView newView(Activity activity, int pageIndex){
        RecyclerView rv = new RecyclerView(activity);
        rv.setLayoutManager(new GridLayoutManager(activity, 7));
        rv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        rv.addItemDecoration(new SpaceItemDecoration(activity));
        int startPosition = pageIndex*DEFAULTCOUNT;
        int endPosition = (pageIndex+1)*DEFAULTCOUNT;
        if(endPosition>=emojis.size()){
            endPosition = emojis.size()-1;
        }
        List<Integer> ls = new ArrayList<>();
        ls.clear();
        ls.addAll(emojis.subList(startPosition, endPosition));
        if(ls.size()==DEFAULTCOUNT){
            ls.add(-1);
        } else {
            int count = ls.size();
            ls.add(-1);
            for(int i = 0;i < DEFAULTCOUNT-count; i++){
                ls.add(0);
            }

        }
        EmojiAdapter adapter = new EmojiAdapter(activity, ls, new OnEmojiClickListener() {
            @Override
            public void onEmojiClick(int emoji) {
                if(onEmojiClickListener!=null) onEmojiClickListener.onEmojiClick(emoji);
            }
        });
        rv.setAdapter(adapter);
        return rv;
    }

    /**
     * 获取页数
     * @return
     */
    public int getPageCount(){
       return  EmojiData.DATA.length%DEFAULTCOUNT == 0?EmojiData.DATA.length/DEFAULTCOUNT:EmojiData.DATA.length/DEFAULTCOUNT+1;
    }

    public interface OnEmojiClickListener {
        void onEmojiClick(int emoji);
    }

    public void setOnEmojiClickListener(OnEmojiClickListener onEmojiClickListener){
        this.onEmojiClickListener = onEmojiClickListener;
    }

    /**
     * 设置选中的点
     * @param index 当前选中的位置
     */
    private void setSelectDot(int index){
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

}
