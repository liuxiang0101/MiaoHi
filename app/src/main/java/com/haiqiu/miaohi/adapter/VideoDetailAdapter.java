package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.Notify_user_result;
import com.haiqiu.miaohi.bean.TextInfo;
import com.haiqiu.miaohi.bean.VideoDetailUserCommentBean;
import com.haiqiu.miaohi.bean.VideoItemPageResult;
import com.haiqiu.miaohi.utils.AbstractTextUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.TextUtil;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.view.MyCircleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频详情适配器
 * Created by ningl on 2016/6/22.
 */
public class VideoDetailAdapter extends BaseAdapter {

    private BaseActivity context;
    private List<VideoDetailUserCommentBean> videoDetailUserCommentBeans;
    private OnCommentItemClick onCommentItemClick;
    private OnCommentLongClick onCommentLongClick;

    public VideoDetailAdapter(BaseActivity context, List<VideoDetailUserCommentBean> videoDetailUserCommentBeans, OnCommentLongClick onCommentLongClick) {
        this.context = context;
        this.videoDetailUserCommentBeans = videoDetailUserCommentBeans;
        this.onCommentLongClick = onCommentLongClick;
    }

    @Override
    public int getCount() {
        return videoDetailUserCommentBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return videoDetailUserCommentBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_videodetail, null);
            holder.mcv_videodetailcomment_header = (MyCircleView) convertView.findViewById(R.id.mcv_videodetailcomment_header);
            holder.tv_videodetailcomment_name = (TextView) convertView.findViewById(R.id.tv_videodetailcomment_name);
            holder.tv_videodetailcomment_note = (TextView) convertView.findViewById(R.id.tv_videodetailcomment_note);
            holder.tv_videodetail_time = (TextView) convertView.findViewById(R.id.tv_videodetail_time);
            holder.iv_videodetail_commentvip = (ImageView) convertView.findViewById(R.id.iv_videodetail_commentvip);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final VideoDetailUserCommentBean videoDetailUserCommentBean = videoDetailUserCommentBeans.get(position);
        if(videoDetailUserCommentBean.getSize() != -1){
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
            lp.height = videoDetailUserCommentBean.getSize();
            convertView.setLayoutParams(lp);
            return convertView;
        } else {
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(lp);
        }
        String headerUri = null;
        if(videoDetailUserCommentBean.getComment_user_portrait_uri()!=null&&videoDetailUserCommentBean.getComment_user_portrait_uri().contains("?")){
            headerUri = videoDetailUserCommentBean.getComment_user_portrait_uri() + "&" + ConstantsValue.Other.HEADER_PARAM;
        } else {
            headerUri = videoDetailUserCommentBean.getComment_user_portrait_uri() + "?" + ConstantsValue.Other.HEADER_PARAM;
        }
        ImageLoader.getInstance().displayImage(headerUri, holder.mcv_videodetailcomment_header, DisplayOptionsUtils.getHeaderDefaultImageOptions());
        holder.tv_videodetailcomment_name.setText(videoDetailUserCommentBean.getComment_user_name());
        holder.tv_videodetail_time.setText(videoDetailUserCommentBean.getComment_time_text());
        //是否是大咖
        if(!TextUtils.isEmpty(videoDetailUserCommentBean.getComment_user_type())&&!TextUtils.equals("null", videoDetailUserCommentBean.getComment_user_type())){
            if(Integer.parseInt(videoDetailUserCommentBean.getComment_user_type())>10){
                holder.iv_videodetail_commentvip.setVisibility(View.VISIBLE);
                holder.tv_videodetailcomment_name.setTextColor(Color.parseColor("#FE6600"));
            } else {
                holder.iv_videodetail_commentvip.setVisibility(View.GONE);
                holder.tv_videodetailcomment_name.setTextColor(Color.parseColor("#1d1d1d"));
            }
        } else {
            holder.iv_videodetail_commentvip.setVisibility(View.GONE);
            holder.tv_videodetailcomment_name.setTextColor(Color.parseColor("#8497a2"));
        }

        String commentNote = videoDetailUserCommentBean.getComment_text();
        if(commentNote!=null){
            commentNote = commentNote.replaceAll("\\s+"," ").replaceAll("\\n", " ");
        }

        VideoItemPageResult pageResult = new VideoItemPageResult();
        pageResult.setVideo_note(commentNote);
        pageResult.setNotify_user_result(videoDetailUserCommentBean.getNotify_user_result());
        holder.tv_videodetailcomment_note.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;
                CharSequence text = ((TextView) v).getText();
                Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
                TextView widget = (TextView) v;
                int action = event.getAction();

