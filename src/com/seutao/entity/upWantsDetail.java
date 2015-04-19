package com.seutao.entity;

public class upWantsDetail {
	private float wantPrice;
	private String wantTitle;
	private String wantContent;
	public float getWantPrice() {
		return wantPrice;
	}
	public void setWantPrice(float wantPrice) {
		this.wantPrice = wantPrice;
	}
	public String getWantTitle() {
		return wantTitle;
	}
	public void setWantTitle(String wantTitle) {
		this.wantTitle = wantTitle;
	}
	public String getWantContent() {
		return wantContent;
	}
	public void setWantContent(String wantContent) {
		this.wantContent = wantContent;
	}
	public String getWantClassify() {
		return wantClassify;
	}
	public void setWantClassify(String wantClassify) {
		this.wantClassify = wantClassify;
	}
	public upWantsDetail(float wantPrice, String wantTitle, String wantContent,
			String wantClassify) {
		super();
		this.wantPrice = wantPrice;
		this.wantTitle = wantTitle;
		this.wantContent = wantContent;
		this.wantClassify = wantClassify;
	}
	private String wantClassify;

}
