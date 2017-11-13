package com.haiqiu.miaohi.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.MainActivity;
import com.haiqiu.miaohi.adapter.ShareItemAdapter;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.SharedItemBean;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.umeng.AbsUMShare;
import com.haiqiu.miaohi.umeng.UmengLogin;
import com.haiqiu.miaohi.umeng.UmengShare;
import com.haiqiu.miaohi.utils.BehaviourStatistic;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.ToastUtils;
import com.haiqiu.miaohi.view.ShareDialog;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享layout
 * Created by ningl on 2016/6/25.
 */
public class ShareLayout extends LinearLayout implements View.OnClickListener, ShareItemAdapter.OnItemClickListener {
    private static final String TAG = "ShareLayout";

    private RecyclerView rv_share;
    private TextView tv_sharedeletevideo;
    private TextView tv_sharecancle;
    private List<SharedItemBean> sharedItemBeans;
    private ShareItemAdapter adapter;
    public static final int SHARE_SINA = 0;
    public static final int SHARE_WECHATCIRCLE = 1;
    public static final int SHARE_WECHAT = 2;
    public static final int SHARE_QQ = 3;
    public static final int SHARE_QQZONE = 4;
    public static final int SHARE_COPYLINK = 5;
    public static final int SHARE_REPORTVIDEO = 6;
    private String[] names = {"微博", "朋友圈", "微信", "QQ", "QQ空间", "复制链接"};
    private int[] drawables = {R.drawable.sharesina, R.drawable.sharewechatcircle, R.drawable.sharewechat, R.drawable.shareqq, R.drawable.shareqqzone, R.drawable.sharecopylink};
    private Context context;
    private BaseActivity activity;
    private ShareRsultListener shareRsultListener;
    private DeleteVideoListener deleteVideoListener;
    private OnDeleteOrReportListener onDeleteOrReportListener;
    private OnShareImgPath onShareImgPath;
    private AbsUMShare umShare;
    private CloseDialog closeDialog;
    private ShareDialog shareDialog;

    int reportType = 0;//0视频 1映答 2用户
    String question_id;
    String user_id;

    private int shareLable;
    private String imgPath;
    private String shareLink;
    private View v_cancletopline;
    private View v_deletetopline;
    private int rootType;

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    private static final int IMGANDTEXT = 0;
    private static final int IMG = 1;

    public static final int VIDEO_DELETE = 1;//删除视频
    public static final int IMG_DELETE = 2;  //删除图片
    public static final int VIDEO_REPORT = 3;//举报视频
    public static final int IMG_REPORT = 4;  //举报图片
    public static final int PEOPLE_REPORT = 5;//举报人
    public static final int QA_REPORT = 6;    //举报映答


    public ShareLayout(Context context) {
        this(context, null);
    }

