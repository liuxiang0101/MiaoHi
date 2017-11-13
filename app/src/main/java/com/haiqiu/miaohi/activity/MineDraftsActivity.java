package com.haiqiu.miaohi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.adapter.SlideAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.receiver.RefreshUploadEvent;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DraftUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.view.CommonNavigation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 我的-草稿箱
 * Created by zhandalin on 2016/6/27.
 */
public class MineDraftsActivity extends BaseActivity {

    private List<VideoUploadInfo> list;

    private CommonNavigation navigation;
    private ListView listview;
    private SlideAdapter adapter;
    private final String TAG = "MineDraftsActivity";
    private TextView tv;
    //    private boolean isUploading;//是否正在上传
    private RelativeLayout rl_clickinterccept;
//    private boolean isTouch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_drafts);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        addHeader();
        initData();
        isShowError();
        if(adapter!=null){
            adapter.setOnDraftDeleteListener(new SlideAdapter.OnDraftDeleteListener() {
                @Override
                public void onDraftDelete(int position) {
                    // 删除
                    MHLogUtil.d(TAG, "position=" + position);
                    if (null == list) return;
                    VideoUploadInfo videoUploadInfo = list.get(position);
                    if (null == videoUploadInfo) return;
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                    DraftUtil.deleteDraft(videoUploadInfo);
                    isShowError();
                    if(list.isEmpty()){
                        showBlankView();
                    }
                }
            });
        }
    }

    private void initView() {
        navigation = (CommonNavigation) findViewById(R.id.navigation);
        listview = (ListView) findViewById(R.id.listview);
        rl_clickinterccept = (RelativeLayout) findViewById(R.id.rl_clickinterccept);
//        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int mPosition, long id) {
//                final int position = mPosition - 1;
//                if(list.get(position).isUploading()) return false;
//                final CommonDialog commonDialog = new CommonDialog(context);
//                commonDialog.setContentMsg("确定要删除该草稿吗？");
//                commonDialog.setRightButtonMsg("确定");
//                commonDialog.setLeftButtonMsg("取消");
//                commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
//                    @Override
//                    public void onLeftButtonOnClick() {
//                        commonDialog.dismiss();
//                    }
//                });
//                commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
//                    @Override
//                    public void onRightButtonOnClick() {
//                        // 删除
//                        MHLogUtil.d(TAG, "position=" + position);
//                        if (null == list) return;
//                        VideoUploadInfo videoUploadInfo = list.get(position);
//                        if (null == videoUploadInfo) return;
//                        list.remove(position);
//                        adapter.notifyDataSetChanged();
//                        DraftUtil.deleteDraft(videoUploadInfo);
//                        isShowError();
//                        if(list.isEmpty()){
//                            showBlankView();
//                        }
//                    }
//                });
//                commonDialog.show();
//                return false;
//            }
//        });
    }

    /**
     * 获取草稿箱数据
     */
    public void initData() {
        try {
            list = DraftUtil.getDraftList();
            if (null == list || list.size() == 0) {
                showBlankView();
                if (adapter != null) {
                    listview.setAdapter(adapter);
                }
            } else {
                if (adapter == null) {
                    adapter = new SlideAdapter(this, list);
                    listview.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    private void addHeader() {
        tv = new TextView(this);
        tv.setText("您有视频上传失败，请重新上传");
        tv.setTextColor(Color.parseColor("#fe6262"));
        tv.setBackgroundColor(Color.parseColor("#fde3e3"));
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        tv.setPadding(0, DensityUtil.dip2px(this, 8), 0, DensityUtil.dip2px(this, 8));
        listview.addHeaderView(tv);
    }

    private void removeHeader() {
        if (list != null) listview.removeHeaderView(tv);
    }

    private void isShowError() {
        try {
            removeHeader();
            for (VideoUploadInfo uploadInfo : list) {
                if (uploadInfo.isUploadFail()) {
                    tv.setVisibility(View.VISIBLE);
                    addHeader();
                    break;
                }
            }
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
            removeHeader();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == 200) {
                if (!isLogin(false)) return;
                int position = getIntent().getExtras().getInt("postion");
                VideoUploadInfo videoUploadInfo = list.get(position);
                videoUploadInfo.setFromInfo(VideoUploadInfo.FROM_DRAFTS);
                Intent intent = new Intent(context, VideoEditActivity.class);
                intent.putExtra("videoUploadInfo", videoUploadInfo);
                context.startActivity(intent);
            }
        }
    }

    /**
     * 刷新上传进度
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshUpdateCountEvent(RefreshUploadEvent event) {
        if (event == null) return;
        if (event.getTask() == null) return;
        VideoUploadInfo task = event.getTask();

        for (int i = 0; i < list.size(); i++) {
            VideoUploadInfo videoUploadInfo = list.get(i);
            if (videoUploadInfo.getQuestionId() != null) {
                //映答
                if (task.getQuestionId() != null && TextUtils.equals(videoUploadInfo.getQuestionId(), task.getQuestionId())) {
                    videoUploadInfo.setUploadState(task.getUploadState());
                    videoUploadInfo.setProsess(task.getProsess());
                    if (task.getUploadState() == VideoUploadInfo.UPLOAD_SUCCESS) {
                        list.remove(i);
                        isShowError();
                        if (list.size() == 0) {
                            showBlankView();
                        }
                    }
                    if (adapter != null) adapter.notifyDataSetChanged();
                    return;
                }
            } else if (videoUploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_VIDEO) {
                //视频
                if (task.getVideoSrcPath() != null && TextUtils.equals(videoUploadInfo.getVideoSrcPath(), task.getVideoSrcPath())) {
                    videoUploadInfo.setUploadState(task.getUploadState());
                    videoUploadInfo.setProsess(task.getProsess());
                    MHLogUtil.d("UploadState", task.getUploadState() + "");

                    if (task.getUploadState() == VideoUploadInfo.UPLOAD_SUCCESS) {
                        list.remove(i);
                        if (list.size() == 0) {
                            showBlankView();
                        }
                        isShowError();
                    }
                    if (adapter != null) adapter.notifyDataSetChanged();
                    return;
                }
            } else if (videoUploadInfo.getMediaType() == VideoUploadInfo.MediaType.MEDIA_TYPE_PICTURE) {
                //图片
                if (task.getPictureSrcPath() != null && TextUtils.equals(videoUploadInfo.getPictureSrcPath(), task.getPictureSrcPath())) {
                    videoUploadInfo.setUploadState(task.getUploadState());
                    videoUploadInfo.setProsess(task.getProsess());
                    if (task.getUploadState() == VideoUploadInfo.UPLOAD_SUCCESS) {
                        list.remove(i);
                        if (list.size() == 0) {
                            showBlankView();
                        }
                        isShowError();
                    }
                    if (adapter != null) adapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != adapter) adapter.notifyDataSetChanged();
    }
}
