package com.seutao.entity;


public class Need {
	private int image = 0;
	private String title = null;
	private String name = null;
	private float price = 0.0f;

	public Need(int image, String title, String name, float price) {
		this.image = image;
		this.title = title;
		this.name = name;
		this.price = price;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

}
