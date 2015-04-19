package com.seutao.core;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.seutao.sharedata.ShareData;
import com.seutao.tools.Tools;

public class SettingsPage extends Activity {
	private Context context=null;
	private Switch sw_collected=null;
	//private Button btn_modifyPwd=null;
	private RelativeLayout layout_inspectNew=null;
	private RelativeLayout layout_feedBack=null;
	private RelativeLayout layout_logOff=null;
	private RequestQueue mQueue = null;
	private Button topBtn;
	private ImageView goBackIv;
	private TextView topTv;
	private static final String title="设置";
	private String url=null;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_page);
		ExitApplication.getInstance().addActivity(this);
		context=this;
		mQueue=Volley.newRequestQueue(context);
		topBtn=(Button)findViewById(R.id.listlayout_top_btn);
		goBackIv=(ImageView)findViewById(R.id.listlayout_top_back);
		topTv=(TextView)findViewById(R.id.listlayout_top_text);
		topBtn.setVisibility(View.GONE);
		topTv.setText(title);
		goBackIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		sw_collected=(Switch)findViewById(R.id.settings_page_switch_collected);
		//btn_modifyPwd=(Button)findViewById(R.id.settings_page_btn_modifypwd);
		layout_inspectNew=(RelativeLayout)findViewById(R.id.settings_page_btn_inspectnew);
		layout_feedBack=(RelativeLayout)findViewById(R.id.settings_page_btn_feedback);
		layout_logOff=(RelativeLayout)findViewById(R.id.settings_page_btn_logoff);
		switch (ShareData.mMyPersonInfo.getColPublic()) {
		case ShareData.PUBLIC_NO:
			sw_collected.setChecked(false);
			break;
		case ShareData.PUBLIC_YES:
			sw_collected.setChecked(true);
			break;
		default:
			break;
		}
		
		layout_inspectNew.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				url=ShareData.SEEVER_BASE_URL+"inspectNewVersion.json";
				JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(url,null , new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						System.out.println(response.toString());
						try {
							String flag = response.getString("flag");
							if (flag.equals("ok")) {
								String url = response.getString("url");
								if (url.equals("#")) {
									Toast.makeText(context,"当前已是最新版本！",Toast.LENGTH_LONG).show();
								}
								else {
									Toast.makeText(context,"点击下载："+url,Toast.LENGTH_LONG).show();
								}
							}
							else {
								Toast.makeText(context,flag,Toast.LENGTH_LONG).show();
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
						Toast.makeText(context, "连接服务器失败！", Toast.LENGTH_SHORT).show();
					}
				});
				mQueue.add(jsonObjectRequest); 
			}
		});
		
		layout_feedBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("android.intent.action.FeedbackPage");
				startActivity(intent);
			}
		});
		layout_logOff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder=new AlertDialog.Builder(context);
				builder.setTitle("您确认要注销吗？");
				builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						sp=getSharedPreferences("user_info", Context.MODE_PRIVATE);
						Editor editor=sp.edit();
						editor.clear().commit();
						ShareData.MyId=-1;
						Intent intent =new Intent(SettingsPage.this,LoginPage.class);
						startActivity(intent);
						ExitApplication.getInstance().exit();
					    
					}
				  });
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
					   @Override
					   public void onClick(DialogInterface dialog, int which) {
						   dialog.dismiss();
					   }
				  });
				builder.create().show();
			}
		});
	}

	/*public void checkPassword(String pwd){
		url=ShareData.SEEVER_BASE_URL+"checkPassword.json";
		Map<String, String> appendHeader = new HashMap<String, String>();  
		appendHeader.put("uid", String.valueOf(ShareData.MyId));
		appendHeader.put("pwd", pwd);
		JsonObjectRequest mJsonObjectRequest=new JsonObjectRequest(url, new JSONObject(appendHeader), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				System.out.println(response.toString());
				try {
					String flag=response.getString("flag");
					if (flag.equals("ok")) {
						System.out.println("zhuan-------");
						Intent intent = new Intent();
						intent.setAction("android.intent.action.ModifyPasswordPage");
						startActivity(intent);
					}
					else {
						Toast.makeText(context, flag, Toast.LENGTH_SHORT).show();
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
				Toast.makeText(context, "连接服务器失败！", Toast.LENGTH_SHORT).show();
			}
		});
		mQueue.add(mJsonObjectRequest); 
	}*/
}
