package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.MaybeInterestToPeopleActivity;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.activity.VideoAndImgActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.BaseHolder;
import com.haiqiu.miaohi.bean.DiscoveryBannerObj;
import com.haiqiu.miaohi.bean.DiscoveryObjectObj;
import com.haiqiu.miaohi.bean.DiscoveryUserObj;
import com.haiqiu.miaohi.bean.VideoAndImg;
import com.haiqiu.miaohi.fragment.FoundSquareFragment;
import com.haiqiu.miaohi.fragment.LoginDialogFragment;
import com.haiqiu.miaohi.utils.ChangeAttentionStateUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHStateSyncUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.callback.ChangeAttentionStateCallBack;
import com.haiqiu.miaohi.widget.CarouselImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuXiang on 2017/2/21.
 */

public class FoundSquareRecycleViewAdapter extends RecyclerView.Adapter<BaseHolder> {
    private final int BANNER_VIEW = 1000;               //类型--轮播图
    private final int FOUND_MORE_VIEW = 1001;           //类型--发现更多
    private final int RECYCLER_VIEW = 1002;             //类型--横向的卡片布局
    private final int FOUND_SORT_VIEW = 1003;           //类型--发现-分类
    private final int NORMAL_VIEW = 1004;               //类型--正常view
    private Context context;
    private Fragment fragment;
    private ImageLoader imageLoader;
    private List<DiscoveryBannerObj> listBanner;
    private List<DiscoveryUserObj> listUser;
    private List<DiscoveryObjectObj> listObj;
    private ArrayList<VideoAndImg> listData;
    private TextView tv_sort_name;
    private CarouselImageView carouselImageView;

    public FoundSquareRecycleViewAdapter(Context context, List<DiscoveryBannerObj> listBanner, List<DiscoveryUserObj> listUser, List<DiscoveryObjectObj> listObj) {
        this.context = context;
        this.listBanner = listBanner;
        this.listUser = listUser;
        this.listObj = listObj;
        this.imageLoader = ImageLoader.getInstance();
        listData = new ArrayList<>();
    }

    public TextView getTv_sort_name() {
        return tv_sort_name;
    }

    public CarouselImageView getCarouselImageView() {
        return carouselImageView;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BANNER_VIEW:           //轮播图布局
                return new BannerHolder(R.layout.found_square_banner_item, parent, viewType);
            case FOUND_MORE_VIEW:       //发现更多布局
                return new FoundMoreHolder(R.layout.found_square_foundmore_item, parent, viewType);
            case RECYCLER_VIEW:         //推荐用户布局
                return new HorizontalViewHolder(R.layout.found_square_rv_item, parent, viewType);
            case FOUND_SORT_VIEW:       //发现分类布局
                return new FoundSortHolder(R.layout.found_square_foundsort_item, parent, viewType);
            case NORMAL_VIEW:           //通用布局
                return new ObjectItemViewHolder(R.layout.item_recommend_sports, parent, viewType);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        if (holder instanceof BannerHolder) {
            holder.refreshData(listBanner, position);
        } else if (holder instanceof FoundMoreHolder) {
            holder.refreshData(listUser, position);
        } else if (holder instanceof HorizontalViewHolder) {
            holder.refreshData(listUser, position);
        } else if (holder instanceof FoundSortHolder) {
            holder.refreshData(null, position);
        } else if (holder instanceof ObjectItemViewHolder) {
            holder.refreshData(listObj, position);
        }

    }

    @Override
    public int getItemCount() {
        return 4 + listObj.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return BANNER_VIEW;
        if (position == 1) return FOUND_MORE_VIEW;
        if (position == 2) return RECYCLER_VIEW;
        if (position == 3) return FOUND_SORT_VIEW;
        return NORMAL_VIEW;
    }

    /**
     * 条目占位--广告轮播布局
     */
    private class BannerHolder extends BaseHolder<List<DiscoveryBannerObj>> {
        public BannerHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            carouselImageView = (CarouselImageView) itemView.findViewById(R.id.carousel_imageview);
        }

