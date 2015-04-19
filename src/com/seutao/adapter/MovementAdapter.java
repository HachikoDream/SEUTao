package com.seutao.adapter;

import java.util.List;
import com.seutao.core.R;
import com.seutao.entity.Movement;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MovementAdapter extends BaseAdapter {
	private Context mContext;
	private List<Movement> movementList;
   private static final int SYS=0;
   private static final int GOODS=1;
   private static final int NEEDS=2;
	public MovementAdapter(Context mContext, List<Movement> movementList) {
		super();
		this.mContext = mContext;
		this.movementList = movementList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return movementList.size();
	}

	@Override
	public Object getItem(int positon) {
		// TODO Auto-generated method stub
		return movementList.get(positon);
	}

	public List<Movement> getMovementList() {
		return movementList;
	}

	public void setMovementList(List<Movement> movementList) {
		this.movementList = movementList;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		System.out.println("getview in!");
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewHolder holder=null;
       if(convertView==null){
    	   convertView=(View)inflater.inflate(R.layout.movementlist_item, parent, false);
    	   holder=new viewHolder();
    	   holder.contentTv=(TextView)convertView.findViewById(R.id.movement_list_item_content);
    	   holder.timeTv=(TextView)convertView.findViewById(R.id.movement_list_item_time);
    	   holder.movementIcon=(ImageView)convertView.findViewById(R.id.movement_list_item_icon);
    	   holder.bgRv=(RelativeLayout)convertView.findViewById(R.id.movement_list_item_bg);
    	   convertView.setTag(holder);
       }else{
    	   holder=(viewHolder)convertView.getTag();
       }
       Movement mMovement=movementList.get(position);
       if(mMovement.getReadornot()==0){
    	   holder.bgRv.setBackgroundColor(mContext.getResources().getColor(R.color.white));
       }else{
    	   holder.bgRv.setBackgroundColor(mContext.getResources().getColor(R.color.background));
       }
       switch (mMovement.getType()) {
	case SYS:
		holder.movementIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sysmovementicon));
		break;
	case GOODS:
		holder.movementIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.goodsmoveicon));
		break;
	case NEEDS:
		holder.movementIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.needsmoveucib));
		break;
	}
       holder.contentTv.setText(mMovement.getContent());
       holder.timeTv.setText(mMovement.getDatetime());
		return convertView;
	}
	private final class viewHolder{
		public TextView contentTv;
		public TextView timeTv;
		public ImageView movementIcon;
		public RelativeLayout bgRv;
	}

}
