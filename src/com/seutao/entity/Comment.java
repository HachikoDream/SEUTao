package com.seutao.entity;


public class Comment {
	private int image = 0;
	private String name = null;
	private String comment = null;
	private int time = 0;

	public Comment(int image, String name, String comment, int time) {
		this.image = image;
		this.name = name;
		this.comment = comment;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
