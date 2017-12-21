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
									<li class=<c:if test="${clienttypeid == 0}"> on</c:if>><a href="/search/emptyList.do?startDate=${startDate}&clienttypeid=0">网站</a></li>
									<li class=<c:if test="${clienttypeid == 1}"> on</c:if>><a href="/search/emptyList.do?startDate=${startDate}&clienttypeid=1">客户端</a></li>
								</ul>
							</div>
						</div>
					</div>
					<div class="contentCol">

						<div class="filterPanel">
							<div class="datepickerPanel custom1 borders">
								<div class="dateselect" id="date-select">
									<span class="start">${startDate}</span> - <span class="end">${startDate}</span>
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
							搜索结果为空<a class="icon help poptips" action-frame="tip_activeTable"
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
									<th>类型</th>
									<th>标签</th>
									<th>关键词</th>
									<th>数量</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<%
								List<Map<String, Object>> rowlst = (List<Map<String, Object>>)request.getSession().getAttribute("rowlst");
								
								//Map<Integer, String> type = request.getSession().getAttribute("type");
								//out.print(rowlst.size());
								for( Map<String, Object> map : rowlst){
									
									String keyword = (String)map.get("keyword");
									String tagname= (String)map.get("tagname");
									Integer num = (Integer)map.get("num");
									Integer typeid  = (Integer)map.get("typeid");

								%>
									<tr>
										<td>
											<%if(typeid == 1){%>标签搜索<%} %>
											<%if(typeid == 2){%>食材搜索<%} %>
											<%if(typeid == 3){%>关键字搜索<%} %>
										</td>
										<td><%=tagname%></td><td><%=keyword%></td>
										<td><%=num%></td>
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
							每天网站和客户端搜索没有结果的关键词和数量，客户端可以根据某个标签搜索。
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="startDate" value="${startDate }" />
<input type="hidden" id="getClienttypeid" value="${clienttypeid }" />

</div>
<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js" type="text/javascript"></script>

<script type="text/javascript">

$(document).ready(function(){
	$(".start").html($("#startDate").val());
	$(".end").html($("#startDate").val());
	$('#date-select').click(function(){
		  var panel = $('#proTemp2');
		  panel.is(':visible') ? panel.hide() : panel.show();

		  panel.find('.mod-body').datepicker({
		    yearRange: '2000:'+window.thisYear,
		    maxDate : -1,
		    onSelect:function(dateText, inst,e){
		      $('.dateselect .start').text($.replaceDate(dateText));
		      $('.dateselect .end').text($.replaceDate(dateText));
		      $.publish('rPage');
		      panel.hide();
		      clienttypeid	= $("#getClienttypeid").val();
		      window.location.href="/search/emptyList.do?startDate="+dateText+"&clienttypeid="+clienttypeid;
		      
		    }
		  })
		  return false;
		});
	$('#proTemp2').bind('click',function(e){
		  return false;
		});
});
</script>

<jsp:include page="../includes/footer.jsp"></jsp:include>