package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.MHApplication;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;


/**
 * Created by zhandalin on 2016-07-05 10:15.
 * 说明:关于秒嗨
 */
public class AboutMiaoHiActivity extends BaseActivity implements View.OnClickListener {

    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_miao_hi);
        initView();
    }

    private void initView() {
        width = ScreenUtils.getScreenSize(context).x;


        findViewById(R.id.rl_user_protocol).setOnClickListener(this);
        findViewById(R.id.rl_manage_rule).setOnClickListener(this);
        findViewById(R.id.rl_check_update).setOnClickListener(this);
        findViewById(R.id.rl_ask_and_question).setOnClickListener(this);
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        if (ConstantsValue.isDeveloperMode(context)) {
            //如果要改版本号,请在这里更改,正式发版不会生效
            tv_version.setText("miaohi " + MHApplication.versionName + "v");
        } else {
            tv_version.setText("miaohi " + MHApplication.versionName + "v");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, WebActivity.class);
        switch (v.getId()) {
            case R.id.rl_user_protocol:
                intent.putExtra("uri", context.getResources().getString(R.string.text_miaohi_user_protocol_url));
                intent.putExtra("title", context.getResources().getString(R.string.text_miaohi_user_protocol));
                startActivity(intent);
                break;
            case R.id.rl_manage_rule:
                intent.putExtra("uri", context.getResources().getString(R.string.text_miaohi_communityt_rule_url));
                intent.putExtra("title", context.getResources().getString(R.string.text_miaohi_communityt_rule));
                startActivity(intent);
                break;
            case R.id.rl_ask_and_question:
                intent.putExtra("uri", context.getResources().getString(R.string.text_ask_and_question_url));
                intent.putExtra("title", context.getResources().getString(R.string.text_ask_and_question));
                startActivity(intent);
                break;
            case R.id.rl_check_update:
                CommonUtil.checkUpdate(context, false,null);
                break;
        }
    }


}
