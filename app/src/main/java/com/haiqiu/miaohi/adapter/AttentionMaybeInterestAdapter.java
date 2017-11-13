package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.MaybeInterestToPeopleActivity;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.bean.MayBeInterest;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.response.HomeFoundResponse;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHStateSyncUtil;
import com.haiqiu.miaohi.view.MyCircleView;
import com.haiqiu.miaohi.widget.CommonDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注列表中可能感兴趣的人适配器
 * Created by ningl on 16/12/16.
 */
public class AttentionMaybeInterestAdapter extends RecyclerView.Adapter<AttentionMaybeInterestAdapter.ViewHolder> {

    private Context context;
    private List<MayBeInterest> data;
    private List<MayBeInterest> totalData;
    private OnRemoveAllInterestPeople onRemoveAllInterestPeople;

    public AttentionMaybeInterestAdapter(Context context, List<MayBeInterest> myData) {
        this.context = context;
        this.data = new ArrayList<>();
        this.totalData = myData;
        if (data != null) {
//            if (totalData.size() > 5) {
//                for (int i = 0; i < 5; i++) {
//                    this.data.add(totalData.get(0));
//                    totalData.remove(0);
//                }
//            } else {
//                this.data.addAll(totalData);
//            }
            if(!totalData.isEmpty()&&data.size()<5){
                int size = 5 - data.size();
                for (int i = 0; i < size; i++) {
                    if(!totalData.isEmpty()){
                        data.add(totalData.get(0));
                        totalData.remove(0);
                    }
                }
            }
            MayBeInterest mayBeInterest = new MayBeInterest();
            mayBeInterest.setFindMore(true);
            this.data.add(mayBeInterest);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, R.layout.item_attention_maybeinterestchild, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MayBeInterest mayBeInterest = data.get(position);
        if (!mayBeInterest.isFindMore()) {//非发现更多item
            holder.rl_attentionnolast.setVisibility(View.VISIBLE);
            holder.rl_attentionlast.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(mayBeInterest.getPortrait_uri(), holder.mcv_attention_childhead, DisplayOptionsUtils.getHeaderDefaultImageOptions());
            holder.tv_attention_childname.setText(mayBeInterest.getUsername());
            holder.iv_attention_childvip.setVisibility(mayBeInterest.getUser_type() > 10 ? View.VISIBLE : View.GONE);
            holder.tv_attention_childdescribe.setText(mayBeInterest.getUser_note());
        } else {//发现更多item
            holder.rl_attentionnolast.setVisibility(View.GONE);
            holder.rl_attentionlast.setVisibility(View.VISIBLE);
        }
        MHStateSyncUtil.State state = MHStateSyncUtil.getSyncState(mayBeInterest.getUser_id());
        if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != state) {
            mayBeInterest.setAttention_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == state);
        }
        if (mayBeInterest.isAttention_state()) {
            holder.tv_attention_childattention.setText("已关注");
            holder.tv_attention_childattention.setTextColor(Color.parseColor("#c4c4c4"));
        } else {
            holder.tv_attention_childattention.setText("关注");
            holder.tv_attention_childattention.setTextColor(Color.parseColor("#00a2ff"));
        }
        holder.tv_attention_childattention.setSelected(mayBeInterest.isAttention_state());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mayBeInterest.isFindMore()) {//发现更多item
                    context.startActivity(new Intent(context, MaybeInterestToPeopleActivity.class));
                    TCAgent.onEvent(context,"关注推人发现更多"+ConstantsValue.android);
                }
            }
        });
        holder.iv_attentionrecommendclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TCAgent.onEvent(context,"关注推人关闭"+ConstantsValue.android);
                unLikePeople(mayBeInterest.getUser_id());
                notifyDataSetChanged();
                //移除总集合中最前面的数据 添加到当前集合删除的位置
                data.remove(position);
                if (!totalData.isEmpty()) {
                    data.add(position, totalData.get(0));
                    totalData.remove(0);
                }
                if (totalData.isEmpty() && data.size() == 1) {
                    if (onRemoveAllInterestPeople != null) onRemoveAllInterestPeople.onRemove();
                }
            }
        });
        holder.mcv_attention_childhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PersonalHomeActivity.class)
                        .putExtra("userId", mayBeInterest.getUser_id()));
                TCAgent.onEvent(context,"关注推人头像"+ConstantsValue.android);
            }
        });
        holder.tv_attention_childattention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!mayBeInterest.isAttention_state()) {
                    attentionOrCancle((TextView) v, mayBeInterest, position);
                    TCAgent.onEvent(context,"关注推人关注"+ConstantsValue.android);
                    return;
                }
                final CommonDialog commonDialog = new CommonDialog(context);
                commonDialog.setContentMsg("确定要取消关注吗？");
                commonDialog.setRightButtonMsg("确定");
                commonDialog.setLeftButtonMsg("取消");
                commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
                    @Override
                    public void onLeftButtonOnClick() {
                        commonDialog.dismiss();
                    }
                });
                commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                    @Override
                    public void onRightButtonOnClick() {
                        attentionOrCancle((TextView) v, mayBeInterest, position);
                        commonDialog.dismiss();
                    }
                });
                commonDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MyCircleView mcv_attention_childhead;
        private TextView tv_attention_childname;
        private TextView tv_attention_childdescribe;
        private TextView tv_attention_childattention;
        private ImageView iv_attention_childdelete;
        private ImageView iv_attention_childvip;
        private RelativeLayout rl_attentionnolast;
        private RelativeLayout rl_attentionlast;
        private ImageView iv_attentionrecommendclose;

        public ViewHolder(View itemView) {
            super(itemView);
            mcv_attention_childhead = (MyCircleView) itemView.findViewById(R.id.mcv_attention_childhead);
            tv_attention_childname = (TextView) itemView.findViewById(R.id.tv_attention_childname);
            tv_attention_childdescribe = (TextView) itemView.findViewById(R.id.tv_attention_childdescribe);
            tv_attention_childattention = (TextView) itemView.findViewById(R.id.tv_attention_childattention);
            iv_attention_childdelete = (ImageView) itemView.findViewById(R.id.iv_attention_childdelete);
            iv_attention_childvip = (ImageView) itemView.findViewById(R.id.iv_attention_childvip);
            rl_attentionlast = (RelativeLayout) itemView.findViewById(R.id.rl_attentionlast);
            rl_attentionnolast = (RelativeLayout) itemView.findViewById(R.id.rl_attentionnolast);
            iv_attentionrecommendclose = (ImageView) itemView.findViewById(R.id.iv_attentionrecommendclose);
        }
    }

    /**
     * 删除推荐的人(不感兴趣的人)
     *
     * @param user_id 用户id
     */
    private void unLikePeople(String user_id) {
        MHRequestParams params = new MHRequestParams();
        params.addParams("object_id", user_id);
        params.addParams("object_type", "1");
        MHHttpClient.getInstance().post(BaseResponse.class, ConstantsValue.Url.UNINTERESTOBJECT, params, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {

            }

            @Override
            public void onFailure(String content) {

            }
        });
    }


    public interface OnRemoveAllInterestPeople {
        void onRemove();
    }

    public void setOnRemoveAllInterestPeopleListener(OnRemoveAllInterestPeople onRemoveAllInterestPeople) {
        this.onRemoveAllInterestPeople = onRemoveAllInterestPeople;
    }

    /**
     * 关注或取消关注
     *
     * @param attentionView
     * @param mayBeInterest
     */
    public void attentionOrCancle(final TextView attentionView, final MayBeInterest mayBeInterest, final int position) {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("action_mark", !mayBeInterest.isAttention_state() + "");
        requestParams.addParams("user_id", mayBeInterest.getUser_id());
        MHHttpClient.getInstance().post(HomeFoundResponse.class, context, ConstantsValue.Url.ATTENTIONDO, requestParams, new MHHttpHandler<HomeFoundResponse>() {
            @Override
            public void onSuccess(HomeFoundResponse response) {
                mayBeInterest.setAttention_state(!mayBeInterest.isAttention_state());
                attentionView.setSelected(mayBeInterest.isAttention_state());
                if (mayBeInterest.isAttention_state()) {
                    attentionView.setText("已关注");
                    attentionView.setTextColor(Color.parseColor("#c4c4c4"));

                    //FIXME 处理关注删除感兴趣的人列表中的卡片
//                    notifyDataSetChanged();
//                    data.remove(position);
//                    if (!totalData.isEmpty()) {
//                        data.add(position, totalData.get(0));
//                        totalData.remove(0);
//                    }
//                    if (totalData.isEmpty() && data.size() == 1) {
//                        if (onRemoveAllInterestPeople != null) onRemoveAllInterestPeople.onRemove();
//                    }
                } else {
                    attentionView.setText("关注");
                    attentionView.setTextColor(Color.parseColor("#00a2ff"));

                }
                MHStateSyncUtil.pushSyncEvent(context, mayBeInterest.getUser_id(), mayBeInterest.isAttention_state());
            }

            @Override
            public void onFailure(String content) {
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
            }
        });
    }
}
