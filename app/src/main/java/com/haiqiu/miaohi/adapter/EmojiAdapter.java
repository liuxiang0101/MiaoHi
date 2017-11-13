package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.view.EmojiDialog;

import java.util.List;

/**
 * emoji适配器
 * Created by ningl on 2016/7/5.
 */
public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder>{

    private Context context;
    private List<Integer> ls;
    private EmojiDialog.OnEmojiClickListener onEmojiClickListener;

    public EmojiAdapter(Context context, List<Integer> ls, EmojiDialog.OnEmojiClickListener onEmojiClickListener) {
        this.context = context;
        this.ls = ls;
        this.onEmojiClickListener = onEmojiClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, R.layout.item_emoji, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(ls.get(position)==0) return;
        else if(ls.get(position)==-1) holder.iv.setImageResource(R.drawable.svg_emoji_delete);
        else holder.tv.setText(getEmojiStringByUnicode(ls.get(position)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ls.get(position)!=0){
                    onEmojiClickListener.onEmojiClick(ls.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_emoji);
            iv = (ImageView) itemView.findViewById(R.id.iv_emoji);
        }
        TextView tv;
        ImageView iv;
    }

    private String getEmojiStringByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }
}
