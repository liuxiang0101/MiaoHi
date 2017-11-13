package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.InterlocutionDetailsActivity;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.activity.VideoRecorderActivity;
import com.haiqiu.miaohi.bean.ObserverQA;
import com.haiqiu.miaohi.bean.QA;
import com.haiqiu.miaohi.bean.VideoUploadInfo;
import com.haiqiu.miaohi.receiver.RefreshUploadEvent;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.DraftUtil;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.TimeFormatUtils;
import com.haiqiu.miaohi.utils.upload.UploadService;
import com.haiqiu.miaohi.view.MyCircleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

/**
 * 我的映答列表适配器
 * Created by ningl on 16/12/9.
 */
public class MyQAAdapter extends RecyclerView.Adapter<MyQAAdapter.AskMeViewHolder> {

    private Context context;
    private List<QA> data;
    private int qaType;
    private static final int MYANSWER = 0;      //我的回答
    private static final int MYQUESTION = 1;    //我的提问
    private static final int MYCIRCUSEE = 2;    //我的围观
    private HashMap<String, VideoUploadInfo> draftQuestionMap;

    public MyQAAdapter(Context context, List<QA> data) {
        this.context = context;
        this.data = data;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        draftQuestionMap = DraftUtil.getDraftQuestionMap();
    }

