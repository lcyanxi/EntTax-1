<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<head>
		<title>应用管理</title>
		<script type="text/javascript" src="/static/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="/static/js/jquery-ui.custom.js"></script>
		<link type="text/css" rel="stylesheet" href="/static/css/jquery-ui.css" />
		<script type="text/javascript" src="/static/js/flexigrid.pack.js"></script>
		<link type="text/css" rel="stylesheet" href="/static/css/flexigrid.pack.css" />
		
		<style>
			body { font-size: 70%; }
		</style>

		<script>
			$(function() {
				function trim(str){
					 return str.replace(/(^\s*)|(\s*$)/g, "");
				}
				
				function isBlank(val) {
				     if (val =="" || trim(val) == "") {
				    	 return true;
				     }
				     return false;
				}
		
				$("#dialog-form").dialog({
					autoOpen: false,
					height: 250,
					width: 350,
					modal: true,
					buttons: {
						"保存": function() {
							var flag = true;
						    var inputValues = $("#dialog-form #userform input");
							inputValues.each(function(){
								if(flag)
									if(isBlank(this.value)){
										alert("表单填写错误，有表单项为空！");
										flag = false;
										return false;
									}
							});
							
							if(flag){
								var params=$("#dialog-form #userform").serialize();
								var tit = $(this).dialog('option', 'title');
								var url = "/manage/app/";
								if (tit == "添加应用") {
									url += "add.do";
								} else if (tit == "编辑应用") {
									url += "update.do";
									var id = $(this).data("id");
									params += '&' + 'id=' + id;
								}
								$.ajax({
										type: "POST",
										url: url,
										context: this,
										data: params,
										dataType : "json",
										success: function(data){
											var ret = data.success;
											if ( ret == 'true' ) {
												$('.mytable-class').flexReload();
												$(this).dialog("close");
											} else {
												$(this).dialog("close");
												alert("有错误发生,msg="+data.msg);
											}
										},
										error: function(msg){
											alert("msg="+msg);
										}
								});
							}
						},
						"取消": function() {
							$(this).dialog("close");
						}
					},
					close: function() {
					    var inputValues = $("#dialog-form #userform input");
						inputValues.each(function(){
							$(this).val("");
						});
					}
				});
			});
			</script>
	</head>
	<body >
		<div>
			<table id="mytable" class="mytable-class" style="display: none"></table>
		</div>
		<div id="dialog-form" title="事件管理" style="font-size: 62.5%;">
			<form id="userform">
				<table>
					<tr>
						<td>
							<label for="name">应用名称</label>
						</td>
						<td>
							<input type="text" name="name" id="name" style="width:250px" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="key">KEY</label>
						</td>
						<td>
							<input type="text" name="key" id="key" style="width:250px" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="user_id">UserID</label>
						</td>
						<td>
							<input type="text" name="user_id" id="user_id" style="width:250px" />
						</td>
					</tr>
				</table>
			</form>
		</div>
	</body>

	<script type="text/javascript">
		$(".mytable-class").flexigrid({
			url : '/manage/app/list.do',
			method:'POST',
			dataType : 'json',
			colModel : [ 
						 { display : 'APP_ID', name : 'id', width : 70, sortable : true, align : 'left' },
			             { display : '名称', name : 'name', width : 200, sortable : true, align : 'left' }, 
						 { display : 'KEY', name : 'key', width : 250, sortable : true, align : 'left' }, 
						 { display : 'USER_ID', name : 'user_id', width : 80, sortable : true, align : 'left' },
						 { display : '创建时间', name : 'create_time', width : 200, sortable : true, align : 'left' },
						 { display : '更新时间', name : 'update_time', width : 200, sortable : true, align : 'left' }
			],
			buttons : [ 
			             { name : '添加', bclass : 'add', onpress : handleClick }, 
						 { name : '编辑', bclass : 'edit', onpress : handleClick }, 
						 { name : '删除', bclass : 'delete', onpress : handleClick}, 
						 { separator : true } 
			],
			searchitems : [ 
			                { display : 'APP_ID', name : 'id' }, 
			                { display : '名称', name : 'name', isdefault : true},
			                { display : 'KEY', name : 'key'} 
			],
			sortname : "id",
			sortorder : "asc",
			usepager : true,
			title : '应用管理',
			rpOptions: [5, 10, 15, 20, 30, 40, 100], //可选择设定的每页结果数
			autoload: true, //自动加载，即第一次发起ajax请求
			resizable: true, //table是否可伸缩
			pagestat: '显示 {from} 到 {to}， 共 {total} 条',
			procmsg: '请等待数据正在加载中 …',
			nomsg: '没有数据',
			useRp : true,
			page : 1, 
			rp : 15,
			showToggleBtn: false,
			showTableToggleBtn : false,
			width : 'auto',
			height : 300
		});
		
		function handleClick(com, grid) {
			if (com == '删除') {
				selected_count = $('.trSelected', grid).length;  
				if (selected_count == 0) {  
					alert('请选择一条记录。', '消息提示');  
					return false;  
				}  
				var conf = confirm('Delete ' + selected_count + ' items?');
				if (conf) {
					var ids = '';
					$.each($('.trSelected td:nth-child(1) div', grid), function(value) {
						if (value) {  
						    ids += ',';  
						}  
						ids += $(this).text(); 
					});
					var actionUrlStr ='/manage/app/delete.do';
					$.ajax( {
						type : "POST",
						url : actionUrlStr,
						data : "ids=" + ids,
						dataType : "json",
						success : function(data) {
							var ret = data.success;
							if ( ret == 'true' ) {
								$(".mytable-class").flexReload();
							} else {
								alert(data.msg);
							}
						},
			            error   : function(data) {
			            	alert('删除事件失败，请重试!');
			            }
					});
				}
			} else if (com == '编辑') {
				selected_count = $('.trSelected', grid).length;  
				if (selected_count == 0) {  
					alert('请选择一条记录。', '消息提示');  
					return false;  
				}  
				if (selected_count > 1) {  
					alert('只能编辑一条记录，请只选择一条记录。', '消息提示');  
					return false;  
				}
				var id = $('.trSelected').children('td').eq(0).children('div').html();
				var name = $('.trSelected').children('td').eq(1).children('div').html();
				var key = $('.trSelected').children('td').eq(2).children('div').html();
				var user_id = $('.trSelected').children('td').eq(3).children('div').html();
				
				if (name == "&nbsp;") {
					$('#name').val("");
				} else {
					$('#name').val(name);
				}
				
				if (key == "&nbsp;"){
					$('#key').val("");
				} else {
					$('#key').val(key);
				}
				
				if (user_id == "&nbsp;"){
					$('#user_id').val("");
				} else {
					$('#user_id').val(user_id);
				}
				
				$("#dialog-form").data("id",id);
				$("#dialog-form").dialog({title:"编辑应用"});  
				$("#dialog-form").dialog("open");
			} else if (com == '添加') {
				$("#dialog-form").dialog({title:"添加应用"});  
				$("#dialog-form").dialog("open");
			}
		}
	</script>
</html>