    public ShareLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View.inflate(context, R.layout.item_share, this);
        sharedItemBeans = new ArrayList<>();
        initView(context);
    }

    private void initView(Context context) {
        rv_share = (RecyclerView) findViewById(R.id.rv_share);
        tv_sharedeletevideo = (TextView) findViewById(R.id.tv_sharedeletevideo);
        tv_sharecancle = (TextView) findViewById(R.id.tv_sharecancle);
        v_cancletopline = findViewById(R.id.v_cancletopline);
        v_deletetopline = findViewById(R.id.v_deletetopline);
        tv_sharecancle.setOnClickListener(this);
        tv_sharedeletevideo.setOnClickListener(this);
        GridLayoutManager glm = new GridLayoutManager(context, 3);
        glm.setOrientation(GridLayoutManager.VERTICAL);
        rv_share.setLayoutManager(glm);
        adapter = new ShareItemAdapter(sharedItemBeans, context);
        adapter.setOnItemClickListener(this);
        rv_share.setAdapter(adapter);
    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sharecancle://取消
                if (null != shareRsultListener) shareRsultListener.cancle();
                break;

            case R.id.tv_sharedeletevideo://删除视频
                if (closeDialog != null) closeDialog.close();
                final CommonDialog commonDialog = new CommonDialog(context);
                if (umShare == null) return;
                if (umShare.getDeleteBtnType() == VIDEO_DELETE) {
                    commonDialog.setContentMsg("( ･᷄ὢ･᷅ )确定要删除这段视频吗？");
                    tv_sharedeletevideo.setText("删除");
                } else if (umShare.getDeleteBtnType() == IMG_DELETE) {
                    commonDialog.setContentMsg("( ･᷄ὢ･᷅ )确定要删除这张图片吗？");
                    tv_sharedeletevideo.setText("删除");
                } else if (umShare.getDeleteBtnType() == VIDEO_REPORT) {
                    commonDialog.setContentMsg("( ･᷄ὢ･᷅ )确定要举报这段视频吗？");
                    tv_sharedeletevideo.setText("举报");
                } else if (umShare.getDeleteBtnType() == IMG_REPORT) {
                    commonDialog.setContentMsg("( ･᷄ὢ･᷅ )确定要举报这张图片吗？");
                    tv_sharedeletevideo.setText("举报");
                } else if (umShare.getDeleteBtnType() == PEOPLE_REPORT) {
                    commonDialog.setContentMsg("( ･᷄ὢ･᷅ )确定要举报这个用户吗？");
                    tv_sharedeletevideo.setText("举报");
                } else if (umShare.getDeleteBtnType() == QA_REPORT) {
                    commonDialog.setContentMsg("( ･᷄ὢ･᷅ )确定要举报这个映答吗？");
                    tv_sharedeletevideo.setText("举报");
                }
                commonDialog.setRightButtonMsg("确定");
                commonDialog.setLeftButtonMsg("取消");
                commonDialog.show();
                commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
                    @Override
                    public void onLeftButtonOnClick() {
                        commonDialog.dismiss();
                    }
                });
                commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
                    @Override
                    public void onRightButtonOnClick() {
                        if (umShare == null) return;
                        if (umShare.getDeleteBtnType() == VIDEO_DELETE) {
                            deleteVideo();
                        } else if (umShare.getDeleteBtnType() == IMG_DELETE) {
                            deleteImg();
                        } else if (umShare.getDeleteBtnType() == VIDEO_REPORT) {
                            reportVideo();
                        } else if (umShare.getDeleteBtnType() == IMG_REPORT) {
                            reportImage();
                        } else if (umShare.getDeleteBtnType() == PEOPLE_REPORT) {
                            reportUser();
                        } else if (umShare.getDeleteBtnType() == QA_REPORT) {
                            reportQuestion();
                        }
                        commonDialog.dismiss();
                    }
                });
                break;
        }
    }

    /**
     * 填充分享平台数据
     */
    public void setData() {
        sharedItemBeans.clear();
        for (int i = 0; i < names.length; i++) {
            if (screenData(i)) {
                SharedItemBean sharedItemBean = new SharedItemBean();
                sharedItemBean.setName(names[i]);
                sharedItemBean.setDrawable(drawables[i]);
                sharedItemBean.setSharedType(i);
                sharedItemBeans.add(sharedItemBean);
            }
        }
        if (sharedItemBeans.size() < 4) {
            ViewGroup.LayoutParams lp = rv_share.getLayoutParams();
            lp.height = DensityUtil.dip2px(context, 90);
            rv_share.setLayoutParams(lp);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 数据筛选
     */
    private boolean screenData(int type) {
        switch (type) {
            case SHARE_WECHATCIRCLE:
            case SHARE_WECHAT:
                if (UmengLogin.isWeixinAvilible(activity))
                    return true;
                break;
            case SHARE_QQ:
            case SHARE_QQZONE:
                if (UmengLogin.isQQClientAvailable(activity))
                    return true;
                break;
            default:
                return true;
        }
        return false;
    }

    /**
     * 设置分享相关参数
     *
     * @param umShare
     */
    public void setShareInfo(AbsUMShare umShare) {
        this.umShare = umShare;
        if (umShare.isHasDetete()) {
            tv_sharedeletevideo.setVisibility(VISIBLE);
//            v_cancletopline.setVisibility(VISIBLE);
//            v_deletetopline.setVisibility(VISIBLE);
        } else {
            tv_sharedeletevideo.setVisibility(GONE);
//            v_cancletopline.setVisibility(GONE);
//            v_deletetopline.setVisibility(GONE);
        }
        tv_sharecancle.setVisibility(umShare.isHasCancleBtn() ? View.VISIBLE : View.GONE);//是否显示取消按钮
        if (umShare.getDeleteBtnType() == VIDEO_DELETE) {
            tv_sharedeletevideo.setText("删除");
            tv_sharedeletevideo.setTextColor(getResources().getColor(R.color.common_red));
        } else if (umShare.getDeleteBtnType() == IMG_DELETE) {
            tv_sharedeletevideo.setText("删除");
            tv_sharedeletevideo.setTextColor(getResources().getColor(R.color.common_red));
        } else if (umShare.getDeleteBtnType() == VIDEO_REPORT) {
            tv_sharedeletevideo.setText("举报");
            tv_sharedeletevideo.setTextColor(getResources().getColor(R.color.fontblue));
        } else if (umShare.getDeleteBtnType() == IMG_REPORT) {
            tv_sharedeletevideo.setText("举报");
            tv_sharedeletevideo.setTextColor(getResources().getColor(R.color.fontblue));
        } else if (umShare.getDeleteBtnType() == PEOPLE_REPORT) {
            tv_sharedeletevideo.setText("举报");
            tv_sharedeletevideo.setTextColor(getResources().getColor(R.color.fontblue));
        } else if (umShare.getDeleteBtnType() == QA_REPORT) {
            tv_sharedeletevideo.setText("举报");
            tv_sharedeletevideo.setTextColor(getResources().getColor(R.color.fontblue));
        }

    }

    /**
     * 设置链接
     *
     * @param shareLink
     */
    public void setLink(String shareLink) {
        this.shareLink = shareLink;
    }

    @Override
    public void callback(int position) {
        if (null == shareRsultListener) return;
        if (null != shareRsultListener) shareRsultListener.cancle();
        BaseActivity baseActivity = (BaseActivity) activity;
        baseActivity.showLoading();
        switch (sharedItemBeans.get(position).getSharedType()) {
            case SHARE_SINA://新浪微博分享
//                baseActivity.showLoading("正在跳转...",false,false);
                if (!checkNet()) break;
                if (shareLable == IMGANDTEXT)
                    UmengShare.sharedSina(activity, umShare.getDescribe(SHARE_MEDIA.SINA), umShare.getShareLink(), umShare.imgUrl, shareRsultListener);
                else if (shareLable == IMG) {
                    onShareImgPath.getimgPath(SHARE_MEDIA.SINA);
                }
                if (rootType == 1)
                    TCAgent.onEvent(context, "视频详情微博" + ConstantsValue.android);
                else if (rootType == 2)
                    TCAgent.onEvent(context, "图片详情微博" + ConstantsValue.android);
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "sharing_weibo");
                    jsonObject.put("description", "微博分享");
                    BehaviourStatistic.uploadBehaviourInfo(jsonObject);
                } catch (Exception e) {

                    MHLogUtil.e(TAG, e);
                }
                break;
            case SHARE_QQ://qq分享
//                baseActivity.showLoading("正在跳转...",false,false);
                if (!checkNet()) break;
                if (shareLable == IMGANDTEXT)
                    UmengShare.sharedQQ(activity, umShare.getTitle(SHARE_MEDIA.QQ), umShare.getDescribe(SHARE_MEDIA.QQ), umShare.getShareLink(), umShare.imgUrl, shareRsultListener);
                else if (shareLable == IMG) {
                    onShareImgPath.getimgPath(SHARE_MEDIA.QQ);
                }
                if (rootType == 1)
                    TCAgent.onEvent(context, "视频详情QQ" + ConstantsValue.android);
                else if (rootType == 2)
                    TCAgent.onEvent(context, "图片详情QQ" + ConstantsValue.android);

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "sharing_qq");
                    jsonObject.put("description", "微信qq分享");
                    BehaviourStatistic.uploadBehaviourInfo(jsonObject);
                } catch (Exception e) {

                    MHLogUtil.e(TAG, e);
                }
                break;
            case SHARE_QQZONE://QQ空间分享
