package com.seutao.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
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
import com.seutao.core.R;
import com.seutao.core.UpNeedsDetailPage;
import com.seutao.entity.PublishedNeed;
import com.seutao.sharedata.ShareData;

public class PublishedNeedsAdapter extends BaseAdapter implements Callback {
	private int uid;
	private List<PublishedNeed>  mPublishedNeeds= null;
	private int mLayoutId = 0;
	private Context mContext = null;
	public List<PublishedNeed> getmPublishedNeeds() {
		return mPublishedNeeds;
	}
	public void setmPublishedNeeds(List<PublishedNeed> mPublishedNeeds) {
		this.mPublishedNeeds = mPublishedNeeds;
	}
	private RequestQueue mQueue = null;
	private String url=null;
	private Handler mHandler;
	private final static int DATA_CHANGE=21; 
	public PublishedNeedsAdapter(List<PublishedNeed> publishedNeeds, int mLayoutId,Context mContext,int uid) {
		super();
		mQueue=Volley.newRequestQueue(mContext);
		mHandler=new Handler(this);
		this.mPublishedNeeds = publishedNeeds;
		this.mLayoutId = mLayoutId;
		this.mContext = mContext;
		this.uid=uid;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPublishedNeeds.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mPublishedNeeds.get(position);
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
			convertView=(View)inflater.inflate(mLayoutId, parent, false);
			holder .tvName = (TextView) convertView.findViewById(R.id.published_needs_list_item_tv_name);
			holder. tvTime = (TextView) convertView.findViewById(R.id.published_needs_list_item_tv_time);
			holder. tvSoldOutTime = (TextView) convertView.findViewById(R.id.published_needs_list_item_tv_soldouttime);
			holder.btnSoldOut = (ImageView) convertView.findViewById(R.id.published_needs_list_item_btn_soldout);
			holder. btnEdit = (ImageView) convertView.findViewById(R.id.published_needs_list_item_btn_edit);
            holder.tvPrice=(TextView)convertView.findViewById(R.id.published_needs_list_item_tv_price);
            holder.tvView=(TextView)convertView.findViewById(R.id.published_needs_list_item_tv_visitCount);
            convertView.setTag(holder);
		}else{
			holder=(viewHolder)convertView.getTag();
		}
		final PublishedNeed mPublishedNeed=(PublishedNeed)getItem(position);
		holder.tvName.setText(mPublishedNeed.getName());
		holder.tvTime.setText(mPublishedNeed.getTime());
		holder.tvPrice.setText(mPublishedNeed.getPrice()+"");
		holder.tvView.setText(mPublishedNeed.getView()+"");
		if (uid==ShareData.MyId) {
			int time=mPublishedNeed.getSoldouttime();
			if (time>0&&(mPublishedNeed.getCheck()==0)) {
				holder.tvSoldOutTime.setText("还有"+String.valueOf(time)+"天下架");
				holder.btnSoldOut.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						setPublishedNeedSolved(mPublishedNeed.getId(), index);
						Toast.makeText(mContext,"第"+String.valueOf(index)+"个求购被解决！",Toast.LENGTH_LONG).show();
					}
				});
				holder.btnEdit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(mContext,UpNeedsDetailPage.class);
						intent.putExtra("source", "edit");
						intent.putExtra("wid", mPublishedNeed.getId()+"");
						mContext.startActivity(intent);
					}
				});
			}
			else {
				holder.tvSoldOutTime.setText("已解决");
				holder.btnSoldOut.setVisibility(View.GONE);
				holder.btnEdit.setVisibility(ViewGroup.GONE);
			}
		}
		else {
			holder.btnSoldOut.setVisibility(ViewGroup.GONE);
			holder.btnEdit.setVisibility(ViewGroup.GONE);
		}
		return convertView;
	}

	public void setPublishedNeedSolved(final int wid,final int index){
		url=ShareData.SEEVER_BASE_URL+"setPublishedNeedSolved.json";
		Map<String, String> appendHeader = new HashMap<String, String>();  
		appendHeader.put("wid", String.valueOf(wid));
		JsonObjectRequest mJsonObjectRequest=new JsonObjectRequest(url, new JSONObject(appendHeader), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				System.out.println(response.toString());
				try {
					String flag=response.getString("flag");
					if (flag.equals("ok")) {
						mPublishedNeeds.get(index).setCheck(1);
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
	public final class viewHolder{
		public TextView tvName;
		public TextView tvTime;
		public TextView tvSoldOutTime;
		public TextView tvPrice;
		public TextView tvView;
		public ImageView btnSoldOut;
		public ImageView btnEdit;
	}
	
}