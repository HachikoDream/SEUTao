﻿package com.seutao.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.seutao.adapter.DelayNeedsAdapter;
import com.seutao.entity.PublishedNeed;
import com.seutao.sharedata.ShareData;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class DelayNeedsList extends Fragment implements Callback
{
	private PullToRefreshListView mPullToRefreshListView=null;
	private DelayNeedsAdapter mDelayNeedsAdapter = null;
	private static Context mContext=null;
	private Gson gson=null;
	private RequestQueue mQueue = null;
	private String url=null;
	private Handler mHandler;
	private final static int DATA_CHANGE=21; 
	//private List<PublishedNeed> ShareData.mDelayNeeds=new ArrayList<PublishedNeed>();
	public static void initContext(Context context){
		mContext=context;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		gson = new Gson();
		mQueue=Volley.newRequestQueue(mContext);
		mHandler=new Handler(this);
		View mDelayNeedsList = inflater.inflate(R.layout.listview, container, false);
		mDelayNeedsAdapter = new DelayNeedsAdapter(ShareData.mDelayNeeds,R.layout.collected_needs_list_item,mContext);
		getDelayNeeds(ShareData.List_init, 0);
		mPullToRefreshListView=(PullToRefreshListView)mDelayNeedsList.findViewById(R.id.listview_lv);		
		mPullToRefreshListView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				getDelayNeeds(ShareData.List_init, 0);
			}
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if(ShareData.mDelayNeeds.size()==0){
					getDelayNeeds(ShareData.List_init, 0);
				}else{
					getDelayNeeds(ShareData.List_load, ShareData.mDelayNeeds.get(ShareData.mDelayNeeds.size()-1).getId());
				}
			}
		});
		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				Toast.makeText(mContext, "end!", Toast.LENGTH_SHORT).show();
			}
		});

		ListView actualListView = mPullToRefreshListView.getRefreshableView();
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);
		mPullToRefreshListView.setAdapter(mDelayNeedsAdapter);
		
		return mDelayNeedsList;
	}
	public DelayNeedsAdapter getAdapter() {
		return mDelayNeedsAdapter;
	}
	
	
	public void getDelayNeeds(int flag,int current){
		final int listflag=flag;
		url=ShareData.SEEVER_BASE_URL+"getPublishedNeeds.json";
		Map<String, String> appendHeader = new HashMap<String, String>();  
		appendHeader.put("uid", String.valueOf(ShareData.MyId));
		appendHeader.put("flag",String.valueOf(flag));//第一次打开==>init/下拉刷新==>refresh/上拉加载==>load 具体实现仍需探讨
		appendHeader.put("currentid", String.valueOf(current));//当前客户端已有数据的下拉刷新==>最新id/上拉加载==>最老id
		JsonObjectRequest mJsonObjectRequest=new JsonObjectRequest(url, new JSONObject(appendHeader), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				System.out.println(response.toString());
				try {
					String flag=response.getString("flag");
					if (flag.equals("ok")) {
						List<PublishedNeed> mDelayNeedss = gson.fromJson(response.getString("publishedNeeds"),  
						        new TypeToken<List<PublishedNeed>>() {  
						        }.getType());
						switch (listflag) {
						case ShareData.List_init:
							ShareData.mDelayNeeds.clear();
							ShareData.mDelayNeeds.addAll(0,mDelayNeedss);
							break;
						case ShareData.List_load:
							ShareData.mDelayNeeds.addAll(ShareData.mDelayNeeds.size(),mDelayNeedss);
							break;
						default:
							break;
						}
						Message msg=new Message();
						msg.what=DATA_CHANGE;
						mHandler.sendMessage(msg);
					}
					else {
						Toast.makeText(mContext, flag, Toast.LENGTH_SHORT).show();
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
				Toast.makeText(mContext, "连接服务器失败！", Toast.LENGTH_SHORT).show();
			}
		});
		mQueue.add(mJsonObjectRequest); 
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==DATA_CHANGE){
			mDelayNeedsAdapter.setmDelayNeeds(ShareData.mDelayNeeds);
			mDelayNeedsAdapter.notifyDataSetChanged();
			mPullToRefreshListView.onRefreshComplete();
		}
		return false;
	}
}
