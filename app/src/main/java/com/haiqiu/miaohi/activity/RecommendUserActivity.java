package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.AttentionSingleBean;
import com.haiqiu.miaohi.bean.RecommendUserResult;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.RecommendUserResponse;
import com.haiqiu.miaohi.utils.MHStateSyncUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SetClickStateUtil;
import com.haiqiu.miaohi.view.MyCircleView;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LiuXiang on 2016/9/20.
 * 首次注册完成后的推荐用户进行关注界面
 */
public class RecommendUserActivity extends BaseActivity {
    public static final String REFRESH_DATA_ACTION = "refreshCurrentFragmentData";
    private GridView gv_recommend_user;
    private TextView tv_next;

    private int listSize = 0;
    private boolean isFromSetting = false;
    private HashMap<Integer, Boolean> hashMap;
    private List<RecommendUserResult> list = new ArrayList<>();
    private ImageLoader imageLoader;
    private TextView tv_size;
    private ImageView iv_close;
    private RelativeLayout relativeLayout;
    private TextView tv_error_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_user);
        isFromSetting = getIntent().getBooleanExtra("isFromSetting", false);
        showLoading();
        initView();
        getNetData();
    }

    /**
     * 初始化操作
     */
    private void initView() {
        hashMap = new HashMap<>();
        tv_size = (TextView) findViewById(R.id.tv_size);
        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
        gv_recommend_user = (GridView) findViewById(R.id.gv_recommend_user);
        tv_next = (TextView) findViewById(R.id.tv_next);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_close.setVisibility(isFromSetting ? View.VISIBLE : View.INVISIBLE);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAttention(v);
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SetClickStateUtil.getInstance().setStateListener(iv_close);
        tv_next.setAlpha(0.9f);
    }

    /**
     * 获取服务端数据
     */
    private void getNetData() {
        showMHLoading(true, false);
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_size", "12");
        requestParams.addParams("page_index", "0");
        MHHttpClient.getInstance().post(RecommendUserResponse.class, context, ConstantsValue.Url.RECOMMEND_USER_FOR_ATTENTION, requestParams, new MHHttpHandler<RecommendUserResponse>() {
            @Override
            public void onSuccess(RecommendUserResponse response) {
                list = response.getData().getPage_result();
                if (list == null) {
                    relativeLayout.setVisibility(View.INVISIBLE);
                    tv_error_msg.setVisibility(View.VISIBLE);
                    return;
                } else if (list.size() == 0) {
                    relativeLayout.setVisibility(View.INVISIBLE);
                    tv_error_msg.setVisibility(View.VISIBLE);
                } else {
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                tv_size.setText(list.size() + "");
                RecommendUserAdapter gvAdapter = new RecommendUserAdapter();
                gv_recommend_user.setAdapter(gvAdapter);
            }

            @Override
            public void onFailure(String content) {
                relativeLayout.setVisibility(View.INVISIBLE);
                showErrorView();
            }

            @Override
            public void onStatusIsError(String message) {
                relativeLayout.setVisibility(View.INVISIBLE);
                showErrorView();
                super.onStatusIsError(message);
            }
        });
    }

    @Override
    protected void reTry() {
        getNetData();
        super.reTry();
    }

    /**
     * 点击确认关注(批量关注)
     *
     * @param view
     */
    public void confirmAttention(View view) {
        if (list.size() > 0) {              //如果有推荐的用户时
//            if (!hashMap.containsValue(true)) {
//                showToastAtBottom("最少选择1位感兴趣的人");
//                return;
//            }
            showLoading("请稍后...", true, false);
            String attentionList = new Gson().toJson(getAttentionUserList());
            MHRequestParams requestParams = new MHRequestParams();
            requestParams.addParams("info", attentionList);
            MHHttpClient.getInstance().post(RecommendUserResponse.class, context, ConstantsValue.Url.BATCH_ATTENTION_DO, requestParams, new MHHttpHandler<RecommendUserResponse>() {
                @Override
                public void onSuccess(RecommendUserResponse response) {
//                    showToastAtBottom("关注用户成功");

                    for (AttentionSingleBean bean : getAttentionUserList()) {
                        MHStateSyncUtil.pushSyncEvent(context, bean.getUser_id(), Boolean.parseBoolean(bean.getAction_mark()));
                    }

                    finish();
                    if (!isFromSetting)
                        startActivity(new Intent(context, MainActivity.class));
//                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                    sendBroadcast(new Intent(REFRESH_DATA_ACTION));
                }

                @Override
                public void onFailure(String content) {
                }

                @Override
                public void onStatusIsError(String message) {
                    super.onStatusIsError(message);
                }
            });
        } else {                            //如果没有推荐的用户时，直接结束
            finish();
            if (!isFromSetting)
                startActivity(new Intent(context, MainActivity.class));
//            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            sendBroadcast(new Intent(REFRESH_DATA_ACTION));
            return;
        }
    }

    /**
     * 适配器适配
     */
    class RecommendUserAdapter extends BaseAdapter {
        public RecommendUserAdapter() {
            imageLoader = ImageLoader.getInstance();
            listSize = list.size() > 12 ? 12 : list.size();
            for (int i = 0; i < listSize; i++) {
                hashMap.put(i, true);
            }
        }

        @Override
        public int getCount() {
            return listSize;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                //删除时删除无用布局
//                convertView = View.inflate(context, R.layout.item_recommend_user, null);
                convertView = View.inflate(context, R.layout.item_common_user_show, null);
                viewHolder = new ViewHolder();
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final RecommendUserResult object = list.get(position);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.iv_vip_mark = (ImageView) convertView.findViewById(R.id.iv_vip_mark);
            viewHolder.iv_head = (MyCircleView) convertView.findViewById(R.id.iv_head);
            viewHolder.iv_gender = (ImageView) convertView.findViewById(R.id.iv_gender);
            viewHolder.bt_attention = (Button) convertView.findViewById(R.id.bt_attention);
            //vip标志
            viewHolder.iv_vip_mark.setVisibility(object.getUser_type() > 10 ? View.VISIBLE : View.INVISIBLE);
            //用户名
            if (MHStringUtils.isEmpty(object.getUser_name()))
                viewHolder.tv_name.setText("");
            else
                viewHolder.tv_name.setText(object.getUser_name());
            //用户头衔
            if (MHStringUtils.isEmpty(object.getVip_note())) {
                viewHolder.tv_title.setVisibility(View.GONE);
            } else {
                viewHolder.tv_title.setVisibility(View.VISIBLE);
                viewHolder.tv_title.setText(object.getVip_note());
            }
            viewHolder.iv_gender.setImageResource(object.getUser_gender() == 2 ? R.drawable.gender_women : R.drawable.gender_man);

            if (hashMap.get(position)) {
                viewHolder.bt_attention.setBackgroundResource(R.drawable.tag_bg);
                viewHolder.bt_attention.setTextColor(context.getResources().getColor(R.color.color_c4));
                viewHolder.bt_attention.setText("已关注");
            } else {
                viewHolder.bt_attention.setBackgroundResource(R.drawable.shape_attention_blue_selector);
                viewHolder.bt_attention.setTextColor(context.getResources().getColor(R.color.fontblue));
                viewHolder.bt_attention.setText("关注");
            }
            //用户头像
            imageLoader.displayImage(object.getPortrait_uri(), viewHolder.iv_head);
            //点击关注按钮
            viewHolder.bt_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hashMap.get(position)) {
                        CommonDialog commonDialog = new CommonDialog(RecommendUserActivity.this);
                        commonDialog.setLeftButtonMsg("取消");
                        commonDialog.setRightButtonMsg("确定");
                        commonDialog.setContentMsg("确定要取消关注吗？");
                        commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                            @Override
                            public void onRightButtonOnClick() {
                                hashMap.put(position, false);
                                notifyDataSetChanged();
                            }
                        });
                        commonDialog.show();
                        return;
                    }
                    hashMap.put(position, true);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        private class ViewHolder {
            MyCircleView iv_head;
            ImageView iv_vip_mark;
            ImageView iv_gender;
            TextView tv_name;
            TextView tv_title;
            Button bt_attention;
        }
    }

