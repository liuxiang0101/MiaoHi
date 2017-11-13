package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.activity.UserListActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.UserData;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHStateSyncUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.view.MyCircleView;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.haiqiu.miaohi.widget.stickyheaderListView.StickyListHeadersAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by zhandalin on 2016-12-22 09:47.
 * 说明:A-Z的用户列表
 */
public class UserStickyAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private final static String TAG = "UserStickyAdapter";
    private final ImageLoader imageLoader;
    private final DisplayImageOptions headerDefaultImageOptions;
    private final String userId;
    private final int contentHeight;
    private BaseActivity baseActivity;
    private List<UserData> dataList;
    private boolean isEnableSelected;
    private final int headerHeight;


    public UserStickyAdapter(Context baseActivity, List<UserData> dataList, boolean isEnableSelected) {
        this.baseActivity = (BaseActivity) baseActivity;
        this.dataList = dataList;
        this.isEnableSelected = isEnableSelected;
        imageLoader = ImageLoader.getInstance();
        headerDefaultImageOptions = DisplayOptionsUtils.getHeaderDefaultImageOptions();
        headerHeight = DensityUtil.dip2px(baseActivity, 20);
        contentHeight = DensityUtil.dip2px(baseActivity, 70);

        userId = UserInfoUtil.getUserId(baseActivity);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            holder.text = (TextView) View.inflate(baseActivity, R.layout.list_group_item, null);
            holder.text.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, headerHeight));
            convertView = holder.text;
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        holder.text.setText(dataList.get(position).user_name_head_char);

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        if (null != dataList.get(position) && null != dataList.get(position).user_name_head_char
                && dataList.get(position).user_name_head_char.length() > 0)
            return dataList.get(position).user_name_head_char.charAt(0);
        return 0;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = View.inflate(baseActivity, R.layout.item_user_list, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        UserData userData = dataList.get(position);
        holder.position = position;
        holder.tv_add_attention.setEnabled(true);
        holder.progress_bar.setVisibility(View.GONE);

        MHStateSyncUtil.State state = MHStateSyncUtil.getSyncState(userData.user_id);
        if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != state) {
            userData.setAttention_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == state);
        }

        //判断该用户是否已关注
        if (userData.isAttention_state()) {
            holder.tv_add_attention.setText("已关注");
            holder.tv_add_attention.setSelected(true);
        } else {
            holder.tv_add_attention.setText("关注");
            holder.tv_add_attention.setSelected(false);
        }

        //判断该用户是否vip
        if (userData.user_type > 10) {
            holder.iv_isvip.setVisibility(View.VISIBLE);
        } else
            holder.iv_isvip.setVisibility(View.INVISIBLE);


        holder.tv_name.setText(userData.user_name);
        imageLoader.displayImage(userData.portrait_uri, holder.iv_user_head, headerDefaultImageOptions);
        if (userData.user_gender == 1) {
            holder.iv_sex.setImageResource(R.drawable.gender_man);
        } else {
            holder.iv_sex.setImageResource(R.drawable.gender_women);
        }
        if (MHStringUtils.isEmpty(userData.user_authentic)) {
            holder.tv_user_authentic.setVisibility(View.GONE);
        } else {
            holder.tv_user_authentic.setText(userData.user_authentic);
            holder.tv_user_authentic.setVisibility(View.VISIBLE);
        }
        if (userData.answer_auth == 1) {
            holder.iv_answer_auth.setVisibility(View.VISIBLE);
        } else {
            holder.iv_answer_auth.setVisibility(View.GONE);
        }

        if (UserListActivity.nameUserIdHashMap.containsKey(userData.user_id)) {
            holder.check_box.setChecked(true);
        } else {
            holder.check_box.setChecked(false);
        }

        if (isEnableSelected) {
            holder.tv_add_attention.setVisibility(View.GONE);
            holder.check_box.setVisibility(View.VISIBLE);
            holder.rl_container.setClickable(false);
            //判断该用户是否是自己,是则隐藏选择按钮
            if (null != userId && userId.equals(userData.user_id)) {
                holder.check_box.setVisibility(View.GONE);
            }
        } else {
            holder.tv_add_attention.setVisibility(View.VISIBLE);
            holder.check_box.setVisibility(View.GONE);
            holder.rl_container.setClickable(true);
            //判断该用户是否是自己,是则隐藏关注
            if (null != userId && userId.equals(userData.user_id)) {
                holder.tv_add_attention.setVisibility(View.GONE);
                holder.progress_bar.setVisibility(View.GONE);
            }
        }

        return view;
    }


    public class ViewHolder {
        public View rootView;
        //        public TextView tv_group_title;
        public MyCircleView iv_user_head;
        public ImageView iv_isvip;
        public TextView tv_name;
        public ImageView iv_sex;
        public ImageView iv_answer_auth;
        public TextView tv_user_authentic;
        public TextView tv_add_attention;
        public ProgressBar progress_bar;
        public int position;
        private CheckBox check_box;
        private View rl_container;


        public ViewHolder(View rootView) {
            this.rootView = rootView;
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, contentHeight));

            this.check_box = (CheckBox) rootView.findViewById(R.id.check_box);
            this.rl_container = rootView.findViewById(R.id.rl_container);

            this.iv_user_head = (MyCircleView) rootView.findViewById(R.id.iv_user_head);
            this.iv_isvip = (ImageView) rootView.findViewById(R.id.iv_isvip);
            this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
            this.iv_sex = (ImageView) rootView.findViewById(R.id.iv_sex);
            this.iv_answer_auth = (ImageView) rootView.findViewById(R.id.iv_answer_auth);
            this.tv_user_authentic = (TextView) rootView.findViewById(R.id.tv_user_authentic);
            this.tv_add_attention = (TextView) rootView.findViewById(R.id.tv_add_attention);
            this.progress_bar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

            tv_add_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!baseActivity.isLogin(false)) return;
                    final UserData pageResult = dataList.get(position);
                    if (pageResult.isAttention_state()) {//取消关注,要弹对话框
                        CommonDialog commonDialog = new CommonDialog(baseActivity);
                        commonDialog.setContentMsg("确定要取消关注吗?");
                        commonDialog.setLeftButtonMsg("取消");
                        commonDialog.setRightButtonMsg("确认");
                        commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                            @Override
                            public void onRightButtonOnClick() {
                                doAttentionAction(pageResult);
                            }
                        });
                        commonDialog.show();
                    } else {
                        doAttentionAction(pageResult);
                    }
                }
            });
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEnableSelected) {
                        UserData userData = dataList.get(position);
                        userData.isSelected = !userData.isSelected;
                        if (userData.isSelected) {
                            UserListActivity.nameUserIdHashMap.put(userData.user_id, userData.user_name);
                        } else {
                            UserListActivity.nameUserIdHashMap.remove(userData.user_id);
                        }
                        check_box.setChecked(userData.isSelected);
                    } else {
                        if (!baseActivity.isLogin(false)) return;
                        Intent intent = new Intent(baseActivity, PersonalHomeActivity.class);
                        intent.putExtra("userId", dataList.get(position).user_id);
                        baseActivity.startActivity(intent);
                    }
                }
            });
        }


        private void doAttentionAction(final UserData pageResult) {
            tv_add_attention.setEnabled(false);
            tv_add_attention.setText(null);
            progress_bar.setVisibility(View.VISIBLE);
            MHRequestParams requestParams = new MHRequestParams();
            requestParams.addParams("action_mark", !pageResult.isAttention_state() + "");
            requestParams.addParams("user_id", pageResult.user_id);
            MHHttpClient.getInstance().post(BaseResponse.class, baseActivity, ConstantsValue.Url.ATTENTIONDO, requestParams, new MHHttpHandler<BaseResponse>() {
                @Override
                public void onSuccess(BaseResponse response) {
                    pageResult.setAttention_state(!pageResult.isAttention_state());
                    tv_add_attention.setSelected(pageResult.isAttention_state());
                    resetState(pageResult);
                    MHStateSyncUtil.pushSyncEvent(baseActivity, pageResult.user_id, pageResult.isAttention_state());
                }

                @Override
                public void onFailure(String content) {
                    resetState(pageResult);
                }

                @Override
                public void onStatusIsError(String message) {
                    super.onStatusIsError(message);
                    resetState(pageResult);
                }
            });
        }

        private void resetState(final UserData userData) {
            progress_bar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progress_bar.setVisibility(View.GONE);
                    tv_add_attention.setEnabled(true);
                    if (userData.isAttention_state())
                        tv_add_attention.setText("已关注");
                    else
                        tv_add_attention.setText("关注");
                }
            }, 300);
        }
    }


    public void addData(List<UserData> mList, AddDataType addDataType) {
        if (null == mList || mList.size() == 0) return;

        switch (addDataType) {
            case DEFAULT:
                this.dataList.addAll(mList);
                break;
            case FIRST:
                this.dataList.addAll(0, mList);
                break;
            case CLEAR:
                this.dataList.clear();
                this.dataList.addAll(mList);
                break;
        }
    }

    private class HeaderViewHolder {
        TextView text;
    }

    public enum AddDataType {
        DEFAULT, FIRST, CLEAR
    }
}
