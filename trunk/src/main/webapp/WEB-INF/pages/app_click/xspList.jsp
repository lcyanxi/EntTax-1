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
					<div class="leftCol" style="width: 590px;">
						<div class="mod-select">
							<div class="tabpanel">
								<ul id="toptab" class="borders">
									<li class=<c:if test="${type == '38'}"> on</c:if>><a
										href="/appclick/xsplist.do?startDate=${startDate}&endDate=${endDate}&type=38&data1=0">美食导航</a></li>
									<li class=<c:if test="${type == '39'}"> on</c:if>><a
										href="/appclick/xsplist.do?startDate=${startDate}&endDate=${endDate}&type=39">标签TAB导航</a></li>
									<li class=<c:if test="${type == '41'}"> on</c:if>><a
										href="/appclick/xsplist.do?startDate=${startDate}&endDate=${endDate}&type=41">上传日记</a></li>
									<li class=<c:if test="${type == '42'}"> on</c:if>><a
										href="/appclick/xsplist.do?startDate=${startDate}&endDate=${endDate}&type=42">我的导航</a></li>
									<li class=<c:if test="${type == '43'}"> on</c:if>><a
										href="/appclick/xsplist.do?startDate=${startDate}&endDate=${endDate}&type=43">个人信息</a></li>
								</ul>
							</div>
						</div>
					</div>
					<div class="contentCol" style="margin-left: 530px;">
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
									<th>日志来源</th>
									<c:if test="${type == '38'}">
										<th>来源</th>
									</c:if>
									<th>统计项</th>
									<th>点击数</th>
									<th>人数</th>
									<th>统计日期</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="appclick">
									<tr>
										<td><c:out value="${appclick.type}" /></td>
										<c:if test="${type == '38'}">
											<td val="<c:out value="${appclick.data3}" />">
												<c:set var="key" value="${appclick.type}_3_${appclick.data3}"></c:set>
												<c:out value="${mapTypeDict[key]}" />
											</td>
										</c:if>
										<td val="<c:out value="${appclick.data1}" /> ">
											<c:set var="key" value="${appclick.type}_1_${appclick.data1}"></c:set>
											<c:out value="${mapTypeDict[key]}" />
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
<input type="hidden" id="type" value="${type}" />
<input type="hidden" id="data1" value="${data1}" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						global.renderPage = function() {
							type = $("#type").val();
							data1= $("#data1").val();
							startDateShow = $(".start").html().replace(/\./g,
									"-");
							endDateShow = $(".end").html().replace(/\./g, "-");
							if (startDateShow != $("#startDate").val()
									|| endDateShow != $("#endDate").val()) {
								window.location.href = "/appclick/xsplist.do?startDate="
										+ startDateShow
										+ "&endDate="
										+ endDateShow
										+ "&type="
										+ type
										+ "&data1=" + data1;
							}
						}
					});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>