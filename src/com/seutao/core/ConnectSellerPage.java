package com.seutao.core;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ConnectSellerPage extends Activity implements OnClickListener {
	private ImageView msgIv;
	private ImageView phoneIv;
	private ImageView quitIv;
	private String phoneNum;
    private String goodsName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Transparent);
		setContentView(R.layout.activity_connect_seller_page);
		goodsName=this.getIntent().getStringExtra("goodsName");
		phoneNum = this.getIntent().getStringExtra("phoneNum");
		initView();
		setListener();
	}

	private void initView() {
		msgIv = (ImageView) findViewById(R.id.connect_dialog_msg);
		phoneIv = (ImageView) findViewById(R.id.connect_dialog_phone);
		quitIv = (ImageView) findViewById(R.id.connect_dialog_quit);
	}

	private void setListener() {
		msgIv.setOnClickListener(this);
		phoneIv.setOnClickListener(this);
		quitIv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.connect_dialog_msg:
			String sms_content = "您好，我在校园淘上看到了您发布的 "+goodsName;
			Uri uri = Uri.parse("smsto:"+phoneNum);
			Intent it = new Intent(Intent.ACTION_SENDTO, uri);
			it.putExtra("sms_body",sms_content);
			startActivity(it);        
			break;
		case R.id.connect_dialog_phone:
			Intent phoneIntent = new Intent("android.intent.action.CALL",
					Uri.parse("tel:" + phoneNum));
			startActivity(phoneIntent);
			break;
		case R.id.connect_dialog_quit:
			finish();
			break;
		}
	}
}
