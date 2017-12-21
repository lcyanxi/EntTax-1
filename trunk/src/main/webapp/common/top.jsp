<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import ="com.douguo.dc.user.model.User" %>
<%@ page import ="com.douguo.dc.user.utils.UserInfoUtil" %>
<%@ include file="taglibs.jsp"%>
<%
	User obj = (User) session.getAttribute(UserInfoUtil.GLOBAL_SESSION_USR_CACHE_KEY);
	String mod = "main";
	String file = "";
	String name = "guest";
	String strUserName = "";
	if(obj!=null){
        	strUserName = obj.getUsername();	
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Douguo DC system 1.0 </title>
<link type="text/css" href="/styles/common.css" rel="stylesheet" />
	<script type="text/javascript">		
	function  goinit(){
		//top.document.getElementById("mainFrameset").cols="0,*";
	}
	    function openMangeIndex() {
	        var mainFrameset = top.document.getElementById("mainFrameset");
	        //mainFrameset.cols = "0,*";
	        top.frames["main"].location.replace("/user/preEditPass.do");
	    }
		
		function showSupportStaffManage(){
			//top.frames["main"].location.replace("${ctx}/supportStaff.do?method=searchSupportStaff");
			//top.frames["leftFrame"].location.replace("${ctx}/menu.do?method=showMenus&menutemplateID=1&autoshow=yes");
		}
		
		function showPersonalSettings(){
			//top.frames["main"].location.replace("${ctx}/personalSettings.do?method=personalSettingView");
			//top.frames["leftFrame"].location.replace("${ctx}/menu1.jsp");	
			//top.frames["leftFrame"].location.replace("${ctx}/menu.do?method=showMenus&menutemplateID=5&autoshow=yes");
		}
		
		function showCKGL(){
			//top.frames["leftFrame"].location.replace("${ctx}/menu.do?method=showMenus&menutemplateID=2&autoshow=yes");
		}
		
		function showYWGL(){
			//top.frames["leftFrame"].location.replace("${ctx}/menu.do?method=showMenus&menutemplateID=3&autoshow=yes");
		}
		
		function showFZXS(){
			//top.frames["leftFrame"].location.replace("${ctx}/menu.do?method=showMenus&menutemplateID=4&autoshow=yes");
		}
		
		function displayMenu(obja){
	 
			var arryA = document.getElementsByTagName("A");
			
			for(var att = 0;att < arryA.length; att++){
				if(arryA[att].name == "display_menu"){
					arryA[att].className="";
				}
			}
			obja.className="avon";
		}
	</script>
</head>
<body onload="goinit()">
<title>index.html</title>	
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="this is my page">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<div class="headerBg">
	<div class="header">
    		<div class="leftBg">&nbsp;</div>
		<h1><span>Qunar Operation System 2.0 </span></h1>
		<div class="headerCon">
			<p class="subnav">
				<a>您好，<%=strUserName%>！&nbsp;|</a>
				<a href="/common/logout.jsp">退出</a>
				<a href="#" class="yhzn" onclick="javascript:alert('完善中...');">用户指南</a>
			</p>
		</div>
	</div>
</div>
<div class="nav">
	<div class="navL">
		<div class="navR">
			<ul>
				<li><a name="display_menu" href="#" onclick="openMangeIndex();displayMenu(this)" class="avon">首页</a></li>
				<li><a name="display_menu" href="#" onclick="showPersonalSettings();displayMenu(this)">系统设置</a></li>
			</ul>
		</div>
	</div>
</div>
</body>
</html>
