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

public class CommentListAdapter extends BaseAdapter {
	private List<Map<String, Object>> comments = null;
	private LayoutInflater mInflater = null;
	private int _uid = 0;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public CommentListAdapter(Context context,
			List<Map<String, Object>> comments, int _uid) {
		mInflater = LayoutInflater.from(context);
		this.comments = comments;
		this._uid = _uid;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		CommentsListItem item = null;
		if (view == null) {
			item = new CommentsListItem();
			view = mInflater.inflate(R.layout.comment_item, parent,false);
			item.image = (ImageView) view.findViewById(R.id.comment_item_image);
			item.name = (TextView) view.findViewById(R.id.comment_item_name);
			item.content = (TextView) view.findViewById(R.id.comment_item_content);
			item.comment = (TextView) view
					.findViewById(R.id.comment_item_comment);
			item.time = (TextView) view.findViewById(R.id.comment_item_time);
			view.setTag(item);
		} else {
			item = (CommentsListItem) view.getTag();
		}

	ShareData.getImageLoader().displayImage(
		comments.get(position).get("uimage").toString(),
				item.image, ShareData.getRoundOptions(), animateFirstListener);
		item.name.setText(comments.get(position).get("uname").toString());
		int touid = Integer.parseInt(comments.get(position).get("touid")
				.toString());
		if (_uid == touid) {
			item.content.setText("评论内容：");
		} else {
			item.content.setText("回复 " + comments.get(position).get("tousername")
									.toString() + "：");
		}

		item.comment.setText(comments.get(position).get("comment").toString());
		item.time.setText(comments.get(position).get("commentdate") + "");
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

class CommentsListItem {
	public ImageView image = null;
	public TextView name = null;
	public TextView content = null;
	public TextView comment = null;
	public TextView time = null;
}
