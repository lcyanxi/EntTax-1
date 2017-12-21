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
									<li class=<c:if test="${tab == 'GROUP_BASE'}"> on</c:if>><a
										href="/group/base/list.do?startDate=${startDate}&endDate=${endDate}&tab=GROUP_BASE">圈子汇总统计</a></li>
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

			<!-- 图形展示 begin -->
			<!--Step:1 Prepare a dom for ECharts which (must) has size (width & hight)-->
		    <div id="main" style="height:500px;border:1px solid #ccc;padding:10px;"></div>
		    
		    <!--Step:2 Import echarts.js-->
		    <script src="/scripts/echarts/echarts.js"></script>
		    
		    <script type="text/javascript">
		    // Step:3 conifg ECharts's path, link to echarts.js from current page.
		    // Step:3 为模块加载器配置echarts的路径，从当前页面链接到echarts.js，定义所需图表路径
		    require.config({
		        paths: {
		            echarts: '/scripts/echarts/'
		        }
		    });
		    
		    // Step:4 require echarts and use it in the callback.
		    // Step:4 动态加载echarts然后在回调函数中开始使用，注意保持按需加载结构定义图表路径
		    require(
		        [
		            'echarts',
		            'echarts/chart/bar',
		            'echarts/chart/line',
		            'echarts/chart/map'
		        ],
		        function (ec) {
		            //--- 折柱 ---
		            var myChart = ec.init(document.getElementById('main'));
		            myChart.setOption({
		                tooltip : {
		                    trigger: 'axis'
		                },
		                legend: {
		                    data:['总发帖数','主帖数','回复数','发帖用户数','总浏览数','总浏览人数']
		                },
		                toolbox: {
		                    show : true,
		                    feature : {
		                        mark : {show: true},
		                        dataView : {show: true, readOnly: false},
		                        magicType : {show: true, type: ['line', 'bar','stack', 'tiled']},
		                        restore : {show: true},
		                        saveAsImage : {show: true}
		                    }
		                },
		                calculable : true,
		                xAxis : [
		                         <c:out value="${xAxis}" escapeXml="false"/>
		                ],
		                yAxis : [
		                    {
		                        type : 'value',
		                        splitArea : {show : true}
		                    }
		                ],
		                series : <c:out value="${series}" escapeXml="false"/>
		            });
		        }
		    );
		    </script>
    		<!-- 图形展示 end -->
			<div class="wrap-table">
				<div class="mod mod1 parent-table" id="active_user-detail-table">
					<div class="mod-header radius">
						<h2>
							圈圈统计 <a class="icon help poptips"
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
									<th>圈子id</th>
									<th>总发帖数</th>
									<th>主贴数</th>
									<th>回复数</th>
									<th>发帖用户数</th>
									<th>总浏览数</th>
									<th>总浏览人数</th>
									<th>圈子PV</th>
									<th>圈子UV</th>
									<th>帖子PV</th>
									<th>帖子UV</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="group_base">
									<tr>
										<td><a title="点一下又不会怀孕，^_^" href="/group/base/daylist.do?app=${group_base.posts}&startDate=${group_base.statdate}&endDate=${group_base.statdate}"><c:out value="${group_base.statdate}" /></a></td>
										<td><c:out value="${group_base.group_id}" /></td>
										<td><c:out value="${group_base.total_actions}" /></td>
										<td><c:out value="${group_base.posts}" /></td> 
										<td><c:out value="${group_base.replies}" /></td>
										<td><c:out value="${group_base.total_users}" /></td>
										<td><c:out value="${group_base.pv}" /></td>
										<td><c:out value="${group_base.uv}" /></td>
										<td><c:out value="${group_base.group_pv}" /></td>
										<td><c:out value="${group_base.group_uv}" /></td>
										<td><c:out value="${group_base.post_pv}" /></td>
										<td><c:out value="${group_base.post_uv}" /></td>
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
						<p>圈圈统计</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="startDate" value="${startDate}" />
<input type="hidden" id="endDate" value="${endDate}" />
<input type="hidden" id="app" value="${app}" />
<input type="hidden" id="tab" value="${tab}" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>

<script type="text/javascript">
	$(document).ready(
			function() {

				global.renderPage = function() {
					app = $("#app").val();
					startDateShow = $(".start").html().replace(/\./g, "-");
					endDateShow = $(".end").html().replace(/\./g, "-");
					if (startDateShow != $("#startDate").val()
							|| endDateShow != $("#endDate").val()) {
						window.location.href = "/group/base/list.do?startDate="
								+ startDateShow + "&endDate=" + endDateShow
								+ "&app=" + app;
					}
				}
			});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>