//    /**
//     * 更改对用户的关注状态
//     *
//     * @param userId
//     * @param targetAttentionState
//     */
//    private void changeAttentionState(String userId, final String targetAttentionState, final View view) {
//        if (MHStringUtils.isEmpty(userId) || MHStringUtils.isEmpty(targetAttentionState)) return;
//        final TextView textView = (TextView) view;
//        MHRequestParams requestParams = new MHRequestParams();
//        requestParams.addParams("action_mark", targetAttentionState);
//        requestParams.addParams("user_id", userId);
//        textView.setEnabled(false);
//        textView.setText(null);
//        MHHttpClient.getInstance().post(HomeFoundResponse.class, context, ConstantsValue.Url.ATTENTIONDO, requestParams, new MHHttpHandler<HomeFoundResponse>() {
//            @Override
//            public void onSuccess(HomeFoundResponse response) {
//                resetState(textView, Boolean.parseBoolean(targetAttentionState));
//            }
//
//            @Override
//            public void onFailure(String content) {
//                resetState(textView, !Boolean.parseBoolean(targetAttentionState));
//            }
//
//            @Override
//            public void onStatusIsError(String message) {
//                super.onStatusIsError(message);
//                resetState(textView, !Boolean.parseBoolean(targetAttentionState));
//            }
//        });
//    }
//
//    private void resetState(TextView view, boolean state) {
//        view.setEnabled(true);
//        if (state) {
//            view.setText("已关注");
//            view.setTextColor(getResources().getColor(R.color.color_c4));
//        } else {
//            view.setText("关 注");
//            view.setTextColor(getResources().getColor(R.color.fontblue));
//        }
//    }


    public ArrayList<AttentionSingleBean> getAttentionUserList() {
        ArrayList<AttentionSingleBean> arrayList = new ArrayList<>();
        if (listSize == 0) return arrayList;
        for (int i = 0; i < listSize; i++) {
            arrayList.add(new AttentionSingleBean(hashMap.get(i) + "", list.get(i).getUser_id()));
        }
        return arrayList;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isFromSetting)
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
