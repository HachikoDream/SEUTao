package com.seutao.core;

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

public class PriceList extends Fragment implements Callback {
	protected static final int LOAD_COMPLETE = 1;
	private PullToRefreshListView goods_price_list = null;
	private List<Map<String, Object>> goods;
	private GoodsListAdapter mAdapter = null;
	private Context context = null;
	private int pageKind = 0;
	private int kind = 0;
	private int index = 0;
	private int check = 0;
	private String url = ShareData.SEEVER_BASE_URL + "getgoods.json";
	private Handler handler = null;

	public PriceList(List<Map<String, Object>> goods, Context context,
			int pageKind, int kind) {
		this.goods = goods;
		this.context = context;
		this.pageKind = pageKind;
		this.kind = kind;
	}

	public void getGoodsByPrice(final int flag,int check) {
		RequestQueue mQueue = Volley.newRequestQueue(context);
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("kind", kind );
		appendHeader.put("type", "price");
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
						handler.sendMessage(msg);

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
		View view = inflater.inflate(R.layout.goods_price_list, container,
				false);
		handler=new Handler(this);
		goods_price_list = (PullToRefreshListView) view
				.findViewById(R.id.goods_list_page_price_list);
		goods_price_list
				.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		mAdapter = new GoodsListAdapter(getActivity(), goods);
		goods_price_list.setAdapter(mAdapter);
		goods_price_list
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						index=0;
						getGoodsByPrice(ShareData.List_init,check);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						index++;
						getGoodsByPrice(ShareData.List_load,check);
					}
				});

		goods_price_list
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						Toast.makeText(getActivity(), "已到最尾部", Toast.LENGTH_LONG).show();
					}
				});

		goods_price_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("positon:      "+position);
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
		getGoodsByPrice(ShareData.List_init,check);
		return view;
	}


	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==LOAD_COMPLETE){
			mAdapter.setGoods(goods);
			mAdapter.notifyDataSetChanged();
			goods_price_list.onRefreshComplete();
		}
		return false;
	}

}
