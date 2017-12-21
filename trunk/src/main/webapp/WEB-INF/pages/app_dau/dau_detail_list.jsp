<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
					<div class="leftCol" style="width: 590px;">
						<div class="mod-select">
							<div class="tabpanel">
								<ul id="toptab" class="borders">
									<li class=<c:if test="${app == '3'}"> on</c:if>><a
											href="/appdau/daulist.do?startDate=${startDate}&endDate=${endDate}&app=${app}">返回</a></li>

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
							渠道日活统计 <a class="icon help poptips"
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
								<th>渠道</th>
								<th>日活数</th>
								<th>作品数</th>
								<th>主贴数</th>
								<th>回复数</th>
								<th>查看菜谱数</th>
								<th>订单数</th>
								<th>成交数</th>
								<th>质量</th>
								<th>用户贡献度</th>
								<th>分数</th>
								<th>统计日期</th>
							</tr>
							</thead>
							<tbody id="data-list">
							<c:forEach items="${rowlst}" var="appdau" varStatus="ast">
								<c:if test="${ast.first}">
									<c:set var="scores" value='100' />
									<c:set var="frs" value='${appdau.rs}' />
									<c:set var="fst_date" value='${appdau.statdate}' />
								</c:if>
								<c:set var="lst_date" value='${appdau.statdate}' />
								<c:if test="${!ast.first}">
									<c:if test="${fst_date != lst_date}">
										<c:set var="scores" value='100' />
										<c:set var="frs" value='${appdau.rs}' />
										<c:set var="fst_date" value='${appdau.statdate}' />
									</c:if>
									<c:if test="${fst_date == lst_date}">
										<c:set var="scores" value='${(appdau.rs * 100) /frs}' />
									</c:if>
								</c:if>

								<tr>
									<td><c:out value="${appdau.channel}" /></td>
									<td><c:out value="${appdau.uid}" /></td>
									<td><c:out value="${appdau.dishs}" /></td>
									<td><c:out value="${appdau.posts}" /></td>
									<td><c:out value="${appdau.replys}" /></td>
									<td><c:out value="${appdau.view_cooks}" /></td>
									<td><c:out value="${appdau.orders}" /></td>
									<td><c:out value="${appdau.pays}" /></td>
									<td><c:out value="${appdau.cs}" /></td>
									<td><c:out value="${appdau.rs}" /></td>
									<td><fmt:formatNumber value="${scores}" pattern="##" type="number"/></td>
									<td><c:out value="${appdau.statdate}" /></td>
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
						<p>日活统计</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="startDate" value="${startDate}" />
<input type="hidden" id="endDate" value="${endDate}" />
<input type="hidden" id="app" value="${app}" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
		type="text/javascript"></script>

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						global.renderPage = function() {
							app = $("#app").val();
							startDateShow = $(".start").html().replace(/\./g,
									"-");
							endDateShow = $(".end").html().replace(/\./g, "-");
							if (startDateShow != $("#startDate").val()
									|| endDateShow != $("#endDate").val()) {
								window.location.href = "/appdau/daudetaillist.do?startDate="
										+ startDateShow
										+ "&endDate="
										+ endDateShow + "&app=" + app;
							}
						}
					});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>