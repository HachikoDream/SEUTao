package com.seutao.core;

import java.util.HashMap;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.seutao.sharedata.ShareData;

public class RegisterPage extends Activity implements Callback {
	private EditText phoneEt;
	private EditText pwdEt;
	private Button confirmButton;
	/**
	 * 顶部控件
	 */
	private TextView topTv;
	private Button searchBtn;
	private ImageView backIv;
	
	private String phoneNum;
	private String pwd;
	private String tips;
	private String url;
    private final String topTitle="填写注册信息";
    private final int PHONE_FIRST=0;
    private final int PHONE_NOTFIRST=1;
    private RequestQueue mQue;
    private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_page);
		ExitApplication.getInstance().addActivity(this);
		mHandler=new Handler(this);
		mQue=Volley.newRequestQueue(this);
		ShareSDK.initSDK(RegisterPage.this);
		initView();
	}
	private void initView(){
		phoneEt=(EditText)findViewById(R.id.edit_register_account);
		pwdEt=(EditText)findViewById(R.id.edit_register_password);
		confirmButton=(Button)findViewById(R.id.button_register_commite);
		confirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				phoneNum=phoneEt.getText().toString();
				pwd=pwdEt.getText().toString();
				if(isValid()){
					checkPhoneIsFirst();
				}else{
					Toast.makeText(RegisterPage.this, tips, Toast.LENGTH_LONG).show();
				}
			}
		});
		topTv=(TextView)findViewById(R.id.allayout_top_text);
		topTv.setText(topTitle);
		topTv.setTextSize(20);
		backIv=(ImageView)findViewById(R.id.alllayout_top_back);
		backIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		searchBtn=(Button)findViewById(R.id.alllayout_top_btn);
		searchBtn.setVisibility(View.INVISIBLE);
	}
	private boolean isValid(){
		phoneNum=phoneEt.getText().toString();
		pwd=pwdEt.getText().toString();
		if(phoneNum.isEmpty()){
			tips="手机号不能为空！";
			return false;
		}
		if(pwd.isEmpty()){
			tips="密码不能为空！";
			return false;
		}
		if(phoneNum.length()!=11){
			tips="手机号码长度非法！";
			return false;
		}
		if(pwd.length()<5||pwd.length()>15){
			tips="密码长度应该处于5-15之间";
			return false;
		}
		return true;
	}
	public void checkPhoneIsFirst() {
		url = ShareData.SEEVER_BASE_URL + "PhoneExits.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("phoneNum", phoneNum);
		JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						System.out.println(response.toString());
						try {
							String  flag = response.getString("flag");
							if (flag.equals("ok")) {
								Message msg = new Message();
								msg.what = PHONE_NOTFIRST;
								mHandler.sendMessage(msg);
							} else {
								Message msg = new Message();
								msg.what = PHONE_FIRST;
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
						System.out.println("myJSReq--json receive failed!");
						Toast.makeText(getBaseContext(), "连接服务器失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQue.add(mJsonObjectRequest);
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case PHONE_FIRST:
			Intent intent =new Intent(RegisterPage.this,RegisterConfirmPage.class);
			Bundle b=new Bundle();
			b.putString("IsFirst", "yes");
			b.putString("phoneNum", phoneNum);
			b.putString("pwd",pwd);
			intent.putExtras(b);
			startActivity(intent);
			break;

		case PHONE_NOTFIRST:
			Intent intentB =new Intent(RegisterPage.this,RegisterConfirmPage.class);
			Bundle bB=new Bundle();
			bB.putString("IsFirst", "not");
			bB.putString("phoneNum", phoneNum);
			bB.putString("pwd",pwd);
			intentB.putExtras(bB);
			startActivity(intentB);
			break;
		}
		return false;
	}
    

}
