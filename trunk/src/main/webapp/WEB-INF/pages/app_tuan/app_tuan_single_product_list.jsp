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
					<div class="leftCol" style="width: 390px;">
						<div class="mod-select">
							<div class="tabpanel">
								<ul id="toptab" class="borders">
									<li class=<c:if test="${plat == 'PRODUCT'}"> on</c:if>><a
										href="/apptuan/productList.do?startDate=${startDate}&endDate=${endDate}&plat=PRODUCT">返回</a></li>
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
			
			<form name="exportForm" id="exportForm" action="${pageContext.request.contextPath}/apptuan/exportSingleProductExcel.do" method="post">
				<c:forEach items="${rowlst}" var="apptuan_id">
					<input name="tuan_id" value="${apptuan_id.tuan_id}" type="hidden"></input>
				</c:forEach>
				<input name="startDate" value="${startDate}" type="hidden"></input>
				<input name="endDate" value="${endDate}" type="hidden"></input>
				<input type="submit" value="导出商品数据">
				<br>
			</form>

			<br/>
			
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
									<th>团品id</th>
									<th>团品名称</th>
									<th onclick="uf_refer('clicks');">浏览数</th>
									<th onclick="uf_refer('uids');">UV数</th>
									<th onclick="uf_refer('orders');">下单数</th>
									<th>下单率</th>
									<th onclick="uf_refer('pays');">成交数</th>
									<th>成交率</th>
									<th onclick="uf_refer('goods');">商品数</th>
									<th onclick="uf_refer('moneys');">总支付金额</th>
									<th>统计日期</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="apptuan">
									<tr>
										<td width="7%"><c:out value="${apptuan.tuan_id}" /></td>
										<td width=""><c:out value="${apptuan.tuan_name}" /></td>
										<td width="8%"><c:out value="${apptuan.clicks}" /></td>
										<td width="8%"><c:out value="${apptuan.uids}" /></td>
										<td width="8%"><c:out value="${apptuan.orders}" /></td>
										<td width="9%"><c:out value="${apptuan.orderRate}" />%</td>
										<td width="8%"><c:out value="${apptuan.pays}" /></td>
										<td width="9%"><c:out value="${apptuan.payRate}" />%</td>
										<td width="8%"><c:out value="${apptuan.goods}" /></td>
										<td width="10%"
											<c:if test="${apptuan.moneys >= 1000 }">style="color:red;"</c:if>>
											<fmt:formatNumber value="${apptuan.moneys/100}" pattern="#.00" type="number"/>
										</td>
										<td width="11%"><c:out value="${apptuan.statdate}" /></td>
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
						<p>特卖恵单品点击量统计</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="startDate" value="${startDate}" />
<input type="hidden" id="endDate" value="${endDate}" />
<input type="hidden" id="plat" value="${plat}" />
<input type="hidden" id="tuanId" value="${tuanId}" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>

<script type="text/javascript">
	function uf_refer(order) {
		plat = $("#plat").val();
		startDateShow = $(".start").html().replace(/\./g, "-");
		endDateShow = $(".end").html().replace(/\./g, "-");
		window.location.href = "/apptuan/singleProductList.do?startDate="
				+ startDateShow + "&endDate=" + endDateShow + "&plat=" + plat
				+ "&order=" + order + "&tuanId=${tuanId}";
	}

	$(document).ready(
			global.renderPage = function() {
				plat = $("#plat").val();
				startDateShow = $(".start").html().replace(/\./g, "-");
				endDateShow = $(".end").html().replace(/\./g, "-");
				if (startDateShow != $("#startDate").val()
						|| endDateShow != $("#endDate").val()) {
					window.location.href = "/apptuan/singleProductList.do?startDate="
							+ startDateShow + "&endDate=" + endDateShow
							+ "&plat=" + plat + "&tuanId=${tuanId}";
				}
			});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>