package com.haiqiu.miaohi.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.haiqiu.miaohi.base.BaseActivity;

/**
 * Created by LiuXiang on 2016/9/13.
 * 公用的一个activity,用于注册成功后关闭一系列界面
 */
public class CommonLRActivity extends BaseActivity {
    private CommonLRReceiver commonLrReceiver = new CommonLRReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册广播
        IntentFilter bubbleFilter = new IntentFilter();
        bubbleFilter.addAction("common_lr");
        registerReceiver(commonLrReceiver, bubbleFilter);
    }

    @Override
    protected void onDestroy() {
        //注销广播
        if (commonLrReceiver != null) {
            unregisterReceiver(commonLrReceiver);
        }
        super.onDestroy();
    }

    /**
     * 接收广播,关闭相应界面
     */
    public class CommonLRReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
}