    @Override
    public AskMeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AskMeViewHolder(View.inflate(context, R.layout.item_qa, null));
    }

    @Override
    public void onBindViewHolder(final AskMeViewHolder holder, int position) {
        AskMeViewHolder askMeViewHolder = (AskMeViewHolder) holder;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(context), ViewGroup.LayoutParams.WRAP_CONTENT);
        holder.itemView.setLayoutParams(lp);
        final QA qa = data.get(position);
        holder.iv_myqaprivate.setVisibility(qa.is_private() ? View.VISIBLE : View.GONE);
        if (qa.getAnswer_user_type() > 10) {
            holder.iv_myqavip.setVisibility(View.VISIBLE);
        } else {
            holder.iv_myqavip.setVisibility(View.GONE);
        }
        holder.tv_myqatime.setText(qa.getAnswer_time_text());
        holder.tv_myqadescribe.setText(qa.getQuestion_content());
        holder.tv_myqacircuseecount.setText(qa.getObserve_count());

        switch (qaType) {
            case MYANSWER://我的回答
                //处理上传
                handleUpload(holder, position);
                ImageLoader.getInstance().displayImage(qa.getAnswer_user_portrait(), holder.mcv_myqaheader, DisplayOptionsUtils.getSilenceDisplayBuilder());
                holder.tv_myqaname.setText("我");
                if (draftQuestionMap.containsKey(qa.getQuestion_id()) && !qa.isUploadding()) {
                    //草稿箱中存在该数据并且是否正在上传 正在上传要将失败view设置GONE
                    holder.rl_itemqa_fail.setVisibility(View.VISIBLE);
                    holder.tv_myqaremaintime.setVisibility(View.GONE);
                    holder.tv_myqabanswer.setText("点击查看");
                    holder.tv_itemqafail_time.setText("还剩 " + TimeFormatUtils.getCountDownFormat((int)qa.getTime_remain()));
                    VideoUploadInfo uploadInfo = draftQuestionMap.get(qa.getQuestion_id());
                    if (uploadInfo.isUploadFail()) {
                        holder.tv_upload_info.setText("上传失败, 已存入草稿箱");
                    } else {
                        holder.tv_upload_info.setText("已存入草稿箱");
                    }
                } else {
                    holder.rl_itemqa_fail.setVisibility(View.GONE);
                    if (qa.getQuestion_state() == 10) {
                        //已回答
                        holder.tv_myqabanswer.setText("点击查看");
                        holder.tv_myqaremaintime.setVisibility(View.GONE);
                    } else if (qa.getQuestion_state() == 0) {
                        //未回答
                        holder.tv_myqabanswer.setText("点击回答");
                        holder.tv_myqaremaintime.setText("还剩 " + TimeFormatUtils.getCountDownFormat((int)qa.getTime_remain()));
                        holder.tv_myqaremaintime.setVisibility(View.VISIBLE);
                    }
                }
                holder.tv_myqabanswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (qa.getQuestion_state() == 10) {
                            context.startActivity(new Intent(context, InterlocutionDetailsActivity.class)
                                    .putExtra("question_id", qa.getQuestion_id()));
                        }
                    }
                });
                holder.tv_myqabanswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (qa.getQuestion_state() == 10) {
                            //已回答(点击查看)
                            context.startActivity(new Intent(context, InterlocutionDetailsActivity.class)
                                    .putExtra("question_id", qa.getQuestion_id()));
                        } else if (qa.getQuestion_state() == 0) {
                            //未回答（点击回答）
                            Intent intent = new Intent(context, VideoRecorderActivity.class);
                            VideoUploadInfo videoUploadInfo = new VideoUploadInfo();
                            videoUploadInfo.setFromInfo(VideoUploadInfo.FROM_ASK_AND_ANSWER);
                            videoUploadInfo.setQuestionEndTime(TimeFormatUtils.getCurrentTimeMillis_CH() + qa.getTime_remain());
                            videoUploadInfo.setQuestionId(qa.getQuestion_id());
                            videoUploadInfo.setUpLoadType(qa.is_private() ? 1 : 0);//0为私密的
                            videoUploadInfo.setQuestion_text(qa.getQuestion_content());
                            intent.putExtra("videoUploadInfo", videoUploadInfo);
                            context.startActivity(intent);
                        }
                    }
                });

                holder.tv_itemqareupload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //重新上传
                        Intent it = new Intent(context, UploadService.class);
                        VideoUploadInfo videoUploadInfo = draftQuestionMap.get(qa.getQuestion_id());
                        videoUploadInfo.setUploadState(VideoUploadInfo.FROM_ASK_AND_ANSWER);
                        it.putExtra("uploadTask", videoUploadInfo);
                        context.startService(it);
                    }
                });
                break;
            case MYQUESTION://我的提问
                ImageLoader.getInstance().displayImage(qa.getAnswer_user_portrait(), holder.mcv_myqaheader, DisplayOptionsUtils.getSilenceDisplayBuilder());
                holder.tv_myqaname.setText(qa.getAnswer_user_name());
                if (qa.getQuestion_state() == 10) {//已回答
                    holder.tv_myqaremaintime.setVisibility(View.GONE);
                    holder.tv_myqabanswer.setText("点击查看");
                    holder.tv_myqabanswer.setVisibility(View.VISIBLE);
                    holder.ll_myqajoin.setVisibility(View.VISIBLE);
                    Object obj = ((AskMeViewHolder) holder).itemView.getTag();
                    if (obj == null || !TextUtils.equals(qa.getQuestion_id(), ((String) obj))) {
                        addCircusee(holder.tv_myqacircuseecount, holder.ll_circusee, qa);
                    }
                    ((AskMeViewHolder) holder).itemView.setTag(qa.getQuestion_id());
//                    addCircusee(holder.tv_myqacircuseecount, holder.ll_circusee, qa);
                } else if (qa.getQuestion_state() == 0) {//未回答
                    holder.rl_myqabtn.setVisibility(View.GONE);
                    if (qa.getTime_remain() <= 0) {
                        //未回答超时
                        holder.tv_myqaremaintime.setText("超时未回答");
                        holder.tv_myqaremaintime.setTextColor(Color.parseColor("#ff4545"));
                    } else {
                        //未回答计时中
                        holder.tv_myqaremaintime.setText("还剩 " + TimeFormatUtils.getCountDownFormat((int)qa.getTime_remain()));
                        holder.tv_myqaremaintime.setTextColor(Color.parseColor("#c4c4c4"));
                    }
                    holder.tv_myqaremaintime.setVisibility(View.VISIBLE);
                    holder.tv_myqabanswer.setVisibility(View.GONE);
                    holder.ll_myqajoin.setVisibility(View.GONE);
                }
                holder.tv_myqabanswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (qa.getQuestion_state() == 10) {
                            context.startActivity(new Intent(context, InterlocutionDetailsActivity.class)
                                    .putExtra("question_id", qa.getQuestion_id()));
                        }
                    }
                });
                holder.mcv_myqaheader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userId = qa.getAnswer_user_id();
                        if (MHStringUtils.isEmpty(userId)) return;

                        context.startActivity(new Intent(context, PersonalHomeActivity.class)
                                .putExtra("userId", userId));
                    }
                });
                break;

            case MYCIRCUSEE://我的围观
                ImageLoader.getInstance().displayImage(qa.getAnswer_user_portrait(), holder.mcv_myqaheader, DisplayOptionsUtils.getSilenceDisplayBuilder());
                holder.tv_myqaname.setText(qa.getAnswer_user_name());
                holder.tv_myqabanswer.setText("点击查看");
                //处理频繁刷新列表 加载图片闪烁问题
                Object obj = ((AskMeViewHolder) holder).itemView.getTag();
                if (obj == null || !TextUtils.equals(qa.getQuestion_id(), ((String) obj))) {
                    addCircusee(holder.tv_myqacircuseecount, holder.ll_circusee, qa);
                }
                ((AskMeViewHolder) holder).itemView.setTag(qa.getQuestion_id());
                holder.tv_myqabanswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, InterlocutionDetailsActivity.class)
                                .putExtra("question_id", qa.getQuestion_id()));
                    }
                });
                holder.mcv_myqaheader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userId = qa.getAnswer_user_id();
                        if (MHStringUtils.isEmpty(userId)) return;

                        context.startActivity(new Intent(context, PersonalHomeActivity.class)
                                .putExtra("userId", userId));
                    }
                });

                holder.rl_myqabtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, InterlocutionDetailsActivity.class)
                                .putExtra("question_id", qa.getQuestion_id()));
                    }
                });

                if (qa.is_question_owner()) {
                    //点击查看
                    holder.tv_myqabanswer.setVisibility(View.VISIBLE);
                    holder.rl_myqabtn.setVisibility(View.INVISIBLE);
                    holder.rl_myqabtn.setEnabled(false);
                    holder.tv_myqaremaintime.setVisibility(View.GONE);
                } else {
                    if (qa.isTemporary_free()) {
                        //限时免费
                        holder.tv_myqabanswer.setVisibility(View.GONE);
                        holder.rl_myqabtn.setVisibility(View.VISIBLE);
                        holder.tv_myqaremaintime.setVisibility(View.VISIBLE);
                        holder.rl_myqabtn.setEnabled(true);
                        if (qa.getTime_remain() < 1000) {
//                            holder.tv_myqaremaintime.setText("还剩 00:00:00");
                            qa.setIs_question_owner(false);
                            qa.setTemporary_free(false);
                            holder.tv_myqaremaintime.setVisibility(View.VISIBLE);
                        } else if (qa.getTime_remain() < (3600 * 1000)) {
                            //剩余时间少于一个小时 则显示倒计时
                            holder.tv_myqaremaintime.setText("还剩 " + TimeFormatUtils.getCountDownFormat((int) qa.getTime_remain()));
                            holder.tv_myqaremaintime.setVisibility(View.VISIBLE);
                            holder.rl_myqabtn.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
                            holder.tv_myqabtntext.setText(CommonUtil.formatPrice2(qa.getObserve_price()) + "元围观");
                        } else {
                            holder.tv_myqaremaintime.setVisibility(View.GONE);
                        }
                    } else {
                        //xx元围观
                        holder.rl_myqabtn.setEnabled(true);
                        holder.tv_myqabanswer.setVisibility(View.GONE);
                        holder.rl_myqabtn.setVisibility(View.VISIBLE);
                        holder.tv_myqaremaintime.setVisibility(View.GONE);
                        holder.rl_myqabtn.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
                        holder.tv_myqabtntext.setText(qa.getObserve_price_text());
                    }
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    /**
     * 设置映答类型
     *
     * @param qaType
     */
    public void setQaType(int qaType) {
        this.qaType = qaType;
    }

    /**
     * 添加围观人头像
     *
     * @param tv 围观数
     * @param ll 围观头像容器
     * @param qa 数据源
     */
    public void addCircusee(TextView tv, LinearLayout ll, final QA qa) {
//        if(ll.getChildCount()!=0) return;
        ll.removeAllViews();
        final List<ObserverQA> circusees = qa.getObserver_list();
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv.measure(w, h);
        int width = tv.getMeasuredWidth();
        int circuseeWidth = ScreenUtils.getScreenWidth(context) - width - DensityUtil.dip2px(context, 87);
        int circuseeCount = circuseeWidth / (DensityUtil.dip2px(context, 30));
        int count;
        if (circusees.size() == circuseeCount) count = circuseeCount;
        else {
            count = Math.min(circuseeCount, circusees.size());
        }
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.width = DensityUtil.dip2px(context, 25);
            lp.height = DensityUtil.dip2px(context, 25);
            lp.setMargins(0, 0, DensityUtil.dip2px(context, 5), 0);
            MyCircleView circleView = new MyCircleView(context);
            circleView.setLayoutParams(lp);
            circleView.setImageResource(R.drawable.head_default);
            ImageLoader.getInstance().displayImage(circusees.get(i).getObserver_portrait(), circleView, DisplayOptionsUtils.getSilenceDisplayBuilder());
            circleView.setTag(circusees.get(i).getObserver_portrait());

            ll.addView(circleView);
            final int position = i;
            circleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId = circusees.get(position).getObserver_id();
                    context.startActivity(new Intent(context, PersonalHomeActivity.class)
                            .putExtra("userId", userId));
                }
            });
        }

    }


    /**
     * 问我的
     */
    public class AskMeViewHolder extends RecyclerView.ViewHolder {
        private MyCircleView mcv_myqaheader;
        private LinearLayout ll_myqajoin;
        private ImageView iv_myqaprivate;
        private ImageView iv_myqavip;
        private TextView tv_myqaname;
        private TextView tv_myqatime;
        private TextView tv_myqadescribe;
        private TextView tv_myqacircuseecount;
        private TextView tv_myqabanswer;
        private TextView tv_myqaremaintime;
        private LinearLayout ll_circusee;
        private RelativeLayout rl_itemqa_on;
        private TextView tv_itemqa_percent;
        private ProgressBar pb_itemqa_progressbar;
        private RelativeLayout rl_itemqa_fail;
        private TextView tv_itemqareupload;
        private TextView tv_itemqafail_time;
        private TextView tv_upload_info;

        private RelativeLayout rl_myqabtn;
        private TextView tv_myqabtntext;

        public AskMeViewHolder(View itemView) {
            super(itemView);
            mcv_myqaheader = (MyCircleView) itemView.findViewById(R.id.mcv_myqaheader);
            ll_myqajoin = (LinearLayout) itemView.findViewById(R.id.ll_myqajoin);
            iv_myqaprivate = (ImageView) itemView.findViewById(R.id.iv_myqaprivate);
            iv_myqavip = (ImageView) itemView.findViewById(R.id.iv_myqavip);
            tv_myqadescribe = (TextView) itemView.findViewById(R.id.tv_myqadescribe);
            tv_myqacircuseecount = (TextView) itemView.findViewById(R.id.tv_myqacircuseecount);
            tv_myqatime = (TextView) itemView.findViewById(R.id.tv_myqatime);
            tv_myqabanswer = (TextView) itemView.findViewById(R.id.tv_myqabanswer);
            tv_myqaremaintime = (TextView) itemView.findViewById(R.id.tv_myqaremaintime);
            tv_myqaname = (TextView) itemView.findViewById(R.id.tv_myqaname);
            ll_circusee = (LinearLayout) itemView.findViewById(R.id.ll_circusee);
            rl_itemqa_on = (RelativeLayout) itemView.findViewById(R.id.rl_itemqa_on);
            tv_itemqa_percent = (TextView) itemView.findViewById(R.id.tv_itemqa_percent);
            pb_itemqa_progressbar = (ProgressBar) itemView.findViewById(R.id.pb_itemqa_progressbar);
            rl_itemqa_fail = (RelativeLayout) itemView.findViewById(R.id.rl_itemqa_fail);
            tv_itemqareupload = (TextView) itemView.findViewById(R.id.tv_itemqareupload);
            tv_itemqafail_time = (TextView) itemView.findViewById(R.id.tv_itemqafail_time);
            tv_upload_info = (TextView) itemView.findViewById(R.id.tv_upload_info);

            rl_myqabtn = (RelativeLayout) itemView.findViewById(R.id.rl_myqabtn);
            tv_myqabtntext = (TextView) itemView.findViewById(R.id.tv_myqabtntext);
        }
    }

    /**
     * 解除绑定
     */
    public void unRegistEvent() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 刷新上传进度
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshUpdateCountEvent(RefreshUploadEvent event) {
        //处理问我的
        if (qaType != MYANSWER) return;
        //处理上传刷新
        if (event == null) return;
        if (event.getTask().getQuestionId() == null) return;
        String question_id = event.getTask().getQuestionId();
        double progress = event.getTask().getProsess();
        int uploadType = event.getTask().getUpLoadType();
        int uploadState = event.getTask().getUploadState();
        for (int i = 0; i < data.size(); i++) {
            QA qa = data.get(i);
            if (TextUtils.equals(question_id, qa.getQuestion_id())) {
                //校验是否是当前item
                qa.setUploadState(uploadState);
                qa.setProgress(progress);
                qa.setUploadType(uploadType);
                if (event.getTask().getUploadState() == VideoUploadInfo.UPLOAD_FAILE) {
                    //上传失败存入草稿箱
                    VideoUploadInfo videoUploadInfo = event.getTask();
                    videoUploadInfo.setUploadFail(true);
                    qa.setUploadState(VideoUploadInfo.UPLOAD_FAILE);
                    DraftUtil.saveDraft(videoUploadInfo);
                    draftQuestionMap.put(videoUploadInfo.getQuestionId(), videoUploadInfo);
                }
            }

        }
        notifyDataSetChanged();
    }

    /**
     * 处理上传开始、走进度、成功、失败
     *
     * @param holder
     * @param position
     */
    private void handleUpload(final AskMeViewHolder holder, int position) {
        QA qa = data.get(position);
        switch (qa.getUploadState()) {
            case VideoUploadInfo.UPLOAD_PRE: //开始上传
                holder.rl_itemqa_on.setVisibility(View.VISIBLE);
                holder.tv_itemqa_percent.setText("0%");
                holder.pb_itemqa_progressbar.setProgress(0);
                holder.rl_itemqa_fail.setVisibility(View.GONE);
                qa.setUploadding(true);
                break;

            case VideoUploadInfo.UPLOAD_PROGRESS: //走进度
                holder.rl_itemqa_on.setVisibility(View.VISIBLE);
                holder.tv_itemqa_percent.setText(qa.getProgress() + "%");
                holder.pb_itemqa_progressbar.setProgress((int)qa.getProgress());
                holder.rl_itemqa_fail.setVisibility(View.GONE);
                qa.setUploadding(true);
                break;

            case VideoUploadInfo.UPLOAD_SUCCESS: //上传成功
                holder.tv_itemqa_percent.setText("100%");
                holder.pb_itemqa_progressbar.setProgress(100);
                qa.setQuestion_state(10);
                holder.rl_itemqa_on.setVisibility(View.GONE);
                draftQuestionMap.remove(qa.getQuestion_id());
                holder.rl_itemqa_fail.setVisibility(View.GONE);
                holder.tv_myqatime.setText("刚刚回答了");
                qa.setUploadding(true);
                break;

            case VideoUploadInfo.UPLOAD_FAILE: //上传失败
                MHLogUtil.i("上传失败", "上传失败了！");
                holder.rl_itemqa_on.setVisibility(View.GONE);
                holder.rl_itemqa_fail.setVisibility(View.VISIBLE);
                qa.setUploadding(false);
                break;
            default:
                holder.rl_itemqa_on.setVisibility(View.GONE);
                holder.rl_itemqa_fail.setVisibility(View.GONE);
                qa.setUploadding(false);
                break;
        }
    }


    /**
     * 刷新数据
     */
    public void refreshData() {
        int removeCount = 0;
        for (int i = 0; i < data.size(); i++) {
            int index = i - removeCount;
            if (data.get(index).getTime_remain() <= 0) {
                if (qaType == MYANSWER && data.get(i).getQuestion_state() == 0) {
                    //我的回答需移除数据并且是未回答状态
                    draftQuestionMap.remove(data.get(index).getQuestion_id());
                    data.remove(index);
                    removeCount++;
                }
            } else {
                QA qa = data.get(index);
                if (qa.getTime_remain() <= 1000) {
                    qa.setTime_remain(0);
                } else {
                    qa.setTime_remain(qa.getTime_remain() - 1000);
                }
            }
        }
        notifyDataSetChanged();
    }
}
