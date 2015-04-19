package com.seutao.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.seutao.core.R;
import com.seutao.entity.PublishedNeed;

public class DelayNeedsAdapter extends BaseAdapter {
	private List<PublishedNeed>  mDelayNeeds= null;
	private int mLayoutId = 0;
	private Context mContext = null;
	public DelayNeedsAdapter(List<PublishedNeed> mDelayNeeds, int mLayoutId,Context mContext) {
		super();
		this.mDelayNeeds = mDelayNeeds;
		this.mLayoutId = mLayoutId;
		this.mContext = mContext;
	}
	public List<PublishedNeed> getmDelayNeeds() {
		return mDelayNeeds;
	}
	public void setmDelayNeeds(List<PublishedNeed> mDelayNeeds) {
		this.mDelayNeeds = mDelayNeeds;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDelayNeeds.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDelayNeeds.get(position);
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
			convertView = (View)inflater.inflate(mLayoutId,null);
			holder.tvName = (TextView) convertView.findViewById(R.id.collected_needs_list_item_tv_name);
			holder.tvTime = (TextView) convertView.findViewById(R.id.collected_needs_list_item_tv_time);
			holder.lastTimeTv=(TextView) convertView.findViewById(R.id.collected_needs_list_item_tv_soldouttime);
			holder.viewTv=(TextView)convertView.findViewById(R.id.collected_needs_list_item_visitCount);
			holder.priceTv=(TextView)convertView.findViewById(R.id.collected_needs_list_item_needsPrice);
			holder.cb = (CheckBox)convertView.findViewById(R.id.collected_needs_list_item_cb_choose);
			convertView.setTag(holder);
		}else{
			holder=(viewHolder)convertView.getTag();
		}
		PublishedNeed mPublishedNeed=(PublishedNeed)getItem(position);
		holder.tvName.setText(mPublishedNeed.getName());
		holder.tvTime.setText(mPublishedNeed.getTime());
		holder.viewTv.setText(mPublishedNeed.getView()+"");
		holder.priceTv.setText(mPublishedNeed.getPrice()+"");
		holder.cb.setChecked(mPublishedNeed.getIsSelect());
		int time=mPublishedNeed.getSoldouttime();
		if (time>0&&(mPublishedNeed.getCheck()==0)) 
			holder.lastTimeTv.setText("还有"+String.valueOf(time)+"天下架");
		else
			holder.lastTimeTv.setText("已解决");
		
		holder.cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDelayNeeds.get(index).oppositeIsSelect();
			}
		});
		return convertView;
	}
	public final class viewHolder{
		public TextView tvTime;
		public TextView tvName;
		public CheckBox cb;
		public TextView priceTv;
		public TextView viewTv;
		public TextView lastTimeTv;
 	}
}