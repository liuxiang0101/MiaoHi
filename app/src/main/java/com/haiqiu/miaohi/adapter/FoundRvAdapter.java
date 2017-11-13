package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.VideoAndImgActivity;
import com.haiqiu.miaohi.base.BaseRecyclerAdapter;
import com.haiqiu.miaohi.bean.DiscoveryObjectObj;
import com.haiqiu.miaohi.bean.VideoAndImg;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuXiang on 2017/1/10.
 */

public class FoundRvAdapter extends BaseRecyclerAdapter {
    private ImageLoader imageLoader;
    private ArrayList<VideoAndImg> listData;

    public FoundRvAdapter(List mDatas, Context mContext) {
        super(mDatas, mContext);
        imageLoader = ImageLoader.getInstance();
        listData = new ArrayList<>();
    }

    @Override
    protected void bindItemData(RecyclerView.ViewHolder viewHolder, final Object data, final int position) {
        RvViewHolder vh = (RvViewHolder) viewHolder;
        final DiscoveryObjectObj object = (DiscoveryObjectObj) data;
        if (object == null) return;
        vh.view_right.setVisibility((position + 1) % 3 == 0 ? View.GONE : View.VISIBLE);
        vh.iv_video_mark.setVisibility(object.getObject_type() == 1 ? View.VISIBLE : View.INVISIBLE);
        imageLoader.displayImage(object.getObject_img_uri(), vh.imageView);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoAndImgActivity.class);
                intent.putParcelableArrayListExtra("data", listData)
                        .putExtra("currentIndex", position)
                        .putExtra("userId", object.getObject_id())
                        .putExtra("pageIndex", position)
                        .putExtra("command", ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_recommend_sports, null);
        RvViewHolder vh = new RvViewHolder(view);
        return vh;
    }

    class RvViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView iv_video_mark;
        View view_right;

        public RvViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            iv_video_mark = (ImageView) itemView.findViewById(R.id.iv_video_mark);
            view_right = itemView.findViewById(R.id.view_right);
        }
    }

    public void setVideoList(List<DiscoveryObjectObj> list) {
        listData.clear();
        for (DiscoveryObjectObj object : list) {
            VideoAndImg obj = new VideoAndImg();
            obj.setElement_type(object.getObject_type());
            obj.setContent_type(object.getObject_type());
            obj.setPhoto_id(object.getObject_id());
            obj.setVideo_id(object.getObject_id());
            listData.add(obj);
        }
    }
}
