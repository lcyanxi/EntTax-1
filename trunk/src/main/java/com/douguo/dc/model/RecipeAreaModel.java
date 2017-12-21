package com.douguo.dc.model;

public class RecipeAreaModel {
	private long id;
	private int province_id;
	private int city_id;
	private int source;
	private int usertype_id;
	private int num;
	private String statdate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getProvince_id() {
		return province_id;
	}
	public void setProvince_id(int province_id) {
		this.province_id = province_id;
	}
	public int getCity_id() {
		return city_id;
	}
	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public int getUsertype_id() {
		return usertype_id;
	}
	public void setUsertype_id(int usertype_id) {
		this.usertype_id = usertype_id;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getStatdate() {
		return statdate;
	}
	public void setStatdate(String statdate) {
		this.statdate = statdate;
	}
	
}
