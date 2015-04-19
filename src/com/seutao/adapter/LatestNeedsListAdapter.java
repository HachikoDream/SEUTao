package com.seutao.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.seutao.core.R;
import com.seutao.entity.LatestWant;

public class LatestNeedsListAdapter extends BaseAdapter {
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private LayoutInflater layoutInflater;
	private List<LatestWant> latestWants;
	private DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public LatestNeedsListAdapter(Context c, List<LatestWant> latestWants,
			DisplayImageOptions options) {
		this.layoutInflater = LayoutInflater.from(c);
		this.latestWants = latestWants;
		this.options = options;
	}

	public List<LatestWant> getLatestWants() {
		return latestWants;
	}

	public void setLatestWants(List<LatestWant> latestWants) {
		this.latestWants = latestWants;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return latestWants.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		viewHolderOfList holder = null;
		if (convertView == null) {
			holder = new viewHolderOfList();
			convertView = layoutInflater.inflate(R.layout.home_page_two_listviewitem,
					parent,false);
			holder.needsUserAvaterIv = (ImageView) convertView
					.findViewById(R.id.home_page_two_avater);
			holder.needsTitleTv = (TextView) convertView
					.findViewById(R.id.home_page_two_needsTitle);
			holder.needsVisitCountTv = (TextView) convertView
					.findViewById(R.id.home_page_two_visitCount);
			holder.needsPriceTv = (TextView) convertView
					.findViewById(R.id.home_page_two_needsPrice);
			holder.needsTimeTv = (TextView) convertView
					.findViewById(R.id.home_page_two_uptime);
			holder.needsUserNameTv = (TextView) convertView
					.findViewById(R.id.home_page_two_userName);
			convertView.setTag(holder);
		} else {
			holder = (viewHolderOfList) convertView.getTag();
		}
		LatestWant latestWant = latestWants.get(position);
		holder.needsPriceTv.setText(latestWant.getWantPrice() + "");
		holder.needsTimeTv.setText(latestWant.getTimescap());
		holder.needsVisitCountTv.setText(latestWant.getView() + "");
		holder.needsTitleTv.setText(latestWant.getWantTitle());
		holder.needsUserNameTv.setText(latestWant.getUserName());
		imageLoader.displayImage(latestWant.getImageURL(),
				holder.needsUserAvaterIv, options, animateFirstListener);
		return convertView;
	}

	/**
	 * 图片加载第一次显示监听器
	 * 
	 * @author Administrator
	 * 
	 */
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

	public final class viewHolderOfList {

		public ImageView needsUserAvaterIv;
		public TextView needsTitleTv;
		public TextView needsVisitCountTv;
		public TextView needsPriceTv;
		public TextView needsTimeTv;
		public TextView needsUserNameTv;
	}

}
