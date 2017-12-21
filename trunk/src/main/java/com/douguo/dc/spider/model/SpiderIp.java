package com.douguo.dc.spider.model;

import java.io.Serializable;

public class SpiderIp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9087072999174133785L;
	private int id;
	private String ip;
	private String spider;
	private String ipDesc;
	private String createTime;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSpider() {
		return spider;
	}

	public void setSpider(String spider) {
		this.spider = spider;
	}

	public String getIpDesc() {
		return ipDesc;
	}

	public void setIpDesc(String ip_desc) {
		this.ipDesc = ip_desc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}