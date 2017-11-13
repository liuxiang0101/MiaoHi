package com.haiqiu.miaohi.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;

/**
 * 解绑 换绑对话框
 * Created by ningl on 16/9/1.
 */
public class AccountBindDialog extends Dialog {

    private LinearLayout ll_parent;
    private OnItemClick onItemClick;
    private int type;

    public AccountBindDialog(Context context) {
        super(context, R.style.MiaoHiDialog);
        ll_parent = (LinearLayout) View.inflate(context, R.layout.dialog_accountbind, null);
        setContentView(ll_parent);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = ScreenUtils.getScreenSize(context).x;
        win.setAttributes(lp);
        win.setGravity(Gravity.BOTTOM);
        win.setWindowAnimations(R.style.BottomInAnim);
    }

    /**
     * 生成item
     * @param text
     */
    private RelativeLayout getItem(String text){
        RelativeLayout rl = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        TextView tv = new TextView(getContext());
        tv.setLayoutParams(lp);
        tv.setText(text);
        tv.setTextColor(Color.parseColor("#666666"));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        rl.addView(tv);
        rl.setBackgroundResource(R.drawable.common_enter_selector);
        return rl;
    }

    /**
     * 设置数据
     */
    public AccountBindDialog setData(String... params){
        ll_parent.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.width = ScreenUtils.getScreenSize(getContext()).x;
        lp.height = DensityUtil.dip2px(getContext(), 49);
        int i = 0;
        for (; i < params.length; i++) {
            LinearLayout.LayoutParams line_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            View v_line = new View(getContext());
            v_line.setBackgroundColor(Color.parseColor("#dfdfdf"));
            ll_parent.addView(v_line, line_lp);
            final RelativeLayout rl_item = getItem(params[i]);
            ll_parent.addView(rl_item, lp);
            ll_parent.setTag(i);
            final int position = i;
            rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null != onItemClick) onItemClick.onItemClick(rl_item, position, type);
                }
            });
        }
        LinearLayout.LayoutParams line_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        View v_line = new View(getContext());
        v_line.setBackgroundColor(Color.parseColor("#dfdfdf"));
        ll_parent.addView(v_line, line_lp);
        final RelativeLayout rl_cancleItem = getItem("取消");
        rl_cancleItem.setBackgroundColor(Color.parseColor("#dfdfdf"));
        rl_cancleItem.setTag(i);
        ll_parent.addView(rl_cancleItem, lp);
        rl_cancleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return this;
    }

    public AccountBindDialog setType(int type){
        this.type = type;
        return this;
    }

    public interface OnItemClick{
        void onItemClick(RelativeLayout ll_item, int tag, int type);
    }

    public AccountBindDialog setOnItemClickListener(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
        return this;
    }
}
