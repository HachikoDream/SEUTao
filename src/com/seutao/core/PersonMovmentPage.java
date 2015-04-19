package com.seutao.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seutao.adapter.MovementAdapter;
import com.seutao.entity.Movement;
import com.seutao.sharedata.ShareData;
import com.seutao.view.CustomProgressDialog;
import com.seutao.view.SwipeDismissListView;
import com.seutao.view.SwipeDismissListView.OnDismissCallback;

public class PersonMovmentPage extends Activity implements Callback {
	private SwipeDismissListView movementListview;
	private List<Movement> movementList;
	private MovementAdapter movementAdapter;
	private RequestQueue mQue;
	private Handler mHandler;
	private Gson gson;
	private String url;
	private static final int SYS_MSG = 0;
	private static final int GOODS_MSG = 1;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getMovement();
		super.onResume();
	}

	private static final int WANT_MSG = 2;
	protected static final int MOVEMENT_DATE = 0;
	protected static final int ITEM_DELETE = 1;
	private CustomProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_movment_page);
		movementList = new ArrayList<Movement>();
		mQue = Volley.newRequestQueue(this);
		gson = new Gson();
		mHandler = new Handler(this);
		movementListview = (SwipeDismissListView) findViewById(R.id.person_movement_swipeDismissListView);
		movementAdapter = new MovementAdapter(this, movementList);
		movementListview.setAdapter(movementAdapter);
		movementListview.setOnDismissCallback(new OnDismissCallback() {

			@Override
			public void onDismiss(int dismissPosition) {
				// TODO Auto-generated method stub
				deleteMovement(movementList.get(dismissPosition)
						.getMovementid(), dismissPosition);
			}
		});
		getMovement();
		movementListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View itemView,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println("movement list positon:  "+position);
				Movement mM = (Movement) movementList.get(position);
				readMovement(mM.getMovementid());
				Intent intent = new Intent();
				switch (mM.getType()) {
				case SYS_MSG:
					break;
				case GOODS_MSG:
					intent.setClass(PersonMovmentPage.this, NewsPage.class);
					intent.putExtra("type", 1);
					intent.putExtra("gid", mM.getRelatedId());
					startActivity(intent);
					break;
				case WANT_MSG:
					intent.setClass(PersonMovmentPage.this, NewsPage.class);
					intent.putExtra("type", 2);
					intent.putExtra("wid", mM.getRelatedId());
					startActivity(intent);
					break;
				}
			}

		});

	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage("");
		}

		progressDialog.show();
	}

	private void getMovement() {
		System.out.println();
		startProgressDialog();
		url = ShareData.SEEVER_BASE_URL + "getMovement.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("uid", ShareData.MyId + "");
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
								movementList = gson.fromJson(
										response.getString("movementList"),
										new TypeToken<List<Movement>>() {
										}.getType());
								Message msg = new Message();
								msg.what = MOVEMENT_DATE;
								mHandler.sendMessage(msg);
							} else {
								Toast.makeText(getApplicationContext(), flag,
										Toast.LENGTH_LONG).show();
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
						Toast.makeText(getBaseContext(), "连接服务器失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQue.add(mJsonObjectRequest);
	}

	private void deleteMovement(int mid, final int dispostion) {
		startProgressDialog();
		url = ShareData.SEEVER_BASE_URL + "deleteMovement.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("movementId", mid + "");
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
								Message msg = new Message();
								msg.what = ITEM_DELETE;
								msg.arg1 = dispostion;
								mHandler.sendMessage(msg);
							} else {
								Toast.makeText(getApplicationContext(), flag,
										Toast.LENGTH_LONG).show();
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
						Toast.makeText(getBaseContext(), "删除失败,请检查网络环境",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQue.add(mJsonObjectRequest);
	}

	private void readMovement(int mid) {
		startProgressDialog();
		url = ShareData.SEEVER_BASE_URL + "readMovement.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("movementId", mid + "");
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
								stopProgressDialog();
							} else {
								Toast.makeText(getApplicationContext(), flag,
										Toast.LENGTH_LONG).show();
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
						Toast.makeText(getBaseContext(), "删除失败,请检查网络环境",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQue.add(mJsonObjectRequest);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == MOVEMENT_DATE) {
            stopProgressDialog();
			System.out.println("------movement date in ! movementlist:"
					+ movementList);
			movementAdapter.setMovementList(movementList);
			movementAdapter.notifyDataSetChanged();
					} else if (msg.what == ITEM_DELETE) {
			stopProgressDialog();
			movementList.remove(msg.arg1);
			movementAdapter.setMovementList(movementList);
			movementAdapter.notifyDataSetChanged();
			Toast.makeText(getApplicationContext(), "删除成功！", Toast.LENGTH_LONG)
					.show();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stopProgressDialog();
		super.onDestroy();
	}
}
