<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import ="com.douguo.dc.user.utils.UserInfoUtil" %>
<%@ page import ="com.douguo.dc.user.common.UserConstants" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<%
	session.removeAttribute(UserInfoUtil.GLOBAL_SESSION_USR_CACHE_KEY);
	//response.sendRedirect("../login.jsp");
	response.sendRedirect(UserConstants.GLOBAL_WEB_LOGIN_URL);
%>
</body>
</html>
