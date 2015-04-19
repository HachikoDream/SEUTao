package com.seutao.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PublishedSuccessPage extends Activity {

	private Button gotoHome;
	private Button gotoDetail;
	private int id;
	private int sourcePage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_published_success_page);
		Intent intent=this.getIntent();
		sourcePage=intent.getIntExtra("source", -1);
		if(sourcePage==1){
			id=intent.getIntExtra("gid", -1);	
		}else{
			id=intent.getIntExtra("wid", -1);
		}
		
		gotoDetail = (Button) findViewById(R.id.publish_suucess_goodsDetailButton);
		gotoHome = (Button) findViewById(R.id.pubish_success_gotoHome);
		if(sourcePage==1){
			gotoDetail.setText("查看商品");
		}else{
			gotoDetail.setText("查看求购");
		}
		gotoDetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				if(sourcePage==1){
					intent.setClass(PublishedSuccessPage.this,
							GoodsDetailInfoPage.class);
					Bundle bundle = new Bundle();
					bundle.putInt("pageKind", sourcePage);
					bundle.putInt("gid", id);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}else{
					intent.setClass(PublishedSuccessPage.this,
							NeedsDetailInfoPage.class);	
					Bundle bundle = new Bundle();
					bundle.putInt("pageKind", sourcePage);
					bundle.putInt("wid", id);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}
				
			}
		});
		gotoHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(PublishedSuccessPage.this,MainTabContainer.class);
				startActivity(intent);
				finish();
			}
		});
	}

}
