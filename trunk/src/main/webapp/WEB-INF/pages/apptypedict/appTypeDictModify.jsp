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
                      <form method="post" name="functioninfo_modify" id="modify" action="/apptypedict/update.do" title='' style="width:350px;margin:0px;"> 
                          <fieldset> 
                              <legend>app类型词典</legend> 
                              <table> 
                                       <tbody> 
                                           <tr>
                                                 <td> Id:</td> 
                                                 <td><input type="text" name="id" value="${appTypeDict.id}" /></td> 
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> type:</td> 
                                                 <td><input type="text" name="type" value="${appTypeDict.type}" /></td> 
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> data:</td> 
                                                 <td>
                                                 	<select name="data">
                                                       <option value ="1" <c:if test="${appTypeDict.data == '1'}">selected</c:if> >1</option>
                                                       <option value ="2" <c:if test="${appTypeDict.data == '2'}">selected</c:if> >2</option>
                                                       <option value ="3" <c:if test="${appTypeDict.data == '3'}">selected</c:if> >3</option>
                                                      </select>&nbsp;
                                                 </td> 
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> val:</td> 
                                                 <td>
                                                      <select name="val">
                                                       <option value ="1" <c:if test="${appTypeDict.val == '1'}">selected</c:if> >1</option>
                                                       <option value ="2" <c:if test="${appTypeDict.val == '2'}">selected</c:if> >2</option>
                                                       <option value ="3" <c:if test="${appTypeDict.val == '3'}">selected</c:if> >3</option>
                                                       <option value ="4" <c:if test="${appTypeDict.val == '4'}">selected</c:if> >4</option>
                                                       <option value ="5" <c:if test="${appTypeDict.val == '5'}">selected</c:if> >5</option>
                                                       <option value ="6" <c:if test="${appTypeDict.val == '6'}">selected</c:if> >6</option>
                                                       <option value ="7" <c:if test="${appTypeDict.val == '7'}">selected</c:if> >7</option>
                                                       <option value ="8" <c:if test="${appTypeDict.val == '8'}">selected</c:if> >8</option>
                                                       <option value ="9" <c:if test="${appTypeDict.val == '9'}">selected</c:if> >9</option>
                                                       <option value ="10" <c:if test="${appTypeDict.val == '10'}">selected</c:if> >10</option>
                                                      </select>&nbsp;
                                                 </td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> name:</td>
                                                 <td><input type="text" name="name" value="${appTypeDict.name}" /></td> 
                                           </tr>
                                           <tr>
                                                 <td> desc:</td> 
                                                 <td><input type="text" name="valDesc" value="${appTypeDict.valDesc}" /></td> 
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