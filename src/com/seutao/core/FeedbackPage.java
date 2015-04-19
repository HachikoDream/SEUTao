package com.seutao.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.seutao.entity.CollectedGood;
import com.seutao.sharedata.ShareData;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackPage extends Activity {

	private Button btn_submit=null;
	private EditText et_info=null;
	private RequestQueue mQueue = null;
	private String url=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback_page);
		mQueue=Volley.newRequestQueue(this);
		btn_submit=(Button)findViewById(R.id.feedback_page_btn_submit);
		et_info=(EditText)findViewById(R.id.feedback_page_et_info);
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String info=et_info.getText().toString();
				if (info.equals("")) {
					Toast.makeText( FeedbackPage.this,"反馈信息不能为空哦~", Toast.LENGTH_LONG).show();
				}
				else {
					feedbackInfo(info);
				}
			}
		});
	}


	private void feedbackInfo (String info) {
		url=ShareData.SEEVER_BASE_URL+"feedbackInfo.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("info", info);
		appendHeader.put("uid", String.valueOf(ShareData.MyId));
		JsonObjectRequest mJsonObjectRequest=new JsonObjectRequest(url, new JSONObject(appendHeader), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				System.out.println(response.toString());
				try {
					String flag=response.getString("flag");
					if (flag.equals("ok")) {
						Toast.makeText(FeedbackPage.this, "反馈成功！", Toast.LENGTH_SHORT).show();
						finish();
					}
					else
						Toast.makeText(FeedbackPage.this, "反馈失败！", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(FeedbackPage.this, "连接服务器失败！", Toast.LENGTH_SHORT).show();
			}
		});
		mQueue.add(mJsonObjectRequest); 
	}

}
