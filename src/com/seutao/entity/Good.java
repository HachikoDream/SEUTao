package com.seutao.entity;


public class Good {
	private int image = 0;
	private String name = null;
	private int view = 0;
	private float price = 0.0f;
	private int time = 0;

	public Good(int image, String name, int view, float price, int time) {
		this.image = image;
		this.name = name;
		this.view = view;
		this.price = price;
		this.time = time;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
