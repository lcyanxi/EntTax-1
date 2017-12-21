package com.douguo.dc.channel.model;

import java.io.Serializable;

public class ChannelCooperation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 245703110048385212L;
	private int id;
	private String copName;
	private String copDesc;
	private int sort;
	private String createTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCopName() {
		return copName;
	}
	public void setCopName(String copName) {
		this.copName = copName;
	}
	public String getCopDesc() {
		return copDesc;
	}
	public void setCopDesc(String copDesc) {
		this.copDesc = copDesc;
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