package com.seutao.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jauker.widget.BadgeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.seutao.entity.PersonInfo;
import com.seutao.entity.thirdPlatInfo;
import com.seutao.sharedata.ShareData;
import com.seutao.tools.Tools;

public class PersonInfoPage extends Activity implements Callback,
		PlatformActionListener {
	private int uid;
	private PersonInfo mPersonInfo = null;
	private TextView tv_piont = null, tv_nicName = null, tv_psnsig = null;
	private Context mContext = null;
	private String picturePath;
	private ImageView iv_head;
	private RelativeLayout signinLayout;
	private RelativeLayout collectedLayout;
	private RelativeLayout pubishedLayout;
	private RelativeLayout privilegeLayout;
	private Button btn_moreinfo;
	private RequestQueue mQueue;
	private String url = null;
	private Gson gson = null;
	private Button btn_setting;
	private Handler mHandler;
	private TextView publishTv;
	private TextView collectTv;
	private RelativeLayout renren_confirm_layout;
	private RelativeLayout qq_confirm_layout;
	private RelativeLayout weibo_confirm_layout;
	private TextView renrenTv;
	private TextView weiboTv;
	private TextView qqTv;
	private TextView renrenConfirmTv;
	private TextView weiboConfirmTv;
	private TextView qqConfirmTv;
	private ImageView msgIv;
	private TextView topTv;
	private DisplayImageOptions options; // 显示图片的设置
	private static final int LOGINBYRENREN = 2;
	private static final int LOGINBYWEIBO = 0;
	private static final int LOGINBYQQ = 1;
	private int source;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private static final int INTENT_ACTION_CROPED = 2;
	protected static final int CAMERA_REQUEST_CODE = 1;
	protected static final int IMAGE_REQUEST_CODE = 0;
	protected static final int AVATER_SUCCESS = 1;
	private static final int DATE_COMPLETE = 0;
	private String[] items = new String[] { "选择本地图片", "拍照" };
	protected static final String IMAGE_FILE_NAME = "userIcon.jpg";// 照相后的存放文件的名称
	private static final String PICTURE_NAME = "faceImage.jpg";// 从相册和照相机取出的相片裁剪后的存放名称
	protected static final int CONFIRM_SUCCESSS = 5;
	protected static final int FIRST_COMEIN = 2;
	protected static final int NOT_FIRST = 3;
	protected static final int CHANGE_SUCCESS = 4;
	protected static final int THIRD_INFO_COME = 6;
	private String headImage = "";
	private String thirdUserAvaterUrl;
	private String thirdFuid;
	private String thirdUserName;
	private int existuserId;
	private List<thirdPlatInfo> thirdInfoList;
	private boolean[] isThirdConnect;
	private static final String title="Ta的信息";
	protected static final int UNREAD_MSG_COME = 7;
	private int unreadCount=0;
	private BadgeView msgBadgeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_info_page);
		ExitApplication.getInstance().addActivity(this);
		mContext = this;
		gson = new Gson();
		isThirdConnect = new boolean[3];
		for (int i = 0; i < 3; i++) {
			isThirdConnect[i] = false;
		}
		mHandler = new Handler(this);
		ShareSDK.initSDK(PersonInfoPage.this);
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
				.build();
		mQueue = Volley.newRequestQueue(mContext);
		btn_setting = (Button) findViewById(R.id.person_info_page_btn_setting);
		pubishedLayout = (RelativeLayout) findViewById(R.id.person_info_page_btn_pubished);
		btn_moreinfo = (Button) findViewById(R.id.person_info_page_btn_moreinfo);
		privilegeLayout = (RelativeLayout) findViewById(R.id.person_info_page_btn_privilege);
		signinLayout = (RelativeLayout) findViewById(R.id.person_info_page_btn_signin);
		collectedLayout = (RelativeLayout) findViewById(R.id.person_info_page_btn_collected);
		iv_head = (ImageView) findViewById(R.id.person_info_page_iv_head);
		tv_nicName = (TextView) findViewById(R.id.person_info_page_tv_tvNicName);
		tv_piont = (TextView) findViewById(R.id.person_info_page_tv_piont);
		tv_psnsig = (TextView) findViewById(R.id.person_info_page_tv_psnsig);
		publishTv = (TextView) findViewById(R.id.person_info_page_publish_text);
		collectTv = (TextView) findViewById(R.id.person_info_page_collected_text);
		msgIv=(ImageView)findViewById(R.id.person_info_page_iv_newmessage);
		msgBadgeView=new BadgeView(this);
		msgBadgeView.setVisibility(View.INVISIBLE);
		msgBadgeView.setTargetView(msgIv);
		topTv=(TextView)findViewById(R.id.listlayout_top_text);
		renren_confirm_layout = (RelativeLayout) findViewById(R.id.renren_confirm_layout);
		qq_confirm_layout = (RelativeLayout) findViewById(R.id.qq_confirm_layout);
		weibo_confirm_layout = (RelativeLayout) findViewById(R.id.weibo_confirm_layout);
		renrenTv = (TextView) findViewById(R.id.page_info_renren_tv);
		qqTv = (TextView) findViewById(R.id.page_info_qq_tv);
		weiboTv = (TextView) findViewById(R.id.page_info_weibo_tv);
		renrenConfirmTv = (TextView) findViewById(R.id.page_info_renren_confirm_tv);
		qqConfirmTv = (TextView) findViewById(R.id.page_info_qq_confirm_tv);
		weiboConfirmTv = (TextView) findViewById(R.id.page_info_weibo_confirm_tv);
		renrenConfirmTv.setVisibility(View.INVISIBLE);
		qqConfirmTv.setVisibility(View.INVISIBLE);
		weiboConfirmTv.setVisibility(View.INVISIBLE);
		Intent intent = this.getIntent();
		uid = intent.getIntExtra("uid", ShareData.MyId);
		initPhotoPath();
		pubishedLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("uid", uid);
				intent.setAction("android.intent.action.PublishedPage");
				startActivity(intent);
			}
		});
		getThirdInfo();
		if (ShareData.MyId == uid) {
			if (ShareData.mMyPersonInfo == null) {
				getPersonInfo();
				btn_setting.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(mContext, "请稍等！", Toast.LENGTH_SHORT)
								.show();
					}
				});
			} else {
				mPersonInfo = ShareData.mMyPersonInfo;
				setView();
			}
			privilegeLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setAction("android.intent.action.PrivilegePage");
					startActivity(intent);
				}
			});
			msgIv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				  Intent intent=new Intent(PersonInfoPage.this,PersonMovmentPage.class);
				  startActivity(intent);
				}
			});
			btn_moreinfo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setAction("android.intent.action.PersonEditInfoPage");
					startActivity(intent);
				}
			});
		} else {
			topTv.setText(title);
			msgIv.setVisibility(View.GONE);
			btn_setting.setVisibility(View.GONE);
			signinLayout.setVisibility(View.GONE);
			privilegeLayout.setVisibility(View.GONE);
			publishTv.setText("Ta的发布");
			collectTv.setText("Ta的收藏");
			getOtherPersonInfo();
			btn_moreinfo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra("uid", uid);
					intent.setAction("android.intent.action.PersonDetailInfoPage");
					startActivity(intent);
				}
			});
		}
	}

	public void setView() {
		tv_nicName.setText(mPersonInfo.getNicName());
		tv_piont.setText(String.valueOf(mPersonInfo.getPoint()));
		tv_psnsig.setText(mPersonInfo.getPsnsig());
		setHeadImage( mPersonInfo.getHead());
		if (uid == ShareData.MyId) {
			btn_setting.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setAction("android.intent.action.SettingsPage");
					startActivity(intent);
				}
			});
			collectedLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra("uid", uid);
					System.out.println("uid in personInfo:  --"+uid);
					intent.setAction("android.intent.action.CollectedPage");
					startActivity(intent);
				}
			});
			iv_head.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDialog();
				}
			});
			signinLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					signIn();
				}
			});
		} else {
			collectedLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					switch (mPersonInfo.getColPublic()) {
					case ShareData.PUBLIC_YES:
						Intent intent1 = new Intent();
						intent1.putExtra("uid", uid);
						intent1.setAction("android.intent.action.CollectedPage");
						startActivity(intent1);
						break;
					case ShareData.PUBLIC_NO:
						Intent intent2 = new Intent();
						intent2.setAction("android.intent.action.BlankPage");
						startActivity(intent2);
						break;
					default:
						break;
					}
				}
			});
		}
	}

	private void authorize(Platform plat) {
		plat.removeAccount();
		ShareSDK.removeCookieOnAuthorize(true);
		plat.setPlatformActionListener(this);
		// 关闭SSO授权
		plat.SSOSetting(true);
		plat.showUser(null);
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

	public void setHeadImage(String url) {
		imageLoader.displayImage(url, iv_head, options, null);
	}

	/** 初始化数据 */
	private void initPhotoPath() {
		// 初始化照片保存地址
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String thumPicture = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/"
					+ mContext.getPackageName()
					+ "/download";
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
			picturePath = pictureFile.getAbsolutePath();
			Log.e("picturePath ==>>", picturePath);
		} else {
			Log.e("change user icon ==>>", "there is not sdcard!");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IMAGE_REQUEST_CODE
				&& resultCode == Activity.RESULT_OK && null != data) {
			startPhotoZoom(data.getData());
		} else if (requestCode == CAMERA_REQUEST_CODE
				&& resultCode == Activity.RESULT_OK) {
			if (Tools.hasSdcard()) {
				File tempFile = new File(
						Environment.getExternalStorageDirectory() + "/"
								+ IMAGE_FILE_NAME);
				startPhotoZoom(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(PersonInfoPage.this, "未找到存储卡，无法存储照片！",
						Toast.LENGTH_LONG).show();
			}

		} else if (requestCode == INTENT_ACTION_CROPED) {
			if (data != null) {
				getImageToView(data);
			}
		}
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
			File file = new File(picturePath);
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
			headImage = Tools.getImageStr(picturePath);
			Drawable drawable = new BitmapDrawable(getResources(), photo);
			iv_head.setImageDrawable(drawable);
			updateHeadImage();
		}
	}

	public void signIn() {
		url = ShareData.SEEVER_BASE_URL + "signIn.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("uid", String.valueOf(uid));
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
								Toast.makeText(PersonInfoPage.this,
										"签到成功，积分+2！", Toast.LENGTH_SHORT)
										.show();
								mPersonInfo.setPoint(mPersonInfo.getPoint() + 2);
								ShareData.mMyPersonInfo.setPoint(mPersonInfo.getPoint());
								tv_piont.setText(String.valueOf(mPersonInfo
										.getPoint()));
							} else {
								Toast.makeText(mContext, flag,
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
						Toast.makeText(mContext, "连接服务器失败！", Toast.LENGTH_SHORT)
								.show();
					}
				});
		mQueue.add(mJsonObjectRequest);
	}

	private void getPersonInfo() {
		System.out.println("enter---personinfo");
		url = ShareData.SEEVER_BASE_URL + "getPersonInfo.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("uid", String.valueOf(uid));
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
								ShareData.mMyPersonInfo = gson.fromJson(
										response.getString("personInfo"),
										new TypeToken<PersonInfo>() {
										}.getType());
								mPersonInfo = ShareData.mMyPersonInfo;
								Message msg = new Message();
								msg.what = DATE_COMPLETE;
								mHandler.sendMessage(msg);
							} else {
								Toast.makeText(mContext, flag,
										Toast.LENGTH_SHORT).show();
							}
						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
						Toast.makeText(mContext, "连接服务器失败！", Toast.LENGTH_SHORT)
								.show();
					}
				});
		mQueue.add(jsonObjectRequest);
	}

	private void getOtherPersonInfo() {
		url = ShareData.SEEVER_BASE_URL + "getPersonInfo.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("uid", String.valueOf(uid));// new
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
								mPersonInfo = gson.fromJson(
										response.getString("personInfo"),
										new TypeToken<PersonInfo>() {
										}.getType());
								Message msg = new Message();
								msg.what = DATE_COMPLETE;
								mHandler.sendMessage(msg);
							} else {
								Toast.makeText(mContext, flag,
										Toast.LENGTH_SHORT).show();
							}

						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
						Toast.makeText(mContext, "连接服务器失败！", Toast.LENGTH_SHORT)
								.show();
					}
				});
		mQueue.add(jsonObjectRequest);
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == DATE_COMPLETE) {
			setView();
		} else if (msg.what == AVATER_SUCCESS) {
			Toast.makeText(this, "成功更改头像！", Toast.LENGTH_LONG).show();
		} else if (msg.what == FIRST_COMEIN) {
			confirmThirdPlat();
		} else if (msg.what == NOT_FIRST) {
			showExitsDialog();
		} else if (msg.what == CONFIRM_SUCCESSS) {
			Toast.makeText(this, "账号绑定成功！今后您可以用此账号来登陆！", Toast.LENGTH_LONG)
					.show();
			changeThirdView(source, thirdUserName);
		} else if (msg.what == THIRD_INFO_COME) {
			System.out.println("third info come!");
			System.out.println("third list:" + thirdInfoList);
			for (int i = 0; i < thirdInfoList.size(); i++) {
				if (!thirdInfoList.get(i).getUserName().isEmpty()) {
					changeThirdView(thirdInfoList.get(i).getType(),
							thirdInfoList.get(i).getUserName());
				}
			}
			if (ShareData.MyId == uid) {
				setThirdView();
			} else {
				setOtherThirdView();
			}
		}else if(msg.what==UNREAD_MSG_COME){
			System.out.println("handle messgae in ! outside");
			if(unreadCount==0){
				msgBadgeView.setVisibility(View.INVISIBLE);
			}else{
				System.out.println("handle messgae in ! unread come!");
				msgBadgeView.setBadgeCount(unreadCount);
				msgBadgeView.setTextSize(12);
				msgBadgeView.setVisibility(View.VISIBLE);
			}
		}
		return false;
	}

	private void setOtherThirdView() {
		if (!isThirdConnect[LOGINBYQQ]) {
			qq_confirm_layout.setVisibility(View.GONE);
		}
		if (!isThirdConnect[LOGINBYRENREN]) {
			renren_confirm_layout.setVisibility(View.GONE);
		}
		if (!isThirdConnect[LOGINBYWEIBO]) {
			weibo_confirm_layout.setVisibility(View.GONE);
		}
	}

	private void setThirdView() {
		if (!isThirdConnect[LOGINBYRENREN]) {
			renren_confirm_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					source = LOGINBYRENREN;
					Platform renren = ShareSDK.getPlatform(Renren.NAME);
					authorize(renren);
				}
			});
		}
		if (!isThirdConnect[LOGINBYWEIBO]) {
			weibo_confirm_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					source = LOGINBYWEIBO;
					Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
					authorize(weibo);
				}
			});
		}
		if (!isThirdConnect[LOGINBYQQ]) {
			qq_confirm_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					source = LOGINBYQQ;
					Platform qq = ShareSDK.getPlatform(QQ.NAME);
					authorize(qq);
				}
			});
		}
	}

	private void showExitsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("检测到该第三方账号已经关联过其他账号，是否解除已有关联，绑定到当前账号？");
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				changeThirdPlat();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.setTitle("系统提示");

	}

	private void updateHeadImage() {
		url = ShareData.SEEVER_BASE_URL + "updateHeadImage.json";
		Map<String, String> appendHeader = new HashMap<String, String>();
		appendHeader.put("uid", String.valueOf(uid));
		appendHeader.put("headImage", headImage);
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
								Message msg = new Message();
								msg.what = AVATER_SUCCESS;
								mHandler.sendMessage(msg);
							} else {
								Toast.makeText(mContext, flag,
										Toast.LENGTH_SHORT).show();
							}

						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
						Toast.makeText(mContext, "上传头像失败，网络不稳定，请稍后重试！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQueue.add(jsonObjectRequest);

	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete(Platform plat, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		PlatformDb platDB = plat.getDb();// 获取数平台数据DB
		// 通过DB获取各种数据
		thirdUserAvaterUrl = platDB.getUserIcon();
		thirdFuid = platDB.getUserId();
		thirdUserName = platDB.getUserName();
		checkThirdIsFirst();

	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub

	}

	public void confirmThirdPlat() {
		url = ShareData.SEEVER_BASE_URL + "confirmThirdPlat.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("fuid", thirdFuid);
		appendHeader.put("type", source + "");
		appendHeader.put("userName", thirdUserName);
		appendHeader.put("userAvaterUrl", thirdUserAvaterUrl);
		appendHeader.put("userId", uid + "");
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
								msg.what = CONFIRM_SUCCESSS;
								mHandler.sendMessage(msg);
							} else {
								Toast.makeText(PersonInfoPage.this, flag,
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
						Toast.makeText(getBaseContext(), "网络连接失败，请稍后再试！",
								Toast.LENGTH_SHORT).show();
					}
				});
		mQueue.add(mJsonObjectRequest);
	}

	public void checkThirdIsFirst() {
		url = ShareData.SEEVER_BASE_URL + "ThirdLogin.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("fuid", thirdFuid);
		appendHeader.put("type", source + "");
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
								msg.what = FIRST_COMEIN;
								mHandler.sendMessage(msg);
							} else {
								existuserId = response.getInt("userId");
								Message msg = new Message();
								msg.what = NOT_FIRST;
								mHandler.sendMessage(msg);
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
		mQueue.add(mJsonObjectRequest);
	}

	private void changeThirdPlat() {
		url = ShareData.SEEVER_BASE_URL + "changeThirdPlat.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("oldUserId", existuserId + "");
		appendHeader.put("type", source + "");
		appendHeader.put("newUserId", uid + "");
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
								msg.what = CHANGE_SUCCESS;
								mHandler.sendMessage(msg);
							} else {
								Toast.makeText(PersonInfoPage.this, flag,
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
		mQueue.add(mJsonObjectRequest);
	}

	private void changeThirdView(int type, String userName) {
		switch (type) {
		case LOGINBYQQ:
			qqConfirmTv.setVisibility(View.VISIBLE);
			qqTv.setText(userName);
			isThirdConnect[LOGINBYQQ] = true;
			break;
		case LOGINBYRENREN:
			renrenConfirmTv.setVisibility(View.VISIBLE);
			renrenTv.setText(userName);
			isThirdConnect[LOGINBYRENREN] = true;
			break;
		case LOGINBYWEIBO:
			weiboConfirmTv.setVisibility(View.VISIBLE);
			weiboTv.setText(userName);
			isThirdConnect[LOGINBYWEIBO] = true;
			break;
		}

	}

	private void getThirdInfo() {
		url = ShareData.SEEVER_BASE_URL + "GetThirdInfo.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("uid", uid);
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
								thirdInfoList = gson.fromJson(
										response.getString("thirdInfo"),
										new TypeToken<List<thirdPlatInfo>>() {
										}.getType());
								System.out
										.println("thirdinfolist in response: "
												+ thirdInfoList);
								Message msg = new Message();
								msg.what = THIRD_INFO_COME;
								mHandler.sendMessage(msg);
							} else {
								Toast.makeText(PersonInfoPage.this, flag,
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
		mQueue.add(mJsonObjectRequest);
	}
	private void getunReadCount(){
		url = ShareData.SEEVER_BASE_URL + "GetUnReadCount.json";
		Map<String, Object> appendHeader = new HashMap<String, Object>();
		appendHeader.put("uid", uid);
		JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(url,
				new JSONObject(appendHeader),
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						System.out.println(response.toString());
						try {
								unreadCount=response.getInt("num");
								System.out.println("num is "+unreadCount);
								Message msg = new Message();
								msg.what = UNREAD_MSG_COME;
								mHandler.sendMessage(msg);
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
		mQueue.add(mJsonObjectRequest);
	}
	@Override
	public void onResume() {
		super.onResume();
		System.out.println("person Info on resume!");
		if(uid==ShareData.MyId){
			if (ShareData.mMyPersonInfo!=null) {
				mPersonInfo=ShareData.mMyPersonInfo;
				tv_piont.setText(String.valueOf(mPersonInfo.getPoint()));
				tv_nicName.setText(String.valueOf(mPersonInfo.getNicName()));
				tv_psnsig.setText(String.valueOf(mPersonInfo.getPsnsig()));
			}
			MainTabContainer.isMessageCome=true;
			getunReadCount();
		}

	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

}
