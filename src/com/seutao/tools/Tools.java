package com.seutao.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import com.rt.BASE64Encoder;

import android.os.Environment;
/**
 * 
 * @author XuZhiwei (xuzw13@gmail.com)
 * Create at 2012-8-17 上午10:14:40
 */
public class Tools {
	/**
	 * 检查是否存在SDCard
	 * @return
	 */
	public static boolean hasSdcard(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
	 public static String getImageStr(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
			InputStream in = null;
			byte[] data = null;
			// 读取图片字节数组
			try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
			} catch (IOException e) {
			e.printStackTrace();
			}
			// 对字节数组Base64编码
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	 }
	 // MD5加码。32位  
	 public static String MD5(String inStr) {  
		  MessageDigest md5 = null;  
		  try {  
			  md5 = MessageDigest.getInstance("MD5");  
		  } catch (Exception e) {  
			  System.out.println(e.toString());  
			  e.printStackTrace();  
			  return "";  
		  }  
		  char[] charArray = inStr.toCharArray();  
		  byte[] byteArray = new byte[charArray.length];  
	  
		  for (int i = 0; i < charArray.length; i++)  
			  byteArray[i] = (byte) charArray[i];  
		  
		  byte[] md5Bytes = md5.digest(byteArray);  
		  StringBuffer hexValue = new StringBuffer();  
		  
		  for (int i = 0; i < md5Bytes.length; i++) {  
			   int val = ((int) md5Bytes[i]) & 0xff;  
			   if (val < 16)  
				   hexValue.append("0");  
			   hexValue.append(Integer.toHexString(val));  
		  }  
		  return hexValue.toString();  
	 }  

	 
}

