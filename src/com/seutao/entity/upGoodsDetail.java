package com.seutao.entity;

public class upGoodsDetail {
	private String goodsName;
	private float goodsPrice;
	private String boughtTime;
	private String oldDegree;
	private String schoolPart;
	private String goodsDetail;
	private String classifyName;
	public String getClassifyName() {
		return classifyName;
	}
	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}
	public upGoodsDetail(String classifyName, String goodsName, float goodsPrice,
			String boughtTime, String oldDegree, String schoolPart,
			String goodsDetail) {
		super();
		this.goodsName = goodsName;
		this.goodsPrice = goodsPrice;
		this.boughtTime = boughtTime;
		this.oldDegree = oldDegree;
		this.schoolPart = schoolPart;
		this.goodsDetail = goodsDetail;
		this.classifyName=classifyName;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public float getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(float goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getBoughtTime() {
		return boughtTime;
	}
	public void setBoughtTime(String boughtTime) {
		this.boughtTime = boughtTime;
	}
	public String getOldDegree() {
		return oldDegree;
	}
	public void setOldDegree(String oldDegree) {
		this.oldDegree = oldDegree;
	}
	public String getSchoolPart() {
		return schoolPart;
	}
	public void setSchoolPart(String schoolPart) {
		this.schoolPart = schoolPart;
	}
	public String getGoodsDetail() {
		return goodsDetail;
	}
	public void setGoodsDetail(String goodsDetail) {
		this.goodsDetail = goodsDetail;
	}

}
