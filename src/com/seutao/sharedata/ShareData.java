package com.seutao.sharedata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.seutao.core.R;
import com.seutao.entity.CollectedGood;
import com.seutao.entity.CollectedNeed;
import com.seutao.entity.PersonDetailInfo;
import com.seutao.entity.PersonInfo;
import com.seutao.entity.PublishedGood;
import com.seutao.entity.PublishedNeed;

public class ShareData {
	public static final int PUBLIC_NO = 0;
	public static final int PUBLIC_YES = 1;
	public static List<CollectedGood> mCollectedGoods = new ArrayList<CollectedGood>();
	public static List<CollectedNeed> mCollectedNeeds = new ArrayList<CollectedNeed>();
	public static List<PublishedGood> mDelayGoods=new ArrayList<PublishedGood>();
	public static List<PublishedNeed> mDelayNeeds=new ArrayList<PublishedNeed>();
	public static final int PersonDetailInfo_nicName = 0;
	public static final int PersonDetailInfo_psnsig = 1;
	public static final int PersonDetailInfo_hobby = 2;
	public static final int PersonDetailInfo_sex = 3;
	public static final int PersonDetailInfo_hometown_province = 4;
	public static final int PersonDetailInfo_hometown_city = 5;
	public static final int PersonDetailInfo_birth = 6;
	public static final int PersonDetailInfo_constellation = 7;
	public static final int PersonDetailInfo_school = 8;
	public static final int PersonDetailInfo_department = 9;
	public static final int PersonDetailInfo_enrolltime = 10;
	public static final int PersonDetailInfo_email = 11;
	public static final int PersonDetailInfo_tel = 12;
	public static final int PersonDetailInfo_qq = 13;
	public static final int PersonDetailInfo_phonepublic = 14;
	public static final int PersonDetailInfo_collectionpublic = 15;
	public static final int PersonDetailInfo_hometown_area = 16;
	public static final int List_init = 0;
	public static final int List_load = 2;
	public static final int List_length = 2;
	public static final int Sex_boy = 1;
	public static final int Sex_girl = 0;
	// public static String IP="218.244.136.155";
	public static String IP = "192.168.0.100";
	public static String PORT = "8080";
	public static String SEEVER_BASE_URL = "http://" + IP + ":" + PORT + "/";
	public static int MyId = -1;
	public static String SEUTAO_PACKAGE_NAME = "SeuTao";
	public static boolean delayIsSelectAll = false;
	public static boolean collectedIsSelectAll = false;
	public static boolean collectedIsEdit = false;
	public static PersonDetailInfo mMyPersonDetailInfo = null;
	public static List<String> HobbyList = null;
	public static PersonInfo mMyPersonInfo = null;
	public static String[] hobbies = null;
	public static final String SecondClassify[][] = new String[][] {
			new String[] { "衣服", "裤子", "鞋子", "帽子", "手套", "其他" },
			new String[] { "电脑", "手机", "数码影音", "存储产品", "配件", "其他" },
			new String[] { "自行车", "日用电器", "体育用品", "乐器", "其他" },
			new String[] { "本科教材", "GRE", "考研", "雅思", "托福", "其他" },
			new String[] { "0元转让" }, new String[] { "其他" } };
	public static final String FirstClassify[] = new String[] { "衣物鞋帽", "数码电子",
			"生活日用", "书籍杂志", "零元转让", "其他" };
	public static String photoInfo = "empty";

	// 配置
	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
		public static final boolean IS_RUNNING = false;
	}

	public static Map<String, Integer> classifyIdMap = new HashMap<String, Integer>() {

		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;

		{
			put("数码电子-手机", 1);
			put("数码电子-电脑", 2);
			put("衣物鞋帽-衣服", 3);
			put("衣物鞋帽-裤子", 4);
			put("衣物鞋帽-鞋子", 5);
			put("衣物鞋帽-帽子", 6);
			put("衣物鞋帽-手套", 7);
			put("衣物鞋帽-其他", 8);
			put("数码电子-数码影音", 9);
			put("数码电子-存储产品", 10);
			put("数码电子-配件", 11);
			put("数码电子-其他", 12);
			put("生活日用-自行车", 13);
			put("生活日用-日用电器", 14);
			put("生活日用-体育用品", 15);
			put("生活日用-乐器", 16);
			put("生活日用-其他", 17);
			put("书籍杂志-本科教材", 18);
			put("书籍杂志-GRE", 19);
			put("书籍杂志-考研", 20);
			put("书籍杂志-雅思", 21);
			put("书籍杂志-托福", 22);
			put("书籍杂志-其他", 23);
			put("0元转让-0元转让", 24);
			put("其他-其他", 25);
		}
	};

	public static int getClassifyId(String classifyName) {

		return classifyIdMap.get(classifyName);

	}

	public static ImageLoader getImageLoader() {
		return ImageLoader.getInstance();
	}

	public static DisplayImageOptions getOptions() {
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_stub) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_stub) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();
		return options;
	}

	public static DisplayImageOptions getRoundOptions() {
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_stub) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_stub) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(90)) // 设置成圆角图片
				.build();
		return options;
	}

	public static String getTime(long time) {
		String timestamp;
		long second = new Date().getTime() / 1000 - time / 1000;
		if (second > 59) {
			second /= 60;
			if (second > 59) {
				second /= 60;
				if (second > 59) {
					second /= 24;
					timestamp = second + "天前";
				} else {
					timestamp = second + "小时前";
				}
			} else {
				timestamp = second + "分钟前";
			}
		} else {
			timestamp = second + "秒前";
		}
		return timestamp;
	}

}
