package com.douguo.crm.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class VIPHuodong implements Serializable {

    private int id;
    private String address;
    private String title;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

    
    
}