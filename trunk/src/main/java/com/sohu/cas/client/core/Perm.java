package com.sohu.cas.client.core;

import java.io.Serializable;

public class Perm implements Serializable {

	private static final long serialVersionUID = 1L;

	public Perm(String name, int code, int type) {
		this.name = name;
		this.code = code;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private String name;
	private int code;
	private int type;

}
