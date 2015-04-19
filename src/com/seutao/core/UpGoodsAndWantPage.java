package com.seutao.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class UpGoodsAndWantPage extends Activity implements OnClickListener {
	private ImageView greenBallIv;
	private ImageView yellowBallIv;
	private ImageView upGoodsIv;
	private ImageView upWantIv;
	private ImageView closeIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Transparent);
		setContentView(R.layout.activity_up_goods_and_want_page);
		initView();
		setListener();
	}

	private void initView() {
		greenBallIv = (ImageView) findViewById(R.id.add_up_page_greenball);
		yellowBallIv = (ImageView) findViewById(R.id.add_up_page_yellowball);
		upGoodsIv = (ImageView) findViewById(R.id.add_up_page_upgoods_iv);
		upWantIv = (ImageView) findViewById(R.id.add_up_page_upwant_iv);
		closeIv = (ImageView) findViewById(R.id.add_up_page_close);
	}
  private void setListener(){
	  upGoodsIv.setOnClickListener(this);
	  upWantIv.setOnClickListener(this);
	  closeIv.setOnClickListener(this);
  }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add_up_page_upgoods_iv:
			GotoUpGoodsPage();
			break;
		case R.id.add_up_page_upwant_iv:
			GotoUpwantPage();
		case R.id.add_up_page_close:
			finish();
			break;
		}
	}

	private void GotoUpGoodsPage() {
		Intent intent = new Intent(UpGoodsAndWantPage.this,
				upGoodsPhotoPage.class);
		intent.putExtra("source", "publish");
		startActivity(intent);
	}

	private void GotoUpwantPage() {
		Intent intent = new Intent(UpGoodsAndWantPage.this,
				UpNeedsDetailPage.class);
		intent.putExtra("source", "publish");
		startActivity(intent);

	}
}