//                baseActivity.showLoading("正在跳转...",false,false);
                if (!checkNet()) break;
                if (shareLable == IMGANDTEXT)
                    UmengShare.sharedQZone(activity, umShare.getTitle(SHARE_MEDIA.QZONE), umShare.getDescribe(SHARE_MEDIA.QZONE), umShare.getShareLink(), umShare.imgUrl, shareRsultListener);
                else if (shareLable == IMG) {
                    onShareImgPath.getimgPath(SHARE_MEDIA.QZONE);
                }
                if (rootType == 1)
                    TCAgent.onEvent(context, "视频详情QQ空间" + ConstantsValue.android);
                else if (rootType == 2)
                    TCAgent.onEvent(context, "图片详情QQ空间" + ConstantsValue.android);

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "sharing_qqzone");
                    jsonObject.put("description", "微信qq空间分享");
                    BehaviourStatistic.uploadBehaviourInfo(jsonObject);
                } catch (Exception e) {

                    MHLogUtil.e(TAG, e);
                }
                break;
            case SHARE_WECHAT://微信分享
//                baseActivity.showLoading("正在跳转...",false,false);
                if (!checkNet()) break;
                if (shareLable == IMGANDTEXT)
                    UmengShare.sharedWX(activity, umShare.getTitle(SHARE_MEDIA.WEIXIN), umShare.getDescribe(SHARE_MEDIA.WEIXIN), umShare.getShareLink(), umShare.imgUrl, shareRsultListener);
                else if (shareLable == IMG) {
                    onShareImgPath.getimgPath(SHARE_MEDIA.WEIXIN);
                }
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "sharing_wechat");
                    jsonObject.put("description", "微信分享");
                    BehaviourStatistic.uploadBehaviourInfo(jsonObject);
                } catch (Exception e) {

                    MHLogUtil.e(TAG, e);
                }
                if (rootType == 1)
                    TCAgent.onEvent(context, "视频详情微信" + ConstantsValue.android);
                else if (rootType == 2)
                    TCAgent.onEvent(context, "图片详情微信" + ConstantsValue.android);
                break;
            case SHARE_WECHATCIRCLE://朋友圈分享
