package com.seutao.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.seutao.adapter.NeedsListAdapter;
import com.seutao.sharedata.ShareData;
import com.seutao.volley.MyJsonArrayRequest;

public class NeedsListPage extends Activity {
	private PullToRefreshListView needs_list_page_goods_list = null;
	private NeedsListAdapter adapter = null;
	private TextView topTv;
	private ImageView goBackIv;
	private List<Map<String, Object>> needs = new ArrayList<Map<String, Object>>();
	private String url = ShareData.SEEVER_BASE_URL + "getwants.json";
	private int pageKind = 0;
	private int kind = 0;
	private static final String title = "求购列表";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_needs_list_page);
		pageKind = getIntent().getIntExtra("pageKind", 0);
		kind = getIntent().getIntExtra("kind", 0);
		setData();
		topTv = (TextView) findViewById(R.id.allayout_top_text);
		goBackIv = (ImageView) findViewById(R.id.alllayout_top_back);
		topTv.setText(title);
		goBackIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		adapter = new NeedsListAdapter(getApplicationContext(), needs);
		needs_list_page_goods_list = (PullToRefreshListView) findViewById(R.id.needs_list_page_goods_list);
		needs_list_page_goods_list
				.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);

		needs_list_page_goods_list.setAdapter(adapter);

		needs_list_page_goods_list
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						setData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						if(needs.size()==0){
							getNeedsList(ShareData.List_init,kind,0);
						}else{
							getNeedsList(ShareData.List_load, kind,(Integer)needs.get(needs.size()-1).get("wid"));
						}
					}
				});

		needs_list_page_goods_list
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						Toast.makeText(NeedsListPage.this, "沒有更多求购信息！",
								Toast.LENGTH_LONG).show();
					}
				});

		needs_list_page_goods_list
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent();
						intent.setClass(NeedsListPage.this,
								NeedsDetailInfoPage.class);
						Bundle bundle = new Bundle();
						bundle.putInt(
								"wid",
								Integer.parseInt(needs.get(position - 1)
										.get("wid").toString()));
						bundle.putInt("pageKind", pageKind);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});

	}

	private void setData() {
		getNeedsList(ShareData.List_init,kind,0);
	}
   private void finishLoadingDate(){
	   adapter.setNeeds(needs);
	   adapter.notifyDataSetChanged();
	   needs_list_page_goods_list.onRefreshComplete();
   }
	private void getNeedsList(final int flag,int kind,int index) {
		RequestQueue mQueue = Volley.newRequestQueue(NeedsListPage.this);
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("kind", kind + "");
		appendHeader.put("index", index + "");
        appendHeader.put("flag", flag+"");
		MyJsonArrayRequest myJAReq = new MyJsonArrayRequest(url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						switch (flag) {
						case ShareData.List_init:
							needs.clear();
							for (int i = 0; i < response.length(); i++) {
								JSONObject t;
								try {
									t = (JSONObject) response.get(i);
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("wid", t.getInt("wid"));
									map.put("title", t.getString("title"));
									map.put("price",
											Float.parseFloat(t.getString("price")));
									map.put("view", t.getInt("view"));
									map.put("uimage", t.getString("uimage"));
									map.put("uname", t.getString("uname"));
									String date = ShareData.getTime(t
											.getLong("date"));
									map.put("date", date);
									needs.add(map);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							break;

						case ShareData.List_load:
							for (int i = 0; i < response.length(); i++) {
								JSONObject t;
								try {
									t = (JSONObject) response.get(i);
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("wid", t.getInt("wid"));
									map.put("title", t.getString("title"));
									map.put("price",
											Float.parseFloat(t.getString("price")));
									map.put("view", t.getInt("view"));
									map.put("uimage", t.getString("uimage"));
									map.put("uname", t.getString("uname"));
									String date = ShareData.getTime(t
											.getLong("date"));
									map.put("date", date);
									needs.add(map);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							break;
						}
							System.out.println(response.toString());
						finishLoadingDate();
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
