package com.douguo.dc.cookusertag.model;

import java.io.Serializable;

public class CookTag implements Serializable {

	private int id;
	private String user_tag_id;
	private String ingre_categ_id ;
	private int flag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_tag_id() {
		return user_tag_id;
	}

	public void setUser_tag_id(String user_tag_id) {
		this.user_tag_id = user_tag_id;
	}

	public String getIngre_categ_id() {
		return ingre_categ_id;
	}

	public void setIngre_categ_id(String ingre_categ_id) {
		this.ingre_categ_id = ingre_categ_id;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}