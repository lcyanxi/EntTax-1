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

<input type="hidden" id="startDate" value="${startDate}" />
<input type="hidden" id="endDate" value="${endDate}" />


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
									<li><a
										href="/cookSum/cookSumStat.do?startDate=${startDate}&endDate=${endDate}">菜谱汇总数据统计</a></li>
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
		                	data:[
								'菜谱数',
								'菜谱人数',
								'首次上传菜谱数',
								'首次上传菜谱人数',
								'菜谱评论数',
								'菜谱评论人数',
								'菜谱评论回复数',
								'菜谱评论回复人数',
								'新用户菜谱评论回复数'],
							selected: {
								'菜谱数':true,
								'菜谱人数':false,
								'首次上传菜谱数':true,
								'首次上传菜谱人数':false,
								'菜谱评论数':false,
								'菜谱评论人数':false,
								'菜谱评论回复数':false,
								'菜谱评论回复人数':false,
								'新用户菜谱评论回复数':false
							}
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
    		
    		<!-- 图表展示 -->
			<div class="wrap-table">
				<div class="mod mod1 parent-table" id="active_user-detail-table">
					<div class="mod-header radius">
						<h2>
							菜谱汇总数据统计 <a class="icon help poptips"
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
									<th>日期</th>
									<th>菜谱数</th>
									<th>菜谱人数</th>
									<th>首次上传菜谱数</th>
									<th>首次上传菜谱人数</th>
									<th>菜谱评论数</th>
									<th>菜谱评论人数</th>
									<th>菜谱评论回复数</th>
									<th>菜谱评论回复人数</th>
									<th>新用户菜谱评论回复数</th>
									<th>新用户菜谱评论回复人数</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="cook_sum">
									<tr>
										<td><c:out value="${cook_sum.statdate}" /></td>
										<td><c:out value="${cook_sum.cooks}" /></td>
										<td><c:out value="${cook_sum.cook_users}" /></td>
										<td><c:out value="${cook_sum.first_cooks}" /></td>
										<td><c:out value="${cook_sum.first_cook_users}" /></td> 
										<td><c:out value="${cook_sum.cook_comments}" /></td>
										<td><c:out value="${cook_sum.cook_comment_users}" /></td>
										<td><c:out value="${cook_sum.cook_comment_replys}" /></td>
										<td><c:out value="${cook_sum.cook_comment_reply_users}" /></td>
									    <td><c:out value="${cook_sum.first_cook_comment_replys}" /></td>
                                        <td><c:out value="${cook_sum.first_cook_comment_reply_users}" /></td>
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
				<div id="tip_active" >
					<div id="tip_activeTable" class="tips">
						<div class="corner"></div>
						<p>菜谱汇总统计</p>
					</div>
				</div>
			</div>
			
		</div>
	</div>
</div>

<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(
		function() {

			global.renderPage = function() {
				startDateShow = $(".start").html().replace(/\./g, "-");
				endDateShow = $(".end").html().replace(/\./g, "-");
				if (startDateShow != $("#startDate").val()
						|| endDateShow != $("#endDate").val()) {
					window.location.href = "/cookSum/cookSumStat.do?startDate="
							+ startDateShow + "&endDate=" + endDateShow;
				}
			}
		});
</script>

<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>