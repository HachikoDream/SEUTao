package com.seutao.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.seutao.tools.Tools;

public class upGoodsPhotoPage extends Activity implements Callback {
	private Button gotoPageTwo;
	private ImageView mPhotoIv;
	private ImageView goBackIv;
	private TextView topTv;
	private Button topBtn;
	private File tempFile = null;
	private String localImagePath = null;
	private boolean isPhotoReady;
	private Handler mHandler;
	private String upImagePath=null;
	/* 组件 */
	private String[] items = new String[] { "选择本地图片", "拍照" };
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "goodsPic.jpg";

	/* 请求码 */
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int INTENT_ACTION_PICTURE = 3;
	protected static final int LOAD_USER_ICON = 0;
	private String imageUrl;
	private int gid;
	private String source;
    private static final String title="选择商品照片";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_up_goods_page_one);
	    localImagePath=this.getFilesDir().getParent()+"/cache/goodsImage.jpg";
	    upImagePath=this.getFilesDir().getParent()+"/cache/upGoodsImage.jpg"; 
	    gotoPageTwo=(Button)findViewById(R.id.up_goods_gotoPageTwo);
		mPhotoIv=(ImageView)findViewById(R.id.up_goods_page_goodsPhoto);
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
		isPhotoReady=false;
		mHandler=new Handler(this);
		Intent comeIntent=this.getIntent();
		 source=comeIntent.getStringExtra("source");
		if(source.equals("edit")){
			System.out.println("edit in!");
			imageUrl=comeIntent.getStringExtra("photoPath");
			gid=comeIntent.getIntExtra("gid", -1);
			loadIcon(imageUrl);
		}
		mPhotoIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
		gotoPageTwo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isPhotoReady){
					Intent intent =new Intent(upGoodsPhotoPage.this,upGoodsDetailPage.class);
					Bundle b=new Bundle();
					b.putString("localImagePath",localImagePath);
					b.putString("source", source);
					if(source.equals("edit")){
						b.putInt("gid", gid);
					}
					intent.putExtras(b);
					startActivity(intent);
					finish();
					
				}else{
					Toast.makeText(upGoodsPhotoPage.this, "请先点击加号图标选择商品照片", Toast.LENGTH_LONG).show();
				}
			}
		});
				
	}

	private void loadIcon(final String avaterUrl) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("loadicon in!");
				try {
					URL picUrl = new URL(avaterUrl);
					Bitmap userIcon = BitmapFactory.decodeStream(picUrl
							.openStream());
					FileOutputStream b = null;
					try {
						b = new FileOutputStream(upImagePath);
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
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle("选择商品图片")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							getPicture();
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

	/** 从相册获取图片 */
	private void getPicture() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, INTENT_ACTION_PICTURE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {

			switch (requestCode) {
			case CAMERA_REQUEST_CODE:
				if (Tools.hasSdcard()) {
					String path = Environment.getExternalStorageDirectory()
							+ "/" + IMAGE_FILE_NAME;
					getFileToView(path);
				} else {
					Toast.makeText(upGoodsPhotoPage.this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}

				break;
			case INTENT_ACTION_PICTURE:
				if (resultCode == Activity.RESULT_OK && null != data) {
					Cursor c = this.getContentResolver().query(data.getData(),
							null, null, null, null);
					c.moveToNext();
					String path = c.getString(c
							.getColumnIndex(MediaStore.MediaColumns.DATA));
					getFileToView(path);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void getBitMapToFile(Bitmap photo) {
		tempFile = new File(localImagePath);
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(tempFile));
			photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getFileToView(String path) {
		System.out.println("getfile in!");
		Bitmap scalePhoto = compressImageFromFile(path);
		getBitMapToFile(scalePhoto);
		mPhotoIv.setImageBitmap(scalePhoto);
		isPhotoReady = true;
	}

	private Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		System.out.println("outwidth:" + w);
		System.out.println("outheight:" + h);
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率
		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == LOAD_USER_ICON) {
			System.out.println("load user_icon in!");
			Bitmap scalePhoto = compressImageFromFile(upImagePath);
			getBitMapToFile(scalePhoto);
			mPhotoIv.setImageBitmap(scalePhoto);
			isPhotoReady = true;
		}
		return false;
	}

}
