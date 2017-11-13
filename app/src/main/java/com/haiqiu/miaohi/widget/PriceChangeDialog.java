package com.haiqiu.miaohi.widget;

import android.content.Context;

/**
 * 价格改变dialog
 * Created by ningl on 17/1/3.
 */

public class PriceChangeDialog extends CommonDialog{
    
    private long price;
    
    public PriceChangeDialog(Context context, long price) {
        super(context);
        setCancelable(false);
        setLeftButtonMsg("不了");
        setRightButtonMsg("继续问");
//        setTitleMsg(response.getData().getTitle());
        setContentMsg("价格变为"+price/100+"嗨币,\n是否继续提问");
        setOnLeftButtonOnClickListener(new LeftButtonOnClickListener() {
            @Override
            public void onLeftButtonOnClick() {
                dismiss();
            }
        });
    }
}
