package com.douguo.dc.user.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ToolKit {
	private static final Logger logger = LoggerFactory.getLogger(ToolKit.class);

//	public static String getID(HttpServletRequest req, HttpServletResponse res) {
//		CookieManager cookieManager = new CookieManager();
//		cookieManager.processCookie(req, res);
//
//		if (!(cookieManager.isPassed())) {
//			return null;
//		}
//		cookieManager.setCookie();
//		return cookieManager.getUserName().toLowerCase();
//	}

	public static void setCookie(HttpServletResponse response, String name,
			String value, int time, String domain) {
		try {
			Cookie c = new Cookie(name, value);
			c.setDomain(domain);
			c.setPath("/");
			c.setMaxAge(time);
			response.addCookie(c);
		} catch (Exception e) {
			logger.error("ToolKit.setCookie exception: " + e.getMessage());
		}
	}
	
	public static String getCookie(HttpServletRequest request,String name){
		Cookie[] cs = request.getCookies();
		if(cs == null){
			return null;
		}
		for(int i = 0;i<cs.length;++i){
			if(cs[i].getName().equals(name)){
				return cs[i].getValue();
			}
		}
		return null;
	}
	
}
