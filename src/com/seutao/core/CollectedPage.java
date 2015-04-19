/*package com.example.seutao;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CollectedPage extends Activity {
	private Context context=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collected_page);
		context=this;
		((Button)findViewById(R.id.collected_page_btn_topback)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Activity)context).finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collected_page, menu);
		return true;
	}

}
 */

package com.seutao.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.seutao.entity.CollectedGood;
import com.seutao.entity.CollectedNeed;
import com.seutao.sharedata.ShareData;

public class CollectedPage extends FragmentActivity implements Callback {
	private int uid;
	private Context context = null;
	private ViewPager mViewPager = null;
	private FragmentPagerAdapter mAdapter = null;
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	private Button mBtnGoods;
	private Button mBtnNeeds;
	private RelativeLayout mRLBottom = null;
	private CheckBox btnSelectAll = null;
	private Button btnDelete = null;
	private ImageView goBackIv;
	private TextView topTv;
	private Button topBtn;
	private ImageView belowGoodsLine;
	private ImageView belowNeedsLine;
	private CollectedGoodsList mCollectedGoodsList = null;
	private CollectedNeedsList mCollectedNeedsList = null;
	private int currentIndex = 0;
	private String cids = "";
	private RequestQueue mQueue = null;
	private String url = null;
	private final static String title = "收藏列表";
	private Handler mHandler;
	private final static int GOODS_CHANGED = 118;
	private final static int NEEDS_CHANGED = 119;
	private List<CollectedGood> mCollectedGoods = ShareData.mCollectedGoods;
	private List<CollectedNeed> mCollectedNeeds = ShareData.mCollectedNeeds;
	@Override
	public void finish() {
		ShareData.collectedIsEdit = false;
		ShareData.collectedIsSelectAll = false;
		super.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		mQueue = Volley.newRequestQueue(context);
		setContentView(R.layout.activity_collected_page);
		Intent intent = this.getIntent();
		uid = intent.getIntExtra("uid", -1);
		mHandler = new Handler(this);
		belowGoodsLine = (ImageView) findViewById(R.id.collected_page_btn_goods_below);
		belowNeedsLine = (ImageView) findViewById(R.id.collected_page_btn_needs_below);
		topTv = (TextView) findViewById(R.id.listlayout_top_text);
		topTv.setText(title);
		goBackIv = (ImageView) findViewById(R.id.listlayout_top_back);
		goBackIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mRLBottom = (RelativeLayout) findViewById(R.id.collected_page_rl_bottom);
		mRLBottom.setVisibility(View.GONE);
		btnSelectAll = (CheckBox) findViewById(R.id.collected_page_cb_selectall);
		btnDelete = (Button) findViewById(R.id.collected_page_btn_delete);
		topBtn = (Button) findViewById(R.id.listlayout_top_btn);
		mViewPager = (ViewPager) findViewById(R.id.collected_page_viewpager);
		initView();
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mFragments.get(arg0);
			}
		};
		mViewPager.setAdapter(mAdapter);
		if (uid == ShareData.MyId) {
			btnSelectAll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ShareData.collectedIsSelectAll = !ShareData.collectedIsSelectAll;
					btnSelectAll.setChecked(ShareData.collectedIsSelectAll);
					switch (currentIndex) {
					case 0:
						for (int i = 0; i < mCollectedGoods.size(); i++) {
							mCollectedGoods.get(i).setIsSelect(
									ShareData.collectedIsSelectAll);
						}
						mCollectedGoodsList.getAdapter().notifyDataSetChanged();
						break;
					case 1:
						for (int j = 0; j < mCollectedNeeds.size(); j++) {
							mCollectedNeeds.get(j).setIsSelect(
									ShareData.collectedIsSelectAll);
						}
						mCollectedNeedsList.getAdapter().notifyDataSetChanged();
					default:
						break;
					}
				}
			});

			topBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ShareData.collectedIsEdit = !ShareData.collectedIsEdit;
					if (ShareData.collectedIsEdit) {
						topBtn.setText("完成");
						mRLBottom.setVisibility(View.VISIBLE);
						switch (currentIndex) {
						case 0:
							for (int i = 0; i < mCollectedGoods.size(); i++) {
								mCollectedGoods.get(i).setIsSelect(false);
							}
							mCollectedGoodsList.getAdapter()
									.notifyDataSetChanged();
							break;
						case 1:
							for (int j = 0; j < mCollectedNeeds.size(); j++) {
								mCollectedNeeds.get(j).setIsSelect(false);
							}
							mCollectedGoodsList.getAdapter()
									.notifyDataSetChanged();
							break;
						}
					} else {
						topBtn.setText("编辑");
						mRLBottom.setVisibility(View.GONE);
					}
				}
			});
			btnDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int num = 0;
					// TODO Auto-generated method stub
					switch (currentIndex) {
					case 0:
						for (int i = 0; i < mCollectedGoods.size(); i++) {
							if (mCollectedGoods.get(i).getIsSelect()) {
								num++;
								cids += mCollectedGoods.get(i).getId() + "|";
							}
						}
						if (num == 0) {
							Toast.makeText(context, "您还没选择任何收藏哦~",
									Toast.LENGTH_SHORT).show();
						} else {
							dialog(num);
							num = 0;
						}
						break;
					case 1:
						for (int j = 0; j < mCollectedNeeds.size(); j++) {
							if (mCollectedNeeds.get(j).getIsSelect()) {
								num++;
								cids += mCollectedNeeds.get(j).getId() + "|";
							}
						}
						if (num == 0) {
							Toast.makeText(context, "您还没选择任何收藏哦~",
									Toast.LENGTH_SHORT).show();
						} else {
							dialog(num);
							num = 0;
						}
					default:
						break;
					}
				}
			});
			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					topBtn.setText("编辑");
					mRLBottom.setVisibility(View.GONE);
					ShareData.collectedIsEdit = false;
					ShareData.collectedIsSelectAll = false;
					btnSelectAll.setChecked(false);
					resetTabBtn();
					switch (position) {
					case 0:
						belowGoodsLine.setImageDrawable(getResources()
								.getDrawable(R.color.yellow));
						for (int i = 0; i < mCollectedNeeds.size(); i++) {
							mCollectedNeeds.get(i).setIsSelect(false);
						}
						break;
					case 1:
						belowNeedsLine.setImageDrawable(getResources()
								.getDrawable(R.color.yellow));
						for (int j = 0; j < mCollectedGoods.size(); j++) {
							mCollectedGoods.get(j).setIsSelect(false);
						}
						break;
					}
					currentIndex = position;
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
		} else {
			topBtn.setVisibility(View.INVISIBLE);
			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					resetTabBtn();
					switch (position) {
					case 0:
						belowGoodsLine.setImageDrawable(getResources()
								.getDrawable(R.color.yellow));
						break;
					case 1:
						belowNeedsLine.setImageDrawable(getResources()
								.getDrawable(R.color.yellow));
						break;
					}
					currentIndex = position;
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
		}
	}

	protected void resetTabBtn() {
		mBtnGoods.setBackgroundColor(getResources()
				.getColor(R.color.main_color));
		mBtnNeeds.setBackgroundColor(getResources()
				.getColor(R.color.main_color));
		belowGoodsLine.setImageDrawable(getResources().getDrawable(
				R.color.main_color));
		belowNeedsLine.setImageDrawable(getResources().getDrawable(
				R.color.main_color));

	}

	private void initView() {

		mBtnGoods = (Button) findViewById(R.id.collected_page_btn_goods);
		mBtnGoods.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(0);
			}
		});
		mBtnNeeds = (Button) findViewById(R.id.collected_page_btn_needs);
		mBtnNeeds.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(1);
			}
		});
		CollectedGoodsList.initContext(this);
		mCollectedGoodsList = new CollectedGoodsList(uid);
		mFragments.add(mCollectedGoodsList);
		CollectedNeedsList.initContext(this);
		mCollectedNeedsList = new CollectedNeedsList(uid);
		mFragments.add(mCollectedNeedsList);

	}

	protected void dialog(int i) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("确认要删除这" + String.valueOf(i) + "个收藏吗");
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (currentIndex) {
						case 0:
							deleteCollectedGoods(cids);
							cids = "";
							break;
						case 1:
							deleteCollectedNeeds(cids);
							cids = "";
						default:
							break;
						}
						dialog.dismiss();
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						cids = "";
						dialog.dismiss();
					}
				});

		builder.create().show();
	}

	public void deleteCollectedGoods(String cids) {
		url = ShareData.SEEVER_BASE_URL + "deleteCollectedGoods.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("cids", cids);
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
								for (int i = 0; i < mCollectedGoods.size();) {
									if (mCollectedGoods.get(i).getIsSelect())
										mCollectedGoods.remove(i);
									else
										i++;
								}
								Message msg = new Message();
								msg.what = GOODS_CHANGED;
								mHandler.sendMessage(msg);
							} else {
								Toast.makeText(context, flag,
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
						Toast.makeText(context, "连接服务器失败！", Toast.LENGTH_SHORT)
								.show();
					}
				});
		mQueue.add(mJsonObjectRequest);
	}

	public void deleteCollectedNeeds(String cids) {
		url = ShareData.SEEVER_BASE_URL + "deleteCollectedNeeds.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("cids", cids);
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
								for (int j = 0; j < mCollectedNeeds.size();) {
									if (mCollectedNeeds.get(j).getIsSelect())
										mCollectedNeeds.remove(j);
									else
										j++;
								}
								Message msg = new Message();
								msg.what = NEEDS_CHANGED;
								mHandler.sendMessage(msg);
							} else {
								Toast.makeText(context, flag,
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
						Toast.makeText(context, "连接服务器失败！", Toast.LENGTH_SHORT)
								.show();
					}
				});
		mQueue.add(mJsonObjectRequest);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == NEEDS_CHANGED) {
			mCollectedNeedsList.getAdapter().notifyDataSetChanged();
		} else if (msg.what == GOODS_CHANGED) {
			mCollectedGoodsList.getAdapter().notifyDataSetChanged();

		}

		return false;
	}
}
