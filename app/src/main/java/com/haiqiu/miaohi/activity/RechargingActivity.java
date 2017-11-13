package com.haiqiu.miaohi.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BasePayActivity;
import com.haiqiu.miaohi.base.MyBaseAdapter;
import com.haiqiu.miaohi.bean.PayInfoData;
import com.haiqiu.miaohi.bean.PayInfoRequestParam;
import com.haiqiu.miaohi.bean.RechargingItem;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.receiver.AutoConfirmEvent;
import com.haiqiu.miaohi.response.RechargingResponse;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.NoDoubleClickUtils;
import com.haiqiu.miaohi.view.CommonNavigation;
import com.haiqiu.miaohi.view.CustomDialog;
import com.haiqiu.miaohi.view.SmoothCheckBox;
import com.haiqiu.miaohi.widget.CommonDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * 充值
 * Created by hackest on 16/8/15.
 */
public class RechargingActivity extends BasePayActivity implements View.OnClickListener {
    private CommonNavigation title_bar;
    private TextView tv_balance;
    private ListView listview_recharging;
    private TextView tv_next;
    private List<RechargingItem> mList = new ArrayList<>();
    private RechargingListAdapter adapter;

    private int selectIndex;

    private TextView tv_problem;
    private CustomDialog.Builder builder;
    private Dialog dialog;
    private String not_wallet;
    private String ask;


