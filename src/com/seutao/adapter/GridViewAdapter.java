package com.seutao.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.seutao.core.R;
import com.seutao.sharedata.ShareData;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter {
//	int count;
	List<Map<String, Object>> kinds = null;
	Context context;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();


	public GridViewAdapter(Context context, List<Map<String, Object>> kinds) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.kinds = kinds;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return kinds.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return kinds.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stud
		System.out.println("1111111111111111111");
		ViewHolder holder;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.grid_view_item, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.imageView1);
//			holder.tv = (TextView) convertView.findViewById(R.id.textView1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ShareData.getImageLoader().displayImage(
				kinds.get(position).get("image").toString(), holder.iv,
				ShareData.getOptions(), animateFirstListener);
//		holder.tv.setText("数量 " + (position + 1) + " ");
		return convertView;
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

	class ViewHolder {
		ImageView iv;
//		TextView tv;
	}

}