//                baseActivity.showLoading("正在跳转...",false,false);
                if (!checkNet()) break;
                if (shareLable == IMGANDTEXT)
                    UmengShare.sharedWXCirle(activity, umShare.getDescribe(SHARE_MEDIA.WEIXIN_CIRCLE), umShare.getShareLink(), umShare.imgUrl, shareRsultListener);
                else if (shareLable == IMG) {
                    onShareImgPath.getimgPath(SHARE_MEDIA.WEIXIN_CIRCLE);
                }
                if (rootType == 1)
                    TCAgent.onEvent(context, "视频详情朋友圈" + ConstantsValue.android);
                else if (rootType == 2)
                    TCAgent.onEvent(context, "图片详情朋友圈" + ConstantsValue.android);

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "sharing_wechat_circlefriends");
                    jsonObject.put("description", "微信朋友圈分享");
                    BehaviourStatistic.uploadBehaviourInfo(jsonObject);
                } catch (Exception e) {

                    MHLogUtil.e(TAG, e);
                }
                break;
            case SHARE_COPYLINK://复制链接
                baseActivity.hiddenLoadingView();
                ClipboardManager cmb = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                if (shareLable == IMG) {
                    if (shareLink == null) {
                        MHLogUtil.e("链接为空");
                        break;
                    }
                    cmb.setText(shareLink);
                } else if (shareLable == IMGANDTEXT) {
                    cmb.setText(umShare.getShareLink());
                }
                ToastUtils.showToastAtCenter(context, "已经复制到剪切板");
