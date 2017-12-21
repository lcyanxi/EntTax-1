<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<script type="text/javascript" src="/scripts/common/jquery.js"></script>
<link rel="stylesheet" href="${ctx}/styles/common.css" type="text/css">
<script>
	
	function  goinit(){
		
		var menubarSize = "${menubarSize}";
		var menutemplateID = "${menutemplateID}";
		var from = "${param.from}";
		alert(menubarSize);
		if(menubarSize==0){
			top.document.getElementById("mainFrameset").cols="0,*";
			if(menutemplateID==1){
				top.frames["main"].location.replace("${ctx}/pages/maint.html");
			}else if(menutemplateID==2){
				top.frames["main"].location.replace("${ctx}/pages/extend.html");
			}else if(menutemplateID==3){
				top.frames["main"].location.replace("${ctx}/pages/client_manage.html");
			}else if(menutemplateID==4){
				top.frames["main"].location.replace("${ctx}/pages/set_purview.html");
			}

			return;
		}else{
			top.document.getElementById("mainFrameset").cols="28%,*";
			if(menutemplateID==1){
				//top.frames["main"].location.replace("${ctx}/"+first);
			}else if(menutemplateID==2){
			  if(from != 'index') {
				  var url = "<%=request.getContextPath()%>/WebStats.do?method=getCookie";
				  //new Ajax.Request( url, {method: "post",onComplete: success } );
				//top.frames["main"].location.replace("${ctx}/WebStats.do?method=queryIndex");
			  }
			}else if(menutemplateID==3){
			  if(from != 'index') {
				//top.frames["main"].location.replace("${ctx}/customer.do?method=customerIndex");
			  }
			}else if(menutemplateID==4){
			  if(from != 'index') {
				top.frames["main"].location.replace("${ctx}/userManage.do?method=doGetUserList");
			  }
			}

		}
		var first = "${firstAction}";
		if(first=='shownull'){
			//alert('对不起，您无操作该项功能的权限！');
			return;
		}
		if(first!=""){
			top.frames["main"].location.replace("${ctx}/"+first);
		}
	}
	function success( response ){
		var value = response.responseText;
		var mainFrameset=top.document.getElementById("mainFrameset");
		if( value.indexOf("wztj")<0 ){
			mainFrameset.cols="0,*";
			top.frames["main"].location.replace("${ctx}/WebStats.do?method=tip");
		}else{
			top.frames["main"].location.replace("${ctx}/WebStats.do?method=queryIndex");
		}
	}
	function clickeds(tableid){
		if(document.getElementById(tableid).style.display=="none"){
			document.getElementById(tableid).style.display = '';
		}else{
			document.getElementById(tableid).style.display = 'none';
		}
	}
	$(document).ready(function(){
		$(".leftMenue .list li").click(function(){
			$(".leftMenue .list li").removeClass("over");
			$(".leftMenue .list li").removeClass("topOver");

			var m_class = $(this).attr("class");
			var re = new RegExp("top");
	 		if( re.test(m_class) ){
				$(this).addClass("topOver");
			}else{
				$(this).addClass("over");
			}
		});
	});

</script>
</head>
<body onload="goinit()">
<div class="leftMenue">
	<ul class="list">
		<c:forEach  items="${menus}" var="menu_1" varStatus="status">
			<li  
				<c:choose>
					<c:when test="${status.first}">
						class="top <c:if test="${ menu_1.itemid == _menu.itemid }">topOver</c:if>"
					</c:when>
					<c:otherwise>
						class="<c:if test="${ menu_1.itemid == _menu.itemid }">over</c:if>"
					</c:otherwise>
				</c:choose>
			>
			<a  target="main"
				<c:choose>
					<c:when test="${menu_1.strurl=='#'}">href="javaScript:void(0);"</c:when>
					<c:otherwise>href="${menu_1.strurl}"</c:otherwise>
				</c:choose>
				class="${menu_1.styles}"
				>
				<c:out value="${menu_1.itemname}"/>
			</a>
			</li>
		</c:forEach>
	</ul>
</div>
</body>
</html>
