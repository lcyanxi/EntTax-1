<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
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
</head>
<body>
	<table id="tb_function",align:"center" ></table>
	<div id="pager2"></div>
	<script>
		$(document).ready(
				function() {
					jQuery("#tb_function").jqGrid(
							{
								url : '/appclickdict/queryJson.do',
								datatype : "json",
								colNames : [ 'Id', '页面', '视图', '位置',
										'名称', '描述', '修改' ],
								colModel : [ {
									name : 'Id',
									index : 'Id',
									width : 100,
									align : "center",
									sortable : false
								}, {
									name : 'page',
									index : 'page',
									width : 100,
									align : "center",
									sortable : false
								}, {
									name : 'view',
									index : 'view',
									width : 100,
									align : "center",
									sortable : false
								}, {
									name : 'position',
									index : 'position',
									width : 100,
									align : "center",
									sortable : false
								}, {
									name : 'title',
									index : 'title',
									width : 100,
									align : "center",
									sortable : false
								}, {
									name : 'titleDesc',
									index : 'titleDesc',
									width : 200,
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
								caption : "app点击词典管理"
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

	<form method="post" name="functioninfo_add" id="add"
		action="/appclickdict/insert.do" title=''
		style="width: 550px; margin: 0px;">
		<fieldset>
			<table>
				<tbody>
					<tr>
						<td>页面:</td>
						<td><input type="text" name="page" /></td>
						<td>视图:</td>
						<td><input type="text" name="view" />&nbsp;</td>
						<td>位置:</td>
						<td><input type="text" name="position" />&nbsp;</td>
					</tr>
					<tr>
						<td>名称:</td>
						<td><input type="text" name="title" /></td>
						<td>描述:</td>
						<td colspan="3"><input type="text" name="titleDesc" /></td>
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
</body>
</html>


