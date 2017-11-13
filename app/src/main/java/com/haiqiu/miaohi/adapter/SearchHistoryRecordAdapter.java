package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.SearchBodyActivity;
import com.haiqiu.miaohi.activity.SearchHistoryRecordActivity;
import com.haiqiu.miaohi.utils.SpUtils;

import java.util.List;

/**
 * Created by LiuXiang on 2016/11/29.
 */
public class SearchHistoryRecordAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private Gson gson;
    private int searchType;

    public SearchHistoryRecordAdapter(Context context, List<String> list, int searchType) {
        gson = new Gson();
        this.context = context;
        this.list = list;
        this.searchType = searchType;
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
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final View view;
        ViewHolder vh;
        if (convertView == null || ((ViewHolder) convertView.getTag()).needInflate) {
            view = View.inflate(context, R.layout.item_search_history_record, null);
            vh = new ViewHolder();
            vh.textView = (TextView) view.findViewById(R.id.textView);
            vh.imageView = (ImageView) view.findViewById(R.id.imageView);
            vh.needInflate = false;
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }

        final int showPostion = list.size() - 1 - position;
        vh.textView.setText(list.get(showPostion));
        vh.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() == 1)
                    ((SearchHistoryRecordActivity) context).setfooterViewText();

                deleteCell(view, showPostion);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchBodyActivity.class);
                intent.putExtra("keyWords", list.get(showPostion));
                intent.putExtra("searchType", searchType);
                ((SearchHistoryRecordActivity) context).startActivityNoAnimation(intent);
                ((SearchHistoryRecordActivity) context).finish();
            }
        });
        return view;
    }

    private class ViewHolder {
        TextView textView;
        ImageView imageView;
        boolean needInflate;
    }

    private void deleteCell(final View v, final int index) {
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                //删除单条的历史记录
                list.remove(index);
                SpUtils.put("history_record", gson.toJson(list));
                ViewHolder vh = (ViewHolder) v.getTag();
                vh.needInflate = true;

                notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        };

        collapse(v, al);
    }

    private void collapse(final View v, Animation.AnimationListener al) {
        final int initialHeight = v.getMeasuredHeight();

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if (al != null) {
            anim.setAnimationListener(al);
        }
        anim.setDuration(300);
        v.startAnimation(anim);
    }
}
