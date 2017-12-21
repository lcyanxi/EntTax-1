<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<head>
		<title>事件管理</title>
		<script type="text/javascript" src="/static/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="/static/js/jquery-ui.custom.js"></script>
		<link type="text/css" rel="stylesheet" href="/static/css/jquery-ui.css" />
		<script type="text/javascript" src="/static/js/flexigrid.pack.js"></script>
		<link type="text/css" rel="stylesheet" href="/static/css/flexigrid.pack.css" />
		
		<style>
			body { font-size: 70%; }
		</style>
		<script type="text/javascript">
			$(function() {
		        $.ajax({
		        	type: "POST",
		            url: '/manage/getapps.do',
		            dataType: 'json',
		            success: function(data) {
		            	appName = data;
		            	if (data != null) {
			            	if (data.length > 0) {
			            		var app_id = document.getElementById("app_id");
				                for(var o in data){
				              		var opt = new Option(data[o].name,data[o].id);
				              		app_id.options.add(opt);
		    					}  
			            	}
		            	}
		            },
		            error: function(data) {
		            	alert('获取应用列表失败!');
		            }
		        });
			});
		</script>
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
								var selectValue = $("select[name='app_id']").val();
								if(isBlank(selectValue)){
									alert("表单填写错误，有表单项为空,请选择所属应用！");
									flag = false;
								}
							}
							
							if(flag){
								var selectValue = $("select[name='status']").val();
								if(isBlank(selectValue)){
									alert("表单填写错误，有表单项为空,请选择状态！");
									flag = false;
								}
							}
							
							if(flag){
								var params=$("#dialog-form #userform").serialize();
								var tit = $(this).dialog('option', 'title');
								var url = "/manage/version/";
								if (tit == "添加事件") {
									url += "add.do";
								} else if (tit == "编辑事件") {
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
		<div id="dialog-form" title="版本管理" style="font-size: 62.5%;">
			<form id="userform">
				<table>
					<tr>
						<td>
							<label for="app_id">应用名称</label>
						</td>
						<td>
							<select id="app_id" name="app_id" style="width:250px">
							</select>
						</td>
					</tr>
					<tr>
						<td>
							<label for="app_version">版本</label>
						</td>
						<td>
							<input type="text" name="app_version" id="app_version" style="width:250px" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="status">状态</label>
						</td>
						<td>
							<select id="status" name="status" style="width:250px">
								<option value="1">有效</option>
								<option value="2">无效</option>
							</select>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</body>

	<script type="text/javascript">
		$(".mytable-class").flexigrid({
			url : '/manage/version/list.do',
			method:'POST',
			dataType : 'json',
			colModel : [ 
						 { display : 'id', name : 'id', width : 90, sortable : true, align : 'left' },
			             { display : '应用名称', name : 'app_id', width : 220, sortable : true, align : 'left', process : transform}, 
			             { display : '版本', name : 'app_version', width : 200, sortable : true, align : 'left' }, 
						 { display : '状态', name : 'status', width : 80, sortable : true, align : 'right', process:transformStatus } 
			],
			buttons : [ 
			             { name : '添加', bclass : 'add', onpress : handleClick }, 
						 { name : '编辑', bclass : 'edit', onpress : handleClick }, 
						 { name : '删除', bclass : 'delete', onpress : handleClick}, 
						 { separator : true } 
			],
			searchitems : [ 
			                { display : 'ID', name : 'id' }, 
			                { display : '版本', name : 'app_version', isdefault : true} 
			],
			sortname : "id",
			sortorder : "asc",
			usepager : true,
			title : '版本管理',
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
		
		function transform(celDiv, pid){
		    var text = $(celDiv).html();
		    if (text == "&nbsp;") {
		    	 $(celDiv).html('');;
		    }
        	if (appName != null) {
            	if (appName.length > 0) {
	                for(var o in appName){
	                	if (appName[o].id == text) {
	                		$(celDiv).html(appName[o].name);
	                		break;
	                	}
					}  
            	}
        	}
		}
		
		function transformStatus(celDiv, pid){
		    var text = $(celDiv).html();
		    if (text == '1') {
		    	$(celDiv).html('有效');
		    } else {
		    	$(celDiv).html('无效');
		    }
		}
	
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
					var actionUrlStr ='/manage/version/delete.do';
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
				id = $('.trSelected').children('td').eq(0).children('div').html();
				app_id = $('.trSelected').children('td').eq(1).children('div').html();
				app_version = $('.trSelected').children('td').eq(2).children('div').html();
				status = $('.trSelected').children('td').eq(3).children('div').html();
				
				if (app_id == "&nbsp;"){
					$('#app_id').val(1); 
				} else {
					var count=$("#app_id option").length;
					for(var i=0;i<count;i++)  {
						if($("#app_id").get(0).options[i].text == app_id) {  
					       $("#app_id").get(0).options[i].selected = true;  
					       break;  
					    }  
					} 
				}
				
				if (app_version == "&nbsp;") {
					$('#app_version').val("");
				} else {
					$('#app_version').val(app_version);
				}
				
				if (status == "&nbsp;"){
					$('#status').val(1); 
				} else {
					var count=$("#status option").length;
					for(var i=0;i<count;i++)  {
						if($("#status").get(0).options[i].text == status) {  
					       $("#status").get(0).options[i].selected = true;  
					       break;  
					    }  
					} 
				}
				
				$("#dialog-form").data("id",id);
				$("#dialog-form").dialog({title:"编辑事件"});  
				$("#dialog-form").dialog("open");
			} else if (com == '添加') {
				$("#dialog-form").dialog({title:"添加事件"});  
				$("#dialog-form").dialog("open");
			}
		}
	</script>
</html>