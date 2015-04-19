package com.seutao.adapter;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.seutao.core.R;
import com.seutao.sharedata.ShareData;

/**
 * Adapter for countries
 */
public class ClassifyAdapter extends AbstractWheelTextAdapter {
	// Countries names

	/**
	 * Constructor
	 */
	public ClassifyAdapter(Context context) {
		super(context, R.layout.up_goods_page_two_selectclassifyitem, NO_RESOURCE);

		setItemTextResource(R.id.up_goods_firstclassify_name);
	}

	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		View view = super.getItem(index, cachedView, parent);
		return view;
	}

	@Override
	public int getItemsCount() {
		return ShareData.FirstClassify.length;
	}

	@Override
	protected CharSequence getItemText(int index) {
		return ShareData.FirstClassify[index];
	}
}