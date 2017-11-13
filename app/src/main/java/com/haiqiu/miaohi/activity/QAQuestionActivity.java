package com.haiqiu.miaohi.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.response.ConfirmRequestResponse;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.NoDoubleClickUtils;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.haiqiu.miaohi.widget.PriceChangeDialog;
import com.tendcloud.tenddata.TCAgent;

/**
 * 映答提问
 * Created by ningl on 16/12/8.
 */
public class QAQuestionActivity extends BaseActivity {

    private CommonNavigation cn_qaquestion;
    private EditText et_qaqustion;
    private CheckBox cb_qaquestionprivate;
    private TextView tv_qaquestiontextcount;
    private String userId;
    private ImageView iv_back;
    private ImageView iv_submit;
    private int maxLength = 100;
    //提问价钱
    private long question_price;
    //回答者id
    private String answer_userId;
    private Dialog reportDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qaquestion);
        initView();
        question_price = getIntent().getLongExtra("question_price", 0);
        answer_userId = getIntent().getStringExtra("answer_userId");
        cb_qaquestionprivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {//返回
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_submit.setOnClickListener(new View.OnClickListener() {//提交
            @Override
            public void onClick(View v) {
                String content = et_qaqustion.getText().toString().trim();
                if (MHStringUtils.isEmpty(content)) {
                    showRectangleToast("提问不能为空");
                    return;
                }

                if (!NoDoubleClickUtils.isDoubleClick()) {
                    if (question_price != 0) {
                        confirmPayResultRequest(content);
                    } else {
                        askQuestion(content);
                    }
                    TCAgent.onEvent(context, "确定提问" + ConstantsValue.android);
                }
            }
        });

        et_qaqustion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                int currentLength = 0;
                currentLength = maxLength - length;
                tv_qaquestiontextcount.setText(currentLength + "");
            }
        });
    }

    private void initView() {
        cn_qaquestion = (CommonNavigation) findViewById(R.id.cn_qaquestion);
        et_qaqustion = (EditText) findViewById(R.id.et_qaqustion);
        cb_qaquestionprivate = (CheckBox) findViewById(R.id.cb_qaquestionprivate);
        tv_qaquestiontextcount = (TextView) findViewById(R.id.tv_qaquestiontextcount);
        iv_back = new ImageView(context);
        iv_back.setPadding(DensityUtil.dip2px(context, 11), 0, 0, 0);
        iv_back.setImageResource(R.drawable.backx);
        cn_qaquestion.setLeftLayoutView(iv_back);
        iv_submit = new ImageView(context);
        iv_submit.setImageResource(R.drawable.checkmark);
        iv_submit.setPadding(0, 0, DensityUtil.dip2px(context, 10), 0);
        cn_qaquestion.setRightLayoutView(iv_submit);
    }


    /**
     * 提问
     *
     * @param content 提问的内容
     */
    private void askQuestion(final String content) {
        showLoading();
        iv_submit.setEnabled(false);
        final MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("answer_user", answer_userId);
        requestParams.addParams("question_content", Base64Util.getBase64Str(content));
        requestParams.addParams("question_private", cb_qaquestionprivate.isChecked() ? "0" : "1");
        requestParams.addParams("question_price", question_price + "");

        MHHttpClient.getInstance().post(BaseResponse.class, context, ConstantsValue.Url.SUBMIT_QUESTION_TOVIP, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                Intent intent = new Intent();
                intent.putExtra("questionprice", question_price);
                setResult(ConstantsValue.Other.ASKQUESTION_RESULT, intent);
                finish();
                et_qaqustion.setText(null);
                iv_submit.setEnabled(true);
                TCAgent.onEvent(getApplicationContext(), "向ta提问成功" + ConstantsValue.android);
            }

            @Override
            public void onFailure(String content) {
                iv_submit.setEnabled(true);
                TCAgent.onEvent(getApplicationContext(), "向ta提问失败" + ConstantsValue.android);
            }

            @Override
            public void onStatusIsError(String message) {
                iv_submit.setEnabled(true);
                TCAgent.onEvent(getApplicationContext(), "向ta提问失败" + ConstantsValue.android);
                super.onStatusIsError(message);
            }
        });
    }


    /**
     * 确认钱包是否有钱 如果有钱直接支付否则充值
     *
     * @param content 提问内容
     */
    private void confirmPayResultRequest(final String content) {
        final MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("answer_user", answer_userId);//二次确认类型，10：围观，30：提问
        requestParams.addParams("type", "30");
        requestParams.addParams("price", question_price + "");

        MHHttpClient.getInstance().post(ConfirmRequestResponse.class, context, ConstantsValue.Url.CONFIRMPAYSECONDREQUEST, requestParams, new MHHttpHandler<ConfirmRequestResponse>() {
            @Override
            public void onSuccess(ConfirmRequestResponse response) {
                ConfirmRequestResponse.DataBean dataBean = response.getData();
                if (dataBean == null) return;
                if (dataBean.getState() == ConstantsValue.Other.CONFIRM_PAYSECONDRE_BALANCE) {//付钱去提问
                    askQuestion(content);
                } else if (dataBean.getState() == ConstantsValue.Other.CONFIRM_PAYSECONDRE_ASK) {//提问价钱改变
                    question_price = dataBean.getPrice();
                    PriceChangeDialog dialog = new PriceChangeDialog(context, dataBean.getPrice());
                    dialog.show();
                    dialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                        @Override
                        public void onRightButtonOnClick() {
                            confirmPayResultRequest(content);
                        }
                    });
                } else if (dataBean.getState() == ConstantsValue.Other.CONFIRM_PAYSECONDRE_NOBALANCE) {//去充值
                    final CommonDialog commonDialog = new CommonDialog(context);
                    commonDialog.setCancelable(true);
                    commonDialog.setLeftButtonMsg("取消");
                    commonDialog.setRightButtonMsg("去充值");
                    commonDialog.setTitleMsg(response.getData().getTitle());
                    if (!TextUtils.isEmpty(response.getData().getContent()) && response.getData().getContent().contains("，")) {
                        String content = response.getData().getContent().replace("，", "，\n");
                        commonDialog.setContentMsg(content);
                    }
                    commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
                        @Override
                        public void onLeftButtonOnClick() {
                            if (!isFinishing()) {
                                commonDialog.dismiss();
                            }

                        }
                    });
                    commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                        @Override
                        public void onRightButtonOnClick() {
                            Intent intent = new Intent();
                            intent.setClass(context, RechargingActivity1.class);
                            intent.putExtra("not_wallet", "not_wallet");
                            intent.putExtra("ask", "ask");

                            startActivity(intent);
                        }
                    });
                    commonDialog.show();

                }


            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == RechargingActivity1.PAYRESULT_CODE) {
                confirmPayResultRequest(et_qaqustion.getText().toString().trim());
            }
        }
    }
}
