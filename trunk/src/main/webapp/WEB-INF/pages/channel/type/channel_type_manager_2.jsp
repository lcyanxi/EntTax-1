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
	<div class="leftCol" style="width: 490px;">
		<div class="mod-select">
			<div class="tabpanel">
				<ul id="toptab" class="borders">
					<li class=<c:if test="${level == '1'}"> on</c:if>><a
						href="/channel/type/preList/1.do?level=1">渠道大类</a></li>
					<li class=<c:if test="${level == '2'}"> on</c:if>><a
						href="/channel/type/preList/2.do?level=2">渠道类别</a></li>
					<li class=<c:if test="${level == '3'}"> on</c:if>><a
						href="/channel/type/preList/3.do?level=3">渠道小类</a></li>
				</ul>
			</div>
		</div>
	</div>
	<br/>
	<form method="post" name="functioninfo_add" id="add"
		action="/channel/type/insert.do" title=''
		style="width: 550px; margin: 0px;">
		<fieldset>
			<table>
				<tbody>
					<tr>
						<td>渠道类别名称:</td>
						<td>
							<input type="text" name="typeName" />
							<input type="hidden" name="level" id="level" value="${level}">
						</td>
						<td>排序:</td>
						<td>
							<input type="text" name="sort" />
						</td>
					</tr>
					<tr>
						<td>渠道大类:</td>
						<td>
							<select name="parentId">
								<c:forEach items="${parentList}" var="parentType">
									<option value="${parentType.id}">${parentType.typeName}</option>
								</c:forEach>							
							</select>
						</td>
						<td>描述:</td>
						<td colspan="3"><input type="text" name="typeDesc" /></td>
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
								url : '/channel/type/queryJson.do?level=${level}',
								datatype : "json",
								colNames : [ 'Id', '名称', '排序',
										'描述', '创建时间', '修改' ],
								colModel : [ {
									name : 'Id',
									index : 'Id',
									width : 30,
									align : "center",
									sortable : false
								}, {
									name : 'type_name',
									index : 'type_name',
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
									name : 'typeDesc',
									index : 'typeDesc',
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
								caption : "渠道类别管理"
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