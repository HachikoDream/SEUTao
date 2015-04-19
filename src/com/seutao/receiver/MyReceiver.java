package com.seutao.receiver;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;
import com.seutao.core.MainTabContainer;
import com.seutao.core.PersonMovmentPage;
import com.seutao.tools.VibratorUtil;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	System.out.println("[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	ActivityManager activityManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            String packageName="com.seutao.core";
            String activityName=packageName+".MainTabContainer";
            List<RunningTaskInfo>tasksinfo=activityManager.getRunningTasks(1);
            if(tasksinfo.size()>0){
            	ComponentName topComponentName=tasksinfo.get(0).topActivity;
            	if(packageName.equals(topComponentName.getPackageName())){
            		System.out.println("当前Acitivity: "+topComponentName.getClassName());
            		if(activityName.equals(topComponentName.getClassName())){
            			//在当前期望的程序的期望Activity中
            			System.out.println("在当前期望的程序的期望Activity中");
            			processCustomMessage(context,bundle);
            		}else{
            			//在当前期望的程序的其他Activity中
            			MainTabContainer.isMessageCome = true;
            			System.out.println("在当前期望的程序的其他Activity中");
            			VibratorUtil.Vibrate(context,1000);   //震动1000ms  
            		}
            	}else{
            		//不在期望的程序中
            		System.out.println("不在期望之中");
            		JPushLocalNotification ln = new JPushLocalNotification();
            		ln.setBuilderId(0);
            		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            		String title=bundle.getString(JPushInterface.EXTRA_TITLE);
            		ln.setContent(message);
            		ln.setTitle(title);
            		ln.setNotificationId(11111111) ;
            		ln.setBroadcastTime(System.currentTimeMillis());
//            		Map<String , Object> map = new HashMap<String, Object>() ;
//            		map.put("name", "jpush") ;
//            		map.put("test", "111") ;
//            		JSONObject json = new JSONObject(map) ;
//            		ln.setExtras(json.toString()) ;
            		JPushInterface.addLocalNotification(context.getApplicationContext(), ln);
            	}
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            
        	//打开自定义的Activity
        	Intent i = new Intent(context, PersonMovmentPage.class);
//        	i.putExtras(bundle);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	context.startActivity(i);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
			System.out.println("my receive send broadcast to main in!");
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainTabContainer.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainTabContainer.KEY_MESSAGE, message);
			if (!extras.isEmpty()) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(MainTabContainer.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
	}
}
