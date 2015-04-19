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
import com.seutao.adapter.ConnectDialog;
import com.seutao.sharedata.ShareData;
import com.seutao.volley.MyJsonArrayRequest;

public class GoodsDetailInfoPage extends Activity implements Callback {
	private Button goods_detail_info_page_detail_like_two = null;
	private Button goods_detail_info_page_detail_comment = null;
	private Button goods_detail_info_page_detail_share = null;
	private Button goods_detail_info_page_detail_connect = null;
	private TextView connect_seller_phone = null;
	private TextView connect_seller_message = null;
	private TextView connect_seller_cancel = null;
	private String url = ShareData.SEEVER_BASE_URL + "getgooddetails.json";
	private String collect_url = ShareData.SEEVER_BASE_URL + "collectgood.json";
	private List<Map<String, Object>> goodDetails = null;
	private Handler handler = null;
	private int pageKind = -1;
	private int gid = -1;
	private int uid = -1;
	private ImageView gImage = null;
	private TextView gname = null;
	private TextView uname = null;
	private TextView price = null;
	private TextView school = null;
	private TextView newOld = null;
	private TextView date = null;
	private TextView kname = null;
	private ImageView uimage = null;
	private TextView content = null;
	private ImageView goBackIv;
	private TextView topTv;
	private Button topBtn;
	private static final String title = "商品详情";
	protected static final int COLLECT_SUCCESS = 3;
	protected static final int COLLECT_FAILED = 4;
	protected static final int DETAIL_COME = 2;
	private String phoneNum=null;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_goods_detail_info_page);
		pageKind = getIntent().getIntExtra("pageKind", 0);
		gid = getIntent().getIntExtra("gid", 0);
		uid=ShareData.MyId;
		goBackIv = (ImageView) findViewById(R.id.alllayout_top_back);
		topTv = (TextView) findViewById(R.id.allayout_top_text);
		goBackIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		topTv.setText(title);
		topBtn = (Button) findViewById(R.id.alllayout_top_btn);
		topBtn.setVisibility(View.GONE);
		gImage = (ImageView) findViewById(R.id.goods_detail_info_page_images);
		gname = (TextView) findViewById(R.id.goods_detail_info_page_name);
		uname = (TextView) findViewById(R.id.goods_detail_info_page_seller_name);
		price = (TextView) findViewById(R.id.goods_detail_info_page_price);
		school = (TextView) findViewById(R.id.goods_detail_info_page_place);
		newOld = (TextView) findViewById(R.id.goods_detail_info_page_newold);
		date = (TextView) findViewById(R.id.goods_detail_info_page_time);
		kname = (TextView) findViewById(R.id.goods_detail_info_page_kind);
		uimage = (ImageView) findViewById(R.id.goods_detail_info_page_seller);
		content = (TextView) findViewById(R.id.goods_detail_info_page_detail);
		goods_detail_info_page_detail_like_two = (Button) findViewById(R.id.goods_detail_info_page_detail_like_two);
		goods_detail_info_page_detail_comment = (Button) findViewById(R.id.goods_detail_info_page_detail_comment);
		goods_detail_info_page_detail_share = (Button) findViewById(R.id.goods_detail_info_page_detail_share);
		goods_detail_info_page_detail_connect = (Button) findViewById(R.id.goods_detail_info_page_detail_connect);
		handler = new Handler(this);
		InitGoodDetail();
		goods_detail_info_page_detail_like_two
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						if (goods_detail_info_page_detail_like_two.getText()
								.toString().equals("收藏")) {
							collectGoodVolley(gid, uid, 1);
							goods_detail_info_page_detail_like_two
									.setText("取消收藏");
						} else {
							collectGoodVolley(gid, uid, 0);
							goods_detail_info_page_detail_like_two
									.setText("收藏");
						}
					}
				});
		goods_detail_info_page_detail_comment
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Toast.makeText(GoodsDetailInfoPage.this, "评论",
								Toast.LENGTH_LONG).show();
						Intent intent = new Intent();
						intent.setClass(GoodsDetailInfoPage.this,
								NewsPage.class);
						Bundle bundle = new Bundle();
						bundle.putInt("pageKind", pageKind);
						bundle.putInt("uid", uid);
						bundle.putInt(
								"g_uid",
								Integer.parseInt(goodDetails.get(0).get("uid")
										.toString()));
						bundle.putInt("gid", gid);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});

		goods_detail_info_page_detail_share
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						Toast.makeText(getApplicationContext(), "分享",
								Toast.LENGTH_LONG).show();
					}
				});

		goods_detail_info_page_detail_connect
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent=new Intent(GoodsDetailInfoPage.this,ConnectSellerPage.class);
						intent.putExtra("phoneNum", phoneNum);
						intent.putExtra("goodsName", goodDetails.get(0).get("gname").toString());
						startActivity(intent);
					}
				});
		uimage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(GoodsDetailInfoPage.this,
						PersonInfoPage.class);
				intent.putExtra(
						"uid",
						Integer.parseInt(goodDetails.get(0).get("uid")
								.toString()));
				startActivity(intent);
			}
		});
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

	private void InitGoodDetail() {
		goodDetails = new ArrayList<Map<String, Object>>();
		goodDetails = getGoodDetails(gid, uid);
	}

	private List<Map<String, Object>> getGoodDetails(int gid, int uid) {
		RequestQueue mQueue = Volley.newRequestQueue(GoodsDetailInfoPage.this);
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("gid", gid + "");
		appendHeader.put("uid", uid + "");
		System.out.println(new JSONObject(appendHeader).toString());
		MyJsonArrayRequest myJAReq = new MyJsonArrayRequest(url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						goodDetails.clear();
						for (int i = 0; i < response.length(); i++) {
							JSONObject t;
							try {
								t = (JSONObject) response.get(i);
								phoneNum=t.getString("phonenum");
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("gid", t.getInt("gid"));
								map.put("kid", t.getInt("kid"));
								map.put("s_kid", t.getInt("s_kid"));
								map.put("uid", t.getInt("uid"));
								map.put("uname", t.getString("uname"));
								map.put("uimage", t.getString("uimage"));
								map.put("gname", t.getString("gname"));
								map.put("neworold", t.getString("neworold"));
								map.put("price",
										Float.parseFloat(t.getString("price")));
								map.put("image", t.getString("image"));
								map.put("view", t.getInt("view"));
								map.put("date", t.getString("date"));
								map.put("school", t.getString("school"));
								map.put("content", t.getString("content"));
								map.put("kname", t.getString("kname"));
								map.put("f_kname", t.getString("f_kname"));
								map.put("collect", t.getInt("collect"));
								goodDetails.add(map);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						Message msg = new Message();
						msg.what = DETAIL_COME;
						handler.sendMessage(msg);
						System.out.println(response.toString());
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
		return goodDetails;
	}

	private void collectGoodVolley(int gid, int uid, int check) {
		RequestQueue mQueue = Volley.newRequestQueue(GoodsDetailInfoPage.this);
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("gid", gid + "");
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
								msg.what = COLLECT_SUCCESS;
								handler.sendMessage(msg);
							} else {
								Message msg = new Message();
								msg.what = COLLECT_FAILED;
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
						System.out.println("myJSReq--json receive failed!");
						Toast.makeText(getApplicationContext(), "连接服务器失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQueue.add(myJAReq);
	}

	private void setView() {
		System.out.println("goodsdetail size:"+goodDetails.size());
		ShareData.getImageLoader().displayImage(
				goodDetails.get(0).get("image").toString(), gImage,
				ShareData.getOptions(), animateFirstListener);
		gname.setText(goodDetails.get(0).get("gname") + "");
		uname.setText(goodDetails.get(0).get("uname") + "");
		price.setText("￥" + goodDetails.get(0).get("price"));
		school.setText(goodDetails.get(0).get("school") + "");
		newOld.setText(goodDetails.get(0).get("neworold") + "");
		ShareData.getImageLoader().displayImage(
				goodDetails.get(0).get("uimage").toString(), uimage,
				ShareData.getRoundOptions(), animateFirstListener);
		date.setText(goodDetails.get(0).get("date").toString());
		kname.setText(goodDetails.get(0).get("kname").toString());
		content.setText(goodDetails.get(0).get("content").toString());
		if ((Integer) goodDetails.get(0).get("collect") == 1) {
			goods_detail_info_page_detail_like_two.setText("取消收藏");
		} else {
			goods_detail_info_page_detail_like_two.setText("收藏");
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == DETAIL_COME) {
			setView();

		} else if (msg.what == COLLECT_SUCCESS) {
			Toast.makeText(getApplicationContext(), "已收藏", Toast.LENGTH_LONG)
					.show();
		} else if (msg.what == COLLECT_FAILED) {
			Toast.makeText(getApplicationContext(), "收藏失败，请检查网络",
					Toast.LENGTH_LONG).show();
		}
		return false;
	}

}
