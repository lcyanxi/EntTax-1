package com.douguo.dc.channel.model;

import java.io.Serializable;

public class ChannelPlat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6912949280580735521L;
	private int id;
	private String platName;
	private String platDesc;
	private int sort;
	private String createTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPlatName() {
		return platName;
	}
	public void setPlatName(String platName) {
		this.platName = platName;
	}
	public String getPlatDesc() {
		return platDesc;
	}
	public void setPlatDesc(String platDesc) {
		this.platDesc = platDesc;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}