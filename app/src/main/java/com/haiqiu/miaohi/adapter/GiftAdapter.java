package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.base.BaseRecyclerAdapter;
import com.haiqiu.miaohi.bean.GiftResultBean;
import com.haiqiu.miaohi.receiver.RefreshGift;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.ToastUtils;
import com.haiqiu.miaohi.view.SendGiftDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by hackest on 2016/9/1.
 */
public class GiftAdapter extends BaseRecyclerAdapter<GiftResultBean, GiftAdapter.GiftViewHolder> {
    public static final int APP_PAGE_SIZE = 8;//每一页装载数据的大小

    ImageLoader imageLoader = ImageLoader.getInstance();

    /**
     * 每页显示最大条目个数 ,默认是8
     */
    private int mPageSize;
    private int mIndex;
    int layoutPosition = 0;


    public GiftAdapter(List<GiftResultBean> mDatas, Context mContext, int page) {
        super(mDatas, mContext);
        this.mIndex = page;
        mPageSize = APP_PAGE_SIZE;


    }

    //    @Override
    //    public int getItemCount() {
    //        return 8;
    //    }

    /**
     * 先判断数据集的大小是否足够显示满本页？mDatas.size() > (mIndex+1)*mPageSize,
     * 如果够，则直接返回每一页显示的最大条目个数mPageSize,
     * 如果不够，则有几项返回几,(mDatas.size() - mIndex * mPageSize);(说白了 最后一页就显示剩余item)
     */
    @Override
    public int getItemCount() {
        return mDatas.size() > (mIndex + 1) * mPageSize ? mPageSize : (mDatas.size() - mIndex * mPageSize);

    }

    public GiftResultBean getItem(int position) {
        return mDatas.get(position + mIndex * mPageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + mIndex * mPageSize;
    }


    @Override
    public GiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.gift_grid_item_view, parent, false);
        GiftViewHolder vh = new GiftViewHolder(contentView);
        return vh;
    }

    public int getLayoutPosition() {
        return layoutPosition;
    }

    public void setLayoutPosition(int layoutPosition) {
        this.layoutPosition = layoutPosition;
    }

    @Override
    protected void bindItemData(final GiftViewHolder viewHolder, GiftResultBean data, final int position) {
        //        viewHolder.tv_gift_item_name.setText(data.getGift_name());
        //        if (null != mDatas.get(mIndex * 8 + position)) {
        //            viewHolder.tv_gift_item_name.setText(mDatas.get(mIndex * 8 + position).getGift_name());
        //        }

        /**
         * 在给View绑定显示的数据时，计算正确的position = position + mIndex * mPageSize，
         */
        final int pos = position + mIndex * mPageSize;

        viewHolder.tv_gift_item_name.setText(mDatas.get(pos).getGift_name());
        if (data.getGift_name().equals("敬请期待")) {
            viewHolder.tv_gift_item_hicoin.setText("？嗨币");
            viewHolder.tv_gift_item_name.setTextColor(0x80666666);
            viewHolder.tv_gift_item_hicoin.setTextColor(0x808497a2);
            imageLoader.displayImage("drawable://" + R.drawable.icon_no_gift, viewHolder.img_gift_item_thumb);
            viewHolder.iv_donate.setVisibility(View.GONE);
        } else {
            viewHolder.tv_gift_item_name.setTextColor(0xe6666666);
            viewHolder.tv_gift_item_hicoin.setTextColor(0xff8497a2);
            if (mDatas.get(pos).getHi_coin() == 0) {
                viewHolder.tv_gift_item_hicoin.setText("免费");
                viewHolder.tv_gift_item_hicoin.setTextColor(Color.parseColor("#00a0e9"));
                if(data.isGift_has_sent()){
                    viewHolder.iv_donate.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.iv_donate.setVisibility(View.GONE);
                }
            } else {
                viewHolder.tv_gift_item_hicoin.setText(CommonUtil.formatPrice(mDatas.get(pos).getHi_coin()) + "嗨币");
                viewHolder.tv_gift_item_hicoin.setTextColor(Color.parseColor("#fe6262"));
                viewHolder.iv_donate.setVisibility(View.GONE);
            }
            imageLoader.displayImage(mDatas.get(pos).getIcon_uri(), viewHolder.img_gift_item_thumb, DisplayOptionsUtils.getDefaultMinRectImageOptions());


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MHLogUtil.e(SendGiftDialog.selectPos + "");
                    if(mDatas.get(pos).isGift_has_sent()) {
                        ToastUtils.showToastAtCenter(mContext, "免费礼物不能重复赠送嗷～");
                        return;
                    }
                    SendGiftDialog.selectPos = pos;
                    layoutPosition = position;

                    mDatas.get(pos).setSelect(1);
                    notifyDataSetChanged();

                    for (int i = 0; i < mDatas.size(); i++) {
                        mDatas.get(i).setSelect(0);
                    }
                    mDatas.get(pos).setSelect(1);

                    EventBus.getDefault().post(new RefreshGift());

                }
            });

            //更改状态
            if(SendGiftDialog.selectPos == -1){
                viewHolder.layout_select_frame.setVisibility(View.GONE);
                viewHolder.layout_gift_item.setBackgroundColor(0xffffffff);
            } else {
                if (mDatas.get(pos).getSelect() == 1) {
                    viewHolder.layout_select_frame.setVisibility(View.VISIBLE);
                    viewHolder.layout_gift_item.setBackgroundColor(0xfff2fdfd);
                } else {
                    viewHolder.layout_select_frame.setVisibility(View.GONE);
                    viewHolder.layout_gift_item.setBackgroundColor(0xffffffff);
                }
            }


        }


    }


    public static class GiftViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public RelativeLayout layout_select_frame;
        public ImageView img_gift_item_thumb;
        public TextView tv_gift_item_name;
        public TextView tv_gift_item_hicoin;
        public RelativeLayout layout_gift_item;
        public ImageView iv_donate;
        //        int layoutPosition = -1;

        public GiftViewHolder(View view) {
            super(view);
            this.rootView = view;
            this.layout_select_frame = (RelativeLayout) rootView.findViewById(R.id.layout_select_frame);
            this.img_gift_item_thumb = (ImageView) rootView.findViewById(R.id.img_gift_item_thumb);
            this.tv_gift_item_name = (TextView) rootView.findViewById(R.id.tv_gift_item_name);
            this.tv_gift_item_hicoin = (TextView) rootView.findViewById(R.id.tv_gift_item_hicoin);
            this.layout_gift_item = (RelativeLayout) rootView.findViewById(R.id.layout_gift_item);
            this.iv_donate = (ImageView) rootView.findViewById(R.id.iv_donate);

        }


    }

}
