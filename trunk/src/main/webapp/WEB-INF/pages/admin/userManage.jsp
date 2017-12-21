<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import ="java.io.IOException" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>用户信息管理</title>
		<link rel="stylesheet" type="text/css" media="screen" href="/jqgrid/themes/redmond/jquery-ui-1.8.1.custom.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="/jqgrid/themes/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="/jqgrid/themes/ui.multiselect.css" />
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
                              <div id="Search" style="width:90%">
                                <div  class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-state-default">
                                        <form name="searchForm" action="/user/manager.do" method="post">用户ID：<input type="text" name="selID" />
                                        <input type="hidden" name="action" value="search"/>
                                        <button class="ui-button ui-widget ui-state-hover ui-corner-all ui-button-text-only " type="submit">
                                            <span class="ui-button-text ui-corner-all">查询</span></button>
                                        </form>
                                </div>
                              </div>
              

              <%
              
                request.setCharacterEncoding("utf-8"); 

                String action = request.getParameter("action");
                String selID=null;
                selID=request.getParameter("selID");
                /**
                if("edit".equals(action))
                {
                    String userID = request.getParameter("userID");
                    if(userID != null && userID.trim().length()>0) {
                       String sql = "select count(1) from usersinfo where userID=?";
                       String[] data = con.queryRow(sql,new String[]{userID});
                       String[][] re = new String[1][2];
                            re[0][0] = userID;
                            re[0][1] = request.getParameter("userName");
                       //判断userID是否存在，若存在则更新编辑信息，若不存在，则新增一个userID
                       if("0".equals(data[0]))
                                  {
                                           sql = "insert into usersinfo (userID,userName) values (?,?)";
                                            con.exec(sql,re);
                                            out.println("新增成功！");
                                   }
                                   else
                                   {
                                                 sql = "update usersinfo set userID=?,userName=? where userID='" + userID + "'";
                                                 con.exec(sql,re);
                                                 out.println("修改成功！");
                                   }                  
      
                    } else
                    {
                       out.println("用户id不能为空！");
                    }
                    
                }
                if("del".equals(action)){   
                String id = request.getParameter("id");        
                  if(id != null && id.trim().length()>0) {                             //删除操作              
                    String[][] re = new String[1][1];                       
                    re[0][0] = id;          
                    String delSql = "delete from usersinfo where userID=?";            //删除usersinfo表中信息
                    con.exec(delSql,re);
                    String del_uf_sql = "delete from userfunctions where userID=?";    //删除userfunctions表中关联信息
                    con.exec(del_uf_sql,re);            
                  }
                }
               else if("add".equals(action)){                                          //添加操作
                    String userID = request.getParameter("userID");
                	if(userID !=null && userID.trim().length()>0)                            //判断添加用户ID是否为空
                  	{
                 		String sql = "select count(1) from usersinfo where userID=?";    //判断要添加的userID是否已经存在
                   		String[] data = con.queryRow(sql,new String[]{userID});
                       		if("0".equals(data[0]))	
                        	{
                			String[][] re = new String[1][3];                        //获取添加用户的信息
                      			re[0][0] = userID;
                  			re[0][1] = request.getParameter("pass");
                 			re[0][2] = request.getParameter("userName");
					sql = "insert into usersinfo (userID,pass,username) values (?,password(?),?)";
                			con.exec(sql,re);
                    			out.println("新增成功！");
		               }
                 		else out.println(userID+"此用户ID已存在，请重新输入！");
                        }
                        else {
                                 out.println("用户ID不能为空！");
                             }
                }
                */
              %>
		<table id="tb_user",align:"center" ></table>
		<div id="pager2"></div>
		<script>

            function showUrlInDialog(url, options){ 
				options = options || {};
			  	$('<div><iframe src="' +url + '" width="100%" frameborder="0" height="100%"></iframe></div>').dialog({
			        width: options.width || 400,
			        height: options.height ||150,
      			    title: options.title || '拷贝权限到新用户'
			    }) .css({padding:0, overflow:'hidden'});
			} 

			jQuery("#tb_user").jqGrid({
			   	url:'/user/queryJson.do?selID=<%=selID%>',
				datatype: "json",
			   	colNames:['用户ID', '用户名称', '编辑','删除','拷贝','授权'],
			   	colModel:[
			   		{name:'uid',index:'uid', width:220,align:"center",sortable:false},
			   		{name:'userName',index:'userName', width:220,align:"center",sortable:false},
			   		{name:'edit',index:'edit', width:100,align:"right",sortable:false},
			   		{name:'delete',index:'delete', width:100, align:"right",sortable:false},
			   		{name:'copy',index:'copy', width:100, align:"right",sortable:false},
			   		{name:'authorize',index:'authorize', width:100, align:"right",sortable:false}		
			   			
			   	],
			   	rowNum:20,
			   	rowList:[10,20,30],
			   	pager: '#pager2',
			   	sortname: 'id',
			    viewrecords: true,
			    sortorder: "desc",                                  //降序排序
	                    loadonce: true,                                     //自动翻页
			    caption:"用户信息管理"
			});
			
			jQuery("#tb_user").jqGrid('navGrid','#pager2',{edit:false,add:false,del:false,autowidth:true});
	                $(window).bind('load', function() {
	                    $("#tb_user").setGridWidth($(window).width()*0.90);
	                    $("#tb_user").setGridHeight($(window).height()*0.65);
	                 });
	                $(window).bind('resize', function() {
	                    $("#tb_user").setGridWidth($(window).width()*0.90);
	                    $("#tb_user").setGridHeight($(window).height()*0.65);
	                 });


		</script>

               <div>
	        <br/>
	        <form name="addForm" action="/user/insert.do" method="post">
			用户ID：<input type="text" name="uid"/>&nbsp;
			密码：<input type="password" name="pass"/>&nbsp;
			用户名称：<input type="text" name="userName"/>&nbsp;
			<input type="hidden" name="action" value="add">
			<button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only " type="submit">
                        <span class="ui-button-text ui-corner-all">新增</span></button>
	       </form>
	      </div>
	      
	      <div>
	      	<form name="authorize" action="/userfun/authorize.do" method="post">
	      		Function_Id : <input type="text" name="authorize_funtion_id"/>&nbsp;
	      		Function_Id_toAdd :  <input type="text" name="authorize_funtion_id_toAdd"/>&nbsp;
	      		<button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only " type="submit">
                        <span class="ui-button-text ui-corner-all">OK</span></button>
	      	</form>
	      </div>
	      
	</body>
</html>


