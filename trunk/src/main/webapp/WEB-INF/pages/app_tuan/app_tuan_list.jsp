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
					<div class="leftCol" style="width: 490px;">
						<div class="mod-select">
							<div class="tabpanel">
								<ul id="toptab" class="borders">
									
									<li class=<c:if test="${plat == 'CHART'}"> on</c:if>><a
										href="/apptuan/list.do?startDate=${startDate}&endDate=${endDate}&plat=CHART">优食汇订单漏斗图</a></li>
									<li class=<c:if test="${plat == 'LIST'}"> on</c:if>><a
										href="/apptuan/mapList.do?startDate=${startDate}&endDate=${endDate}&plat=LIST">优食汇列表数据</a></li>
									<li class=<c:if test="${plat == 'LIST'}"> on</c:if>><a
										href="/apptuan/productList.do?startDate=${startDate}&endDate=${endDate}&plat=PRODUCT">优食汇单品点击数据</a></li>
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
							团购数据统计 <a class="icon help poptips"
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
									<th>特卖会列表pv</th>
									<th>特卖会列表uv</th>
									<th>商品浏览pv</th>
									<th>商品浏览uv</th>
									<th>下单数</th>
									<th>点击支付数</th>
									<th>支付成功数</th>
									<th>统计日期</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="apptuan">
									<tr>
										<td width="15%"><c:out value="${apptuan.list_pv}" /></td>
										<td width="15%"><c:out value="${apptuan.list_uv}" /></td>
										<td width="10%"><c:out value="${apptuan.detail_pv}" /></td>
										<td width="10%"><c:out value="${apptuan.detail_uv}" /></td>
										<td width="10%"><c:out value="${apptuan.order_pv}" /></td>
										<td width="10%"><c:out value="${apptuan.pay_sign_pv}" /></td>
										<td width="12%" <c:if test="${apptuan.pay_sign_pv <= apptuan.pay_success_pv }">style="color:red;"</c:if> ><c:out value="${apptuan.pay_success_pv}" /></td>
										<td width="18%" ><c:out value="${apptuan.statdate}" /></td>
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
	$(document).ready(
			function() {

				global.renderPage = function() {
					plat = $("#plat").val();
					startDateShow = $(".start").html().replace(/\./g, "-");
					endDateShow = $(".end").html().replace(/\./g, "-");
					if (startDateShow != $("#startDate").val()
							|| endDateShow != $("#endDate").val()) {
						window.location.href = "/apptuan/list.do?startDate="
								+ startDateShow + "&endDate=" + endDateShow
								+ "&plat=" + plat;
					}
				}
			});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>