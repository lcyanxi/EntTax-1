<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ page import="com.douguo.dc.user.model.User,java.util.*"%>
<%@ include file="common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>登陆</title>
<link rel="stylesheet" href="${ctx}/styles/log.css" type="text/css" />
</head>
<script type="text/javascript">
	function loginCheck() {
		loginName = loginForm.uid.value;
		if (loginName.length == 0) {
			alert("请输入用户名");
			return false;
		}
		loginForm.submit();
	}
	//alert(self.location.href);
	if(top != self){  
		//alert(self.location.href);
	    top.location.href = self.location.href;  
	}  
</script>
<body>
	<div id="login">
		<div class="sysTitle">豆果DC后台</div>
		<div panel="a" class="ct">
			<div>登录系统</div>
			<br>
			<br> 
			<form name="loginForm" action="/user/login.do" method="post"
				onsubmit="return loginCheck();">
				用户名：<input name="uid" type="text" value="${uid}" /><br>
				密&nbsp;&nbsp;&nbsp;&nbsp;码：<input name="pwd" type="password" /><br>
				<br> <input type="submit" value="登陆">
			</form>
		</div>
	</div>
</body>
</html>