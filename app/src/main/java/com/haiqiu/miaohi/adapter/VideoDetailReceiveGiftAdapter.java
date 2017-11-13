package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.VideoDetailReceiveGiftBean;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.view.MyCircleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 视频详情获取礼物适配器
 * Created by ningl on 16/9/5.
 */
public class VideoDetailReceiveGiftAdapter extends RecyclerView.Adapter<VideoDetailReceiveGiftAdapter.ViewHolder> {

    private Context context;
    private List<VideoDetailReceiveGiftBean> pageResult;

    public VideoDetailReceiveGiftAdapter(Context context, List<VideoDetailReceiveGiftBean> pageResult) {
        this.context = context;
        this.pageResult = pageResult;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, R.layout.item_videodetailreceivegift, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VideoDetailReceiveGiftBean receiveGiftBean = pageResult.get(position);
        holder.tv_vdreceivegift_name.setText(receiveGiftBean.getSender_name());
        ImageLoader.getInstance().displayImage(receiveGiftBean.getSender_portrait_uri(), holder.iv_vdreceivegift_header, DisplayOptionsUtils.getHeaderDefaultImageOptions());
        ImageLoader.getInstance().displayImage(receiveGiftBean.getGift_icon_uri(), holder.iv_vdreceivegift_gift, DisplayOptionsUtils.getDefaultMinRectImageOptions());
        if(receiveGiftBean.getSender_gender() == 1){
            holder.iv_vdreceivegift_gender.setImageResource(R.drawable.gender_man);
            holder.iv_vdreceivegift_gender.setVisibility(View.VISIBLE);
        } else if(receiveGiftBean.getSender_gender() == 2){
            holder.iv_vdreceivegift_gender.setImageResource(R.drawable.gender_women);
            holder.iv_vdreceivegift_gender.setVisibility(View.VISIBLE);
        } else {
            holder.iv_vdreceivegift_gender.setVisibility(View.GONE);
        }
        if(receiveGiftBean.isSender_question_auth()){
            holder.iv_vdreceivegift_qa.setVisibility(View.VISIBLE);
        } else {
            holder.iv_vdreceivegift_qa.setVisibility(View.GONE);
        }

        if(receiveGiftBean.getSender_type()>10){
            holder.iv_rdreceivegift_vip.setVisibility(View.VISIBLE);
            holder.tv_receivegift_discribe.setVisibility(View.VISIBLE);
            holder.tv_receivegift_discribe.setText(receiveGiftBean.getSender_note());
        } else {
            holder.iv_rdreceivegift_vip.setVisibility(View.GONE);
            holder.tv_receivegift_discribe.setVisibility(View.GONE);
        }

        try {
//            String time = TimeFormatUtils.formatDate(new Date(receiveGiftBean.getSend_gitf_time()));
            holder.iv_vdreceivegift_time.setText(receiveGiftBean.getSend_gift_time_text());
        } catch(Exception e) {
            MHLogUtil.e("VideoDetailReceiveGiftAdapter",e);
            holder.iv_vdreceivegift_time.setText("");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!((BaseActivity)context).isLogin(false)){

                } else {
                    Intent intent = new Intent(context, PersonalHomeActivity.class);
                    intent.putExtra("userId", receiveGiftBean.getSender_id());
                    if (TextUtils.equals(UserInfoUtil.getUserId(context), receiveGiftBean.getSender_id())) {
                        intent.putExtra("isSelf", true);
                    } else {
                        intent.putExtra("isSelf", false);
                    }
                    intent.putExtra("activityType", 0);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pageResult.size();
    }

    public boolean isLogin() {
        return !MHStringUtils.isEmpty(SpUtils.getString(ConstantsValue.Sp.TOKEN_MIAOHI));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            iv_vdreceivegift_header = (MyCircleView) itemView.findViewById(R.id.iv_vdreceivegift_header);
            iv_rdreceivegift_vip = (ImageView) itemView.findViewById(R.id.iv_rdreceivegift_vip);
            tv_vdreceivegift_name = (TextView) itemView.findViewById(R.id.tv_vdreceivegift_name);
            iv_vdreceivegift_gender = (ImageView) itemView.findViewById(R.id.iv_vdreceivegift_gender);
            iv_vdreceivegift_qa = (ImageView) itemView.findViewById(R.id.iv_vdreceivegift_qa);
            iv_vdreceivegift_time = (TextView) itemView.findViewById(R.id.iv_vdreceivegift_time);
            iv_vdreceivegift_gift = (ImageView) itemView.findViewById(R.id.iv_vdreceivegift_gift);
            tv_receivegift_discribe = (TextView) itemView.findViewById(R.id.tv_receivegift_discribe);
        }
        private MyCircleView iv_vdreceivegift_header;
        private ImageView iv_rdreceivegift_vip;
        private TextView tv_vdreceivegift_name;
        private ImageView iv_vdreceivegift_gender;
        private ImageView iv_vdreceivegift_qa;
        private TextView iv_vdreceivegift_time;
        private ImageView iv_vdreceivegift_gift;
        private TextView tv_receivegift_discribe;

    }
}
