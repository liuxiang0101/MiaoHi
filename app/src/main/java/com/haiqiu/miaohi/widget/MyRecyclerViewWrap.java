package com.haiqiu.miaohi.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by zhandalin on 2017-02-16 15:40.
 * 说明:RecyclerView的头部停留,目前没有写回收HeaderView的逻辑,哪天心情好了就加上
 * 网上找了一圈都没有好的解决方案,MD自己写,这么简单的逻辑为什么搞得这么复杂?
 */
public class MyRecyclerViewWrap extends RelativeLayout {
    private final String TAG = "MyRecyclerViewWrap";
    private RecyclerView myRecyclerView;
    private int lastHeaderPosition = -1;
    private int headerHeight;//高最好一致,headerHeight小于item的高度
    private View headerView;
    private RecyclerView.ViewHolder viewHolder;
    public boolean hasRegister;
    private RecyclerView.Adapter adapter;
    private int firstVisibleItemPosition;


    public MyRecyclerViewWrap(Context context) {
        this(context, null);
    }

    public MyRecyclerViewWrap(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerViewWrap(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        myRecyclerView = new RecyclerView(context, attrs);
        addView(myRecyclerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                View viewByPosition = layoutManager.findViewByPosition(firstVisibleItemPosition);

//                MHLogUtil.d(TAG, "dx=" + dx + "---dy=" + dy + "--firstVisibleItemPosition=" + firstVisibleItemPosition + "---viewByPosition=" + viewByPosition.getTop());
                if (dy >= 0) {//上滑
                    int tempTop = viewByPosition.getTop();
                    if (tempTop < -headerHeight) {//这种表示这一条已经滑太多了,需要检测下一个item
                        View viewNext = layoutManager.findViewByPosition(firstVisibleItemPosition + 1);
                        if (null != viewNext) {
                            tempTop = viewNext.getTop();
                            if (null != headerView && tempTop - headerHeight < 0)
                                headerView.setY(tempTop - headerHeight);
                        }
                    } else if (null != headerView) {
                        headerView.setY(0);
                    }
                } else {//下滑
                    View nextView = layoutManager.findViewByPosition(firstVisibleItemPosition + 1);
                    if (null != nextView && nextView.getTop() > 0 && nextView.getTop() < headerHeight) {
                        if (null != headerView)
                            headerView.setY(nextView.getTop() - headerHeight);
                    } else {
                        if (null != headerView)
                            headerView.setY(0);
                    }
                }
                adapter = myRecyclerView.getAdapter();
                if (null != adapter && !hasRegister) {
                    hasRegister = true;
                    adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onChanged() {
                            super.onChanged();
                            lastHeaderPosition = -1;
                            updateHeaderView(adapter, firstVisibleItemPosition);
                        }

                        @Override
                        public void onItemRangeChanged(int positionStart, int itemCount) {
                            super.onItemRangeChanged(positionStart, itemCount);
                            lastHeaderPosition = -1;
                            updateHeaderView(adapter, firstVisibleItemPosition);
                        }

                        @Override
                        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                            super.onItemRangeChanged(positionStart, itemCount, payload);
                            lastHeaderPosition = -1;
                            updateHeaderView(adapter, firstVisibleItemPosition);
                        }

                        @Override
                        public void onItemRangeInserted(int positionStart, int itemCount) {
                            super.onItemRangeInserted(positionStart, itemCount);
                            lastHeaderPosition = -1;
                            updateHeaderView(adapter, firstVisibleItemPosition);
                        }

                        @Override
                        public void onItemRangeRemoved(int positionStart, int itemCount) {
                            super.onItemRangeRemoved(positionStart, itemCount);
                            lastHeaderPosition = -1;
                            updateHeaderView(adapter, firstVisibleItemPosition);
                        }

                        @Override
                        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                            lastHeaderPosition = -1;
                            updateHeaderView(adapter, firstVisibleItemPosition);
                        }
                    });
                }
                updateHeaderView(adapter, firstVisibleItemPosition);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void updateHeaderView(RecyclerView.Adapter adapter, int position) {
        if (null == adapter) return;

        if (adapter instanceof HeaderAdapterInterface && lastHeaderPosition != position) {
            HeaderAdapterInterface adapterInterface = (HeaderAdapterInterface) adapter;
            if (null == headerView || null == viewHolder) {
                for (int i = 1; i < MyRecyclerViewWrap.this.getChildCount(); i++) {
                    MyRecyclerViewWrap.this.removeViewAt(i);
                }
                viewHolder = adapterInterface.onCreateHeaderViewHolder(this, position);
                headerView = viewHolder.itemView;
                headerHeight = headerView.getLayoutParams().height;
                MyRecyclerViewWrap.this.addView(headerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            adapterInterface.onBindHeaderViewHolder(viewHolder, position);
            lastHeaderPosition = position;
        }
    }

    public RecyclerView getRecyclerView() {
        return myRecyclerView;
    }
}
