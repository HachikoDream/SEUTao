package com.seutao.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.utils.UIHandler;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.seutao.sharedata.ShareData;
import com.seutao.tools.Tools;

public class RegisterDetailInfo extends Activity implements Callback, TagAliasCallback {
	private ImageView userAvaterIv;//
	private RelativeLayout userGenderManRv;
	private RelativeLayout userGenderGirlRv;
	private ImageView manIv, girlIv;
	private TextView manTv, girlTv;
	private EditText userNameEv;
	private EditText userPhoneEv;
	private Button commitButton;
	private String ThirduserName="";
	private String gender="";
	private TextView topTv;
	private ImageView goBackIv;
	private Button topBtn;
	private String ThirduserAvaterUrl="";// 用户头像网络存储地址
	private String localAvaterUrl;// 本地头像存储地址
	private static final String PICTURE_NAME = "userIcon.jpg";
	private static final String topTitle = "填写注册信息";
	private String url;
	private int genderInt;
	private String userName="";
	private String userImage;
	private String phoneNum="";
	private String pwd;
	private int userId;
	private int type;
	private int sourcePage;
	private String fuid;
	private static final int COMEFROMTHIRD =22;
	private final int COMEFROMPHONE = 23;
	private final int REGISTER_SUCCESS = 24;
	private final int THIRD_REGISTER_SUCCESS=25;
	private Handler mHandler;
	private boolean avaterSelected = false;
	private RequestQueue mQue;
	/* 请求码 */
	public static final int LOAD_USER_ICON = 1;
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 3;
	private static final int INTENT_ACTION_CROPED = 4;
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	/* 组件 */
	private String[] items = new String[] { "选择本地图片", "拍照" };
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_detailinfo_page);
		mQue = Volley.newRequestQueue(this);
		mHandler=new Handler(this);
		Bundle b = this.getIntent().getExtras();
		sourcePage = b.getInt("SourcePage");
		if (sourcePage == COMEFROMPHONE) {
			phoneNum = b.getString("phoneNum");
			pwd = b.getString("pwd");
		} else {
			ThirduserName = b.getString("name");
			gender = b.getString("gender");
			ThirduserAvaterUrl = b.getString("profile_image_url");
			fuid=b.getString("fuid");
			type=b.getInt("source");
		}
		initView();
		initAvaterFile();
		if (!ThirduserAvaterUrl.isEmpty()) {
			avaterSelected = true;
			loadIcon(ThirduserAvaterUrl);
		}
	}

	private void initView() {

		topTv = (TextView) findViewById(R.id.allayout_top_text);
		topTv.setText(topTitle);
		topTv.setTextSize(20);
		goBackIv = (ImageView) findViewById(R.id.alllayout_top_back);
		goBackIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		topBtn = (Button) findViewById(R.id.alllayout_top_btn);
		topBtn.setVisibility(View.INVISIBLE);
		userAvaterIv = (ImageView) findViewById(R.id.iv_user_icon);
		userAvaterIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
		userGenderManRv = (RelativeLayout) findViewById(R.id.register_gender_mlayout);
		userGenderGirlRv = (RelativeLayout) findViewById(R.id.register_gender_wlayout);
		userGenderManRv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				genderInt = 1;
				changeGenderView(1);
			}
		});
		userGenderGirlRv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				genderInt = 0;
				changeGenderView(0);
			}
		});
		manIv = (ImageView) findViewById(R.id.register_man_Iv);
		manTv = (TextView) findViewById(R.id.register_man_tv);
		girlIv = (ImageView) findViewById(R.id.register_woman_Iv);
		girlTv = (TextView) findViewById(R.id.register_woman_tv);
		userNameEv = (EditText) findViewById(R.id.register_detailInfo_username);
		userPhoneEv = (EditText) findViewById(R.id.register_detailInfo_userphone);
		commitButton = (Button) findViewById(R.id.register_commiteinfo_bt);
		commitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userName = userNameEv.getText().toString();
				phoneNum = userPhoneEv.getText().toString();
				if (isValid()) {
                        userImage=Tools.getImageStr(localAvaterUrl);
        				System.out.println("userName: " + userName + "phoneNum:"
        						+ phoneNum + "userImage:" + userImage);
        				if(sourcePage==COMEFROMPHONE){
        					RegisterUser();
        				}else{
        					ThirdRegisterUser();
        				}
				}
			}
		});
		if (!phoneNum.isEmpty()) {
			userPhoneEv.setText(phoneNum);
		}
		if (!ThirduserName.isEmpty()) {
			userNameEv.setText(ThirduserName);
		}
		if (!gender.isEmpty()) {
			if (gender.equals("m")) {
				genderInt = 1;
				changeGenderView(1);
			} else {
				genderInt = 0;
				changeGenderView(0);
			}
		}

	}


	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle("设置头像")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // 设置文件类型
							intentFromGallery
									.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1:

							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (Tools.hasSdcard()) {

								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												IMAGE_FILE_NAME)));
							}

							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 360);
		intent.putExtra("outputY", 360);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, INTENT_ACTION_CROPED);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			File file = new File(localAvaterUrl);
			BufferedOutputStream bos;
			try {
				bos = new BufferedOutputStream(new FileOutputStream(file));
				photo.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				bos.flush();
				bos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Drawable drawable = new BitmapDrawable(getResources(), photo);
			userAvaterIv.setImageDrawable(drawable);
			avaterSelected=true;
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == IMAGE_REQUEST_CODE
				&& resultCode == Activity.RESULT_OK && null != data) {
			startPhotoZoom(data.getData());
		} else if (requestCode == CAMERA_REQUEST_CODE
				&& resultCode == Activity.RESULT_OK) {
			if (Tools.hasSdcard()) {
				File tempFile = new File(
						Environment.getExternalStorageDirectory()
								+ "/" +  IMAGE_FILE_NAME);
				startPhotoZoom(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(RegisterDetailInfo.this, "未找到存储卡，无法存储照片！",
						Toast.LENGTH_LONG).show();
			}

		} else if (requestCode == INTENT_ACTION_CROPED) {
			if (data != null) {
				getImageToView(data);
			}
		}
	}


	private boolean isValid() {
		if (userName.isEmpty()) {
			showTextByToast("请输入用户名！");
			return false;
		}
		if (phoneNum.isEmpty()) {
			showTextByToast("请输入您的手机号！");
			return false;
		}
		if (!avaterSelected) {
			showTextByToast("请选择您的头像！");
			return false;
		}
		return true;

	}

	private void showTextByToast(String txt) {
		Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
	}

	private void backToCommonView() {
		userGenderManRv.setBackgroundColor(getResources().getColor(
				R.color.white));
		userGenderGirlRv.setBackgroundColor(getResources().getColor(
				R.color.white));
		manIv.setImageDrawable(getResources().getDrawable(R.drawable.man0));
		manTv.setTextColor(getResources().getColor(R.color.gray));
		girlIv.setImageDrawable(getResources().getDrawable(R.drawable.girl0));
		girlTv.setTextColor(getResources().getColor(R.color.gray));
	}

	private void changeGenderView(int tag) {
		switch (tag) {
		case 1:
			backToCommonView();
			userGenderManRv.setBackgroundColor(getResources().getColor(
					R.color.yellow));
			manIv.setImageDrawable(getResources().getDrawable(R.drawable.man1));
			manTv.setTextColor(getResources().getColor(R.color.white));
			break;
		case 0:
			backToCommonView();
			userGenderGirlRv.setBackgroundColor(getResources().getColor(
					R.color.yellow));
			girlIv.setImageDrawable(getResources()
					.getDrawable(R.drawable.girl1));
			girlTv.setTextColor(getResources().getColor(R.color.white));
			break;
		}
	}

	private void initAvaterFile() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String thumPicture = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/" + getPackageName() + "/download";
			File pictureParent = new File(thumPicture);
			File pictureFile = new File(pictureParent, PICTURE_NAME);

			if (!pictureParent.exists()) {
				pictureParent.mkdirs();
			}
			try {
				if (!pictureFile.exists()) {
					pictureFile.createNewFile();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			localAvaterUrl = pictureFile.getAbsolutePath();
			System.out.println("picturePath ==>>" + localAvaterUrl);
		} else {
			System.out
					.println("change user icon ==>>" + "there is not sdcard!");
		}
	}

	// 加载用户头像
	private void loadIcon(final String avaterUrl) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL picUrl = new URL(avaterUrl);
					Bitmap userIcon = BitmapFactory.decodeStream(picUrl
							.openStream());
					FileOutputStream b = null;
					try {
						b = new FileOutputStream(localAvaterUrl);
						userIcon.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						try {
							b.flush();
							b.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					Message msg = new Message();
					msg.what = LOAD_USER_ICON;
					UIHandler.sendMessage(msg, RegisterDetailInfo.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void saveInfoInLocalOfPhoneLogin() {
		sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		Editor editer = sp.edit();
		editer.clear();
		editer.putInt("type", COMEFROMPHONE);
		editer.putString("phoneNum", phoneNum);
		editer.putString("pwd", Tools.MD5(pwd));
		editer.apply();
	}
	private void saveInfoInLocalOfThirdLogin() {
		sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		Editor editer = sp.edit();
		editer.clear();
		editer.putInt("type", COMEFROMTHIRD);
		editer.putInt("thirdType", type);
		editer.putString("fuid", fuid);
		editer.apply();
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == LOAD_USER_ICON) {
			userAvaterIv.setImageURI(Uri.parse(localAvaterUrl));
		}
		else if(msg.what==REGISTER_SUCCESS){
			saveInfoInLocalOfPhoneLogin();
			JPushInterface.setAlias(this, ShareData.MyId+"", this);
		
		}else if(msg.what==THIRD_REGISTER_SUCCESS){
			saveInfoInLocalOfThirdLogin();
			JPushInterface.setAlias(this, ShareData.MyId+"", this);
		}
		return false;
	}

	public void RegisterUser() {
		url = ShareData.SEEVER_BASE_URL + "Register.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("gender", genderInt+"");
		appendHeader.put("userName", userName);
		appendHeader.put("phoneNum", phoneNum);
		appendHeader.put("userImage", userImage);
		appendHeader.put("pwd", Tools.MD5(pwd));
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
								userId = response.getInt("userId");
								ShareData.MyId=userId;
								Message msg = new Message();
								msg.what = REGISTER_SUCCESS;
								mHandler.sendMessage(msg);
							} else {
								showTextByToast(flag);
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

	public void ThirdRegisterUser() {
		url = ShareData.SEEVER_BASE_URL + "ThirdRegister.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("type", type+"");
		appendHeader.put("gender", genderInt+"");
		appendHeader.put("userName", userName);
		appendHeader.put("phoneNum", phoneNum);
		appendHeader.put("userImage", userImage);
		appendHeader.put("fuid", fuid);
		appendHeader.put("thirdUserName", ThirduserName);
		appendHeader.put("thirdUserImage", ThirduserAvaterUrl);
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
								userId = response.getInt("userId");
								ShareData.MyId=userId;
								Message msg = new Message();
								msg.what = THIRD_REGISTER_SUCCESS;
								mHandler.sendMessage(msg);
							} else {
								showTextByToast(flag);
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

	@Override
	public void gotResult(int responseCode, String alias, Set<String> tags) {
		// TODO Auto-generated method stub
		if(responseCode==0){
			Intent intent=new Intent(RegisterDetailInfo.this,MainTabContainer.class);
			startActivity(intent);
			finish();
		}
		else{
			Toast.makeText(this, "请检查网络连接！", Toast.LENGTH_LONG).show();
		}
	}

}
