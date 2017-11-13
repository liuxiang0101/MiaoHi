package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.AccountBindBean;
import com.haiqiu.miaohi.bean.AccountBindInfo;
import com.haiqiu.miaohi.bean.UmengLoginInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.AccountBindResponse;
import com.haiqiu.miaohi.umeng.IUMLoginCallback;
import com.haiqiu.miaohi.umeng.UmengLogin;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.view.AccountBindDialog;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

/**
 * 账号绑定
 * Created by ningl on 16-8-29.
 */
public class AccountBindActivity extends BaseActivity implements View.OnClickListener, AccountBindDialog.OnItemClick {
    private ImageView iv_accountbind_phone;
    private TextView tv_accountbind_phonename;
    private TextView tv_accountbind_phonebtn;
    private RelativeLayout rl_accountbind_phone;
    private ImageView iv_accountbind_wechat;
    private TextView tv_accountbind_wechatname;
    private TextView tv_accountbind_wechatbtn;
    private RelativeLayout rl_accountbind_wechat;
    private ImageView iv_accountbind_qq;
    private TextView tv_accountbind_qqname;
    private TextView tv_accountbind_qqbtn;
    private RelativeLayout rl_accountbind_qq;
    private ImageView iv_accountbind_sina;
    private TextView tv_accountbind_sinaname;
    private TextView tv_accountbind_sinabtn;
    private RelativeLayout rl_accountbind_sina;

    public static final int ACCOUNTBIND_PHONE = 1;
    public static final int ACCOUNTBIND_QQ = 2;
    public static final int ACCOUNTBIND_WECHAT = 3;
    public static final int ACCOUNTBIND_SINA = 4;

    public static final int ACCOUNT_BIND = 10;//绑定
    public static final int ACCOUNT_CHANGEBIND = 20;//更换绑定

//    private boolean isPhoneBind;
//    private boolean isQQBind;
//    private boolean isWechatBind;
//    private boolean isSinaBind;

