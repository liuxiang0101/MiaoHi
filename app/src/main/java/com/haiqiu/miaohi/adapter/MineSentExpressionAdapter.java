package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.VideoDetailActivity;
import com.haiqiu.miaohi.bean.MineSendFaceBean;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.TimeFormatUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Date;
import java.util.List;

/**
 * 所送表情
 * Created by ningl on 2016/6/2.
 */
public class MineSentExpressionAdapter extends RecyclerView.Adapter<MineSentExpressionAdapter.ViewHolder>{

    private Context context;
    private List list;

    public MineSentExpressionAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, R.layout.list_item_my_sent_expression, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MineSendFaceBean obj = (MineSendFaceBean) list.get(position);
        Date date = TimeFormatUtils.strToDate(obj.getSend_face_time());
        holder.tv_time.setText(MHStringUtils.getTimeCH(TimeFormatUtils.dateToStr(date)));
        ImageLoader.getInstance().displayImage("" + obj.getFace_icon_uri(), holder.ivGift);
        ImageLoader.getInstance().displayImage("" + obj.getVideo_cover_uri(), holder.ivVideo, DisplayOptionsUtils.getCornerImageOptions(DensityUtil.dip2px(context, 10)));

        final String videoId = obj.getVideo_id();
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoDetailActivity.class);
                intent.putExtra("video_id", videoId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
            ll = (LinearLayout) itemView.findViewById(R.id.ll_video_receive_expression);
            tv_time = (TextView) itemView.findViewById(R.id.tv_mine_sent_expression);
            ivGift = (ImageView) itemView.findViewById(R.id.iv_item_mine_sent_expression_gift);
            ivVideo = (ImageView) itemView.findViewById(R.id.iv_item_mine_sent_expression_video);
        }

        LinearLayout ll;
        ImageView ivVideo;
        ImageView ivGift;
        TextView tv_time;
    }

}
