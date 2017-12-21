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
				overflow: auto;	/* Remove scroll bars on browser window */	
			    font-size: 100%;
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
                      <form method="post" name="functioninfo_modify" id="modify" action="/vipusernew/manage/update.do" title='' style="width:550px;margin:0px;">
                          <fieldset> 
                              <legend>达人管理</legend>
                              <table> 
                                       <tbody> 
                                           <tr>
                                                 <td> Id:</td> 
                                                 <td>
                                                 	<input type="text" name="id" readonly="readonly" value="${vipUserNew.id}" />
                                                 </td> 
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> 用户id:</td>
                                                 <td>
                                                 	<input type="text" name="userId" readonly="readonly" value="${vipUserNew.userId}" />
                                                 </td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> 用户名:</td>
                                                 <td><input type="text" name="userName" readonly="readonly" value="${vipUserNew.userName}" /></td>
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                            <tr>
                                            	<td> 昵称:</td>
                                                <td><input type="text" name="nickname" readonly="readonly" value="${vipUserNew.nickName}" /></td>
                                           </tr>
                                           <tr>
                                           		<td>&nbsp;</td>
                                           </tr>
                                            <tr>
                                                <td>&nbsp;</td>
                                           	</tr>
                                            <tr>
                                                <td> 标签名称:</td>
                                                <td>
                                                    <select name="tagId">
                                                        <option <c:if test='${vipUserNew.tagId==1}'>selected</c:if> value='1'>核心用户</option>
                                                        <option <c:if test='${vipUserNew.tagId==2}'>selected</c:if> value='2'>菜谱达人</option>
                                                        <option <c:if test='${vipUserNew.tagId==3}'>selected</c:if> value='3'>圈圈达人</option>
                                                        <option <c:if test='${vipUserNew.tagId==4}'>selected</c:if> value='4'>马甲</option>
                                                        <option <c:if test='${vipUserNew.tagId==5}'>selected</c:if> value='5'>黑名单</option>
                                                        <option <c:if test='${vipUserNew.tagId==0}'>selected</c:if> value='0'>未分类</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>&nbsp;</td>
                                           	</tr>
                                            <tr>
                                                <td> 负责人:</td>
                                                <td><input type="text" name="principal" value="${vipUserNew.principal}" /></td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr>
                                                  <td>
                                                    <input type="hidden" name="action" value="modify" />
                                                    <input type="hidden" name="queryCondition" value="${queryCondition}" />
                                                  </td>
                                                  <td></td> 
                                           </tr>
                                     </tbody> 
                           </table> 
				<button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only " type="submit">
                <span class="ui-button-text ui-corner-all">提交修改</span></button>
                   </fieldset> 
              </form>
	</body>
</html>