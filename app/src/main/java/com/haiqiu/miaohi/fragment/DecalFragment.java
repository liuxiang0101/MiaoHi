package com.haiqiu.miaohi.fragment;

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
import com.haiqiu.miaohi.adapter.DecalAdapter;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.DecalInfo;
import com.haiqiu.miaohi.okhttp.MHHttpBaseHandler;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.StickerResponse;
import com.haiqiu.miaohi.service.AssertService;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.FileUtils;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.widget.OnItemClickListener;
import com.tendcloud.tenddata.TCAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhandalin on 2016-12-01 15:27.
 * 说明:贴纸Fragment
 */
public class DecalFragment extends BaseFragment implements OnItemClickListener {
    private final static int pageSize = 30;
    private final static String DECAL_JSON_NAME = "decal_json.data";

    private RecyclerView recyclerView;
    private List<DecalInfo> decalInfos;
    private OnDecalSelectedListener onDecalSelectedListener;
    private DecalAdapter decalAdapter;
    private LinearLayoutManager layoutManager;
    private int lastLoadMorePosition;
    private int pageIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = new RecyclerView(context);
        recyclerView.setClipToPadding(false);
        int padding = DensityUtil.dip2px(context, 5);
        recyclerView.setPadding(padding, 0, padding, 0);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (AssertService.isRunning()) {
            context.showToastAtCenter("资源正在加载中...");
            return;
        }
        //先加载本地默认的
        File file = new File(context.getFilesDir() + "/" + ConstantsValue.VideoEdit.PASTER_FILE_INFO_PATH);
        if (file.exists()) {
            String json = FileUtils.readFile(file.getAbsolutePath());
            if (null == json) {
                gotoFail();
                return;
            }
            decalInfos = new Gson().fromJson(json, new TypeToken<ArrayList<DecalInfo>>() {
            }.getType());
            decalAdapter = new DecalAdapter(context, decalInfos);
            decalAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(decalAdapter);
        } else {
            gotoFail();
        }

        initDataFromNetWork();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int itemPosition = layoutManager.findLastVisibleItemPosition();
                if (null != decalAdapter && itemPosition == decalAdapter.getItemCount() - pageSize - 1 && itemPosition != lastLoadMorePosition) {
                    lastLoadMorePosition = itemPosition;
                    initDataFromNetWork();
                    MHLogUtil.d(TAG, "lastLoadMorePosition=" + lastLoadMorePosition);
                }
            }
        });
    }

    //网络请求失败后用本地数据
    private void initDataFromNetWork() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_size", pageSize + "");
        requestParams.addParams("page_index", pageIndex + "");

        MHHttpClient.getInstance().post4NoParseJson(ConstantsValue.Url.GET_VIDEO_STICKERS, requestParams, new MHHttpBaseHandler() {
            @Override
            public void onSuccess(String content) {
                StickerResponse response = new Gson().fromJson(content, StickerResponse.class);
                if (null == response || null == response.data || null == response.data.page_result || response.data.page_result.size() == 0) {
                    MHLogUtil.d(TAG, "无更多贴纸---pageIndex=" + pageIndex);
                    return;
                }

                decalAdapter.addData(response.data.page_result);
                MHLogUtil.d(TAG, "从网络获取贴纸成功");
                FileUtils.writeFile(context, DECAL_JSON_NAME, content);
                pageIndex++;
            }

            @Override
            public void onFailure(String content) {
                if (pageIndex == 0)
                    initDataFromCache();
            }
        });
    }

    private void initDataFromCache() {
        try {
            String json = FileUtils.readFile(context, DECAL_JSON_NAME);
            StickerResponse response = new Gson().fromJson(json, StickerResponse.class);
            decalAdapter.addData(response.data.page_result);
            MHLogUtil.d(TAG, "从本地获取成功--size=" + response.data.page_result.size());
        } catch (Exception e) {

            MHLogUtil.e(TAG, e);
        }
    }


    public void setCurrentSelected(DecalInfo decalInfo) {
        if (decalInfos.size() == 0 || null == decalAdapter) return;
        if (null == decalInfo) decalInfo = decalInfos.get(0);

        for (int i = 0; i < decalInfos.size(); i++) {
            DecalInfo decal = decalInfos.get(i);
            decal.setSelected(false);
            if (decal == decalInfo) {
                decal.setSelected(true);
                decalAdapter.setLastSelectedPosition(i);
            }
        }
        if (null == decalInfo) {
            decalInfos.get(0).setSelected(true);
            decalAdapter.setLastSelectedPosition(0);
        }
        decalAdapter.notifyDataSetChanged();
    }

    private void gotoFail() {
        context.showToastAtCenter("资源获取失败\n请卸载并重新安装");
    }


    @Override
    public void OnItemClick(View view, int position) {
        if (null != onDecalSelectedListener) {
            onDecalSelectedListener.onDecalSelectedListener(decalInfos.get(position));
            TCAgent.onEvent(context, "BOOM-" + decalInfos.get(position).getSticker_name() + ConstantsValue.android);
        }
    }

    public interface OnDecalSelectedListener {
        void onDecalSelectedListener(DecalInfo decalInfo);
    }

    public void setOnDecalSelectedListener(OnDecalSelectedListener onDecalSelectedListener) {
        this.onDecalSelectedListener = onDecalSelectedListener;
    }
}
