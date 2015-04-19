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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.seutao.sharedata.ShareData;

public class SelectMonthPage extends Activity {
	private RadioButton rb_month1=null;
	private RadioButton rb_month2=null;
	private RadioButton rb_month3=null;
	private RadioButton rb_month4=null;
	private RadioButton rb_month5=null;
	private RadioButton rb_month6=null;
	private Button btn_submit=null;
	private ImageView goBackIv;
	private TextView topTitleTv;
	private Button topBtn;
	private int selectMonth=1;
	private Context mContext;
	private String selectIds;
	private int numOfIds;
	private int currentIndex;
	private RequestQueue mQueue = null;
	private String url=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_month_page);
		mContext=this;
		mQueue=Volley.newRequestQueue(mContext);
		goBackIv=(ImageView)findViewById(R.id.listlayout_top_back);
		topTitleTv=(TextView)findViewById(R.id.listlayout_top_text);
		topBtn=(Button)findViewById(R.id.listlayout_top_btn);
		topBtn.setVisibility(View.GONE);
		topTitleTv.setText("选择延长时间");
		goBackIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("android.intent.action.PrivilegePage");
				startActivity(intent);
				((Activity)mContext).finish();
			}
		});
		rb_month1=(RadioButton)findViewById(R.id.select_month_page_rb_one);
		rb_month2=(RadioButton)findViewById(R.id.select_month_page_rb_two);
		rb_month3=(RadioButton)findViewById(R.id.select_month_page_rb_three);
		rb_month4=(RadioButton)findViewById(R.id.select_month_page_rb_four);
		rb_month5=(RadioButton)findViewById(R.id.select_month_page_rb_five);
		rb_month6=(RadioButton)findViewById(R.id.select_month_page_rb_six);
		rb_month1.setChecked(true);
		btn_submit=(Button)findViewById(R.id.select_month_page_btn_submit);
		Intent intent=this.getIntent();
		selectIds=intent.getStringExtra("selectIds");
		currentIndex=intent.getIntExtra("currentIndex", 2);
		String []ids=selectIds.split("\\|");
		numOfIds=ids.length;
		
		OnClickListener mClickListener=new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rb_month1.setChecked(false);
				rb_month2.setChecked(false);
				rb_month3.setChecked(false);
				rb_month4.setChecked(false);
				rb_month5.setChecked(false);
				rb_month6.setChecked(false);
				switch (v.getId()) {
				case R.id.select_month_page_rb_one:
					rb_month1.setChecked(true);
					selectMonth=1;
					break;
				case R.id.select_month_page_rb_two:
					rb_month2.setChecked(true);
					selectMonth=2;
					break;
				case R.id.select_month_page_rb_three:
					rb_month3.setChecked(true);
					selectMonth=3;
					break;
				case R.id.select_month_page_rb_four:
					rb_month4.setChecked(true);
					selectMonth=4;
					break;
				case R.id.select_month_page_rb_five:
					rb_month5.setChecked(true);
					selectMonth=5;
					break;
				case R.id.select_month_page_rb_six:
					rb_month6.setChecked(true);
					selectMonth=6;
					break;
				default:
					break;
				}
			}
		};
		rb_month1.setOnClickListener(mClickListener);
		rb_month2.setOnClickListener(mClickListener);
		rb_month3.setOnClickListener(mClickListener);
		rb_month4.setOnClickListener(mClickListener);
		rb_month5.setOnClickListener(mClickListener);
		rb_month6.setOnClickListener(mClickListener);
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int sum=selectMonth*numOfIds*10;
				AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
				   builder.setTitle(numOfIds+"件商品延长"+selectMonth+"个月，将扣除"+sum+"积分，您确认进行此操作吗？");
				   builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (ShareData.mMyPersonInfo.getPoint()>=sum) {
								//tv_delaytime.setText(String.valueOf(selectMonth)+"个月");
								ShareData.mMyPersonInfo.setPoint(ShareData.mMyPersonInfo.getPoint()-sum);
								switch (currentIndex) {
								case 0:
									delayGoodsEndTime(selectIds,selectMonth,ShareData.mMyPersonInfo.getPoint());
									break;
								case 1:
									delayNeedsEndTime(selectIds,selectMonth,ShareData.mMyPersonInfo.getPoint());
									break;
								default:
									break;
								}
								selectMonth=1;
							}
							else {
								Toast.makeText(mContext,"对不起，当前积分不足，设置失败！",Toast.LENGTH_LONG).show();
								selectMonth=1;
							}
							Intent intent=new Intent();
							//intent.putExtra("uid", ShareData.USER_ID);
							intent.setAction("android.intent.action.PrivilegePage");
							startActivity(intent);
							((Activity)mContext).finish();
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
	public void delayGoodsEndTime(String gids,int addtime,int score){
		url=ShareData.SEEVER_BASE_URL+"delayGoodsEndTime.json";
		Map<String, String> appendHeader = new HashMap<String, String>();  
		appendHeader.put("uid", String.valueOf(ShareData.MyId));
		appendHeader.put("gids", gids);
		appendHeader.put("addtime", String.valueOf(addtime));
		appendHeader.put("score", String.valueOf(score));
		final int point=score;
		JsonObjectRequest mJsonObjectRequest=new JsonObjectRequest(url, new JSONObject(appendHeader), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				System.out.println(response.toString());
				try {
					String flag=response.getString("flag");
					if (flag.equals("ok")) {
						ShareData.mMyPersonInfo.setPoint(point);
						Toast.makeText(mContext,"设置成功！您当前的积分为"+point,Toast.LENGTH_LONG).show();
					}
					else {
						Toast.makeText(mContext, flag, Toast.LENGTH_SHORT).show();
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
				Toast.makeText(mContext, "连接服务器失败！", Toast.LENGTH_SHORT).show();
			}
		});
		mQueue.add(mJsonObjectRequest); 
	}
	
	public void delayNeedsEndTime(String gids,int addtime,int score){
		System.out.println("ned!!!");
		url=ShareData.SEEVER_BASE_URL+"delayNeedsEndTime.json";
		Map<String, String> appendHeader = new HashMap<String, String>();  
		appendHeader.put("uid", String.valueOf(ShareData.MyId));
		appendHeader.put("nids", gids);
		appendHeader.put("addtime", String.valueOf(addtime));
		appendHeader.put("score", String.valueOf(score));
		final int point=score;
		JsonObjectRequest mJsonObjectRequest=new JsonObjectRequest(url, new JSONObject(appendHeader), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				System.out.println(response.toString());
				try {
					String flag=response.getString("flag");
					if (flag.equals("ok")) {
						ShareData.mMyPersonInfo.setPoint(point);
						Toast.makeText(mContext,"设置成功！您当前的积分为"+point,Toast.LENGTH_LONG).show();
					}
					else {
						Toast.makeText(mContext, flag, Toast.LENGTH_SHORT).show();
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
				Toast.makeText(mContext, "连接服务器失败！", Toast.LENGTH_SHORT).show();
			}
		});
		mQueue.add(mJsonObjectRequest); 
	}

}
