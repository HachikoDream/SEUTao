package com.seutao.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.seutao.adapter.GoodsListAdapter;
import com.seutao.sharedata.ShareData;
import com.seutao.volley.MyJsonArrayRequest;

public class ZongheList extends Fragment implements Callback {
	private PullToRefreshListView goods_zonghe_list = null;
	private Context context = null;
	private List<Map<String, Object>> goods = new ArrayList<Map<String, Object>>();
	private GoodsListAdapter adapter = null;
	private int pageKind = 0;
	private int kind = 0;
	private int index = 0;
	private Handler mHandler=null;
	protected static final int LOAD_COMPLETE = 1;
	private String url = ShareData.SEEVER_BASE_URL + "getgoods.json";
   private static final int PRICE_LOW_TO_HIGH=0;
	public ZongheList(Context context, int pageKind, int kind) {
		this.context = context;
		this.pageKind = pageKind;
		this.kind = kind;
	}

	public void getGoodsByZH(final int flag,int check) {
		RequestQueue mQueue = Volley.newRequestQueue(context);
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("kind", kind);
		appendHeader.put("type", "zonghe");
		appendHeader.put("check", check);
		appendHeader.put("index", index);
		appendHeader.put("flag", flag);
		MyJsonArrayRequest myJAReq = new MyJsonArrayRequest(url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						switch (flag) {
						case ShareData.List_init:
							goods.clear();
							break;

						case ShareData.List_load:
							break;
						}
						for (int i = 0; i < response.length(); i++) {
							JSONObject t;
							try {
								t = (JSONObject) response.get(i);
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("gid", t.getInt("gid"));
								map.put("gname", t.getString("gname"));
								map.put("price",
										Float.parseFloat(t.getString("price")));
								map.put("image", t.getString("image"));
								map.put("view", t.getInt("view"));
								String date = ShareData.getTime(t
										.getLong("date"));
								map.put("date", date);
								if(!goods.contains(map)){
									goods.add(map);	
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						System.out.println(response.toString());
						Message msg=new Message();
						msg.what=LOAD_COMPLETE;
						mHandler.sendMessage(msg);

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getActivity(), "连接服务器失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQueue.add(myJAReq);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.goods_zonghe_list, container,
				false);
		mHandler=new Handler(this);
		goods_zonghe_list = (PullToRefreshListView) view
				.findViewById(R.id.goods_list_page_zonghe_list);
		goods_zonghe_list
				.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		adapter = new GoodsListAdapter(getActivity(), goods);
		goods_zonghe_list.setAdapter(adapter);

		goods_zonghe_list
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub]
						index=0;
						getGoodsByZH(ShareData.List_init,0);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						index++;
						getGoodsByZH(ShareData.List_load, PRICE_LOW_TO_HIGH);
					}
				});

		goods_zonghe_list
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						Toast.makeText(getActivity(), "没有更多商品！",
								Toast.LENGTH_LONG).show();
					}
				});

		goods_zonghe_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), GoodsDetailInfoPage.class);
				Bundle bundle = new Bundle();
				bundle.putInt("pageKind", pageKind);
				bundle.putInt("gid",
						(Integer) goods.get(position - 1).get("gid"));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		getGoodsByZH(ShareData.List_init,PRICE_LOW_TO_HIGH);
		return view;
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==LOAD_COMPLETE){
			adapter.setGoods(goods);
			adapter.notifyDataSetChanged();
			goods_zonghe_list.onRefreshComplete();
		}
		return false;
	}

}
