package com.seutao.entity;

public class LatestWant {
	private String imageURL;//用户头像地址
	private int wid;
	private String wantTitle;
	private int view;
	private String userName;
	private float wantPrice;
	private String timescap;


	public String getImageURL() {
		return imageURL;
	}



	public void setWid(int wid) {
		this.wid = wid;
	}


	public int getWid() {
		return wid;
	}
	
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}


	public String getWantTitle() {
		return wantTitle;
	}


	public void setWantTitle(String wantTitle) {
		this.wantTitle = wantTitle;
	}


	public int getView() {
		return view;
	}


	public void setView(int view) {
		this.view = view;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public float getWantPrice() {
		return wantPrice;
	}


	public void setWantPrice(float wantPrice) {
		this.wantPrice = wantPrice;
	}


	public String getTimescap() {
		return timescap;
	}


	public void setTimescap(String timescap) {
		this.timescap = timescap;
	}


	public LatestWant(String imageURL,int wid, String wantTitle, int view,
			String userName, float wantPrice, String timescap) {
		super();
		this.imageURL = imageURL;
		this.wid=wid;
		this.wantTitle = wantTitle;
		this.view = view;
		this.userName = userName;
		this.wantPrice = wantPrice;
		this.timescap = timescap;
	}

}
