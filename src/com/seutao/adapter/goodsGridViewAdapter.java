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
import com.seutao.entity.IntroduceGood;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class goodsGridViewAdapter extends BaseAdapter{
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private LayoutInflater mInflater;
	private List<IntroduceGood> intrList;
	private DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public goodsGridViewAdapter(Context context,List<IntroduceGood> intrList,DisplayImageOptions options){
		this.mInflater=LayoutInflater.from(context);
		this.intrList=intrList;
		this.options=options;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return intrList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 ViewHolder holder = null;
			
			//如果当前View中没用holder 新建并初始化
			if (convertView == null){
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.home_page_one_gridviewitem, parent,false);
				holder.priceTv=(TextView)convertView.findViewById(R.id.home_page_one_price);
				holder.goodsTitleTv=(TextView)convertView.findViewById(R.id.home_page_one_gridview_goodsName);
				holder.imaView=(ImageView)convertView.findViewById(R.id.home_page_one_gridview_goodsImage);
				holder.timeTv=(TextView)convertView.findViewById(R.id.home_page_one_time);
				holder.visitCountTv=(TextView)convertView.findViewById(R.id.home_page_one_visitCount);
				convertView.setTag(holder);
	        }
			else{
				holder=(ViewHolder)convertView.getTag();
			}
			IntroduceGood mIntroduceGood=intrList.get(position);
			holder.priceTv.setText(mIntroduceGood.getGoodsPrice()+"");
			holder.goodsTitleTv.setText(mIntroduceGood.getGoodsName()+"");
			System.out.println("time in getview :"+mIntroduceGood.getTimescap());
			holder.timeTv.setText(mIntroduceGood.getTimescap());
			holder.visitCountTv.setText(mIntroduceGood.getView()+"");
			imageLoader.displayImage(mIntroduceGood.getGoodsImage(), holder.imaView, options, animateFirstListener);
			return convertView;

}
	public List<IntroduceGood> getIntrList() {
		return intrList;
	}
	public void setIntrList(List<IntroduceGood> intrList) {
		this.intrList = intrList;
	}
	/**
	 * 图片加载第一次显示监听器
	 * @author Administrator
	 *
	 */
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
		public ImageView imaView;
		public TextView goodsTitleTv;
		public TextView visitCountTv;
		public TextView timeTv;
		public TextView priceTv;
	}	

}