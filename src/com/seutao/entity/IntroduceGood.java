package com.seutao.entity;

public class IntroduceGood {
	private String goodsImage;
	private int iid;
	private int goodsId;
	private String goodsName;
	private int view;
	private float goodsPrice;
	private String timescap;// 用当前时间-商品创建时间，处理一下： N分钟前 N小时前 N天前

	public IntroduceGood(int iid, String goodsImage, int goodsId,
			String goodsName, int view, float goodsPrice, String time) {
		this.iid = iid;
		this.goodsImage = goodsImage;
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.view = view;
		this.goodsPrice = goodsPrice;
		this.timescap = time;
	}

	@Override
	public String toString() {
		return "IntroduceGood [goodsImage=" + goodsImage + ", iid=" + iid
				+ ", goodsId=" + goodsId + ", goodsName=" + goodsName
				+ ", view=" + view + ", goodsPrice=" + goodsPrice
				+ ", Timescap=" + timescap + "]";
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}

	public float getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(float goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getTimescap() {
		return timescap;
	}

	public void setTimescap(String timescap) {
		this.timescap = timescap;
	}

}