    private UMShareAPI umShareAPI;
    private AccountBindInfo accountBindInfo;//绑定请求的参数
    private AccountBindBean accountBindBean;//绑定成功 刷新界面参数
    private AccountBindDialog accountBindDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountbind);
        umShareAPI = UMShareAPI.get(context);
        initView();
        getNetData();
    }

    /**
     * 控件初始化
     */
    private void initView() {
        accountBindInfo = new AccountBindInfo();
        iv_accountbind_phone = (ImageView) findViewById(R.id.iv_accountbind_phone);
        tv_accountbind_phonename = (TextView) findViewById(R.id.tv_accountbind_phonename);
        tv_accountbind_phonebtn = (TextView) findViewById(R.id.tv_accountbind_phonebtn);
        rl_accountbind_phone = (RelativeLayout) findViewById(R.id.rl_accountbind_phone);
        iv_accountbind_wechat = (ImageView) findViewById(R.id.iv_accountbind_wechat);
        tv_accountbind_wechatname = (TextView) findViewById(R.id.tv_accountbind_wechatname);
        tv_accountbind_wechatbtn = (TextView) findViewById(R.id.tv_accountbind_wechatbtn);
        rl_accountbind_wechat = (RelativeLayout) findViewById(R.id.rl_accountbind_wechat);
        iv_accountbind_qq = (ImageView) findViewById(R.id.iv_accountbind_qq);
        tv_accountbind_qqname = (TextView) findViewById(R.id.tv_accountbind_qqname);
        tv_accountbind_qqbtn = (TextView) findViewById(R.id.tv_accountbind_qqbtn);
        rl_accountbind_qq = (RelativeLayout) findViewById(R.id.rl_accountbind_qq);
        iv_accountbind_sina = (ImageView) findViewById(R.id.iv_accountbind_sina);
        tv_accountbind_sinaname = (TextView) findViewById(R.id.tv_accountbind_sinaname);
        tv_accountbind_sinabtn = (TextView) findViewById(R.id.tv_accountbind_sinabtn);
        rl_accountbind_sina = (RelativeLayout) findViewById(R.id.rl_accountbind_sina);
        addEvent();
    }

    /**
     * 为控件添加事件
     */
    private void addEvent() {
        tv_accountbind_phonebtn.setOnClickListener(this);
        tv_accountbind_qqbtn.setOnClickListener(this);
        tv_accountbind_wechatbtn.setOnClickListener(this);
        tv_accountbind_sinabtn.setOnClickListener(this);
        rl_accountbind_phone.setOnClickListener(this);
        rl_accountbind_qq.setOnClickListener(this);
        rl_accountbind_wechat.setOnClickListener(this);
        rl_accountbind_sina.setOnClickListener(this);
        rl_accountbind_phone.setEnabled(false);
        rl_accountbind_qq.setEnabled(false);
        rl_accountbind_wechat.setEnabled(false);
        rl_accountbind_sina.setEnabled(false);
    }

    /**
     * 获取用户相关的绑定数据
     */
    private void getNetData() {
        showLoading("loading...", true, false);
        MHHttpClient.getInstance().post(AccountBindResponse.class, context, ConstantsValue.Url.SEARCHBINDACCOUNT, new MHHttpHandler<AccountBindResponse>() {
            @Override
            public void onSuccess(AccountBindResponse response) {
                if (null != response) {
                    List<AccountBindBean> accountBindBeans = response.getData().getPage_result();
                    initBind(accountBindBeans);
                }
            }

            @Override
            public void onFailure(String content) {
            }
        });
    }

    /**
     * 初始化绑定的数据
     */
    private void initBind(List<AccountBindBean> accountBindBeans) {
        for (AccountBindBean accountBindBean : accountBindBeans) {
            setBind(true, accountBindBean.getAccount_type(), accountBindBean.getAccount_num());
        }
    }

    /**
     * 设置控件显隐,展示绑定信息
     */
    private void setBind(boolean isBindAccount, int accountType, String accountName) {
        switch (accountType) {
            case ACCOUNTBIND_PHONE:
                setViewOption(isBindAccount, tv_accountbind_phonebtn, tv_accountbind_phonename, rl_accountbind_phone, accountName);
//                isPhoneBind = true;
                break;
            case ACCOUNTBIND_QQ:
                setViewOption(isBindAccount, tv_accountbind_qqbtn, tv_accountbind_qqname, rl_accountbind_qq, accountName);
//                isQQBind = true;
                break;
            case ACCOUNTBIND_WECHAT:
                setViewOption(isBindAccount, tv_accountbind_wechatbtn, tv_accountbind_wechatname, rl_accountbind_wechat, accountName);
//                isWechatBind = true;
                break;
            case ACCOUNTBIND_SINA:
                setViewOption(isBindAccount, tv_accountbind_sinabtn, tv_accountbind_sinaname, rl_accountbind_sina, accountName);
//                isSinaBind = true;
                break;
        }
    }

    /**
     * 设置未绑定数据
     */
    private void setUnbind(int bindType) {
        switch (bindType) {
            case ACCOUNTBIND_PHONE:
                setViewOption(false, tv_accountbind_phonebtn, tv_accountbind_phonename, rl_accountbind_phone, "");
//                isPhoneBind = false;
                break;
            case ACCOUNTBIND_QQ:
                setViewOption(false, tv_accountbind_qqbtn, tv_accountbind_qqname, rl_accountbind_qq, "");
//                isQQBind = false;
                break;
            case ACCOUNTBIND_WECHAT:
                setViewOption(false, tv_accountbind_wechatbtn, tv_accountbind_wechatname, rl_accountbind_wechat, "");
//                isWechatBind = false;
                break;
            case ACCOUNTBIND_SINA:
                setViewOption(false, tv_accountbind_sinabtn, tv_accountbind_sinaname, rl_accountbind_sina, "");
//                isSinaBind = false;
                break;
        }
    }

    private void setViewOption(boolean b, TextView tvButton, TextView tvName, RelativeLayout rl, String name) {
        tvButton.setVisibility(b ? View.GONE : View.VISIBLE);
        tvName.setVisibility(b ? View.VISIBLE : View.GONE);
        tvName.setText(name);
        rl.setEnabled(b);
    }

    /**
     * 获取用户绑定数
     */
    private int getBindCount() {
        int count = 0;
        TextView[] textViews = {tv_accountbind_phonename, tv_accountbind_qqname, tv_accountbind_wechatname, tv_accountbind_sinaname};
        for (int i = 0; i < textViews.length; i++) {
            if (textViews[i].getVisibility() == View.VISIBLE) {
                count++;
            }
        }
        return count;
    }

    /**
     * 绑定或更换绑定账号
     */
    private void bind(final int bindType, final int bind) {
        showLoading();
        MHRequestParams params = new MHRequestParams();
        params.addParams("account_type", String.valueOf(bindType));
        params.addParams("bind_type", String.valueOf(bind));
        params.addParams("login_name_id", accountBindInfo.getLogin_name_id());
        params.addParams("nick_name", Base64Util.getBase64Str(accountBindInfo.getNick_name()));
        if (bindType == ACCOUNTBIND_WECHAT) {
            params.addParams("weixin_union_id", accountBindInfo.getWeixin_union_id());
            params.addParams("weixin_open_id", accountBindInfo.getWeixin_open_id());
        }
        MHHttpClient.getInstance().post(AccountBindResponse.class, context, ConstantsValue.Url.BINDACCOUNT, params, new MHHttpHandler<AccountBindResponse>() {
            @Override
            public void onSuccess(AccountBindResponse response) {
                accountBindBean = new AccountBindBean(accountBindInfo.getNick_name(), bindType);
                setBind(true, accountBindBean.getAccount_type(), accountBindBean.getAccount_num());
                showRectangleToast(R.drawable.registsuccess, "绑定成功，你现在可以使用新绑定的账号登录了！");
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

    /**
     * 绑定第三方账户
     */
    private void goLogin4Bind(final int bindType, final int bind) {
        switch (bindType) {
            case ACCOUNTBIND_QQ:
                UmengLogin.loginQQ(AccountBindActivity.this, umShareAPI, new IUMLoginCallback() {

                    @Override
                    public void callBackInfo(UmengLoginInfo loginInfo) {
                        accountBindInfo.setAccount_type(ACCOUNTBIND_QQ);
                        accountBindInfo.setLogin_name_id(loginInfo.getThreeId());
                        accountBindInfo.setNick_name(loginInfo.getNickname());
                        bind(ACCOUNTBIND_QQ, bind);
                    }
                });
                break;
            case ACCOUNTBIND_WECHAT:
                UmengLogin.loginWX(AccountBindActivity.this, umShareAPI, new IUMLoginCallback() {

                    @Override
                    public void callBackInfo(UmengLoginInfo loginInfo) {
                        accountBindInfo.setAccount_type(ACCOUNTBIND_WECHAT);
                        accountBindInfo.setLogin_name_id(loginInfo.getThreeId());
                        accountBindInfo.setNick_name(loginInfo.getNickname());
                        accountBindInfo.setWeixin_union_id(loginInfo.getThreeId());
                        accountBindInfo.setWeixin_open_id(loginInfo.getMap().get("openid"));
                        bind(ACCOUNTBIND_WECHAT, bind);
                    }
                });
                break;
            case ACCOUNTBIND_SINA:
                UmengLogin.loginSina(AccountBindActivity.this, umShareAPI, new IUMLoginCallback() {

                    @Override
                    public void callBackInfo(UmengLoginInfo loginInfo) {
                        accountBindInfo.setAccount_type(ACCOUNTBIND_SINA);
                        accountBindInfo.setLogin_name_id(loginInfo.getThreeId());
                        accountBindInfo.setNick_name(loginInfo.getNickname());
                        bind(ACCOUNTBIND_SINA, bind);
                    }
                });
                break;
        }
    }

    /**
     * 解除绑定功能
     */
    private void unBind(final int bindType) {
        showLoading();
        MHRequestParams params = new MHRequestParams();
        params.addParams("account_type", String.valueOf(bindType));
        MHHttpClient.getInstance().post(AccountBindResponse.class, context, ConstantsValue.Url.REMOVEBINDACCOUNT, params, new MHHttpHandler<AccountBindResponse>() {
            @Override
            public void onSuccess(AccountBindResponse response) {
                setUnbind(bindType);
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

    /**
     * 设置对话框
     *
     * @return
     */
    private void showBindDialog(int bindType) {
        if (null == accountBindDialog) accountBindDialog = new AccountBindDialog(context);
        accountBindDialog.setOnItemClickListener(this)
                .setType(bindType);
        MHLogUtil.e("绑定数量" + getBindCount());
        if (getBindCount() <= 1) {
            accountBindDialog.setData("更换账号");
        } else {
            accountBindDialog.setData("更换绑定账号", "解除绑定");
        }
        accountBindDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_accountbind_phonebtn:
                startActivity(new Intent(AccountBindActivity.this, LRBoundPhoneActivity.class)
                        .putExtra("isFromAccountBound", ACCOUNT_BIND));
                break;
            case R.id.tv_accountbind_qqbtn:
                showLoading();
                goLogin4Bind(ACCOUNTBIND_QQ, ACCOUNT_BIND);
                break;
            case R.id.tv_accountbind_wechatbtn:
                showLoading();
                goLogin4Bind(ACCOUNTBIND_WECHAT, ACCOUNT_BIND);
                break;
            case R.id.tv_accountbind_sinabtn:
                showLoading();
                goLogin4Bind(ACCOUNTBIND_SINA, ACCOUNT_BIND);
                break;
            case R.id.rl_accountbind_phone:
                accountBindDialog = new AccountBindDialog(context)
                        .setData("更换手机号")
                        .setOnItemClickListener(this)
                        .setType(ACCOUNTBIND_PHONE);
                accountBindDialog.show();
                break;
            case R.id.rl_accountbind_qq:
                showBindDialog(ACCOUNTBIND_QQ);
                break;
            case R.id.rl_accountbind_wechat:
                showBindDialog(ACCOUNTBIND_WECHAT);
                break;
            case R.id.rl_accountbind_sina:
                showBindDialog(ACCOUNTBIND_SINA);
                break;
        }
    }

    @Override
    public void onItemClick(RelativeLayout ll_item, int tag, int type) {
        switch (tag) {
            case 0://更换账户
                if (type == ACCOUNTBIND_PHONE) {
                    startActivity(new Intent(AccountBindActivity.this, LRBoundPhoneActivity.class)
                            .putExtra("isFromAccountBound", ACCOUNT_CHANGEBIND));
                } else {
//                    if(bindCount>1){
                    goLogin4Bind(type, ACCOUNT_CHANGEBIND);
//                    } else {
//                        if(null != accountBindDialog) accountBindDialog.dismiss();
//                        unBind(type);
//                    }
                }
                break;
            case 1://解除绑定
                if (type != ACCOUNTBIND_PHONE && getBindCount() > 1) {
                    if (null != accountBindDialog) accountBindDialog.dismiss();
                    unBind(type);
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        hiddenLoadingView();
        if (null != accountBindDialog) accountBindDialog.dismiss();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        umShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            String phoneNum = intent.getExtras().getString("phoneNum");
            if (!MHStringUtils.isEmpty(phoneNum)) {
                AccountBindBean accountBindBean = new AccountBindBean("+86" + phoneNum, ACCOUNTBIND_PHONE);
                setBind(true, accountBindBean.getAccount_type(), accountBindBean.getAccount_num());
            }
        }

    }
}
