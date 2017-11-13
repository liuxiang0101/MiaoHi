package com.haiqiu.miaohi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.CommonPersonInfo;
import com.haiqiu.miaohi.activity.InterlocutionDetailsActivity;
import com.haiqiu.miaohi.activity.SearchBodyActivity;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.SearchQuestionList;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.SearchEnginesResponse;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.utils.CommonUtil;
import com.haiqiu.miaohi.view.CommonPersonalInfoView;
import com.haiqiu.miaohi.view.TimeTextView;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuXiang on 2016/11/30.
 * 说明:搜索-映答
 */
public class SearchQaFragment extends BaseFragment {
    private ListView listView;
    private int pageIndex;
    private boolean isHasLoaded = false;
    private PullToRefreshListView pull_to_refresh_listview;
    private QaListAdapter adapter;
    private String keyWords;
    private List<SearchQuestionList> listPage = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pull_to_refresh_listview = new PullToRefreshListView(context);
        pull_to_refresh_listview.setPullLoadEnabled(true);
        pull_to_refresh_listview.setPullRefreshEnabled(true);
        listView = pull_to_refresh_listview.getRefreshableView();
        listView.setDividerHeight(0);
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
        requestParams.addParams("default_type", "3");
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

            }
        });
    }

    private void pageLoadLogic(SearchEnginesResponse response) {
        List<SearchQuestionList> li = response.getData().getPage_result().getQuestion_list();
        if (pageIndex == 0 && (response == null || null == li || li.size() == 0)) {
            pull_to_refresh_listview.showBlankView();
        } else {
            pull_to_refresh_listview.hideAllTipView();
        }

        if (pageIndex == 0) {
            listPage.clear();
        }
        if (null != li)
            listPage.addAll(li);
        if (null == li || li.size() == 0)
            pull_to_refresh_listview.onLoadComplete(false);
        else {
            pull_to_refresh_listview.onLoadComplete(true);
        }

        //数据正常，进行显示
        if (adapter == null) {
            adapter = new QaListAdapter(listPage, getActivity());
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        pageIndex++;
    }

    /**
     * listview布局适配器
     */
    class QaListAdapter extends BaseAdapter {
        private List<SearchQuestionList> list;
        private Context context;
        private ImageLoader imageLoader;

        public QaListAdapter(List<SearchQuestionList> list, Context context) {
            this.list = list;
            this.context = context;
            this.imageLoader = ImageLoader.getInstance();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            final SearchQuestionList obj = list.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(context, R.layout.item_choice_qa, null);
                viewHolder.cpiv_qa_recommend = (CommonPersonalInfoView) convertView.findViewById(R.id.cpiv_qa_recommend);
                viewHolder.rl_outer_shape = (RelativeLayout) convertView.findViewById(R.id.rl_outer_shape);
                viewHolder.tv_qa_price = (TextView) convertView.findViewById(R.id.tv_qa_price);
                viewHolder.tv_qaetail_remaintime = (TimeTextView) convertView.findViewById(R.id.tv_qaetail_remaintime);
                viewHolder.tv_qa_recommenddescribe = (TextView) convertView.findViewById(R.id.tv_qa_recommenddescribe);
                viewHolder.tv_qarecommendcircusee_count = (TextView) convertView.findViewById(R.id.tv_qarecommendcircusee_count);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            CommonPersonInfo info = new CommonPersonInfo();
            info.setHeadUri(obj.getAnswer_user_portrait());
            info.setName(obj.getAnswer_user_name());
            info.setName_nodescribe(obj.getAnswer_user_note());
            info.setDescribe(obj.getAnswer_user_note());
            info.setTime(obj.getAnswer_time_text() + "回答了");
            info.setUserType(obj.getAnswer_user_type());
            info.setUserId(obj.getAnswer_user_id());
            viewHolder.cpiv_qa_recommend.setUserInfo(info);
            viewHolder.tv_qa_recommenddescribe.setText(obj.getQuestion_content());
            viewHolder.tv_qarecommendcircusee_count.setText(obj.getObserve_count());
            if (obj.isTemporary_free()) {
                viewHolder.rl_outer_shape.setBackgroundResource(R.drawable.shape_limit_free_radius_red_selector);
                viewHolder.tv_qa_price.setText("限时免费");
            } else {
                viewHolder.rl_outer_shape.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
                viewHolder.tv_qa_price.setText(obj.getObserve_price_text());
            }
            //倒计时结束，改变限时免费状态
            viewHolder.tv_qaetail_remaintime.setVisibility(View.VISIBLE);
            viewHolder.tv_qaetail_remaintime.setTimes(obj.getTime_remain());
            viewHolder.tv_qaetail_remaintime.setOnTimeMonitorListener(new TimeTextView.OnTimeMonitorListener() {
                @Override
                public void isTimeUp(boolean b) {
                    viewHolder.rl_outer_shape.setBackgroundResource(R.drawable.shape_limit_free_radius_blue_selector);
                    viewHolder.tv_qa_price.setText(CommonUtil.formatPrice2(obj.getObserve_price()) + "元围观");
                }
            });
            //点击整个条目
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, InterlocutionDetailsActivity.class).putExtra("question_id", obj.getQuestion_id()));
                }
            });
            return convertView;
        }

        class ViewHolder {
            CommonPersonalInfoView cpiv_qa_recommend;
            RelativeLayout rl_outer_shape;
            TextView tv_qa_price;
            TimeTextView tv_qaetail_remaintime;
            TextView tv_qa_recommenddescribe;
            TextView tv_qarecommendcircusee_count;
        }
    }

}
