package com.douguo.dc.model;

public class BasestatModel {

	private long id;
	private String statdate;
	private String createtime;
	private int totalnum;
	private String statcontent;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getStatdate() {
		return statdate;
	}
	public void setStatdate(String statdate) {
		this.statdate = statdate;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public int getTotalnum() {
		return totalnum;
	}
	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}
	public String getStatcontent() {
		return statcontent;
	}
	public void setStatcontent(String statcontent) {
		this.statcontent = statcontent;
	}
	

}
