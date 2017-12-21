package com.douguo.dc.user;

import com.douguo.dc.user.model.User;
import com.douguo.dc.user.utils.UserInfoUtil;
import com.douguo.dc.util.DateUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PermissionFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException,
			IOException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String uri = req.getRequestURI();

		//未登录状态权限跳出
		if (uri.contains("/user/login.do") || uri.contains("/datashow/toRoastShow.do")|| uri.contains("/datashow/queryAllData.do")
				||uri.contains("/datashow/queryList.do")||uri.contains("/datashow/worldData.do")||uri.contains("/datashow/numData.do")||
				uri.contains("/datashow/toDouguoPersonShow.do")) {
			System.out.println(">>>>>>>>>>>>> unLogin permission jump ...");
			chain.doFilter(request, response);
		} else {
			HttpSession session = req.getSession(true);
			User user = (User) session.getAttribute(UserInfoUtil.GLOBAL_SESSION_USR_CACHE_KEY);
			
			if (user == null) {
				res.sendRedirect("/login.jsp");
			} else {
				// 登陆状态下权限跳出
				if (uri.contains("/test.do")){
					System.out.println(">>>>>>>>>>>>> logined permission jump ...");
					chain.doFilter(request, response);
				} else {

					System.out.println("=======uri~" + uri + "~user~" + user.getUid() + "~date~" + DateUtil.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
					boolean judge = false;

					List<Map<String, Object>> functions = user.getFunctions();

					for (Map<String, Object> map : functions) {
						if (map.get("uri").equals(uri)) {
							judge = true;
							break;
						}
					}

					if (judge) {
						chain.doFilter(request, response);
					} else {
						res.sendRedirect("/nopermission.jsp");
					}
				}
			}

		}
	}

	public void init(FilterConfig config) throws ServletException {
	}

	public void destroy() {
	}

}
