<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import ="com.douguo.dc.user.model.User" %>
<%@ page import ="com.douguo.dc.user.utils.UserInfoUtil" %>
<%@ page import ="com.douguo.dc.user.common.UserConstants" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/styles/log.css" type="text/css" />
<title>修改密码</title>
<script type="text/javascript">
function editCheck()
{
	pass= editForm.pass.value;
	passSec = editForm.passSec.value;

	if(pass.length>0 && passSec.length>0){
		if(pass == passSec)	{
			editForm.submit();
			alert("密码修改成功~");
		}
		else{
			alert("两次新密码输入不一致，请重新输入！");
			return false;
		}
	}
	else{
		alert("新密码不能为空");
		return false;
	}
}
</script>
</head>
<body>
<% 
	User obj = (User) session.getAttribute(UserInfoUtil.GLOBAL_SESSION_USR_CACHE_KEY);
	if (obj == null) response.sendRedirect(UserConstants.GLOBAL_WEB_LOGIN_URL);
%>
<div>
	<!-- jsp:include page="top.jsp" flush="true"/> -->
	<!-- jsp:include page="leftmenu.jsp" flush="true"/> -->
	<div id="RightList">
		<div >============== 用户信息管理 ==============</div>
		<br/>
		<form name="editForm" action="/user/editPass.do" method="post" onsubmit="return editCheck();">
		新&nbsp;&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;&nbsp;&nbsp;码：<input name="pass" type="password"><br/><br/>
		新密码确认：<input name="passSec" type="password"><br/><br/>
		<input type="submit" value="修改" class="submit" >
		</form>
	</div>
</div>
</body>
</html>