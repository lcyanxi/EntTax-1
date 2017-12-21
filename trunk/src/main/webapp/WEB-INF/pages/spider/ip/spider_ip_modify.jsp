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
                      <form method="post" name="functioninfo_modify" id="modify" action="/spider/ip/update.do" title='' style="width:350px;margin:0px;"> 
                          <fieldset> 
                              <legend>爬虫IP管理</legend> 
                              <table> 
                                       <tbody> 
                                           <tr>
                                                 <td> Id:</td> 
                                                 <td><input type="text" name="id" readonly="readonly" value="${spiderIp.id}" /></td> 
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> IP:</td> 
                                                 <td><input type="text" name="ip" value="${spiderIp.ip}" /></td> 
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> spider:</td> 
                                                 <td>
                                                 	<select name="spider">
														<option value="baidu" <c:if test="${spiderIp.spider == 'baidu'}">selected</c:if>>baidu</option>
														<option value="google" <c:if test="${spiderIp.spider == 'google'}">selected</c:if>>google</option>
														<option value="360" <c:if test="${spiderIp.spider == '360'}">selected</c:if>>360</option>
														<option value="sogou" <c:if test="${spiderIp.spider == 'sogou'}">selected</c:if>>sougou</option>
														<option value="shenma" <c:if test="${spiderIp.spider == 'shenma'}">selected</c:if>>shenma</option>
														<option value="youdao" <c:if test="${spiderIp.spider == 'youdao'}">selected</c:if>>youdao</option>
														<option value="chinaso" <c:if test="${spiderIp.spider == 'chinaso'}">selected</c:if>>chinaso</option>
														<option value="bing" <c:if test="${spiderIp.spider == 'bing'}">selected</c:if>>bing</option>
														<option value="yahoo" <c:if test="${spiderIp.spider == 'yahoo'}">selected</c:if>>yahoo</option>
														<option value="soso" <c:if test="${spiderIp.spider == 'soso'}">selected</c:if>>soso</option>
														<option value="qihoo" <c:if test="${spiderIp.spider == 'qihoo'}">selected</c:if>>qihoo</option>
														<option value="msn" <c:if test="${spiderIp.spider == 'msn'}">selected</c:if>>msn</option>
														<option value="YandexBot" <c:if test="${spiderIp.spider == 'YandexBot'}">selected</c:if>>YandexBot</option>
														<option value="ip" <c:if test="${spiderIp.spider == 'ip'}">selected</c:if>>ip</option>
														<option value="rival" <c:if test="${spiderIp.spider == 'rival'}">selected</c:if>>rival</option>
													</select>
                                                 </td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> 时间:</td>
                                                 <td><input type="text" name="createTime" readonly="readonly" value="${spiderIp.createTime}" /></td> 
                                           </tr>
                                            <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr>
                                                 <td> 描述:</td> 
                                                 <td><input type="text" name="ipDesc" value="${spiderIp.ipDesc}" /></td> 
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