package com.haiqiu.miaohi.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.MHApplication;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;

/**
 * Created by zhandalin on 2017-01-22 11:02.
 * 说明:开发者专用的页面,正式环境会关闭入口
 */
public class DeveloperActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_server_address;
    private EditText et_change_server_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        initView();
    }

    private void initView() {
        tv_server_address = (TextView) findViewById(R.id.tv_server_address);
        TextView tv_info = (TextView) findViewById(R.id.tv_info);
        tv_info.setText("客户端信息:\nmac=" + MHApplication.mac
                + "\nToken=" + MHApplication.mMiaohiToken
                + "\nimei=" + MHApplication.imei);
        et_change_server_address = (EditText) findViewById(R.id.et_change_server_address);
        Button bt_conform = (Button) findViewById(R.id.bt_conform);

        tv_server_address.setText("当前服务器地址:" + ConstantsValue.SERVER_URL);
        et_change_server_address.setText(ConstantsValue.SERVER_URL);
        et_change_server_address.setSelection(ConstantsValue.SERVER_URL.length());
        bt_conform.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_conform:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String address = et_change_server_address.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            showToastAtCenter("address不能为空");
            return;
        }
        ConstantsValue.SERVER_URL = address;

        tv_server_address.setText("当前服务器地址:" + ConstantsValue.SERVER_URL);
        showToastAtCenter("修改成功,请重新登录");
    }
}
