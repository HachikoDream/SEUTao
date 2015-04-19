package com.seutao.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.seutao.sharedata.ShareData;
import com.seutao.volley.MyJsonArrayRequest;

public class NeedsDetailInfoPage extends Activity implements Callback {
	private ImageView needs_detail_info_page_image = null;
	private Button needs_detail_info_page_detail_like = null;
	private Button needs_detail_info_page_detail_comment = null;
	private Button needs_detail_info_page_detail_share = null;
	private Button needs_detail_info_page_detail_connect = null;
	private Handler handler = null;
	private List<Map<String, Object>> needDetails = new ArrayList<Map<String, Object>>();
	private String url = ShareData.SEEVER_BASE_URL + "getwantdetails.json";
	private String collect_url = ShareData.SEEVER_BASE_URL + "collectwant.json";
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	// 数据源
	private int check = 0;
	private int pageKind = 0;
	private int wid = -1;
	private int uid = -1;
	private TextView title = null;
	private TextView uname = null;
	private TextView wcontent = null;
	private ImageView goBackIv = null;
	private TextView topTv = null;
	private Button topBtn = null;
	private static final String toptitle = "求购详情";
    private String phoneNum=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_needs_detail_info_page);
		goBackIv = (ImageView) findViewById(R.id.alllayout_top_back);
		goBackIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		topTv = (TextView) findViewById(R.id.allayout_top_text);
		topTv.setText(toptitle);
		topBtn = (Button) findViewById(R.id.alllayout_top_btn);
		topBtn.setVisibility(View.GONE);
		needs_detail_info_page_image = (ImageView) findViewById(R.id.needs_detail_info_page_image);
		needs_detail_info_page_detail_like = (Button) findViewById(R.id.needs_detail_info_page_detail_like);
		needs_detail_info_page_detail_comment = (Button) findViewById(R.id.needs_detail_info_page_detail_comment);
		needs_detail_info_page_detail_share = (Button) findViewById(R.id.needs_detail_info_page_detail_share);
		needs_detail_info_page_detail_connect = (Button) findViewById(R.id.needs_detail_info_page_detail_connect);
		title = (TextView) findViewById(R.id.needs_detail_info_page_title);
		uname = (TextView) findViewById(R.id.needs_detail_info_page_name);
		wcontent = (TextView) findViewById(R.id.needs_detail_info_page_detail);
		pageKind = getIntent().getIntExtra("pageKind", 0);
		wid = getIntent().getIntExtra("wid", 0);
		uid = ShareData.MyId;
		getWantDetails(wid);
		handler = new Handler(this);
		needs_detail_info_page_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(NeedsDetailInfoPage.this, "home",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(NeedsDetailInfoPage.this,
						PersonInfoPage.class);
				intent.putExtra(
						"uid",
						Integer.parseInt(needDetails.get(0).get("uid")
								.toString()));
				startActivity(intent);
			}
		});
		needs_detail_info_page_detail_like
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (check == 1) {
							check = 0;
							collectWantVolley(wid, uid, check);
							needs_detail_info_page_detail_like.setText("收藏");
							Toast.makeText(getApplicationContext(), "取消收藏",
									Toast.LENGTH_LONG).show();
						} else {
							check = 1;
							collectWantVolley(wid, uid, check);
							needs_detail_info_page_detail_like.setText("取消收藏");
							Toast.makeText(getApplicationContext(), "收藏",
									Toast.LENGTH_LONG).show();
						}
					}
				});

		needs_detail_info_page_detail_comment
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setClass(NeedsDetailInfoPage.this,
								NewsPage.class);
						Bundle bundle = new Bundle();
						bundle.putInt("pageKind", pageKind);
						bundle.putInt("uid", uid);
						bundle.putInt("wid", wid);
						bundle.putInt(
								"w_uid",
								Integer.parseInt(needDetails.get(0).get("uid")
										.toString()));
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});

		needs_detail_info_page_detail_share
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Toast.makeText(getApplicationContext(), "分享",
								Toast.LENGTH_LONG).show();
					}
				});

		needs_detail_info_page_detail_connect
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent=new Intent(NeedsDetailInfoPage.this,ConnectSellerPage.class);
						intent.putExtra("phoneNum", phoneNum);
						intent.putExtra("goodsName", needDetails.get(0).get("title").toString());
						startActivity(intent);
					}
				});
	}

	private void collectWantVolley(int wid, int uid, int check) {
		RequestQueue mQueue = Volley.newRequestQueue(NeedsDetailInfoPage.this);
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("wid", wid + "");
		appendHeader.put("uid", uid + "");
		appendHeader.put("check", check + "");
		MyJsonArrayRequest myJAReq = new MyJsonArrayRequest(collect_url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						System.out.println(response.toString());
						try {
							if ((((JSONObject) response.get(0)).get("ok"))
									.toString().equals("collect success")) {
								Message msg = new Message();
								msg.what = 3;
								handler.sendMessage(msg);
							} else {
								Message msg = new Message();
								msg.what = 4;
								handler.sendMessage(msg);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), "连接服务器失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQueue.add(myJAReq);
	}

	private List<Map<String, Object>> getWantDetails(int wid) {
		RequestQueue mQueue = Volley.newRequestQueue(NeedsDetailInfoPage.this);
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("wid", wid + "");
		appendHeader.put("uid", uid + "");
		MyJsonArrayRequest myJAReq = new MyJsonArrayRequest(url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						needDetails.clear();
						for (int i = 0; i < response.length(); i++) {
							JSONObject t;
							try {
								t = (JSONObject) response.get(i);
								Map<String, Object> map = new HashMap<String, Object>();
								phoneNum=t.getString("phonenum");
								map.put("wid", t.getInt("wid"));
								map.put("s_kid", t.getInt("s_kid"));
								map.put("f_kid", t.getInt("f_kid"));
								map.put("uid", t.getInt("uid"));
								map.put("title", t.getString("title"));
								map.put("price",
										Float.parseFloat(t.getString("price")));
								map.put("uimage", t.getString("uimage"));
								map.put("uname", t.getString("uname"));
								map.put("details", t.getString("details"));
								map.put("collect", t.getInt("collect"));
								needDetails.add(map);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						Message msg = new Message();
						msg.what = 2;
						handler.sendMessage(msg);
						System.out.println(response.toString());// 杈撳嚭杩斿洖缁撴灉
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						System.out.println("myJSReq--json receive failed!");
						Toast.makeText(getApplicationContext(), "连接服务器失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQueue.add(myJAReq);
		return needDetails;
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == 2) {
			title.setText(needDetails.get(0).get("title").toString());
			ShareData.getImageLoader().displayImage(
					needDetails.get(0).get("uimage").toString(),
					needs_detail_info_page_image, ShareData.getRoundOptions(),
					animateFirstListener);
			uname.setText(needDetails.get(0).get("uname").toString());
			wcontent.setText(needDetails.get(0).get("details").toString());
			check = Integer.parseInt(needDetails.get(0).get("collect")
					.toString());
			if (check == 1) {
				needs_detail_info_page_detail_like.setText("取消收藏");
			} else {
				needs_detail_info_page_detail_like.setText("收藏");
			}
		} else if (msg.what == 3) {
			Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_LONG)
					.show();
		} else if (msg.what == 4) {
			Toast.makeText(getApplicationContext(), "操作失败，检查网络",
					Toast.LENGTH_LONG).show();
		}
		return false;
	}

}
