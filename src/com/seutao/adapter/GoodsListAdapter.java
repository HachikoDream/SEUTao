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

public class GoodsListAdapter extends BaseAdapter {
	private List<Map<String, Object>> goods;
	private LayoutInflater mInflater = null;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public GoodsListAdapter(Context context, List<Map<String, Object>> goods) {
		mInflater = LayoutInflater.from(context);
		this.goods = goods;
	}

	public List<Map<String, Object>> getGoods() {
		return goods;
	}

	public void setGoods(List<Map<String, Object>> goods) {
		this.goods = goods;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return goods.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return goods.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		GoodsListItem item = null;
		if (view == null) {
			item = new GoodsListItem();
			view = mInflater.inflate(R.layout.goods_list_item, parent,false);
			item.image = (ImageView) view
					.findViewById(R.id.goods_list_item_image);
			item.name = (TextView) view.findViewById(R.id.goods_list_item_name);
			item.view = (TextView) view.findViewById(R.id.goods_list_item_view);
			item.price = (TextView) view
					.findViewById(R.id.goods_list_item_price);
			item.time = (TextView) view.findViewById(R.id.goods_list_item_time);
			view.setTag(item);
		} else {
			item = (GoodsListItem) view.getTag();
		}
		ShareData.getImageLoader().displayImage(
				goods.get(position).get("image").toString(), item.image,
				ShareData.getOptions(), animateFirstListener);

		item.name.setText(goods.get(position).get("gname").toString());
		item.view.setText(goods.get(position).get("view").toString());
		item.price.setText("￥" + goods.get(position).get("price").toString());
		item.time.setText(goods.get(position).get("date").toString());
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

class GoodsListItem {
	public ImageView image = null;
	public TextView name = null;
	public TextView view = null;
	public TextView price = null;
	public TextView time = null;
}