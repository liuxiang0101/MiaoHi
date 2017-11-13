package com.haiqiu.miaohi.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.adapter.VideoDetailDialog_giftAdapter;
import com.haiqiu.miaohi.bean.VideoDetailAttentionResponse;
import com.haiqiu.miaohi.bean.VideoDetail_dialogGiftBean;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频详情礼物对话框
 * Created by ningl on 2016/6/23.
 */
public class VideoDetail_gift_dialog extends Dialog implements View.OnClickListener, VideoDetailDialog_giftAdapter.OnItemClickListener {
    private TextView tv_dialog_gifttitle;
    private ImageView iv_dialog_giftcancle;
    private ImageView iv_dialog_giftrighttarrow;
    private ImageView iv_dialog_giftleftarrow;
    private RelativeLayout tv_dialog_giftvpout;
    private MyCircleView mcv_dialog_giftheader;
    private ImageView iv_dialog_giftvip;
    private TextView tv_dialog_giftname;
    private TextView tv_dialog_giftnote;
    private TextView tv_dialog_giftfanscount;
    private TextView tv_dialog_giftattention;
    private RelativeLayout rl_dialog_bottomgift;
    private RecyclerView rv_dialog_gift;
    private TextView tv_dialog_gifttime;
    private ImageView iv_dialog_gift;
    private Context context;
    private List<VideoDetail_dialogGiftBean> videoDetail_dialogGiftBeans;//数据源
    private List<Integer> selectList;//选中项记录
    private VideoDetailDialog_giftAdapter adapter;
    private int currentSelectIndex;//当前选中项
    private int lastSelectIndex;//上一次选中项
    private OnAddDataListener onAddDataListener;
    private boolean hasMoreData = true;
    private LinearLayoutManager lm;

    /****** 检测滑动到底部参数******/
    //最后一个可见的item的位置
    int lastVisibleItemPosition;
    //当前滑动的状态
    int currentScrollState = 0;
    //列表数据是否正在加载
    private boolean isLoading;
    private View progress_bar;

    /****************************/