                if (action == MotionEvent.ACTION_UP ||
                        action == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    x -= widget.getTotalPaddingLeft();
                    y -= widget.getTotalPaddingTop();

                    x += widget.getScrollX();
                    y += widget.getScrollY();

                    Layout layout = widget.getLayout();
                    int line = layout.getLineForVertical(y);
                    int off = layout.getOffsetForHorizontal(line, x);

                    ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);

                    if (link.length != 0) {
                        if (action == MotionEvent.ACTION_UP) {
                            link[0].onClick(widget);
                        }
                        ret = true;
                    }
                }
                return ret;
            }
        });
        if(pageResult.getNotify_user_result()== null){
            List<Notify_user_result> notify_user_results = new ArrayList<>();
            pageResult.setNotify_user_result(notify_user_results);
        }
        SpannableStringBuilder desciveSpn = TextUtil.getInstance().handleVideoDescribe(pageResult, new AbstractTextUtil() {
            @Override
            public void onClickSpan(TextInfo textInfo) {
                if(!context.isLogin(false)){

                } else {
                    skipPersonalCenter(context, textInfo.getTarget());
                }
            }
        });
        //判断是回复还是评论
        if(TextUtils.equals(videoDetailUserCommentBean.getRoot_id(), videoDetailUserCommentBean.getCommented_id())){//评论视频
            holder.tv_videodetailcomment_note.setText(desciveSpn);
        } else {//回复
            TextInfo textInfo = new TextInfo();
            textInfo.setOriginalStr("回复@"+videoDetailUserCommentBean.getCommented_user_name()+":");
            textInfo.setStart(2);
            textInfo.setEnd(2+videoDetailUserCommentBean.getCommented_user_name().length()+1);
            textInfo.setTarget(videoDetailUserCommentBean.getCommented_user_id());
            textInfo.setColor(Color.parseColor("#00a0e9"));
            SpannableStringBuilder replySpan = TextUtil.getInstance().setClickAndColor(textInfo, new AbstractTextUtil() {
                @Override
                public void onClickSpan(TextInfo textInfo) {
                    if(!context.isLogin(false)){

                    } else {
                        skipPersonalCenter(context, textInfo.getTarget());
                    }
                }
            });
            SpannableStringBuilder resultSpan = replySpan.append(desciveSpn);
            holder.tv_videodetailcomment_note.setText(resultSpan);
        }

//        //是自己的评论可以删除
//        if(videoDetailUserCommentBean.getComment_user_id()!=null){
//            if(!TextUtils.equals(UserInfoUtil.getUserId(context), videoDetailUserCommentBean.getComment_user_id())){
//                convertView.setTag(R.string.isSwipe, false);
//            } else {
//                convertView.setTag(R.string.isSwipe, true);
//            }
//        } else{
//            convertView.setTag(R.string.isSwipe, false);
//        }
        convertView.setTag(R.string.isSwipe, false);
        holder.mcv_videodetailcomment_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!context.isLogin(false)) {
                    return;
                }
                Intent intent = new Intent(context, PersonalHomeActivity.class);
                intent.putExtra("userId", videoDetailUserCommentBean.getComment_user_id());
                context.startActivity(intent);
            }
        });
        //长按删除
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onCommentLongClick!=null) {
                    if(UserInfoUtil.isMyself(context, videoDetailUserCommentBean.getComment_user_id())){
                        onCommentLongClick.onLongClick(position);
                    }
                }
                return true;
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onCommentLongClick!=null) onCommentLongClick.onClick(position);
            }
        });
        return convertView;
    }

    public class ViewHolder{
        MyCircleView mcv_videodetailcomment_header;
        TextView tv_videodetailcomment_name;
        TextView tv_videodetailcomment_note;
        TextView tv_videodetail_time;
        ImageView iv_videodetail_commentvip;
    }

    /**
     * 设置@好友颜色
     */
    public String getAtFrientColor(String str){
        if(str!=null){
            String result = "";
            String[] names = str.split(" ");
            for(int i = 0; i<names.length; i++){
                if(names[i].startsWith("@")){
                    result+="<font color='#00a0e9'>"+names[i]+"</font>";
                } else {
                    result+=" "+names[i];
                }
            }
            return result;
        }
        return "";
    }

    /**
     * 跳转到个人中心
     *
     * @param context
     * @param userId
     */
    private void skipPersonalCenter(Context context, String userId) {
        Intent intent = new Intent(context, PersonalHomeActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    public interface OnCommentItemClick {
        void OnItemClick(int position);
    }

    public void setOnCommentItemClickListener(OnCommentItemClick onCommentItemClick){
        this.onCommentItemClick = onCommentItemClick;
    }

    public interface OnCommentLongClick{
        void onLongClick(int position);
        void onClick(int position);
    }
}
