package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.VideoDetail_dialogGiftBean;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 视频详情礼物对话框列表适配器
 * Created by ningl on 2016/6/24.
 */
public class VideoDetailDialog_giftAdapter extends RecyclerView.Adapter<VideoDetailDialog_giftAdapter.ViewHolder> {

    private Context context;
    private List<VideoDetail_dialogGiftBean> videoDetail_dialogGiftBeans;
    private List<Integer> selectList; //记录选中索引
    private OnItemClickListener onItemClickListener;

    public VideoDetailDialog_giftAdapter(Context context, List<VideoDetail_dialogGiftBean> videoDetail_dialogGiftBeans, List<Integer> selectList) {
        this.context = context;
        this.videoDetail_dialogGiftBeans = videoDetail_dialogGiftBeans;
        this.selectList = selectList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, R.layout.item_videodetail_giftdialog, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(videoDetail_dialogGiftBeans.get(position).getGift_icon_uri(), holder.iv_itemvideodetail_dialogheader, DisplayOptionsUtils.getSilenceDisplayBuilder());
        holder.tv_itemvideodetail_dialogname.setText(videoDetail_dialogGiftBeans.get(position).getSender_name()+"送");
        holder.tv_itemvideodetail_dialogtitle.setText(videoDetail_dialogGiftBeans.get(position).getGift_name());
        if(selectList.get(position) == 1) {
            holder.rl_videodetail_dialogselect.setBackgroundResource(R.drawable.shape_videodetail_dialog_giftselect);
            int padding = DensityUtil.dip2px(context, 1);
            holder.rl_videodetail_dialogselect.setPadding(padding, padding, padding, padding);
        } else {
            holder.rl_videodetail_dialogselect.setBackgroundColor(Color.parseColor("#fafafa"));
            holder.rl_videodetail_dialogselect.setPadding(0, 0, 0, 0);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        if(position == 0){
            holder.itemView.setPadding(DensityUtil.dip2px(context, 15), 0, 0 , 0);
        } else {
            holder.itemView.setPadding(DensityUtil.dip2px(context, 10), 0, 0 , 0);
        }
    }

    @Override
    public int getItemCount() {
        return videoDetail_dialogGiftBeans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
            iv_itemvideodetail_dialogheader = (ImageView) itemView.findViewById(R.id.iv_itemvideodetail_dialogheader);
            tv_itemvideodetail_dialogtitle = (TextView) itemView.findViewById(R.id.tv_itemvideodetail_dialogtitle);
            tv_itemvideodetail_dialogname = (TextView) itemView.findViewById(R.id.tv_itemvideodetail_dialogname);
            rl_videodetail_dialogselect = (RelativeLayout) itemView.findViewById(R.id.rl_videodetail_dialogselect);
        }

        ImageView iv_itemvideodetail_dialogheader;
        TextView tv_itemvideodetail_dialogtitle;
        TextView tv_itemvideodetail_dialogname;
        RelativeLayout rl_videodetail_dialogselect;
    }

    /**
     * item点击设置监听
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * item点击回调接口
     */
    public interface OnItemClickListener{
        public void onItemClick(int position);
    }
}
