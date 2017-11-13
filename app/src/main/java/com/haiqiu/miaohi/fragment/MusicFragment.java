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
import com.haiqiu.miaohi.adapter.MusicAdapter;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.MusicInfo;
import com.haiqiu.miaohi.okhttp.MHHttpBaseHandler;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BackgroundMusicResponse;
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
 * Created by zhandalin on 2016-12-05 16:23.
 * 说明:配乐Fragment
 */
public class MusicFragment extends BaseFragment implements OnItemClickListener {
    private final static int pageSize = 30;
    private final static String MUSIC_JSON_NAME = "music_json.data";
    private int pageIndex;

    private RecyclerView recyclerView;
    private List<MusicInfo> localMusicInfoList;
    private OnMusicSelectedListener onMusicSelectedListener;
    private MusicAdapter musicAdapter;
    private LinearLayoutManager layoutManager;
    private int lastLoadMorePosition;


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
        File file = new File(context.getFilesDir() + "/" + ConstantsValue.VideoEdit.MUSIC_FILE_INFO_PATH);
        if (file.exists()) {
            String json = FileUtils.readFile(file.getAbsolutePath());
            if (null == json || json.length() < 5) {
                gotoFail();
                return;
            }
            localMusicInfoList = new Gson().fromJson(json, new TypeToken<ArrayList<MusicInfo>>() {
            }.getType());
            if (null == localMusicInfoList) localMusicInfoList = new ArrayList<>();

            ArrayList<MusicInfo> arrayList = new ArrayList<>();
            arrayList.addAll(localMusicInfoList);
            musicAdapter = new MusicAdapter(context, arrayList);
            musicAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(musicAdapter);
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
                if (null != musicAdapter && itemPosition == musicAdapter.getItemCount() - pageSize - 1 && itemPosition != lastLoadMorePosition) {
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

        MHHttpClient.getInstance().post4NoParseJson(ConstantsValue.Url.GET_ALL_BACKGROUND_MUSIC, requestParams, new MHHttpBaseHandler() {
            @Override
            public void onSuccess(String content) {
                BackgroundMusicResponse response = new Gson().fromJson(content, BackgroundMusicResponse.class);
                if (null == response || null == response.data || null == response.data.page_result || response.data.page_result.size() == 0) {
                    MHLogUtil.d(TAG, "无更多配乐---pageIndex=" + pageIndex);
                    return;
                }

                String filePath = context.getFilesDir().getAbsolutePath() + "/";
                for (MusicInfo musicInfo : response.data.page_result) {
                    File file = new File(filePath + musicInfo.getMusic_id() + ".mp3");
                    if (file.exists()) {
                        musicInfo.setDownloadMusicPath(file.getAbsolutePath());
                        musicInfo.setMusic_state(MusicInfo.MUSIC_STATE_DOWNLOAD_COMPLETE);
                    }
                }

                musicAdapter.addData(response.data.page_result);
                MHLogUtil.d(TAG, "从网络获取成功");
                FileUtils.writeFile(context, MUSIC_JSON_NAME, content);
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
            String json = FileUtils.readFile(context, MUSIC_JSON_NAME);
            BackgroundMusicResponse response = new Gson().fromJson(json, BackgroundMusicResponse.class);

            String filePath = context.getFilesDir().getAbsolutePath() + "/";
            for (MusicInfo musicInfo : response.data.page_result) {
                File file = new File(filePath + musicInfo.getMusic_id() + ".mp3");
                if (file.exists()) {
                    musicInfo.setDownloadMusicPath(file.getAbsolutePath());
                    musicInfo.setMusic_state(MusicInfo.MUSIC_STATE_DOWNLOAD_COMPLETE);
                }
            }
            musicAdapter.addData(response.data.page_result);
            MHLogUtil.d(TAG, "从本地获取成功--size=" + response.data.page_result.size());
        } catch (Exception e) {

            MHLogUtil.e(TAG, e);
        }
    }


    private void gotoFail() {
        context.showToastAtCenter("资源获取失败\n请卸载并重新安装");
    }

    @Override
    public void OnItemClick(View view, int position) {
        if (null != onMusicSelectedListener && null != musicAdapter) {
            MusicInfo musicInfo = musicAdapter.getMusicInfo(position);
            onMusicSelectedListener.onMusicSelectedListener(musicInfo);
            if (null != musicInfo)
                TCAgent.onEvent(context, "C1-" + musicInfo.getMusic_name() + ConstantsValue.android);
        }
    }


    public interface OnMusicSelectedListener {
        void onMusicSelectedListener(MusicInfo musicInfo);
    }

    public void setOnMusicSelectedListener(OnMusicSelectedListener onMusicSelectedListener) {
        this.onMusicSelectedListener = onMusicSelectedListener;
    }
}
