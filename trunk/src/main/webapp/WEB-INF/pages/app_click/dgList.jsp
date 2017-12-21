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
					<div class="leftCol" style="width: 740px;">
						<div class="mod-select">
							<div class="tabpanel">
								<ul id="toptab" class="borders">
									<li class=<c:if test="${page == '1'}"> on</c:if>><a
										href="/appclick/dglist.do?startDate=${startDate}&endDate=${endDate}&page=1">食谱</a></li>
									<li class=<c:if test="${page == '2'}"> on</c:if>><a
										href="/appclick/dglist.do?startDate=${startDate}&endDate=${endDate}&page=2">优食汇</a></li>
									<li class=<c:if test="${page == '11'}"> on</c:if>><a
										href="/appclick/dglist.do?startDate=${startDate}&endDate=${endDate}&page=11">圈圈</a></li>
									<li class=<c:if test="${page == '3'}"> on</c:if>><a
										href="/appclick/dglist.do?startDate=${startDate}&endDate=${endDate}&page=3">主页活动</a></li>
									<li class=<c:if test="${page == '4'}"> on</c:if>><a
										href="/appclick/dglist.do?startDate=${startDate}&endDate=${endDate}&page=4">未登左屏</a></li>
									<li class=<c:if test="${page == '5'}"> on</c:if>><a
										href="/appclick/dglist.do?startDate=${startDate}&endDate=${endDate}&page=5">已登左屏</a></li>
									<li class=<c:if test="${page == '6'}"> on</c:if>><a
										href="/appclick/dglist.do?startDate=${startDate}&endDate=${endDate}&page=6">菜谱详情</a></li>
									<li class=<c:if test="${page == '7'}"> on</c:if>><a
										href="/appclick/dglist.do?startDate=${startDate}&endDate=${endDate}&page=7">商品详情</a></li>
									<li class=<c:if test="${page == '8'}"> on</c:if>><a
										href="/appclick/dglist.do?startDate=${startDate}&endDate=${endDate}&page=8">IOS主页</a></li>
								</ul>
							</div>
						</div>
					</div>
					<div class="contentCol" style="margin-left: 730px;">
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
							统计 <a class="icon help poptips" action-frame="tip_activeTable"
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
									<th>page</th>
									<th>统计项</th>
									<th>点击数</th>
									<th>人数</th>
									<th>统计日期</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="appclick">
									<tr>
										<td><c:out value="${appclick.page}" /></td>
										<c:set var="key" value="${appclick.page}_${appclick.view}"></c:set>
										<td title="${key}" val="<c:out value="${appclick.view}" /> ">
											<a  href="/appclick/dgpolist.do?startDate=${appclick.statDate}&endDate=${appclick.statDate}&page=${appclick.page}&view=${appclick.view}"><c:out value="${mapClickDict[key]}" /></a>
										</td>
										<td><c:out value="${appclick.clicks}" /></td>
										<td><c:out value="${appclick.uvs}" /></td>
										<td><c:out value="${appclick.statdate}" /></td>
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
					<div id="tip_activeTable" class="tips">
						<div class="corner"></div>
						<p>统计</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="startDate" value="${startDate}" />
<input type="hidden" id="endDate" value="${endDate}" />
<input type="hidden" id="page" value="${page}" />
<input type="hidden" id="view" value="${view}" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						global.renderPage = function() {
							page = $("#page").val();
							view = $("#view").val();
							startDateShow = $(".start").html().replace(/\./g,
									"-");
							endDateShow = $(".end").html().replace(/\./g, "-");
							if (startDateShow != $("#startDate").val()
									|| endDateShow != $("#endDate").val()) {
								window.location.href = "/appclick/dglist.do?startDate="
										+ startDateShow
										+ "&endDate="
										+ endDateShow
										+ "&page="
										+ page
										+ "&view=" + view;
							}
						}
					});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>