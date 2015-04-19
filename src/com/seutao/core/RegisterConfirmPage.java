package com.seutao.core;

import java.util.HashMap;
import java.util.Map;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.seutao.sharedata.ShareData;
import com.seutao.tools.Tools;

public class RegisterConfirmPage extends Activity implements Callback {
	private EditText confirmEt;// 接受用户输入的验证码
	private Button resendButton;// 重新发送验证码按钮
	private Button checkButton;// 验证按钮
	/*
	 * 顶部控件
	 */
	private TextView TopTv;
	private ImageView gobackIv;
	private Button TopIconBtn;

	private String phoneNum;
	private String pwd;
	private String appkey = "58f88d067edc";
	private String appsecret = "839ce16250d011433490634bbe4761a7";
	private Handler handler;
	private final int BUTTON_ABLE = 411;
	private final int BUTTON_ENABLE = 243;
	private final int RECEIVE_MSG = 826;
	private int enableTime = 60;
	private String topString = "填写验证码";
	private String url;
	private String isFirst;
	private final int COMEFROMPHONE = 23;
	private Handler mHandler;
	private RequestQueue mQue;
	private final static int RESET_SUCCESS = 886;
	private TextView tipsTv;
	private SharedPreferences sp;
	private final static String tipsOfFirst="您好，您是首次注册，我们已经将验证码发送到您的手机，请注意查收！";
    private final static String tipsOfNotFirst="您好，检测到本手机号已注册，输入收到的验证码，我们将密码更改为您刚才输入的密码";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receiveconfirm_page);
		mHandler = new Handler(this);
		mQue = Volley.newRequestQueue(this);
		TopTv = (TextView) findViewById(R.id.allayout_top_text);
		gobackIv = (ImageView) findViewById(R.id.alllayout_top_back);
		tipsTv=(TextView)findViewById(R.id.register_tipstext);
		TopIconBtn = (Button) findViewById(R.id.alllayout_top_btn);
		TopTv.setText(topString);
		TopTv.setTextSize(20);
		gobackIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TopIconBtn.setVisibility(View.INVISIBLE);
		confirmEt = (EditText) findViewById(R.id.register_confirmEdit);
		resendButton = (Button) findViewById(R.id.register_resendButton);
		resendButton.setEnabled(false);
		resendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				enableTime = 60;
				SMSSDK.getVerificationCode("86", phoneNum);
				resendButton.setEnabled(false);
				handler.post(timeConsumeBackground);
			}
		});
		checkButton = (Button) findViewById(R.id.register_checkButton);

		Bundle b = this.getIntent().getExtras();
		handler = new Handler(this);
		handler.post(timeConsumeBackground);
		SMSSDK.initSDK(this, appkey, appsecret);
		EventHandler eh = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.what = RECEIVE_MSG;
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册短信验证的监听
		SMSSDK.registerEventHandler(eh);
		phoneNum = b.getString("phoneNum");
		pwd = b.getString("pwd");
		isFirst = b.getString("IsFirst");
		if(isFirst.equals("yes")){
			tipsTv.setText(tipsOfFirst);
		}else{
			tipsTv.setText(tipsOfNotFirst);
		}
		SMSSDK.getVerificationCode("86", phoneNum);
		checkButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String verifyCode = confirmEt.getText().toString();
				SMSSDK.submitVerificationCode("86", phoneNum, verifyCode);
			}
		});
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == RECEIVE_MSG) {

			if (msg.arg2 == SMSSDK.RESULT_ERROR) {
				Toast.makeText(this, "验证码错误，请您检查手机重新输入，或等待重新发送验证码到手机",
						Toast.LENGTH_SHORT).show();
			} else {
				switch (msg.arg1) {
				case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE: {
					Toast.makeText(this, "提交验证码成功", Toast.LENGTH_SHORT).show();
					if (isFirst.equals("yes")) {
						Intent intent = new Intent(RegisterConfirmPage.this,
								RegisterDetailInfo.class);
						Bundle b = new Bundle();
						b.putString("phoneNum", phoneNum);
						b.putString("pwd", pwd);
						b.putInt("SourcePage", COMEFROMPHONE);
						intent.putExtras(b);
						startActivity(intent);
						ExitApplication.getInstance().closeActivityInList();
						ExitApplication.getInstance().clearAll();
						finish();
						
					} else {
                            ResetPhonePwd();
					}
				}
					break;
				case SMSSDK.EVENT_GET_VERIFICATION_CODE: {
					Toast.makeText(this, "验证码已经发送", Toast.LENGTH_SHORT).show();
				}
					break;
				}
			}
		} else if (msg.what == BUTTON_ENABLE) {
			enableTime--;
			resendButton.setText("重新发送验证码(" + enableTime + ")");
		} else if (msg.what == BUTTON_ABLE) {
			resendButton.setEnabled(true);
			resendButton.setText("重新发送验证码到手机");
		} else if (msg.what == RESET_SUCCESS) {
			saveInfoInLocalOfPhoneLogin();
			Intent intent = new Intent(RegisterConfirmPage.this,
					MainTabContainer.class);
			startActivity(intent);
			ExitApplication.getInstance().closeActivityInList();
			ExitApplication.getInstance().clearAll();
			finish();
	
		}
		return false;

	}
	private void saveInfoInLocalOfPhoneLogin() {
		sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		Editor editer = sp.edit();
		editer.clear();
		editer.putInt("type", COMEFROMPHONE);
		editer.putString("phoneNum", phoneNum);
		editer.putString("pwd", Tools.MD5(pwd));
		editer.apply();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		SMSSDK.unregisterAllEventHandler();
		if (enableTime > 0) {
			handler.removeCallbacks(timeConsumeBackground);
		}
		super.onDestroy();
	}

	private Runnable timeConsumeBackground = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (enableTime > 0) {
				Message msg = new Message();
				msg.what = BUTTON_ENABLE;
				handler.sendMessage(msg);
				handler.postDelayed(timeConsumeBackground, 1000);
			} else {
				Message msg = new Message();
				msg.what = BUTTON_ABLE;
				handler.sendMessage(msg);
				handler.removeCallbacks(timeConsumeBackground);
			}

		}
	};

	public void ResetPhonePwd() {
		url = ShareData.SEEVER_BASE_URL + "ResetPhonePwd.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
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
								int userId = response.getInt("userId");
								ShareData.MyId=userId;
								Message msg = new Message();
								msg.what = RESET_SUCCESS;
								mHandler.sendMessage(msg);
							} else {
								Toast.makeText(RegisterConfirmPage.this, flag,
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

}
