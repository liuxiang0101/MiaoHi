package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.UmengLoginInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.CheckBindWeChatResponse;
import com.haiqiu.miaohi.umeng.IUMLoginCallback;
import com.haiqiu.miaohi.umeng.UmengLogin;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * 绑定微信
 * Created by ningl on 2016/7/26.
 */
public class BindWeChatActivity extends BaseActivity {

    private TextView tv_bindwechat_btn;
    private UMShareAPI umShareAPI;
    private CommonNavigation cv_bindwechat;
    private int postion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindwechat);
        if(getIntent().getExtras()!=null){
            postion = getIntent().getExtras().getInt("position");
        }
        initView();
        umShareAPI = UMShareAPI.get(this);
    }

    private void initView() {
        tv_bindwechat_btn = (TextView) findViewById(R.id.tv_bindwechat_btn);
        cv_bindwechat = (CommonNavigation) findViewById(R.id.cv_bindwechat);
        TextView tv = new TextView(context);
        tv.setTextColor(Color.parseColor("#8497a2"));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        tv.setPadding(0, 0, DensityUtil.dip2px(context, 17), 0);
        tv.setText("暂不绑定");
        cv_bindwechat.setRightLayoutView(tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_bindwechat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UmengLogin.isWeixinAvilible(BindWeChatActivity.this)){
                    UmengLogin.loginWX(BindWeChatActivity.this, umShareAPI, new IUMLoginCallback() {

                        @Override
                        public void callBackInfo(UmengLoginInfo loginInfo) {
                            bindWeChat(loginInfo.getMap().get("unionid"), loginInfo.getMap().get("openid"));
                        }
                    });
                } else {
                    showToastAtCenter("请安装微信客户端");
                }
            }
        });
    }

    /**
     * 绑定微信
     */
    private void bindWeChat(String unionId, String openId){
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("login_name_id", unionId);
        requestParams.addParams("weixin_union_id", unionId);
        requestParams.addParams("weixin_open_id", openId);
        MHHttpClient.getInstance().post(CheckBindWeChatResponse.class, context, ConstantsValue.Url.BINDINGWECHAT, requestParams, new MHHttpHandler<CheckBindWeChatResponse>() {
            @Override
            public void onSuccess(CheckBindWeChatResponse response) {
                Intent intent = new Intent("bindWeChat");
                intent.putExtra("position", postion);
                sendBroadcast(intent);
                finish();
            }

            @Override
            public void onFailure(String content) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        umShareAPI.onActivityResult(requestCode, resultCode, data);
    }
}
