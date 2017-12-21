package com.douguo.dc.common.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

import com.douguo.dc.user.common.UserConstants;
import com.douguo.dc.user.model.User;
import com.douguo.dc.user.utils.UserInfoUtil;

/**
 * BaseController
 * 
 * @author zhangyaozhou
 */
public class BaseController extends DispatcherServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3482836686398612893L;
	public static String LOG_FORMATTER = "ServerName:[{}] ACTION:{},PASSPORT:{},PARAMS:{}.";
	public static String ERROR_LOG_FORMATTER = "ServerName:[%s] ACTION:%s,PASSPORT:%s,PARAMS:%s,ERROR MSG:%s.";

	protected static ThreadLocal<User> userLocal = new ThreadLocal<User>();

	protected UserInfoUtil userInfoUtil = UserInfoUtil.getInstance();

	private static final Logger log = LoggerFactory
			.getLogger(BaseController.class);

	@Override
	protected void doDispatch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 脱离权限控制-外部调用地址(URL以/api/开始)
		if (this.leapAccessControl(request)) {
			super.doDispatch(request, response);
			return;
		}

		userLocal.set(null);

		User user = this.getUser(request);

		String requestURI = "";
		// 获取到用户，用户登录
		if (user != null) {
			userLocal.set(user);
			super.doDispatch(request, response);
		} else {
			if ((requestURI = getAccessbleURI(request, user)) != null) {
				super.doDispatch(request, response);
			} else {
				// 正式环境跳转登陆页面
				response.sendRedirect(UserConstants.GLOBAL_WEB_LOGIN_URL);
			}
		}

		return;
	}

	public User getUser(HttpServletRequest request) {
		User curUser = userLocal.get();
		if (curUser == null) {
			return userInfoUtil.getUser(request);
		} else {
			return curUser;
		}
	}

	/**
	 * 清除用户信息
	 * 
	 * @param request
	 */
	// protected void clearUserInfo(HttpServletRequest request) {
	// String userName = this.getUserName(request);
	// setCache(AdminConstants.UserCache.USER_INFO_KEY + "_" + passport, 0,
	// null);
	// setCache(AdminConstants.UserCache.USER_MENU_KEY + "_" + passport, 0,
	// null);
	// }

	/**
	 * 脱离权限控制-外部API调用地址(URI以/api/开始)
	 * 
	 * @param request
	 * @return 是外部调用地址返回true
	 */
	private boolean leapAccessControl(HttpServletRequest request) {
		return StringUtils
				.startsWithIgnoreCase(request.getRequestURI(), "/api");
	}

	public static void main(String[] args) {
		boolean flag = isInnerIP("10.10.68.50");
		System.out.println("flag : " + flag);
	}

	public static boolean isInnerIP(String ipAddress) {
		boolean isInnerIp = false;
		long ipNum = getIpNum(ipAddress);
		/**
		 * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类
		 * 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
		 **/
		long aBegin = getIpNum("10.0.0.0");
		long aEnd = getIpNum("10.255.255.255");
		long bBegin = getIpNum("172.16.0.0");
		long bEnd = getIpNum("172.31.255.255");
		long cBegin = getIpNum("192.168.0.0");
		long cEnd = getIpNum("192.168.255.255");
		isInnerIp = isInner(ipNum, aBegin, aEnd)
				|| isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd)
				|| ipAddress.equals("127.0.0.1");
		return isInnerIp;
	}

	private static long getIpNum(String ipAddress) {
		String[] ip = ipAddress.split("\\.");
		long a = Integer.parseInt(ip[0]);
		long b = Integer.parseInt(ip[1]);
		long c = Integer.parseInt(ip[2]);
		long d = Integer.parseInt(ip[3]);

		long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
		return ipNum;
	}

	private static boolean isInner(long userIp, long begin, long end) {
		return (userIp >= begin) && (userIp <= end);
	}

	/**
	 * 错误页面
	 * 
	 * @param title
	 * @param txt
	 * @return
	 */
	public ModelAndView err(String title, String txt) {
		ModelAndView modeAndView = new ModelAndView();
		try {
			modeAndView.setViewName("redirect:/err.jsp?t="
					+ java.net.URLEncoder.encode(title, "gbk") + "&c="
					+ java.net.URLEncoder.encode(txt, "gbk"));
		} catch (Exception e) {
		}
		return modeAndView;
	}

	public ModelAndView errForbidden() {
		ModelAndView modeAndView = new ModelAndView();
		modeAndView.setViewName("redirect:/err403.jsp");
		return modeAndView;
	}

	public ModelAndView error404() {
		ModelAndView modeAndView = new ModelAndView();
		modeAndView.setViewName("redirect:/err404.jsp");
		return modeAndView;
	}

	public String jsonOutput(int status, String statusText, Object data)
			throws JSONException {
		JSONObject ret = new JSONObject();
		ret.put("status", status);
		ret.put("statusText", statusText);
		ret.put("data", data);
		return ret.toString();
	}

	private String getAccessbleURI(HttpServletRequest request, User user) {
		// 获取用户权限
		Map<String, String> userpermissions = userInfoUtil.initUserPermissions(
				request, user);
		if (userpermissions != null && userpermissions.size() > 0) {
			// 用户访问URL
			String requestURI = request.getRequestURI();
			String contextPath = request.getContextPath();
			requestURI = requestURI.replaceFirst(contextPath, "");
			// login可以访问
			if (requestURI.equals("/user/login")
					|| requestURI.equals("/user/logout")
					|| requestURI.equals("/user/qqlogin")
					|| requestURI.equals("/user/afterqqlogin")) {
				return requestURI;
			}
			// 是否可以访问
			String pattern = getPatterByRequestURI(userpermissions, requestURI);
			if (pattern != null && getMatchedParameters(pattern, requestURI)) {
				return requestURI;
			}
		}
		return null;
	}

	private String getPatterByRequestURI(Map<String, String> userpermissions,
			String requestURI) {
		String pattern = userpermissions.get(requestURI);
		if (StringUtils.isNotEmpty(requestURI) && StringUtils.isEmpty(pattern)) {
			String subRquestURI = requestURI.substring(0,
					requestURI.lastIndexOf("/"));
			return getPatterByRequestURI(userpermissions, subRquestURI);
		}
		return pattern;
	}

	// 正则匹配
	public static Map<String, Pattern> PATTERN_MAP = new HashMap<String, Pattern>();

	// 匹配URL
	public static boolean getMatchedParameters(String pattern, String url) {
		Pattern p = PATTERN_MAP.get(pattern);
		if (p == null) {
			p = Pattern.compile(pattern);
			PATTERN_MAP.put(pattern, p);
		}
		Matcher m = p.matcher(url);
		return m.matches();
	}

	protected void doOutputJson(HttpServletResponse response, String json) {
		PrintWriter writer = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=UTF-8");
			writer = response.getWriter();
			writer.print(json);
			writer.flush();
		} catch (IOException e) {
			logger.error("out put tab json error: " + e);
		} finally {
			try {
				writer.close();
			} catch (Exception e2) {
				logger.error(e2 + "");
			}
		}
	}

	/*
	 * 获取用户ip *
	 */
	public String getRemoteIp(HttpServletRequest request) {
		return userInfoUtil.getRemoteIp(request);
	}

	/**
	 * 抽象日志方法
	 * 
	 * @param request
	 * @param model
	 * @param action
	 * @param params
	 */
	protected void actionLog(HttpServletRequest request, String model,
			String action, String params) {
		log.info(BaseController.LOG_FORMATTER, new Object[] { model, action,
				getUsrLogInfo(request), params });
	}

	protected void actionErrorLog(HttpServletRequest request, String model,
			String action, String params, String errorMsg, Throwable e) {
		if (StringUtils.isBlank(errorMsg)) {
			errorMsg = e.getMessage();
		}
		log.error(String.format(BaseController.ERROR_LOG_FORMATTER, model,
				action, getUsrLogInfo(request), params, errorMsg), e);
	}

	/**
	 * 获取用户日志信息
	 * 
	 * @param request
	 * @return
	 */
	protected String getUsrLogInfo(HttpServletRequest request) {
		String remoteIp = this.getRemoteIp(request);
		User usr = getUser(request);
		if (usr == null) {
			return "usr is null";
		}
		return remoteIp + "_" + usr.getId() + "_" + usr.getUid() + "_"
				+ usr.getUsername();
	}
}
