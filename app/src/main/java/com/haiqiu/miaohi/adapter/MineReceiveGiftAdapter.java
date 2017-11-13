package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.activity.VideoDetailActivity;
import com.haiqiu.miaohi.bean.MineReceiveGiftBean;
import com.haiqiu.miaohi.utils.ImageLoadingConfig;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 所受礼物适配器
 * Created by ningl on 2016/6/2.
 */
public class MineReceiveGiftAdapter extends RecyclerView.Adapter<MineReceiveGiftAdapter.ViewHolder> {
    private Context context;
    private List<MineReceiveGiftBean> list;
    DisplayImageOptions ImgOption = ImageLoadingConfig.generateDisplayImageOptionsWithCorner(R.color.color_f1);

    public MineReceiveGiftAdapter(Context context, List<MineReceiveGiftBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MineReceiveGiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MineReceiveGiftAdapter.ViewHolder(View.inflate(context, R.layout.list_item_my_receivegift, null));
    }

    @Override
    public void onBindViewHolder(MineReceiveGiftAdapter.ViewHolder holder, final int position) {
        MineReceiveGiftBean obj = list.get(position);
        ImageLoader.getInstance().displayImage(obj.getSender_portrait_uri(), holder.iholderead);
        holder.tv_name.setText(obj.getSender_name());
        holder.tv_gift_name.setText(obj.getGift_name());
        holder.tv_time.setText(obj.getSend_gitf_time_text());
        ImageLoader.getInstance().displayImage(obj.getGift_icon_uri(), holder.ivGift);
        ImageLoader.getInstance().displayImage(obj.getVideo_cover_uri(), holder.ivVideo, ImgOption);
        if(!MHStringUtils.isEmpty(obj.getSender_type())&&Integer.parseInt(obj.getSender_type())>10){
            holder.ivIsVip.setVisibility(View.VISIBLE);
        } else {
            holder.ivIsVip.setVisibility(View.GONE);
        }

        final String videoId = obj.getVideo_id();
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoDetailActivity.class);
                intent.putExtra("video_id", videoId);
                context.startActivity(intent);
            }
        });
        holder.iholderead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PersonalHomeActivity.class);
                intent.putExtra("userId", list.get(position).getSender_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl_receive_gift);
            tv_name = (TextView) itemView.findViewById(R.id.tv_myattention_item_name);
            iholderead = (ImageView) itemView.findViewById(R.id.iv_myattention_item_head);
            ivVideo = (ImageView) itemView.findViewById(R.id.iv_item_mine_receive_gift_video);
            ivGift = (ImageView) itemView.findViewById(R.id.iv_item_mine_receive_gift);
            tv_time = (TextView) itemView.findViewById(R.id.tv_myattention_item_time);
            tv_gift_name = (TextView) itemView.findViewById(R.id.tv_gift_name);
            ivIsVip = (ImageView) itemView.findViewById(R.id.iv_isvip);
        }

        RelativeLayout rl;
        ImageView iholderead;
        ImageView ivGift;
        ImageView ivIsVip;
        ImageView ivVideo;
        TextView tv_name;
        TextView tv_time;
        TextView tv_gift_name;
    }
}
