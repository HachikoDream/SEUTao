package com.seutao.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.seutao.adapter.GridViewAdapter;
import com.seutao.sharedata.ShareData;
import com.seutao.volley.MyJsonArrayRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class KindsPage extends Activity {
//	private LinearLayout kind1 = null;
//	private LinearLayout kind2 = null;
//	private LinearLayout kind3 = null;
//	private LinearLayout kind4 = null;
	private ImageView kind5 = null;
	private ImageView kind6 = null;
	private ImageView kind7 = null;
	private ImageView kind8 = null;
	private ImageView kind9 = null;
	private ImageView kind10 = null;
	private ImageView kind11 = null;
	private ImageView kind12 = null;
	private ImageView kind13 = null;
	private ImageView kind14 = null;
	private ImageView kind15 = null;
	private ImageView kind16 = null;
	private ImageView kind17 = null;
	private ImageView kind18 = null;
	private ImageView kind19 = null;
	private ImageView kind20 = null;
	private ImageView kind21 = null;
	private LinearLayout kind22 = null;
	private LinearLayout kind23 = null;
	private LinearLayout kind24 = null;
	private LinearLayout kind25 = null;
	private LinearLayout kind26 = null;
	private LinearLayout kind27 = null;
	
	private List<Map<String, Object>> kinds = new ArrayList<Map<String,Object>>();
	private String url = ShareData.SEEVER_BASE_URL + "gethotkinds.json";
	private HorizontalScrollView horizontalScrollView;
	private GridViewAdapter adapter = null;
	private GridView gridView = null;
	private Handler handler = null;
	private DisplayMetrics dm = null;
	private int NUM = 4; // 每行显示个数
	private int LIEWIDTH;//每列宽度
	
	private TextView _yuanzhuanrang = null;
	private TextView _qita = null;
    private ImageView goBackIv=null;
	private int pageKind = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_kinds_page);
		pageKind = getIntent().getIntExtra("pageKind", 0);
		initData();
		setListener();
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 1){
					setValue();
				}
			}
		};
	}

	private void initData() {
		goBackIv=(ImageView)findViewById(R.id.alllayout_top_back);
		goBackIv.setVisibility(View.GONE);
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.kinds_page_hot_kinds_scrollView);
		gridView = (GridView) findViewById(R.id.kinds_page_hot_kinds);
		horizontalScrollView.setHorizontalScrollBarEnabled(false);
		getScreenDen();
		LIEWIDTH = dm.widthPixels / NUM;
		getHotKinds();
//		kind1 = (LinearLayout) findViewById(R.id.kind1);
//		kind2 = (LinearLayout) findViewById(R.id.kind2);
//		kind3 = (LinearLayout) findViewById(R.id.kind3);
//		kind4 = (LinearLayout) findViewById(R.id.kind4);
		kind5 = (ImageView) findViewById(R.id.kinds_page_shumadianzi_kinds1);
		kind6 = (ImageView) findViewById(R.id.kinds_page_shumadianzi_kinds2);
		kind7 = (ImageView) findViewById(R.id.kinds_page_shumadianzi_kinds3);
		kind8 = (ImageView) findViewById(R.id.kinds_page_shumadianzi_kinds4);
		kind9 = (ImageView) findViewById(R.id.kinds_page_shumadianzi_kinds5);
		kind10 = (ImageView) findViewById(R.id.kinds_page_shumadianzi_kinds6);
		kind11 = (ImageView) findViewById(R.id.kinds_page_yiwuxiemao_kinds1);
		kind12 = (ImageView) findViewById(R.id.kinds_page_yiwuxiemao_kinds2);
		kind13 = (ImageView) findViewById(R.id.kinds_page_yiwuxiemao_kinds3);
		kind14 = (ImageView) findViewById(R.id.kinds_page_yiwuxiemao_kinds4);
		kind15 = (ImageView) findViewById(R.id.kinds_page_yiwuxiemao_kinds5);
		kind16 = (ImageView) findViewById(R.id.kinds_page_yiwuxiemao_kinds6);
		kind17 = (ImageView) findViewById(R.id.kinds_page_shenghuorichang_kinds1);
		kind18 = (ImageView) findViewById(R.id.kinds_page_shenghuorichang_kinds2);
		kind19 = (ImageView) findViewById(R.id.kinds_page_shenghuorichang_kinds3);
		kind20 = (ImageView) findViewById(R.id.kinds_page_shenghuorichang_kinds4);
		kind21 = (ImageView) findViewById(R.id.kinds_page_shenghuorichang_kinds5);
		kind22 = (LinearLayout) findViewById(R.id.kind22);
		kind23 = (LinearLayout) findViewById(R.id.kind23);
		kind24 = (LinearLayout) findViewById(R.id.kind24);
		kind25 = (LinearLayout) findViewById(R.id.kind25);
		kind26 = (LinearLayout) findViewById(R.id.kind26);
		kind27 = (LinearLayout) findViewById(R.id.kind27);
		_yuanzhuanrang = (TextView) findViewById(R.id.kinds_page_0yuanzhuanrang_kinds_lable);
		_qita = (TextView) findViewById(R.id.kinds_page_qita_kinds_lable);
	}

	private void getScreenDen() {
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
	}
	
	private void setValue() {
		adapter = new GridViewAdapter(this, kinds);
		gridView.setAdapter(adapter);
		LayoutParams params = new LayoutParams(adapter.getCount() * LIEWIDTH,
				LayoutParams.WRAP_CONTENT);
		gridView.setLayoutParams(params);
		gridView.setColumnWidth(dm.widthPixels / NUM);
		gridView.setStretchMode(GridView.NO_STRETCH);
		int count = adapter.getCount();
		gridView.setNumColumns(count);
	}
	
	private void getHotKinds() {
		RequestQueue mQueue = Volley.newRequestQueue(KindsPage.this);
		Map<String, String> appendHeader = new HashMap<String, String>();

		MyJsonArrayRequest myJAReq = new MyJsonArrayRequest(url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						kinds.clear();
						for (int i = 0; i < response.length(); i++) {
							JSONObject t;
							try {
								t = (JSONObject) response.get(i);
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("kid", t.getInt("kid"));
								map.put("image", t.getString("image"));
								map.put("kname", t.getString("kname"));
								kinds.add(map);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						System.out.println(response.toString());
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessage(msg);
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

	private void setListener() {
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Toast.makeText(getApplicationContext(), kinds.get(position).get("kid").toString(), 1000).show();
				Bundle bundle = new Bundle();
				bundle.putInt("kind", Integer.parseInt(kinds.get(position).get("kid").toString()));
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		kind5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 1);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 2);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 9);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 10);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind9.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 11);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind10.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 12);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind11.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 3);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind12.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 4);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind13.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 5);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind14.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 6);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind15.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 7);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind16.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 8);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind17.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 13);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind18.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 14);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind19.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 15);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind20.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 16);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind21.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 17);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind22.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 18);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind23.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 19);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind24.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 20);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind25.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 21);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind26.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 22);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		kind27.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 23);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		_yuanzhuanrang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 24);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		_qita.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (pageKind == 1) {
					intent.setClass(getApplicationContext(),
							GoodsListPage.class);
				} else {
					intent.setClass(getApplicationContext(),
							NeedsListPage.class);
				}
				Bundle bundle = new Bundle();
				bundle.putInt("kind", 25);
				bundle.putInt("pageKind", pageKind);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}


}
