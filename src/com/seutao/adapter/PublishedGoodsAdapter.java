package com.seutao.adapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.seutao.core.R;
import com.seutao.core.upGoodsPhotoPage;
import com.seutao.entity.PublishedGood;
import com.seutao.sharedata.ShareData;

public class PublishedGoodsAdapter extends BaseAdapter implements Callback {
	private int uid;
	private List<PublishedGood>  mPublishedGoods= null;
	private int mLayoutId = 0;
	private Context mContext = null;
	private RequestQueue mQueue = null;
	private String url=null;
	private Handler mHandler;
	private DisplayImageOptions options=null;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public List<PublishedGood> getmPublishedGoods() {
		return mPublishedGoods;
	}
	public void setmPublishedGoods(List<PublishedGood> mPublishedGoods) {
		this.mPublishedGoods = mPublishedGoods;
	}
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private final static int DATA_CHANGE=21; 
	
	public PublishedGoodsAdapter(List<PublishedGood> publishedGoods, int mLayoutId,Context mContext,DisplayImageOptions options,int uid) {
		super();
		mQueue=Volley.newRequestQueue(mContext);
		mHandler=new Handler(this);
		this.mPublishedGoods = publishedGoods;
		this.mLayoutId = mLayoutId;
		this.mContext = mContext;
		this.options=options;
		this.uid=uid;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPublishedGoods.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mPublishedGoods.get(position);
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
			holder=new viewHolder();
			convertView=(View)inflater.inflate(mLayoutId,parent,false);
			holder.tvName = (TextView) convertView.findViewById(R.id.published_goods_list_item_tv_name);
			holder.tvTime = (TextView) convertView.findViewById(R.id.published_goods_list_item_tv_time);
			holder.tvSoldOutTime = (TextView) convertView.findViewById(R.id.published_goods_list_item_tv_soldouttime);
			holder.btnSoldOut = (ImageView) convertView.findViewById(R.id.published_goods_list_item_btn_soldout);
			holder.ivGoodPic = (ImageView) convertView.findViewById(R.id.published_goods_list_item_iv_goodpic);
			holder.btnEdit = (ImageView) convertView.findViewById(R.id.published_goods_list_item_btn_edit);
			holder.viewTv=(TextView)convertView.findViewById(R.id.published_goods_list_item_tv_visitCount);
			holder.PriceTv=(TextView)convertView.findViewById(R.id.published_goods_list_item_tv_price);
			convertView.setTag(holder);
		}else{
			holder=(viewHolder)convertView.getTag();
		}
		final PublishedGood mPublishedGood=(PublishedGood)getItem(position);
		holder.tvName.setText(mPublishedGood.getName());
		holder.tvTime.setText(mPublishedGood.getTime());
		holder.PriceTv.setText(mPublishedGood.getPrice()+"");
		holder.viewTv.setText(mPublishedGood.getView()+"");
		if (uid==ShareData.MyId) {
			int time=mPublishedGood.getSoldouttime();
			if (time>0&&(mPublishedGood.getCheck()==1)) {
				holder.tvSoldOutTime.setText("还有"+String.valueOf(time)+"天下架");
				holder.btnSoldOut.setOnClickListener(new OnClickListener() {
					@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							setPublishedGoodSoldOut(mPublishedGood.getId(),index);
							Toast.makeText(mContext,"第"+String.valueOf(index)+"件商品下架！",Toast.LENGTH_LONG).show();
						}
					});
				holder.btnEdit.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(mContext,"第"+String.valueOf(index)+"件商品被编辑！",Toast.LENGTH_LONG).show();
							Intent intent=new Intent(mContext,upGoodsPhotoPage.class);
							intent.putExtra("source", "edit");
							intent.putExtra("photoPath", mPublishedGood.getImageUrl());
							intent.putExtra("gid", mPublishedGood.getId());
							mContext.startActivity(intent);
						}
					});
			}
			else {
				holder.tvSoldOutTime.setText("已下架");
				holder.btnSoldOut.setVisibility(View.GONE);
				holder.btnEdit.setVisibility(ViewGroup.GONE);
			}
			
		}
		else {
			holder.btnSoldOut.setVisibility(ViewGroup.GONE);
			holder.btnEdit.setVisibility(ViewGroup.GONE);
		}
		imageLoader.displayImage(mPublishedGood.getImageUrl(), holder.ivGoodPic, options, animateFirstListener);
		return convertView;
	}
	

	public void setPublishedGoodSoldOut(final int gid,final int index){
		url=ShareData.SEEVER_BASE_URL+"setPublishedGoodSoldOut.json";
		Map<String, String> appendHeader = new HashMap<String, String>();  
		appendHeader.put("gid", String.valueOf(gid));
		JsonObjectRequest mJsonObjectRequest=new JsonObjectRequest(url, new JSONObject(appendHeader), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				System.out.println(response.toString());
				try {
					String flag=response.getString("flag");
					if (flag.equals("ok")) {
						mPublishedGoods.get(index).setCheck(1);
						Message msg=new Message();
						msg.what=DATA_CHANGE;
						mHandler.sendMessage(msg);
					}
					else {
						Toast.makeText(mContext, flag, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				System.out.println("myJSReq--json receive failed!");
				Toast.makeText(mContext, "连接服务器失败！", Toast.LENGTH_SHORT).show();
			}
		});
		mQueue.add(mJsonObjectRequest); 
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==DATA_CHANGE){
			this.notifyDataSetChanged();
		}
		return false;
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
		public TextView tvSoldOutTime;
		public ImageView ivGoodPic;
		public ImageView btnSoldOut;
		public ImageView btnEdit;
		public TextView PriceTv;
		public TextView viewTv;
	}
}