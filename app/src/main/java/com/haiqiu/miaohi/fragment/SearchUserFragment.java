package com.haiqiu.miaohi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.activity.SearchBodyActivity;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.SearchUserList;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.SearchEnginesResponse;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.ChangeAttentionStateUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHStateSyncUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.callback.ChangeAttentionStateCallBack;
import com.haiqiu.miaohi.view.MyCircleView;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.haiqiu.miaohi.R.id.progress_bar;


/**
 * Created by zhandalin on 2016-06-23 17:17.
 * 说明:
 */
public class SearchUserFragment extends BaseFragment {
    private ListView listView;
    private PullToRefreshListView pull_to_refresh_listview;
    private ListViewAdapter adapter;

    private int pageIndex;
    private String keyWords;
    private boolean isHasLoaded = false;
    private List<SearchUserList> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pull_to_refresh_listview = new PullToRefreshListView(context);
        pull_to_refresh_listview.setPullLoadEnabled(true);
        pull_to_refresh_listview.setPullRefreshEnabled(true);
        listView = pull_to_refresh_listview.getRefreshableView();
        pull_to_refresh_listview.setBackgroundColor(getResources().getColor(R.color.white));
        pull_to_refresh_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 0;
                initData(true);
            }

            @Override
            public void onLoadMore() {
                initData();
            }

        });
        return pull_to_refresh_listview;
    }

    public void startSearch(String keyWords) {
        if (!isHasLoaded) {
            this.keyWords = keyWords;
            pageIndex = 0;
            if (null != context)
                context.showMHLoading();
            initData();
            isHasLoaded = true;
        }
    }

    private void initData() {
        initData(false);
    }

    private void initData(final boolean isNeedUpdateHead) {
        MHRequestParams requestParams = new MHRequestParams();
        if (null != keyWords)
            requestParams.addParams("search_key_word", Base64Util.getBase64Str(keyWords));
        requestParams.addParams("default_type", "2");
        requestParams.addParams("page_size", "20");
        requestParams.addParams("page_index", pageIndex + "");
        MHHttpClient.getInstance().post(SearchEnginesResponse.class, context, ConstantsValue.Url.SEARCH_ENGINES, requestParams, new MHHttpHandler<SearchEnginesResponse>() {
            @Override
            public void onSuccess(SearchEnginesResponse response) {
                if (pageIndex == 0 && isNeedUpdateHead)
                    ((SearchBodyActivity) getActivity()).fillTabTitle(response.getData());
                pageLoadLogic(response);
            }

            @Override
            public void onFailure(String content) {
                pull_to_refresh_listview.onLoadComplete(true);
            }
        });
    }

    private void pageLoadLogic(SearchEnginesResponse response) {
        List<SearchUserList> li = response.getData().getPage_result().getUser_list();

        if (pageIndex == 0 && (null == li || li.size() == 0)) {
            pull_to_refresh_listview.showBlankView();
        } else {
            pull_to_refresh_listview.hideAllTipView();
        }


        if (pageIndex == 0) {
            list.clear();
        }
        if (null != li)
            list.addAll(li);
        if (null == li || li.size() == 0)
            pull_to_refresh_listview.onLoadComplete(false);
        else {
            pull_to_refresh_listview.onLoadComplete(true);
        }

        //数据正常，进行显示
        if (adapter == null) {
            adapter = new ListViewAdapter(list);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        pageIndex++;
    }

    class ListViewAdapter extends BaseAdapter {
        private List<SearchUserList> list;
        private ImageLoader imageLoader;

        public ListViewAdapter(List<SearchUserList> list) {
            this.list = list;
            imageLoader = ImageLoader.getInstance();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (null == convertView) {
                convertView = View.inflate(context, R.layout.item_common_user_show, null);
                viewHolder = new ViewHolder();
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final SearchUserList object = list.get(position);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.iv_vip_mark = (ImageView) convertView.findViewById(R.id.iv_vip_mark);
            viewHolder.iv_head = (MyCircleView) convertView.findViewById(R.id.iv_head);
            viewHolder.iv_gender = (ImageView) convertView.findViewById(R.id.iv_gender);
            viewHolder.iv_qa = (ImageView) convertView.findViewById(R.id.iv_qa);
            viewHolder.bt_attention = (Button) convertView.findViewById(R.id.bt_attention);
            viewHolder.progress_bar = convertView.findViewById(progress_bar);
            //vip标志
            viewHolder.iv_vip_mark.setVisibility(object.getUser_type() > 10 ? View.VISIBLE : View.INVISIBLE);
            //映答权限
            viewHolder.iv_qa.setVisibility(object.isAnswer_auth() ? View.VISIBLE : View.INVISIBLE);
            //性别
            viewHolder.iv_gender.setImageResource(object.getUser_gender() == 2 ? R.drawable.gender_women : R.drawable.gender_man);
            //用户名
            if (MHStringUtils.isEmpty(object.getUser_name()))
                viewHolder.tv_name.setText("");
            else
                viewHolder.tv_name.setText(object.getUser_name());
            //用户头衔
            if (MHStringUtils.isEmpty(object.getUser_authentic())) {
                viewHolder.tv_title.setVisibility(View.GONE);
            } else {
                viewHolder.tv_title.setVisibility(View.VISIBLE);
                viewHolder.tv_title.setText(object.getUser_authentic());
            }
            //用户头像
            imageLoader.displayImage(object.getPortrait_uri(), viewHolder.iv_head, DisplayOptionsUtils.getHeaderDefaultImageOptions());
            //判断是否是自己，是则隐藏关注按钮
            if (SpUtils.getString(ConstantsValue.Sp.USER_ID).equals(object.getUser_id()))
                viewHolder.bt_attention.setVisibility(View.INVISIBLE);
            else
                viewHolder.bt_attention.setVisibility(View.VISIBLE);
            //关注状态
            MHStateSyncUtil.State syncState = MHStateSyncUtil.getSyncState(object.getUser_id());
            if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != syncState) {
                object.setAttention_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == syncState);
            }
            if (!object.isAttention_state()) {
                viewHolder.bt_attention.setText("关注");
                viewHolder.bt_attention.setTextColor(getResources().getColor(R.color.fontblue));
                viewHolder.bt_attention.setBackgroundResource(R.drawable.shape_attention_blue_selector);
            } else {
                viewHolder.bt_attention.setText("已关注");
                viewHolder.bt_attention.setTextColor(getResources().getColor(R.color.color_c4));
                viewHolder.bt_attention.setBackgroundResource(R.drawable.tag_bg);
            }
            //点击关注按钮
            viewHolder.bt_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeAttentionStateUtil stateUtil = new ChangeAttentionStateUtil(context);
                    stateUtil.changeAttentionState(object.getUser_id(), !object.isAttention_state(), v, viewHolder.progress_bar, new ChangeAttentionStateCallBack() {
                        @Override
                        public void callBackInfo(boolean attentionState) {
                            object.setAttention_state(attentionState);
                        }
                    });
                }
            });
            //点击整个条目
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((context).isLogin(false))
                        context.startActivity(new Intent(context, PersonalHomeActivity.class).putExtra("userId", object.getUser_id()));
                }
            });
            return convertView;
        }

        private class ViewHolder {
            MyCircleView iv_head;
            ImageView iv_vip_mark;
            ImageView iv_gender;
            ImageView iv_qa;
            TextView tv_name;
            TextView tv_title;
            Button bt_attention;
            View progress_bar;
        }
    }

    @Override
    public void onResume() {
        if (adapter != null) adapter.notifyDataSetChanged();
        super.onResume();
    }
}
