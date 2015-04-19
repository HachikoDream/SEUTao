package com.seutao.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HobbyLabel {
	private Context mContext=null;
	private String label=null;
	private TextView tv_label=null;
	private boolean isChecked=false;
	private LinearLayout ll=null;
	public HobbyLabel(Context context,String label,boolean isChecked) {
		// TODO Auto-generated constructor stub
		this.mContext=context;
		this.label=label;
		this.isChecked=isChecked;
		setRLHobbyLabel();
	}
	@SuppressLint("NewApi")
	public void setRLHobbyLabel()
	{
		LayoutInflater infalter = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ll = (LinearLayout)infalter.inflate(R.layout.hobby_label,null);
		tv_label=(TextView)ll.findViewById(R.id.hobby_label_tv_label);
		tv_label.setText(label);
		if (isChecked) {
			tv_label.setBackground(mContext.getResources().getDrawable(R.drawable.hobbylabel1));
			tv_label.setTextColor(mContext.getResources().getColor(R.color.white));
		}
		ll.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isChecked) {
					tv_label.setBackground(mContext.getResources().getDrawable(R.drawable.hobbylabel0));
					tv_label.setTextColor(mContext.getResources().getColor(R.color.person_info_color));
				}
				else {
					tv_label.setBackground(mContext.getResources().getDrawable(R.drawable.hobbylabel1));
					tv_label.setTextColor(mContext.getResources().getColor(R.color.white));
				}
				isChecked=!isChecked;
			}
		});
		
	}
	
	public LinearLayout getLL() {
		return ll;
	}
	public boolean getIsChecked() {
		return isChecked;
	}
	
	public String getLabel() {
		return label;
	}
}
