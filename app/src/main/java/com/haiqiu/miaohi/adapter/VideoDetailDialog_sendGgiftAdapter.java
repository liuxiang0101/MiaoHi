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
import com.haiqiu.miaohi.bean.SendGiftBean;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 视频详情礼物对话框列表适配器
 * Created by ningl on 2016/6/24.
 */
public class VideoDetailDialog_sendGgiftAdapter extends RecyclerView.Adapter<VideoDetailDialog_sendGgiftAdapter.ViewHolder> {

    private Context context;
    private List<SendGiftBean> sendGiftBeans;
    private List<Integer> selectList; //记录选中索引
    private OnItemClickListener onItemClickListener;

    public VideoDetailDialog_sendGgiftAdapter(Context context, List<SendGiftBean> sendGiftBeans, List<Integer> selectList) {
        this.context = context;
        this.sendGiftBeans = sendGiftBeans;
        this.selectList = selectList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, R.layout.item_dialog_sendgift, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(sendGiftBeans.get(position).getIcon_uri(), holder.iv_itemvideodetail_dialogsendgiftheader, DisplayOptionsUtils.getSilenceDisplayBuilder());
        holder.tv_itemvideodetail_dialogsendgifttitle.setText(sendGiftBeans.get(position).getGift_name());
//        if(!TextUtils.isEmpty(sendGiftBeans.get(position).getRemain_count())
//                &&!TextUtils.equals("null", sendGiftBeans.get(position).getRemain_count())){
//            if(Integer.parseInt(sendGiftBeans.get(position).getRemain_count())==-1){
//                holder.tv_itemvideodetail_sendgiftcount.setText("无限");
//            } else {
//                holder.tv_itemvideodetail_sendgiftcount.setText(sendGiftBeans.get(position).getRemain_count());
//            }
//        }
        holder.tv_itemvideodetail_sendgiftcount.setVisibility(View.GONE);

        if(selectList.get(position) == 1) {
            holder.rl_videodetail_dialogsendgiftselect.setBackgroundResource(R.drawable.shape_videodetail_dialog_giftselect);
            int padding = DensityUtil.dip2px(context, 1);
            holder.rl_videodetail_dialogsendgiftselect.setPadding(padding, padding, padding, padding);
        } else {
            holder.rl_videodetail_dialogsendgiftselect.setBackgroundColor(Color.parseColor("#fafafa"));
            holder.rl_videodetail_dialogsendgiftselect.setPadding(0, 0, 0, 0);
        }
        if(position == 0){
            holder.itemView.setPadding(DensityUtil.dip2px(context, 15), 0, 0 , 0);
        } else {
            holder.itemView.setPadding(DensityUtil.dip2px(context, 10), 0, 0 , 0);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
            iv_itemvideodetail_dialogsendgiftheader = (ImageView) itemView.findViewById(R.id.iv_itemvideodetail_dialogsendgiftheader);
            tv_itemvideodetail_dialogsendgifttitle = (TextView) itemView.findViewById(R.id.tv_itemvideodetail_dialogsendgifttitle);
            tv_itemvideodetail_sendgiftcount = (TextView) itemView.findViewById(R.id.tv_itemvideodetail_sendgiftcount);
            rl_videodetail_dialogsendgiftselect = (RelativeLayout) itemView.findViewById(R.id.rl_videodetail_dialogsendgiftselect);
        }

        ImageView iv_itemvideodetail_dialogsendgiftheader;
        TextView tv_itemvideodetail_dialogsendgifttitle;
        TextView tv_itemvideodetail_sendgiftcount;
        RelativeLayout rl_videodetail_dialogsendgiftselect;
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
