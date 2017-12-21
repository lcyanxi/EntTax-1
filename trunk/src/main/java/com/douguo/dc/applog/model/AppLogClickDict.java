package com.douguo.dc.applog.model;

import java.io.Serializable;

public class AppLogClickDict implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4254811344388647043L;
	private int id;
	private int page;
	private int view;
	private int position;
	private String title;
	private String titleDesc;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleDesc() {
		return titleDesc;
	}

	public void setTitleDesc(String titleDesc) {
		this.titleDesc = titleDesc;
	}
}
