package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.DecalInfo;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.widget.OnItemClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * Created by zhandalin on 2016-09-04 20:47.
 * 说明:贴纸adapter
 */
public class DecalAdapter extends RecyclerView.Adapter<DecalAdapter.ViewHolder> {

    private Context context;
    private List<DecalInfo> decalInfos;
    private OnItemClickListener onItemClickListener;
    private int lastSelectedPosition;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions silenceDisplayBuilder;
    private final int padding;

    public DecalAdapter(Context context, List<DecalInfo> decalInfos) {
        this.context = context;
        this.decalInfos = decalInfos;
        decalInfos.get(0).setSelected(true);
        imageLoader = ImageLoader.getInstance();
        padding = DensityUtil.dip2px(context, 5);

        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .showImageForEmptyUri(R.drawable.xuanzewu)
                .showImageOnFail(R.drawable.xuanzewu)
                .showImageOnLoading(R.drawable.xuanzewu)
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                .resetViewBeforeLoading(true);// 设置图片在下载前是否重置，复位
        silenceDisplayBuilder = builder.build();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, R.layout.item_choose_filter, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MHLogUtil.d("kkk","position="+position);
        DecalInfo decalInfo = decalInfos.get(position);
        if (DecalInfo.FROM_LOCAL == decalInfo.getType()) {
            if (position == 0) {
                holder.iv_filter_preview.setPadding(0, 0, 0, 0);
                imageLoader.displayImage("", holder.iv_filter_preview, silenceDisplayBuilder);
            } else {
                holder.iv_filter_preview.setPadding(padding, padding, padding, padding);
                String path = "file://" + context.getFilesDir() + "/" + ConstantsValue.VideoEdit.PASTER_DIR_NAME + "/" + decalInfo.getSticker_uri();
                imageLoader.displayImage(path, holder.iv_filter_preview, silenceDisplayBuilder);
            }
        } else {
            if (position == 0) {
                holder.iv_filter_preview.setPadding(0, 0, 0, 0);
                imageLoader.displayImage("", holder.iv_filter_preview, silenceDisplayBuilder);
            } else {
                holder.iv_filter_preview.setPadding(padding, padding, padding, padding);
                imageLoader.displayImage(decalInfo.getSticker_uri(), holder.iv_filter_preview);
            }
        }

        holder.tv_filter_name.setSelected(decalInfo.isSelected());
        holder.tv_filter_name.setText(decalInfo.getSticker_name());
        if (decalInfo.isSelected()) {
            holder.iv_xuanze.setVisibility(View.VISIBLE);
            holder.iv_white.setVisibility(View.VISIBLE);
            holder.tv_filter_name.setBackgroundResource(0);
        } else {
            holder.iv_xuanze.setVisibility(View.GONE);
            holder.iv_white.setVisibility(View.GONE);
            holder.tv_filter_name.setBackgroundResource(R.color.transparent_80);
        }
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return decalInfos.size();
    }

    public void addData(List<DecalInfo> decalInfos) {
        if (null == decalInfos || decalInfos.size() == 0) return;
        this.decalInfos.addAll(decalInfos);
        notifyDataSetChanged();
    }

    public void clearData() {
        if(null!=decalInfos)
            decalInfos.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_filter_preview;
        public TextView tv_filter_name;
        public View rl_filter_image;
        public View iv_xuanze;
        public View iv_white;
        public int position;

        public ViewHolder(View rootView) {
            super(rootView);
            this.iv_filter_preview = (ImageView) rootView.findViewById(R.id.iv_filter_preview);
            iv_filter_preview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            this.tv_filter_name = (TextView) rootView.findViewById(R.id.tv_filter_name);
            this.rl_filter_image = rootView.findViewById(R.id.rl_filter_image);
            this.iv_xuanze = rootView.findViewById(R.id.iv_xuanze);
            this.iv_white = rootView.findViewById(R.id.iv_white);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    decalInfos.get(lastSelectedPosition).setSelected(false);
                    notifyItemChanged(lastSelectedPosition);
                    lastSelectedPosition = position;
                    decalInfos.get(position).setSelected(true);
                    notifyItemChanged(position);
                    if (null != onItemClickListener)
                        onItemClickListener.OnItemClick(view, position);

                }
            });

        }
    }

    public void setLastSelectedPosition(int lastSelectedPosition) {
        this.lastSelectedPosition = lastSelectedPosition;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
