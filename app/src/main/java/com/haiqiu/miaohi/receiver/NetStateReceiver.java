package com.haiqiu.miaohi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.haiqiu.miaohi.widget.CommonDialog;

/**
 * 接受广播--网络断开时
 * Created by miaohi on 2016/5/6.
 */
public class NetStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent arg1) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!gprs.isConnected() && !wifi.isConnected()) {
            CommonDialog commonDialog = new CommonDialog(context);
            commonDialog.setAutoDismiss(true);
            commonDialog.hideTitle();
            commonDialog.hideRightButton();
            commonDialog.setLeftButtonMsg("确定");
            commonDialog.setRightButtonMsg("取消");
            commonDialog.setContentMsg("当前网络状态不佳，请重试或存入草稿箱");
            commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
                @Override
                public void onLeftButtonOnClick() {
                    //进入无线网络配置界面
//                    context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
//                    context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));  //进入手机中的wifi网络设置界面
                }
            });
//            commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
//                @Override
//                public void onRightButtonOnClick() {
//                    Toast.makeText(context, "当前无网络", Toast.LENGTH_SHORT).show();
//                }
//            });
            commonDialog.show();
        }

    }
}
