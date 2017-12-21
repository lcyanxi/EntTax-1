package com.douguo.dc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sohu.cas.client.AuthProxy;
import com.sohu.cas.client.AuthenticationBase;

public class PermissionsInterceptor implements HandlerInterceptor {

	private AuthenticationBase auth = null;

	private static Log logger = LogFactory.getLog(PermissionsInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		// 截取URI
		String url = request.getRequestURI();
		
		String loginPath = "http://" + request.getServerName() + ":" + request.getServerPort() + "/index.do";
		String getPermissonPath = "http://" + request.getServerName() + ":" + request.getServerPort() + "/get_permisson.html";

		// 判断是否登陆，没有登陆则跳到登陆页面
		auth = (AuthenticationBase) request.getSession().getAttribute("auth");
		String ticket = request.getParameter("cas_ticket");
		if (auth != null) {
			// continue
		} else if (ticket != null) {
			try {
				auth = AuthProxy.getInstance().authenticateByTicket(ticket);
				request.getSession().setAttribute("auth", auth);
			} catch (Exception e) {
				logger.warn("ticket 无效 !");
			}
			if (null == auth) {
				response.sendRedirect("http://cas.cms5.sohu.com/sso/login/?ret_url=" + loginPath);
				return false;
			}
		} else {
			response.sendRedirect("http://cas.cms5.sohu.com/sso/login/?ret_url=" + loginPath);
			return false;
		}

		// 系统管理
		if (url.startsWith("/manage/")) {
			if (auth.isShow("sohu.wireless.maa.app.manage")) {
				return true;
			}
			response.sendRedirect("/error/err.jsp");
			return false;
		}
		
		// 判断是否有权限
		if (auth.isShow("sohu.wireless.maa.app.overview")) {
			return true;
		} else {
			response.sendRedirect(getPermissonPath);
			return false;
		}
		


	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
