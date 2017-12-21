package com.douguo.dc.user.model;

import java.util.List;
import java.util.Map;

public class User {

	private Integer id;
	private String uid;
	private String pass;
	private String username;
	private String group_id;
//	private String[][] roles;
//	private String[][] functions;
	private List<Map<String,Object>> functions;

//	public String[][] getFunctions() {
//		return functions;
//	}
//
//	public void setFunctions(String[][] functions) {
//		this.functions = functions;
//	}
	
	public List<Map<String,Object>> getFunctions() {
		return functions;
	}

	public void setFunctions(List<Map<String,Object>> functions) {
		this.functions = functions;
	}


//	public String[][] getRoles() {
//		return roles;
//	}
//
//	public void setRoles(String[][] roles) {
//		this.roles = roles;
//	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
}