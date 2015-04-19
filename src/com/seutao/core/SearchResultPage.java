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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.seutao.adapter.GoodsListAdapter;
import com.seutao.adapter.NeedsListAdapter;
import com.seutao.adapter.UsersListAdapter;
import com.seutao.sharedata.ShareData;
import com.seutao.volley.MyJsonArrayRequest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchResultPage extends Activity {

	private String url;
	private PullToRefreshListView result_list = null;
	private List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	private BaseAdapter adapter = null;
	private static final String[] types = { "商品", "求购", "商家" };
	private String key = "";
	private int type = 0;
	private Spinner spinner;
	private ArrayAdapter<String> arrayAdapter;
	private ImageView search = null;
	private EditText keyWord = null;
	private ImageView cancel = null;
	private LinearLayout parent = null;
	private LinearLayout history = null;
	private String baseUrl = ShareData.SEEVER_BASE_URL;
	private TextView topTv;
	private ImageView goBackIv;
	private Button topBtn;
	private static final String title = "搜索";
	protected static final int SEARCH_GOODS = 1;
	protected static final int SEARCH_WANTS = 2;
	protected static final int SEARCH_USERS = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search_result_page);
		topTv = (TextView) findViewById(R.id.allayout_top_text);
		goBackIv = (ImageView) findViewById(R.id.alllayout_top_back);
		topBtn = (Button) findViewById(R.id.alllayout_top_btn);
		topTv = (TextView) findViewById(R.id.allayout_top_text);
		topTv.setText(title);
		topBtn.setVisibility(View.GONE);
		goBackIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		spinner = (Spinner) findViewById(R.id.search_result_page_type);
		search = (ImageView) findViewById(R.id.search_result_page_search);
		keyWord = (EditText) findViewById(R.id.search_result_page_key);
		cancel = (ImageView) findViewById(R.id.search_result_page_cancel);
		parent = (LinearLayout) findViewById(R.id.search_page_list_parent);
		history = (LinearLayout) findViewById(R.id.search_result_page_history);
		if (!keyWord.getText().toString().equals("")) {
			cancel.setVisibility(View.VISIBLE);
		} else {
			cancel.setVisibility(View.INVISIBLE);
		}
		result_list = (PullToRefreshListView) findViewById(R.id.search_result_page_list);
		result_list
				.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		adapter = new GoodsListAdapter(getApplicationContext(), result);
		result_list.setAdapter(adapter);
		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, types);
		arrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(arrayAdapter);
		spinner.setVisibility(View.VISIBLE);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == 0) {
					type = SEARCH_GOODS;
					adapter = new GoodsListAdapter(getApplicationContext(),
							result);
					url = baseUrl + "searchgoods.json";
				} else if (position == 1) {
					type = SEARCH_WANTS;
					adapter = new NeedsListAdapter(getApplicationContext(),
							result);
					url = baseUrl + "searchwants.json";
				} else {
					type = SEARCH_USERS;
					adapter = new UsersListAdapter(getApplicationContext(),
							result);
					url = baseUrl + "searchusers.json";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				adapter = new GoodsListAdapter(getApplicationContext(), result);
			}
		});

		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				key = keyWord.getText().toString();
				if (key.equals("")) {
					Toast.makeText(getApplicationContext(), "关键字不能为空", Toast.LENGTH_LONG)
							.show();
				} else {
					search(ShareData.List_init,key,0);
					history.setVisibility(View.GONE);
					parent.setVisibility(View.VISIBLE);
				}
			}
		});

		keyWord.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				parent.setVisibility(View.GONE);
				history.setVisibility(View.VISIBLE);
				return false;
			}
		});
		keyWord.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (!keyWord.getText().toString().equals("")) {
					cancel.setVisibility(View.VISIBLE);
				} else {
					cancel.setVisibility(View.INVISIBLE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				key = keyWord.getText().toString();
				if (!key.equals("")) {
					search(ShareData.List_init,key,0);
					history.setVisibility(View.GONE);
					parent.setVisibility(View.VISIBLE);
				} else {
					result.clear();
					adapter.notifyDataSetChanged();
				}
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				keyWord.setText("");
				keyWord.setHint("输入关键字");
				cancel.setVisibility(View.INVISIBLE);
			}
		});

		result_list.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				search(ShareData.List_init,key,0);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if(result.size()==0){
					search(ShareData.List_init,key,0);
				}else{
					int id=-1;
					switch (type) {
					case SEARCH_GOODS:
						id=(Integer)result.get(result.size()-1).get("gid");
						break;

					case SEARCH_USERS:
						id=(Integer)result.get(result.size()-1).get("uid");
						break;
					case SEARCH_WANTS:
						id=(Integer)result.get(result.size()-1).get("wid");
						break;
					}
					search(ShareData.List_load,key,id);
				}
			}
		});

		result_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent();

				Bundle bundle = new Bundle();
				if (type == SEARCH_GOODS) {
					intent.setClass(SearchResultPage.this,
							GoodsDetailInfoPage.class);
					bundle.putInt(
							"gid",
							Integer.parseInt(result.get(position - 1)
									.get("gid").toString()));
				} else if (type == SEARCH_WANTS) {
					intent.setClass(SearchResultPage.this,
							NeedsDetailInfoPage.class);
					bundle.putInt(
							"wid",
							Integer.parseInt(result.get(position - 1)
									.get("wid").toString()));
				} else {
					intent.setClass(SearchResultPage.this, PersonInfoPage.class);
					bundle.putInt(
							"uid",
							Integer.parseInt(result.get(position - 1)
									.get("uid").toString()));
				}
				bundle.putInt("pageKind", type);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		result_list
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						Toast.makeText(SearchResultPage.this, "没有更多数据", Toast.LENGTH_LONG)
								.show();
					}
				});

	}

	private void search(final int flag,String key,int index) {
		RequestQueue mQueue = Volley.newRequestQueue(SearchResultPage.this);
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("key", key);
		appendHeader.put("index", index + "");
        appendHeader.put("flag", flag+"");
		MyJsonArrayRequest myJAReq = new MyJsonArrayRequest(url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						if(flag==ShareData.List_init){
							result.clear();							
						}
						if (type == SEARCH_GOODS) {
							for (int i = 0; i < response.length(); i++) {
								JSONObject t;
								try {
									t = (JSONObject) response.get(i);
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("gid", t.getInt("gid"));
									map.put("gname", t.getString("gname"));
									map.put("price", Float.parseFloat(t
											.getString("price")));
									map.put("image", t.getString("image"));
									map.put("view", t.getInt("view"));
									String date = ShareData.getTime(t
											.getLong("date"));
									map.put("date", date);
									result.add(map);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						} else if (type == SEARCH_WANTS) {
							for (int i = 0; i < response.length(); i++) {
								JSONObject t;
								try {
									t = (JSONObject) response.get(i);
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("wid", t.getInt("wid"));
									map.put("title", t.getString("title"));
									map.put("price", Float.parseFloat(t
											.getString("price")));
									map.put("uimage", t.getString("uimage"));
									map.put("uname", t.getString("uname"));
									String date = ShareData.getTime(t
											.getLong("date"));
									map.put("date", date);
									map.put("view", t.getInt("view"));
									result.add(map);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						} else {
							for (int i = 0; i < response.length(); i++) {
								JSONObject t;
								try {
									t = (JSONObject) response.get(i);
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("uid", t.getInt("uid"));
									map.put("uname", t.getString("uname"));
									map.put("uimage", t.getString("uimage"));
									map.put("sign", t.getString("sign"));
									result.add(map);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}
						System.out.println(response.toString());
						result_list.setAdapter(adapter);
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

}
