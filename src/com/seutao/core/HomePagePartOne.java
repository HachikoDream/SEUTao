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
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.seutao.adapter.goodsGridViewAdapter;
import com.seutao.core.R;
import com.seutao.entity.IntroduceGood;
import com.seutao.sharedata.ShareData;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.GridView;
import android.widget.Toast;

public class HomePagePartOne extends Fragment implements Callback {
	protected static final int INTRODUCE_COME = 0;
	private static final int REFRESH = 1;
	private PullToRefreshGridView goodsGridView;// 用于显示推荐商品
	private List<IntroduceGood> intrList;
	private goodsGridViewAdapter mGridViewAddapter;
	private Gson gson;
	private GridView mGridView;
	private DisplayImageOptions options; // 显示图片的设置
	private String url = null;
	private RequestQueue mQue;
	private Handler mHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View newView = inflater.inflate(R.layout.activity_home_page_part_one,
				container, false);
		intrList = new ArrayList<IntroduceGood>();
		gson = new Gson();
		mQue = Volley.newRequestQueue(getActivity());
		mHandler = new Handler(this);
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
				.build();
		goodsGridView = (PullToRefreshGridView) newView
				.findViewById(R.id.homepage_gridview);
		mGridView = goodsGridView.getRefreshableView();
		mGridViewAddapter = new goodsGridViewAdapter(getActivity(), intrList,
				options);
		goodsGridView.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				String word1 = "东大淘，松手即惊喜";
				String word2 = "东大淘，惊喜正在加载";
				String word3 = "东大淘，加载成功！";
				refreshView.getLoadingLayoutProxy().setPullLabel(word1);
				refreshView.getLoadingLayoutProxy().setRefreshingLabel(word2);
				refreshView.getLoadingLayoutProxy().setReleaseLabel(word3);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(word3);
				getIntroduceGoods(REFRESH);
			}

		});
		goodsGridView.setAdapter(mGridViewAddapter);
		getIntroduceGoods(INTRODUCE_COME);
		goodsGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				// TODO Auto-generated method stub
					IntroduceGood introduceGood=intrList.get(position);
					int gid=introduceGood.getGoodsId();
					Intent intent = new Intent();
					intent.setClass(getActivity(), GoodsDetailInfoPage.class);
					Bundle bundle = new Bundle();
					bundle.putInt("pageKind", 1);
					bundle.putInt("gid",
							gid);
					intent.putExtras(bundle);
					startActivity(intent);
			}
		});

		return newView;
	}

	public void getIntroduceGoods(final int action) {
		url = ShareData.SEEVER_BASE_URL + "GetIntruceGoods.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						System.out.println(response.toString());
						try {
							String flag = response.getString("flag");
							if (flag.equals("ok")) {
								intrList = gson.fromJson(
										response.getString("introduceGoods"),
										new TypeToken<List<IntroduceGood>>() {
										}.getType());
								Message msg = new Message();
								msg.what = action;
								mHandler.sendMessage(msg);
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
						Toast.makeText(getActivity(), "连接服务器失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQue.add(mJsonObjectRequest);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == INTRODUCE_COME) {
			System.out.println("in handler:"+intrList);
			mGridViewAddapter.setIntrList(intrList);
			mGridViewAddapter.notifyDataSetChanged();
			
		} else if (msg.what == REFRESH) {
			mGridViewAddapter.setIntrList(intrList);
			mGridViewAddapter.notifyDataSetChanged();
			goodsGridView.onRefreshComplete();
		}
		return false;
	}

}