    int other_price = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharging);
        not_wallet = getIntent().getStringExtra("not_wallet");
        ask = getIntent().getStringExtra("ask");

        setResult(400);
        initView();
        initData();
    }

    private void initData() {
        //        RechargingItem item1 = new RechargingItem("6嗨币", 6, false);
        //        RechargingItem item2 = new RechargingItem("28嗨币", 28, false);
        //        RechargingItem item3 = new RechargingItem("68嗨币", 68, false);
        //        RechargingItem item4 = new RechargingItem("108嗨币", 108, false);
        //        RechargingItem item5 = new RechargingItem("298嗨币", 298, false);
        //        RechargingItem item6 = new RechargingItem("其他金额", null, false);
        //        mList.add(item1);
        //        mList.add(item2);
        //        mList.add(item3);
        //        mList.add(item4);
        //        mList.add(item5);
        //        mList.add(item6);

        initHeader();//在setAdapter之前调用

        adapter = new RechargingListAdapter(this);
        listview_recharging.setAdapter(adapter);
        adapter.setSelectedPosition(0);


        listview_recharging.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //屏蔽header的点击
                    return;
                }

                if (position == adapter.getAdapterData().size()) {
                    MHLogUtil.e("其他金额");
                    showKeyboard(view);
                    builder = new CustomDialog.Builder(context);
                    builder.setTitle(R.string.input_money);
                    builder.setMessage("");
                    builder.setPositiveButton(R.string.confirm, dialogButtonClickListener);
                    builder.setNegativeButton(R.string.cancel, dialogButtonClickListener);
                    dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.show();

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            builder.showKeyboard();
                        }
                    });


                } else {
                    if (position == adapter.getAdapterData().size() + 1) {
                        //footer点击事件
                        final CommonDialog commonDialog = new CommonDialog(context);
                        commonDialog.setCancelable(true);
                        commonDialog.setLeftButtonMsg("确定");
                        commonDialog.setTitleMsg("遇到问题？");
                        commonDialog.setContentMsg(getString(R.string.problem_text));
                        commonDialog.hideRightButton();
                        commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
                            @Override
                            public void onLeftButtonOnClick() {
                                if (!isFinishing()) {
                                    commonDialog.dismiss();
                                }

                            }
                        });
                        commonDialog.show();


                        return;
                    }

                }

                selectIndex = position - 1;
                adapter.setSelectedPosition(position - 1);
                tv_next.setEnabled(true);//点击金额的时候下一步可以点击
                tv_next.setBackgroundResource(R.drawable.next_blue_gray_selector);
                adapter.getAdapterData().get(adapter.getAdapterData().size() - 1).setGoods_price(-1);//点击别的时候要把其他金额清空
                adapter.update();


            }
        });

        tv_next.setEnabled(false);
        showMHLoading(true, false);
        MHHttpClient.getInstance().post(RechargingResponse.class, context, ConstantsValue.Url.GETDEPOSITINFO, new MHHttpHandler<RechargingResponse>() {
            @Override
            public void onSuccess(RechargingResponse response) {
                tv_next.setEnabled(true);
                mList = response.getData().getPage_result();
                tv_balance.setText(CommonUtil.formatPrice4Point(response.getData().getBalance()));

                RechargingItem item = new RechargingItem(-1, -1, "PROD_def_dd1554a5_f111_43e1_b166_c621dc745079", false);
                mList.add(item);

                adapter.appendData(mList, true);
                adapter.update();


            }

            @Override
            public void onFailure(String content) {

            }
        });


    }

    private void initHeader() {
        View header = LayoutInflater.from(context).inflate(R.layout.recharging_list_header, null);
        tv_balance = (TextView) header.findViewById(R.id.tv_balance);

//        Typeface textFont = TypefaceHelper.get(context, "fonts/DINCond_Bold.otf");
//        tv_balance.setTypeface(textFont);
        tv_balance.getPaint().setFakeBoldText(true);

        listview_recharging.addHeaderView(header);
        View footer = LayoutInflater.from(context).inflate(R.layout.recharging_list_footer, null);
        tv_problem = (TextView) footer.findViewById(R.id.tv_problem);
        listview_recharging.addFooterView(footer);
    }


    private void initView() {
        title_bar = (CommonNavigation) findViewById(R.id.title_bar);
        title_bar.setRightText(getString(R.string.recharging_record));
        title_bar.setOnRightLayoutClickListener(new CommonNavigation.OnRightLayoutClick() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RechargingActivity.this, RechargingRecordActivity.class));

            }
        });
        listview_recharging = (ListView) findViewById(R.id.listview_recharging);
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next://下一步
                if (!NoDoubleClickUtils.isDoubleClick()) {

                    PayInfoRequestParam requestParam = new PayInfoRequestParam();

                    MHLogUtil.i(mList.get(selectIndex)+"");
                    requestParam.setGoods_id(mList.get(selectIndex).getGoods_id() + "");
                    requestParam.setDeposit_price(mList.get(selectIndex).getGoods_price() + "");//商品价格以分为单位
                    toPay(requestParam);
                }
                break;
            default:
                break;

        }

    }

    @Override
    protected void onPaySuccess(PayInfoData payInfoData) {
        super.onPaySuccess(payInfoData);

        tv_next.setEnabled(false);
        MHHttpClient.getInstance().post(RechargingResponse.class, context, ConstantsValue.Url.GETDEPOSITINFO, new MHHttpHandler<RechargingResponse>() {
            @Override
            public void onSuccess(RechargingResponse response) {
                tv_next.setEnabled(true);
                tv_balance.setText(CommonUtil.formatPrice4Point(response.getData().getBalance()));

                if (!TextUtils.isEmpty(ask)) {
                    EventBus.getDefault().post(new AutoConfirmEvent());
                }

                if (!TextUtils.isEmpty(not_wallet)) {
                    RechargingActivity.this.finish();
                }

            }

            @Override
            public void onFailure(String content) {

            }
        });

    }

    private DialogInterface.OnClickListener dialogButtonClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    dialog = (Dialog) dialogInterface;
                    EditText edit = (EditText) dialog.findViewById(R.id.edittext);
                    String text = edit.getText().toString();
                    if (TextUtils.isEmpty(text)) {
                        showToastAtCenter("请输入您要充值的金额");
                        return;
                    } else {

                        try {
                            MHLogUtil.e(Integer.parseInt(text)+"");

                            if (Integer.parseInt(text) >= 10 && Integer.parseInt(text) <= 1000) {

                                MHLogUtil.e("其他金额", other_price + "");
                                mList.get(mList.size() - 1).setGoods_price(Integer.parseInt(text) * 100);//商品价格以分为单位

                                adapter.update();

                            } else {
                                showToastAtCenter("请输入10-1000元的整数");
                                return;
                            }
                        } catch (NumberFormatException e) {
                            MHLogUtil.e(TAG,e);
                        }

                    }

                    dialog.dismiss();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:

                    tv_next.setEnabled(false);
                    tv_next.setBackgroundColor(getResources().getColor(R.color.color_df));


                    dialog.dismiss();
                    break;

                default:
                    break;
            }
        }
    };

    class RechargingListAdapter extends MyBaseAdapter<RechargingItem> {
        public int selectedId = -1; // 记录选中项


        public RechargingListAdapter(Context context) {
            super(context);
        }

        public void setSelectedPosition(int position) {
            selectedId = position;
        }

        public int getSelectedId() {
            return selectedId;
        }


        @Override
        public View getMyView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_recharging_list, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final RechargingItem bean = myList.get(position);

            if (position == myList.size() - 1) {
                //最后一条其他金额
                holder.tv_value.setText("其他金额");
                if (bean.getGoods_price() != -1) {
                    holder.tv_recharge_amount.setText("¥" + CommonUtil.formatPrice(bean.getGoods_price()));
                } else {
                    holder.tv_recharge_amount.setText(null);
                }

            } else {
                holder.tv_value.setText(CommonUtil.formatPrice(bean.getHi_coin()) + " 嗨币");
                holder.tv_recharge_amount.setText("¥" + CommonUtil.formatPrice(bean.getGoods_price()));
            }


            if (selectedId == position) {
                bean.setIsChecked(true);
                holder.root_view.setBackgroundResource(R.drawable.item_corners_bg_selected);
            } else {
                holder.root_view.setBackgroundResource(R.drawable.item_corners_bg_normal);
                bean.setIsChecked(false);
            }
            if (bean.getIsChecked()) {
                holder.cb.setChecked(true, true);
            } else {
                holder.cb.setChecked(false, false);
            }


            return convertView;
        }


        private DialogInterface.OnClickListener dialogButtonClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;

                    default:
                        break;
                }
            }
        };


        class ViewHolder {
            SmoothCheckBox cb;
            TextView tv_value;
            TextView tv_recharge_amount;
            View root_view;

            public ViewHolder(View rootView) {
                tv_value = (TextView) rootView.findViewById(R.id.tv_value);
                tv_recharge_amount = (TextView) rootView.findViewById(R.id.tv_recharge_amount);
                cb = (SmoothCheckBox) rootView.findViewById(R.id.scb);
                root_view = rootView.findViewById(R.id.root_view);


            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (hasPayAction) {
            tv_next.setEnabled(false);
            MHHttpClient.getInstance().post(RechargingResponse.class, context, ConstantsValue.Url.GETDEPOSITINFO, new MHHttpHandler<RechargingResponse>() {
                @Override
                public void onSuccess(RechargingResponse response) {
                    tv_next.setEnabled(true);
                    tv_balance.setText(CommonUtil.formatPrice4Point(response.getData().getBalance()));

                }

                @Override
                public void onFailure(String content) {

                }
            });
        }
    }
}
