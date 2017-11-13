package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.MaybeInterestToPeopleActivity;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.DiscoveryUserObj;
import com.haiqiu.miaohi.fragment.LoginDialogFragment;
import com.haiqiu.miaohi.utils.ChangeAttentionStateUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHStateSyncUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.callback.ChangeAttentionStateCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;

import java.util.List;

/**
 * Created by LiuXiang on 2016/12/22.
 * 发现广场--横式卡片适配器
 */
public class FoundSquareRvAdapter extends RecyclerView.Adapter<FoundSquareRvAdapter.MyRvViewHolder> {
    private Context context;
    private List<DiscoveryUserObj> list;
    private ImageLoader imageLoader;

    public FoundSquareRvAdapter(List<DiscoveryUserObj> list, Context context) {
        this.list = list;
        this.context = context;
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public MyRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_interested_people_card, null);
        MyRvViewHolder holder = new MyRvViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyRvViewHolder holder, final int position) {
        if (position < list.size()) {
            final DiscoveryUserObj obj = list.get(position);
            imageLoader.displayImage(obj.getPortrait_uri(), holder.iv_head, DisplayOptionsUtils.getHeaderDefaultImageOptions());
            holder.tv_name.setText(obj.getUser_name());
            holder.tv_title.setText(obj.getUser_note());
            holder.iv_vip_mark.setVisibility(obj.getUser_type() > 10 ? View.VISIBLE : View.GONE);

            MHStateSyncUtil.State syncState = MHStateSyncUtil.getSyncState(obj.getUser_id());
            if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != syncState) {
                obj.setAttention_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == syncState);
            }

            if (obj.isAttention_state()) {
                holder.tv_addention.setText("已关注");
                holder.tv_addention.setTextColor(context.getResources().getColor(R.color.color_c4));
                holder.tv_addention.setBackgroundResource(R.drawable.tag_bg);
            } else {
                holder.tv_addention.setText("关注");
                holder.tv_addention.setTextColor(context.getResources().getColor(R.color.fontblue));
                holder.tv_addention.setBackgroundResource(R.drawable.shape_attention_blue_selector);
            }
            //点击关注按钮
            holder.tv_addention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ChangeAttentionStateUtil((BaseActivity) context).changeAttentionState(obj.getUser_id(), !obj.isAttention_state(), v, holder.progress_bar, new ChangeAttentionStateCallBack() {
                        @Override
                        public void callBackInfo(boolean attentionState) {
                            obj.setAttention_state(attentionState);
                        }
                    });
                    TCAgent.onEvent(context, "发现推人关注" + ConstantsValue.android);
                }
            });
            //点击整张卡片时
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MHStringUtils.isEmpty(SpUtils.getString(ConstantsValue.Sp.TOKEN_MIAOHI))) {
                        LoginDialogFragment loginDialog = new LoginDialogFragment();
                        loginDialog.show(((BaseActivity) context).getSupportFragmentManager(), "loginDialog");
                    } else {
                        Intent intent = new Intent(context, PersonalHomeActivity.class);
                        intent.putExtra("userId", list.get(position).getUser_id());
                        context.startActivity(intent);
                    }
                    TCAgent.onEvent(context, "发现推人头像" + ConstantsValue.android);
                }
            });
            holder.iv_head.setVisibility(View.VISIBLE);
            holder.tv_name.setVisibility(View.VISIBLE);
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.tv_addention.setVisibility(View.VISIBLE);
            holder.ll.setVisibility(View.GONE);
            holder.view_interval.setVisibility(View.GONE);
        } else {//如果是最后一张卡片
            holder.iv_vip_mark.setVisibility(View.GONE);
            holder.iv_head.setVisibility(View.GONE);
            holder.tv_name.setVisibility(View.GONE);
            holder.tv_title.setVisibility(View.GONE);
            holder.tv_addention.setVisibility(View.GONE);
            holder.ll.setVisibility(View.VISIBLE);
            holder.view_interval.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MaybeInterestToPeopleActivity.class);
                    intent.putExtra("isFromDiscover", true);
                    context.startActivity(intent);
                    TCAgent.onEvent(context, "发现推人发现更多" + ConstantsValue.android);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    class MyRvViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_head;
        private ImageView iv_vip_mark;
        private TextView tv_name;
        private TextView tv_title;
        private TextView tv_addention;
        private LinearLayout ll;
        private View view_interval;
        private View progress_bar;

        public MyRvViewHolder(View itemView) {
            super(itemView);
            iv_head = (ImageView) itemView.findViewById(R.id.iv_head);
            iv_vip_mark = (ImageView) itemView.findViewById(R.id.iv_vip_mark);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_addention = (TextView) itemView.findViewById(R.id.tv_addention);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            view_interval = itemView.findViewById(R.id.view_interval);
            progress_bar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
