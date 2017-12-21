<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jqGrid Demos</title>
<link rel="stylesheet" type="text/css" media="screen"
	href="/jqgrid/themes/redmond/jquery-ui-1.8.1.custom.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="/jqgrid/themes/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="/jqgrid/themes/ui.multiselect.css" />
<style>
html,body {
	margin: 3px; /* Remove body margin/padding */
	padding: 0;
	overflow: auto; /* Remove scroll bars on browser window */
	font-size: 75%;
}
</style>
<script src="/jqgrid/js/jquery.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery-ui-1.8.1.custom.min.js"
	type="text/javascript"></script>
<script src="/jqgrid/js/jquery.layout.js" type="text/javascript"></script>
<script src="/jqgrid/js/i18n/grid.locale-en.js" type="text/javascript"></script>

<script src="/jqgrid/js/ui.multiselect.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery.jqGrid.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery.tablednd.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery.contextmenu.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript" src="/scripts/My97DatePicker/WdatePicker.js"></script>

<script>
	function show_sub(v){
		var day_timeUnit = document.getElementById("day_timeUnit");
		var week_timeUnit = document.getElementById("week_timeUnit");
		var month_timeUnit = document.getElementById("month_timeUnit");
		var custom_timeUnit = document.getElementById("custom_timeUnit");
		var tpid = v ;
		var test_arr = tpid.getAttribute("data1") ;
		if(test_arr == "day"){
			day_timeUnit.style.display="inline";
			week_timeUnit.style.display="none"
			month_timeUnit.style.display="none"
			custom_timeUnit.style.display="none"
		} else if(test_arr == "week"){
			day_timeUnit.style.display="none";
			week_timeUnit.style.display="inline"
			month_timeUnit.style.display="none"
			custom_timeUnit.style.display="none"
		} else if(test_arr == "month"){
			day_timeUnit.style.display="none";
			week_timeUnit.style.display="none"
			month_timeUnit.style.display="inline"
			custom_timeUnit.style.display="none"
		} else if(test_arr == "custom"){
			day_timeUnit.style.display="none";
			week_timeUnit.style.display="none"
			month_timeUnit.style.display="none"
			custom_timeUnit.style.display="inline"
		}
	}
</script>

