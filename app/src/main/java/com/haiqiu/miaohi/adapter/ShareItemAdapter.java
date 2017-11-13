package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.SharedItemBean;
import com.haiqiu.miaohi.utils.NoDoubleClickUtils;

import java.util.List;

/**
 * 分享适配器
 * Created by ningl on 2016/6/25.
 */
public class ShareItemAdapter extends RecyclerView.Adapter<ShareItemAdapter.ViewHolder> {

    private List<SharedItemBean> sharedItemBeans;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ShareItemAdapter(List<SharedItemBean> sharedItemBeans, Context context) {
        this.sharedItemBeans = sharedItemBeans;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, R.layout.shareditem, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.iv_itemshare.setImageResource(sharedItemBeans.get(position).getDrawable());
        holder.tv_itemshare.setText(sharedItemBeans.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null&&!NoDoubleClickUtils.isDoubleClick()) onItemClickListener.callback(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sharedItemBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
            iv_itemshare = (ImageView) itemView.findViewById(R.id.iv_itemshare);
            tv_itemshare = (TextView) itemView.findViewById(R.id.tv_itemshare);
        }

        ImageView iv_itemshare;
        TextView tv_itemshare;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void callback(int position);
    }

}
