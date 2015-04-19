package com.seutao.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.seutao.core.R;
import com.seutao.entity.CollectedGood;
import com.seutao.sharedata.ShareData;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class CollectedGoodsAdapter extends BaseAdapter {
	private List<CollectedGood>  mCollectedGoods= null;
	private int mLayoutId = 0;
	private Context mContext = null;
	private DisplayImageOptions options=null;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	public CollectedGoodsAdapter(List<CollectedGood> collectedGoods, int mLayoutId,Context mContext, DisplayImageOptions options) {
		super();
		this.mCollectedGoods = collectedGoods;
		this.mLayoutId = mLayoutId;
		this.mContext = mContext;
		this.options=options;
	}
	public List<CollectedGood> getmCollectedGoods() {
		return mCollectedGoods;
	}
	public void setmCollectedGoods(List<CollectedGood> mCollectedGoods) {
		this.mCollectedGoods = mCollectedGoods;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCollectedGoods.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mCollectedGoods.get(position);
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
		ViewHolder holder=null;
		if(convertView==null){
			convertView= (View)inflater.inflate(mLayoutId,parent,false);
			holder=new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.collected_goods_list_item_tv_name);
			holder.tvTime = (TextView) convertView.findViewById(R.id.collected_goods_list_item_tv_time);
			holder.cb = (CheckBox)convertView.findViewById(R.id.collected_goods_list_item_cb_choose);
			holder.ivGoodPic = (ImageView) convertView.findViewById(R.id.collected_goods_list_item_iv_goodpic);
			holder.priceTv=(TextView)convertView.findViewById(R.id.collected_goods_list_item_tv_price);
			holder.viewCount=(TextView)convertView.findViewById(R.id.collected_goods_list_item_tv_visitCount);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		CollectedGood mCollectedGood=(CollectedGood)getItem(position);
		holder.tvName.setText(mCollectedGood.getName());
		holder.tvTime.setText(mCollectedGood.getTime());
		holder.priceTv.setText(mCollectedGood.getPrice()+"");
		holder.viewCount.setText(mCollectedGood.getView()+"");
		if (!ShareData.collectedIsEdit) 
			holder.cb.setVisibility(View.INVISIBLE);
		else{
			holder.cb.setVisibility(View.VISIBLE);
			holder.cb.setChecked(mCollectedGood.getIsSelect());
		}
			
		
		holder.cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCollectedGoods.get(index).oppositeIsSelect();
			}
		});
		imageLoader.displayImage(mCollectedGood.getImageUrl(), holder.ivGoodPic, options, animateFirstListener);
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
public final class ViewHolder{
	public TextView tvName;
	public TextView tvTime;
	public CheckBox cb;
	public ImageView ivGoodPic;
	public TextView viewCount;
	public TextView priceTv;
}	
}