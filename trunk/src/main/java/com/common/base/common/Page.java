package com.common.base.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author zhangyaozhou
 */
public class Page implements Serializable{
	private static int DEFAULT_PAGE_SIZE = 20;

	private int pageSize = DEFAULT_PAGE_SIZE;

	private int start;

	private Object data;

	public void setData(Object data) {
		this.data = data;
	}

	private int totalCount;

	public Page() {
		this(0, 0, DEFAULT_PAGE_SIZE, new ArrayList<Object>());
	}

	public Page(int start, int totalSize, int pageSize, Object data) {
		this.pageSize = pageSize;
		this.start = start;
		this.totalCount = totalSize;
		this.data = data;
	}
	public int getTotalCount() {
		return this.totalCount;
	}

	public int getTotalPageCount() {
		if (totalCount % pageSize == 0)
			return totalCount / pageSize;
		else
			return totalCount / pageSize + 1;
	}

	public long getPageSize() {
		return pageSize;
	}

	public Object getResult() {
		return data;
	}

	public long getCurrentPageNo() {
		return start / pageSize + 1;
	}

	public boolean hasNextPage() {
		return this.getCurrentPageNo() < this.getTotalPageCount() - 1;
	}

	public boolean hasPreviousPage() {
		return this.getCurrentPageNo() > 1;
	}

	protected static long getStartOfPage(long pageNo) {
		return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
	}

	public static long getStartOfPage(long pageNo, long pageSize) {
		return (pageNo - 1) * pageSize;
	}

    public long getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
