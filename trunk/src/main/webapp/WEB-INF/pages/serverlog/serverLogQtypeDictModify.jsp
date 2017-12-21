<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
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
				margin: 30px;			/* Remove body margin/padding */
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
              %>
                      <form method="post" name="functioninfo_modify" id="modify" action="/slog/qtypedict/update.do" title='' style="width:350px;margin:0px;"> 
                          <fieldset> 
                              <legend>Server Log Qtype词典</legend> 
                              <table> 
                                       <tbody> 
                                           <tr>
                                                 <td> Id:</td> 
                                                 <td><input type="text" name="id" readonly="readonly" value="${slogQtypeDict.id}" /></td> 
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> qtype:</td> 
                                                 <td><input type="text" name="qtype" value="${slogQtypeDict.qtype}" /></td> 
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> 名称:</td> 
                                                 <td>
	                                                 <input type="text" name="qtypeName" value="${slogQtypeDict.qtypeName}" />
                                                 </td> 
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> 业务:</td> 
                                                 <td>
                                                 	<select name="service">
														<option value="caipu" <c:if test="${slogQtypeDict.service == 'caipu'}">selected</c:if>>caipu</option>
														<option value="mall" <c:if test="${slogQtypeDict.service == 'mall'}">selected</c:if>>mall</option>
														<option value="comment" <c:if test="${slogQtypeDict.service == 'comment'}">selected</c:if>>评论</option>
														<option value="dish" <c:if test="${slogQtypeDict.service == 'dish'}">selected</c:if>>作品</option>
														<option value="user" <c:if test="${slogQtypeDict.service == 'user'}">selected</c:if>>用户</option>
														<option value="group" <c:if test="${slogQtypeDict.service == 'group'}">selected</c:if>>圈圈</option>
														<option value="other" <c:if test="${slogQtypeDict.service == 'other'}">selected</c:if>>其他</option>
													</select>
                                                 </td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> 时间:</td>
                                                 <td><input type="text" name="createTime" readonly="readonly" value="${slogQtypeDict.createTime}" /></td> 
                                           </tr>
                                            <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr>
                                                 <td> 描述:</td> 
                                                 <td><input type="text" name="qdesc" value="${slogQtypeDict.qdesc}" /></td> 
                                           </tr>
                                           <tr>
                                                  <td><input type="hidden" name="action" value="modify" /></td>
                                                  <td><button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only " type="submit">
                                                   <span class="ui-button-text ui-corner-all">提交修改</span></button></td> 
                                           </tr>
                                     </tbody> 
                           </table> 
                   </fieldset> 
              </form>
	</body>
</html>