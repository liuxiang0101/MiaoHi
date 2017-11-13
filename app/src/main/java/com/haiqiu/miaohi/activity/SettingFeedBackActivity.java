package com.haiqiu.miaohi.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.net.utils.Base64;

/**
 * 设置-意见反馈
 * Created by ningl on 2016/4/11.
 */
public class SettingFeedBackActivity extends BaseActivity {
    private TextView tv_word_count;
    private EditText et_feedback;
    private EditText et_feedback_mial;
    private Button bt_setting_feedback_commit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting_feedback);
        initView();
    }

    private void initView() {
        tv_word_count = (TextView) findViewById(R.id.tv_word_count);
        et_feedback = (EditText) findViewById(R.id.et_feedback);
        et_feedback_mial = (EditText) findViewById(R.id.et_feedback_mial);
        bt_setting_feedback_commit = (Button) findViewById(R.id.bt_setting_feedback_commit);

        bt_setting_feedback_commit.setOnClickListener(l);
        EditChangedListener editChangedListener = new EditChangedListener();
        et_feedback.addTextChangedListener(editChangedListener);
    }

    private void setFeedBack() {
        showLoading();
        MHRequestParams requestParams = new MHRequestParams();
        String questionText = et_feedback.getText().toString().trim();
        final String contactAddress = et_feedback_mial.getText().toString().trim();
        requestParams.addParams("question_text", Base64.encodeBase64String(questionText.getBytes()));
        requestParams.addParams("contact_address", Base64.encodeBase64String(contactAddress.getBytes()));
        MHHttpClient.getInstance().post(BaseResponse.class, context, ConstantsValue.Url.FEEDBACK, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                onBackPressed();
                Toast.makeText(getApplicationContext(), "已提交，感谢您的反馈", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String content) {
            }
        });
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_setting_feedback_commit:   //提交
                    TCAgent.onEvent(context, "设置-意见反馈提交" + ConstantsValue.android);
                    if (TextUtils.equals("", et_feedback.getText().toString().trim())) {
                        showToastAtBottom("亲,您还没有输入宝贵的意见或建议");
                        break;
                    }
                    if (et_feedback.getText().toString().trim().length() > 300) {
                        showToastAtBottom("亲，您最多只能输入300个字");
                        break;
                    }
//                    if (TextUtils.equals("", et_feedback_mial.getText().toString().trim())) {
//                        Toast.makeText(SettingFeedBackActivity.this, "联系方式不能为空", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
                    setFeedBack();
                    break;
            }
        }
    };

    //编辑框输入监听
    class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (et_feedback.getText().toString().trim().length() > 0/* && et_feedback_mial.getText().toString().trim().length() > 0*/) {
                bt_setting_feedback_commit.setBackgroundResource(R.drawable.blue_gray_selector_rectangle);
            } else {
                bt_setting_feedback_commit.setBackgroundColor(getResources().getColor(R.color.color_df));
            }

            int count = 300 - et_feedback.getText().toString().trim().length();
            tv_word_count.setText(count + "");
            if (count < 0) tv_word_count.setTextColor(getResources().getColor(R.color.red_text));
            else tv_word_count.setTextColor(getResources().getColor(R.color.feedback_et_bg));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onHideSoftInput(event);
        return super.onTouchEvent(event);
    }
}
