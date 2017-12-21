<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="zh">
<head>
<title>手机搜狐统计系统 New</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<link href="/static/css/cached_report.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/static/js/jquery-1.7.2.min.js"></script>
<script src="/static/js/i18n.js" type="text/javascript"></script>
<script src="/static/js/zh.js" type="text/javascript"></script>

</head>
<body class="report">
	<div id="doc">
		<div class="hd">
			<div class="userHeader clearfix">
				<div class="fr" style="display: block;">
					<span class="tip">欢迎,${sessionScope.auth.user.name}, <a href="/logout.do">登出</a></span>
				</div>
				<div class="logo">
					<span style="margin-left: 30px; font-size: 20px; color: white;">手机搜狐统计系统</span>
				</div>
			</div>
			<div class="globalNav">
				<div class="bd2">
					<div class="leftCol js-app">
						<div class="search_Apps js-search-Apps" style="display: none">
							<input type="text" id="search_apps">
						</div>
						<div class="app-select js-app-select">
							<h4 class="select-head custom">
								<c:set var="isOk" value="0"></c:set>
								<c:if test="${apps.size gt 0}">
									<c:forEach items="${apps.datas }" var="app">
										<c:if test="${app.id eq globalAppid}">
											<c:set var="isOk" value="1"></c:set>
											<div class="selected js-selected" value="${app.id }">
												<img alt="${app.name }" title="${app.name }" width="24" src="/static/img/android-small.png" />${app.name }
											</div>
										</c:if>
									</c:forEach>
								</c:if>
								<c:if test="${isOk == '0'}">
									<div class="selected js-selected" value="1">
										<img alt="游戏平台-android" title="游戏平台-android" width="24" src="/static/img/android-small.png" />游戏平台-android
									</div>
								</c:if>
								<b class="icon pulldown"></b>
							</h4>
						</div>
						<ul class="select-list js-select-list" style="display: none;">
							<c:if test="${apps.size gt 0}">
								<c:forEach items="${apps.datas }" var="app">
									<li app_id="${app.id }"><img alt="${app.name }" title="${app.name }" width="24" src="/static/img/android-small.png" />${app.name }</li>
								</c:forEach>
							</c:if>
						</ul>
					</div>
				</div>
			</div>

			<input id="app_id" attr-id="${globalAppid}" type="hidden" /> 
			<input id="app_ids" type="hidden" />
		</div>