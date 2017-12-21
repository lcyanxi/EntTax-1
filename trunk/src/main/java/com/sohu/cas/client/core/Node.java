package com.sohu.cas.client.core;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. User: harry Date: 2010-4-7 Time: 16:10:37 To change
 * this template use File | Settings | File Templates.
 */
public class Node implements Serializable {

	private static final long serialVersionUID = 1L;

	public Node(String fullName) {
		this(fullName, "", "");
	}

	public Node(String fullName, String stageName) {
		this(fullName, stageName, "");
	}

	public Node(String fullName, String stageName, String detail) {
		String[] node_names = fullName.split("\\.");
		this.name = node_names[node_names.length - 1];
		this.fullName = fullName;
		this.detail = detail;
		this.stageName = stageName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	private String name;
	private String stageName;
	private String fullName;
	private String detail;
}
