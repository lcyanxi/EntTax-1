<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.douguo.dc.util.PHPSerializer"%>
<%@ page language="java" import="com.douguo.dc.util.TypeUtil"%>
<%@ page language="java" import="com.douguo.dc.util.Cast"%>
<%@ page language="java" import="org.springframework.ui.ModelMap"%>

<jsp:include page="../../includes/header_new.jsp"></jsp:include>

<script type="text/javascript">
	I18n.default_locale = "zh";
	I18n.locale = "zh";
	I18n.fallbacks = true;
</script>

<div class="bd clearfix" style="overflow:auto">

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
										href="/apptuan/mallOrderDayStatList.do?startDate=${startDate}&endDate=${endDate}&plat=CHART">电商销售日报</a></li>
									<!--
									<li class=<c:if test="${plat == 'LIST'}"> on</c:if>><a
										href="/apptuan/mapList.do?startDate=${startDate}&endDate=${endDate}&plat=LIST">销售销售月报</a></li>
									<li class=<c:if test="${plat == 'LIST'}"> on</c:if>><a
										href="/apptuan/productList.do?startDate=${startDate}&endDate=${endDate}&plat=PRODUCT">电商销售年报</a></li>
									-->
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
							电商销售日报 <a class="icon help poptips"
								action-frame="tip_activeTable" title=""></a>
						</h2>
						<div class="option" style="display: none;">
							<span class="icon export exportCsv" title="导出"></span>
						</div>
					</div>
					<div class="mod-body " id="data-load">
						<table class="data-load " width="2000px" border="0" cellspacing="0">
							<thead>
								<tr>
									<th>统计日期</th>
									<th>优食汇列表pv</th>
									<th>优食汇uv</th>
									<th>商品浏览pv</th>
									<th>商品浏览uv</th>
									<th>订单数</th>
									<th>成交单数</th>
									<th>成交人数</th>
									<th>成交转化率</th>
									<th>成交率</th>
									<th>成交金额(元)</th>
									<th>客单价（元)</th>
									<th>补贴金额(元)</th>
									<th>优惠券(元)</th>
									<th>非补贴-成交单</th>
									<th>非补贴-SKU</th>
									<th>非补贴-成交金额(元)</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="apptuan">
									<tr>
										<td width="70" ><c:out value="${apptuan.statdate}" /></td>
										<td width="70"><c:out value="${apptuan.list_pv}" /></td>
										<td width="70"><c:out value="${apptuan.list_uv}" /></td>
										<td width="70"><c:out value="${apptuan.detail_pv}" /></td>
										<td width="70"><c:out value="${apptuan.detail_uv}" /></td>
										<td width="50"><c:out value="${apptuan.orders}" /></td>
										<td width="50"><c:out value="${apptuan.pays}" /></td>
										<td width="50"><c:out value="${apptuan.order_uv}" /></td>
										<td width="60"><c:out value="${apptuan.conversionRate}" />%</td>
										<td width="60"><c:out value="${apptuan.payRate}" />%</td>
										<td width="60"><c:out value="${apptuan.moneys}" /></td>
                                        <td width="60"><c:out value="${apptuan.customerPrice}" /></td>
										<td width="50"><c:out value="${apptuan.conpons}" /></td>
										<td width="50"><c:out value="${apptuan.subsidys}" /></td>
										<td width="55"><c:out value="${apptuan.com_pays}" /></td>
										<td width="55"><c:out value="${apptuan.com_sku}" /></td>
										<td width="90"><c:out value="${apptuan.com_moneys}" /></td>
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
						<p></p>
					</div>
					<div id="tip_activeTable" class="tips">
						<div class="corner"></div>
						<p>电商销售日报</p>
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
						window.location.href = "/apptuan/mallOrderDayStatList.do?startDate="
								+ startDateShow + "&endDate=" + endDateShow
								+ "&plat=" + plat;
					}
				}
			});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../../includes/footer.jsp"></jsp:include>