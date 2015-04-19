package com.seutao.entity;

public class PublishedNeed {
	private int id;
	private String name=null;
	private String time=null;
	private int soldouttime;
	private int check;
	private boolean isSelect=false;
	private int view;
	private float price;
	public PublishedNeed(int id, String name, String time, int soldouttime,
			int check,int view,float price) {
		super();
		this.id = id;
		this.name = name;
		this.time = time;
		this.soldouttime = soldouttime;
		this.check = check;
		this.view=view;
		this.price=price;
				
	}
	
	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	public int getSoldouttime() {
		return soldouttime;
	}
	public void setSoldouttime(int soldouttime) {
		this.soldouttime = soldouttime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public boolean getIsSelect() {
		return isSelect;
	}
	public void setIsSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	public void oppositeIsSelect() {
		this.isSelect = !isSelect;
	}
}