//                    umShare.setCopyLinkStatistics();
                if (shareRsultListener != null)
                    shareRsultListener.reportSuccess();
                if (rootType == 1)
                    TCAgent.onEvent(context, "视频详情复制链接" + ConstantsValue.android);
                else if (rootType == 2)
                    TCAgent.onEvent(context, "图片详情复制链接" + ConstantsValue.android);

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "sharing_copy");
                    jsonObject.put("description", "分享复制链接");
                    BehaviourStatistic.uploadBehaviourInfo(jsonObject);
                } catch (Exception e) {

                    MHLogUtil.e(TAG, e);
                }
                break;
            case SHARE_REPORTVIDEO://举报视频
                if (!checkNet()) break;
                if (reportType == 0) {
                    reportVideo();
                } else if (reportType == 1) {
                    reportQuestion();
                } else if (reportType == 2) {
                    reportUser();
                }

                break;
        }
    }


    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
        this.rootType = reportType;
    }


    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    /**
     * 举报视频
     */
    public void reportVideo() {
        activity.showLoading();
        TCAgent.onEvent(context, "视频详情举报" + ConstantsValue.android);
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("video_id", umShare.shareId);
        MHHttpClient.getInstance().post(BaseResponse.class, activity, ConstantsValue.Url.REPORTVIDEO, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                Toast.makeText(context, "举报成功", Toast.LENGTH_SHORT).show();
                if (onDeleteOrReportListener != null)
                    onDeleteOrReportListener.onDeleteOrReport(VIDEO_REPORT);
            }

            @Override
            public void onFailure(String content) {
                Toast.makeText(context, "举报失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 举报图片
     */
    public void reportImage() {
        activity.showLoading();
        TCAgent.onEvent(context, "图片详情举报" + ConstantsValue.android);
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("photo_id", umShare.shareId);
        MHHttpClient.getInstance().post(BaseResponse.class, activity, ConstantsValue.Url.REPORTPHOTO, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                Toast.makeText(context, "举报成功", Toast.LENGTH_SHORT).show();
                if (onDeleteOrReportListener != null)
                    onDeleteOrReportListener.onDeleteOrReport(VIDEO_REPORT);
            }

            @Override
            public void onFailure(String content) {
                Toast.makeText(context, "举报失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 举报映答
     */
    public void reportQuestion() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("question_id", umShare.shareId);
        MHHttpClient.getInstance().post(BaseResponse.class, activity, ConstantsValue.Url.REPORT_QUESTION, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                Toast.makeText(context, "举报成功", Toast.LENGTH_SHORT).show();
                if (onDeleteOrReportListener != null)
                    onDeleteOrReportListener.onDeleteOrReport(QA_REPORT);
            }

            @Override
            public void onFailure(String content) {
                Toast.makeText(context, "举报失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 举报用户
     */
    public void reportUser() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("user_id", umShare.shareId);
        MHHttpClient.getInstance().post(BaseResponse.class, activity, ConstantsValue.Url.REPORT_USER, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                Toast.makeText(context, "举报成功", Toast.LENGTH_SHORT).show();
                if (onDeleteOrReportListener != null)
                    onDeleteOrReportListener.onDeleteOrReport(PEOPLE_REPORT);
            }

            @Override
            public void onFailure(String content) {
                Toast.makeText(context, "举报失败", Toast.LENGTH_SHORT).show();
                if (shareRsultListener != null)
                    shareRsultListener.reportFail();
            }
        });
    }

    /**
     * 删除视频
     */
    public void deleteVideo() {
        if (!checkNet()) return;

        if (ConstantsValue.isDeveloperMode(context)) {
            if (onDeleteOrReportListener != null)
                onDeleteOrReportListener.onDeleteOrReport(VIDEO_DELETE);
            return;
        }
//
        activity.showLoading();
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("video_id", umShare.shareId);
        MHHttpClient.getInstance().post(BaseResponse.class, activity, ConstantsValue.Url.DELETEVIDEO, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                MainActivity.isDeleteVideo = true;
                ToastUtils.showToastAtCenter(context, "删除视频成功");
                if (onDeleteOrReportListener != null)
                    onDeleteOrReportListener.onDeleteOrReport(VIDEO_DELETE);
//                Intent intent = new Intent(ConstantsValue.IntentFilterAction.VIDEO_DELETE_ACTION);
//                intent.putExtra(ConstantsValue.IntentFilterAction.VIDEO_ID_KEY, umShare.shareId);
//                context.sendBroadcast(intent);
            }

            @Override
            public void onFailure(String content) {
                ToastUtils.showToastAtCenter(context, "删除视频失败");
//                if (deleteVideoListener != null)
//                    deleteVideoListener.deleteVideoFail();
            }
        });
    }

    /**
     * 删除图片
     */
    public void deleteImg() {
        if (ConstantsValue.isDeveloperMode(context)) {
            if (onDeleteOrReportListener != null)
                onDeleteOrReportListener.onDeleteOrReport(IMG_DELETE);
            return;
        }
        activity.showLoading();
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("photo_id", umShare.shareId);
        MHHttpClient.getInstance().post(BaseResponse.class, activity, ConstantsValue.Url.DELETEPHOTO, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                MainActivity.isDeleteVideo = true;
                ToastUtils.showToastAtCenter(context, "图片删除成功");
                if (onDeleteOrReportListener != null)
                    onDeleteOrReportListener.onDeleteOrReport(IMG_DELETE);
            }

            @Override
            public void onFailure(String content) {
                ToastUtils.showToastAtCenter(context, "图片删除失败");
                if (deleteVideoListener != null)
                    deleteVideoListener.deleteVideoFail();
            }
        });
    }

    /**
     * 隐藏取消按钮
     */
    public void hidenCancleBtn() {
        tv_sharecancle.setVisibility(View.GONE);
    }

    /**
     * 隐藏删除按钮
     */
    public void hidenDeleteBtn() {
        tv_sharedeletevideo.setVisibility(View.GONE);
//        v_cancletopline.setVisibility(GONE);
//        v_deletetopline.setVisibility(GONE);
    }

    public void setShareDialog(ShareDialog dialog) {
        this.shareDialog = dialog;
    }

    /**
     * 分享前检查网络
     */
    private boolean checkNet() {
        if (!CommonUtil.isNetworkAvailable(context)) {
            //未连接网络
            if (this.shareDialog != null) {
                shareDialog.dismiss();
                activity.showToastAtBottom("好像断网了");
                return false;
            }
        }
        return true;
    }

    /**
     * 隐藏举报
     */
    public void hidenReport() {
        sharedItemBeans.remove(sharedItemBeans.size() - 1);
        adapter.notifyDataSetChanged();
    }

    public void setShareLable(int lable) {
        this.shareLable = lable;
    }

    public void setOnShareRsultListener(ShareRsultListener shareRsultListener) {
        this.shareRsultListener = shareRsultListener;
    }

    public void setDeleteOrReportListener(OnDeleteOrReportListener onDeleteOrReportListener) {
        this.onDeleteOrReportListener = onDeleteOrReportListener;
    }

    public interface OnDeleteOrReportListener {
        void onDeleteOrReport(int type);
    }

    public interface ShareRsultListener {
        //成功
        public void success();

        //失败
        public void fail();

        //举报成功
        public void reportSuccess();

        //取消
        public void cancle();

        //举报失败
        public void reportFail();

    }

    public void setDeleteListener(DeleteVideoListener deleteVideoListener) {
        this.deleteVideoListener = deleteVideoListener;
    }

    public interface DeleteVideoListener {
        //删除视频成功
        public void deleteVideoSuccess();

        //删除视频失败
        public void deleteVideoFail();
    }

    public void setOnShareImgPath(OnShareImgPath onShareImgPath) {
        this.onShareImgPath = onShareImgPath;
    }

    public interface OnShareImgPath {
        void getimgPath(SHARE_MEDIA platform);
    }

    public interface CloseDialog {
        void close();
    }

    public void setCloseDialog(CloseDialog closeDialog) {
        this.closeDialog = closeDialog;
    }
}
