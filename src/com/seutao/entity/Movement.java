package com.seutao.entity;

public class Movement {
	private String datetime;
	private String content;
	private int relatedId;
	private int type;
	private int movementid;
	private int readornot;
	public Movement(String datetime, String content, int relatedId, int type,
			int movementid, int readornot) {
		super();
		this.datetime = datetime;
		this.content = content;
		this.relatedId = relatedId;
		this.type = type;
		this.movementid = movementid;
		this.readornot = readornot;
	}
	@Override
	public String toString() {
		return "Movement [datetime=" + datetime + ", content=" + content
				+ ", relatedId=" + relatedId + ", type=" + type
				+ ", movementid=" + movementid + ", readornot=" + readornot
				+ "]";
	}
	public int getReadornot() {
		return readornot;
	}
	public void setReadornot(int readornot) {
		this.readornot = readornot;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(int relatedId) {
		this.relatedId = relatedId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getMovementid() {
		return movementid;
	}
	public void setMovementid(int movementid) {
		this.movementid = movementid;
	}
	

}
