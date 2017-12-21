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
			table.gridtable { font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse; } 
			table.gridtable th { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #dedede; } 
			table.gridtable td { border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff; }
			table.gridtable lable {font-color: #DBDBDB; font-size: 12px}
			input {border-left:0px;border-top:0px;border-right:0px;border-bottom:1px ;}
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
        <div id="Search" style="width:90%">
          <div  class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-state-default">
              <form name="searchForm" action="/userProfile/manage/index.do" method="post">用户ID/IMEI：
              <input type="text" name="rk" />
              <input type="hidden" name="action" value="search"/>
              <button class="ui-button ui-widget ui-state-hover ui-corner-all ui-button-text-only " type="submit">
                  <span class="ui-button-text ui-corner-all">查询</span></button>
              </form>
          </div>
        </div>

          <fieldset>
              <legend style="font-weight: 900; font-size: 20px">用户画像</legend>
                <table>
                    <c:forEach var="up" items="${up}">
                        <tr>
                           <td>${up.key}</td>
                           <td>${up.value}</td>
                        </tr>
                    </c:forEach>
                 </table>
          </fieldset>
	</body>
</html>