<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>jqGrid Demos</title>
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
              <%
                request.setCharacterEncoding("utf-8");
    
           /*              String userID=request.getParameter("userID");
                        String selSql = "SELECT userID,userName FROM usersinfo where userID=?";
                        String[][] data = con.query(selSql,new String[]{userID});
                        String[][] arryFun =data; */
              %>
                      <form method="post" name="userinfo_edit" id="modify" action="/user/edit.do" title='' style="width:350px;margin:0px;"> 
                          <fieldset> 
                              <legend>用户信息</legend> 
                              <table> 
                                       <tbody> 
                                           <tr>
                                                 <td> userID:</td> 
                                                 <td><input type="text" name="uid" value="${user.uid}" /></td> 
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> userName:</td> 
                                                 <td><input type="text" name="username" value="${user.username}" /></td> 
                                           </tr> 
                                           <tr>
                                                  <td><input type="hidden" name="action" value="edit" /></td>
                                                  <td><button class="submit" type="submit">提交修改</button></td> 
                                           </tr>
                                     </tbody> 
                           </table> 
                   </fieldset> 
              </form>
	</body>
</html>