    public VideoDetail_gift_dialog(final Context context, final List<VideoDetail_dialogGiftBean> videoDetail_dialogGiftBeans) {
        super(context, R.style.MiaoHiDialog);
        setContentView(R.layout.dialog_gift);
        this.context = context;
        this.videoDetail_dialogGiftBeans = videoDetail_dialogGiftBeans;
        show();
        initView();
        selectList = new ArrayList<>();
        setSelectList(videoDetail_dialogGiftBeans.size());
        setSelcetedItem(0);
        adapter = new VideoDetailDialog_giftAdapter(context, videoDetail_dialogGiftBeans, selectList);
        adapter.setOnItemClickListener(this);
        lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_dialog_gift.setLayoutManager(lm);
        rv_dialog_gift.setAdapter(adapter);
        setVipData(videoDetail_dialogGiftBeans.get(0));
        checkBottom();
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = ScreenUtils.getScreenSize(context).x;
        lp.height = ScreenUtils.getScreenSize(context).y;
        win.setAttributes(lp);
        mcv_dialog_giftheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PersonalHomeActivity.class);
                intent.putExtra("userId", videoDetail_dialogGiftBeans.get(currentSelectIndex).getSender_id());
                if (TextUtils.equals(UserInfoUtil.getUserId(context), videoDetail_dialogGiftBeans.get(currentSelectIndex).getSender_id())) {
                    intent.putExtra("isSelf", true);
                } else {
                    intent.putExtra("isSelf", false);
                }
                intent.putExtra("activityType", 0);
                context.startActivity(intent);
            }
        });
    }

    /**
     * 初始化控件
     */
    public void initView() {
        tv_dialog_gifttitle = (TextView) findViewById(R.id.tv_dialog_gifttitle);
        iv_dialog_giftcancle = (ImageView) findViewById(R.id.iv_dialog_giftcancle);
        iv_dialog_giftrighttarrow = (ImageView) findViewById(R.id.iv_dialog_giftrighttarrow);
        iv_dialog_giftleftarrow = (ImageView) findViewById(R.id.iv_dialog_giftleftarrow);
        tv_dialog_giftvpout = (RelativeLayout) findViewById(R.id.tv_dialog_giftvpout);
        mcv_dialog_giftheader = (MyCircleView) findViewById(R.id.mcv_dialog_giftheader);
        iv_dialog_giftvip = (ImageView) findViewById(R.id.iv_dialog_giftvip);
        tv_dialog_giftname = (TextView) findViewById(R.id.tv_dialog_giftname);
        tv_dialog_giftnote = (TextView) findViewById(R.id.tv_dialog_giftnote);
        tv_dialog_giftfanscount = (TextView) findViewById(R.id.tv_dialog_giftfanscount);
        tv_dialog_giftattention = (TextView) findViewById(R.id.tv_dialog_giftattention);
        rl_dialog_bottomgift = (RelativeLayout) findViewById(R.id.rl_dialog_bottomgift);
        progress_bar = findViewById(R.id.progress_bar);
        rv_dialog_gift = (RecyclerView) findViewById(R.id.rv_dialog_gift);
        tv_dialog_gifttime = (TextView) findViewById(R.id.tv_dialog_gifttime);
        iv_dialog_gift = (ImageView) findViewById(R.id.iv_dialog_gift);
        iv_dialog_giftcancle.setOnClickListener(this);
        iv_dialog_giftleftarrow.setOnClickListener(this);
        iv_dialog_giftrighttarrow.setOnClickListener(this);
        tv_dialog_giftattention.setOnClickListener(this);
    }

    /**
     * 加关注&取消关注
     */
    private void setAttentionOrCancleAttention() {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("action_mark", !videoDetail_dialogGiftBeans.get(currentSelectIndex).isSender_attention_state() + "");
        requestParams.addParams("user_id", videoDetail_dialogGiftBeans.get(currentSelectIndex).getSender_id());
        tv_dialog_giftattention.setEnabled(false);
        tv_dialog_giftattention.setText(null);
        progress_bar.setVisibility(View.VISIBLE);
        MHHttpClient.getInstance().post(VideoDetailAttentionResponse.class, ConstantsValue.Url.ATTENTIONDO, requestParams, new MHHttpHandler<VideoDetailAttentionResponse>() {
            @Override
            public void onSuccess(VideoDetailAttentionResponse response) {
                tv_dialog_giftattention.setEnabled(true);
                setAttentionBtnState(!videoDetail_dialogGiftBeans.get(currentSelectIndex).isSender_attention_state());
                setAttentionState();
            }

            @Override
            public void onFailure(String content) {
                tv_dialog_giftattention.setEnabled(true);
                setAttentionBtnState(videoDetail_dialogGiftBeans.get(currentSelectIndex).isSender_attention_state());
            }

            @Override
            public void onStatusIsError(String message) {
                super.onStatusIsError(message);
                tv_dialog_giftattention.setEnabled(true);
                setAttentionBtnState(videoDetail_dialogGiftBeans.get(currentSelectIndex).isSender_attention_state());
            }
        });
    }

    /**
     * 更改所有被关注过的人和取消关注的人的关注状态
     */
    private void setAttentionState() {
        String sender_id = videoDetail_dialogGiftBeans.get(currentSelectIndex).getSender_id();
        for (VideoDetail_dialogGiftBean videoDetail_dialogGiftBean : videoDetail_dialogGiftBeans) {
            if (sender_id != null
                    && videoDetail_dialogGiftBean != null
                    && TextUtils.equals(sender_id, videoDetail_dialogGiftBean.getSender_id())) {
                videoDetail_dialogGiftBean.setSender_attention_state(!videoDetail_dialogGiftBean.isSender_attention_state());
            }
        }
    }

    /**
     * 设置VIP信息
     *
     * @param videoDetail_dialogGiftBean
     */
    private void setVipData(VideoDetail_dialogGiftBean videoDetail_dialogGiftBean) {
        ImageLoader.getInstance().displayImage(videoDetail_dialogGiftBean.getSender_portrait_uri(), mcv_dialog_giftheader, DisplayOptionsUtils.getHeaderDefaultImageOptions());
        ImageLoader.getInstance().displayImage(videoDetail_dialogGiftBean.getGift_icon_uri(), iv_dialog_gift, DisplayOptionsUtils.getSilenceDisplayBuilder());
        tv_dialog_giftname.setText(videoDetail_dialogGiftBean.getSender_name());
        tv_dialog_giftnote.setText(videoDetail_dialogGiftBean.getSender_note());
        tv_dialog_gifttitle.setText(videoDetail_dialogGiftBean.getGift_name());
        tv_dialog_giftfanscount.setText("粉丝量:" + MHStringUtils.countFormat(videoDetail_dialogGiftBean.getSender_fans_count()));
//        tv_dialog_gifttime.setText(MHStringUtils.timeFormat(videoDetail_dialogGiftBean.getSend_gift_time()));
        tv_dialog_giftattention.setSelected(videoDetail_dialogGiftBean.isSender_attention_state());
        if (!TextUtils.isEmpty(videoDetail_dialogGiftBean.getSender_name())
                && TextUtils.equals(videoDetail_dialogGiftBean.getSender_id(), UserInfoUtil.getUserId(context))) {
            tv_dialog_giftattention.setVisibility(View.GONE);
        } else {
            tv_dialog_giftattention.setVisibility(View.VISIBLE);
        }
        if(!MHStringUtils.isEmpty(videoDetail_dialogGiftBean.getSender_type())
                &&Integer.parseInt(videoDetail_dialogGiftBean.getSender_type())>10){
            iv_dialog_giftvip.setVisibility(View.VISIBLE);
        } else {
            iv_dialog_giftvip.setVisibility(View.GONE);
        }
    }

    /**
     * 设置选中项并更新
     *
     * @param selectItemIndex 选中position
     */
    private void setSelcetedItem(int selectItemIndex) {
        lastSelectIndex = currentSelectIndex;
        currentSelectIndex = selectItemIndex;
        selectList.set(lastSelectIndex, 0);
        selectList.set(selectItemIndex, 1);
        ImageLoader.getInstance().displayImage(videoDetail_dialogGiftBeans.get(selectItemIndex).getGift_icon_uri(), iv_dialog_gift, DisplayOptionsUtils.getSilenceDisplayBuilder());
    }

    /**
     * 设置选中选中项记录集合
     *
     * @param size 新增加数量
     */
    private void setSelectList(int size) {
        for (int i = 0; i < size; i++) {
            selectList.add(0);
        }
    }


    /**
     * 设置关注按钮状态
     *
     * @param attentionState 当前关注状态
     */
    public void setAttentionBtnState(final boolean attentionState) {
        progress_bar.postDelayed(new Runnable() {
            @Override
            public void run() {
                progress_bar.setVisibility(View.GONE);
                tv_dialog_giftattention.setEnabled(true);
                tv_dialog_giftattention.setSelected(attentionState);
                if (attentionState) {
                    tv_dialog_giftattention.setText("已关注");
                } else {
                    tv_dialog_giftattention.setText("+关注");
                }
            }
        }, 300);
    }

    /**
     * 判断列表是否滑动到底部
     */
    private void checkBottom() {
        rv_dialog_gift.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                if (lm.findLastCompletelyVisibleItemPosition() == videoDetail_dialogGiftBeans.size() - 1 && hasMoreData) {
                    if (!isLoading) {
                        isLoading = true;
                        onAddDataListener.callBack();
                    }
                }
            }
        });
    }

    /**
     * 加载数据
     *
     * @param data 新增数据
     */
    public void addData(List<VideoDetail_dialogGiftBean> data) {
        videoDetail_dialogGiftBeans.addAll(data);
        setSelectList(data.size());
        adapter.notifyDataSetChanged();

    }

    /**
     * 设置加载状态
     *
     * @param isLoading
     */
    public void setLoadingState(boolean isLoading) {
        this.isLoading = isLoading;
    }

    /**
     * 设置是否有更多数据
     */
    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dialog_giftcancle://取消
                dismiss();
                break;
            case R.id.iv_dialog_giftleftarrow://左侧按钮
                if (currentSelectIndex != 0) {
                    setSelcetedItem(currentSelectIndex - 1);
                    setVipData(videoDetail_dialogGiftBeans.get(currentSelectIndex));
                    if (currentSelectIndex - 1 < lm.findFirstVisibleItemPosition() || currentSelectIndex - 1 > lm.findLastVisibleItemPosition()) {
                        lm.scrollToPosition(currentSelectIndex - 1);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.iv_dialog_giftrighttarrow://右侧按钮
                if (selectList.size() != 0 && currentSelectIndex != videoDetail_dialogGiftBeans.size() - 1) {
                    setSelcetedItem(currentSelectIndex + 1);
                    setVipData(videoDetail_dialogGiftBeans.get(currentSelectIndex));
                    if (currentSelectIndex + 1 < lm.findFirstVisibleItemPosition() || currentSelectIndex + 1 > lm.findLastVisibleItemPosition()) {
                        lm.scrollToPosition(currentSelectIndex + 1);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.tv_dialog_giftattention:
                setAttentionOrCancleAttention();
                break;
        }
    }

    /**
     * item点击回调
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        setSelcetedItem(position);
        setVipData(videoDetail_dialogGiftBeans.get(position));
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置请求数据监听
     *
     * @param onAddDataListener
     */
    public void setOnAddDataListerner(OnAddDataListener onAddDataListener) {
        this.onAddDataListener = onAddDataListener;
    }

    public void notifyDataSetChanged() {
        setAttentionBtnState(videoDetail_dialogGiftBeans.get(currentSelectIndex).isSender_attention_state());
    }

    /**
     * 添加数据回调接口
     */
    public interface OnAddDataListener {
        public void callBack();
    }
}
