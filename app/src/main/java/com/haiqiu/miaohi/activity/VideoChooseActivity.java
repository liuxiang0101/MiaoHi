package com.haiqiu.miaohi.activity;

import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.ChooseVideoAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.ChooseMediaBean;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhandalin on 2016-06-27 14:27.
 * 说明:视频本地上传
 */
public class VideoChooseActivity extends BaseActivity {

    private static final String TAG = "VideoChooseActivity";
    private RecyclerView recycler_view;
    private ChooseVideoAdapter adapter;
    private VideoUploadInfo videoUploadInfo;
    private View tv_tip_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_choose);
        videoUploadInfo = getIntent().getParcelableExtra("videoUploadInfo");
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new GridLayoutManager(context, 3));
        tv_tip_info = findViewById(R.id.tv_tip_info);
        initData();
    }

    private void initData() {
        showLoading("载入中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaScannerConnection.scanFile(getApplication(), new String[]{
                                Environment.getExternalStorageDirectory().getAbsolutePath(),
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath(),
                                Environment.getDownloadCacheDirectory().getAbsolutePath()
                        },
                        new String[]{"Video", "Image"}, null);
                getData("external");//外部存储获取资源
                getData("internal");//内部存储获取资源
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hiddenLoadingView();
                        if (null == adapter || adapter.getItemCount() == 0) {
                            tv_tip_info.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }).start();
    }

    private void getData(String volumeName) {
        Cursor query = null;
        try {
            MHLogUtil.d(TAG, "volumeName=" + volumeName);
            Uri contentUri = MediaStore.Files.getContentUri(volumeName);
            String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + " = " + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE +
                    " or " + MediaStore.Files.FileColumns.MEDIA_TYPE + " = " + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

            query = getContentResolver().query(contentUri, new String[]{MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.MEDIA_TYPE,
                            "width", "height", MediaStore.Files.FileColumns.SIZE, "duration"},
                    selection, null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");

            String path;
            long duration;
            long size;
            float den;
            File file;

            final List<ChooseMediaBean> dataList = new ArrayList<>();
            while (query.moveToNext()) {
                path = query.getString(query.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                duration = query.getLong(query.getColumnIndex("duration"));
                size = query.getInt(query.getColumnIndex(MediaStore.Files.FileColumns.SIZE));
                size = (long) (size / 1024f / 1024 + 0.5);

                //添加过滤条件
                if (null != path && path.endsWith(".ts")) continue;
                if (null == path) return;

                file = new File(path);
                if (!file.exists()) continue;

                //表示是映答,不显示图片
                int media_type = query.getInt(query.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE));
                if (media_type == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE && !MHStringUtils.isEmpty(videoUploadInfo.getQuestionId())) {
                    continue;
                }


                ChooseMediaBean mediaBean = new ChooseMediaBean();
                mediaBean.setMediaPath(path);
                mediaBean.setMediaType(media_type);
                mediaBean.setDuration(duration);
                mediaBean.setMediaHeight(query.getInt(query.getColumnIndex("height")));
                mediaBean.setMediaWidth(query.getInt(query.getColumnIndex("width")));
                mediaBean.setMediaSize(size);

                if (!MHStringUtils.isEmpty(videoUploadInfo.getQuestionId()) && duration < 30 * 1000) {
                    mediaBean.setFail(true);
                    mediaBean.setFailMessage("映答视频最少要30秒哦");
                }
                if (duration > 5 * 60 * 1000 + 3000) {
                    mediaBean.setFail(true);
                    mediaBean.setFailMessage("不支持上传超过5分钟的视频");
                }
                den = duration / 1000.0f;
                if (den != 0 && size / den > 3) {
                    MHLogUtil.d(TAG, "--超高清视频--");
                    mediaBean.setFail(true);
                    mediaBean.setFailMessage("您选择的是超高清视频，暂不支持上传");
                }

                dataList.add(mediaBean);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null == adapter) {
                        adapter = new ChooseVideoAdapter(context, dataList);
                        if (null != videoUploadInfo)
                            adapter.setInfo(videoUploadInfo);
                        recycler_view.setAdapter(adapter);
                    } else {
                        adapter.addData(dataList);
                    }
                }
            });
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        } finally {
            if (null != query) query.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MHLogUtil.d(TAG, "VideoChooseActivity--onDestroy");
    }
}
