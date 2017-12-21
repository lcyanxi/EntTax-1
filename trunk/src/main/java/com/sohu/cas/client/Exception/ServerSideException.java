package com.sohu.cas.client.Exception;

/**
 * Created by IntelliJ IDEA. User: harry Date: 2010-6-11 Time: 17:06:13 To
 * change this template use File | Settings | File Templates.
 */
public class ServerSideException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServerSideException(String msg) {
		super(msg);
	}

	public ServerSideException() {
		super();
	}
}
