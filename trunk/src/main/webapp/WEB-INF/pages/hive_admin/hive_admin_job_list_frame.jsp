<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jqGrid Demos</title>
<link rel="stylesheet" type="text/css" media="screen" href="../jqgrid/themes/redmond/jquery-ui-1.8.1.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../jqgrid/themes/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../jqgrid/themes/ui.multiselect.css" />
<link rel="stylesheet" href="/static/css/lrtk.css" />
<style>
html,body {
	margin: 0; /* Remove body margin/padding */
	padding: 0;
	overflow: hidden; /* Remove scroll bars on browser window */
	font-size: 75%;
}
</style>
<script src="/jqgrid/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript" src="/static/js/jquery.imgbox.pack.js"></script>
<script src="/jqgrid/js/jquery-ui-1.8.1.custom.min.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery.layout.js" type="text/javascript"></script>
<script src="/jqgrid/js/i18n/grid.locale-en.js" type="text/javascript"></script>

<script src="/jqgrid/js/ui.multiselect.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery.jqGrid.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery.tablednd.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery.contextmenu.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript" src="/scripts/My97DatePicker/WdatePicker.js"></script>


<script type="text/javascript">
	$(document).ready(function() {
		$("#yulan").imgbox({
			'speedIn'		: 0,
			'speedOut'		: 0,
			'alignment'		: 'center',
			'overlayShow'	: true,
			'allowMultiple'	: true
		});
	});
</script>

<script>
	var nullValue = "" ;
	
	function check_timeGap(){
		var dslMsg = document.getElementById("dslMsg").value ;
		var dpMsg = document.getElementById("dpMsg").value ;
		var dwpMsg = document.getElementById("dwpMsg").value ;
		if(nullValue != dslMsg){
			alert(dslMsg) ;
		}
		if(nullValue != dpMsg){
			alert(dpMsg) ;
		}
		if(nullValue != dwpMsg){
			alert(dwpMsg) ;
		}
		document.getElementById("dslMsg").value=nullValue;
		document.getElementById("dpMsg").value=nullValue;
		document.getElementById("dwpMsg").value=nullValue;
	}
	
	function check_mailStatus(){
		var queryMailSuccessMsg = document.getElementById("queryMailSuccessMsg").value ;
		var checkMailSuccessMsg = document.getElementById("checkMailSuccessMsg").value ;
		if(nullValue != queryMailSuccessMsg){
			alert(queryMailSuccessMsg) ;
		}
		if(nullValue != checkMailSuccessMsg){
			alert(checkMailSuccessMsg) ;
		}
		document.getElementById("queryMailSuccessMsg").value=nullValue;
		document.getElementById("checkMailSuccessMsg").value=nullValue;
	}
	
	function show_sub(v){   
		var jobTypeContent = document.getElementById("jobTypeContent");
		var tpid = v ;
		var test_arr = tpid.getAttribute("data1") ;
		document.getElementById("showVarTitle").innerHTML=tpid.getAttribute("data1")+": ";
		if(test_arr == "select"){
			document.getElementById("showVarTitle").innerHTML=("");
			jobTypeContent.style.visibility="hidden";
		} else if(test_arr == "无"){
			document.getElementById("showVarTitle").innerHTML=("无参数");
			jobTypeContent.style.visibility="hidden";
		} else if(test_arr != null){
            document.getElementById("showVarTitle").innerHTML=test_arr+":";
            jobTypeContent.style.visibility="visible";
		} else{
			jobTypeContent.style.visibility="visible";
		}
		
		var myselect=document.getElementById("jobType");
		  var index=myselect.selectedIndex ;
		  var a=myselect.options[index].value;
		  var yulan=document.getElementById("yulan");
		  var url="/images/jobyulan/"+a+".jpg";
		  yulan.setAttribute("href",url);
		  yulan.setAttribute("title","JOb DESC JOb DESC JOb DESC JOb DESC ");
	}
	
	function delete_confirm(){
		var deleteConfirmMsg = document.getElementById("deleteConfirmMsg").value ;
		if(nullValue!=deleteConfirmMsg){
			
		}
	}
	

