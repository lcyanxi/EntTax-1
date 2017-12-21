package com.douguo.dc.user.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.douguo.dc.user.common.ToolKit;
import com.douguo.dc.user.model.User;
import com.douguo.dc.user.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * UserInfoUtil
 * 
 * @author zhangyaozhou
 */
public class UserInfoUtil {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(UserInfoUtil.class);

	private static UserInfoUtil instance = new UserInfoUtil();

	@Autowired
	protected UserService userService;

	public static final String GLOBAL_REQ_USR_CACHE_KEY = "G_REQ_CUR_USR";
	public static final String GLOBAL_SESSION_USR_CACHE_KEY = "G_SESSION_CUR_USR";
	public static final String GLOBAL_PASSPORT_COOKIE_KEY = "GC_PASSPORT_ID";
	public static final String PASSPORT_COOKIE_DOMAIN = "douguo.net";

	public static final String PASSPORT_TEST_KEY = "^Kssdf12(/I)}~%#*&@`+";

	private UserInfoUtil() {
	}

	public static UserInfoUtil getInstance() {
		return instance;
	}

	/**
	 * 
	 * 
	 * @param request
	 * @return
	 */
	public String getPassportFromCookie(HttpServletRequest request) {
		String cookie = ToolKit.getCookie(request,
				UserInfoUtil.GLOBAL_PASSPORT_COOKIE_KEY);
		//@TODO 写cookie部分还需要完善
		if (cookie != null) {
			String[] values = cookie.split("\\|");
			if (values.length == 2) {
				String passport = values[0];
				String md5Value = values[1];
				Calendar calendar = Calendar.getInstance();
				int day = calendar.get(Calendar.DAY_OF_YEAR);
				// 检查md5
				String md5Check = MD5Util.MD5(passport + "|" + day + "|"
						+ UserInfoUtil.PASSPORT_TEST_KEY);
				if (md5Check.equalsIgnoreCase(md5Value)) {
					return passport;
				}
			}
		}
		return null;
	}

	/*
	 * 获取用户passport
	 * 
	 * *
	 */
	public String getPassport(HttpServletRequest request) {
		String passport = getPassportFromCookie(request);
		if (passport == null ? false : passport.length() >= 2) {
			passport = passport.toLowerCase();
		} else {
			passport = "";
		}

		return passport;
	}

	/*
	 * 获取referer *
	 */
	public String getReferer(HttpServletRequest request) {
		return request.getHeader("Referer");
	}

	/*
	 * 判断用户是否登录 *
	 */
//	public boolean isLogin(HttpServletRequest request) {
//		String passport = getPassport(request);
//		if (passport.trim().length() < 2)
//			return false;
//		return true;
//	}

	/*
	 * 获取当前用户信息 *
	 */
	public User getUser(HttpServletRequest request) {
		String passport = getPassport(request);
		if("".equals(passport)){
			return (User)request.getSession().getAttribute(UserInfoUtil.GLOBAL_SESSION_USR_CACHE_KEY);
		}
		if (passport.trim().length() < 2)
			return null;
		User user = null;
		try {
			user = (User) request.getAttribute(GLOBAL_REQ_USR_CACHE_KEY);
		} catch (Exception e) {
		}
		if (user != null)
			return user;
		try {
			user = userService.getUser(passport);
			if (user != null)
				request.setAttribute(GLOBAL_REQ_USR_CACHE_KEY, user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * 初始化用户所有权限--暂未使用，等待后期扩展
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	public Map<String, String> initUserPermissions(HttpServletRequest request,
			User user) {
		/**
		 * String passport = this.getPassport(request); String cacheKey =
		 * AdminConstants.UserCache.USER_PERMISSIONS_KEY + "_" + passport;
		 * Map<String, String> userpermissions = (Map<String, String>)
		 * memcached.get(cacheKey); if (userpermissions == null ||
		 * userpermissions.size() == 0) { userpermissions =
		 * getUserPermissions(user); memcached.set(cacheKey,
		 * AdminConstants.UserCache.USER_INFO_TIMEOUT, userpermissions); }
		 * return userpermissions;
		 */
		Map<String, String> userpermissions = new HashMap<String, String>();
		userpermissions.put("/user/login", "/user/login");
		userpermissions.put("/user/logout", "/user/logout");
		return userpermissions;
	}
	
	/*
	 * 获取用户ip *
	 */
	public String getRemoteIp(HttpServletRequest request) {
		String remoteip = request.getRemoteAddr();
		if (request.getHeader("X-Real-IP") != null) {
			remoteip = request.getHeader("X-Real-IP");
		}
		return remoteip;
	}

	public void setCookies(HttpServletRequest request,
			HttpServletResponse response, String passport) {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		String md5Check = MD5Util.MD5(passport + "|" + day + "|"
				+ UserInfoUtil.PASSPORT_TEST_KEY);
		ToolKit.setCookie(response, UserInfoUtil.GLOBAL_PASSPORT_COOKIE_KEY, passport
				+ "|" + md5Check, 315360000,
				UserInfoUtil.PASSPORT_COOKIE_DOMAIN);
	}

	public void clearCookie(HttpServletRequest request,
			HttpServletResponse response) {
		ToolKit.setCookie(response, UserInfoUtil.GLOBAL_PASSPORT_COOKIE_KEY, null, 0,
				UserInfoUtil.PASSPORT_COOKIE_DOMAIN);
	}

	/**
	 * 清cookies
	 * 
	 * @param request
	 * @param response
	 */
	public void clearCookies(HttpServletRequest request,
			HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		Cookie sCookie = null;
		for (int i = 0; i < cookies.length; i++) {
			sCookie = cookies[i];
			Cookie cookie = new Cookie(sCookie.getName(), null);
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}
}
