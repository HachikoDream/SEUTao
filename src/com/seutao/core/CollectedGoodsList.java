package com.seutao.core;

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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.seutao.adapter.CollectedGoodsAdapter;
import com.seutao.entity.CollectedGood;
import com.seutao.sharedata.ShareData;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class CollectedGoodsList extends Fragment implements Callback
{
	private PullToRefreshListView mPullToRefreshListView=null;
	private CollectedGoodsAdapter mCollectedGoodsAdapter = null;
	private static Context mContext=null;
	private Gson gson=null;
	private RequestQueue mQueue = null;
	private String url=null;
	private Handler mHandler;
	private final static int DATA_CHANGE=21; 
    private DisplayImageOptions options;		// 显示图片的设置
    private int uid;
    //private List<CollectedGood> ShareData.mCollectedGoods=new ArrayList<CollectedGood>();
	public CollectedGoodsList(int uid) {
		super();
		this.uid = uid;
		System.out.println("uid in collected construct--"+uid);
	}
	public static void initContext(Context context){
		mContext=context;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		gson = new Gson();
		mQueue=Volley.newRequestQueue(mContext);
		mHandler=new Handler(this);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_stub)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)	 //设置图片的解码类型
		.build();
		View mCollectedGoodsList = inflater.inflate(R.layout.listview, container, false);
		mCollectedGoodsAdapter = new CollectedGoodsAdapter(ShareData.mCollectedGoods,R.layout.collected_goods_list_item,mContext,options);
		getCollectedGoods(ShareData.List_init, 0);
		mPullToRefreshListView=(PullToRefreshListView)mCollectedGoodsList.findViewById(R.id.listview_lv);
		mPullToRefreshListView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				getCollectedGoods(ShareData.List_init, 0);
			}
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if(ShareData.mCollectedGoods.size()==0){
					getCollectedGoods(ShareData.List_init, 0);
				}else{
					getCollectedGoods(ShareData.List_load, ShareData.mCollectedGoods.get(ShareData.mCollectedGoods.size()-1).getId());
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
		registerForContextMenu(actualListView);
		mPullToRefreshListView.setAdapter(mCollectedGoodsAdapter);
		return mCollectedGoodsList;
	}
	
	public CollectedGoodsAdapter getAdapter() {
		return mCollectedGoodsAdapter;
	}
	
	
	public void getCollectedGoods(int flag,int current){
		final int listflag=flag;
		url=ShareData.SEEVER_BASE_URL+"getCollectedGoods.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		System.out.println("uid in collected--"+uid);
		appendHeader.put("uid", String.valueOf(uid));
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
						List<CollectedGood> mCollectedGoodss= gson.fromJson(response.getString("collectedGoods"),  
						        new TypeToken<List<CollectedGood>>() {  
						        }.getType());
						switch (listflag) {
						case ShareData.List_init:
							ShareData.mCollectedGoods.clear();
							ShareData.mCollectedGoods.addAll(0,mCollectedGoodss);
							break;
						case ShareData.List_load:
							ShareData.mCollectedGoods.addAll(ShareData.mCollectedGoods.size(),mCollectedGoodss);
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
			mCollectedGoodsAdapter.setmCollectedGoods(ShareData.mCollectedGoods);
			mCollectedGoodsAdapter.notifyDataSetChanged();
			mPullToRefreshListView.onRefreshComplete();
		}
		return false;
	}
}