</head>
<body>
	<%
		request.setCharacterEncoding("utf-8");
	%>
	<form method="post" name="functioninfo_modify" id="modify"
		action="/sysmailset/update.do" title=''
		style="width: 990px; margin: 0px;">
		<fieldset>
			<legend>邮件发送设置</legend>
			<table>
				<tbody>
					<tr>
						<td>Id:</td>
						<td><input type="text" name="id" readonly="readonly"
							value="${sysMailSet.id}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>邮件类别:</td>
						<td><select name="mailType">
                            <option value="MAIL_TYPE_TIME_SEND"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_TIME_SEND'}">selected</c:if>>定时邮件</option>
                            <option value="MAIL_TYPE_DG_MALL_SUM"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_DG_MALL_SUM'}">selected</c:if>>电商汇总报表</option>
                            <option value="MAIL_TYPE_CLIENT_CHANNEL_SUM"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_CLIENT_CHANNEL_SUM'}">selected</c:if>>客户端渠道报表</option>
                            <option value="MAIL_TYPE_CLIENT_CHANNEL_MONITOR"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_CLIENT_CHANNEL_MONITOR'}">selected</c:if>>客户端渠道日监控</option>
                            <option value="MAIL_TYPE_CLIENT_CHANNEL_NEWUSER"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_CLIENT_CHANNEL_NEWUSER'}">selected</c:if>>客户端渠道新增用户</option>
                            <option value="MAIL_TYPE_GROUP_BASE_SUM"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_GROUP_BASE_SUM'}">selected</c:if>>圈子报表</option>
                            <option value="MAIL_TYPE_GAOTIE_SUM"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_GAOTIE_SUM'}">selected</c:if>>高铁报表</option>
                            <option value="MAIL_TYPE_MALL_DAILY_TIME_SUM"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_MALL_DAILY_TIME_SUM'}">selected</c:if>>电商时段订单报表</option>
                            <option value="MAIL_TYPE_MALL_POSITION_SUM"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_MALL_POSITION_SUM'}">selected</c:if>>电商资源位报表</option>
                            <option value="MAIL_TYPE_DISH_BASE_SUM"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_DISH_BASE_SUM'}">selected</c:if>>作品日统计报表</option>
                            <option value="MAIL_TYPE_COOK_SUM"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_COOK_SUM'}">selected</c:if>>菜谱日统计报表</option>
                            <option value="MAIL_TYPE_USER_7DAY_ACTIONS_SUM_MONITOR"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_USER_7DAY_ACTIONS_SUM_MONITOR'}">selected</c:if>>用户-7DAY-网站行为监测报表</option>
                            <option value="MAIL_TYPE_USER_DAY_ACTIONS_SUM_MONITOR"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_USER_DAY_ACTIONS_SUM_MONITOR'}">selected</c:if>>用户-DAY-网站行为监测报表</option>
                            <option value="MAIL_TYPE_HOME_FOCUS_MONITOR"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_HOME_FOCUS_MONITOR'}">selected</c:if>>首页焦点图</option>
                            <option value="MAIL_TYPE_COMMON_TIME_SUM"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_COMMON_TIME_SUM'}">selected</c:if>>通用报表</option>
                            <option value="MAIL_QTYPE_STAT"
                                <c:if test="${sysMailSet.mailType == 'MAIL_QTYPE_STAT'}">selected</c:if>>Qtyp统计报表</option>
                            <option value="MAIL_TYPE_FAMILY_MONITER"
                                <c:if test="${sysMailSet.mailType == 'MAIL_TYPE_FAMILY_MONITER'}">selected</c:if>>家庭统计报表</option>
                            <option value="MAIL_TYPE_INDEX_FLUSH_STAT"
                                <c:if test="${sysMailSet.mailType=='MAIL_TYPE_INDEX_FLUSH_STAT'}">selected</c:if>>首页类型统计报表</option>
                            <option value="MAIL_TYPE_SHOW_PERSON_NUM_STAT"
                                <c:if test="${sysMailSet.mailType=='MAIL_TYPE_SHOW_PERSON_NUM_STAT'}">selected</c:if>>每小时在线总人数数据监控</option>
                            <option value="MAIL_TYPE_CLICK_RATE_STAT"
                                <c:if test="${sysMailSet.mailType=='MAIL_TYPE_CLICK_RATE_STAT'}">selected</c:if>>首页推荐数据点击率</option>
                            <option value="MAIL_TYPE_WORKING_OF_OPERATION_DEPARTMENT_STAT"
                                <c:if test="${sysMailSet.mailType=='MAIL_TYPE_WORKING_OF_OPERATION_DEPARTMENT_STAT'}">selected</c:if>>运营部门工作任务统计报表-总计</option>
                            <option value="MAIL_TYPE_WORKING_OF_OPERATION_BASE_STAT"
                                <c:if test="${sysMailSet.mailType=='MAIL_TYPE_WORKING_OF_OPERATION_BASE_STAT'}">selected</c:if>>运营部门工作任务统计报表-基本</option>
                            <option value="MAIL_TYPE_WORKING_OF_OPERATION_HUDONG_STAT"
                                <c:if test="${sysMailSet.mailType=='MAIL_TYPE_WORKING_OF_OPERATION_HUDONG_STAT'}">selected</c:if>>运营部门工作任务统计报表-互动</option>
                            <option value="MAIL_TYPE_WORKING_OF_OPERATION_DIANSHANG_STAT"
                                <c:if test="${sysMailSet.mailType=='MAIL_TYPE_WORKING_OF_OPERATION_DIANSHANG_STAT'}">selected</c:if>>运营部门工作任务统计报表-电商</option>
                            <option value="MAIL_TYPE_WORKING_OF_OPERATION_WEEK_STAT"
                                <c:if test="${sysMailSet.mailType=='MAIL_TYPE_WORKING_OF_OPERATION_WEEK_STAT'}">selected</c:if>>运营部门工作任务统计报表-周报</option>
							<option value="MAIL_TYPE_INDEX_INTERST_TAGS_STAT"
									<c:if test="${sysMailSet.mailType=='MAIL_TYPE_INDEX_INTERST_TAGS_STAT'}">selected</c:if>>首页兴趣标签统计</option>
							<option value="MAIL_TYPE_LIVE_CLASS_DAILY_TIME_SUM_STAT"
									<c:if test="${sysMailSet.mailType=='MAIL_TYPE_LIVE_CLASS_DAILY_TIME_SUM_STAT'}">selected</c:if>>豆果课堂_分时段销售数据报表</option>
							<option value="MAIL_TYPE_QUIZE_REPLAY_DAILY_TIME_SUM_STAT"
									<c:if test="${sysMailSet.mailType=='MAIL_TYPE_QUIZE_REPLAY_DAILY_TIME_SUM_STAT'}">selected</c:if>>问答每日数据报表</option>
							<option value="MAIL_TYPE_ARTICLES_LIFENUM_STAT"
                                    <c:if test="${sysMailSet.mailType=='MAIL_TYPE_ARTICLES_LIFENUM_STAT'}">selected</c:if>>文章_生活号数据统计报表</option>
							<option value="MAIL_TYPE_PING_YIN_CONVER_STAT"
									<c:if test="${sysMailSet.mailType=='MAIL_TYPE_PING_YIN_CONVER_STAT'}">selected</c:if>>	拼音转换任务执行</option>

						</select>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>主题:</td>
						<td><input type="text" name="subject"
							value="${sysMailSet.subject}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>收件人:</td>
						<td><input type="text" name="mailTo"
							value="${sysMailSet.mailTo}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>发送类型:</td>
						<td><select name="sendType" onchange="show_sub(this.options[this.options.selectedIndex])">
								<option data1='hour' value="hour"
									<c:if test="${sysMailSet.sendType == 'hour'}">selected</c:if>>每小时</option>
								<option data1='day' value="day"
									<c:if test="${sysMailSet.sendType == 'day'}">selected</c:if>>每天</option>
								<option data1='week' value="week"
									<c:if test="${sysMailSet.sendType == 'week'}">selected</c:if>>每周
								</option>
								<option data1='month' value="month"
									<c:if test="${sysMailSet.sendType == 'month'}">selected</c:if>>每月
								</option>
								<option data1='custom' value="custom"
									<c:if test="${sysMailSet.sendType == 'custom'}">selected</c:if>>定制
								</option>
								<option data1='abandoned' value="abandoned"
									<c:if test="${sysMailSet.sendType == 'abandoned'}">selected</c:if>>废弃
								</option>
						</select>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>原始发送时间:</td>
						<td><input disabled="true"　readOnly="true" type="text" value="${sysMailSet.sendTime}"/></td>
					</tr>
					<tr>
						<td>修改发送时间:</td>
						<td id="day_timeUnit" style="display: inline"><input type="text" name="sendTime_day" onClick="WdatePicker({dateFmt:'HH:mm',isShowClear:false,isShowToday:false});" />&nbsp;</td>
						<td id="week_timeUnit" style="display: none"><select type="text" name="sendTime_week">
							<option value="0">周日</option>
							<option value="1">周一</option>
							<option value="2">周二</option>
							<option value="3">周三</option>
							<option value="4">周四</option>
							<option value="5">周五</option>
							<option value="6">周六</option>
						</select>&nbsp;
							<input style="width: 100px" type="text" name="sendTime_week_exactTime" onClick="WdatePicker({dateFmt:'HH:mm',isShowClear:false,isShowToday:false});" />
						</td>
						<td id="month_timeUnit" style="display: none">
							<select type="text" name="sendTime_month">
								<c:forEach begin="1" end="31" var="per_day">
									<option value="${per_day}">${per_day}日</option>
								</c:forEach>
							</select>&nbsp;
							<input style="width: 100px" type="text" name="sendTime_month_exactTime" onClick="WdatePicker({dateFmt:'HH:mm',isShowClear:false,isShowToday:false});" />
						</td>
						<td id="custom_timeUnit" style="display: none"><input type="text" name="sendTime_custom" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false,isShowToday:false});" />&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>描述:</td>
						<td><input type="text" name="desc" value="${sysMailSet.desc}" /></td>
					</tr>
					<tr>
                        <td>配置:</td>
                    	<td>
                    	    <textarea name="config" cols ="85" rows = "20">${sysMailSet.config}</textarea>

<textarea name="config" cols ="85" rows = "17" disabled>
{
    "trace_days": 8,
    "begin_date": "2017-05-01",
    "end_date": "2017-05-03",
	"qtypeStr":"view_tuan_detail",
	"qtypeCh":"严选商品来源详情",
	"position":"7",
    "files": [
        {
            "file_path": "/opt/DATA/goldmine/src/kpi/time_stat/live_class/data/data_class_all.log",
            "title": "直播汇总数据",
            "view_style": "table"
        },
        {
            "file_path": "/opt/DATA/goldmine/src/kpi/time_stat/live_class/data/data_class.log",
            "title": "直播课时数据",
            "view_style": "table"
        }
    ]
}
                                </textarea>

                    	</td>
                    </tr>
					<tr>
						<td><input type="hidden" name="action" value="modify" /></td>
						<td><button
								class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only "
								type="submit">
								<span class="ui-button-text ui-corner-all">提交修改</span>
							</button></td>
					</tr>
				</tbody>
			</table>
		</fieldset>
	</form>
</body>
</html>