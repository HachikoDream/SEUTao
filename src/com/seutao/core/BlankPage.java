package com.seutao.core;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BlankPage extends Activity {
 
	private Button topBtn;
	private ImageView goBackIv;
	private TextView topTv;
	private static final String title="收藏列表";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blank_page);
		topBtn=(Button)findViewById(R.id.listlayout_top_btn);
		goBackIv=(ImageView)findViewById(R.id.listlayout_top_back);
		topTv=(TextView)findViewById(R.id.listlayout_top_text);
		topBtn.setVisibility(View.GONE);
		topTv.setText(title);
		goBackIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		((TextView)findViewById(R.id.blank_page_tv_info)).setText("TA的收藏夹没有对外开放哦~");
	}


}
