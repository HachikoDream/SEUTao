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
import android.widget.TextView;

public class UsersListAdapter extends BaseAdapter {
	private List<Map<String, Object>> users;
	private LayoutInflater mInflater = null;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public UsersListAdapter(Context context, List<Map<String, Object>> goods) {
		mInflater = LayoutInflater.from(context);
		this.users = goods;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return users.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		UserItem item = null;
		if (view == null) {
			item = new UserItem();
			view = mInflater.inflate(R.layout.users_list_item, parent,false);
			item.image = (ImageView) view
					.findViewById(R.id.users_list_item_image);
			item.name = (TextView) view.findViewById(R.id.users_list_item_name);
			item.sign = (TextView) view.findViewById(R.id.users_list_item_sign);
			view.setTag(item);
		} else {
			item = (UserItem) view.getTag();
		}
		// item.image.setBackgroundResource((Integer)goods.get(position).get("image"));
		ShareData.getImageLoader().displayImage(
				users.get(position).get("uimage").toString(), item.image,
				ShareData.getRoundOptions(), animateFirstListener);

		item.name.setText(users.get(position).get("uname").toString());
		item.sign.setText(users.get(position).get("sign").toString());
		return view;
	}
	
	public class UserItem {
		public ImageView image = null;
		public TextView name = null;
		public TextView sign = null;
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
