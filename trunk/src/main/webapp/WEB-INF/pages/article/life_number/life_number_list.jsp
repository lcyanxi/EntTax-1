<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="org.springframework.ui.ModelMap"%>


<jsp:include page="../../includes/header_new.jsp"></jsp:include>

<script type="text/javascript">
	I18n.default_locale = "zh";
	I18n.locale = "zh";
	I18n.fallbacks = true;
</script>

<div class="bd clearfix" style="overflow:auto">

	<div id="mainContainer" style="margin-left:0px;">
		<div class="contentCol">

			<input type="hidden" value="active_users" id="action_stats" />

			<div class="operations">
				<div class="bd3">
					<div class="leftCol">
						<div class="mod-select">
							<div class="tabpanel">
							    <!--
								<ul id="toptab" class="borders">
									<li class=<c:if test="${type == 'source'}"> on</c:if>><a href="/lifenumber/list.do?startDate=${startDate}&endDate=${endDate}&type=source">日统计</a></li>
									<li class=<c:if test="${type == 'week'}"> on</c:if>><a href="/lifenumber/weeklist.do?startDate=${startDate}&endDate=${endDate}&type=week">周统计</a></li>
									<li class=<c:if test="${type == 'month'}"> on</c:if>><a href="/lifenumber/monthlist.do?startDate=${startDate}&endDate=${endDate}&type=month">月统计</a></li>
								</ul>
								-->
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
                            // '累计作者数','累计申请入驻数','累计邀请入驻数',
                            data:['新增文章数','运营上传文章数','用户上传文章数','爬取文章数','优质文章数','优质文章占比',
                                  '累计申请入驻数',
                                  '文章浏览量','文章浏览UV',
                                  '文章内关注数','文章点赞数','文章评论数']
                        },
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                //dataView : {show: true, readOnly: false},
                                //magicType : {show: true, type: ['line', 'bar','stack', 'tiled']},
                                magicType : {show: true, type: ['line', 'bar']},
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

            <p></p>

            <!-- table展示 BEGIN -->
			<div class="wrap-table">
				<div class="mod mod1 parent-table" id="active_user-detail-table">
					<div class="mod-header radius">
						<h2>
							生活号数据统计<a class="icon help poptips" action-frame="tip_activeTable"
								title=""></a>
						</h2>
						<div class="option" style="display: none;">
							<span class="icon export exportCsv" title="导出"></span>
						</div>
					</div>
					<div class="mod-body " id="data-load" >
						<table class="data-load" border="0" cellspacing="0">
							<thead>
								<tr>
								    <th>日期</th>

									<th>新增文章数</th>
                                    <th>运营上传文章数</th>
                                    <th>用户上传文章数</th>
                                    <th>爬取文章数</th>
                                    <th>优质文章数</th>
                                    <th>优质文章占比</th>

                                    <!-- <th>累计作者数</th> -->
                                    <th>累计申请入驻数</th>
                                    <!-- <th>累计邀请入驻数</th> -->

                                    <th>文章浏览量</th>
                                    <th>文章浏览UV</th>

                                    <th>文章内关注数</th>
                                    <th>文章点赞数</th>
                                    <th>文章评论数</th>
								</tr>
							</thead>
                            <tbody id="data-list">
                                <c:forEach items="${rowlst}" var="v">
                                    <tr>
                                        <td width="160"><c:out value="${v.statdate}" /></td>

                                        <td width="120"><c:out value="${v.articles}" /></td>
                                        <td width="200"><c:out value="${v.articles_user_upload}" /></td>
                                        <td width="200"><c:out value="${v.articles_upload}" /></td>
                                        <td width="200"><c:out value="${v.articles_grab}" /></td>
                                        <td width="200"><c:out value="${v.quality_articles}" /></td>
                                        <td width="200"><c:out value="${v.quality_rate }" />%</td>

                                        <!-- <td width="150"><c:out value="${v.all_users}" /></td> -->
                                        <td width="200"><c:out value="${v.apply_all_users}" /></td>
                                        <!-- <td width="200"><c:out value="${v.invite_all_users}" /></td> -->

                                        <td width="160"><c:out value="${v.article_views_pv}" /></td>
                                        <td width="160"><c:out value="${v.article_views_uv}" /></td>

                                        <td width="200"><c:out value="${v.article_user_follows}" /></td>
                                        <td width="200"><c:out value="${v.article_user_favs}" /></td>
                                        <td width="200"><c:out value="${v.article_user_cmmts}" /></td>
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
			<!-- table展示 END -->
			
			<div class="">
				<div id="tip_active">
					<div id="tip_activeTrend" class="tips">
						<div class="corner"></div>
						<p>aaa</p>
					</div>
					<div id="tip_activeTable" class="tips">
						<div class="corner"></div>
						<p>
							bbb
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="startDate" value="${startDate }" />
<input type="hidden" id="endDate" value="${endDate }" />
<input type="hidden" id="type" value="${type}" />

<input type="hidden" name="umengAds" id="pos" value="1" />
<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js" type="text/javascript"></script>

<script type="text/javascript">

$(document).ready(function(){
	
	
	global.renderPage = function() {
		type	= $("#type").val();
		startDateShow	= $(".start").html().replace(/\./g,"-");
		endDateShow	= $(".end").html().replace(/\./g,"-");
		if(startDateShow != $("#startDate").val() || endDateShow != $("#endDate").val())
		{
			window.location.href="/article/lifenumber/list.do?startDate="+startDateShow+"&endDate="+endDateShow+"&type="+type;
		}		
	}
});
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../../includes/footer.jsp"></jsp:include>