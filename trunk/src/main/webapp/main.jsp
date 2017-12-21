<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import ="com.douguo.dc.user.model.User" %>
<%@ page import ="com.douguo.dc.user.utils.UserInfoUtil" %>
<%@ include file="common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	User obj = (User) session.getAttribute(UserInfoUtil.GLOBAL_SESSION_USR_CACHE_KEY);
	if (obj == null) response.sendRedirect("login.jsp");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!--meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" /-->
<title>Douguo DC System 1.0</title>
<script type="text/javascript" src="/scripts/common/jquery.js"></script>
<script type="text/javascript" src="/scripts/common/common.js"></script>
<link type="text/css" href="/styles/common.css" rel="stylesheet" />
</head>

<frameset rows="107,*,27" cols="*" frameborder="no"  framespacing="0" border="0" name="mainall">
	<frame src="/common/top.jsp" name="topFrame" scrolling="NO" noresize marginwidth="0" marginheight="0"/>
	<frameset id="mainFrameset" name="mainFrameset" cols="217,*" frameborder="no" border="1" framespacing="0" >
		<frame id="leftFrame" src="/common/leftmenu.jsp" name="leftFrame" scrolling="auto" noresize marginwidth="0" marginheight="0"/>
		<frame id="main" src="/user/preEditPass.do" name="main" scrolling="yes" noresize marginwidth="0"/>
	</frameset>
	<frame src="/common/bottom.jsp" name="bottomFrame" scrolling="NO" noresize marginwidth="0" marginheight="0"/>
</frameset>
<noframes></noframes>
</html>
