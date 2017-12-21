<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../includes/header_new.jsp"></jsp:include>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jqGrid Demos</title>
<link rel="stylesheet" type="text/css" media="screen" href="/jqgrid/themes/redmond/jquery-ui-1.8.1.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/jqgrid/themes/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/jqgrid/themes/ui.multiselect.css" />
<style>
	html,body {
		margin: 0; /* Remove body margin/padding */
		padding: 0;
		/*overflow: hidden;*/ /* Remove scroll bars on browser window */
		font-size: 75%;
	}
</style>
<script src="/jqgrid/js/jquery.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery-ui-1.8.1.custom.min.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery.layout.js" type="text/javascript"></script>
<script src="/jqgrid/js/i18n/grid.locale-en.js" type="text/javascript"></script>

<script src="/jqgrid/js/ui.multiselect.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery.jqGrid.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery.tablednd.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery.contextmenu.js" type="text/javascript"></script>
</head>
<body>
<br>
<br>

<br/>
<form action="${pageContext.request.contextPath}/vipuser/manage/exportExcel.do" method="post">
	<fieldset>
		<table>
			<tbody>

			<tr>
				<td colspan="3">
					<input type="button" onclick="gridReload()" id="submitButton" class="" value="  查 询  "/>
					&nbsp;&nbsp;
					<input type="submit" value="导出数据"><br>
				</td>
			</tr>

			<tr>
				<td>用户id:</td>
				<td><input type="text" id="userId" name="userId" value="${userId}"/ /></td>

				<td>&nbsp;&nbsp;UID:</td>
				<td><input type="text" id="userName" name="userName" value="${userName}"/ />&nbsp;</td>

				<td>&nbsp;&nbsp;昵称:</td>
				<td><input type="text" id="nickName" name="nickName" value="${nickName}"/ />&nbsp;</td>
			</tr>

			</tbody>
		</table>
	</fieldset>
</form>


<br/>
<table id="tb_function", align:"center" >
</table>
<div id="pager2"></div>
<script>
	$(document).ready(
			function() {
				jQuery("#tb_function").jqGrid(
						{
							url : '/userInfo/manage/queryJson.do?${queryCondition}',
							datatype : "json",
							colNames : [ '用户id' ,'UID' ,'昵称','性别','手机', 'email',
								'私信' ,'修改' ,'查看','删除' ],
							colModel : [ {
								name : 'userId',
								index : 'userId',
								width : 100,
								align : "center",
								sortable : true
							}, {
								name : 'userName',
								index : 'userName',
								width : 150,
								align : "center",
								sortable : true
							}, {
								name : 'nickName',
								index : 'nickName',
								width : 150,
								align : "center",
								sortable : true
							}, {
								name : 'sex',
								index : 'sex',
								width : 80,
								align : "center",
								sortable : false

							},{
								name : 'mobile',
								index : 'mobile',
								width : 180,
								align : "center",
								sortable : false
							},{
								name : 'email',
								index : 'email',
								width : 200,
								align : "center",
								sortable : false
							}, {
								name : 'msgbox',
								index : 'msgbox',
								width : 80,
								align : "center",
								sortable : false
							},{
								name : 'modify',
								index : 'modify',
								width : 80,
								align : "center",
								sortable : false
							},{
								name : 'show',
								index : 'show',
								width : 80,
								align : "center",
								sortable : false
							},{
								name : 'delete',
								index : 'delete',
								width : 80,
								align : "center",
								sortable : false
							}
							],
							height : 500,
							rowNum : 50,
							rowList : [ 50 ],
							pager : '#pager2',
							sortname : 'id',
							viewrecords : true,
							sortorder : "desc",
							loadonce : false, //设置自动翻页为真
							scroll:false,//当为true时，翻页栏被禁用
							multiselect: false,
							viewrecords: true,
							caption : "User Info Managerer"
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

							gridReload();
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
	function gridReload(){
		var userId = $("#userId").val();
		var userName = $("#userName").val();
		var nickName = $("#nickName").val();

		jQuery("#tb_function").jqGrid('setGridParam',
				{
					url:"/userInfo/manage/queryJson.do?userId="+userId+"&userName="+userName+"&nickName="+nickName,
					page:1
				}
		).trigger("reloadGrid");
	}
</script>

</body>
</html>