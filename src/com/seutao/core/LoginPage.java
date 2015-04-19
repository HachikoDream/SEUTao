package com.seutao.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.seutao.sharedata.ShareData;
import com.seutao.tools.Tools;
import com.seutao.view.CustomProgressDialog;

public class LoginPage extends Activity implements PlatformActionListener,
		Callback, TagAliasCallback {
	protected static final int LOGIN_SUCCESS = 0;
	protected static final int THIRD_LOGIN_SUCCESS = 2;
	protected static final int FIRST_COMEIN = 1;
	private EditText phoneEt;
	private EditText pwdEt;
	private Button mLoginButton;
	private Button mRegisterButton;
	private Button LoginBySinaWeibo;
	private Button LoginByRenRen;
	private Button LoginByQQ;
	private String url;
	private String phoneNum;
	private String pwd;
	private String fuid;
	private int userId;
	private Handler mHandler;
	private RequestQueue mQue;
	private final int COMEFROMTHIRD = 22;
	private final int COMEFROMPHONE = 23;
	private final int LOGINBYRENREN = 2;
	private final int LOGINBYWEIBO = 0;
	private final int LOGINBYQQ = 1;
	private int source;
	private String userGender;
	private String userName;
	private String userAvaterUrl;
	private SharedPreferences sp;
	private CustomProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_page);
		ExitApplication.getInstance().addActivity(this);
		ShareSDK.initSDK(LoginPage.this);
		mHandler = new Handler(this);
		mQue = Volley.newRequestQueue(this);
		initView();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mQue.cancelAll(this);
		stopProgressDialog();
		super.onDestroy();

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

	private void initView() {
		phoneEt = (EditText) findViewById(R.id.Login_userName);
		pwdEt = (EditText) findViewById(R.id.Login_pwd);
		mLoginButton = (Button) findViewById(R.id.Login_page_loginButton);
		mRegisterButton = (Button) findViewById(R.id.Login_page_RegisterButton);
		mRegisterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginPage.this, RegisterPage.class);
				startActivity(intent);
			}
		});
		mLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				phoneNum = phoneEt.getText().toString();
				pwd = pwdEt.getText().toString();
				if (isValid()) {
					startProgressDialog();
					RequestLogin(phoneNum, pwd);
				}
			}
		});
		LoginByRenRen = (Button) findViewById(R.id.login_loginbyRenRen);
		LoginByRenRen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				source = LOGINBYRENREN;
				Platform renren = ShareSDK.getPlatform(Renren.NAME);
				authorize(renren);
			}
		});
		LoginByQQ = (Button) findViewById(R.id.login_loginbyQQ);
		LoginByQQ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				source = LOGINBYQQ;
				Platform qq = ShareSDK.getPlatform(QQ.NAME);
				authorize(qq);
			}
		});
		LoginBySinaWeibo = (Button) findViewById(R.id.login_loginbySinaWeibo);
		LoginBySinaWeibo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				source = LOGINBYWEIBO;
				Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
				authorize(sinaWeibo);
			}
		});
	}

	private void authorize(Platform plat) {
		plat.removeAccount();
		ShareSDK.removeCookieOnAuthorize(true);
		plat.setPlatformActionListener(this);
		// 关闭SSO授权
		plat.SSOSetting(true);
		plat.showUser(null);
		startProgressDialog();
	}

	public boolean isValid() {
		if (phoneNum.isEmpty()) {
			showTextByToast("用户名不能为空！");
			return false;
		}
		if (pwd.isEmpty()) {
			showTextByToast("密码不能为空！");
			return false;
		}
		return true;
	}

	private void showTextByToast(String txt) {
		Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete(Platform plat, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		PlatformDb platDB = plat.getDb();// 获取数平台数据DB
		// 通过DB获取各种数据
		userGender = platDB.getUserGender();
		userAvaterUrl = platDB.getUserIcon();
		fuid = platDB.getUserId();
		userName = platDB.getUserName();
		System.out.println("userid:  " + userId + " userGender:   "
				+ userGender + "userName:   " + userName + "userImage:  "
				+ userAvaterUrl);
		checkThirdIsFirst();
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub

	}

	public void RequestLogin(String phoneNum, String pwd) {
		url = ShareData.SEEVER_BASE_URL + "Login.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("phoneNum", phoneNum);
		appendHeader.put("pwd", Tools.MD5(pwd));
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
								userId = response.getInt("userId");
								ShareData.MyId = userId;
								Message msg = new Message();
								msg.what = LOGIN_SUCCESS;
								mHandler.sendMessage(msg);
							} else {
								Toast.makeText(getBaseContext(), flag,
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
						Toast.makeText(getBaseContext(), "连接服务器失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mJsonObjectRequest.setTag(this);
		mQue.add(mJsonObjectRequest);
	}

	public void checkThirdIsFirst() {
		url = ShareData.SEEVER_BASE_URL + "ThirdLogin.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("fuid", fuid);
		appendHeader.put("type", source + "");
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
								msg.what = FIRST_COMEIN;
								mHandler.sendMessage(msg);
							} else {
								userId = response.getInt("userId");
								ShareData.MyId = userId;
								Message msg = new Message();
								msg.what = THIRD_LOGIN_SUCCESS;
								mHandler.sendMessage(msg);
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
		mJsonObjectRequest.setTag(this);
		mQue.add(mJsonObjectRequest);
	}

	private void saveInfoInLocalOfPhoneLogin() {
		sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		Editor editer = sp.edit();
		editer.clear();
		editer.putInt("type", COMEFROMPHONE);
		editer.putString("phoneNum", phoneNum);
		System.out.println("pwd before:  " + pwd + "pwd after:"
				+ Tools.MD5(pwd));
		editer.putString("pwd", Tools.MD5(pwd));
		editer.apply();
	}

	private void saveInfoInLocalOfThirdLogin() {
		sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		Editor editer = sp.edit();
		editer.clear();
		editer.putInt("type", COMEFROMTHIRD);
		editer.putInt("thirdType", source);
		editer.putString("fuid", fuid);
		editer.apply();
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == LOGIN_SUCCESS) {
			// stopProgressDialog();
			saveInfoInLocalOfPhoneLogin();
			JPushInterface.setAlias(this, ShareData.MyId + "", this);
		} else if (msg.what == FIRST_COMEIN) {
			stopProgressDialog();
			Intent intent = new Intent(LoginPage.this, RegisterDetailInfo.class);
			Bundle b = new Bundle();
			b.putString("fuid", fuid);
			b.putString("name", userName);
			b.putString("profile_image_url", userAvaterUrl);
			b.putString("gender", userGender);
			b.putInt("userId", userId);
			b.putInt("source", source);
			b.putInt("SourcePage", COMEFROMTHIRD);
			intent.putExtras(b);
			startActivity(intent);
			finish();
		} else if (msg.what == THIRD_LOGIN_SUCCESS) {
			stopProgressDialog();
			saveInfoInLocalOfThirdLogin();
			Intent intent = new Intent(LoginPage.this, MainTabContainer.class);
			startActivity(intent);
			finish();
		}
		return false;
	}

	@Override
	public void gotResult(int responseCode, String alias, Set<String> tags) {
		// TODO Auto-generated method stub
		if (responseCode == 0) {
			Toast.makeText(getApplicationContext(), "alias is: "+alias,Toast.LENGTH_LONG).show();
			stopProgressDialog();
			Intent intent = new Intent(LoginPage.this, MainTabContainer.class);
			startActivity(intent);
			finish();
		} else {
			Toast.makeText(this, "请检查网络连接", Toast.LENGTH_LONG).show();
		}
	}
}
