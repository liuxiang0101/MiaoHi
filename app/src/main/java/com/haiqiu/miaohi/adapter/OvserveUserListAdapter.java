package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.ObserveUserListObj;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.MHStateSyncUtil;
import com.haiqiu.miaohi.view.MyCircleView;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by LiuXiang on 2016-06-22 21:40.
 * 围观列表adapter
 */
public class OvserveUserListAdapter extends BaseAdapter {
    private BaseActivity baseActivity;
    private List<ObserveUserListObj> dataList;
    private final ImageLoader imageLoader;
    private OnAttentionOnClickListener onAttentionOnClickListener;
    private boolean isObserve;

    public OvserveUserListAdapter(Context context, List<ObserveUserListObj> dataList, boolean isObserve) {
        baseActivity = (BaseActivity) context;
        this.dataList = dataList;
        this.isObserve = isObserve;
        imageLoader = ImageLoader.getInstance();
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
        FansAndAttentionHolder holder;
        if (null == convertView) {
            convertView = View.inflate(baseActivity, R.layout.item_fans_and_attention_layout, null);
            holder = new FansAndAttentionHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (FansAndAttentionHolder) convertView.getTag();
        }
        holder.position = position;
        ObserveUserListObj pageResult = dataList.get(position);
        if (isObserve) {
            imageLoader.displayImage(pageResult.getUser_portrait_uri(), holder.iv_user_head, DisplayOptionsUtils.getHeaderDefaultImageOptions());
            holder.tv_name.setText(pageResult.getNick_name());
            holder.tv_is_free.setVisibility(pageResult.getFree_observer() == 1 ? View.VISIBLE : View.GONE);
        } else {
            imageLoader.displayImage(pageResult.getPortrait_uri(), holder.iv_user_head, DisplayOptionsUtils.getHeaderDefaultImageOptions());
            holder.tv_name.setText(pageResult.getUser_name());
        }

        holder.tv_add_attention.setEnabled(true);
        holder.progress_bar.setVisibility(View.GONE);

        holder.iv_sex.setVisibility(View.VISIBLE);
        holder.iv_sex.setImageResource(pageResult.getUser_gender() == 1 ? R.drawable.gender_man : R.drawable.gender_women);
        holder.iv_is_qa.setVisibility(pageResult.getAnswer_auth() == 1 ? View.VISIBLE : View.GONE);

        //判断该用户是否是自己,是则隐藏关注按钮
        if (!MHStringUtils.isEmpty(SpUtils.getString(ConstantsValue.Sp.USER_ID))) {
            if (SpUtils.getString(ConstantsValue.Sp.USER_ID).equals(pageResult.getUser_id()))
                holder.tv_add_attention.setVisibility(View.INVISIBLE);
            else
                holder.tv_add_attention.setVisibility(View.VISIBLE);
        }

        //判断该用户是否已关注
        MHStateSyncUtil.State syncState = MHStateSyncUtil.getSyncState(pageResult.getUser_id());
        if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != syncState) {
            pageResult.setAttention_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == syncState);
        }
        if (pageResult.isAttention_state()) {
            holder.tv_add_attention.setText("已关注");
            holder.tv_add_attention.setSelected(true);
        } else {
            holder.tv_add_attention.setText("关注");
            holder.tv_add_attention.setSelected(false);
        }
        //判断该用户是否vip
        if (pageResult.getUser_type() > 10) {
            holder.iv_isvip.setVisibility(View.VISIBLE);
        } else {
            holder.iv_isvip.setVisibility(View.INVISIBLE);
        }
        //判断是否有用户头衔
        if (MHStringUtils.isEmpty(pageResult.getUser_authentic())) {
            holder.tv_title.setVisibility(View.GONE);
        } else {
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.tv_title.setText(pageResult.getUser_authentic());
        }

        return convertView;
    }

    public class FansAndAttentionHolder {
        public View progress_bar;
        public View rootView;
        public MyCircleView iv_user_head;
        public ImageView iv_isvip;
        public ImageView iv_sex;
        public ImageView iv_is_qa;
        public TextView tv_name;
        public TextView tv_title;
        public TextView tv_is_free;
        public TextView tv_add_attention;
        public int position;

        public FansAndAttentionHolder(View itemView) {
            this.rootView = itemView;
            this.rootView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(baseActivity, 60)));
            this.iv_user_head = (MyCircleView) rootView.findViewById(R.id.iv_user_head);
            this.iv_isvip = (ImageView) rootView.findViewById(R.id.iv_isvip);
            this.iv_sex = (ImageView) rootView.findViewById(R.id.iv_sex);
            this.iv_is_qa = (ImageView) rootView.findViewById(R.id.iv_is_qa);
            this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
            this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            this.tv_is_free = (TextView) rootView.findViewById(R.id.tv_is_free);
            this.progress_bar = rootView.findViewById(R.id.progress_bar);
            this.tv_add_attention = (TextView) rootView.findViewById(R.id.tv_add_attention);
            tv_add_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!baseActivity.isLogin(false)) return;
                    tv_add_attention.setEnabled(false);
                    tv_add_attention.setText(null);
                    progress_bar.setVisibility(View.VISIBLE);
                    final ObserveUserListObj pageResult = dataList.get(position);

                    final MHRequestParams requestParams = new MHRequestParams();
                    requestParams.addParams("action_mark", !pageResult.isAttention_state() + "");
                    requestParams.addParams("user_id", pageResult.getUser_id());

                    if (pageResult.isAttention_state()) {
                        CommonDialog commonDialog = new CommonDialog(baseActivity);
                        commonDialog.setLeftButtonMsg("取消");
                        commonDialog.setRightButtonMsg("确定");
                        commonDialog.setContentMsg("是否确定取消关注?");
                        commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                            @Override
                            public void onRightButtonOnClick() {
                                MHHttpClient.getInstance().post(BaseResponse.class, baseActivity, ConstantsValue.Url.ATTENTIONDO, requestParams, new MHHttpHandler<BaseResponse>() {
                                    @Override
                                    public void onSuccess(BaseResponse response) {
                                        pageResult.setAttention_state(!pageResult.isAttention_state());
                                        tv_add_attention.setSelected(pageResult.isAttention_state());
                                        resetState(pageResult);
                                        MHStateSyncUtil.pushSyncEvent(baseActivity, pageResult.getUser_id(),pageResult.isAttention_state());

                                        if (null != onAttentionOnClickListener) {
                                            onAttentionOnClickListener.attentionOnClick(pageResult.isAttention_state());
                                        }
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
                        });
                        commonDialog.show();
                        return;
                    }

                    MHHttpClient.getInstance().post(BaseResponse.class, baseActivity, ConstantsValue.Url.ATTENTIONDO, requestParams, new MHHttpHandler<BaseResponse>() {
                        @Override
                        public void onSuccess(BaseResponse response) {
                            pageResult.setAttention_state(!pageResult.isAttention_state());
                            tv_add_attention.setSelected(pageResult.isAttention_state());
                            resetState(pageResult);
                            MHStateSyncUtil.pushSyncEvent(baseActivity, pageResult.getUser_id(),pageResult.isAttention_state());

                            if (null != onAttentionOnClickListener) {
                                onAttentionOnClickListener.attentionOnClick(pageResult.isAttention_state());
                            }
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
            });
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!baseActivity.isLogin(false)) return;
                    Intent intent = new Intent(baseActivity, PersonalHomeActivity.class);
                    intent.putExtra("userId", dataList.get(position).getUser_id());
                    baseActivity.startActivity(intent);
                }
            });
        }

        private void resetState(final ObserveUserListObj pageResult) {
            progress_bar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progress_bar.setVisibility(View.GONE);
                    tv_add_attention.setEnabled(true);
                    if (pageResult.isAttention_state())
                        tv_add_attention.setText("已关注");
                    else
                        tv_add_attention.setText("关注");
                }
            }, 300);

        }
    }


    public void resetData(List<ObserveUserListObj> page_result) {
        if (null == page_result) return;
        dataList = page_result;
        notifyDataSetChanged();
    }

    public void addData(List<ObserveUserListObj> page_result) {
        if (null == page_result || page_result.size() == 0) return;
        if (dataList.size() > 1000) {
            dataList = dataList.subList(980, dataList.size());
        }
        dataList.addAll(page_result);
        notifyDataSetChanged();
    }

    public List<ObserveUserListObj> getDataList() {
        return dataList;
    }

    public interface OnAttentionOnClickListener {
        void attentionOnClick(boolean isAttention);
    }

    public void setOnAttentionOnClickListener(OnAttentionOnClickListener onAttentionOnClickListener) {
        this.onAttentionOnClickListener = onAttentionOnClickListener;
    }
}
