<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="com.sohu.cas.client.AuthenticationBase"%>

<div id="leftColContainer">
	<div class="leftCol">
		<div id="siderNav">
			<ul class="nav-items">
				<li class="nav-item item-top <%if ("overview".equals(request.getParameter("menu1"))) {%> current-item on <%}%>"><span><a href="/overview.do?appId=${globalAppid}" page_id="AppSummary"><b class="icon item-1"></b>概况</a></span></li>

				<li class="nav-item <%if ("app_trend".equals(request.getParameter("menu1"))) {%> current-item on <%}%>"><span><a><b class="icon item-2"></b>应用趋势</a></span>
					<ul class="sub-list">
						<li><a href="/trends/newuser.do?appId=${globalAppid }" <%if ("Install".equals(request.getParameter("menu2"))) {%> class="current-item" <%}%> page_id="Install">新增用户</a></li>
						<li><a href="/trends/activeuser.do?appId=${globalAppid }" <%if ("Active".equals(request.getParameter("menu2"))) {%> class="current-item" <%}%> page_id="Active">活跃用户</a></li>
						<li><a href="/trends/retention.do?appId=${globalAppid }" <%if ("Retention".equals(request.getParameter("menu2"))) {%> class="current-item" <%}%> page_id="Retention">留存用户</a></li>
						<li><a href="/trends/launch.do?appId=${globalAppid }" <%if ("Launch".equals(request.getParameter("menu2"))) {%> class="current-item" <%}%> page_id="Launch">启动次数</a></li>
					</ul>
				</li>

				<li class="nav-item <%if ("user_online".equals(request.getParameter("menu1"))) {%> current-item on <%}%>"><span><a><b class="icon item-3"></b>用户参与度</a></span>
					<ul class="sub-list">
						<li><a href="/userengagement/nav/duration.do?appId=${globalAppid }" <%if ("Duration".equals(request.getParameter("menu2"))) {%> class="current-item" <%}%>  page_id="Duration">使用时长</a></li>
						<li><a href="/userengagement/nav/frequency.do?appId=${globalAppid }" <%if ("Frequency".equals(request.getParameter("menu2"))) {%> class="current-item" <%}%>  page_id="Frequency">使用频率</a></li>
					</ul>
				</li>

				<li class="nav-item <%if ("page_function".equals(request.getParameter("menu1"))) {%> current-item on <%}%>"><span><a><b class="icon item-6"></b>功能使用</a></span>
					<ul class="sub-list">
						<li><a href="/events/dashboard.do?appId=${globalAppid }" <%if ("Event".equals(request.getParameter("menu2"))) {%> class="current-item" <%}%>   page_id="Event">自定义事件</a></li>
					</ul>
				</li>


				<li class="nav-item <%if ("Terminal".equals(request.getParameter("menu1"))) {%> current-item on <%}%>"><span><a><b class="icon item-5"></b>终端属性</a></span>
					<ul class="sub-list">
						<li><a href="/terminal/device.do?appId=${globalAppid }&which=devices" <%if ("Device".equals(request.getParameter("menu2"))) {%> class="current-item" <%}%> page_id="Device">设备</a></li>
						<li><a href="/terminal/device.do?appId=${globalAppid }&which=network" <%if ("Network".equals(request.getParameter("menu2"))) {%> class="current-item" <%}%> page_id="Network">网络及运营商</a></li>
						<li><a href="/terminal/device.do?appId=${globalAppid }&which=location" <%if ("Location".equals(request.getParameter("menu2"))) {%> class="current-item" <%}%> page_id="Location">地域</a></li>
					</ul>
				</li>
				

				<li class="nav-item  <%if ("ErrorAnalysis".equals(request.getParameter("menu1"))) {%> current-item on <%}%>"><span> <a href="/erroranalysis/errors.do?appId=${globalAppid }" page_id="Errors"><b class="icon item-7"></b>错误分析</a></span></li>
				
				<%if(  ((AuthenticationBase)session.getAttribute("auth")).isShow("sohu.wireless.maa.app.manage")){ %>
				<li class="nav-item item-bottom <%if ("manage".equals(request.getParameter("menu1"))) {%> current-item on <%}%>"><span><a><b class="icon item-1"></b>系统管理</a></span>
					<ul class="sub-list">
						<li><a href="/manage/qudao/index.do" <%if ("Qudao".equals(request.getParameter("menu2"))) {%> class="current-item" <%}%> page_id="Qudao">渠道管理</a></li>
						<li><a href="/manage/events/index.do" <%if ("Events".equals(request.getParameter("menu2"))) {%> class="current-item" <%}%> page_id="Events">事件管理</a></li>
						<li><a href="/manage/version/index.do" <%if ("Version".equals(request.getParameter("menu2"))) {%> class="current-item" <%}%> page_id="Version">版本管理</a></li>
						<li><a href="/manage/app/index.do" <%if ("App".equals(request.getParameter("menu2"))) {%> class="current-item" <%}%> page_id="App">应用管理</a></li>
					</ul>
				</li>
				<%} %>
				
			</ul>
		</div>
		<div id="siderDownLoad">
			<div class="sdkdown"></div>
		</div>
	</div>
</div>
