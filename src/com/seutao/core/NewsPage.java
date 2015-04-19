package com.seutao.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.seutao.adapter.CommentDialog;
import com.seutao.adapter.CommentDialog.OnSureClickListener;
import com.seutao.adapter.CommentListAdapter;
import com.seutao.sharedata.ShareData;
import com.seutao.volley.MyJsonArrayRequest;

public class NewsPage extends Activity {
	private PullToRefreshListView commentsList = null;
	private EditText news_page_comment_content = null;
	private Button news_page_comment = null;
	private CommentListAdapter adapter = null;
	private String good_url = ShareData.SEEVER_BASE_URL
			+ "getgoodcomments.json";
	private String want_url = ShareData.SEEVER_BASE_URL
			+ "getwantcomments.json";
	private String comment_url, url;
	private List<Map<String, Object>> comments = new ArrayList<Map<String, Object>>();
	private int pageKind = 0;
	private int gid = 0;
	private int g_uid = 0;
	private int w_uid = 0;
	private int uid = 0;
	private int wid = 0;
	private int id = 0;
	private int _uid = 0;
	private String id_key;
	private int touid = 0;
	private String tousername = null;
	private String comment = null;
	private ImageView goBackIv;
	private Button topBtn;
	private TextView topTv;
	private static final String title = "评论列表";
	private static boolean isReMsg=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_page);
		pageKind = getIntent().getIntExtra("pageKind", 0);
		gid = getIntent().getIntExtra("gid", 0);
		uid = getIntent().getIntExtra("uid", 0);
		wid = getIntent().getIntExtra("wid", 0);
		g_uid = getIntent().getIntExtra("g_uid", 0);
		w_uid = getIntent().getIntExtra("w_uid", 0);

		if (pageKind == 1) {
			comment_url = good_url;
			id = gid;
			_uid = g_uid;
			id_key = "gid";
			url = ShareData.SEEVER_BASE_URL + "commentgood.json";
		} else {
			comment_url = want_url;
			id = wid;
			_uid = w_uid;
			id_key = "wid";
			url = ShareData.SEEVER_BASE_URL + "commentwant.json";
		}
		touid = _uid;
		setData();
		topBtn = (Button) findViewById(R.id.alllayout_top_btn);
		topTv = (TextView) findViewById(R.id.allayout_top_text);
		goBackIv = (ImageView) findViewById(R.id.alllayout_top_back);
		topBtn.setVisibility(View.GONE);
		topTv.setText(title);
		commentsList = (PullToRefreshListView) findViewById(R.id.news_page_comment_list);
		commentsList
				.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		news_page_comment_content = (EditText) findViewById(R.id.news_page_comment_content);
		news_page_comment = (Button) findViewById(R.id.news_page_comment);

		adapter = new CommentListAdapter(getApplicationContext(), comments,
				_uid);
		commentsList.setAdapter(adapter);
		commentsList.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				setData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (comments.size() == 0) {
					setData();
				} else {
					getComment(
							ShareData.List_load,
							(Integer) comments.get(comments.size() - 1).get(
									"cid"));
				}
			}
		});

		commentsList
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						Toast.makeText(NewsPage.this, "没有更多评论！",
								Toast.LENGTH_LONG).show();
					}
				});

		commentsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long _id) {
				touid = Integer.parseInt(comments.get(position - 1).get("uid")
						.toString());
				tousername = comments.get(position - 1).get("uname").toString();
				if (touid != _uid) {
					OnSureClickListener listener = new OnSureClickListener() {

						@Override
						public void getText(String string) {
							comment = string;
						}
					};
					Dialog dialog = new CommentDialog(NewsPage.this,
							R.style.MyDialog, tousername,listener);
					Window window = dialog.getWindow();
					window.setGravity(Gravity.CENTER);
					dialog.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							if(isReMsg){
								isReMsg=false;
								comment(uid, id, touid, comment);	
							}
						}
					});
					dialog.show();
				} else {
					news_page_comment_content.setFocusable(true);
				}
			}
		});

		goBackIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		news_page_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				touid = _uid;
				comment(uid, id, touid, news_page_comment_content.getText()
						.toString());
				news_page_comment_content.setText("");
				touid = _uid;
			}
		});

	}
    public  static void setReMsg(boolean reMsg){
    	isReMsg=reMsg;
    }
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	private void setData() {
		getComment(ShareData.List_init, 0);
	}

	private void getComment(final int flag, int index) {
		RequestQueue mQueue = Volley.newRequestQueue(NewsPage.this);
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put(id_key, id + "");
		appendHeader.put("index", index + "");
		appendHeader.put("flag", flag + "");
		MyJsonArrayRequest myJAReq = new MyJsonArrayRequest(comment_url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						switch (flag) {
						case ShareData.List_init:
							comments.clear();
							for (int i = 0; i < response.length(); i++) {
								JSONObject t;
								try {
									t = (JSONObject) response.get(i);
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("cid", t.getInt("cid"));
									map.put("uid", t.getInt("uid"));
									map.put("uname", t.getString("uname"));
									map.put("touid", t.getInt("touid"));
									map.put("uimage", t.getString("uimage"));
									map.put("comment", t.getString("comment"));
									String date = ShareData.getTime(t
											.getLong("commentdate"));
									map.put("commentdate", date);
									map.put("tousername",
											t.getString("tousername"));
									comments.add(map);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							break;
						case ShareData.List_load:
							for (int i = 0; i < response.length(); i++) {
								JSONObject t;
								try {
									t = (JSONObject) response.get(i);
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("cid", t.getInt("cid"));
									map.put("uid", t.getInt("uid"));
									map.put("uname", t.getString("uname"));
									map.put("touid", t.getInt("touid"));
									map.put("uimage", t.getString("uimage"));
									map.put("comment", t.getString("comment"));
									String date = ShareData.getTime(t
											.getLong("commentdate"));
									map.put("commentdate", date);
									map.put("tousername",
											t.getString("tousername"));
									comments.add(map);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							break;
						}
						adapter.notifyDataSetChanged();
						commentsList.onRefreshComplete();
						System.out.println(response.toString());
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), "连接服务器失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQueue.add(myJAReq);
	}

	private void comment(int uid, int id, int touid, String comment) {
		RequestQueue mQueue = Volley.newRequestQueue(NewsPage.this);
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("uid", uid + "");
		appendHeader.put(id_key, id + "");
		appendHeader.put("touid", touid + "");
		appendHeader.put("comment", comment);
		MyJsonArrayRequest myJAReq = new MyJsonArrayRequest(url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						System.out.println(response.toString());
						JSONObject resultJson;
						try {
							resultJson = response.getJSONObject(0);
							String flag = resultJson.getString("flag");
							if (flag.equals("ok")) {
								if (comments.size() < ShareData.List_length) {
									getComment(ShareData.List_init, 0);
								}
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
						Toast.makeText(getApplicationContext(), "连接服务器失败！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQueue.add(myJAReq);
	}

}
