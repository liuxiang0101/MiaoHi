package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.InterlocutionDetailsActivity;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.activity.VideoAndImgActivity;
import com.haiqiu.miaohi.activity.WebViewActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.BannerSubjectBean;
import com.haiqiu.miaohi.bean.DiscoveryBannerObj;
import com.haiqiu.miaohi.bean.VideoAndImg;
import com.haiqiu.miaohi.utils.BehaviourStatistic;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHLogUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhandalin 2015年09月10日 14:21.
 * 最后修改者: zhandalin  version 1.0
 * 说明:轮播图的Adapter
 */
public class CarouselAdapter extends PagerAdapter {
    private List<DiscoveryBannerObj> carousel_data;
    private Context context;
    private View reuseView;
    private final ImageLoader imageLoader;
    private Gson gson;

    public CarouselAdapter(final Context context, List<DiscoveryBannerObj> carousel_data) {
        this.context = context;
        this.carousel_data = carousel_data;
        this.imageLoader = ImageLoader.getInstance();
        this.gson = new Gson();
    }

    @Override
    public int getCount() {//注意这个,不要改,自己在ViewPager里面改
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        if (carousel_data == null)
            return null;
        if (carousel_data.size() == 0)
            return null;
        final DiscoveryBannerObj focusData = carousel_data.get(position % carousel_data.size());
        //设置缓存
        View view = reuseView;
        if (null == view || null != view.getParent()) {
            view = getNewView();
            view.setTag(focusData.getBanner_id());
        }
        imageLoader.displayImage(focusData.getBanner_uri(), (ImageView) view.findViewById(R.id.image_view), DisplayOptionsUtils.getDefaultMaxRectImageOptions());

        TextView tv_1 = (TextView) view.findViewById(R.id.tv_1);
        TextView tv_2 = (TextView) view.findViewById(R.id.tv_2);
        RelativeLayout rl_found_banner = (RelativeLayout) view.findViewById(R.id.rl_found_banner);
        tv_1.setText("");
        tv_2.setText("");


        rl_found_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;

                if (MHStringUtils.isEmpty(focusData.getTarget_name())) return;
                switch (focusData.getTarget_name()) {
                    case "video":               //跳转视频详情
                        enterDetail(1, focusData.getTarget_content());
                        TCAgent.onEvent(context, "banner点击" + ConstantsValue.android + focusData.getBanner_id());
                        break;
                    case "photo":               //跳转图片详情页
                        enterDetail(2, focusData.getTarget_content());
                        TCAgent.onEvent(context, "banner点击" + ConstantsValue.android + focusData.getBanner_id());
                        break;
                    case "activity":            //跳转活动H5页
                        intent = new Intent(context, WebViewActivity.class);
                        if (!MHStringUtils.isEmpty(focusData.getTarget_content()))
                            intent.putExtra("uri", focusData.getTarget_content());
                        BannerSubjectBean bean = new BannerSubjectBean();
                        if (!MHStringUtils.isEmpty(focusData.getTarget_content_extra())) {
                            bean = gson.fromJson(focusData.getTarget_content_extra(), BannerSubjectBean.class);
                            if (bean == null) return;
                            intent.putExtra("title", bean.getActivity_name());
                            intent.putExtra("activity_note", bean.getActivity_note());
                            intent.putExtra("activity_picture", bean.getShare_icon());
                            TCAgent.onEvent(context, bean.getActivity_name() + "点击" + ConstantsValue.android);
                        } else {
                            intent.putExtra("", bean.getActivity_name());
                        }
                        break;
                    case "html5":               //跳转H5页
                        intent = new Intent(context, WebViewActivity.class);
                        if (!MHStringUtils.isEmpty(focusData.getTarget_content()))
                            intent.putExtra("uri", focusData.getTarget_content());
                        BannerSubjectBean bean1 = new BannerSubjectBean();
                        if (!MHStringUtils.isEmpty(focusData.getTarget_content_extra())) {
                            bean = gson.fromJson(focusData.getTarget_content_extra(), BannerSubjectBean.class);
                            if (bean == null) return;
                            intent.putExtra("title", bean.getHtml_name());
                            intent.putExtra("activity_note", bean.getHtml_note());
                            intent.putExtra("activity_picture", bean.getShare_icon());
                            TCAgent.onEvent(context, bean.getHtml_name() + "点击" + ConstantsValue.android);
                        } else {
                            intent.putExtra("", bean1.getActivity_name());
                        }
                        break;
                    case "answer":              //跳转映答详情页
                        intent = new Intent(context, InterlocutionDetailsActivity.class);
                        intent.putExtra("question_id", focusData.getTarget_content());
                        TCAgent.onEvent(context, "banner点击" + ConstantsValue.android + focusData.getBanner_id());
                        break;
                    case "user":                //跳转个人主页
                        if (!((BaseActivity) context).isLogin(false)) return;
                        intent = new Intent(context, PersonalHomeActivity.class);
                        intent.putExtra("userId", focusData.getTarget_content());
                        TCAgent.onEvent(context, "banner点击" + ConstantsValue.android + focusData.getBanner_id());
                        break;
                }
                if (intent != null)
                    context.startActivity(intent);

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "clicksinglebanner");
                    jsonObject.put("description", "banner点击量");
                    jsonObject.put("id", focusData.getBanner_id());

                    BehaviourStatistic.uploadBehaviourInfo(jsonObject);
                } catch (Exception e) {
                    MHLogUtil.e("CarouselAdapter",e);
                }
            }
        });

        container.addView(view);
        //埋点统计逻辑
        BannerSubjectBean bean;
        if ("activity".equals(focusData.getTarget_name())) {
            if (!MHStringUtils.isEmpty(focusData.getTarget_content_extra())) {
                bean = gson.fromJson(focusData.getTarget_content_extra(), BannerSubjectBean.class);
                if (bean != null) {
                    TCAgent.onEvent(context, bean.getActivity_name() + "展示" + ConstantsValue.android);
                }
            }
        } else if ("html5".equals(focusData.getTarget_name())) {
            if (!MHStringUtils.isEmpty(focusData.getTarget_content_extra())) {
                bean = gson.fromJson(focusData.getTarget_content_extra(), BannerSubjectBean.class);
                if (bean != null) {
                    TCAgent.onEvent(context, bean.getHtml_name() + "展示" + ConstantsValue.android);
                }
            }
        } else {
            TCAgent.onEvent(context, "banner展示" + ConstantsValue.android + focusData.getBanner_id());
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        reuseView = (View) object;
    }

    public void changeList(List<DiscoveryBannerObj> carousel_data) {
        this.carousel_data = carousel_data;
        notifyDataSetChanged();
    }

    private View getNewView() {
        final View view = View.inflate(context, R.layout.home_found_carouse_layout, null);
        return view;
    }

    private void enterDetail(int type, String objectId) {
        ArrayList<VideoAndImg> data = new ArrayList<>();
        VideoAndImg obj = new VideoAndImg();
        obj.setElement_type(type);
        obj.setContent_type(type);
        obj.setPhoto_id(objectId);
        obj.setVideo_id(objectId);
        data.add(obj);
        Intent intent = new Intent(context, VideoAndImgActivity.class);
        intent.putParcelableArrayListExtra("data", data)
                .putExtra("currentIndex", 0)
                .putExtra("userId", objectId)
                .putExtra("pageIndex", 0)
                .putExtra("command", ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS);
        context.startActivity(intent);
    }
}

