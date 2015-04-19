package com.seutao.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.seutao.adapter.PublishedGoodsAdapter;
import com.seutao.entity.PublishedGood;
import com.seutao.sharedata.ShareData;

public class PublishedGoodsList extends Fragment
{
	private PullToRefreshListView mPullToRefreshListView=null;
	private PublishedGoodsAdapter mPublishedGoodsAdapter = null;
	private static Context mContext=null;
	private Gson gson=null;
	private RequestQueue mQueue = null;
	private String url=null;
	private  int uid;
	private   List<PublishedGood> mPublishedGoods=new ArrayList<PublishedGood>();
	 private DisplayImageOptions options;		// 显示图片的设置
	public static void initContext(Context context){
		mContext=context;
	}
	public PublishedGoodsList(int uid) {
		super();
		this.uid = uid;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		gson = new Gson();
		mQueue=Volley.newRequestQueue(mContext);
		View mPublishedGoodsList = inflater.inflate(R.layout.listview, container, false);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_stub)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)	 //设置图片的解码类型
		.build();
		mPublishedGoodsAdapter = new PublishedGoodsAdapter(mPublishedGoods,R.layout.published_goods_list_item,mContext,options,uid);
		getPublishedGoods(ShareData.List_init, 0);
		mPullToRefreshListView=(PullToRefreshListView)mPublishedGoodsList.findViewById(R.id.listview_lv);		
		mPullToRefreshListView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				getPublishedGoods(ShareData.List_init, 0);
			}
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if(mPublishedGoods.size()==0){
					getPublishedGoods(ShareData.List_init, 0);
				}else{
					getPublishedGoods(ShareData.List_load, mPublishedGoods.get(mPublishedGoods.size()-1).getId());
				}
			}
		});
		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				Toast.makeText(mContext, "没有更多数据!", Toast.LENGTH_SHORT).show();
			}
		});
		ListView actualListView = mPullToRefreshListView.getRefreshableView();
		registerForContextMenu(actualListView);
		mPullToRefreshListView.setAdapter(mPublishedGoodsAdapter);
		return mPublishedGoodsList;
	}
	
	public void getPublishedGoods(int flag,int current){
		final int listflag=flag;
		url=ShareData.SEEVER_BASE_URL+"getPublishedGoods.json";
		Map<String, String> appendHeader = new HashMap<String, String>();  
		appendHeader.put("uid", uid+"");
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
						List<PublishedGood> mPublishedGoodss = gson.fromJson(response.getString("publishedGoods"),  
						        new TypeToken<List<PublishedGood>>() {  
						        }.getType());
						switch (listflag) {
						case ShareData.List_init:
							mPublishedGoods.clear();
							mPublishedGoods.addAll(0,mPublishedGoodss);
							break;
						case ShareData.List_load:
							mPublishedGoods.addAll(mPublishedGoods.size(),mPublishedGoodss);
							break;
						default:
							break;
						}
						mPublishedGoodsAdapter.setmPublishedGoods(mPublishedGoods);
						mPublishedGoodsAdapter.notifyDataSetChanged();
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
