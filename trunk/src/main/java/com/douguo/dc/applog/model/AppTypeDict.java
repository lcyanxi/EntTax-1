package com.douguo.dc.applog.model;

import java.io.Serializable;

public class AppTypeDict implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6996760682636661282L;

	private int id;
	private int type;
	private int data;
	private int val;
	private String name;
	private String valDesc;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}

	public String getValDesc() {
		return valDesc;
	}

	public void setValDesc(String valDesc) {
		this.valDesc = valDesc;
	}

}
