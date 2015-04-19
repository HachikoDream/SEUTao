package com.seutao.entity;

public class PersonInfo {
	private int uid;
	private String head="0";
	private int point=0;
	private String nicName=null;
	private String psnsig=null;
	private int telPublic=0;
	private int colPublic=0;
	
	public PersonInfo(int uid, String head, int point,String nicName,
			String psnsig,int telPublic,int colPublic) {
		super();
		this.uid = uid;
		this.point = point;
		this.head = head;
		this.nicName = nicName;
		this.psnsig = psnsig;
		this.telPublic = telPublic;
		this.colPublic = colPublic;
	}
	public int getTelPublic() {
		return telPublic;
	}
	public int getChangedTelPublic() {
		return (telPublic+1)%2;
	}
	public void setTelPublic(int telPublic) {
		this.telPublic = telPublic;
	}
	public void changeTelPublic() {
		telPublic = (telPublic+1)%2;
	}
	public int getColPublic() {
		return colPublic;
	}
	public int getChangedColPublic() {
		return (colPublic+1)%2;
	}
	public void setColPublic(int colPublic) {
		this.colPublic = colPublic;
	}
	public void changeColPublic() {
		colPublic = (colPublic+1)%2;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getNicName() {
		return nicName;
	}
	public void setNicName(String nicName) {
		this.nicName = nicName;
	}
	public String getPsnsig() {
		return psnsig;
	}
	public void setPsnsig(String psnsig) {
		this.psnsig = psnsig;
	}
	
}
