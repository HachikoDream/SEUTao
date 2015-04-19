package com.seutao.core;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PrivilegePage extends Activity {

	private Context mContext;
	private ImageView goBackIv;
	private TextView topTitle;
	private Button topBtn;
	private Button delayTimeBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_privilege_page);
		mContext=this;
		goBackIv=(ImageView)findViewById(R.id.listlayout_top_back);
		topTitle=(TextView)findViewById(R.id.listlayout_top_text);
		topBtn=(Button)findViewById(R.id.listlayout_top_btn);
		topBtn.setVisibility(View.GONE);
		topTitle.setText("我的特权");
		goBackIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Activity)mContext).finish();
			}
		});

		delayTimeBtn=(Button)findViewById(R.id.activity_privilige_page_delaytime_btn);
		delayTimeBtn.setOnClickListener(new OnClickListener() {@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("android.intent.action.DelayTimePage");
				startActivity(intent);
				((Activity)mContext).finish();
			}
		});
	}

}
