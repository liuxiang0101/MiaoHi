package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.fragment.FoundSquareFragment;
import com.haiqiu.miaohi.utils.DataCleanManager;
import com.haiqiu.miaohi.utils.DraftUtil;
import com.haiqiu.miaohi.utils.FileUtils;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.haiqiu.miaohi.widget.ItemSettingView;
import com.tendcloud.tenddata.TCAgent;

import java.io.File;

/**
 * 我的-设置
 * Created by ningl on 2016/4/11.
 */
public class MineSettingActivity extends BaseActivity implements View.OnClickListener {
    public static final int LOGINOUT = 105;
    private CommonNavigation cn;
    private ItemSettingView isv_settingdraft;
    private ItemSettingView isv_settingwallet;
    private ItemSettingView isv_settingmaybeinsteret;
    private ItemSettingView isv_settingbindaccount;
    private ItemSettingView isv_settingnotifysetting;
    private ItemSettingView isv_settingauthentication;
    private ItemSettingView isv_settingclearcach;
    private ItemSettingView isv_settingfeedback;
    private ItemSettingView isv_settingaboutus;
    private TextView tv_loginout;
    private CommonDialog commonDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mine_setting);
        initView();
        isv_settingclearcach.setRightText(DataCleanManager.getFormatSize(getFolderSize(getCacheDir())));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置草稿数量
        isv_settingdraft.setRightText(DraftUtil.getDraftCount() + "");
    }

    private void initView() {
        cn = (CommonNavigation) findViewById(R.id.cn);
        isv_settingdraft = (ItemSettingView) findViewById(R.id.isv_settingdraft);
        isv_settingwallet = (ItemSettingView) findViewById(R.id.isv_settingwallet);
        isv_settingmaybeinsteret = (ItemSettingView) findViewById(R.id.isv_settingmaybeinsteret);
        isv_settingbindaccount = (ItemSettingView) findViewById(R.id.isv_settingbindaccount);
        isv_settingnotifysetting = (ItemSettingView) findViewById(R.id.isv_settingnotifysetting);
        isv_settingauthentication = (ItemSettingView) findViewById(R.id.isv_settingauthentication);
        isv_settingclearcach = (ItemSettingView) findViewById(R.id.isv_settingclearcach);
        isv_settingfeedback = (ItemSettingView) findViewById(R.id.isv_settingfeedback);
        isv_settingaboutus = (ItemSettingView) findViewById(R.id.isv_settingaboutus);

        View tv_developer = findViewById(R.id.tv_developer);
        if (!ConstantsValue.SERVER_URL.contains("app")) {
            tv_developer.setOnClickListener(this);
            tv_developer.setVisibility(View.VISIBLE);
        } else
            tv_developer.setVisibility(View.GONE);

        isv_settingdraft.setOnClickListener(this);
        isv_settingwallet.setOnClickListener(this);
        isv_settingmaybeinsteret.setOnClickListener(this);
        isv_settingbindaccount.setOnClickListener(this);
        isv_settingnotifysetting.setOnClickListener(this);
        isv_settingauthentication.setOnClickListener(this);
        isv_settingclearcach.setOnClickListener(this);
        isv_settingfeedback.setOnClickListener(this);
        isv_settingaboutus.setOnClickListener(this);
        tv_loginout = (TextView) findViewById(R.id.tv_loginout);
        tv_loginout.setOnClickListener(this);
        tv_loginout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.isv_settingdraft://草稿箱
                startActivity(new Intent(context, MineDraftsActivity.class));
                break;
            case R.id.isv_settingwallet://钱包
                startActivity(new Intent(context, WalletActivity.class));
                break;
            case R.id.isv_settingmaybeinsteret://可能感兴趣的人
                startActivity(new Intent(context, RecommendSportsActivity.class).putExtra("isFromSetting", true));
                break;
            case R.id.isv_settingbindaccount://绑定账户
                startActivity(new Intent(context, AccountBindActivity.class));
                break;
            case R.id.isv_settingnotifysetting://通知设置
                startActivity(new Intent(context, NotificationSettingActivity.class));
                break;
            case R.id.isv_settingauthentication://秒嗨认证
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("title", "秒嗨认证");
                intent.putExtra("uri", getResources().getString(R.string.vip_authentication_url));
                startActivity(intent);
                break;
            case R.id.isv_settingclearcach://清空缓存
                TCAgent.onEvent(context, "设置-清空缓存" + ConstantsValue.android);
                commonDialog = new CommonDialog(context);
                commonDialog.setLeftButtonMsg("取消");
                commonDialog.setRightButtonMsg("确定");
                commonDialog.setContentMsg("是否要清空缓存?");
                commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                    @Override
                    public void onRightButtonOnClick() {
                        clearCache();
                    }
                });
                commonDialog.show();
                break;
            case R.id.isv_settingfeedback://意见反馈
                TCAgent.onEvent(context, "设置-意见反馈" + ConstantsValue.android);
                startActivity(new Intent(context, SettingFeedBackActivity.class));
                break;
            case R.id.isv_settingaboutus://关于我们
                startActivity(new Intent(context, AboutMiaoHiActivity.class));
                break;
            case R.id.tv_loginout:
                commonDialog = new CommonDialog(context);
                commonDialog.setLeftButtonMsg("取消");
                commonDialog.setRightButtonMsg("确定");
                commonDialog.setContentMsg("您确定要退出当前账户吗?");
                commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                    @Override
                    public void onRightButtonOnClick() {
                        loginoutAccountLogic();
                    }
                });
                commonDialog.show();
                break;
            case R.id.tv_developer:
                startActivity(new Intent(context, DeveloperActivity.class));
                break;
        }

    }

    /**
     * 用户退出登录逻辑
     */
    private void loginoutAccountLogic() {
        //SpUtils.put("is_login_success", false);
        SpUtils.put(ConstantsValue.Sp.IS_LOGIN_SUCCESS, false);
        UserInfoUtil.logout();
        FileUtils.deleteFile(context, ConstantsValue.Other.CHANNEL_NAME_NEW);
//        FileUtils.deleteFile(context, ConstantsValue.Video.VIDEO_TURF_FILE_DATA_PATH);
        Intent intent = new Intent("miaohilogout");
        MineSettingActivity.this.sendBroadcast(intent);
        //通知首页更新气泡
        Intent bubbleIntent = new Intent("broadcast_bubble");
        bubbleIntent.putExtra("giftCount", "0");
        bubbleIntent.putExtra("fansCount", "0");
        bubbleIntent.putExtra("messageCount", "0");
        MineSettingActivity.this.sendBroadcast(bubbleIntent);
        //SpUtils.put("isLoginoutApp", true);
        SpUtils.put(ConstantsValue.Sp.IS_LOGINOUT_APP, true);

        sendBroadcast(new Intent(ConstantsValue.IntentFilterAction.LOGOUT_ACTION));
        FoundSquareFragment.isNeedRefresh = true;
//        startActivity(new Intent(MineSettingActivity.this, LoginActivity.class));
        //RongIMClient.getInstance().logout();
        finish();
    }
//

    /**
     * 清理缓存
     */
    private void clearCache() {
        TCAgent.onEvent(context, "设置-清空缓存-确认" + ConstantsValue.android);
        showMHLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtils.deleteDir(new File(getCacheDir().getAbsolutePath()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hiddenLoadingView();
                        isv_settingclearcach.setRightText("0.0M");
                        showToastAtCenter("已清理完毕");
                    }
                });
            }
        }).start();
    }
//

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(java.io.File file) {
        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            MHLogUtil.e("getFolderSize",e);
        }
        return size;
    }
}
