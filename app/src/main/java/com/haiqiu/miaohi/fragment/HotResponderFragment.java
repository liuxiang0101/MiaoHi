package com.haiqiu.miaohi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.SportsListActivity;
import com.haiqiu.miaohi.adapter.HotResponderAdapter;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.HotResponderData;
import com.haiqiu.miaohi.bean.HotResponderKindResult;
import com.haiqiu.miaohi.bean.HotResponderPageResult;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.HotResponderResponse;
import com.haiqiu.miaohi.utils.DensityUtil;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuXiang on 2016/12/6.
 * 热门答主tab页
 */
public class HotResponderFragment extends BaseFragment {
    private final String TAG = getClass().getSimpleName();
    private PullToRefreshListView pulltorefresh_listview;
    private int pageIndex = 0;
    private List<HotResponderPageResult> listPage;
    private HotResponderAdapter adapter;
    private ListView listView;
    private RecyclerView recyclerview;
    private GridView gv_header;
    private RelativeLayout rl_head;
    private int aScreenVisibleSorts;
    private boolean isHasAutoLoad = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_responder, container, false);

        initView(view);
        initListView();
        getNetData();
        int screenWidthPx = ScreenUtils.getScreenWidth(getActivity());
        int screenWidthDip = DensityUtil.px2dip(getActivity(), screenWidthPx);
        aScreenVisibleSorts = (screenWidthDip - 10) / 52;
        return view;
    }

    private void initView(View view) {
        listPage = new ArrayList<>();
        rl_head = (RelativeLayout) view.findViewById(R.id.rl_head);
        gv_header = (GridView) view.findViewById(R.id.gv_header);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        pulltorefresh_listview = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh_listview);
        pulltorefresh_listview.setPullRefreshEnabled(true);
        pulltorefresh_listview.setPullLoadEnabled(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        listView = pulltorefresh_listview.getRefreshableView();
        listView.setDividerHeight(0);
    }

    private void initListView() {
        pulltorefresh_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 0;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                if (isHasAutoLoad)
                    getNetData();
            }
        });
    }

    private void getNetData() {
        final MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("page_index", "" + pageIndex);
        requestParams.addParams("page_size", "20");
        MHHttpClient.getInstance().post(HotResponderResponse.class, context, ConstantsValue.Url.TOP_QUESTION_VIP, requestParams, new MHHttpHandler<HotResponderResponse>() {
            @Override
            public void onSuccess(HotResponderResponse response) {
                isHasAutoLoad = true;
                pageLoadLogic(response);
            }

            @Override
            public void onFailure(String content) {
                isHasAutoLoad = true;
                pulltorefresh_listview.onLoadComplete(true);
                if (pageIndex == 0) {
                    pulltorefresh_listview.showErrorView();
                    rl_head.setVisibility(View.GONE);
                }
            }

            @Override
            public void onStatusIsError(String message) {
                isHasAutoLoad = true;
                super.onStatusIsError(message);
            }
        });
    }

    private void pageLoadLogic(HotResponderResponse response) {
        rl_head.setVisibility(View.VISIBLE);
        if (pageIndex == 0 && (response.getData() == null || null == response.getData().getPage_result() || response.getData().getPage_result().size() == 0)) {
            pulltorefresh_listview.showBlankView();
            rl_head.setVisibility(View.GONE);
        } else {
            pulltorefresh_listview.hideAllTipView();
        }

        HotResponderData data = response.getData();
        if (data.getKind_result().size() > aScreenVisibleSorts) {      //若分类的数目超出一屏的可显示数目，滑动显示
            gv_header.setVisibility(View.GONE);
            recyclerview.setVisibility(View.VISIBLE);
            recyclerview.setAdapter(new MyRvAdapter(data.getKind_result()));
        } else {                                                       //若分类的数目未超出一屏的可显示数目，平均分配
            gv_header.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);
            gv_header.setNumColumns(data.getKind_result().size());
            gv_header.setAdapter(new MyGvAdapter(data.getKind_result()));
        }

        List<HotResponderPageResult> li = response.getData().getPage_result();
        if (pageIndex == 0) {
            listPage.clear();
        }
        if (null != li)
            listPage.addAll(li);
        if (null == li || li.size() == 0)
            pulltorefresh_listview.onLoadComplete(false);
        else {
            pulltorefresh_listview.onLoadComplete(true);
        }


        //数据正常，进行显示
        if (adapter == null) {
            adapter = new HotResponderAdapter(listPage, getActivity());
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        pageIndex++;
    }

    /**
     * RecyclerView适配器
     */
    class MyRvAdapter extends RecyclerView.Adapter<MyRvAdapter.MyRvViewHolder> {
        private List<HotResponderKindResult> list;
        private ImageLoader imageLoader = ImageLoader.getInstance();

        public MyRvAdapter(List<HotResponderKindResult> kind_result) {
            this.list = kind_result;
        }

        @Override
        public MyRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(context, R.layout.item_hot_responder_header, null);
            MyRvViewHolder holder = new MyRvViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyRvViewHolder holder, final int position) {
            holder.textView.setText(list.get(position).getKind_name());
            imageLoader.displayImage(list.get(position).getKind_icon(), holder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SportsListActivity.class);
                    intent.putExtra("kind_id", list.get(position).getKind_id());
                    intent.putExtra("kind_name", list.get(position).getKind_name());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyRvViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;

            public MyRvViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                textView = (TextView) itemView.findViewById(R.id.textView);
            }
        }
    }

    class MyGvAdapter extends BaseAdapter {
        private List<HotResponderKindResult> list;
        private ImageLoader imageLoader = ImageLoader.getInstance();

        public MyGvAdapter(List<HotResponderKindResult> list) {
            this.list = list;
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
            View view = View.inflate(getActivity(), R.layout.item_hot_responder_header, null);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            textView.setText(list.get(position).getKind_name());
            imageLoader.displayImage(list.get(position).getKind_icon(), imageView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SportsListActivity.class);
                    intent.putExtra("kind_id", list.get(position).getKind_id());
                    intent.putExtra("kind_name", list.get(position).getKind_name());
                    startActivity(intent);
                }
            });
            return view;
        }
    }


}
