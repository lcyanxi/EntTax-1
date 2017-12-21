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
					<div class="leftCol">
						<div class="mod-select">
							<div class="tabpanel">
								<ul id="toptab" class="borders">
									<li class=<c:if test="${type == 'nokeyword'}"> on</c:if>><a
										href="/aizhan/ranklist.do?startDate=${startDate}&endDate=${endDate}&type=nokeyword">百度无排名</a></li>
									<li class=<c:if test="${type == 'lowrank'}"> on</c:if>><a
										href="/aizhan/ranklist.do?startDate=${startDate}&endDate=${endDate}&type=lowrank">百度低排名</a></li>
								</ul>
							</div>
						</div>
					</div>
					<div class="contentCol">

						<div class="filterPanel">
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
							<c:if test="${type == 'nokeyword' }">无排名关键词TOP100</c:if>
							<c:if test="${type == 'lowrank' }">低排名关键词TOP100</c:if>
							<a class="icon help poptips" action-frame="tip_activeTable"
								title=""></a>
						</h2>
						<div class="option" style="display: none;">
							<span class="icon export exportCsv" title="导出"></span>
						</div>
					</div>
					<div class="mod-body " id="data-load">
						<table class="data-load " width="100%" border="0" cellspacing="0">
							<thead>
								<tr>
									<th>关键词</th>
									<th>搜索量</th>
									<c:if test="${type == 'lowrank' }">
									<th>最高排名</th>
									<th>豆果排名</th>
									</c:if>
									<th>统计日期</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="aizhan">
									<tr>
										<td><a href="http://www.baidu.com/s?wd=<c:out value="${aizhan.keyword}" />&rsv_spt=1&issp=1&rsv_bp=0&ie=utf-8&tn=baiduhome_pg&inputT=1453" target="_blank"><c:out value="${aizhan.keyword}" /></a></td>
										<td><c:out value="${aizhan.querys}" /></td>
										<c:if test="${type == 'lowrank' }">
										<td><c:out value="${aizhan.rank}" /></td>
										<td><c:out value="${aizhan.dgrank}" /></td>
										</c:if>
										<td><c:out value="${aizhan.statdate}" /></td>
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
<input type="hidden" id="type" value="${type}" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>

<script type="text/javascript">
	$(document).ready(
			function() {

				global.renderPage = function() {
					type = $("#type").val();
					startDateShow = $(".start").html().replace(/\./g, "-");
					endDateShow = $(".end").html().replace(/\./g, "-");
					if (startDateShow != $("#startDate").val()
							|| endDateShow != $("#endDate").val()) {
						window.location.href = "/aizhan/ranklist.do?startDate="
								+ startDateShow + "&endDate=" + endDateShow
								+ "&type=" + type;
					}
				}
			});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>