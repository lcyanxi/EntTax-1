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
									<li class=<c:if test="${statType == 'common' && repeatType == ''}"> on</c:if>><a
										href="/mall/repeatbuy/day30list.do?startDate=${startDate}&endDate=${endDate}&statType=common&repeatType=">当日-回头客占比</a></li>
									<li class=<c:if test="${statType == 'common' && repeatType == '1'}"> on</c:if>><a
										href="/mall/repeatbuy/day30list.do?startDate=${startDate}&endDate=${endDate}&statType=common&repeatType=1">首单占比</a></li>
									<li class=<c:if test="${statType == 'common' && repeatType == '7'}"> on</c:if>><a
										href="/mall/repeatbuy/day30list.do?startDate=${startDate}&endDate=${endDate}&statType=common&repeatType=7">7日回头客</a></li>
									<li class=<c:if test="${statType == 'common' && repeatType == '15'}"> on</c:if>><a
										href="/mall/repeatbuy/day30list.do?startDate=${startDate}&endDate=${endDate}&statType=common&repeatType=15">15日回头客</a></li>
									<li class=<c:if test="${statType == 'common' && repeatType == '30'}"> on</c:if>><a
										href="/mall/repeatbuy/day30list.do?startDate=${startDate}&endDate=${endDate}&statType=common&repeatType=30">30日回头客</a></li>
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
							回头客占比统计 <a class="icon help poptips"
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
									<th>回头客类别</th>
									<th>总人数</th>
									<th>人数</th>
									<th>占比</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="obj">
										<tr <c:if test="${obj.repeat_type == '2' && obj.ratio > 30 }">style="background-color:mistyrose;"</c:if>>
										<c:if test="${obj.repeat_type != '0' }">
											<td width="20%" ><c:out value="${obj.statdate}" /></td>										
										</c:if>
										
										<c:if test="${obj.repeat_type != '0' && obj.repeat_type != '1'}">
											<td width="15%"><c:out value="${obj.repeat_type}" />日</td>
										</c:if>
										<c:if test="${obj.repeat_type == '1' }">
											<td width="15%">首单</td>
										</c:if>
										<c:if test="${obj.repeat_type != '0' }">
											<td width="15%"><c:out value="${obj.curTotal}" /></td>
											<td width="15%"><c:out value="${obj.repeat_value}" /></td>
											<td width="15%" <c:if test="${obj.ratio > 5 }">style="color:red;"</c:if>><c:out value="${obj.ratio}" />%</td>
										</c:if>	
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
						<p>重复购买率：
							一种：是按交易计算，重复购买交易次数/总交易次数，及重复购买的总次数占比。
								例如某段时间内，一共产生了100笔交易，其中有20个人有了二次购买，这20人中的10个人又有了三次购买，则重复购买次数为30次，重复购买率为30%。
								
							另一种：是按人去重，计算重复购买率；指在单位时间段内，再次购买购买人数/总购买人数，计算出来的比例，则为重复购买率。
								例如在一个月内，有100个客户成交，其中有20个是回头客，则重复购买率为20%。
								关于回头客的定义，又分为两种，
									a.按天非去重，即一个客户一天产生多笔付款交易，则算重复购买。
									b.按天去重，即一个客户一天产生多笔交易付款，则算一次购买，除非在统计周期内另外一天也有购买，则算入回头客。
									按天非去重，是b2c网站统计数据常用计算方法，相对计算出来的重复购买率要高于第二种。
							我们采用按人去重，当日支付订单的人去重，对比30天内的支付订单人对比，计算重复购买率。（当天购买两单的用户也被排除为重复购买用户）。</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="startDate" value="${startDate}" />
<input type="hidden" id="endDate" value="${endDate}" />
<input type="hidden" id="statType" value="${statType}" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>

<script type="text/javascript">
	$(document).ready(
			function() {

				global.renderPage = function() {
					statType = $("#statType").val();
					startDateShow = $(".start").html().replace(/\./g, "-");
					endDateShow = $(".end").html().replace(/\./g, "-");
					if (startDateShow != $("#startDate").val()
							|| endDateShow != $("#endDate").val()) {
						window.location.href = "/mall/repeatbuy/day30list.do?startDate="
								+ startDateShow + "&endDate=" + endDateShow
								+ "&statType=" + statType;
					}
				}
			});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../../includes/footer.jsp"></jsp:include>