</script>
</head>
<body>
	<div>
		<input name="dslMsg" id="dslMsg" type="hidden" value="${dslMsg}" />
		<input name="dpMsg" id="dpMsg" type="hidden" value="${dpMsg }" />
		<input name="dwpMsg" id="dwpMsg" type="hidden" value="${dwpMsg }" />
		<input name="queryMailSuccessMsg" id="queryMailSuccessMsg" type="hidden" value="${queryMailSuccessMsg}" />
		<input name="checkMailSuccessMsg" id="checkMailSuccessMsg" type="hidden" value="${checkMailSuccessMsg}" />
		<input name="deleteConfirmMsg" id="deleteConfirmMsg" type="hidden" value="${deleteConfirmMsg}" />
	</div>
	<form method="post" name="functioninfo_add" id="add"
		action="/hiveadmin/insert.do" title=''
		style="width: 1000px; margin: 0px;">
		
		
		<fieldset>
			<table>
				<tbody>
					<tr style="width: 480px">
						<td>任务名称:</td>
						<td colspan="3"><input type="text" name="jobName" style="width: 413px" /></td>
					</tr>
					<tr style="width: 480px; height: 50px">
						<td>Job类别:</td>
						<td><select id="jobType" name="jobType" style="width: 171px" onchange="show_sub(this.options[this.options.selectedIndex])">
								<option data1='select' value="0" selected="selected">请选择模板</option>
								<option data1='URL' value="1">主站pv统计</option>
								<option data1='URL' value="2">Wap站pv统计</option>
								<option data1='无' value="99">人工定制</option>
								<c:forEach items="${templateList}" var="template">
									<option data1='${template.showVarTitle}'  title='${template.templateDesc}'  value="${template.templateUid}">${template.templateName}</option>
								</c:forEach>
						</select>&nbsp;</td>
						
						<td><p id="showVarTitle"></p></td>
						<td><input type="text" name="jobTypeContent" id="jobTypeContent" style="visibility: hidden;" />&nbsp;</td>
						
					</tr>
					<tr style="width: 480px">
						<td>统计开始:</td>
						<td><input type="text" class="Wdate" id="statBeginTime" name="statBeginTime" onClick="WdatePicker();"/>&nbsp;</td>
						<td>统计结束:</td>
						<td><input type="text" class="Wdate" id="statEndTime" name="statEndTime" onClick="WdatePicker({skin:'whyGreen',maxDate:'%y-%M-%d'});"/></td>
					</tr>
					<tr style="width: 480px">
						<td><input type="hidden" name="action" value="add" /></td>
						<td><button
								class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only "
								type="submit"
								onclick="">
								<span class="ui-button-text ui-corner-all">新增</span>
							</button>
							<button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" type="button" id="yulan" title="" href=""><span class="ui-button-text ui-corner-all">预览</span></button></td>
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
										'统计开始', '统计结束', '任务启动时间', '操作' ],
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
									width : 110,
									align : "center",
									sortable : false
								}, {
									name : 'uid',
									index : 'uid',
									width : 60,
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
									width : 180,
									align : "center",
									sortable : false
								}

								],
								height : 230,
								rowNum : 100,
								rowList : [ 100, 500, 1000 ],
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
										$(window).width() * 0.90);
								$("#tb_function").setGridHeight(
										$(window).height() * 0.60);
								check_timeGap();
								check_mailStatus();
							});
					$(window).bind(
							'resize',
							function() {
								$("#tb_function").setGridWidth(
										$(window).width() * 0.90);
								$("#tb_function").setGridHeight(
										$(window).height() * 0.50);
							});
					$("#tb_function tbody").delegate(".dojob",'click',function(){
						if($(this).attr("data-running") == '3'){
							$(this).css("color",'grey');
							return;
						}else{
							$(this).attr("data-running" , 3);
							$(this).css("color",'grey');
							console.log($(this));
							window.location.href = "/hiveadmin/execJob.do?jobId="+$(this).attr("data-jid");
						}
					});

					$("#tb_function tbody").delegate(".viewstatdata",'click',function(){
						if($(this).attr("data-running") != '5'){
                        	$(this).css("color",'grey');
                            	return;
						}else{
                        	$(this).attr("data-running" , 5);
                            $(this).css("color",'grey');
							console.log($(this));
                    		window.location.href = "/hiveadmin/viewStatData.do?jobId="+$(this).attr("data-jid");
                        }
                    });
					
					$("#tb_function tbody").delegate(".deleteJob",'click',function(){
						if(window.confirm('删除是不可恢复的哦，确认要删除这个任务吗？')){
	                        $(this).css("color",'grey');
							console.log($(this));
	                   		window.location.href = "/hiveadmin/deleteJob.do?jobId="+$(this).attr("data-jid");
			                 return true;
			              }else{
			                 return false;
			    		}
                    });

				});
		
		
	</script>
</body>
</html>


