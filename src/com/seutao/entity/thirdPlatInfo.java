package com.seutao.entity;

public class thirdPlatInfo {
	int type;
	String userName;
	public thirdPlatInfo(int type, String userName) {
		super();
		this.type = type;
		this.userName = userName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "thirdPlatInfo [type=" + type + ", userName=" + userName + "]";
	}
	

}