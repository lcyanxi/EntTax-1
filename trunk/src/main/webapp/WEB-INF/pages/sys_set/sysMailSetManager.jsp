<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jqGrid Demos</title>
<link rel="stylesheet" type="text/css" media="screen"
	href="../jqgrid/themes/redmond/jquery-ui-1.8.1.custom.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="../jqgrid/themes/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="../jqgrid/themes/ui.multiselect.css" />
<style>
html,body {
	margin: 0; /* Remove body margin/padding */
	padding: 0;
	overflow: hidden; /* Remove scroll bars on browser window */
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
	<table id="tb_function",align:"center" ></table>
	<div id="pager2"></div>
	<script>
		$(document).ready(function() {
			jQuery("#tb_function").jqGrid({
				url : '/sysmailset/queryJson.do',
				datatype : "json",
				colNames : [ 'Id', '邮件类别', '邮件主题', '收件', '发送类型', '发送时间','描述', '修改', '测试','重发' ],
				colModel : [ {
					name : 'Id',
					index : 'Id',
					width : 50,
					align : "center",
					sortable : false
				}, {
					name : 'mailType',
					index : 'mailType',
					width : 100,
					align : "center",
					sortable : false
				}, {
					name : 'subject',
					index : 'subject',
					width : 100,
					align : "center",
					sortable : false
				}, {
					name : 'mailTo',
					index : 'mailTo',
					width : 100,
					align : "center",
					sortable : false
				}, {
					name : 'sendType',
					index : 'sendType',
					width : 50,
					align : "center",
					sortable : false
				},{
					name : 'sendTime',
					index : 'sendTime',
					width : 80,
					align : "center",
					sortable : false
				}, {
					name : 'desc',
					index : 'desc',
					width : 200,
					align : "center",
					sortable : false
				}, {
					name : 'modify',
					index : 'modify',
					width : 80,
					align : "center",
					sortable : false
				}, {
					name : 'test',
					index : 'test',
					width : 80,
					align : "center",
					sortable : false
				}, {
					name : 'resend',
					index : 'resend',
					width : 80,
					align : "center",
					sortable : false
				}

				],
				height : 300,
				rowNum : 100,
				rowList : [ 20, 50, 100 ],
				pager : '#pager2',
				sortname : 'id',
				viewrecords : true,
				sortorder : "desc",
				loadonce : true, //设置自动翻页为真
				caption : "豆果邮件发送设置"
			});
			jQuery("#tb_function").jqGrid('navGrid', '#pager2', {
				edit : false,
				add : false,
				del : false,
				autowidth : true
			});

			$(window).bind('load', function() {
				$("#tb_function").setGridWidth($(window).width() * 0.90);
				$("#tb_function").setGridHeight($(window).height() * 0.65);
			});
			$(window).bind('resize', function() {
				$("#tb_function").setGridWidth($(window).width() * 0.90);
				$("#tb_function").setGridHeight($(window).height() * 0.50);
			});
		});

	</script>

	<form method="post" name="functioninfo_add" id="add"
		action="/sysmailset/insert.do" title=''
		style="width:900px; margin-top:20px;">
		<fieldset>
			<table>
				<tbody>
					<tr>
						<td>邮件类别:</td>
						<td><select name="mailType">
								<option value="MAIL_TYPE_TIME_SEND">电商汇总报表</option>
								<option value="MAIL_TYPE_DG_MALL_SUM">电商汇总报表</option>
								<option value="MAIL_TYPE_CLIENT_CHANNEL_SUM">客户端渠道报表</option>
								<option value="MAIL_TYPE_CLIENT_CHANNEL_NEWUSER">客户端渠道新用户统计</option>
								<option value="MAIL_TYPE_GROUP_BASE_SUM">圈子汇总报表</option>
								<option value="MAIL_TYPE_GAOTIE_SUM">tmp-高铁报表</option>
								<option value="MAIL_TYPE_MALL_DAILY_TIME_SUM">电商时段订单报表</option>
								<option value="MAIL_TYPE_MALL_POSITION_SUM">电商资源位报表</option>
								<option value="MAIL_TYPE_DISH_BASE_SUM">作品日统计报表</option>
								<option value="MAIL_TYPE_COOK_SUM">菜谱日统计报表</option>
								<option value="MAIL_TYPE_USER_7DAY_ACTIONS_SUM_MONITOR">用户-7DAY-网站行为监测报表</option>
								<option value="MAIL_TYPE_USER_DAY_ACTIONS_SUM_MONITOR">用户-DAY-网站行为监测报表</option>
								<option value="MAIL_TYPE_HOME_FOCUS_MONITOR">首页焦点图</option>
								<option value="MAIL_TYPE_COMMON_TIME_SUM">通用报表</option>
						    	<option value="MAIL_TYPE_FAMILY_MONITER">家庭统计报表</option>
								<option value="MAIL_QTYPE_STAT">Qtype统计报表</option>
							    <option value="MAIL_TYPE_INDEX_FLUSH_STAT">首页类型统计报表</option>
							    <option value="MAIL_TYPE_SHOW_PERSON_NUM_STAT">每小时在线总人数数据监控</option>
							    <option value="MAIL_TYPE_CLICK_RATE_STAT">首页推荐数据点击率</option>
								<option value="MAIL_TYPE_WORKING_OF_OPERATION_DEPARTMENT_STAT">运营部门工作任务统计报表-总计</option>
								<option value="MAIL_TYPE_WORKING_OF_OPERATION_BASE_STAT">运营部门工作任务统计报表-基本</option>
								<option value="MAIL_TYPE_WORKING_OF_OPERATION_HUDONG_STAT">运营部门工作任务统计报表-互动</option>
								<option value="MAIL_TYPE_WORKING_OF_OPERATION_DIANSHANG_STAT">运营部门工作任务统计报表-电商</option>
								<option value="MAIL_TYPE_WORKING_OF_OPERATION_WEEK_STAT">运营部门工作任务统计报表-周报</option>
							    <option value="MAIL_TYPE_INDEX_INTERST_TAGS_STAT">首页兴趣标签统计报表</option>
							    <option value="MAIL_TYPE_LIVE_CLASS_DAILY_TIME_SUM_STAT">豆果课堂_分时段销售数据报表</option>
							    <option value="MAIL_TYPE_QUIZE_REPLAY_DAILY_TIME_SUM_STAT">问答每日数据报表</option>
							    <option value="MAIL_TYPE_ARTICLES_LIFENUM_STAT">文章_生活号数据统计报表</option>
							    <option value="MAIL_TYPE_PING_YIN_CONVER_STAT">拼音转换任务执行</option>



						</select>&nbsp</td>
						<td>邮件主题:</td>
						<td><input type="text" name="subject" />&nbsp;</td>
						<td>邮件收件人:</td>
						<td><input type="text" name="mailTo" value="填写收件人以,隔开" onfocus="if(value=='填写收件人以,隔开') {value=''}" onblur="if (value=='') {value='填写收件人以,隔开'}"/>&nbsp;</td>
					</tr>
					<tr>
						<td>发送类型:</td>
						<td><select id="sendType" name="sendType" onchange="show_sub(this.options[this.options.selectedIndex])">
								<option data1='day' value="day" selected="selected">每天</option>
								<option data1='week' value="week">每周</option>
								<option data1='month' value="month">每月</option>
								<option data1='custom' value="custom">定制</option>
						</select>&nbsp</td>
						<td>发送时间:</td>
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
						<td>描述:</td>
						<td><input type="text" name="desc" /></td>
					</tr>
					<tr>
						<td><input type="hidden" name="action" value="add" /></td>
						<td><button
								class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only "
								type="submit">
								<span class="ui-button-text ui-corner-all">新增</span>
							</button></td>
					</tr>
				</tbody>
			</table>
			
		</fieldset>
	</form>
	
	<div>
		<form method="post" action="/sysmailset/deleteReceiver.do" name="mailReceiver" id="name="mailReceiver" style="width:900px;">
			<fieldset>
				<table>
					<tr>
						离职人员邮箱：<input type="text" id="mailToDelete" name="mailToDelete" />
					</tr>
					<tr>
						<input type="submit" value="确定" />
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
	
</body>
</html>


