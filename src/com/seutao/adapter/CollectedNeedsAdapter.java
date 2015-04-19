package com.seutao.adapter;

import java.util.List;
import com.seutao.core.R;
import com.seutao.entity.CollectedNeed;
import com.seutao.sharedata.ShareData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CollectedNeedsAdapter extends BaseAdapter {
	private List<CollectedNeed>  mCollectedNeeds= null;
	private int mLayoutId = 0;
	private Context mContext = null;
	public CollectedNeedsAdapter(List<CollectedNeed> collectedNeeds, int mLayoutId,Context mContext) {
		super();
		this.mCollectedNeeds = collectedNeeds;
		this.mLayoutId = mLayoutId;
		this.mContext = mContext;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCollectedNeeds.size();
	}
	public List<CollectedNeed> getmCollectedNeeds() {
		return mCollectedNeeds;
	}
	public void setmCollectedNeeds(List<CollectedNeed> mCollectedNeeds) {
		this.mCollectedNeeds = mCollectedNeeds;
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mCollectedNeeds.get(position);
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
			convertView = (LinearLayout)inflater.inflate(mLayoutId,null);
			holder.tvName = (TextView) convertView.findViewById(R.id.collected_needs_list_item_tv_name);
			holder.tvTime = (TextView) convertView.findViewById(R.id.collected_needs_list_item_tv_time);
			holder.cb = (CheckBox)convertView.findViewById(R.id.collected_needs_list_item_cb_choose);
			holder.priceTv=(TextView)convertView.findViewById(R.id.collected_needs_list_item_needsPrice);
			holder.viewCountTv=(TextView)convertView.findViewById(R.id.collected_needs_list_item_visitCount);
			convertView.setTag(holder);
		}else{
			holder=(viewHolder)convertView.getTag();
		}
		CollectedNeed mCollectedNeed=(CollectedNeed)getItem(position);
		holder.tvName.setText(mCollectedNeed.getName());
		holder.tvTime.setText(mCollectedNeed.getTime());
		holder.priceTv.setText(mCollectedNeed.getPrice()+"");
		holder.viewCountTv.setText(mCollectedNeed.getView()+"");
		if (!ShareData.collectedIsEdit) 
			holder.cb.setVisibility(View.INVISIBLE);
		else{
			holder.cb.setVisibility(View.VISIBLE);
			holder.cb.setChecked(mCollectedNeed.getIsSelect());
		}
		
		holder.cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCollectedNeeds.get(index).oppositeIsSelect();
			}
		});
		return convertView;
	}
	public final class viewHolder{
		public TextView tvName;
		public TextView tvTime;
		public CheckBox cb; 
		public TextView viewCountTv;
		public TextView priceTv;
	}
}