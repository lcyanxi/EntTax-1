package com.sohu.cas.client.Exception;

/**
 * Created by IntelliJ IDEA. User: harry Date: 2010-6-11 Time: 17:28:53 To
 * change this template use File | Settings | File Templates.
 */
public class AuthenticationException extends Exception {

	private static final long serialVersionUID = 1L;

	public AuthenticationException(String msg) {
		super(msg);
	}

	public AuthenticationException() {
		super();
	}
}
