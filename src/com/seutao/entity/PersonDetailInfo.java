package com.seutao.entity;

public class PersonDetailInfo {
	private int uid;
	private String nicName = null;
	private String psnsig = null;
	private String hobby = null;
	private int sex;
	private String hometown_province = null;
	private String hometown_city = null;
	private String birth = null;
	private String constellation = null;
	private String school = null;
	private String department = null;
	private String enrolltime = null;
	private String tel = null;
	private String qq = null;
	private String hometown_area = null;

	public PersonDetailInfo() {
		super();
	}

	public String getHometown() {
		String empty = "";
		if (hometown_province.isEmpty()) {
			return empty;
		} else {
			return hometown_province + "-" + hometown_city + "-"
					+ hometown_area;
		}
	}

	public int getUid() {
		return uid;
	}

	public PersonDetailInfo(int uid, String nicName, String psnsig,
			String hobby, int sex, String hometown_province,
			String hometown_city, String birth, String constellation,
			String school, String department, String enrolltime, String tel,
			String qq, String hometown_area) {
		super();
		this.uid = uid;
		this.nicName = nicName;
		this.psnsig = psnsig;
		this.hobby = hobby;
		this.sex = sex;
		this.hometown_province = hometown_province;
		this.hometown_city = hometown_city;
		this.birth = birth;
		this.constellation = constellation;
		this.school = school;
		this.department = department;
		this.enrolltime = enrolltime;
		this.tel = tel;
		this.qq = qq;
		this.hometown_area = hometown_area;
	}

	public String getHometown_province() {
		return hometown_province;
	}

	public void setHometown_province(String hometown_province) {
		this.hometown_province = hometown_province;
	}

	public String getHometown_city() {
		return hometown_city;
	}

	public void setHometown_city(String hometown_city) {
		this.hometown_city = hometown_city;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getHometown_area() {
		return hometown_area;
	}

	public void setHometown_area(String hometown_area) {
		this.hometown_area = hometown_area;
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

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEnrolltime() {
		return enrolltime;
	}

	public void setEnrolltime(String enrolltime) {
		this.enrolltime = enrolltime;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

}
