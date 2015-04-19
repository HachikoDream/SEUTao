package com.seutao.core;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seutao.sharedata.ShareData;

public class HobbyDialog  implements Callback {
	private Context context;
	private android.app.AlertDialog ad;
	private Button btn_done=null;
	private Button btn_back=null;
	private LinearLayout labelLayout=null;
	private List<HobbyLabel> mHobbyLabels=new ArrayList<HobbyLabel>();
	private String []initLabels=null;
	private RequestQueue mQueue = null;
	private String url=null;
	private Handler mHandler;
	private final static int HOBBY_IS_READY=21; 
	public String []hobbyLabels;
	private Gson gson=null;
	public HobbyDialog(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		gson = new Gson();
		mQueue=Volley.newRequestQueue(context);
		mHandler=new Handler(this);
		ad=new android.app.AlertDialog.Builder(context).create();
		ad.show();
		//关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = ad.getWindow();
		window.setContentView(R.layout.dialog_select_hobby);
		btn_done=(Button)window.findViewById(R.id.dialog_select_hobby_btn_done);
		btn_back=(Button)window.findViewById(R.id.dialog_select_hobby_btn_back);
		labelLayout=(LinearLayout)window.findViewById(R.id.dialog_select_hobby_ll_vertical);
		if (ShareData.hobbies==null) {
			getHobbies();
		}
		else {
			hobbyLabels=ShareData.hobbies;
			initHobbyDialog();
		}
	}
	
	/**
	 * 设置按钮
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(String text,final View.OnClickListener listener)
	{
		btn_done.setOnClickListener(listener);
	}
	
	/**
	 * 设置按钮
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(String text,final View.OnClickListener listener)
	{
		btn_back.setOnClickListener(listener);
	}

	/**
	 * 关闭
	 */
	public void dismiss() {
		ad.dismiss();
	}
	
	/**
	 * 完成选择 提交
	 */
	
	public String getHobbyLabelsString(){
		String labels="";
		for (int i = 0; i < mHobbyLabels.size(); i++) {
			if (mHobbyLabels.get(i).getIsChecked()) {
				if (labels.equals("")) {
					labels=mHobbyLabels.get(i).getLabel()+"、";
				}
				else {
					labels=labels+mHobbyLabels.get(i).getLabel()+"、";
				}
			}
		}
		return labels;
	}
	

	public void getHobbies(){
		url=ShareData.SEEVER_BASE_URL+"getHobbies.json";
		JsonObjectRequest mJsonObjectRequest=new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				System.out.println(response.toString());
				try {
					String flag=response.getString("flag");
					if (flag.equals("ok")) {
						ShareData.hobbies= gson.fromJson(response.getString("hobbies"),  
						        new TypeToken<String[]>() {  
						        }.getType());
						hobbyLabels=ShareData.hobbies;
						Message msg=new Message();
						msg.what=HOBBY_IS_READY;
						mHandler.sendMessage(msg);
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
	}
	

	private void initHobbyDialog() {
		String hobby="";
		if(ShareData.mMyPersonDetailInfo!=null){
			hobby=ShareData.mMyPersonDetailInfo.getHobby();
		}
		initLabels=hobby.split("\\、");
		int column=3;
		LinearLayout ll=null;
		for (int i = 0; i < hobbyLabels.length; i++) {
			if (i%column==0) {
				ll=new LinearLayout(this.context);
				labelLayout.addView(ll);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			}
			boolean flag=false;
			for (int j = 0; j < initLabels.length; j++) {
				System.out.println(initLabels[j]);
				if (hobbyLabels[i].equals(initLabels[j])) {
					flag=true;
					break;
				}
			}
			HobbyLabel mHobbyLabel=new HobbyLabel(context,hobbyLabels[i],flag);
			mHobbyLabels.add(mHobbyLabel);
			ll.addView(mHobbyLabel.getLL());
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==HOBBY_IS_READY){
			initHobbyDialog();
		}
		return false;
	}

}