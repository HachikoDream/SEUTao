package com.seutao.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seutao.entity.PersonDetailInfo;
import com.seutao.sharedata.ShareData;

public class PersonEditInfoPage extends Activity {
	private Context context = null;
	private EditText et_nicName = null;
	private EditText et_psnsig = null;
	private TextView tv_hobby = null;
	private ImageView addhobbyIv = null;
	private TextView tv_hometown = null;
	private TextView et_birth = null;
	private EditText et_constellation = null;
	private EditText et_school = null;
	private EditText et_department = null;
	private TextView et_enrolledtime = null;
	private EditText et_tel = null;
	private EditText et_qq = null;
	private PersonDetailInfo mPersonDetailInfo = null;
	private int sex = 0;
	private String birth = null;
	private String enrolledtime = null;
	private Gson gson = null;
	private RequestQueue mQueue = null;
	private String url = null;
	private ImageView goBackIv;
	private Button topBtn;
	private TextView topTitleTv;
	private RelativeLayout userGenderManRv;
	private RelativeLayout userGenderGirlRv;
	private ImageView manIv, girlIv;
	private TextView manTv, girlTv;
	private final static String title = "编辑信息";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_edit_info_page);
		context = this;
		gson = new Gson();
		mQueue = Volley.newRequestQueue(context);
		goBackIv = (ImageView) findViewById(R.id.listlayout_top_back);
		topBtn = (Button) findViewById(R.id.listlayout_top_btn);
		topTitleTv = (TextView) findViewById(R.id.listlayout_top_text);
		topTitleTv.setText(title);
		topBtn.setText("保存");
		tv_hobby = (TextView) findViewById(R.id.person_edit_info_page_tv_hobby);
		addhobbyIv = (ImageView) findViewById(R.id.person_edit_info_page_iv_hobby);
		userGenderGirlRv = (RelativeLayout) findViewById(R.id.person_edit_info_page_gender_wlayout);
		userGenderManRv = (RelativeLayout) findViewById(R.id.person_edit_info_page_gender_mlayout);
		userGenderGirlRv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sex = 0;
				changeGenderView(sex);
			}
		});
		userGenderManRv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sex = 1;
				changeGenderView(sex);
			}
		});
		manIv = (ImageView) findViewById(R.id.person_edit_info_page_man_Iv);
		girlIv = (ImageView) findViewById(R.id.person_edit_info_page_woman_Iv);
		manTv = (TextView) findViewById(R.id.person_edit_info_page_man_tv);
		girlTv = (TextView) findViewById(R.id.person_edit_info_pagea_woman_tv);
		goBackIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Activity) context).finish();
			}
		});
		et_nicName = ((EditText) findViewById(R.id.person_edit_info_page_et_nicName));
		et_psnsig = ((EditText) findViewById(R.id.person_edit_info_page_et_psnsig));
		tv_hometown = ((TextView) findViewById(R.id.person_edit_info_page_tv_hometown));
		et_birth = ((TextView) findViewById(R.id.person_edit_info_page_tv_birth));
		et_constellation = ((EditText) findViewById(R.id.person_edit_info_page_et_constellation));
		et_school = ((EditText) findViewById(R.id.person_edit_info_page_et_school));
		et_department = ((EditText) findViewById(R.id.person_edit_info_page_et_department));
		et_enrolledtime = ((TextView) findViewById(R.id.person_edit_info_page_tv_enrolledtime));
		et_tel = ((EditText) findViewById(R.id.person_edit_info_page_et_tel));
		et_qq = ((EditText) findViewById(R.id.person_edit_info_page_et_qq));
		tv_hometown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(PersonEditInfoPage.this,
						AddressActivity.class);
				startActivityForResult(i, 1);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			}
		});
		et_birth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				Calendar c = Calendar.getInstance();
				// 直接创建一个DatePickerDialog对话框实例，并将它显示出来
				Dialog dateDialog = new DatePickerDialog(
						PersonEditInfoPage.this,
						// 绑定监听器
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker dp, int year,
									int month, int dayOfMonth) {
								// str.append(year + "-" + (month + 1) + "-"+
								// dayOfMonth + " ");
								birth = year + "-" + (++month) + "-"
										+ dayOfMonth + " ";
								et_birth.setText(birth);
								switch (month) {
								case 1:
									if (dayOfMonth < 20)
										et_constellation.setText("摩羯座");
									else
										et_constellation.setText("水瓶座");
									break;
								case 2:
									if (dayOfMonth < 19)
										et_constellation.setText("水瓶座");
									else
										et_constellation.setText("双鱼座");
									break;
								case 3:
									if (dayOfMonth < 21)
										et_constellation.setText("双鱼座");
									else
										et_constellation.setText("白羊座");
									break;
								case 4:
									if (dayOfMonth < 20)
										et_constellation.setText("白羊座");
									else
										et_constellation.setText("金牛座");
									break;
								case 5:
									if (dayOfMonth < 21)
										et_constellation.setText("金牛座");
									else
										et_constellation.setText("双子座");
									break;
								case 6:
									if (dayOfMonth < 22)
										et_constellation.setText("双子座");
									else
										et_constellation.setText("巨蟹座");
									break;
								case 7:
									if (dayOfMonth < 23)
										et_constellation.setText("巨蟹座");
									else
										et_constellation.setText("狮子座");
									break;
								case 8:
									if (dayOfMonth < 23)
										et_constellation.setText("狮子座");
									else
										et_constellation.setText("处女座");
									break;
								case 9:
									if (dayOfMonth < 23)
										et_constellation.setText("处女座");
									else
										et_constellation.setText("天秤座");
									break;
								case 10:
									if (dayOfMonth < 24)
										et_constellation.setText("天秤座");
									else
										et_constellation.setText("天蝎座");
									break;
								case 11:
									if (dayOfMonth < 23)
										et_constellation.setText("天蝎座");
									else
										et_constellation.setText("射手座");
									break;
								case 12:
									if (dayOfMonth < 22)
										et_constellation.setText("射手座");
									else
										et_constellation.setText("摩羯座");
									break;

								default:
									break;
								}
							}
						}
						// 设置初始日期
						, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH));
				dateDialog.setTitle("请选择日期");
				dateDialog.show();
			}
		});
		et_enrolledtime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				Calendar c = Calendar.getInstance();
				// 直接创建一个DatePickerDialog对话框实例，并将它显示出来
				Dialog dateDialog = new DatePickerDialog(
						PersonEditInfoPage.this,
						// 绑定监听器
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker dp, int year,
									int month, int dayOfMonth) {
								enrolledtime = year + "-" + (month + 1) + "-"
										+ dayOfMonth + " ";
								et_enrolledtime.setText("");
								et_enrolledtime.setText(enrolledtime);
							}
						}
						// 设置初始日期
						, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH));
				dateDialog.setTitle("请选择日期");
				dateDialog.show();
			}
		});

		topBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et_nicName.getText().toString().equals(""))
					Toast.makeText(context, "昵称不能为空哦~", Toast.LENGTH_LONG)
							.show();
				else if (et_tel.getText().toString().equals(""))
					Toast.makeText(context, "电话作为联系交易人工具为必填项~",
							Toast.LENGTH_LONG).show();
				else {
					List<HashMap<String, String>> updateList = new ArrayList<HashMap<String, String>>();
					HashMap<String, String> map;
					String newinfo = "";
					int modify = 0;
					if (!et_nicName.getText().toString()
							.equals(mPersonDetailInfo.getNicName())) {
						modify = 1;
						newinfo = et_nicName.getText().toString();
						map = new HashMap<String, String>();
						map.put("flag", String
								.valueOf(ShareData.PersonDetailInfo_nicName));
						map.put("newinfo", newinfo);
						updateList.add(map);
						mPersonDetailInfo.setNicName(newinfo);
						ShareData.mMyPersonInfo.setNicName(newinfo);
					}
					if (!et_psnsig.getText().toString()
							.equals(mPersonDetailInfo.getPsnsig())) {
						modify = 1;
						newinfo = et_psnsig.getText().toString();
						map = new HashMap<String, String>();
						map.put("flag", String
								.valueOf(ShareData.PersonDetailInfo_psnsig));
						map.put("newinfo", newinfo);
						updateList.add(map);
						mPersonDetailInfo.setPsnsig(newinfo);
						ShareData.mMyPersonInfo.setPsnsig(newinfo);
					}
					if (!tv_hometown.getText().toString()
							.equals(mPersonDetailInfo.getHometown())) {
						newinfo = tv_hometown.getText().toString();
						String[] home = newinfo.split("-");
						map = null;
						if (!home[0].equals(mPersonDetailInfo
								.getHometown_province())) {
							modify = 1;
							map = new HashMap<String, String>();
							map.put("flag",
									String.valueOf(ShareData.PersonDetailInfo_hometown_province));
							map.put("newinfo", home[0]);
							updateList.add(map);
							mPersonDetailInfo.setHometown_province(home[0]);
						}
						if (!home[1].equals(mPersonDetailInfo
								.getHometown_city())) {
							modify = 1;
							map = new HashMap<String, String>();
							map.put("flag",
									String.valueOf(ShareData.PersonDetailInfo_hometown_city));
							map.put("newinfo", home[1]);
							updateList.add(map);
							mPersonDetailInfo.setHometown_city(home[1]);
						}
						if (!home[2].equals(mPersonDetailInfo
								.getHometown_area())) {
							modify = 1;
							map = new HashMap<String, String>();
							map.put("flag",
									String.valueOf(ShareData.PersonDetailInfo_hometown_area));
							map.put("newinfo", home[2]);
							updateList.add(map);
							mPersonDetailInfo.setHometown_area(home[2]);
						}
					}
					if (!et_birth.getText().toString()
							.equals(mPersonDetailInfo.getBirth())) {
						modify = 1;
						newinfo = et_birth.getText().toString();
						map = new HashMap<String, String>();
						map.put("flag", String
								.valueOf(ShareData.PersonDetailInfo_birth));
						map.put("newinfo", newinfo);
						updateList.add(map);
						mPersonDetailInfo.setBirth(newinfo);
					}
					if (!et_constellation.getText().toString()
							.equals(mPersonDetailInfo.getConstellation())) {
						modify = 1;
						newinfo = et_constellation.getText().toString();
						map = new HashMap<String, String>();
						map.put("flag",
								String.valueOf(ShareData.PersonDetailInfo_constellation));
						map.put("newinfo", newinfo);
						updateList.add(map);
						mPersonDetailInfo.setConstellation(newinfo);
					}
					if (!et_school.getText().toString()
							.equals(mPersonDetailInfo.getSchool())) {
						modify = 1;
						newinfo = et_school.getText().toString();
						map = new HashMap<String, String>();
						map.put("flag", String
								.valueOf(ShareData.PersonDetailInfo_school));
						map.put("newinfo", newinfo);
						updateList.add(map);
						mPersonDetailInfo.setSchool(newinfo);
					}
					if (!tv_hobby.getText().toString()
							.equalsIgnoreCase(mPersonDetailInfo.getHobby())) {
						modify = 1;
						newinfo = tv_hobby.getText().toString();
						map = new HashMap<String, String>();
						map.put("flag", String
								.valueOf(ShareData.PersonDetailInfo_hobby));
						map.put("newinfo", newinfo);
						updateList.add(map);
						mPersonDetailInfo.setHobby(newinfo);
					}
					if (!et_department.getText().toString()
							.equals(mPersonDetailInfo.getDepartment())) {
						modify = 1;
						newinfo = et_department.getText().toString();
						map = new HashMap<String, String>();
						map.put("flag", String
								.valueOf(ShareData.PersonDetailInfo_department));
						map.put("newinfo", newinfo);
						updateList.add(map);
						mPersonDetailInfo.setDepartment(newinfo);
					}
					if (!et_enrolledtime.getText().toString()
							.equals(mPersonDetailInfo.getEnrolltime())) {
						modify = 1;
						newinfo = et_enrolledtime.getText().toString();
						map = new HashMap<String, String>();
						map.put("flag", String
								.valueOf(ShareData.PersonDetailInfo_enrolltime));
						map.put("newinfo", newinfo);
						updateList.add(map);
						mPersonDetailInfo.setEnrolltime(newinfo);
					}
					if (!et_tel.getText().toString()
							.equals(mPersonDetailInfo.getTel())) {
						modify = 1;
						newinfo = et_tel.getText().toString();
						map = new HashMap<String, String>();
						map.put("flag",
								String.valueOf(ShareData.PersonDetailInfo_tel));
						map.put("newinfo", newinfo);
						updateList.add(map);
						mPersonDetailInfo.setTel(newinfo);
					}
					if (!et_qq.getText().toString()
							.equals(mPersonDetailInfo.getQq())) {
						modify = 1;
						newinfo = et_qq.getText().toString();
						map = new HashMap<String, String>();
						map.put("flag",
								String.valueOf(ShareData.PersonDetailInfo_qq));
						map.put("newinfo", newinfo);
						updateList.add(map);
						mPersonDetailInfo.setQq(newinfo);
					}
					switch (sex) {
					case ShareData.Sex_boy:
						if (mPersonDetailInfo.getSex() == ShareData.Sex_girl) {
							modify = 1;
							map = new HashMap<String, String>();
							map.put("flag", String
									.valueOf(ShareData.PersonDetailInfo_sex));
							map.put("newinfo",
									String.valueOf(ShareData.Sex_boy));
							updateList.add(map);
							mPersonDetailInfo.setSex(ShareData.Sex_boy);
						}
						break;
					case ShareData.Sex_girl:
						if (mPersonDetailInfo.getSex() == ShareData.Sex_boy) {
							modify = 1;
							map = new HashMap<String, String>();
							map.put("flag", String
									.valueOf(ShareData.PersonDetailInfo_sex));
							map.put("newinfo",
									String.valueOf(ShareData.Sex_girl));
							updateList.add(map);
							mPersonDetailInfo.setSex(ShareData.Sex_girl);
						}
						break;
					default:
						break;
					}
					if (modify == 1) {
						ShareData.mMyPersonDetailInfo = mPersonDetailInfo;
						updatePersonDetailInfo(updateList);
						Toast.makeText(context, "修改成功！", Toast.LENGTH_LONG)
								.show();
						((Activity) context).finish();
					} else {
						Toast.makeText(context, "当前信息并没有修改！", Toast.LENGTH_LONG)
								.show();
					}

				}
			}
		});

		addhobbyIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final HobbyDialog mHobbyDialog = new HobbyDialog(
						PersonEditInfoPage.this);

				mHobbyDialog.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mHobbyDialog.dismiss();
						Toast.makeText(PersonEditInfoPage.this, "被点到确定",
								Toast.LENGTH_LONG).show();
						tv_hobby.setText(mHobbyDialog.getHobbyLabelsString());
					}
				});

				mHobbyDialog.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mHobbyDialog.dismiss();
						Toast.makeText(PersonEditInfoPage.this, "被点到取消",
								Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		setPersonDetailInfo();

	}

	protected void onActivityResult(int resultCode, int re, Intent data) {
		String cityName;
		String provinceName;
		String districtName;
		if (data == null) {
			return;
		}
		cityName = data.getStringExtra("home_city");
		provinceName = data.getStringExtra("home_province");
		districtName = data.getStringExtra("home_district");
		tv_hometown.setText(provinceName + "-" + cityName + "-" + districtName);
	}

	@SuppressLint("NewApi")
	private void resetGenderView() {
		userGenderManRv.setBackground(getResources().getDrawable(
				R.drawable.linearlayoutbg));
		userGenderGirlRv.setBackground(getResources().getDrawable(
				R.drawable.linearlayoutbg));
		manIv.setImageDrawable(getResources().getDrawable(R.drawable.man0));
		manTv.setTextColor(getResources().getColor(R.color.gray));
		girlIv.setImageDrawable(getResources().getDrawable(R.drawable.girl0));
		girlTv.setTextColor(getResources().getColor(R.color.gray));
	}

	@SuppressLint("NewApi")
	private void changeGenderView(int tag) {
		switch (tag) {
		case 1:
			resetGenderView();
			userGenderManRv.setBackground(getResources().getDrawable(
					R.drawable.genderlayout));
			manIv.setImageDrawable(getResources().getDrawable(R.drawable.man1));
			manTv.setTextColor(getResources().getColor(R.color.white));
			break;
		case 0:
			resetGenderView();
			userGenderGirlRv.setBackground(getResources().getDrawable(
					R.drawable.genderlayout));
			girlIv.setImageDrawable(getResources()
					.getDrawable(R.drawable.girl1));
			girlTv.setTextColor(getResources().getColor(R.color.white));
			break;
		}
	}

	private void setPersonDetailInfo() {
		url = ShareData.SEEVER_BASE_URL + "getPersonDetailInfo.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("uid", String.valueOf(ShareData.MyId));
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
								ShareData.mMyPersonDetailInfo = mPersonDetailInfo;
								setView();
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

	private void updatePersonDetailInfo(List<HashMap<String, String>> updatelist) {
		url = ShareData.SEEVER_BASE_URL + "updatePersonDetailInfo.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("uid", String.valueOf(ShareData.MyId));
		appendHeader.put("updatelist", updatelist);
		JSONObject mJsonObject = null;
		try {
			mJsonObject = new JSONObject(gson.toJson(appendHeader));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
				mJsonObject, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						System.out.println(response.toString());
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

	public void setView() {
		et_nicName.setText(mPersonDetailInfo.getNicName());
		et_psnsig.setText(mPersonDetailInfo.getPsnsig());
		tv_hobby.setText(mPersonDetailInfo.getHobby());
		if (mPersonDetailInfo.getSex() == ShareData.Sex_boy) {
			sex = ShareData.Sex_boy;
		} else if (mPersonDetailInfo.getSex() == ShareData.Sex_girl) {
			sex = ShareData.Sex_girl;
		}
		changeGenderView(sex);
		tv_hometown.setText(mPersonDetailInfo.getHometown());
		et_birth.setText(mPersonDetailInfo.getBirth());
		et_constellation.setText(mPersonDetailInfo.getConstellation());
		et_school.setText(mPersonDetailInfo.getSchool());
		et_department.setText(mPersonDetailInfo.getDepartment());
		et_enrolledtime.setText(mPersonDetailInfo.getEnrolltime());
		et_tel.setText(mPersonDetailInfo.getTel());
		et_qq.setText(mPersonDetailInfo.getQq());
	}

	public void splitString(String[] a, String b) {
		String[] temp = new String[] { "" };
		temp = b.split("\\|");
		for (int i = 0; i < temp.length; i++) {
			a[i] = temp[i];
		}
	}
}
