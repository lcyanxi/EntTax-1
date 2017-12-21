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
	<form method="post" name="functioninfo_add" id="add"
    		action="/vipuser/manage/save.do" title=''
    		style="width: 550px; margin: 0px;">
    		<fieldset>
    			<table>
    				<tbody>
    					<tr>
    						<td>&nbsp;&nbsp;&nbsp;豆果id/昵称:</td>
							<td>
            					<input type="text" name="uname" />&nbsp;&nbsp;&nbsp;&nbsp;
            					<button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only " type="submit">
                                    <span class="ui-button-text ui-corner-all">新增</span>
                                </button>
                            </td>
    					</tr>
    					<tr>
    						<td><input type="hidden" name="action" value="add" /></td>
    						<td></td>
    					</tr>
    				</tbody>
    			</table>
    		</fieldset>
    	</form>
     <br/>
    <form action="${pageContext.request.contextPath}/vipuser/manage/exportExcel.do" method="post">
        <fieldset>
        			<table>
        				<tbody>
        					<tr>
        						<td>用户id:</td>
        						<td><input type="text" id="userId" name="userId" value="${userId}"/ /></td>

        						<td>UID:</td>
        						<td><input type="text" id="userName" name="userName" value="${userName}"/ />&nbsp;</td>
                                <td>tag:</td>
        						<td>
        						    <select id="tagId" name="tagId">
        								<option value="">全部</option>
        								<option <c:if test="${tagId==1}" >selected</c:if> value="1">核心用户</option>
        								<option <c:if test="${tagId==2}" >selected</c:if> value="2">菜谱达人</option>
        								<option <c:if test="${tagId==3}" >selected</c:if> value="3">圈圈达人</option>
        								<option <c:if test="${tagId==4}" >selected</c:if> value="4">马甲</option>
        								<option <c:if test="${tagId==5}" >selected</c:if> value="5">黑名单</option>
        								<option <c:if test="${tagId==0}" >selected</c:if> value="0">未分类</option>
        						    </select>&nbsp;
        						</td>
                               <td>负责人:</td>
                               <td><input type="text" id="principal" name="principal" value="${principal}"/ />&nbsp;</td>
        					</tr>
        					<tr>
        						<td>昵称:</td>
        						<td><input type="text" id="nickName" name="nickName" value="${nickName}"/ />&nbsp;</td>
        						<td>生育状况:</td>
        						<td>
        						    <select id="hasChild" name="hasChild">
                                        <option value="">全部</option>
                                        <option <c:if test="${hasChild=='YES'}" >selected</c:if> value="YES">有孩</option>
                                        <option <c:if test="${hasChild=='NO'}" >selected</c:if> value="NO">无孩</option>
                                    </select>&nbsp;
        						</td>
        						<td>省:</td>
        						<td><input type="text" id="province" name="province" value="${province}"/ />&nbsp;</td>
        						<td>市:</td>
        						<td><input type="text" id="city" name="city" vvalue="${city}"/ />&nbsp;</td>
        					</tr>
        					<tr>
        					    <td>
        					        擅长领域：
        					    </td>
                                <td colspan=6>
                                 	<label><input name="domain" <c:if test="${domain1==1}" >checked=checked</c:if> type="checkbox" value="1" />烘焙</label>&nbsp;&nbsp;
                                    <label><input name="domain" <c:if test="${domain2==1}" >checked=checked</c:if> type="checkbox" value="2" />家常菜</label>&nbsp;&nbsp;
                                    <label><input name="domain" <c:if test="${domain3==1}" >checked=checked</c:if> type="checkbox" value="3" />创意菜</label>&nbsp;&nbsp;
                                    <label><input name="domain" <c:if test="${domain4==1}" >checked=checked</c:if> type="checkbox" value="4" />宝宝餐</label>&nbsp;&nbsp;
                                    <label><input name="domain" <c:if test="${domain5==1}" >checked=checked</c:if> type="checkbox" value="5" />早餐</label>&nbsp;&nbsp;
                                    <label><input name="domain" <c:if test="${domain6==1}" >checked=checked</c:if> type="checkbox" value="6" />西餐</label>&nbsp;&nbsp;
                                    <label><input name="domain" <c:if test="${domain7==1}" >checked=checked</c:if> type="checkbox" value="7" />全能</label>&nbsp;&nbsp;
                                    <label><input name="domain" <c:if test="${domain8==1}" >checked=checked</c:if> type="checkbox" value="8" />其他</label>&nbsp;&nbsp;
                                </td>
        						<td >
        						 	<input type="button" onclick="gridReload()" id="submitButton" class="" value="  查 询  "/>
                                    &nbsp;&nbsp;
                                    <input type="submit" value="导出数据"><br>
        						</td>
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
								url : '/vipuser/manage/queryJson.do?${queryCondition}',
								datatype : "json",
								colNames : [ 'Id', '用户id' ,'UID' ,'头像' ,'昵称' ,
								             '真实姓名' ,'地址','手机','国家' ,'省',
								             '市','微博地址', '标签名称','qq群' ,'负责人','生育状况',
								             '创建时间', '私信' ,'修改' ,'查看','删除' ],
								colModel : [ {
									name : 'Id',
									index : 'Id',
									width : 30,
									align : "center",
									sortable : false
								}, {
									name : 'user_id',
									index : 'user_id',
									width : 100,
									align : "center",
									sortable : true
								}, {
									name : 'username',
									index : 'username',
									width : 150,
									align : "center",
									sortable : true
								}, {
									name : 'headicon',
									index : 'headicon',
									width : 37,
									align : "center",
									sortable :true 
								}, {
									name : 'nickname',
									index : 'nickname',
									width : 100,
									align : "center",
									sortable :true 
								}, {
									name : 'real_name',
									index : 'real_name',
									width : 70,
									align : "center",
									sortable :true
								}, {
                                	name : 'address',
                                	index : 'address',
                                    width : 150,
                                    align : "center",
                                    sortable :true
								}, {
									name : 'mobile',
									index : 'mobile',
									width : 90,
									align : "center",
									sortable :true 
								}, {
									name : 'country',
									index : 'country',
									width : 70,
									align : "center",
									sortable :true 
								}, {
									name : 'province',
									index : 'province',
									width : 70,
									align : "center",
									sortable :true 
								}, {
									name : 'city',
									index : 'city',
									width : 70,
									align : "center",
									sortable :true 
								}, {
									name : 'weibo_url',
									index : 'weibo_url',
									width : 120,
									align : "center",
									sortable :true 
								}, {
									name : 'tag_name',
									index : 'tag_name',
									width : 70,
									align : "center",
									sortable :true
								}, {
                                	name : 'qq_group',
                                	index : 'qq_group',
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
                                    name : 'has_child',
                                    index : 'has_child',
                                 	width : 100,
                                    align : "center",
                                    sortable :true
                                }, {
                                    name : 'createtime',
                                    index : 'createtime',
                                    width : 140,
                                    align : "center",
                                    sortable :true
								}, {
									name : 'modify',
									index : 'modify',
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
								}
								,{
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
								caption : "达人管理"
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
                	var province = $("#province").val();
                	var city = $("#city").val();
                	var tagId = $("#tagId").val();
                	var principal = $("#principal").val();
                	var hasChild = $("#hasChild").val();
                	//var domain = $("#domain").val();
                	var domain="";
                    //$("[name='domain'][domain]").each(function(){
                    $("input:checkbox[name='domain']:checked").each(function(){
                        domain+=$(this).val()+",";
                    })

                	jQuery("#tb_function").jqGrid('setGridParam',
                	                                {
                	                                    url:"/vipuser/manage/queryJson.do?userId="+userId+"&userName="+userName+"&nickName="+nickName+"&province="+province+"&city="+city+"&tagId="+tagId+"&principal="+principal+"&hasChild="+hasChild+"&domain="+domain,
                	                                    page:1
                	                                }
                	                             ).trigger("reloadGrid");
                }
	</script>
</body>
</html>