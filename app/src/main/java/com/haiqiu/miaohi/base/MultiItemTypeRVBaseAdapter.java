package com.haiqiu.miaohi.base;

import android.content.Context;

import java.util.List;

/**
 * Created by Mikee
 * 支持多种ItemType的Adapter（适用于RecyclerView）
 */
public abstract class MultiItemTypeRVBaseAdapter<T> extends RVBaseAdapter<T> {
    public MultiItemTypeRVBaseAdapter(Context context, List<T> beans) {
        super(context, beans);
    }

    @Override
    public abstract int getItemViewType(int position);


    /**
     *demo
     */
/*    public class BookAdapter extends SolidRVBaseAdapter<BookBean> {
     public BookAdapter(Context context, List<BookBean> beans) {
     super(context, beans);
     }
     @Override
     public int getItemLayoutID(int vieWType) {
     return R.layout.item_book;
     }
     @Override
     protected void onItemClick(int position) {
     Intent intent = new Intent(mContext, BookDetailActivity.class);
     intent.putExtra("url", mBeans.get(position - 1).getUrl());
     mContext.startActivity(intent);
     }
     @Override
     protected void onBindDataToView(SolidCommonViewHolder holder, BookBean bean) {
     holder.setText(R.id.tv_title, bean.getTitle());
     holder.setText(R.id.tv_price, "￥" + bean.getPrice());
     holder.setText(R.id.tv_author, "作者:" + bean.getAuthor() + "");
     holder.setText(R.id.tv_date, "出版日期:" + bean.getPubdate());
     holder.setText(R.id.tv_publisher, "出版社:" + bean.getPublisher());
     holder.setText(R.id.tv_num_rating, bean.getRating().getNumRaters() + "人评分");
     holder.setImageFromInternet(R.id.iv_image, bean.getImage());
     }*/
}


