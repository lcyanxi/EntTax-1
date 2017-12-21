package com.sohu.cas.client.core;

import com.sohu.cas.client.AuthProxy;
import com.sohu.cas.client.Exception.AuthenticationException;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable {

	private static final long serialVersionUID = 1L;

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

	public List<User> getUsers() throws AuthenticationException {
		if (users == null) {
			users = AuthProxy.getInstance().getGroupUsers(this);
		}
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Group(int id, String name) {
		this.id = id;
		this.name = name;
	}

	private int id = -1;
	private String name = null;
	private List<User> users;
}
