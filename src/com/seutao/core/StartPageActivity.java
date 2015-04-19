package com.seutao.core;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.seutao.sharedata.ShareData;

public class StartPageActivity extends Activity implements Callback {
	private SharedPreferences sp = null;
	private static final String PHONE_NOT_EXIST ="EMPTY" ;
	private static final int TYPE_NOT_EXIST = -1;
	private static final String PWD_NOT_EXIST = "EMPTY";
	protected static final String FUID_NOT_EXIT = "EMPTY";
	protected static final int LOGIN_SUCCESS = 0;
	protected static final int LOGIN_FAILED = 1;
	private static final int THIRD_TYPE_NOT_EXIST = -1;
	private static final int FIRST_COME = 2;
	private final int COMEFROMPHONE = 23;
	private final int COMEFROMTHIRD = 22;
    private String url;
    private RequestQueue mQue;
	protected int userId;
	private Handler mHandler;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_start_page);
		mQue=Volley.newRequestQueue(this);
		mHandler=new Handler(this);
		sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		int type = sp.getInt("type", TYPE_NOT_EXIST);
		if (type == COMEFROMPHONE) {
			String phoneNum = sp.getString("phoneNum", PHONE_NOT_EXIST);
			String pwd = sp.getString("pwd", PWD_NOT_EXIST);
			RequestLogin(phoneNum, pwd);
		} else if (type == COMEFROMTHIRD) {
			String fuid = sp.getString("fuid", FUID_NOT_EXIT);
			int thirdType=sp.getInt("thirdType", THIRD_TYPE_NOT_EXIST);
			checkThirdIsFirst(fuid, thirdType);
		} else if (type == TYPE_NOT_EXIST) {
			mHandler.sendEmptyMessageDelayed(FIRST_COME, 2000);
		}

	}

	public void RequestLogin(String phoneNum, String pwd) {
		url = ShareData.SEEVER_BASE_URL + "Login.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("phoneNum", phoneNum);
		appendHeader.put("pwd", pwd);
		JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						try {
							String flag = response.getString("flag");
							if (flag.equals("ok")) {
								userId = response.getInt("userId");
								ShareData.MyId = userId;
								mHandler.sendEmptyMessageDelayed(LOGIN_SUCCESS, 2000);
							} else {
								mHandler.sendEmptyMessageDelayed(LOGIN_FAILED,2000);
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
						Toast.makeText(getBaseContext(), "连接服务器失败,请检查您的网络",
								Toast.LENGTH_SHORT).show();
					}
				});
		mJsonObjectRequest.setTag(this);
		mQue.add(mJsonObjectRequest);
	}
	public void checkThirdIsFirst(String fuid,int type) {
		url = ShareData.SEEVER_BASE_URL + "ThirdLogin.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("fuid", fuid);
		appendHeader.put("type", type+"");
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
								mHandler.sendEmptyMessageDelayed(LOGIN_FAILED,2000);
							} else {
								userId = response.getInt("userId");
								ShareData.MyId = userId;
								mHandler.sendEmptyMessageDelayed(LOGIN_SUCCESS,2000);
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
						Toast.makeText(getBaseContext(), "连接服务器失败,请检查您的网络",
								Toast.LENGTH_SHORT).show();
					}
				});
		mJsonObjectRequest.setTag(this);
		mQue.add(mJsonObjectRequest);
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==LOGIN_SUCCESS){
			Toast.makeText(getApplicationContext(), "myId is : ---"+ShareData.MyId, Toast.LENGTH_LONG);
			 Intent intentDirect = new Intent(this, MainTabContainer.class);
			 startActivity(intentDirect);
			 finish();
		}else if(msg.what==LOGIN_FAILED){
			Toast.makeText(this, "自动登陆失败，请重新登陆。", Toast.LENGTH_LONG).show();
			Intent intent=new Intent(this,LoginPage.class);
			startActivity(intent);
			finish();
		}else if(msg.what==FIRST_COME){
			Intent intent=new Intent(this,LoginPage.class);
			startActivity(intent);
			finish();
		}
		return false;
	}


}