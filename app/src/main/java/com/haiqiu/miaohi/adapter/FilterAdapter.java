package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.FilterInfo;
import com.haiqiu.miaohi.utils.BitmapUtil;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.widget.OnItemClickListener;

import org.wysaid.nativePort.CGENativeLibrary;

import java.util.List;

/**
 * Created by zhandalin on 2016-09-04 20:47.
 * 说明:
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private Context context;
    private List<FilterInfo> filterInfoList;
    private OnItemClickListener onItemClickListener;
    private int lastSelectedPosition;
    private boolean isFiltering;
    private Bitmap logoSrcBitmap;

    public FilterAdapter(Context context, List<FilterInfo> filterInfoList, String first_frame_image, int rotation) {
        this.context = context;
        this.filterInfoList = filterInfoList;
        filterInfoList.get(0).setSelected(true);
        int size = DensityUtil.dip2px(context, 75);
        logoSrcBitmap = BitmapUtil.loadBitmap(first_frame_image, size, size);
        if (rotation != 0) {
            logoSrcBitmap = BitmapUtil.rotateBitmap(logoSrcBitmap, rotation);
        }
        logoSrcBitmap = BitmapUtil.getARGB_8888Bitmap(logoSrcBitmap);
    }

    public FilterAdapter(Context context, List<FilterInfo> filterInfoList, Bitmap first_frame_bitmap, int rotation) {
        this.context = context;
        this.filterInfoList = filterInfoList;
        if (null == first_frame_bitmap) return;

        filterInfoList.get(0).setSelected(true);
        int size = DensityUtil.dip2px(context, 75);
        logoSrcBitmap = BitmapUtil.scaleBitmap(first_frame_bitmap, size, size);
        if (rotation != 0) {
            logoSrcBitmap = BitmapUtil.rotateBitmap(logoSrcBitmap, rotation);
        }
        //转换成RGBA_8888
        logoSrcBitmap = BitmapUtil.getARGB_8888Bitmap(logoSrcBitmap);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, R.layout.item_choose_filter, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FilterInfo filterInfo = filterInfoList.get(position);
//        holder.iv_filter_preview.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(filterInfo.getIcon_name(), "drawable", context.getPackageName())));
        if (null == filterInfo.getIcon_bitmap()) {
            filterInfo.setIcon_bitmap(CGENativeLibrary.filterImage_MultipleEffects(logoSrcBitmap, filterInfo.getFilter_param(), filterInfo.getIntensity()));
        }
        holder.iv_filter_preview.setImageBitmap(filterInfo.getIcon_bitmap());
//        holder.iv_filter_preview.setImageBitmap(logoSrcBitmap);

        holder.tv_filter_name.setSelected(filterInfo.isSelected());
        holder.tv_filter_name.setText(filterInfo.getFilter_name());
        if (filterInfo.isSelected()) {
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
        return filterInfoList.size();
    }

    public void setLastSelectedPosition(int lastSelectedPosition) {
        this.lastSelectedPosition = lastSelectedPosition;
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
            this.tv_filter_name = (TextView) rootView.findViewById(R.id.tv_filter_name);
            this.rl_filter_image = rootView.findViewById(R.id.rl_filter_image);
            this.iv_xuanze = rootView.findViewById(R.id.iv_xuanze);
            this.iv_white = rootView.findViewById(R.id.iv_white);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFiltering) return;

                    filterInfoList.get(lastSelectedPosition).setSelected(false);
                    notifyItemChanged(lastSelectedPosition);
                    lastSelectedPosition = position;
                    filterInfoList.get(position).setSelected(true);
                    notifyItemChanged(position);
                    if (null != onItemClickListener)
                        onItemClickListener.OnItemClick(view, position);

                }
            });

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setFilterState(boolean isFiltering) {
        this.isFiltering = isFiltering;
    }
}
