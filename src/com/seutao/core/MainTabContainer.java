package com.seutao.core;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jauker.widget.BadgeView;
import com.seutao.sharedata.ShareData;

@SuppressWarnings("deprecation")
public class MainTabContainer extends TabActivity implements Callback {

	protected static final int UNREAD_MSG_COME = 0;
	private String url = null;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

	private TabHost tabHost;
	/**
	 * 底部控件
	 */
	private LinearLayout indexLayout;
	private LinearLayout goodsLayout;
	private LinearLayout needsLayout;
	private LinearLayout personLayout;
	private ImageView indexIv;
	private ImageView goodsIv;
	private ImageView needsIv;
	private ImageView personIv;
	private ImageView centerIv;
	private TextView indexTv;
	private TextView goodsTv;
	private TextView needsTv;
	private TextView personTv;
	private final int INDEX_PRESSED = 2;
	private final int GOODS_PRESSED = 3;
	private final int NEEDS_PRESSED = 4;
	private final int PERSON_PRESSED = 5;
	public static boolean isMessageCome;
	private BadgeView msgBadgeView;
	protected int unreadCount;
	protected Handler mHandler;
	private RequestQueue mQue;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println(" MainTabContainer ------on Resume !");
		if (isMessageCome) {
              isMessageCome=false;
              getunReadCount();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabhost_main);
		ExitApplication.getInstance().addActivity(this);
		mQue = Volley.newRequestQueue(this);
		mHandler = new Handler(this);
		dotab();
		initBottomView();
		setIconOnBottom(INDEX_PRESSED);
		registerMessageReceiver();
	}

	private void dotab() {
		tabHost = this.getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		intent = new Intent().setClass(this, HomePage.class);
		spec = tabHost.newTabSpec("主页").setIndicator("主页").setContent(intent);
		tabHost.addTab(spec);
		intent = new Intent().setClass(this, KindsPage.class);
		intent.putExtra("pageKind", 1);
		spec = tabHost.newTabSpec("商品").setIndicator("商品").setContent(intent);
		tabHost.addTab(spec);
		intent = new Intent().setClass(this, KindsPage.class);
		intent.putExtra("pageKind", 2);
		spec = tabHost.newTabSpec("求购").setIndicator("求购").setContent(intent);
		tabHost.addTab(spec);
		intent = new Intent().setClass(this, PersonInfoPage.class);
		intent.putExtra("uid", ShareData.MyId);
		spec = tabHost.newTabSpec("个人").setIndicator("个人").setContent(intent);
		tabHost.addTab(spec);
	}

	private void setIconOnBottom(int event) {
		switch (event) {
		case INDEX_PRESSED:
			backtoCommonOnBottom();
			indexIv.setImageDrawable(getResources().getDrawable(
					R.drawable.nindex1));
			indexTv.setTextColor(getResources().getColor(R.color.main_color));
			break;
		case GOODS_PRESSED:
			backtoCommonOnBottom();
			goodsIv.setImageDrawable(getResources().getDrawable(
					R.drawable.ngoods1));
			goodsTv.setTextColor(getResources().getColor(R.color.main_color));
			break;
		case NEEDS_PRESSED:
			backtoCommonOnBottom();
			needsIv.setImageDrawable(getResources().getDrawable(
					R.drawable.nneeds1));
			needsTv.setTextColor(getResources().getColor(R.color.main_color));
			break;
		case PERSON_PRESSED:
			backtoCommonOnBottom();
			personIv.setImageDrawable(getResources().getDrawable(
					R.drawable.nperson1));
			personTv.setTextColor(getResources().getColor(R.color.main_color));
			break;
		}

	}

	private void backtoCommonOnBottom() {
		indexIv.setImageDrawable(getResources().getDrawable(R.drawable.nindex0));
		goodsIv.setImageDrawable(getResources().getDrawable(R.drawable.ngoods0));
		needsIv.setImageDrawable(getResources().getDrawable(R.drawable.nneeds0));
		personIv.setImageDrawable(getResources()
				.getDrawable(R.drawable.nperson0));
		indexTv.setTextColor(getResources().getColor(R.color.black));
		goodsTv.setTextColor(getResources().getColor(R.color.black));
		needsTv.setTextColor(getResources().getColor(R.color.black));
		personTv.setTextColor(getResources().getColor(R.color.black));
	}

	private void initBottomView() {
		indexLayout = (LinearLayout) findViewById(R.id.main_page_index_layout);
		indexIv = (ImageView) findViewById(R.id.main_page_index_img);
		indexTv = (TextView) findViewById(R.id.main_page_index_txt);
		centerIv = (ImageView) findViewById(R.id.main_page_centerImg);
		goodsLayout = (LinearLayout) findViewById(R.id.main_page_goods_layout);
		goodsIv = (ImageView) findViewById(R.id.main_page_goods_img);
		goodsTv = (TextView) findViewById(R.id.main_page_goods_txt);
		needsLayout = (LinearLayout) findViewById(R.id.main_page_needs_layout);
		needsIv = (ImageView) findViewById(R.id.main_page_needs_img);
		needsTv = (TextView) findViewById(R.id.main_page_needs_txt);
		personLayout = (LinearLayout) findViewById(R.id.main_page_person_layout);
		personIv = (ImageView) findViewById(R.id.main_page_person_img);
		personTv = (TextView) findViewById(R.id.main_page_person_txt);
		msgBadgeView = new BadgeView(this);
		msgBadgeView.setVisibility(View.GONE);
		msgBadgeView.setTargetView(personIv);
		indexLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setIconOnBottom(INDEX_PRESSED);
				tabHost.setCurrentTabByTag("主页");
			}
		});
		goodsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setIconOnBottom(GOODS_PRESSED);
				System.out.println("goods!");
				tabHost.setCurrentTabByTag("商品");
			}
		});
		needsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setIconOnBottom(NEEDS_PRESSED);
				tabHost.setCurrentTabByTag("求购");
			}
		});
		personLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setIconOnBottom(PERSON_PRESSED);
				tabHost.setCurrentTabByTag("个人");
			}
		});
		centerIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainTabContainer.this,
						UpGoodsAndWantPage.class);
				startActivity(intent);
			}
		});
	}

	private void getunReadCount() {
		url = ShareData.SEEVER_BASE_URL + "GetUnReadCount.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("uid", ShareData.MyId);
		JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						System.out.println(response.toString());
						try {
								unreadCount = response.getInt("num");
								Message msg = new Message();
								msg.what = UNREAD_MSG_COME;
								mHandler.sendMessage(msg);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Toast.makeText(getBaseContext(), "连接服务器失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQue.add(mJsonObjectRequest);
	}

	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.seutao.core.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				getunReadCount();
			}
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == UNREAD_MSG_COME) {
			if (unreadCount == 0) {
				msgBadgeView.setVisibility(View.INVISIBLE);
			} else {
				msgBadgeView.setBadgeCount(unreadCount);
				msgBadgeView.setVisibility(View.VISIBLE);
			}
		}
		return false;
	}
}
