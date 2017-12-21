package com.douguo.dc.user.utils.grid;

import java.io.Serializable;
import java.util.List;

public class GridPager<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int page;

	private int total;

	private int records;

	private List<T> rows;

	public GridPager(int page, int total, int records, List<T> rows) {
		super();
		this.page = page;
		this.total = total;
		this.records = records;
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public int getTotal() {
		return total;
	}

	public int getRecords() {
		return records;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}