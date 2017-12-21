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
		action="/spider/ip/insert.do" title=''
		style="width: 550px; margin: 0px;">
		<fieldset>
			<table>
				<tbody>
					<tr>
						<td>IP:</td>
						<td><input type="text" name="ip" /></td>
						<td>spider:</td>
						<td>
							<select name="spider">
								<option value="baidu">baidu</option>
								<option value="google">google</option>
								<option value="360">360</option>
								<option value="sougou">sougou</option>
								<option value="shenma">shenma</option>
								<option value="youdao">youdao</option>
								<option value="chinaso">chinaso</option>
								<option value="bing">bing</option>
								<option value="yahoo">yahoo</option>
								<option value="soso">soso</option>
								<option value="qihoo">qihoo</option>
								<option value="msn">msn</option>
								<option value="YandexBot">YandexBot</option>
								<option value="ip">ip</option>
								<option value="rival">竞品爬虫</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>创建时间:</td>
						<td><input type="text" class="Wdate" name="createTime" onClick="WdatePicker();"/></td>
						<td>描述:</td>
						<td colspan="3"><input type="text" name="ipDesc" /></td>
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
								url : '/spider/ip/queryJson.do',
								datatype : "json",
								colNames : [ 'Id', 'IP', 'spider',
										'描述', '创建时间', '修改' ],
								colModel : [ {
									name : 'Id',
									index : 'Id',
									width : 30,
									align : "center",
									sortable : false
								}, {
									name : 'ip',
									index : 'ip',
									width : 150,
									align : "center",
									sortable : true
								}, {
									name : 'spider',
									index : 'spider',
									width : 60,
									align : "center",
									sortable : true
								}, {
									name : 'ipDesc',
									index : 'ipDesc',
									width : 100,
									align : "center",
									sortable : false
								}, {
									name : 'createTime',
									index : 'createTime',
									width : 120,
									align : "center",
									sortable : false
								}, {
									name : 'modify',
									index : 'modify',
									width : 80,
									align : "center",
									sortable : false
								}

								],
								height : 300,
								rowNum : 20,
								rowList : [ 10, 20, 30 ],
								pager : '#pager2',
								sortname : 'id',
								viewrecords : true,
								sortorder : "desc",
								loadonce : true, //设置自动翻页为真
								caption : "爬虫IP管理"
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
										$(window).width() * 0.90);
								$("#tb_function").setGridHeight(
										$(window).height() * 0.65);
							});
					$(window).bind(
							'resize',
							function() {
								$("#tb_function").setGridWidth(
										$(window).width() * 0.90);
								$("#tb_function").setGridHeight(
										$(window).height() * 0.50);
							});
				});
	</script>
</body>
</html>