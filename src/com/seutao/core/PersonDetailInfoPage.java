package com.seutao.core;

import java.util.HashMap;
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
import com.seutao.entity.PersonDetailInfo;
import com.seutao.sharedata.ShareData;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonDetailInfoPage extends Activity implements Callback {
	protected static final int INFO_COME = 0;
	private Context context = null;
	private int uid;
	private PersonDetailInfo mPersonDetailInfo = null;
	private Gson gson = null;
	private RequestQueue mQueue = null;
	private String url = null;
	private TextView userNameTv;
	private TextView phoneTv;
	private TextView qqTv;
	private TextView schoolTv;
	private TextView departmentTv;
	private TextView enrollTimeTv;
	private TextView personsignTv;
	private TextView hometownTv;
	private TextView birthdayTv;
	private TextView xzTv;
	private TextView hobbyTv;
	private RelativeLayout manRelativeLayout;
	private RelativeLayout womanRelativeLayout;
	private ImageView manIv;
	private ImageView womanIv;
	private TextView manTv;
	private TextView womanTv;
	private Handler mHandler;
	private TextView topTv;
	private ImageView goBackIv;
	private Button topBtn;
    private static final String title="详细信息";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_detail_info_page);
		context = this;
		gson = new Gson();
		mHandler=new Handler(this);
		mQueue = Volley.newRequestQueue(context);
		Intent intent = this.getIntent();
		uid = intent.getIntExtra("uid", -1);
		initView();
		setPersonDetailInfo();
	}

	private void setView() {
		changeGenderView(mPersonDetailInfo.getSex());
		userNameTv.setText(mPersonDetailInfo.getNicName());
		personsignTv.setText(mPersonDetailInfo.getPsnsig());
		hobbyTv.setText(mPersonDetailInfo.getHobby());
		hometownTv.setText(mPersonDetailInfo.getHometown());
		birthdayTv.setText(mPersonDetailInfo.getBirth());
		xzTv.setText(mPersonDetailInfo.getConstellation());
		schoolTv.setText(mPersonDetailInfo.getSchool());
		departmentTv.setText(mPersonDetailInfo.getDepartment());
		enrollTimeTv.setText(mPersonDetailInfo.getEnrolltime());
		phoneTv.setText(mPersonDetailInfo.getTel());
		qqTv.setText(mPersonDetailInfo.getQq());
	}

	private void initView() {
		topBtn=(Button)findViewById(R.id.listlayout_top_btn);
		topBtn.setVisibility(View.GONE);
		topTv=(TextView)findViewById(R.id.listlayout_top_text);
		topTv.setText(title);
		goBackIv=(ImageView)findViewById(R.id.listlayout_top_back);
		goBackIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		manRelativeLayout=(RelativeLayout)findViewById(R.id.person_detail_info_page_gender_mlayout);
		womanRelativeLayout=(RelativeLayout)findViewById(R.id.person_detail_info_page_gender_wlayout);
		manIv=(ImageView)findViewById(R.id.person_detail_info_page_man_Iv);
		manTv=(TextView)findViewById(R.id.person_detail_info_page_man_tv);
		womanIv=(ImageView)findViewById(R.id.person_detail_info_page_woman_Iv);
		womanTv=(TextView)findViewById(R.id.person_detail_info_pagea_woman_tv);
		userNameTv = (TextView) findViewById(R.id.person_detail_info_page_tv_nicNmae);
		personsignTv = (TextView) findViewById(R.id.person_detail_info_page_tv_psnsig);
		hobbyTv = (TextView) findViewById(R.id.person_detail_info_page_tv_hobby);
		hometownTv = (TextView) findViewById(R.id.person_detail_info_page_tv_hometown);
		birthdayTv = (TextView) findViewById(R.id.person_detail_info_page_tv_birth);
		xzTv = (TextView) findViewById(R.id.person_detail_info_page_tv_constellation);
		schoolTv = (TextView) findViewById(R.id.person_detail_info_page_tv_school);
		departmentTv = (TextView) findViewById(R.id.person_detail_info_page_tv_department);
		enrollTimeTv = (TextView) findViewById(R.id.person_detail_info_page_tv_enrolledtime);
		phoneTv = (TextView) findViewById(R.id.person_detail_info_page_tv_tel);
		qqTv = (TextView) findViewById(R.id.person_detail_info_page_tv_qq);

	}

	private void setPersonDetailInfo() {
		url = ShareData.SEEVER_BASE_URL + "getPersonDetailInfo.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("uid", String.valueOf(uid));// new
														// JSONObject(appendHeader)
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						System.out.println(response.toString());
						try {
							String flag = response.getString("flag");
							if (flag.equals("ok")) {
								mPersonDetailInfo = gson.fromJson(
										response.getString("personDetailInfo"),
										new TypeToken<PersonDetailInfo>() {
										}.getType());
								Message msg=new Message();
								msg.what=INFO_COME;
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
						System.out.println("myJSReq--json receive failed!");
						Toast.makeText(context, "连接服务器失败！", Toast.LENGTH_SHORT)
								.show();
					}
				});
		mQueue.add(jsonObjectRequest);
	}
	private void backToCommonView() {
		manRelativeLayout.setBackgroundColor(getResources().getColor(
				R.color.white));
		womanRelativeLayout.setBackgroundColor(getResources().getColor(
				R.color.white));
		manIv.setImageDrawable(getResources().getDrawable(R.drawable.man0));
		manTv.setTextColor(getResources().getColor(R.color.gray));
		womanIv.setImageDrawable(getResources().getDrawable(R.drawable.girl0));
		womanTv.setTextColor(getResources().getColor(R.color.gray));
	}

	private void changeGenderView(int tag) {
		switch (tag) {
		case 1:
			backToCommonView();
			manRelativeLayout.setBackgroundColor(getResources().getColor(
					R.color.yellow));
			manIv.setImageDrawable(getResources().getDrawable(R.drawable.man1));
			manTv.setTextColor(getResources().getColor(R.color.white));
			break;
		case 0:
			backToCommonView();
			womanRelativeLayout.setBackgroundColor(getResources().getColor(
					R.color.yellow));
			womanIv.setImageDrawable(getResources()
					.getDrawable(R.drawable.girl1));
			womanTv.setTextColor(getResources().getColor(R.color.white));
			break;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==INFO_COME){
			setView();
		}
		return false;
	}
}