        @Override
        public void refreshData(List<DiscoveryBannerObj> data, int position) {
            super.refreshData(data, position);
            //banner轮播图
            if (data == null)
                carouselImageView.setVisibility(View.GONE);
            else if (data.size() == 0)
                carouselImageView.setVisibility(View.GONE);
            else {
                carouselImageView.setVisibility(View.VISIBLE);
                carouselImageView.initData(data);
            }
        }
    }

    /**
     * 条目占位--发现更多布局
     */
    private class FoundMoreHolder extends BaseHolder<List<DiscoveryUserObj>> {
        private TextView tv_discover_more;
        private View rl_card;

        public FoundMoreHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            tv_discover_more = (TextView) itemView.findViewById(R.id.tv_discover_more);
            rl_card = itemView.findViewById(R.id.rl_card);
        }

        @Override
        public void refreshData(List<DiscoveryUserObj> data, int position) {
            super.refreshData(data, position);
            if (data.size() <= 0)
                rl_card.setVisibility(View.GONE);
            else
                rl_card.setVisibility(View.VISIBLE);
            tv_discover_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, MaybeInterestToPeopleActivity.class));
                }
            });
        }
    }

    /**
     * 条目占位--横向滑动的卡片布局
     */
    private class HorizontalViewHolder extends BaseHolder<List<DiscoveryUserObj>> {
        private RecyclerView horizontal_recyclerview;
        private List<DiscoveryUserObj> data;
        private HorizontalAdapter horizontalAdapter;

        public HorizontalViewHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            horizontal_recyclerview = (RecyclerView) itemView.findViewById(R.id.horizontal_recyclerview);
        }

        @Override
        public void refreshData(List<DiscoveryUserObj> data, int position) {
            this.data = data;
            if (data.size() <= 0)
                horizontal_recyclerview.setVisibility(View.GONE);
            else
                horizontal_recyclerview.setVisibility(View.VISIBLE);
            if (null == horizontalAdapter) {
                //设置布局管理器
                horizontal_recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                //设置适配器
                horizontalAdapter = new HorizontalAdapter();
                horizontal_recyclerview.setAdapter(horizontalAdapter);
            } else {
                horizontalAdapter.notifyItemRangeChanged(0, data.size());
            }
        }

        private class HorizontalAdapter extends RecyclerView.Adapter<BaseHolder> {

            @Override
            public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new UserItemViewHolder(R.layout.item_interested_people_card, parent, viewType);
            }

            @Override
            public void onBindViewHolder(BaseHolder holder, int position) {
                holder.refreshData(data, position);
            }

            @Override
            public int getItemCount() {
                return data.size() + 1;
            }
        }
    }

    /**
     * 条目占位--发现-选择分类布局
     */
    private class FoundSortHolder extends BaseHolder<Integer> {
        private LinearLayout ll_select_sort;

        public FoundSortHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            tv_sort_name = (TextView) itemView.findViewById(R.id.tv_sort_name);
            ll_select_sort = (LinearLayout) itemView.findViewById(R.id.ll_select_sort);

        }

        @Override
        public void refreshData(Integer data, int position) {
            super.refreshData(data, position);
            ll_select_sort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != fragment)
                        ((FoundSquareFragment) fragment).clickSort();
                }
            });
        }
    }

    /**
     * 条目占位--推荐用户子条目holder
     */
    private class UserItemViewHolder extends BaseHolder<List<DiscoveryUserObj>> {
        private ImageView iv_head;
        private ImageView iv_vip_mark;
        private TextView tv_name;
        private TextView tv_title;
        private TextView tv_addention;
        private LinearLayout ll;
        private View view_interval;
        private View progress_bar;

        public UserItemViewHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            iv_head = (ImageView) itemView.findViewById(R.id.iv_head);
            iv_vip_mark = (ImageView) itemView.findViewById(R.id.iv_vip_mark);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_addention = (TextView) itemView.findViewById(R.id.tv_addention);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            view_interval = itemView.findViewById(R.id.view_interval);
            progress_bar = itemView.findViewById(R.id.progress_bar);
        }

        @Override
        public void refreshData(final List<DiscoveryUserObj> data, final int position) {
            super.refreshData(data, position);
            if (position < data.size()) {
                final DiscoveryUserObj obj = data.get(position);
                imageLoader.displayImage(obj.getPortrait_uri(), iv_head, DisplayOptionsUtils.getHeaderDefaultImageOptions());
                tv_name.setText(obj.getUser_name());
                tv_title.setText(obj.getUser_note());
                iv_vip_mark.setVisibility(obj.getUser_type() > 10 ? View.VISIBLE : View.GONE);

                MHStateSyncUtil.State syncState = MHStateSyncUtil.getSyncState(obj.getUser_id());
                if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != syncState) {
                    obj.setAttention_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == syncState);
                }

                if (obj.isAttention_state()) {
                    tv_addention.setText("已关注");
                    tv_addention.setTextColor(context.getResources().getColor(R.color.color_c4));
                    tv_addention.setBackgroundResource(R.drawable.tag_bg);
                } else {
                    tv_addention.setText("关注");
                    tv_addention.setTextColor(context.getResources().getColor(R.color.fontblue));
                    tv_addention.setBackgroundResource(R.drawable.shape_attention_blue_selector);
                }
                //点击关注按钮
                tv_addention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ChangeAttentionStateUtil((BaseActivity) context).changeAttentionState(obj.getUser_id(), !obj.isAttention_state(), v, progress_bar, new ChangeAttentionStateCallBack() {
                            @Override
                            public void callBackInfo(boolean attentionState) {
                                obj.setAttention_state(attentionState);
                            }
                        });
                        TCAgent.onEvent(context, "发现推人关注" + ConstantsValue.android);
                    }
                });
                //点击整张卡片时
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MHStringUtils.isEmpty(SpUtils.getString(ConstantsValue.Sp.TOKEN_MIAOHI))) {
                            LoginDialogFragment loginDialog = new LoginDialogFragment();
                            loginDialog.show(((BaseActivity) context).getSupportFragmentManager(), "loginDialog");
                        } else {
                            Intent intent = new Intent(context, PersonalHomeActivity.class);
                            intent.putExtra("userId", data.get(position).getUser_id());
                            context.startActivity(intent);
                        }
                        TCAgent.onEvent(context, "发现推人头像" + ConstantsValue.android);
                    }
                });
                iv_head.setVisibility(View.VISIBLE);
                tv_name.setVisibility(View.VISIBLE);
                tv_title.setVisibility(View.VISIBLE);
                tv_addention.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
                view_interval.setVisibility(View.GONE);
            } else {//如果是最后一张卡片
                iv_vip_mark.setVisibility(View.GONE);
                iv_head.setVisibility(View.GONE);
                tv_name.setVisibility(View.GONE);
                tv_title.setVisibility(View.GONE);
                tv_addention.setVisibility(View.GONE);
                ll.setVisibility(View.VISIBLE);
                view_interval.setVisibility(View.VISIBLE);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MaybeInterestToPeopleActivity.class);
                        intent.putExtra("isFromDiscover", true);
                        context.startActivity(intent);
                        TCAgent.onEvent(context, "发现推人发现更多" + ConstantsValue.android);
                    }
                });
            }
        }
    }

    /**
     * 条目占位--通用子条目holder
     */
    private class ObjectItemViewHolder extends BaseHolder<List<DiscoveryObjectObj>> {
        private ImageView imageView;
        private ImageView iv_video_mark;
        private View view_right;

        public ObjectItemViewHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            iv_video_mark = (ImageView) itemView.findViewById(R.id.iv_video_mark);
            view_right = itemView.findViewById(R.id.view_right);
        }

        @Override
        public void refreshData(List<DiscoveryObjectObj> data, final int p) {
            super.refreshData(data, p);
            final int position = p - 4;
            final DiscoveryObjectObj object = data.get(position);
            if (object == null) return;
            view_right.setVisibility((position + 1) % 3 == 0 ? View.GONE : View.VISIBLE);
            iv_video_mark.setVisibility(object.getObject_type() == 1 ? View.VISIBLE : View.INVISIBLE);
            imageLoader.displayImage(object.getObject_img_uri(), imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VideoAndImgActivity.class);
                    intent.putParcelableArrayListExtra("data", listData)
                            .putExtra("currentIndex", position)
                            .putExtra("userId", object.getObject_id())
                            .putExtra("pageIndex", position)
                            .putExtra("command", ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void setVideoList(List<DiscoveryObjectObj> list) {
        listData.clear();
        for (DiscoveryObjectObj object : list) {
            VideoAndImg obj = new VideoAndImg();
            obj.setElement_type(object.getObject_type());
            obj.setContent_type(object.getObject_type());
            obj.setPhoto_id(object.getObject_id());
            obj.setVideo_id(object.getObject_id());
            listData.add(obj);
        }
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}

