<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.douguo.dc.util.PHPSerializer"%>
<%@ page language="java" import="com.douguo.dc.util.TypeUtil"%>
<%@ page language="java" import="com.douguo.dc.util.Cast"%>
<%@ page language="java" import="org.springframework.ui.ModelMap"%>

<jsp:include page="../includes/header_new.jsp"></jsp:include>

<script type="text/javascript">
	I18n.default_locale = "zh";
	I18n.locale = "zh";
	I18n.fallbacks = true;
</script>

<div class="bd clearfix">

	<div id="mainContainer" style="margin-left: 0px;">
		<div class="contentCol">

			<input type="hidden" value="active_users" id="action_stats" />

			<div class="operations">
				<div class="bd3">
					<div class="leftCol" style="width: 390px;">
						<div class="mod-select">
							<div class="tabpanel">
								<ul id="toptab" class="borders">
									<li class=<c:if test="${plat == 'JOB'}"> on</c:if>><a
										href="/hiveadmin/preList.do?startDate=${startDate}&endDate=${endDate}&plat=JOB">JOB管理</a></li>
									<li class=<c:if test="${plat == 'FOCUS'}"> on</c:if>><a
										href="/hiveadmin/preFocusList.do?startDate=${startDate}&endDate=${endDate}&plat=FOCUS">焦点图数据统计</a></li>
									<li class=<c:if test="${plat == 'AD'}"> on</c:if>><a
										href="/hiveadmin/preAdList.do?startDate=${startDate}&endDate=${endDate}&plat=AD">广告数据统计</a></li>
								</ul>
							</div>
						</div>
					</div>
					<div class="contentCol">

						<div class="filterPanel">
							广告id:&nbsp;&nbsp;&nbsp;<input type="text" onblur="window.global.renderPage();"  style="width: 30px" name="adId"
								id="adId" value="${adId}" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<div class="datepickerPanel custom1 borders">
								<div class="dateselect js-um-datepicker" id="date-select">
									<span class="start">${startDate}</span> - <span class="end">${endDate}</span>
									<b class="icon pulldown"></b>
								</div>
								<div id="proTemp2" style="display: none;">
									<div class="mod-body"></div>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>

			<div class="wrap-table">
				<div class="mod mod1 parent-table" id="active_user-detail-table">
					<div class="mod-header radius">
						<h2>
							广告数据统计 <a class="icon help poptips"
								action-frame="tip_activeTable" title=""></a>
						</h2>
						<div class="option" style="display: none;">
							<span class="icon export exportCsv" title="导出"></span>
						</div>
					</div>
					<div class="mod-body " id="data-load">
						<table class="data-load " width="100%" border="0" cellspacing="0">
							<thead>
								<tr>
									<th>统计日期</th>
									<th>pv</th>
									<th>点击数</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="focusData">
									<tr>
										<td width="20%"><c:out value="${focusData.statdate}" /></td>
										<td width="15%"><c:out value="${focusData.pv}" /></td>
										<td width="20%"><c:out value="${focusData.clicks}" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="mod-bottom clearfix">
						<div class="fr pagination"></div>
					</div>
				</div>
			</div>

			<div class="">
				<div id="tip_active">
					<div id="tip_activeTrend" class="tips">
						<div class="corner"></div>
						<p>每天网站和客户端搜索没有结果的关键词和数量，客户端可以根据某个标签搜索。</p>
					</div>
					<div id="tip_activeTable" class="tips">
						<div class="corner"></div>
						<p>TOP100排名</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="startDate" value="${startDate}" />
<input type="hidden" id="endDate" value="${endDate}" />
<input type="hidden" id="plat" value="${plat}" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>

<script type="text/javascript">
	//$(document).ready(function() { });
	window.global.renderPage = function() {
		var plat = $("#plat").val();
		var adId = $("#adId").val();
		var type = $("#type").val();
		var startDateShow = $(".start").html().replace(/\./g, "-");
		var endDateShow = $(".end").html().replace(/\./g, "-");
		if (adId != "" && (startDateShow != $("#startDate").val() || endDateShow != $("#endDate").val() || adId != "${adId}")) {
			strurl = "/hiveadmin/preAdList.do?startDate=" + startDateShow + "&endDate=" + endDateShow + "&plat=" + plat + "&adId=" + adId;
			window.location.href = strurl;
		}
	}
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>