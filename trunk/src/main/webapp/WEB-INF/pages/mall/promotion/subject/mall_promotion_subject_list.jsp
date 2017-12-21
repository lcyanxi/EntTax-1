<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.douguo.dc.util.PHPSerializer"%>
<%@ page language="java" import="com.douguo.dc.util.TypeUtil"%>
<%@ page language="java" import="com.douguo.dc.util.Cast"%>
<%@ page language="java" import="org.springframework.ui.ModelMap"%>

<jsp:include page="/WEB-INF/pages/includes/header_new.jsp"></jsp:include>

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
					<div class="leftCol" style="width: 390px;">
						<div class="mod-select">
							<div class="tabpanel">
								<ul id="toptab" class="borders">
									<li class=<c:if test="${plat == 'SUBJECT'}"> on</c:if>><a
										href="/mall/promotion/subject/subjectList.do?startDate=${startDate}&endDate=${endDate}&plat=SUBJECT">促销专题销售统计</a></li>
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
							促销专题据统计 <a class="icon help poptips"
								action-frame="tip_activeTable" title=""></a>
						</h2>
						<div class="option" style="display: none;">
							<span class="icon export exportCsv" title="导出"></span>
						</div>
					</div>
					<div class="mod-body " id="data-load">
						<table class="data-load " width="2370px" border="0" cellspacing="0">
							<thead>
								<tr>
									<th style="background-color:rgb(83, 142, 213);">日期</th>
									<th style="background-color:rgb(83, 142, 213);" >星期</th>
									<th style="background-color:rgb(83, 142, 213);">专题id</th>
									<th style="background-color:rgb(83, 142, 213);">专题名称</th>
									<th style="background-color:rgb(117, 146, 60);">专题PV</th>
									<th style="background-color:rgb(117, 146, 60);">专题UV</th>
									<th style="background-color:rgb(117, 146, 60);">专题单品PV</th>
									<th style="background-color:rgb(117, 146, 60);">成交单数</th>
									<th style="background-color:rgb(117, 146, 60);">成交商品数</th>
									<th onclick="uf_refer('moneys');" style="background-color:rgb(117, 146, 60);">成交金额</th>
									<th style="background-color:rgb(117, 146, 60);">客单价</th>
									<th style="background-color:rgb(149, 55, 53);">优食汇-pv</th>
									<th style="background-color:rgb(149, 55, 53);">优食汇-商品pv</th>
									<th style="background-color:rgb(149, 55, 53);">优食汇-成交单数</th>
									<th style="background-color:rgb(149, 55, 53);">优食汇-成交商品数</th>
									<th style="background-color:rgb(149, 55, 53);">优食汇-成交金额</th>
									<th style="background-color:rgb(49, 132, 155);">专题/单品转化率</th>
									<th style="background-color:rgb(49, 132, 155);">优食汇转化率</th>
									<th style="background-color:rgb(49, 132, 155);">PV占比</th>
									<th style="background-color:rgb(49, 132, 155);">销量占比</th>
									<th style="background-color:rgb(49, 132, 155);">销售金额占比</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="st">
									<tr>
										<td width="70"><c:out value="${st.statdate}" /></td>
										<td width="40"><fmt:formatDate value="${st.statdate}" pattern="EEEE"/></td>
										<td width="60"><c:out value="${st.subject_id}" /></td>
										<td width=""><c:out value="${st.subject_title}" /></td>
										<td width="50"><c:out value="${st.subject_pv}" /></td>
										<td width="50"><c:out value="${st.subject_uv}" /></td>
										<td width="60"><c:out value="${st.goods_pv}" /></td>
										<td width="50"><c:out value="${st.pays}" /></td>
										<td width="50"><c:out value="${st.goods_num}" /></td>
										<td width="70"
											<c:if test="${st.moneys >= 100000 }">style="color:red;"</c:if>>
											<fmt:formatNumber value="${st.moneys/100}" pattern="###,###,###,###.00" type="number"/></td>
										<td width="50"><c:out value="${st.moneys/100/st.pays}" /></td>
										<td width="50"><c:out value="${st.ysh_list_pv}" /></td>
										<td width="70"><c:out value="${st.ysh_detail_pv}" /></td>
										<td width="85"><c:out value="${st.ysh_pays}" /></td>
										<td width="95"><c:out value="${st.ysh_goods}" /></td>
										<td width="90">
											<fmt:formatNumber value="${st.ysh_moneys/100}" pattern="###,###,###,###.00" type="number"/>
										</td>
										<td width="85"><c:out value="${st.subjectPvPayRate}" />%</td>
										<td width="70"><c:out value="${st.YshPvPayRate}" />%</td>
										<td width="60"><c:out value="${st.subjectGoodsPvRate}" />%</td>
										<td width="60"><c:out value="${st.subjectGoodsRate}" />%</td>
										<td width="70"><c:out value="${st.subjectMoneyRate}" />%</td>
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
						<p>商家销售数据统计</p>
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
	function uf_refer(order) {
		plat = $("#plat").val();
		startDateShow = $(".start").html().replace(/\./g, "-");
		endDateShow = $(".end").html().replace(/\./g, "-");
		window.location.href = "/mall/promotion/subject/subjectList.do?startDate="
				+ startDateShow + "&endDate=" + endDateShow + "&plat=" + plat
				+ "&order=" + order;
	}

	$(document).ready(
			global.renderPage = function() {
				plat = $("#plat").val();
				startDateShow = $(".start").html().replace(/\./g, "-");
				endDateShow = $(".end").html().replace(/\./g, "-");
				if (startDateShow != $("#startDate").val()
						|| endDateShow != $("#endDate").val()) {
					window.location.href = "/mall/promotion/subject/subjectList.do?startDate="
							+ startDateShow + "&endDate=" + endDateShow
							+ "&plat=" + plat;
				}
			});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../../../includes/footer.jsp"></jsp:include>