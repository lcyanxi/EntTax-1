<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="../includes/header_new.jsp"></jsp:include>

<script type="text/javascript">
    I18n.default_locale = "zh";
    I18n.locale = "zh";
    I18n.fallbacks = true;
</script>
<style type="text/css">
    th{
        text-align:center;
    }
</style>
<div class="bd clearfix">

    <div id="mainContainer" style="margin-left: 0px;">
        <div class="contentCol">
            <div class="operations">
                <div class="bd3">
                    <div class="leftCol" style="width: 70px;">
                        <div class="mod-select">
                        </div>
                    </div>
                    <div class="contentCol">
                        <div class="filterPanel">
                            <div class="datepickerPanel custom1 borders" style="float:none;">
                                <div class="dateselect js-um-datepicker" id="date-select">
                                    <span class="start">${startDate}</span> - <span class="end">${endDate}</span>
                                    <b class="icon pulldown"></b>
                                </div>
                                <div id="proTemp2" style="display: none;">
                                    <div class="mod-body"></div>
                                </div>
                            </div>
                            <input type="button" class="button" value="查询" onclick="uf_query();">
                        </div>
                    </div>
                </div>
            </div>
            <div class="wrap-table" style="OVERFLOW-X: scroll;width: 2486px">
                <div class="mod mod1 parent-table" id="active_user-detail-table">
                    <div class="mod-header radius">
                        <h2>
                            考核任务监控<a class="icon help poptips"
                                   action-frame="tip_activeTable" title="" ></a>
                        </h2>
                    </div>
                    <div class="mod-body " id="data-load">
                        <table class="data-load " width="100%" border="0" cellspacing="0">
                            <thead>
                            <tr>
                                <th colspan="3">基础信息</th>
                                <th colspan="3">菜谱（周）</th>
                                <th colspan="3">作品</th>
                                <th colspan="3">帖子</th>
                                <th colspan="3">菜单</th>
                                <th colspan="3">菜谱评论</th>
                                <th colspan="3">作品评论</th>
                                <th colspan="3">帖子评论</th>
                                <th colspan="3">问答回复</th>
                            </tr>
                            <tr>
                                <th width=60px>时间</th><th width=60px>ID</th><th style="width:60px">姓名</th>
                                <th width=10px>kpi</th><th width=10px>实际</th><th style="width:10px">进度</th>
                                <th width=10px>kpi</th><th width=10px>实际</th><th style="width:10px">进度</th>
                                <th width=10px>kpi</th><th width=10px>实际</th><th style="width:10px">进度</th>
                                <th width=10px>kpi</th><th width=10px>实际</th><th style="width:10px">进度</th>
                                <th width=10px>kpi</th><th width=10px>实际</th><th style="width:10px">进度</th>
                                <th width=10px>kpi</th><th width=10px>实际</th><th style="width:10px">进度</th>
                                <th width=10px>kpi</th><th width=10px>实际</th><th style="width:10px">进度</th>
                                <th width=10px>kpi</th><th width=10px>实际</th><th style="width:10px">进度</th>
                            </tr>
                            </thead>
                            <tbody id="data-list">
                            <c:forEach items="${data.task}" var="task" varStatus="vs">
                                <tr>
                                    <td>${task.statdate}</td>
                                    <td>${task.id}</td>
                                    <td>${task.name}</td>
                                    <td>${task.aim_cook_num}</td>
                                    <td>${task.cook_num}</td>
                                    <td>${task.cook_rate}</td>
                                    <td>${task.aim_dish_num}</td>
                                    <td>${task.dish_num}</td>
                                    <td>${task.dish_rate}</td>
                                    <td>${task.aim_post_num}</td>
                                    <td>${task.post_num}</td>
                                    <td>${task.post_rate}</td>
                                    <td>${task.aim_caidan_num}</td>
                                    <td>${task.caidan_num}</td>
                                    <td>${task.caidan_rate}</td>
                                    <td>${task.aim_cookcomment_num}</td>
                                    <td>${task.cookcomment_num}</td>
                                    <td>${task.cookcomment_rate}</td>
                                    <td>${task.aim_dishcomment_num}</td>
                                    <td>${task.dishcomment_num}</td>
                                    <td>${task.dishcomment_rate}</td>
                                    <td>${task.aim_postcomment_num}</td>
                                    <td>${task.postcomment_num}</td>
                                    <td>${task.postcomment_rate}</td>
                                    <td>${task.aim_questioncomment_num}</td>
                                    <td>${task.questioncomment_num}</td>
                                    <td>${task.questioncomment_rate}</td>
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
                        <p>考核任务监控</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="startDate" value="${startDate}" />
<input type="hidden" id="endDate" value="${endDate}" />

<script src="/static/js/cached_lay_reports.js" type="text/javascript"></script>
<script src="/static/js/cached_lay_reports_cus.js"
        type="text/javascript"></script>

<script type="text/javascript">
    $(document).ready(
        function() {
            global.renderPage = function() {
                uf_query();
            }
        });
    function uf_query(){
        startDateShow = $(".start").html().replace(/\./g, "-");
        endDateShow = $(".end").html().replace(/\./g, "-");
        if (startDateShow != $("#startDate").val()
            || endDateShow != $("#endDate").val()) {
            window.location.href = "/task/queryList.do?startDate="
                + startDateShow + "&endDate=" + endDateShow;
        }
    }
</script>
<script src="/static/js/common.js" type="text/javascript"></script>
<jsp:include page="../includes/footer.jsp"></jsp:include>
