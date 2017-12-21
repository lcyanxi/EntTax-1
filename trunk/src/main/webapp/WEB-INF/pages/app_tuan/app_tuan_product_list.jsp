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

<div class="bd clearfix" style="overflow: auto">

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
									<li class=<c:if test="${plat == 'PRODUCT'}"> on</c:if>><a
										href="/apptuan/productList.do?startDate=${startDate}&endDate=${endDate}&plat=PRODUCT">优食汇单品点击数据</a></li>
								</ul>
							</div>
						</div>
					</div>
					<div class="contentCol">
						<div class="filterPanel">
							商品id ： <input type="text" name="goods_ids" id="goods_ids" onblur="window.global.renderPage();" value="${goodsIds}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
			<br />
			<form name="exportForm" id="exportForm" action="${pageContext.request.contextPath}/apptuan/exportProductExcel.do" method="post">
				<input type="submit" value="导出商品数据">
				<br>
			</form>
			<br />
			<div class="wrap-table">
				<div class="mod mod1 parent-table" id="active_user-detail-table">
					<div class="mod-header radius">
						<h2>
							优食汇数据统计(TOP100) <a class="icon help poptips"
								action-frame="tip_activeTable" title=""></a>
						</h2>
						<div class="option" style="display: none;">
							<span class="icon export exportCsv" title="导出"></span>
						</div>
					</div>
					<div class="mod-body " id="data-load">
						<table class="data-load " width="2370px" border="0"
							cellspacing="0">
							<thead>
								<tr>
									<th>商品id</th>
									<th>商品名称</th>
									<th>一级分类</th>
									<th>二级分类</th>
									<th>商家用户id</th>
									<th>商家名称</th>
									<th>上架时间</th>
									<th>商家报价</th>
									<th>豆果售价</th>
									<th onclick="uf_refer('clicks');">浏览数</th>
									<th onclick="uf_refer('uids');">UV数</th>
									<th onclick="uf_refer('orders');">下单数</th>
									<th>下单率</th>
									<th onclick="uf_refer('pays');">成交数</th>
									<th onclick="uf_refer('pay_uids');">成交人数</th>
									<th>成交率</th>
									<th onclick="uf_refer('goods');">商品数</th>
									<th onclick="uf_refer('moneys');">总支付金额</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="apptuan" varStatus="status">
									<c:if test="${status.index <= 100}">
									<tr>
										<td width="50"><a
											href="/apptuan/singleProductList.do?startDate=${startDate}&endDate=${endDate}&plat=PRODUCT&tuanId=<c:out value="${apptuan.tuan_id}" />"><c:out
													value="${apptuan.tuan_id}" /></a></td>
										<td width="150"><c:out value="${apptuan.tuan_name}" /></td>
										<td width="70"><c:out value="${apptuan.fCateName}" /></td>
										<td width="70"><c:out value="${apptuan.cateName}" /></td>
										<td width="70"><c:out value="${apptuan.storeId}" /></td>
										<td width="100"><c:out value="${apptuan.storeName}" /></td>
										<td width="110"><c:out value="${apptuan.sellFlagTime}" /></td>
										<td width="50"><c:out value="${apptuan.marketPrice}" /></td>
										<td width="50"><c:out value="${apptuan.price}" /></td>
										<td width="50"><c:out value="${apptuan.clicks}" /></td>
										<td width="50"><c:out value="${apptuan.uids}" /></td>
										<td width="50"><c:out value="${apptuan.orders}" /></td>
										<td width="50"><c:out value="${apptuan.orderRate}" />%</td>
										<td width="5%"><c:out value="${apptuan.pays}" /></td>
										<td width="5%"><c:out value="${apptuan.pay_uids}" /></td>
										<td width="50"><c:out value="${apptuan.payRate}" />%</td>
										<td width="50"><c:out value="${apptuan.goods}" /></td>
										<td width="70"
											<c:if test="${apptuan.moneys >= 100000 }">style="color:red;"</c:if>>
											<fmt:formatNumber value="${apptuan.moneys/100}"
												pattern="#.00" type="number" />
										</td>
									</tr>
									</c:if>
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
						<p>优食汇单品点击量统计</p>
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
	function uf_exportExcel() {
		var expForm = $("#exportForm");
		alert(expForm);
		var startDateShow = $(".start").html().replace(/\./g, "-");
		var endDateShow = $(".end").html().replace(/\./g, "-");
		alert(startDateShow);
		alert(endDateShow);

		//expForm.action(");
		alert(expForm.action);
		return false;
	}

	function uf_refer(order) {
		plat = $("#plat").val();
		startDateShow = $(".start").html().replace(/\./g, "-");
		endDateShow = $(".end").html().replace(/\./g, "-");
		window.location.href = "/apptuan/productList.do?startDate="
				+ startDateShow + "&endDate=" + endDateShow + "&plat=" + plat
				+ "&order=" + order;
	}

	$(document)
			.ready(
					global.renderPage = function() {
						plat = $("#plat").val();
						var goodsIds = $("#goods_ids").val();
						startDateShow = $(".start").html().replace(/\./g, "-");
						endDateShow = $(".end").html().replace(/\./g, "-");
						if (startDateShow != $("#startDate").val()
								|| endDateShow != $("#endDate").val() || goodsIds != '${goodsIds}') {
							window.location.href = "/apptuan/productList.do?startDate="
									+ startDateShow
									+ "&endDate="
									+ endDateShow
									+ "&plat=" + plat
									+ "&goodsIds=" + goodsIds;
						}
					},
					$("form").submit(
									function() {
										var expForm = $("#exportForm");
										//var startDateShow = $(".start").html().replace(/\./g, "-");
										var startDateShow = $("#startDate").val();
										//var endDateShow = $(".end").html().replace(/\./g, "-");
										var endDateShow = $("#endDate").val();
										var goodsIds = $("#goods_ids").val();
										var strAction = "${pageContext.request.contextPath}/apptuan/exportProductExcel.do?startDate="
												+ startDateShow
												+ "&endDate="
												+ endDateShow 
												+ "&goodsIds=" 
												+ goodsIds;
										expForm.attr("action", strAction);
									}));
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>