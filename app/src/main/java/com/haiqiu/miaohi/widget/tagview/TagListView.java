/**
 *
 */
package com.haiqiu.miaohi.widget.tagview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;

import com.haiqiu.miaohi.R;

import java.util.ArrayList;
import java.util.List;

public class TagListView extends FlowLayout implements OnClickListener {
    private boolean mIsDeleteMode;
    private OnTagCheckedChangedListener mOnTagCheckedChangedListener;
    private OnTagClickListener mOnTagClickListener;
    private int mTagViewBackgroundResId;
    private int mTagViewTextColorResId;
    private final List<Tag> mTags = new ArrayList<>();
    private int num = 0;

    public TagListView(Context context) {
        super(context);
        init();
    }

    public TagListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public TagListView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init();
    }

    @Override
    void getShowedNumber(int num) {
        this.num = num;
        mOnTagClickListener.onGetViewNum(num);
    }


    @Override
    public void onClick(View v) {
        if ((v instanceof TagView)) {
            Tag localTag = (Tag) v.getTag();
            if (this.mOnTagClickListener != null) {
                this.mOnTagClickListener.onTagClick((TagView) v, localTag);
            }
        }
    }

    private void init() {

    }

    private void inflateTagView(final Tag tag, boolean isCheckEnable) {
        //添加一个标签view
        TagView localTagView = (TagView) View.inflate(getContext(), R.layout.tag, null);
        localTagView.setText(tag.getTitle());
        localTagView.setTag(tag);

        if (mTagViewTextColorResId <= 0) {
            int c = getResources().getColor(R.color.color_c4);
            localTagView.setTextColor(c);
        }

        if (mTagViewBackgroundResId <= 0) {
            mTagViewBackgroundResId = R.drawable.tag_bg;
            localTagView.setBackgroundResource(mTagViewBackgroundResId);
        }

        localTagView.setChecked(tag.isChecked());
        localTagView.setCheckEnable(isCheckEnable);
        if (mIsDeleteMode) {
            int k = (int) TypedValue.applyDimension(1, 5.0F, getContext().getResources().getDisplayMetrics());
            localTagView.setPadding(localTagView.getPaddingLeft(),
                    localTagView.getPaddingTop(), k,
                    localTagView.getPaddingBottom());
            localTagView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.forum_tag_close, 0);
        }
        if (tag.getBackgroundResId() > 0) {
            localTagView.setBackgroundResource(tag.getBackgroundResId());
        }
        if ((tag.getLeftDrawableResId() > 0) || (tag.getRightDrawableResId() > 0)) {
            localTagView.setCompoundDrawablesWithIntrinsicBounds(tag.getLeftDrawableResId(), 0, tag.getRightDrawableResId(), 0);
        }
        localTagView.setOnClickListener(this);
        localTagView
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(
                            CompoundButton paramAnonymousCompoundButton,
                            boolean paramAnonymousBoolean) {
                        tag.setChecked(paramAnonymousBoolean);
                        if (TagListView.this.mOnTagCheckedChangedListener != null) {
                            TagListView.this.mOnTagCheckedChangedListener
                                    .onTagCheckedChanged(
                                            (TagView) paramAnonymousCompoundButton,
                                            tag);
                        }
                    }
                });
        addView(localTagView);
    }

    public void addTag(int id, String title) {
        addTag(id, title, true);
    }

    public void addTag(int id, String title, boolean isCheckEnable) {
        addTag(new Tag(id, title), isCheckEnable);
    }

    public void addTag(Tag tag) {
        addTag(tag, true);
    }

    public void addTag(Tag tag, boolean isCheckEnable) {
        mTags.add(tag);
        inflateTagView(tag, isCheckEnable);
    }

    public void addTags(List<Tag> lists) {
        addTags(lists, true);
    }

    public void addTags(List<Tag> lists, boolean isCheckEnable) {
        for (int i = 0; i < lists.size(); i++) {
            addTag(lists.get(i), isCheckEnable);
        }
    }

    public List<Tag> getTags() {
        return mTags;
    }

    public View getViewByTag(Tag tag) {
        return findViewWithTag(tag);
    }

    public void removeTag(Tag tag) {
        mTags.remove(tag);
        removeView(getViewByTag(tag));
    }

    public void setDeleteMode(boolean b) {
        mIsDeleteMode = b;
    }

    public void setOnTagCheckedChangedListener(
            OnTagCheckedChangedListener onTagCheckedChangedListener) {
        mOnTagCheckedChangedListener = onTagCheckedChangedListener;
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        mOnTagClickListener = onTagClickListener;
    }

    public void setTagViewBackgroundRes(int res) {
        mTagViewBackgroundResId = res;
    }

    public void setTagViewTextColorRes(int res) {
        mTagViewTextColorResId = res;
    }

    public void setTags(List<? extends Tag> lists) {
        setTags(lists, true);
    }

    public void setTags(List<? extends Tag> lists, boolean isCheckEnable) {
        removeAllViews();
        mTags.clear();
        for (int i = 0; i < lists.size(); i++) {
            addTag(lists.get(i), isCheckEnable);
        }
    }

    public interface OnTagCheckedChangedListener {
        void onTagCheckedChanged(TagView tagView, Tag tag);
    }

    public interface OnTagClickListener {
        void onTagClick(TagView tagView, Tag tag);
        void onGetViewNum(int num);
    }

}
