<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>jqGrid Demos</title>
		<link rel="stylesheet" type="text/css" media="screen" href="../jqgrid/themes/redmond/jquery-ui-1.8.1.custom.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="../jqgrid/themes/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="../jqgrid/themes/ui.multiselect.css" />
		<style>
			html, body {
				margin: 0;			/* Remove body margin/padding */
				padding: 0;
				overflow: hidden;	/* Remove scroll bars on browser window */	
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
		<table id="tb_function",align:"center" ></table>
		<div id="pager2"></div>
		<script>
		$(document).ready(function(){
			jQuery("#tb_function").jqGrid({
			   	url:'/fun/queryJson.do',
				datatype: "json",
			   	colNames:['functionId', 'functionName', 'URL','type','typeId','修改'],
			   	colModel:[
			   		{name:'functionId',index:'functionId', width:80,align:"center",sortable:false},
			   		{name:'functionName',index:'functionName', width:250,align:"center",sortable:false},
			   		{name:'URL',index:'URL', width:300,align:"center",sortable:false},
			   		{name:'type',index:'type', width:80, align:"center",sortable:false},
			   		{name:'typeId',index:'typeId', width:80, align:"center",sortable:false},
			   		{name:'modify',index:'modify', width:80, align:"center",sortable:false}
			   			
			   	],
			   	height:300,
			   	rowNum:500,
			   	rowList:[100,500,1000],
			   	pager: '#pager2',
			   	sortname: 'id',
			    viewrecords: true,
			    sortorder: "desc",
	                    loadonce: true,                     //设置自动翻页为真
			    caption:"Function信息管理"
			});
			jQuery("#tb_function").jqGrid('navGrid','#pager2',{edit:false,add:false,del:false,autowidth:true});

	                $(window).bind('load', function() {
	                    $("#tb_function").setGridWidth($(window).width()*0.90);
	                    $("#tb_function").setGridHeight($(window).height()*0.65);
	                 });
	                $(window).bind('resize', function() {
	                    $("#tb_function").setGridWidth($(window).width()*0.90);
	                    $("#tb_function").setGridHeight($(window).height()*0.50);
	                 });
		});
		

		</script>

                      <form method="post" name="functioninfo_add" id="add" action="/fun/insert.do" title='' style="width:550px;margin:0px;">
                          <fieldset>
                              <table>
                                       <tbody>
                                           <tr>
                                                 <td>FunctionID:</td>
                                                 <td><input type="text" name="functionId"   /></td>
                                                 <td>FunctionName:</td>
                                                 <td><input type="text" name="functionName" /></td>
                                           </tr>
                                           <tr>
                                                 <td>URI:</td>
                                                 <td><input type="text" name="uri"   /></td>
                                                 <td>type:</td>
                                                 <td>
                                                      <select name="typeId">
                                                       <option value ="0">系统设置</option>
                                                       <option value ="1">全网数据</option>
                                                      </select>&nbsp; 
                                                 </td>
                                           </tr>
                                           <tr>
                                                 <td><input type="hidden" name="action" value="add" /></td>
                                                 <td><button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only " type="submit">
                                                   <span class="ui-button-text ui-corner-all">新增</span></button></td>
                                           </tr>
                                     </tbody>
                             </table>
                       </fieldset>
                    </form>
	</body>
</html>


