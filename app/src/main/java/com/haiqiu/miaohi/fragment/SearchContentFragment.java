package com.haiqiu.miaohi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.SearchBodyActivity;
import com.haiqiu.miaohi.activity.VideoAndImgActivity;
import com.haiqiu.miaohi.base.BaseFragment;
import com.haiqiu.miaohi.bean.SearchObjectList;
import com.haiqiu.miaohi.bean.VideoAndImg;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.SearchEnginesResponse;
import com.haiqiu.miaohi.utils.AnimateFirstDisplayListener;
import com.haiqiu.miaohi.utils.Base64Util;
import com.haiqiu.miaohi.view.HeaderGridView;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshBase;
import com.haiqiu.miaohi.widget.pulltorefreshview.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhandalin on 2016-06-23 17:17.
 * 说明:
 */
public class SearchContentFragment extends BaseFragment {
    private GridView gridView;
    private GridViewAdapter adapter;
    private PullToRefreshGridView pull_to_refresh_gridview;

    private int pageIndex = 0;
    private String keyWords = null;
    private boolean isHasLoaded = false;

    private List<SearchObjectList> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pull_to_refresh_gridview = new PullToRefreshGridView(context);
        pull_to_refresh_gridview.setPullLoadEnabled(true);
        pull_to_refresh_gridview.setPullRefreshEnabled(true);
        gridView = pull_to_refresh_gridview.getRefreshableView();
        gridView.setNumColumns(3);
        pull_to_refresh_gridview.setBackgroundColor(getResources().getColor(R.color.white));
        pull_to_refresh_gridview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<HeaderGridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<HeaderGridView> refreshView) {
                pageIndex = 0;
                initData();
            }

            @Override
            public void onLoadMore() {
                initData();
            }
        });


        return pull_to_refresh_gridview;
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
        MHRequestParams requestParams = new MHRequestParams();
        if (null != keyWords)
            requestParams.addParams("search_key_word", Base64Util.getBase64Str(keyWords));
        requestParams.addParams("page_size", "21");
        requestParams.addParams("default_type", "1");
        requestParams.addParams("page_index", pageIndex + "");
        MHHttpClient.getInstance().post(SearchEnginesResponse.class, context, ConstantsValue.Url.SEARCH_ENGINES, requestParams, new MHHttpHandler<SearchEnginesResponse>() {
            @Override
            public void onSuccess(SearchEnginesResponse response) {
                if (pageIndex == 0)
                    ((SearchBodyActivity) getActivity()).fillTabTitle(response.getData());
                pageLoadLogic(response);
            }

            @Override
            public void onFailure(String content) {
                pull_to_refresh_gridview.onLoadComplete(true);
            }
        });
    }

    private void pageLoadLogic(SearchEnginesResponse response) {
        List<SearchObjectList> li = response.getData().getPage_result().getObject_list();
        if (pageIndex == 0 && (null == li || li.size() == 0)) {
            pull_to_refresh_gridview.showBlankView();
        } else {
            pull_to_refresh_gridview.hideAllTipView();
        }
        if (pageIndex == 0) {
            list.clear();
        }
        if (null == li || li.size() == 0)
            pull_to_refresh_gridview.onLoadComplete(false);
        else {
            pull_to_refresh_gridview.onLoadComplete(true);
        }
        if (null != li)
            list.addAll(li);

        //数据正常，进行显示
        if (adapter == null) {
            adapter = new GridViewAdapter(list);
            gridView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        pageIndex++;
    }

    class GridViewAdapter extends BaseAdapter {
        private List<SearchObjectList> list;
        private ImageLoader imageLoader;

        public GridViewAdapter(List<SearchObjectList> list) {
            this.list = list;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(getActivity(), R.layout.item_recommend_sports, null);
                vh.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                vh.iv_video_mark = (ImageView) convertView.findViewById(R.id.iv_video_mark);
                vh.view_right = convertView.findViewById(R.id.view_right);

                convertView.setTag(vh);
            } else
                vh = (ViewHolder) convertView.getTag();
            vh.view_right.setVisibility((position + 1) % 3 == 0 ? View.GONE : View.VISIBLE);
            vh.iv_video_mark.setVisibility(list.get(position).getObject_type() == 1 ? View.VISIBLE : View.INVISIBLE);
            imageLoader.displayImage(list.get(position).getObject_img_uri(), vh.imageView, new AnimateFirstDisplayListener(vh.imageView));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enterDetail(list.get(position).getObject_type(), list.get(position).getObject_id());
                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            ImageView iv_video_mark;
            View view_right;
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
}
