package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.ActiveDetailActivity;
import com.haiqiu.miaohi.activity.InterlocutionDetailsActivity;
import com.haiqiu.miaohi.activity.LoginActivity;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.activity.VideoDetailActivity;
import com.haiqiu.miaohi.bean.HomeStaggereedgridResult;
import com.haiqiu.miaohi.receiver.RefreshGiftCountEvent;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.ImageLoadingConfig;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.UserInfoUtil;
import com.haiqiu.miaohi.view.MyCircleView;
import com.haiqiu.miaohi.widget.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tendcloud.tenddata.TCAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 瀑布流适配器
 * Created by ningl on 2016/6/17.
 */
public class HomeStaggeredgideAdapter extends BaseAdapter {

    private Context context;
    private List<HomeStaggereedgridResult> homeStaggereedgridResults;
    private int pos;//记录当前选中的位置

    public String kind_tag;


    DisplayImageOptions options = ImageLoadingConfig.displayImageOptions(R.color.color_f1);

    public HomeStaggeredgideAdapter(Context context, List<HomeStaggereedgridResult> homeStaggereedgridResults, String kind_tag) {
        this.context = context;
        this.homeStaggereedgridResults = homeStaggereedgridResults;
        this.kind_tag = kind_tag;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public int getCount() {
        return (null == homeStaggereedgridResults) ? 0 : homeStaggereedgridResults.size();
    }

    @Override
    public Object getItem(int position) {
        return (null == homeStaggereedgridResults) ? null : homeStaggereedgridResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_homestaggeredgrid, null);
            holder.sriv_homevideo = (SelectableRoundedImageView) convertView.findViewById(R.id.sriv_homevideo);
            holder.tv_homeitem_time = (TextView) convertView.findViewById(R.id.tv_homeitem_time);
            holder.tv_homeite_gift = (TextView) convertView.findViewById(R.id.tv_homeite_gift);
            holder.tv_homeitem_zan = (TextView) convertView.findViewById(R.id.tv_homeitem_zan);
            holder.tv_homeitem_videodescribe = (TextView) convertView.findViewById(R.id.tv_homeitem_videodescribe);
            holder.tv_homeitem_name = (TextView) convertView.findViewById(R.id.tv_homeitem_name);
            holder.iv_homeitem_vip = (ImageView) convertView.findViewById(R.id.iv_homeitem_vip);
            holder.iv_right_icon = (ImageView) convertView.findViewById(R.id.iv_right_icon);
            holder.iv_prise_state = (ImageView) convertView.findViewById(R.id.iv_prise_state);
            holder.mcv_homeitem_header = (MyCircleView) convertView.findViewById(R.id.mcv_homeitem_header);
            holder.ll_element_bg = convertView.findViewById(R.id.ll_element_bg);
            holder.ll_root = convertView.findViewById(R.id.ll_root);
            holder.rl_root = convertView.findViewById(R.id.rl_root);
            holder.tv_person_count = (TextView) convertView.findViewById(R.id.tv_person_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        pos = position;
        final HomeStaggereedgridResult homeStaggereedgridResult = homeStaggereedgridResults.get(position);
        if (null != homeStaggereedgridResult) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            int width = (ScreenUtils.getScreenSize(context).x - (3 * DensityUtil.dip2px(context, 10))) / 2;
            lp.width = width - DensityUtil.dip2px(context, 2);
            lp.height = width - DensityUtil.dip2px(context, 2);
            holder.sriv_homevideo.setLayoutParams(lp);
            ImageLoader.getInstance().displayImage(homeStaggereedgridResult.getPortrait_uri(), holder.mcv_homeitem_header, DisplayOptionsUtils.getHeaderDefaultImageOptions());
            ImageLoader.getInstance().displayImage(homeStaggereedgridResult.getCover_uri(), holder.sriv_homevideo, options, new AnimateFirstDisplayListener(homeStaggereedgridResult.getCover_uri(), holder.sriv_homevideo));
            holder.tv_homeitem_time.setText(MHStringUtils.timeFormat(homeStaggereedgridResult.getDuration_second()));
            holder.tv_homeitem_zan.setText(homeStaggereedgridResult.getPraise_count());
            holder.tv_homeite_gift.setText(MHStringUtils.countFormat(homeStaggereedgridResult.getGift_count()));
            holder.tv_homeitem_name.setText(homeStaggereedgridResult.getUser_name());
            holder.iv_prise_state.setSelected(homeStaggereedgridResult.isPraise_state());


            GradientDrawable drawable = (GradientDrawable) holder.ll_element_bg.getBackground();
            //element_type:元素类型 1为视频  2为专题卡片 3为映答卡片
            if (!TextUtils.isEmpty(homeStaggereedgridResult.getElement_type())) {
                switch (homeStaggereedgridResult.getElement_type()) {

                    case "1":

                        holder.ll_root.setVisibility(View.VISIBLE);
                        holder.tv_person_count.setVisibility(View.GONE);
                        holder.iv_right_icon.setVisibility(View.GONE);
                        holder.rl_root.setVisibility(View.VISIBLE);

                        holder.tv_homeitem_videodescribe.setTextColor(0xff666666);

                        drawable.setColor(Color.WHITE);
                        holder.ll_element_bg.setBackgroundDrawable(drawable);

                        holder.tv_homeitem_videodescribe.setText(homeStaggereedgridResult.getVideo_note());

                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(context, VideoDetailActivity.class);
                                intent.putExtra("video_id", homeStaggereedgridResult.getVideo_id());
                                intent.putExtra("kind_tag", kind_tag);
                                intent.putExtra("positon", pos);
                                context.startActivity(intent);
                            }
                        });

                        break;
                    case "2":
                        holder.ll_root.setVisibility(View.GONE);
                        holder.tv_person_count.setVisibility(View.VISIBLE);
                        holder.iv_right_icon.setVisibility(View.GONE);
                        holder.tv_homeitem_videodescribe.setTextColor(context.getResources().getColor(R.color.white70));
                        holder.tv_homeitem_videodescribe.setText(homeStaggereedgridResult.getActivity_note());
                        holder.rl_root.setVisibility(View.GONE);

                        holder.tv_person_count.setText("作品：" + CommonUtil.formatCount(homeStaggereedgridResult.getVideo_count()));


                        if (!TextUtils.isEmpty(homeStaggereedgridResult.getBackground_color())) {
                            drawable.setColor(Color.parseColor(homeStaggereedgridResult.getBackground_color()));
                            holder.ll_element_bg.setBackgroundDrawable(drawable);
                        }

                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TCAgent.onEvent(context, "专题tab页内的专题卡片点击次数");
                                Intent intent = new Intent(context, ActiveDetailActivity.class);
                                intent.putExtra("activityId", homeStaggereedgridResult.getActivity_id());
                                intent.putExtra("activity_name", homeStaggereedgridResult.getActivity_note());
                                intent.putExtra("coverid", homeStaggereedgridResult.getCover_uri());
                                context.startActivity(intent);
                            }
                        });
                        break;
                    case "3":
                        holder.ll_root.setVisibility(View.GONE);
                        holder.tv_person_count.setVisibility(View.GONE);
                        holder.iv_right_icon.setVisibility(View.VISIBLE);
                        holder.rl_root.setVisibility(View.VISIBLE);

                        drawable.setColor(Color.WHITE);
                        holder.ll_element_bg.setBackgroundDrawable(drawable);

                        holder.tv_homeitem_videodescribe.setTextColor(0xff666666);
                        holder.tv_homeitem_videodescribe.setText(homeStaggereedgridResult.getQuestion_text());

                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, InterlocutionDetailsActivity.class);
                                intent.putExtra("question_id", homeStaggereedgridResult.getQuestion_id());
                                context.startActivity(intent);
                            }
                        });

                        break;
                    default:
                        break;
                }
            } else {

                holder.ll_root.setVisibility(View.VISIBLE);
                holder.tv_person_count.setVisibility(View.GONE);
                holder.iv_right_icon.setVisibility(View.GONE);
                holder.rl_root.setVisibility(View.VISIBLE);

                holder.tv_homeitem_videodescribe.setTextColor(0xff666666);

                drawable.setColor(Color.WHITE);
                holder.ll_element_bg.setBackgroundDrawable(drawable);

                holder.tv_homeitem_videodescribe.setText(homeStaggereedgridResult.getVideo_note());


                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VideoDetailActivity.class);
                        intent.putExtra("video_id", homeStaggereedgridResult.getVideo_id());
                        intent.putExtra("kind_tag", kind_tag);
                        context.startActivity(intent);
                    }
                });
            }


            //是否是大咖
            if (!TextUtils.isEmpty(homeStaggereedgridResult.getUser_type()) && !TextUtils.equals("null", homeStaggereedgridResult.getUser_type())) {
                if (Integer.parseInt(homeStaggereedgridResult.getUser_type()) > 10) {
                    holder.iv_homeitem_vip.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_homeitem_vip.setVisibility(View.INVISIBLE);
                }
            }

            holder.mcv_homeitem_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLogin()) {
                        Intent intent = new Intent(context, PersonalHomeActivity.class);
                        intent.putExtra("userId", homeStaggereedgridResult.getUser_id());
                        if (TextUtils.equals(UserInfoUtil.getUserId(context), homeStaggereedgridResult.getUser_id())) {
                            intent.putExtra("isSelf", true);
                        } else {
                            intent.putExtra("isSelf", false);
                        }
                        intent.putExtra("activityType", 0);
                        context.startActivity(intent);
                    } else {
                        skipLogin();
                    }


                }
            });
        }
        return convertView;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public static class ViewHolder {
        SelectableRoundedImageView sriv_homevideo;
        TextView tv_homeitem_time;
        TextView tv_homeitem_zan;
        TextView tv_homeite_gift;
        TextView tv_homeitem_videodescribe;
        TextView tv_homeitem_name;
        ImageView iv_homeitem_vip;
        ImageView iv_right_icon;
        ImageView iv_prise_state;
        MyCircleView mcv_homeitem_header;
        View ll_element_bg;
        View ll_root;
        View rl_root;
        TextView tv_person_count;
    }

    /**
     * 是否登陆
     *
     * @return
     */
    public boolean isLogin() {
        return !MHStringUtils.isEmpty(SpUtils.getString(ConstantsValue.Sp.TOKEN_MIAOHI));
    }

    /**
     * 跳转到登陆
     */
    public void skipLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }


    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        public static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        private final String imageUri;
        private final ImageView imageView;

        AnimateFirstDisplayListener(String imageUri, ImageView imageView) {
            this.imageUri = imageUri;
            this.imageView = imageView;
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (true) {

                    AlphaAnimation fadeImage = new AlphaAnimation(0.3f, 1f);
                    fadeImage.setDuration(600);
                    fadeImage.setInterpolator(new DecelerateInterpolator());
                    imageView.startAnimation(fadeImage);

                    displayedImages.add(imageUri);
                }

            }
        }
    }

    /**
     * 刷新礼物
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshGiftCountEvent(RefreshGiftCountEvent event) {

        if (event.getVideoid() == null) return;
        for (HomeStaggereedgridResult homeStaggereedgridResult : homeStaggereedgridResults) {
            String videoId = homeStaggereedgridResult.getVideo_id();
            if (videoId != null) {
                if (TextUtils.equals(event.getVideoid(), videoId)) {
                    homeStaggereedgridResult.setGift_count(event.getCurrentCount() + "");
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * eventbus解除绑定
     */
    public void unRegistEventbus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}
