<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="com.douguo.dc.util.PHPSerializer"%>
<%@ page language="java" import="com.douguo.dc.util.TypeUtil"%>
<%@ page language="java" import="com.douguo.dc.util.Cast"%>
<%@ page language="java" import="org.springframework.ui.ModelMap"%>

<jsp:include page="../includes/header_new.jsp"></jsp:include>
<style type="text/css">
      #scrollTable {
        width:100%;
      }
      /*表头 div的第一个子元素**/
      #scrollTable table.thead{ 
        width:100%;
      }
      /*能带滚动条的表身*/
      /*div的第二个子元素*/
      #scrollTable div{
        width:100%;
        height:500px;
        overflow:auto;/*必需*/
      }
      /*能带滚动条的表身的正体*/
      #scrollTable table.tbody{
        width:100%;
      }
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
					<div class="leftCol" style="width: 590px;">
						<div class="mod-select">
							<div class="tabpanel">
								<ul id="toptab" class="borders">
									<li class=<c:if test="${tab == 'DAU_ALL'}"> on</c:if>><a
											href="/appdau/daulistAll.do?startDate=${startDate}&endDate=${endDate}&app=3&tab=DAU_ALL">APP日活总统计</a></li>
									<li class=<c:if test="${tab == 'DAU'}"> on</c:if>><a
											href="/appdau/daulist.do?startDate=${startDate}&endDate=${endDate}&app=3&tab=DAU">APP日活区分统计</a></li>
									<li class=<c:if test="${tab == 'CHANNEL'}"> on</c:if>><a
											href="/appdau/daudetaillist.do?startDate=${startDate}&endDate=${endDate}&app=4&tab=CHANNEL">APP日活渠道分析</a></li>
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
		                legend: <c:out value="${legend}" escapeXml="false"/>,
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
							渠道日活统计 <a class="icon help poptips"
								action-frame="tip_activeTable" title=""></a>
						</h2>
						<div class="option" style="display: none;">
							<span class="icon export exportCsv" title="导出"></span>
						</div>
					</div>
					<div class="mod-body " id="data-load">
						<div id="scrollTable">
						<table class="data-load thead" width="100%" border="0" cellspacing="0">
							<col width="10%"></col>
							<col width="8%"></col>
							<col width="7%"></col>
							<col width="7%"></col>
							<col width="7%"></col>
							<col width="10%"></col>
							<col width="7%"></col>
							<col width="7%"></col>
							<col width="10%"></col>
							<col width="10%"></col>
							<col width="7%"></col>
							<col width=""></col>
							<thead>
								<tr>
									<th>渠道</th>
									<th>日活数</th>
									<th>作品数</th>
									<th>主贴数</th>
									<th>回复数</th>
									<th>查看菜谱数</th>
									<th>订单数</th>
									<th>成交数</th>
									<th>质量</th>
									<th>用户贡献度</th>
									<th>分数</th>
									<th>统计日期</th>
								</tr>
							</thead>
						</table>
						<div>
						<table class="data-load tbody">
							 <col width="10%"></col>
                                                        <col width="8%"></col>
                                                        <col width="7%"></col>
                                                        <col width="7%"></col>
                                                        <col width="7%"></col>
                                                        <col width="10%"></col>
                                                        <col width="7%"></col>
                                                        <col width="7%"></col>
                                                        <col width="10%"></col>
                                                        <col width="10%"></col>
                                                        <col width="7%"></col>
                                                        <col width=""></col>
							<tbody id="data-list">
								<c:forEach items="${rowlst}" var="appdau" varStatus="ast">
									<c:if test="${ast.first}">
										<c:set var="scores" value='100' />
										<c:set var="frs" value='${appdau.rs}' />
										<c:set var="fst_date" value='${appdau.statdate}' />
									</c:if>
									<c:set var="lst_date" value='${appdau.statdate}' />
									<c:if test="${!ast.first}">
										<c:if test="${fst_date != lst_date}">	
											<c:set var="scores" value='100' />
											<c:set var="frs" value='${appdau.rs}' />
											<c:set var="fst_date" value='${appdau.statdate}' />
										</c:if>
										<c:if test="${fst_date == lst_date}">
											<c:set var="scores" value='${(appdau.rs * 100) /frs}' />
										</c:if>
									</c:if>
									
									<tr>
										<td><c:out value="${appdau.channel}" /></td>
										<td><c:out value="${appdau.uid}" /></td>
										<td><c:out value="${appdau.dishs}" /></td>
										<td><c:out value="${appdau.posts}" /></td>
										<td><c:out value="${appdau.replys}" /></td>
										<td><c:out value="${appdau.view_cooks}" /></td>
										<td><c:out value="${appdau.orders}" /></td>
										<td><c:out value="${appdau.pays}" /></td>
										<td><c:out value="${appdau.cs}" /></td>
										<td><c:out value="${appdau.rs}" /></td>
										<td><fmt:formatNumber value="${scores}" pattern="##" type="number"/></td>
										<td><c:out value="${appdau.statdate}" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						</div>
						</div>
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
<input type="hidden" id="app" value="${app}" />
<input type="hidden" id="tab" value="${tab}" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
	type="text/javascript"></script>

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						global.renderPage = function() {
							app = $("#app").val();
							startDateShow = $(".start").html().replace(/\./g,
									"-");
							endDateShow = $(".end").html().replace(/\./g, "-");
							if (startDateShow != $("#startDate").val()
									|| endDateShow != $("#endDate").val()) {
								window.location.href = "/appdau/daudetaillist.do?startDate="
										+ startDateShow
										+ "&endDate="
										+ endDateShow + "&app=" + app;
							}
						}
					});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>
