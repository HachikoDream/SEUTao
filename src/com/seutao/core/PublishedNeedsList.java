 package com.seutao.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.seutao.adapter.PublishedNeedsAdapter;
import com.seutao.entity.PublishedNeed;
import com.seutao.sharedata.ShareData;

public class PublishedNeedsList extends Fragment
{
	private PullToRefreshListView mPullToRefreshListView=null;
	private PublishedNeedsAdapter mPublishedNeedsAdapter = null;
	private static Context mContext=null;
	private Gson gson=null;
	private RequestQueue mQueue = null;
	private String url=null;
	private int uid;
	private List<PublishedNeed>mPublishedNeeds=new ArrayList<PublishedNeed>();
	public static void initContext(Context context){
		mContext=context;
	}
	public PublishedNeedsList(int uid) {
		super();
		this.uid = uid;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		gson = new Gson();
		mQueue=Volley.newRequestQueue(mContext);
		View mPublishedNeedsList = inflater.inflate(R.layout.listview, container, false);
		mPublishedNeedsAdapter = new PublishedNeedsAdapter(mPublishedNeeds,R.layout.published_needs_list_item,mContext,uid);
		getPublishedNeeds(ShareData.List_init, 0);
		mPullToRefreshListView=(PullToRefreshListView)mPublishedNeedsList.findViewById(R.id.listview_lv);
		mPullToRefreshListView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				getPublishedNeeds(ShareData.List_init, 0);
			}
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if(mPublishedNeeds.size()==0){
					getPublishedNeeds(ShareData.List_init, 0);
				}else{
					getPublishedNeeds(ShareData.List_load, mPublishedNeeds.get(mPublishedNeeds.size()-1).getId());
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
		mPullToRefreshListView.setAdapter(mPublishedNeedsAdapter);
		return mPublishedNeedsList;
	}

	public void getPublishedNeeds(int flag,int current){
		final int listflag=flag;
		url=ShareData.SEEVER_BASE_URL+"getPublishedNeeds.json";
		Map<String, String> appendHeader = new HashMap<String, String>();  
		appendHeader.put("uid", String.valueOf(uid));
		appendHeader.put("flag",String.valueOf(flag));
		appendHeader.put("currentid", String.valueOf(current));
		JsonObjectRequest mJsonObjectRequest=new JsonObjectRequest(url, new JSONObject(appendHeader), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				System.out.println(response.toString());
				try {
					String flag=response.getString("flag");
					if (flag.equals("ok")) {
						List<PublishedNeed> mPublishedNeedss = gson.fromJson(response.getString("publishedNeeds"),  
						        new TypeToken<List<PublishedNeed>>() {  
						        }.getType());
						switch (listflag) {
						case ShareData.List_init:
							mPublishedNeeds.clear();
							mPublishedNeeds.addAll(0,mPublishedNeedss);
							break;
						case ShareData.List_load:
							mPublishedNeeds.addAll(mPublishedNeeds.size(),mPublishedNeedss);
							break;
						default:
							break;
						}
						mPublishedNeedsAdapter.setmPublishedNeeds(mPublishedNeeds);
						mPublishedNeedsAdapter.notifyDataSetChanged();
						mPullToRefreshListView.onRefreshComplete();
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
				Toast.makeText(mContext, "连接服务器失败！", Toast.LENGTH_SHORT).show();
			}
		});
		mQueue.add(mJsonObjectRequest); 
	}
	
}
