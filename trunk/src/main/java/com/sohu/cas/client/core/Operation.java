package com.sohu.cas.client.core;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. User: zichengxiong Date: 2010-3-26 Time: 17:08:42
 * 
 */
public class Operation implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int READ = 1 << 0;
	public static final int WRITE = 1 << 1;
	public static final int EXTEND = 1 << 24;
	public static final int GRANT = 1 << 25;
	public static final int CONTRIBUTE = 1 << 2; // 投稿
	public static final int APPROVAL = 1 << 3; // 审核
	public static final int VIEWACTION = 1 << 4;
	public static final int ADDACTION = 1 << 5;
	public static final int EDITACTION = 1 << 6;
	public static final int LISTACTION = 1 << 7;
	public static final int DELETEACTION = 1 << 8;
	public static final int AUTHORIZED = 1 << 9;
};
