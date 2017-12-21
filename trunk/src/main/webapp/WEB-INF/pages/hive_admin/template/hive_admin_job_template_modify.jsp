<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">--%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>jqGrid Demos</title>
		<link rel="stylesheet" type="text/css" media="screen" href="/jqgrid/themes/redmond/jquery-ui-1.8.1.custom.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="/jqgrid/themes/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="/jqgrid/themes/ui.multiselect.css" />
<%--		<style>
			html, body {
				margin: 30px;			/* Remove body margin/padding */
				padding: 0;
				overflow: hidden;	/* Remove scroll bars on browser window */	
			    font-size: 75%;
			}
		</style>--%>
<%--		<script src="/jqgrid/js/jquery.js" type="text/javascript"></script>
		<script src="/jqgrid/js/jquery-ui-1.8.1.custom.min.js" type="text/javascript"></script>
		<script src="/jqgrid/js/jquery.layout.js" type="text/javascript"></script>
		<script src="/jqgrid/js/i18n/grid.locale-en.js" type="text/javascript"></script>
		
		<script src="/jqgrid/js/ui.multiselect.js" type="text/javascript"></script>
		<script src="/jqgrid/js/jquery.jqGrid.js" type="text/javascript"></script>
		<script src="/jqgrid/js/jquery.tablednd.js" type="text/javascript"></script>
		<script src="/jqgrid/js/jquery.contextmenu.js" type="text/javascript"></script>--%>

        <link href="/datashow/echarts3-7/css/bootstrap-reset.css" rel="stylesheet">
        <link href="/datashow/echarts3-7/css/bootstrap.min.css" rel="stylesheet">
        <link href="/datashow/echarts3-7/css/style-responsive.css" rel="stylesheet">
        <link href="/datashow/echarts3-7/font-awesome/css/font-awesome.css" rel="stylesheet">
	</head>
	<body>
              <%
                request.setCharacterEncoding("utf-8");
              %>
              <div  style="overflow-y:auto; overflow-x:auto; width:1200px; height:800px;">
                  <form method="post" name="functioninfo_modify" id="modify" action="/hiveadmin/tplt/update.do" title=''>
                      <fieldset>
                          <legend>模板管理</legend>
                          <div class="form-group has-success">
                              <label class="col-lg-1 control-label">Id:</label>
                              <div class="col-lg-11">
                                  <input type="text" name="id" readonly="readonly" value="${template.id}"  class="form-control" autofocus>
                                  <p class="help-block"></p>
                              </div>
                          </div>
                          <div class="form-group has-success">
                              <label class="col-lg-1 control-label"> 用户:</label>
                              <div class="col-lg-11">
                                  <input type="text" name="uid" readonly="readonly" value="${template.uid}" class="form-control" autofocus/>
                                  <p class="help-block"></p>
                              </div>
                          </div>

                          <div class="form-group has-success">
                              <label class="col-lg-1 control-label">列表标题:</label>
                              <div class="col-lg-11">
                                  <input type="text" name="showListTitle" value="${template.showListTitle}" class="form-control" autofocus />
                                  <p class="help-block"></p>
                              </div>
                          </div>
                          <div class="form-group has-success">
                              <label class="col-lg-1 control-label">变量标题:</label>
                              <div class="col-lg-11">
                                  <input type="text" name="showVarTitle" value="${template.showVarTitle}"  class="form-control" autofocus/>
                                  <p class="help-block"></p>
                              </div>
                          </div>
                          <div class="form-group has-success">
                              <label class="col-lg-1 control-label">模板名称:</label>
                              <div class="col-lg-11">
                                  <input type="text" name="templateName" value="${template.templateName}" class="form-control" autofocus/>
                                  <p class="help-block"></p>
                              </div>
                          </div>
                          <div class="form-group has-success">
                              <label class="col-lg-1 control-label">模板标识:</label>
                              <div class="col-lg-11">
                                  <input type="text" name="templateUid" value="${template.templateUid}" class="form-control" autofocus />
                                  <p class="help-block"></p>
                              </div>
                          </div>
                          <div class="form-group has-success">
                              <label class="col-lg-1 control-label">模板分组:</label>
                              <div class="col-lg-11">
                                  <input type="text" name="templateGroup" value="${template.templateGroup}" class="form-control" autofocus />
                                  <p class="help-block"></p>
                              </div>
                          </div>

                          <div class="form-group has-success">
                              <label class="col-lg-1 control-label"> 排序:</label>
                              <div class="col-lg-11">
                                  <input type="text" name="sort" value="${template.sort}" class="form-control" autofocus/>
                                  <p class="help-block"></p>
                              </div>
                          </div>
                          <div class="form-group has-success">
                              <label class="col-lg-1 control-label">模板内容:</label>
                              <div class="col-lg-11">
                                  <textarea rows=12 cols=100 name="templateContent"   class="form-control" autofocus>${template.templateContent}</textarea>
                                  <p class="help-block"></p>
                              </div>
                          </div>
                          <div class="form-group has-success">
                              <label class="col-lg-1 control-label">描述:</label>
                              <div class="col-lg-11">
                                  <textarea rows='3' cols='50' name="templateDesc" class="form-control" autofocus>${template.templateDesc}</textarea>
                                  <p class="help-block"></p>
                              </div>
                          </div>
                          <div class="modal-footer">
                              <button type="reset" class="btn btn-default" >取消</button>
                              <button type="submit" class="btn btn-warning" >
                                  <span class="ui-button-text ui-corner-all">提交修改</span></button>
                          </div>
                          <%--   <table width="1500px">
                                      <tbody>
                                          <tr>
                                                <td> Id:</td>
                                                <td width="1000px">
                                                    <input type="text" name="id" readonly="readonly" value="${template.id}"  width="1000px"/>
                                                </td>
                                          </tr>
                                          <tr>
                                                <td> 用户:</td>
                                                <td>
                                                    <input type="text" name="uid" readonly="readonly" value="${template.uid}" />
                                                </td>
                                          </tr>
                                          <tr>
                                                <td> 列表标题:</td>
                                                <td>
                                                    <input type="text" name="showListTitle" value="${template.showListTitle}" />
                                                </td>
                                          </tr>
                                          <tr>
                                                <td> 变量标题:</td>
                                                <td>
                                                    <input type="text" name="showVarTitle" value="${template.showVarTitle}" />
                                                </td>
                                          </tr>
                                          <tr>
                                                <td> 模板名称:</td>
                                                <td>
                                                    <input type="text" name="templateName" value="${template.templateName}" />
                                                </td>
                                          </tr>
                                          <tr>
                                                <td> 模板标识:</td>
                                                <td>
                                                    <input type="text" name="templateUid" value="${template.templateUid}" />
                                                </td>
                                          </tr>
                                          <tr>
                                                <td> 模板分组:</td>
                                                <td>
                                                    <input type="text" name="templateGroup" value="${template.templateGroup}" />
                                                </td>
                                          </tr>
                                          <tr>
                                                <td> 排序:</td>
                                                <td><input type="text" name="sort" value="${template.sort}" /></td>
                                          </tr>
                                          <tr>
                                                <td> 模板内容:</td>
                                                <td>
                                                   <textarea rows=5 cols=50 name="templateContent" >${template.templateContent}</textarea>
                                                </td>
                                          </tr>
                                          <tr>
                                                <td> 描述:</td>
                                                <td>
                                                   <textarea rows='5' cols='50' name="templateDesc">${template.templateDesc}</textarea>
                                                </td>
                                          </tr>
                                          <tr>
                                                 <td><input type="hidden" name="action" value="modify" /></td>
                                                 <td><button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only " type="submit">
                                                  <span class="ui-button-text ui-corner-all">提交修改</span></button></td>
                                          </tr>
                                    </tbody>
                          </table> --%>
                      </fieldset>
                  </form>
              </div>

    </body>
</html>


