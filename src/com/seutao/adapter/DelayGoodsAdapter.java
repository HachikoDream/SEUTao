package com.seutao.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.seutao.core.R;
import com.seutao.entity.PublishedGood;

public class DelayGoodsAdapter extends BaseAdapter {
	public List<PublishedGood> getmDelayGoods() {
		return mDelayGoods;
	}
	public void setmDelayGoods(List<PublishedGood> mDelayGoods) {
		this.mDelayGoods = mDelayGoods;
	}
	private List<PublishedGood>  mDelayGoods= null;
	private int mLayoutId = 0;
	private Context mContext = null;
	private DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	public DelayGoodsAdapter(List<PublishedGood> mDelayGoods, int mLayoutId,Context mContext,DisplayImageOptions options) {
		super();
		this.mDelayGoods = mDelayGoods;
		this.mLayoutId = mLayoutId;
		this.mContext = mContext;
		this.options=options;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDelayGoods.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDelayGoods.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final int index=position;
		viewHolder holder=null;
		if(convertView==null){
			convertView = (View)inflater.inflate(mLayoutId,null);	
			holder=new viewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.collected_goods_list_item_tv_name);
			holder.tvTime = (TextView) convertView.findViewById(R.id.collected_goods_list_item_tv_time);
			holder.cb = (CheckBox)convertView.findViewById(R.id.collected_goods_list_item_cb_choose);
			holder.ivGoodPic = (ImageView) convertView.findViewById(R.id.collected_goods_list_item_iv_goodpic);
			holder.leftTimeTv=(TextView)convertView.findViewById(R.id.collected_goods_list_item_tv_soldouttime);
			holder.priceTv=(TextView)convertView.findViewById(R.id.collected_goods_list_item_tv_price);
			holder.viewTv=(TextView)convertView.findViewById(R.id.collected_goods_list_item_tv_visitCount);
			convertView.setTag(holder);
		}else{
			holder=(viewHolder)convertView.getTag();
		}

		PublishedGood mPublishedGood=(PublishedGood)getItem(position);
		holder.tvName.setText(mPublishedGood.getName());
		holder.tvTime.setText(mPublishedGood.getTime());
		holder.cb.setChecked(mPublishedGood.getIsSelect());
		holder.viewTv.setText(mPublishedGood.getView()+"");
		holder.priceTv.setText(mPublishedGood.getPrice()+"");
		int time=mPublishedGood.getSoldouttime();
		if (time>0&&(mPublishedGood.getCheck()==0)) 
			holder.leftTimeTv.setText("还有"+String.valueOf(time)+"天下架");
		else
			holder.leftTimeTv.setText("已下架");
		holder.cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDelayGoods.get(index).oppositeIsSelect();
			}
		});
		imageLoader.displayImage(mPublishedGood.getImageUrl(), holder.ivGoodPic, options, animateFirstListener);
		return convertView;
	}
private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		
		
		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
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
	public final class viewHolder{
		public TextView tvName;
		public TextView tvTime;
		public CheckBox cb;
		public ImageView ivGoodPic;
		public TextView leftTimeTv;
		public TextView priceTv;
		public TextView viewTv;
	}
}