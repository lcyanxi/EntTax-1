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
									<li class=<c:if test="${qtype == ''}"> on</c:if>><a
										href="/user/behavior/list.do?startDate=${startDate}&endDate=${endDate}&qtype=">关键词综合排名</a></li>
									<li class=<c:if test="${qtype == 'search_caipu'}"> on</c:if>><a
										href="/user/behavior/list.do?startDate=${startDate}&endDate=${endDate}&qtype=search_caipu">搜索词排名</a></li>
									<li class=<c:if test="${qtype == 'view_caipu_detail'}"> on</c:if>><a
										href="/user/behavior/list.do?startDate=${startDate}&endDate=${endDate}&qtype=view_caipu_detail">菜谱浏览排名</a></li>
									<li class=<c:if test="${qtype == 'view_tuan_detail'}"> on</c:if>><a
										href="/user/behavior/list.do?startDate=${startDate}&endDate=${endDate}&qtype=view_tuan_detail">商品浏览排名</a></li>
									<li class=<c:if test="${qtype == 'view_cookcollect_detail'}"> on</c:if>><a
										href="/cookcollect/list.do?startDate=${startDate}&endDate=${endDate}&qtype=view_cookcollect_detail">菜谱收藏排名</a></li>
								</ul>
							</div>
						</div>
					</div>
					<div class="contentCol">
						<div class="filterPanel" style="margin-left: 730px;">
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
							收藏量排名 <a class="icon help poptips"
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
									<th>排名</th>
									<th>菜谱</th>
									<th>收藏量</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="cookCollect" varStatus="st">
									<tr>
										<td><c:out value="${st.count}"/></td>
										<td><c:out value="${cookCollect.cook_name}" /></td>
										<td><a title="点一下又不会怀孕,^_^" href="/cookcollect/detaillist.do?qtype=${qtype}&cookName=${cookCollect.cook_name}"><c:out value="${cookCollect.collects}" /></a></td>
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
						<p>收藏量排名统计</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="startDate" value="${startDate}" />
<input type="hidden" id="endDate" value="${endDate}" />
<input type="hidden" id="app" value="${app}" />
<input type="hidden" id="qtype" value="${qtype}" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>

<script type="text/javascript">
	$(document).ready(
			function() {

				global.renderPage = function() {
					app = $("#app").val();
					qtype = $("#qtype").val();
					startDateShow = $(".start").html().replace(/\./g, "-");
					endDateShow = $(".end").html().replace(/\./g, "-");
					if (startDateShow != $("#startDate").val()
							|| endDateShow != $("#endDate").val()) {
						window.location.href = "/cookcollect/list.do?startDate="
								+ startDateShow + "&endDate=" + endDateShow
								+ "&qtype=" + qtype;
					}
				}
			});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>