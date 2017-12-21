<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
            <div class="wrap-table">
                <div class="mod mod1 parent-table" id="active_user-detail-table">
                    <div class="mod-header radius">
                        <h2>
                            考核任务监控<a class="icon help poptips"
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
                                <th>姓名</th>
                                <th>日期</th>
                                <th>作品</th>
                                <th>菜谱</th>
                                <th>发帖</th>
                                <th>菜谱评论</th>
                                <th>用户作品评论</th>
                                <th>用户帖子评论</th>
                                <th>用户问答评论</th>
                            </tr>
                            </thead>
                            <tbody id="data-list">
                            <c:forEach items="${data.task}" var="task" varStatus="vs">
                                <tr>
                                    <td>${task.name}</td>
                                    <td>${task.statdate}</td>
                                    <td>${task.dish_num}</td>
                                    <td>${task.cook_num}</td>
                                    <td>${task.post_num}</td>
                                    <td>${task.cookcomment_num}</td>
                                    <td>${task.dishcomment_num}</td>
                                    <td>${task.postcomment_num}</td>
                                    <td>${task.wendacomment_num}</td>
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
