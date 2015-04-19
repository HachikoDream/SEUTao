package com.seutao.core;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seutao.entity.upGoodsDetail;
import com.seutao.sharedata.ShareData;
import com.seutao.tools.Tools;
import com.seutao.view.CustomProgressDialog;

public class upGoodsDetailPage extends Activity implements Callback {
	protected static final int PUBLISH_SUCCESS = 0;
	protected static final int DATA_CHANGE = 1;
	protected static final int UPDATE_SUCCESS = 2;
	private TextView selectClassifyTv;
	private TextView selectBoughtTimeTv;
	private Button confirmButton;
	private Spinner selectSchoolPlace;
	private Spinner selectOldDegree;
	private EditText goodsNameEt;
	private EditText goodsPriceEt;
	private EditText goodsDescriptionEt;
	private TextView CountOfDescripWordsTv;
	private String[] schoolPart = { "九龙湖校区", "丁家桥校区", "四牌楼校区", "浦口校区" };
	private String[] oldDegree = { "全新", "九成新", "八成新", "七成新", "六成新", "五成以下" };
	private ArrayAdapter<String> schoolAdapter = null;
	private ArrayAdapter<String> oldAdapter = null;
	private String InfoOfWrong = null;
	private String goodsName;
	private String goodsPrice;
	private String goodsDescr;
	private String goodsClassify;
	private String goodsPlace;
	private String goodsImage;
	private String goodsOldState;
	public String goodsBoughtTime;
	public String url = null;
	private Handler mHandler;
	private RequestQueue mQue;
	private String source;
	private int gid;
	private upGoodsDetail goodsDetail;
	private Gson gson;
	private CustomProgressDialog progressDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_up_goods_page_two);
		mHandler = new Handler(this);
		mQue = Volley.newRequestQueue(this);
		gson = new Gson();
		selectClassifyTv = (TextView) findViewById(R.id.up_goods_selectClassify);
		selectBoughtTimeTv = (TextView) findViewById(R.id.up_goods_boughtTime);
		confirmButton = (Button) findViewById(R.id.up_goods_commit);
		selectSchoolPlace = (Spinner) findViewById(R.id.up_goods_goodsSchoolAddress);
		selectOldDegree = (Spinner) findViewById(R.id.up_goods_goodsOldDegree);
		goodsNameEt = (EditText) findViewById(R.id.up_goods_goodsName);
		goodsPriceEt = (EditText) findViewById(R.id.up_goods_price);
		goodsDescriptionEt = (EditText) findViewById(R.id.up_goods_goodsDescription);
		CountOfDescripWordsTv = (TextView) findViewById(R.id.up_goods_goodsDecr_count);
		setSpinner();
		Bundle b = this.getIntent().getExtras();
		String photoPath = b.getString("localImagePath");
		goodsImage = Tools.getImageStr(photoPath);
		source = b.getString("source");
		if (source.equals("edit")) {
			gid = b.getInt("gid");
			getUpGoodsDetail();
		}
		selectClassifyTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Dialog chooseDialog = new ChooseClassifyDialog(
						upGoodsDetailPage.this, selectClassifyTv);
				String dialogTitle = "选择分类：";
				chooseDialog.setTitle(dialogTitle);
				chooseDialog.show();
			}
		});
		selectBoughtTimeTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder builder = new AlertDialog.Builder(
						upGoodsDetailPage.this);
				View dialogView = View.inflate(upGoodsDetailPage.this,
						R.layout.up_goods_page_two_selecttime_layout, null);
				final DatePicker datePicker = (DatePicker) dialogView
						.findViewById(R.id.up_goods_page_two_DatePicker);
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				datePicker.init(cal.get(Calendar.YEAR),
						cal.get(Calendar.MONTH),
						cal.get(Calendar.DAY_OF_MONTH),
						new OnDateChangedListener() {

							public void onDateChanged(DatePicker view,
									int year, int monthOfYear, int dayOfMonth) {
							}

						});
				builder.setView(dialogView);
				builder.setTitle("选择购买时间");
				builder.setPositiveButton("确 认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								StringBuffer sb = new StringBuffer();
								sb.append(String.format("%d-%02d-%02d",
										datePicker.getYear(),
										datePicker.getMonth() + 1,
										datePicker.getDayOfMonth()));
								goodsBoughtTime = sb.toString();
								selectBoughtTimeTv.setText(goodsBoughtTime);
								System.out.println(goodsBoughtTime);
								dialog.cancel();
							}
						});
				builder.create().show();
			}
		});
		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goodsName = goodsNameEt.getText().toString();
				goodsPrice = goodsPriceEt.getText().toString();
				goodsDescr = goodsDescriptionEt.getText().toString();
				goodsClassify = selectClassifyTv.getText().toString();
				goodsPlace = selectSchoolPlace.getSelectedItem().toString();
				goodsOldState = selectOldDegree.getSelectedItem().toString();
				goodsBoughtTime = selectBoughtTimeTv.getText().toString();
				if (isInfoCorrect()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							upGoodsDetailPage.this);
					View commitDialogView = View.inflate(
							upGoodsDetailPage.this,
							R.layout.up_goods_page_two_commit_dialog_layout,
							null);
					TextView goodsNameTv = (TextView) commitDialogView
							.findViewById(R.id.up_goods_commit_goodsName);
					TextView goodsPriceTv = (TextView) commitDialogView
							.findViewById(R.id.up_goods_commit_price);
					TextView goodsClassifyTv = (TextView) commitDialogView
							.findViewById(R.id.up_goods_commit_selectClassify);
					TextView goodsBoughtTimeTv = (TextView) commitDialogView
							.findViewById(R.id.up_goods_commit_boughtTime);
					TextView goodsPlaceTv = (TextView) commitDialogView
							.findViewById(R.id.up_goods_commit_goodsSchoolAddress);
					TextView goodsDesTv = (TextView) commitDialogView
							.findViewById(R.id.up_goods_commit_goodsDescription);
					TextView goodsOldTv = (TextView) commitDialogView
							.findViewById(R.id.up_goods_commit_oldstate);
					goodsNameTv.setText(goodsName);
					goodsPriceTv.setText(goodsPrice);
					goodsDesTv.setText(goodsDescr);
					goodsClassifyTv.setText(goodsClassify);
					goodsBoughtTimeTv.setText(goodsBoughtTime + "");
					goodsPlaceTv.setText(goodsPlace);
					goodsOldTv.setText(goodsOldState);
					builder.setView(commitDialogView);
					builder.setTitle("确认商品信息");
					builder.setPositiveButton("确认提交",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									startProgressDialog();
									if (source.equals("edit")) {
										UpdateGoods();
									} else {
										ReleaseGoods();
									}

									dialog.cancel();
								}
							});
					builder.setNegativeButton("返回修改",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							});
					builder.create().show();
				} else {
					Toast.makeText(upGoodsDetailPage.this, InfoOfWrong,
							Toast.LENGTH_LONG).show();
				}
			}
		});
		goodsDescriptionEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				CountOfDescripWordsTv.setText("当前已输入：" + s.length() + " 字");
			}
		});
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
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

	private boolean isInfoCorrect() {
		if (goodsName.isEmpty()) {
			InfoOfWrong = "商品名称不能为空哦~";
			return false;
		}
		if (goodsPrice.isEmpty()) {
			InfoOfWrong = "商品价格好像还没填哦~";
			return false;
		}
		if (goodsDescr.length() < 10) {
			InfoOfWrong = "商品信息详情字数好像还不够哦~";
			return false;
		}
		if (goodsClassify.equals("选择分类")) {
			InfoOfWrong = "请给您的商品选择一个分类";
			return false;
		}
		return true;
	}

	private void setSpinner() {
		schoolAdapter = new ArrayAdapter<String>(upGoodsDetailPage.this,
				android.R.layout.simple_spinner_item, schoolPart);
		selectSchoolPlace.setAdapter(schoolAdapter);
		selectSchoolPlace.setSelection(0, true);
		oldAdapter = new ArrayAdapter<String>(upGoodsDetailPage.this,
				android.R.layout.simple_spinner_item, oldDegree);
		selectOldDegree.setAdapter(oldAdapter);
		selectOldDegree.setSelection(0, true);

	}

	public void ReleaseGoods() {
		url = ShareData.SEEVER_BASE_URL + "ReleaseGoods.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("userId", ShareData.MyId + "");
		appendHeader.put("goodsName", goodsName);
		appendHeader.put("goodsImage", goodsImage);
		appendHeader.put("goodsSchool", goodsPlace);
		appendHeader.put("goodsPrice", goodsPrice);
		appendHeader.put("goodsContent", goodsDescr);
		appendHeader.put("goodsOldState", goodsOldState);
		appendHeader.put("goodkind", ShareData.getClassifyId(goodsClassify)
				+ "");
		appendHeader.put("goodsBoughtTime", goodsBoughtTime);
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
								msg.what = PUBLISH_SUCCESS;
								msg.arg1 = response.getInt("gid");
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
						Toast.makeText(getBaseContext(), "连接服务器失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQue.add(mJsonObjectRequest);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == PUBLISH_SUCCESS) {
			stopProgressDialog();
			int gid = msg.arg1;
			Intent intent = new Intent(upGoodsDetailPage.this,
					PublishedSuccessPage.class);
			intent.putExtra("gid", gid);
			intent.putExtra("source", 1);
			startActivity(intent);
			finish();

		} else if (msg.what == DATA_CHANGE) {
			stopProgressDialog();
			selectClassifyTv.setText(goodsDetail.getClassifyName());
			selectBoughtTimeTv.setText(goodsDetail.getBoughtTime());
			goodsNameEt.setText(goodsDetail.getGoodsName());
			goodsPriceEt.setText(goodsDetail.getGoodsPrice() + "");
			goodsDescriptionEt.setText(goodsDetail.getGoodsDetail());
			selectSchoolPlace.setSelection(getSchoolPosition(goodsDetail
					.getSchoolPart()));
			selectOldDegree.setSelection(getOldStatePosition(goodsDetail
					.getOldDegree()));
		}else if(msg.what==UPDATE_SUCCESS){
			stopProgressDialog();
			Toast.makeText(getApplicationContext(), "商品信息修改成功", Toast.LENGTH_LONG).show();
			finish();
		}
		return false;
	}

	public void getUpGoodsDetail() {
		startProgressDialog();
		url = ShareData.SEEVER_BASE_URL + "getUpGoodsDetail.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("gid", gid + "");
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
								goodsDetail = gson.fromJson(
										response.getString("upGoodsDetail"),
										new TypeToken<upGoodsDetail>() {
										}.getType());
								Message msg = new Message();
								msg.what = DATA_CHANGE;
								mHandler.sendMessage(msg);
							} else {
								Toast.makeText(getApplicationContext(), flag,
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
						Toast.makeText(upGoodsDetailPage.this, "连接服务器失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQue.add(mJsonObjectRequest);

	}

	public void UpdateGoods() {
		url = ShareData.SEEVER_BASE_URL + "updateGoodsInfo.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("goodsName", goodsName);
		appendHeader.put("goodsImage", goodsImage);
		appendHeader.put("goodsSchool", goodsPlace);
		appendHeader.put("goodsPrice", goodsPrice);
		appendHeader.put("goodsContent", goodsDescr);
		appendHeader.put("goodsOldState", goodsOldState);
		appendHeader.put("goodkind", ShareData.getClassifyId(goodsClassify)
				+ "");
		appendHeader.put("goodsBoughtTime", goodsBoughtTime);
		appendHeader.put("gid", gid + "");
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
								Message msg = new Message();
								msg.what = UPDATE_SUCCESS;
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

	public int getSchoolPosition(String schoolName) {
		for (int i = 0; i < schoolPart.length; i++) {
			if (schoolPart[i].equals(schoolName)) {
				return i;
			}
		}
		return -1;
	}

	public int getOldStatePosition(String oldState) {
		for (int i = 0; i < schoolPart.length; i++) {
			if (oldDegree[i].equals(oldState)) {
				return i;
			}
		}
		return -1;
	}
}
