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
	/*overflow: hidden;*/ /* Remove scroll bars on browser window */
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
	<br>	
	<form method="post" name="functioninfo_add" id="add"
		action="/channel/manage/save.do" title=''
		style="width: 550px; margin: 0px;">
		<fieldset>
			<table>
				<tbody>
					<tr>
						<td>&nbsp;&nbsp;&nbsp;渠道名称:</td>
						<td>
							<input type="text" name="channelName" />
						</td>
						<td>&nbsp;&nbsp;&nbsp;渠道代码:</td>
						<td>
							<input type="text" name="channelCode" />
						</td>
					</tr>
					<tr>
						<td colspan="2">
							&nbsp;
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
	<!-- 
	<form action="${pageContext.request.contextPath}/channel/manage/exportBaseExcel.do" method="post">    
    	<input type="submit" value="导出渠道数据"><br>    
    </form>
     -->
     <br/>
    <form action="${pageContext.request.contextPath}/channel/manage/exportSumExcel.do" method="post">    
    	开始时间 ： <input type="text" name="start_date" readonly="readonly" class="Wdate" onClick="WdatePicker();"/>   
    	结束时间 ： <input type="text" name="end_date" readonly="readonly" class="Wdate" onClick="WdatePicker();"/>   
    	<input type="submit" value="导出渠道综合数据"><br>    
    </form>  
	<br/>
	<table id="tb_function",align:"center" ></table>
	<div id="pager2"></div>
	<script>
		$(document).ready(
				function() {
					jQuery("#tb_function").jqGrid(
							{
								url : '/channel/manage/queryJson.do',
								datatype : "json",
								colNames : [ 'Id', '渠道大类' ,'渠道类别' ,'渠道小类' ,'渠道名称' ,
								             '渠道代码' ,'渠道合集','负责人','对接人' ,'部门','联系方式','合作模式',
								             '合作进度' , '修改' ],
								colModel : [ {
									name : 'Id',
									index : 'Id',
									width : 30,
									align : "center",
									sortable : false
								}, {
									name : 'channel_type_1_name',
									index : 'channel_type_1_name',
									width : 150,
									align : "center",
									sortable : true
								}, {
									name : 'channel_type_2_name',
									index : 'channel_type_2_name',
									width : 60,
									align : "center",
									sortable : true
								}, {
									name : 'channel_type_3_name',
									index : 'channel_type_3_name',
									width : 100,
									align : "center",
									sortable :true 
								}, {
									name : 'channel_name',
									index : 'channel_name',
									width : 100,
									align : "center",
									sortable :true 
								}, {
									name : 'channel_code',
									index : 'channel_code',
									width : 100,
									align : "center",
									sortable :true
								}, {
                                	name : 'channel_tag',
                                	index : 'channel_tag',
                                    width : 100,
                                    align : "center",
                                    sortable :true
								}, {
									name : 'userName',
									index : 'userName',
									width : 100,
									align : "center",
									sortable :true 
								}, {
									name : 'principal',
									index : 'principal',
									width : 100,
									align : "center",
									sortable :true 
								}, {
									name : 'principal_dep',
									index : 'principal_dep',
									width : 100,
									align : "center",
									sortable :true 
								}, {
									name : 'principal_contact',
									index : 'principal_contact',
									width : 100,
									align : "center",
									sortable :true 
								}, {
									name : 'channel_cooperation',
									index : 'channel_cooperation',
									width : 100,
									align : "center",
									sortable :true 
								}, {
									name : 'plan_desc',
									index : 'plan_desc',
									width : 100,
									align : "center",
									sortable :true 
								}, {
									name : 'modify',
									index : 'modify',
									width : 80,
									align : "center",
									sortable : false
								}
								],
								height : 700,
								rowNum : 500,
								rowList : [ 500, 1000, 2000 ],
								pager : '#pager2',
								sortname : 'id',
								viewrecords : true,
								sortorder : "desc",
								loadonce : true, //设置自动翻页为真
								scroll:false,//当为true时，翻页栏被禁用
								caption : "渠道管理"
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
								//$("#tb_function").setGridWidth(
								//		$(window).width() * 0.90);
								//$("#tb_function").setGridHeight(
								//		$(window).height() * 0.65);
							});
					$(window).bind(
							'resize',
							function() {
								//$("#tb_function").setGridWidth(
								//		$(window).width() * 0.90);
								//$("#tb_function").setGridHeight(
								//		$(window).height() * 0.50);
							});
				});
	</script>
</body>
</html>