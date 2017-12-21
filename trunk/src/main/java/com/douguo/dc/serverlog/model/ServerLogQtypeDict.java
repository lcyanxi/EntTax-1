package com.douguo.dc.serverlog.model;

import java.io.Serializable;

public class ServerLogQtypeDict implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 740904944630079705L;
	private int id;
	private String qtype;
	private String qtypeName;
	private String service;
	private String createTime;
	private String qdesc;

	public String getQdesc() {
		return qdesc;
	}

	public void setQdesc(String qdesc) {
		this.qdesc = qdesc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQtype() {
		return qtype;
	}

	public void setQtype(String qtype) {
		this.qtype = qtype;
	}

	public String getQtypeName() {
		return qtypeName;
	}

	public void setQtypeName(String qtypeName) {
		this.qtypeName = qtypeName;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}