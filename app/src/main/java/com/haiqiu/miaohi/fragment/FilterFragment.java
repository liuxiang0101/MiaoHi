package com.haiqiu.miaohi.fragment;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.adapter.FilterAdapter;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.FilterInfo;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.widget.OnItemClickListener;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhandalin on 2016-12-01 15:12.
 * 说明:滤镜种类的fragment
 */
public class FilterFragment extends BaseFragment {
    public final static String json = "[{\"filter_name\":\"原画\",\"filter_param\":\"\",\"intensity\":1},{\"filter_name\":\"白炽\",\"filter_param\":\"@curve R(0, 0)(69, 63)(105, 138)(151, 222)(255, 255)G(0, 0)(67, 51)(135, 191)(255, 255)B(0, 0)(86, 76)(150, 212)(255, 255)\",\"intensity\":0.6},{\"filter_name\":\"映像\",\"filter_param\":\"@curve R(40, 162)(108, 186)(142, 208)(193, 227)(239, 249)G(13, 7)(72, 87)(124, 150)(197, 206)(255, 255)B(8, 22)(57, 97)(112, 147)(184, 204)(255, 222)\",\"intensity\":0.56},{\"filter_name\":\"绿茵\",\"filter_param\":\"@adjust lut wildbird.png\",\"intensity\":0.6},{\"filter_name\":\"胶片\",\"filter_param\":\"@adjust saturation 0 @adjust level 0 0.83921 0.8772\",\"intensity\":1},{\"filter_name\":\"LOMO\",\"filter_param\":\"@vigblend mix 10 10 30 255 91 0 1.0 0.5 0.5 3 @curve R(0, 31)(35, 75)(81, 139)(109, 174)(148, 207)(255, 255)G(0, 24)(59, 88)(105, 146)(130, 171)(145, 187)(180, 214)(255, 255)B(0, 96)(63, 130)(103, 157)(169, 194)(255, 255)\",\"intensity\":0.8},{\"filter_name\":\"暖阳\",\"filter_param\":\"@curve R(48, 56)(82, 129)(130, 206)(214, 255)G(7, 37)(64, 111)(140, 190)(232, 220)B(2, 97)(114, 153)(229, 172)\",\"intensity\":0.6},{\"filter_name\":\"怀旧\",\"filter_param\":\"@curve R(39, 0)(93, 61)(130, 136)(162, 193)(208, 255)G(41, 0)(92, 61)(128, 133)(164, 197)(200, 250)B(0, 23)(125, 127)(255, 230)\",\"intensity\":0.44},{\"filter_name\":\"迷幻\",\"filter_param\":\"@style haze 0.5 -0.14 1 0.8 1 \",\"intensity\":-0.7},{\"filter_name\":\"冲印\",\"filter_param\":\"@curve R(40, 40)(86, 148)(255, 255)G(0, 28)(67, 140)(142, 214)(255, 255)B(0, 100)(103, 176)(195, 174)(255, 255) @adjust hsv 0.32 0 -0.5 -0.2 0 -0.4\",\"intensity\":0.54},{\"filter_name\":\"新锐\",\"filter_param\":\"@curve R(42, 2)(53, 52)(80, 102)(100, 123)(189, 196)(255, 255)G(55, 74)(75, 98)(95, 114)(177, 197)(203, 212)(221, 220)(229, 234)(240, 249)B(0, 132)(81, 188)(180, 251)\",\"intensity\":0.46},{\"filter_name\":\"微光\",\"filter_param\":\"@curve R(0, 0)(149, 145)(255, 255)G(0, 0)(149, 145)(255, 255)B(0, 0)(149, 145)(255, 255) @pixblend colordodge 0.937 0.482 0.835 1 20\",\"intensity\":0.66}]";

    private RecyclerView recyclerView;
    private List<FilterInfo> filterInfoList;
    private FilterAdapter filterAdapter;
    private OnFilterSelectListener onFilterSelectListener;
    private String imagePath;
    private String videoPath;
    private Bitmap frameAtTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = new RecyclerView(context);
        recyclerView.setClipToPadding(false);
        int padding = DensityUtil.dip2px(context, 5);
        recyclerView.setPadding(padding, 0, padding, 0);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != imagePath) {
            initImageData();
        } else if (null != videoPath) {
            initVideoData();
        }
    }

    private void initImageData() {
        filterInfoList = new Gson().fromJson(json, new TypeToken<ArrayList<FilterInfo>>() {
        }.getType());
        filterAdapter = new FilterAdapter(context, filterInfoList, imagePath, 0);
        recyclerView.setAdapter(filterAdapter);
        filterAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                if (null != onFilterSelectListener && null != filterInfoList && filterInfoList.size() > position) {
                    onFilterSelectListener.onFilterSelect(filterInfoList.get(position));
                    TCAgent.onEvent(context,"LOMO-"+filterInfoList.get(position).getFilter_name()+ ConstantsValue.android);
                }
            }
        });
    }

    private void initVideoData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(videoPath);
                    frameAtTime = retriever.getFrameAtTime(0);

//                    int rotation = 0;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                        String metadata = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
//                        if (null != metadata)
//                            rotation = Integer.parseInt(metadata);
//                    }
                    filterInfoList = new Gson().fromJson(json, new TypeToken<ArrayList<FilterInfo>>() {
                    }.getType());
                    filterAdapter = new FilterAdapter(context, filterInfoList, frameAtTime, 0);
                    filterAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, int position) {
                            if (null != onFilterSelectListener && null != filterInfoList && filterInfoList.size() > position) {
                                onFilterSelectListener.onFilterSelect(filterInfoList.get(position));
                                TCAgent.onEvent(context,"LOMO-"+filterInfoList.get(position).getFilter_name()+ ConstantsValue.android);
                            }
                        }
                    });
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(filterAdapter);
                        }
                    });
                } catch (Exception e) {
                    MHLogUtil.e(TAG,e);
                }
            }
        }).start();
    }

    public void setCurrentSelected(FilterInfo filterInfo) {
        if (null == filterInfoList || filterInfoList.size() == 0 || null == filterAdapter) return;

        for (int i = 0; i < filterInfoList.size(); i++) {
            FilterInfo filter = filterInfoList.get(i);
            filter.setSelected(false);
            if (filter == filterInfo) {
                filter.setSelected(true);
                filterAdapter.setLastSelectedPosition(i);
            }
        }
        if (null == filterInfo) {
            filterInfoList.get(0).setSelected(true);
            filterAdapter.setLastSelectedPosition(0);
        }
        filterAdapter.notifyDataSetChanged();
    }

    public Bitmap getFirstFrameImage() {
        return frameAtTime;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * 视频调用的方法
     */
    public void setVideoPath(final String videoPath) {
        this.videoPath = videoPath;
    }


    public interface OnFilterSelectListener {
        void onFilterSelect(FilterInfo filterInfo);
    }

    public void setOnFilterSelectListener(OnFilterSelectListener onFilterSelectListener) {
        this.onFilterSelectListener = onFilterSelectListener;
    }
}
