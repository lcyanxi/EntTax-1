<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
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
</head>
<body>
	<form method="post" name="functioninfo_add" id="add"
		action="/hiveadmin/insert.do" title=''
		style="width: 900px; margin: 0px;">
		<fieldset>
			<table>
				<tbody>
					<tr>
						<td>任务名称:</td>
						<td><input type="text" name="jobName" /></td>
						<td>Job类别:</td>
						<td><select name="jobType">
								<option value="999">用户行为统计</option>
						</select>&nbsp;</td>
						<td>类别:</td>
						<td><select name="queryType">
								<option value="1">imei</option>
								<option value="2">uid</option>
								<option value="3">昵称</option>
						</select>&nbsp;</td>
						<td>值:</td>
						<td><input type="text" name="jobTypeContent" />&nbsp;如果需要查询多个，请用英文逗号隔开：001,002,003</td>
					</tr>
					<tr>
						<td>统计开始:</td>
						<td><input type="text" class="Wdate" name="statBeginTime" onClick="WdatePicker();"/>&nbsp;</td>
						<td>统计结束:</td>
						<td><input type="text" class="Wdate" name="statEndTime" onClick="WdatePicker({skin:'whyGreen',maxDate:'%y-%M-%d'});"/></td>
						<td>任务启动时间:</td>
						<td><input type="text" class="Wdate" name="jobStartTime" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d 07:00:00',maxDate:'%y-%M-{%d+7} 21:00:00'});"/></td>
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
	<table id="tb_function",align:"center" ></table>
	<div id="pager2"></div>
	<script>
		$(document).ready(
				function() {
					jQuery("#tb_function").jqGrid(
							{
								url : '/hiveadmin/queryJson.do',
								datatype : "json",
								colNames : [ 'Id', '类型', 'job名称', '创建人','job状态',
										'统计开始', '统计结束', '任务启动时间', '修改' ],
								colModel : [ {
									name : 'Id',
									index : 'Id',
									width : 30,
									align : "center",
									sortable : false
								}, {
									name : 'jobType',
									index : 'jobType',
									width : 35,
									align : "center",
									sortable : true 
								}, {
									name : 'jobName',
									index : 'jobName',
									width : 150,
									align : "center",
									sortable : false
								}, {
									name : 'uid',
									index : 'uid',
									width : 80,
									align : "center",
									sortable : true 
								}, {
									name : 'status',
									index : 'status',
									width : 50,
									align : "center",
									sortable : true 
								}, {
									name : 'statBeginTime',
									index : 'statBeginTime',
									width : 60,
									align : "center",
									sortable : true 
								}, {
									name : 'statEndTime',
									index : 'statEndTime',
									width : 60,
									align : "center",
									sortable : true
								}, {
									name : 'jobStartTime',
									index : 'jobStartTime',
									width : 90,
									align : "center",
									sortable : true
								}, {
									name : 'modify',
									index : 'modify',
									width : 90,
									align : "center",
									sortable : false
								}

								],
								height : 230,
								rowNum : 20,
								rowList : [ 10, 20, 30 ],
								pager : '#pager2',
								sortname : 'id',
								viewrecords : true,
								sortorder : "desc",
								loadonce : true, //设置自动翻页为真
								caption : "豆果数据中心-JOB管理"
							});
					jQuery("#tb_function").jqGrid('navGrid', '#pager2', {
						edit : false,
						add : false,
						del : false,
						autowidth : true
					});

					$(window).bind(
							'load',
							function() {
								$("#tb_function").setGridWidth(
										$(window).width() * 0.95);
								$("#tb_function").setGridHeight(
										$(window).height() * 0.60);
							});
					$(window).bind(
							'resize',
							function() {
								$("#tb_function").setGridWidth(
										$(window).width() * 0.95);
								$("#tb_function").setGridHeight(
										$(window).height() * 0.60);
							});
					$("#tb_function tbody").delegate(".dojob",'click',function(){
						if($(this).attr("data-running") == '3'){
							$(this).css("color",'grey');
							return;
						}else{
							$(this).attr("data-running" , 3);
							$(this).css("color",'grey');
							console.log($(this));
							//window.location.href = "/hiveadmin/execJob.do?jobId="+$(this).attr("data-jid");
							$.get("/hiveadmin/execJob.do?jobId="+$(this).attr("data-jid"));
						}
					});
					$("#tb_function tbody").delegate(".doview",'click',function(){
						if($(this).attr("data-running") != '5'){
							$(this).css("color",'grey');
							return;
						}else{
							$(this).attr("data-running" , 5);
							$(this).css("color",'grey');
							console.log($(this));
							
							window.location.href = "/hiveadmin/viewRes.do?jobId="+$(this).attr("data-jid");
						}
					});

				});
	</script>
</body>
</html>


