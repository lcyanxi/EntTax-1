<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="org.springframework.ui.ModelMap"%>


<jsp:include page="../includes/header_new.jsp"></jsp:include>

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
                    <div class="leftCol" style="width: 590px;">
                        <div class="mod-select">
                            <div class="tabpanel">
                                <ul id="toptab" class="borders">
                                    <li class=<c:if test="${type == 'type'}"> on</c:if>><a href="/trends/register_user_type.do?startDate=${startDate}&endDate=${endDate}&type=type">来源统计</a></li>
                                    <li class=<c:if test="${type == 'source'}"> on</c:if>><a href="/trends/register_user.do?startDate=${startDate}&endDate=${endDate}&type=source">日统计</a></li>
                                    <li class=<c:if test="${type == 'week'}"> on</c:if>><a href="/trends/register_week_user.do?startDate=${startDate}&endDate=${endDate}&type=week">周统计</a></li>
                                    <li class=<c:if test="${type == 'month'}"> on</c:if>><a href="/trends/register_month_user.do?startDate=${startDate}&endDate=${endDate}&type=month">月统计</a></li>
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
            <!-- 图形展示 begin -->
            <!--Step:1 Prepare a dom for ECharts which (must) has size (width & hight)-->
            <div id="main" style="height:500px;border:1px solid #ccc;padding:10px;"></div>

            <script src="/scripts/echarts/echarts.js"></script>

            <script type="text/javascript">
                require.config({
                    paths: {
                        echarts: '/scripts/echarts/'
                    }
                });
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
                         /*   legend: {
                                data:['总共','网站','IOS豆果美食','Android豆果美食']
                            },*/
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
                                {
                                    data: ${userData.name}
                                }
                            ],
                            yAxis : [
                                {
                                    type : 'value',
                                    splitArea : {show : true}
                                }
                            ],
                            series :[
                                {
                                    type: "line",
                                    data:${userData.data}
                                }
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
                            日新增注册用户明细<a class="icon help poptips" action-frame="tip_activeTable"
                                        title=""></a>
                        </h2>
                        <div class="option" style="display: none;">
                            <span class="icon export exportCsv" title="导出"></span>
                        </div>
                    </div>
                    <div class="mod-body " id="data-load">
                        <table class="data-load" width="100%" border="0" cellspacing="0">
                            <thead>
                            <tr>
                                <th>日期</th>
                                <th>注册数</th>
                            </tr>
                            </thead>
                            <tbody id="data-list">
                            <c:forEach items="${userData.nameAndValue}" var="user" varStatus="vs">
                                <tr>
                                    <td>${user.dateTime}</td>
                                    <td>${user.total}</td>
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
                        <p>日注册用户明细</p>
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
                window.location.href="/trends/register_user.do?startDate="+startDateShow+"&endDate="+endDateShow+"&type="+type;
            }
        }
    });
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>
