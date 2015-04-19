package com.seutao.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.seutao.adapter.LatestNeedsListAdapter;
import com.seutao.entity.LatestWant;
import com.seutao.sharedata.ShareData;

public class HomePagePartTwo extends Fragment implements Callback {
	protected static final int LIST_INIT = 1;
	protected static final int LIST_LOAD = 0;
	private PullToRefreshListView latestNeedsLv;
	private List<LatestWant> latestWants;
	private LatestNeedsListAdapter mAdapter;
	private DisplayImageOptions options;
	private String url;
	private Gson gson;
	private RequestQueue mQue;
	private Handler mHandler;
    private static int COME_FROM_NEED=2;
	// DisplayImageOptions options;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_stub) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_stub) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();
		latestWants = new ArrayList<LatestWant>();
		gson = new Gson();
		mHandler = new Handler(this);
		mQue = Volley.newRequestQueue(getActivity());
		View newView = inflater.inflate(R.layout.activity_home_page_part_two,
				container, false);
		latestNeedsLv = (PullToRefreshListView) newView
				.findViewById(R.id.home_page_two_list);
		mAdapter = new LatestNeedsListAdapter(getActivity(), latestWants,
				options);
		latestNeedsLv.setAdapter(mAdapter);
		latestNeedsLv.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				getLatestNeeds(ShareData.List_init, 0);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				getLatestNeeds(ShareData.List_load,
						latestWants.get(latestWants.size() - 1).getWid());
			}

		});
		getLatestNeeds(ShareData.List_init, 0);
		latestNeedsLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View item,
					int positon, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), NeedsDetailInfoPage.class);
				Bundle bundle = new Bundle();
				bundle.putInt(
						"wid",latestWants.get(positon-1).getWid());
				bundle.putInt("pageKind", COME_FROM_NEED);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		return newView;
	}

	public void getLatestNeeds(int flag, int current) {
		final int listflag = flag;
		url = ShareData.SEEVER_BASE_URL + "GetLatestWants.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("flag", flag + "");
		appendHeader.put("current", current + "");
		JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						System.out.println("list flag:" + listflag);
						System.out.println(response.toString());
						try {
							String flag = response.getString("flag");
							if (flag.equals("ok")) {
								List<LatestWant> newLatestWants = gson.fromJson(
										response.getString("latestWants"),
										new TypeToken<List<LatestWant>>() {
										}.getType());
								switch (listflag) {
								case ShareData.List_load:
									System.out.println("list init come in");
									latestWants.addAll(newLatestWants);
									Message msg = new Message();
									msg.what = LIST_INIT;
									mHandler.sendMessage(msg);
									break;
								case ShareData.List_init:
									latestWants = newLatestWants;
									Message nmsg = new Message();
									nmsg.what = LIST_LOAD;
									mHandler.sendMessage(nmsg);
									break;
								}
							} else {
								Toast.makeText(getActivity(), flag,
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						System.out.println("myJSReq--json receive failed!");
						Toast.makeText(getActivity(), "连接服务器失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQue.add(mJsonObjectRequest);

	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == LIST_INIT) {
			System.out.println(" MSG come in! latest want :  " + latestWants);
			mAdapter.setLatestWants(latestWants);
			mAdapter.notifyDataSetChanged();
			latestNeedsLv.onRefreshComplete();
		} else if (msg.what == LIST_LOAD) {
			System.out.println("latest want : " + latestWants);
			mAdapter.setLatestWants(latestWants);
			mAdapter.notifyDataSetChanged();
			latestNeedsLv.onRefreshComplete();
		}
		return false;
	}
}
