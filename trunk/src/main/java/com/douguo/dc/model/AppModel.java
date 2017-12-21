package com.douguo.dc.model;

import java.io.Serializable;

public class AppModel implements Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private int					id;
	private String				name;
	private String				key;
	private int					user_id;
	private String				status;

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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
