package com.zyz.open.hiveadmin.model;

import java.io.Serializable;

public class HiveAdminJob implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9093191942348759983L;

	private int id;
	private String jobName;
	private String uid;
	private String hql;
	private String hqlTemplate;
	private int status;
	private String jobType;
	private String queryType;
	private String jobTypeContent;
	private String statBeginTime;
	private String statEndTime;
	private String jobStartTime;
	private String updateTime;
	private String createTime;

	public String getJobTypeContent() {
		return jobTypeContent;
	}

	public void setJobTypeContent(String jobTypeContent) {
		this.jobTypeContent = jobTypeContent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getHql() {
		return hql;
	}

	public String getHqlTemplate() {
		return hqlTemplate;
	}

	public void setHqlTemplate(String hqlTemplate) {
		this.hqlTemplate = hqlTemplate;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getStatBeginTime() {
		return statBeginTime;
	}

	public void setStatBeginTime(String statBeginTime) {
		this.statBeginTime = statBeginTime;
	}

	public String getStatEndTime() {
		return statEndTime;
	}

	public void setStatEndTime(String statEndTime) {
		this.statEndTime = statEndTime;
	}

	public String getJobStartTime() {
		return jobStartTime;
	}

	public void setJobStartTime(String jobStartTime) {
		this.jobStartTime = jobStartTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}