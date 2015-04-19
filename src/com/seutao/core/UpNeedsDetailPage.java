package com.seutao.core;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seutao.entity.upWantsDetail;
import com.seutao.sharedata.ShareData;
import com.seutao.view.CustomProgressDialog;

public class UpNeedsDetailPage extends Activity implements Callback {
	protected static final int PUBLISH_SUCCESS = 0;
	protected static final int COME_DATE = 1;
	private ImageView goBackIv;
	private TextView topTv;
	private Button topBtn;
	private Button commitButton;
	private TextView selectNeedsClassifyTv;
	private EditText needsTitleEt;
	private EditText needsDescipEv;
	private EditText needsPirceEv;
	private TextView CountOfDesTv;
	private String needsTitle, needsDescr, needsClassify;
	private String infoOfWrong;
	private String needsPrice;
	private String url;
	private RequestQueue mQue;
	private Handler mHandler;
	private String wid;
	private String source;
	protected upWantsDetail wantDetail;
	private Gson gson;
	private static final String title="填写商品详情";
	protected static final int UPDATE_SUCCESS = 2;
	private CustomProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_up_needs_page);
		mQue = Volley.newRequestQueue(this);
		mHandler = new Handler(this);
		gson = new Gson();
		Intent comeIntent = this.getIntent();
		source = comeIntent.getStringExtra("source");
		if (source.equals("edit")) {
			wid = comeIntent.getStringExtra("wid");
			getUpWantDetail();
		}
		goBackIv=(ImageView)findViewById(R.id.alllayout_top_back);
		goBackIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		topTv=(TextView)findViewById(R.id.allayout_top_text);
		topTv.setText(title);
		topBtn=(Button)findViewById(R.id.alllayout_top_btn);
		topBtn.setVisibility(View.GONE);
		commitButton = (Button) findViewById(R.id.up_needs_commit);
		selectNeedsClassifyTv = (TextView) findViewById(R.id.up_needs_selectClassify);
		needsTitleEt = (EditText) findViewById(R.id.up_needs_needsName);
		needsDescipEv = (EditText) findViewById(R.id.up_needs_needsDescription);
		needsPirceEv = (EditText) findViewById(R.id.up_needs_needsPrice);
		CountOfDesTv = (TextView) findViewById(R.id.up_needs_needsDecr_count);
		needsDescipEv.addTextChangedListener(new TextWatcher() {

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
				CountOfDesTv.setText(s.length()+"");
			}
		});
		selectNeedsClassifyTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Dialog chooseClassifyDia = new ChooseClassifyDialog(
						UpNeedsDetailPage.this, selectNeedsClassifyTv);
				chooseClassifyDia.setTitle("选择分类");
				chooseClassifyDia.show();
			}
		});
		commitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				needsTitle = needsTitleEt.getText().toString();
				needsDescr = needsDescipEv.getText().toString();
				needsClassify = selectNeedsClassifyTv.getText().toString();
				needsPrice = needsPirceEv.getText().toString();
				if (isInfoCorrect()) {
					Toast.makeText(UpNeedsDetailPage.this, "commit!",
							Toast.LENGTH_LONG).show();
					if(source.equals("edit")){
						UpdateWant();
					}else{
						ReleaseNeeds();
					}
				} else {
					Toast.makeText(UpNeedsDetailPage.this, infoOfWrong,
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private boolean isInfoCorrect() {
		if (needsTitle.isEmpty()) {
			infoOfWrong = "求购标题好像还没写哦~";
			return false;
		}
		if (needsDescr.length() < 10) {
			infoOfWrong = "求购描述好像还不够10个字哦~";
			return false;
		}
		if (needsClassify.equals("选择分类")) {
			infoOfWrong = "请为您的求购选择一个分类";
			return false;
		}
		if (needsPrice.isEmpty()) {
			infoOfWrong = "请给您的求购一个合适的期望价格";
			return false;
		}
		return true;
	}

	public void ReleaseNeeds() {
		startProgressDialog();
		url = ShareData.SEEVER_BASE_URL + "ReleaseWants.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("userId", ShareData.MyId+"");
		appendHeader.put("wantTitle", needsTitle);
		appendHeader.put("wantprice", needsPrice);
		appendHeader.put("wantcontent", needsDescr);
		appendHeader.put("wantkind", ShareData.getClassifyId(needsClassify)
				+ "");
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
								msg.arg1=response.getInt("wid");
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

	public void getUpWantDetail() {
		startProgressDialog();
		url = ShareData.SEEVER_BASE_URL + "getUpWantDetail.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("wid", wid);
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
								wantDetail = gson.fromJson(
										response.getString("upWantsDetail"),
										new TypeToken<upWantsDetail>() {
										}.getType());
								Message msg = new Message();
								msg.what = COME_DATE;
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

	public void UpdateWant() {
		startProgressDialog();
		url = ShareData.SEEVER_BASE_URL + "updateWantInfo.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("wantPrice", needsPrice);
		appendHeader.put("wantTitle", needsTitle);
		appendHeader.put("wantContent", needsDescr);
		appendHeader.put("wantKind", ShareData.getClassifyId(needsClassify)
				+ "");
		appendHeader.put("wid", wid);
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

	private void setView() {
		selectNeedsClassifyTv.setText(wantDetail.getWantClassify());
		needsDescipEv.setText(wantDetail.getWantContent());
		needsPirceEv.setText(wantDetail.getWantPrice() + "");
		needsTitleEt.setText(wantDetail.getWantTitle());
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == PUBLISH_SUCCESS) {
			stopProgressDialog();
			int wid = msg.arg1;
			Intent intent = new Intent(UpNeedsDetailPage.this,
					PublishedSuccessPage.class);
			intent.putExtra("wid", wid);
			intent.putExtra("source", 2);
			startActivity(intent);
			finish();
		} else if (msg.what == COME_DATE) {
			stopProgressDialog();
			setView();
		}else if (msg.what==UPDATE_SUCCESS){
			stopProgressDialog();
			Toast.makeText(getApplicationContext(), "成功修改求购信息", Toast.LENGTH_LONG).show();
			finish();
		}
		return false;
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


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stopProgressDialog();
		super.onDestroy();
	}

}
