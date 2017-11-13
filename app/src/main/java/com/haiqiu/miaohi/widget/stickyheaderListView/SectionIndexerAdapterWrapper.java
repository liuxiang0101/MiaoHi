package com.haiqiu.miaohi.widget.stickyheaderListView;

import android.content.Context;
import android.widget.SectionIndexer;

public class SectionIndexerAdapterWrapper extends
		AdapterWrapper implements SectionIndexer {
	
	public SectionIndexer mSectionIndexerDelegate;

	public SectionIndexerAdapterWrapper(Context context,
			StickyListHeadersAdapter delegate) {
		super(context, delegate);
		mSectionIndexerDelegate = (SectionIndexer) delegate;
	}

	@Override
	public int getPositionForSection(int section) {
		return mSectionIndexerDelegate.getPositionForSection(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		return mSectionIndexerDelegate.getSectionForPosition(position);
	}

	@Override
	public Object[] getSections() {
		return mSectionIndexerDelegate.getSections();
	}

}
