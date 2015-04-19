package com.seutao.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.seutao.core.R;
import com.seutao.sharedata.ShareData;

public class NeedsListAdapter extends BaseAdapter {
	private List<Map<String, Object>> needs = null;
	private LayoutInflater mInflater = null;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public NeedsListAdapter(Context context, List<Map<String, Object>> needs) {
		mInflater = LayoutInflater.from(context);
		this.needs = needs;
	}

	public List<Map<String, Object>> getNeeds() {
		return needs;
	}

	public void setNeeds(List<Map<String, Object>> needs) {
		this.needs = needs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return needs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return needs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		NeedsListItem item = null;
		if (view == null) {
			item = new NeedsListItem();
			view = mInflater.inflate(R.layout.needs_list_item, parent,false);
			item.title = (TextView) view
					.findViewById(R.id.needs_list_item_title);
			item.image = (ImageView) view
					.findViewById(R.id.needs_list_item_image);
			item.view = (TextView) view.findViewById(R.id.needs_list_item_view);
			item.name = (TextView) view.findViewById(R.id.needs_list_item_name);
			item.price = (TextView) view
					.findViewById(R.id.needs_list_item_price);
			item.time=(TextView)view.findViewById(R.id.needs_list_item_time);
			view.setTag(item);
		} else {
			item = (NeedsListItem) view.getTag();
		}
		item.title.setText(needs.get(position).get("title").toString());
		ShareData.getImageLoader().displayImage(
				needs.get(position).get("uimage").toString(),
				item.image, ShareData.getRoundOptions(), animateFirstListener);
		item.name.setText(needs.get(position).get("uname").toString());
		item.view.setText(needs.get(position).get("view")+"");
		item.price.setText("￥"+needs.get(position).get("price").toString());
		item.time.setText(needs.get(position).get("date").toString());
		return view;
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

}

class NeedsListItem {
	public TextView title;
	public ImageView image;
	public TextView name;
	public TextView view;
	public TextView price;
	public TextView time;
}
