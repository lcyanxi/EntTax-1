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

	<div id="mainContainer" style="margin-left:0px;">
		<div class="contentCol">

			<input type="hidden" value="active_users" id="action_stats" />

			<div class="operations">
				<div class="bd3">
					<div class="leftCol">
						<div class="mod-select">
							<div class="tabpanel">
								<ul id="toptab" class="borders">
									<li class=<c:if test="${source == 0}"> on</c:if>><a href="/dishtag/list.do?startDate=${startDate}&endDate=${endDate}&source=0">话题作品统计</a></li>
									<li class=<c:if test="${source == 1}"> on</c:if>><a href="/dishtag/list.do?startDate=${startDate}&endDate=${endDate}&source=1">话题人数统计</a></li>
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
								<div id="proTemp2" style="display:none;">
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
							话题作品统计<a class="icon help poptips" action-frame="tip_activeTable"
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
									<th>话题ID</th>
									<th>话题名称</th>
									<%
										Map<String, Object> rowMapUsers =  (Map<String, Object>)request.getSession().getAttribute("rowMapUsers");
										Map<String, Object> rowMapTagName =  (Map<String, Object>)request.getSession().getAttribute("rowMapTagName");
										Set<String> tableLine	= (LinkedHashSet<String>)request.getSession().getAttribute("tableLine");
										Set<String> tableColumn	= (LinkedHashSet<String>)request.getSession().getAttribute("tableColumn");
										for( String line : tableLine){
									%>
									<th>
										<%=line %>
									</th>
									<%
									}
									%>
								</tr>
							</thead>
							<tbody id="data-list">
								<%
								for( String column:tableColumn){
								%>
								<tr>
									<td><%=column %></td>
									<%
										String tagname = (String)rowMapTagName.get(column);
										if(tagname == null){ tagname = "";}
									%>
									<td><%=tagname%></td>
								<%
								
									for( String line : tableLine){
										String rownum = (String)rowMapUsers.get(column+"-"+line);
										if(rownum == null){ rownum = "0";}
									%>
									<td><%=rownum %></td>
									<%
									}
								%>
								</tr>
								<%
								}
								%>
							
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
						<p>
							按话题维度显示作品发布量
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="startDate" value="${startDate }" />
<input type="hidden" id="endDate" value="${endDate }" />
<input type="hidden" id="source" value="${source }" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js" type="text/javascript"></script>

<script type="text/javascript">

$(document).ready(function(){
	
	
	global.renderPage = function() {
		source	= $("#source").val();
		startDateShow	= $(".start").html().replace(/\./g,"-");
		endDateShow	= $(".end").html().replace(/\./g,"-");
		if(startDateShow != $("#startDate").val() || endDateShow != $("#endDate").val())
		{
			window.location.href="/dishtag/list.do?startDate="+startDateShow+"&endDate="+endDateShow+"&source="+source;
		}		
	}
});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>