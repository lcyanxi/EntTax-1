<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.douguo.dc.util.PHPSerializer"%>
<%@ page language="java" import="com.douguo.dc.util.TypeUtil"%>
<%@ page language="java" import="com.douguo.dc.util.Cast"%>
<%@ page language="java" import="org.springframework.ui.ModelMap"%>

<jsp:include page="../includes/header.jsp"></jsp:include>

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
									<li class=<c:if test="${tab == 'DAU'}"> on</c:if>><a
										href="/appdau/daulist.do?startDate=${startDate}&endDate=${endDate}&app=3&tab=DAU">APP日活统计</a></li>
									<li class=<c:if test="${tab == 'CHANNEL'}"> on</c:if>><a
										href="/appdau/daudetaillist.do?startDate=${startDate}&endDate=${endDate}&app=4&tab=CHANNEL">APP日活渠道分析</a></li>
									<li class=<c:if test="${tab == 'VER'}"> on</c:if>><a
										href="/appdau/dauverlist.do?startDate=${startDate}&endDate=${endDate}&tab=VER">APP日活版本分析</a></li>
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
		            'echarts/chart/pie',
		            'echarts/chart/line',
		            'echarts/chart/funnel',
		            'echarts/chart/map'
		        ],
		        function (ec) {
		            //--- 折柱 ---
		            var myChart = ec.init(document.getElementById('main'));
		            var idx = 1;
		            myChart.setOption({
		                timeline :<c:out value="${timeline}" escapeXml="false"/>,
		                options : [
		                    {
		                        title : {
		                            text: '日活版本占比变化',
		                            subtext: ''
		                        },
		                        tooltip : {
		                            trigger: 'item',
		                            formatter: "{a} <br/>{b} : {c} ({d}%)"
		                        },
		                        calculable : true,
		                        legend: <c:out value="${legend}" escapeXml="false"/>,
		                        toolbox: {
		                            show : true,
		                            feature : {
		                                mark : {show: true},
		                                dataView : {show: true, readOnly: false},
		                                magicType : {
		                                    show: true, 
		                                    type: ['pie', 'funnel'],
		                                    option: {
		                                        funnel: {
		                                            x: '25%',
		                                            width: '50%',
		                                            funnelAlign: 'left',
		                                            max: 1700
		                                        }
		                                    }
		                                },
		                                restore : {show: true},
		                                saveAsImage : {show: true}
		                            }
		                        },
		                        series : <c:out value="${firstSeries}" escapeXml="false"/>
		                    },
		                     <c:out value="${series}" escapeXml="false"/>
		                    
		                ]
		            });
		        }
		    );
		    </script>
    		<!-- 图形展示 end -->
			<div class="wrap-table">
				<div class="mod mod1 parent-table" id="active_user-detail-table">
					<div class="mod-header radius">
						<h2>
							日活统计 <a class="icon help poptips"
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
									<th>app</th>
									<th>版本</th>
									<th>日活数</th>
									<th>统计日期</th>
								</tr>
							</thead>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="appdau">
									<tr>
										<td><c:out value="${appdau.app}" /></td>
										<td><c:out value="${appdau.vers}" /></td>
										<td><a title="点一下又不会怀孕，^_^" href="/appdau/daudetaillist.do?app=${appdau.app}&startDate=${appdau.statdate}&endDate=${appdau.statdate}"><c:out value="${appdau.uid}" /></a></td> 
										<td><c:out value="${appdau.statdate}" /></td>
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
						<p>日活统计</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="startDate" value="${startDate}" />
<input type="hidden" id="endDate" value="${endDate}" />
<input type="hidden" id="tab" value="${tab}" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>

<script type="text/javascript">
				window.global.renderPage = function() {
					startDateShow = $(".start").html().replace(/\./g, "-");
					endDateShow = $(".end").html().replace(/\./g, "-");
					if (startDateShow != $("#startDate").val()
							|| endDateShow != $("#endDate").val()) {
						window.location.href = "/appdau/dauverlist.do?startDate="
								+ startDateShow + "&endDate=" + endDateShow
								+ "&appId=${globalAppid}";
					}
				}
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>