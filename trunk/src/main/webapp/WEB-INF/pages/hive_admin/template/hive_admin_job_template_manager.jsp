<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../../includes/header_new.jsp"></jsp:include>
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
	<br/>
	<form method="post" name="functioninfo_add" id="add"
		action="/hiveadmin/tplt/insert.do" title=''
		style="width: auto; margin: 0px;">
		<fieldset>
			<table>
				<tbody>
					<tr>
						<td>&nbsp;&nbsp;&nbsp;模板标识:</td>
						<td>
							<input type="text" name="templateUid" />
						</td>
                        <td>&nbsp;&nbsp;&nbsp;模板名称:</td>
						<td>
							<input type="text" name="templateName" />
						</td>
                        <td>&nbsp;&nbsp;&nbsp;排序:</td>
						<td>
							<input type="text" name="sort" />
						</td>
					</tr>
					<tr>
                        <td>&nbsp;&nbsp;&nbsp;列表标题:</td>
						<td>
							<input type="text" name="showListTitle" />
						</td>
                        <td>&nbsp;&nbsp;&nbsp;变量标题:</td>
						<td>
							<input type="text" name="showVarTitle" />
						</td>
                        <td>&nbsp;&nbsp;&nbsp;模板分组:</td>
						<td>
							<input type="text" name="templateGroup" />
						</td>
					</tr>
					<tr>
                        <td>&nbsp;&nbsp;&nbsp;模板内容:</td>
						<td colspan="5">
						    <textarea cols="70" rows="5" name="templateContent" ></textarea>
						</td>
					</tr>
					<tr>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;描述:</td>
						<td colspan="5">
						    <textarea cols="70" rows="5" name="templateDesc" ></textarea>
						</td>
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
	<br/>
	<table id="tb_function",align:"center" ></table>
	<div id="pager2"></div>
	<script>
		$(document).ready(
				function() {
					jQuery("#tb_function").jqGrid(
							{
								url : '/hiveadmin/tplt/queryJson.do',
								datatype : "json",
								colNames : [ 'Id', '标识', '名称', '分组', '排序', '描述', '创建时间', '修改' ],
								colModel : [ {
									name : 'Id',
									index : 'Id',
									width : 30,
									align : "center",
									sortable : false
								}, {
									name : 'templateUid',
									index : 'templateUid',
									width : 150,
									align : "center",
									sortable : true
								}, {
                                	name : 'templateName',
                                	index : 'templateName',
                                	width : 150,
                                	align : "center",
                                	sortable : true
                                }, {
                                    name : 'templateGroup',
                                    index : 'templateGroup',
                                    width : 150,
                                    align : "center",
                                    sortable : true
								}, {
									name : 'sort',
									index : 'sort',
									width : 60,
									align : "center",
									sortable : true
								}, {
									name : 'templateDesc',
									index : 'templateDesc',
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
								rowNum : 10,
								rowList : [ 10, 20, 30 ],
								pager : '#pager2',
								sortname : 'id',
								viewrecords : true,
								sortorder : "desc",
								loadonce : true, //设置自动翻页为真
								caption : "模板管理"
							});
					jQuery("#tb_function").jqGrid('navGrid', '#pager2', {
						edit : false,
						add : false,
						del : false,
						autowidth : true,
						aotoheight : true
					});

					$(window).bind(
							'load',
							function() {
								//$("#tb_function").setGridWidth(
								//		$(window).width() * 0.90);
								//$("#tb_function").setGridHeight(
								//		$(window).height() * 0.65);
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