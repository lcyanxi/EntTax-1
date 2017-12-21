<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.douguo.dc.util.PHPSerializer"%>
<%@ page language="java" import="com.douguo.dc.util.TypeUtil"%>
<%@ page language="java" import="com.douguo.dc.util.Cast"%>
<%@ page language="java" import="org.springframework.ui.ModelMap"%>

<jsp:include page="../includes/header_new.jsp"></jsp:include>
<style>
table{ border-collapse:0; border-spacing:0;}
table tr td{ border:1px solid #ccc;}
</style>
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
									<li class=<c:if test="${plat == 'API'}"> on</c:if>><a
										href="/slog/timelist.do?startDate=${startDate}&endDate=${endDate}&plat=API">API耗时分布</a></li>
									<li class=<c:if test="${plat == 'WWW'}"> on</c:if>><a
										href="/slog/timelist.do?startDate=${startDate}&endDate=${endDate}&plat=WWW">WWW耗时分布</a></li>
									<li class=<c:if test="${plat == 'WAP'}"> on</c:if>><a
										href="/slog/timelist.do?startDate=${startDate}&endDate=${endDate}&plat=WAP">WAP耗时分布</a></li>
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
							接口耗时分布统计 <a class="icon help poptips"
								action-frame="tip_activeTable" title=""></a>
								<input type="button" value=" 全 部 " id="butshow"  />
								<input type="button" value=" 待优化 " id="buthid" />
						</h2>
						<div class="option" style="display: none;">
							<span class="icon export exportCsv" title="导出"></span>
						</div>
					</div>
					<div class="mod-body " id="data-load">
						<table class="data-load " width="100%" border="0" cellspacing="0" id="all">
							<thead>
								<tr>
									<th>plat</th>
									<th>qtype</th>
									<th>耗时</th>
									<th>次数</th>
									<th>占比</th>
									<th>统计日期</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="slog">
									<tr 
										<c:if test="${slog.qtype!=preQtype}" var="isQtype"/> 
										<c:if test="${isQtype}" >
											<c:choose>
												<c:when test="${preBackColor!='FFFFFF'}">
													<c:set var="preBackColor" value="FFFFFF"/>
												</c:when>	
												<c:when test="${preBackColor=='FFFFFF'}">
													<c:set var="preBackColor" value="E8E5E5"/>
												</c:when>
											</c:choose>
										</c:if> 
										 style="background-color:#${preBackColor};" 
									 	<c:if test="${slog.time_seq == 1001 && slog.ratio > 2 }"> class="hid" id="one"</c:if>
									 >
										<td width="15%"><c:out value="${slog.plat}" />
										</td>
										<td width="20%"><a
											href="http://www.baidu.com/s?wd=<c:out value="${slog.qtype}"/>"
											target="_blank"><c:out value="${slog.qtype}" /></a></td>
										<td width="15%">
											<c:if test="${slog.time_seq == 100}">
												0-<c:out value="${slog.time_seq}" />		
											</c:if>
											<c:if test="${slog.time_seq == 500}">
												100-<c:out value="${slog.time_seq}" />		
											</c:if>
											<c:if test="${slog.time_seq == 1000}">
												501-<c:out value="${slog.time_seq}" />		
											</c:if>
											<c:if test="${slog.time_seq == 1001}">
												>=<c:out value="${slog.time_seq}" />		
											</c:if>
										</td>
										<td width="15%"><c:out value="${slog.times}" /></td>
										<td width="15%" <c:if test="${slog.time_seq == 1001 && slog.ratio > 2 }">style="color:red;"</c:if>><c:out value="${slog.ratio}" />%</td>
										<td width="20%" ><c:out value="${slog.statdate}" /></td>
									</tr>
									<c:set var="preQtype" value="${slog.qtype }"/>
									
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
						window.location.href = "/slog/timelist.do?startDate="
								+ startDateShow + "&endDate=" + endDateShow
								+ "&plat=" + plat;
					}
				}
				$("#buthid").click(function(){
				    //$("#all tr").not($("#one")).hide(1000);
					$("#all tr").not($("#[class='hid']")).hide(1000);
				  });
				  $("#butshow").click(function(){
				     $("#all tr").show(1000);
				  });
			});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>