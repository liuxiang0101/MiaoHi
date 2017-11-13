package com.haiqiu.miaohi.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.FadeOutFromBottomItem;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;

import java.util.List;

/**
 * Created by ningl on 16/12/2.
 */
public class DialogFadeOutFromBottom extends Dialog{

    private LinearLayout ll_parent;
    private Activity activity;
    private OnSelectItem onSelectItem;

    public DialogFadeOutFromBottom(Activity activity) {
        super(activity, R.style.MiaoHiDialog);
        this.activity = activity;
        ll_parent = new LinearLayout(activity);
        ll_parent.setPadding(DensityUtil.dip2px(activity, 15), 0, DensityUtil.dip2px(activity,15), 0);
//        ViewGroup.LayoutParams parentLp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ll_parent.setLayoutParams(parentLp);
        ll_parent.setOrientation(LinearLayout.VERTICAL);
        setContentView(ll_parent);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = ScreenUtils.getScreenSize(activity).x;
        win.setAttributes(lp);
        win.setGravity(Gravity.BOTTOM);
        win.setWindowAnimations(R.style.BottomInAnim);
    }

    /**
     * 添加items
     * @param items
     */
    public void setItem(List<FadeOutFromBottomItem> items){
        if(items == null) return;
        ll_parent.removeAllViews();
        for (int i = 0; i < items.size(); i++) {
            FadeOutFromBottomItem item = items.get(i);
            TextView tv = new TextView(activity);
            tv.setGravity(Gravity.CENTER);
            tv.setText(item.getText());
            if(!MHStringUtils.isEmpty(item.getColor())) tv.setTextColor(Color.parseColor(item.getColor()));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            if(items.size() == 1){
                tv.setBackgroundResource(R.drawable.selector_fadeoutfrombottom_cancle);
            } else if(i == 0){
                tv.setBackgroundResource(R.drawable.selector_fadeoutfrombottom_top);
            } else if(i == items.size()-1){
                tv.setBackgroundResource(R.drawable.selector_fadeoutfrombottom_bottom);
            } else {
                tv.setBackgroundResource(R.drawable.selector_fadeoutfrombottom_middle);
            }
            ll_parent.addView(tv, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(activity, 50)));
            if(i!=items.size()-1) addLine();
            final int selectIndex = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onSelectItem!=null) onSelectItem.select(selectIndex);
                }
            });
        }
        TextView tvCancle = new TextView(activity);
        tvCancle.setGravity(Gravity.CENTER);
        tvCancle.setText("取消");
        tvCancle.setTextColor(Color.parseColor("#d1d1d1"));
        tvCancle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        tvCancle.setBackgroundResource(R.drawable.selector_fadeoutfrombottom_cancle);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(activity, 50));
        lp.setMargins(0, DensityUtil.dip2px(activity, 5), 0, DensityUtil.dip2px(activity, 20));
        ll_parent.addView(tvCancle, lp);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void addLine(){
        View line = new View(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        line.setLayoutParams(lp);
        line.setBackgroundColor(Color.parseColor("#d6d6d6"));
        ll_parent.addView(line);
    }

    public interface OnSelectItem{
        void select(int index);
    }

    public void setOnSelectItemListener(OnSelectItem onSelectItem){
        this.onSelectItem = onSelectItem;
    }


    public enum DialogFadeOutColor {
        Blue("#037BFF"), Red("#FD4A2E");

        private String name;

        private DialogFadeOutColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
