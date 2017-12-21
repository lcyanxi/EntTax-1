<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jqGrid Demos</title>
<link rel="stylesheet" type="text/css" media="screen"
	href="/jqgrid/themes/redmond/jquery-ui-1.8.1.custom.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="/jqgrid/themes/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="/jqgrid/themes/ui.multiselect.css" />
<style>
html,body {
	margin: 30px; /* Remove body margin/padding */
	padding: 0;
	#overflow: hidden; /* Remove scroll bars on browser window */
	font-size: 75%;
}
</style>
<script src="/jqgrid/js/jquery.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery-ui-1.8.1.custom.min.js"
	type="text/javascript"></script>
<script src="/jqgrid/js/jquery.layout.js" type="text/javascript"></script>
<script src="/jqgrid/js/i18n/grid.locale-en.js" type="text/javascript"></script>

<script src="/jqgrid/js/ui.multiselect.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery.jqGrid.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery.tablednd.js" type="text/javascript"></script>
<script src="/jqgrid/js/jquery.contextmenu.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript"
	src="/scripts/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<%
		request.setCharacterEncoding("utf-8");
	%>
	<form method="post" name="functioninfo_modify" id="modify"
		action="/hiveadmin/update.do" title=''
		style="width: 650px; margin: 0px;">
		<fieldset>
			<legend>Job管理</legend>
			<table>
				<tbody>
					<tr>
						<td>任务名称:</td>
						<td><input type="text" name="jobName"
							value="${hiveAdminJob.jobName}" /></td>
						<td><input type="hidden" name="jobId"
							value="${hiveAdminJob.id}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>Job类别:</td>
						<td><select name="jobType">
								<option value="1"
									<c:if test="${hiveAdminJob.jobType == '1'}">selected</c:if>>主站pv统计</option>
								<option value="2"
									<c:if test="${hiveAdminJob.jobType == '2'}">selected</c:if>>Wap站pv统计</option>
								<option value="3"
									<c:if test="${hiveAdminJob.jobType == '3'}">selected</c:if>>圈圈帖子统计-按小时</option>
								<option value="4"
									<c:if test="${hiveAdminJob.jobType == '4'}">selected</c:if>>圈圈帖子统计-按天</option>
								<option value="5"
									<c:if test="${hiveAdminJob.jobType == '5'}">selected</c:if>>APP-菜谱统计</option>
								<option value="6"
									<c:if test="${hiveAdminJob.jobType == '6'}">selected</c:if>>APP-菜单统计</option>
								<option value="99"
									<c:if test="${hiveAdminJob.jobType == '99'}">selected</c:if>>人工定制</option>
								<option value="999"
									<c:if test="${hiveAdminJob.jobType == '999'}">selected</c:if>>用户行为统计</option>
								<c:forEach items="${templateList}" var="template">
                                	<option <c:if test="${hiveAdminJob.jobType == template.templateUid}">selected</c:if> value="${template.templateUid}">${template.templateName}</option>
								</c:forEach>
						</select>&nbsp;</td>
					</tr>
					<c:if test="${hiveAdminJob.jobType == '999'}">
					<tr>
						<td>查询类别:${hiveAdminJob.queryType}</td>
						<td><select name="queryType">
								<option value="1"
									<c:if test="${hiveAdminJob.queryType == '1'}">selected</c:if>>imei</option>
								<option value="2"
									<c:if test="${hiveAdminJob.queryType == '2'}">selected</c:if>>uid</option>
								<option value="3"
									<c:if test="${hiveAdminJob.queryType == '3'}">selected</c:if>>昵称</option>
						</select>&nbsp;</td>
					</tr>
					</c:if>
					<tr>
						<c:if test="${hiveAdminJob.jobType == '5'}">
							<td>菜谱id:</td>
						</c:if>
						<c:if test="${hiveAdminJob.jobType != '5'}">
							<td>url:</td>
						</c:if>
						<td><input type="text" name="jobTypeContent"
							style="width: 350px" value="${hiveAdminJob.jobTypeContent}" /></td>
					</tr>

					<tr>
                    	<td>uid:</td>
                    	<td>
                            <input type="text" name="uid" style="width: 350px" value="${hiveAdminJob.uid}" />
                    	</td>
					</tr>

					<tr>
						<td>hql:</td>
						<td><textarea name="hql" <c:if test="${hiveAdminJob.jobType != '99' || not empty hiveAdminJob.hqlTemplate}">readonly="readonly" </c:if> rows="10" cols="70">${hiveAdminJob.hql}</textarea></td>
					</tr>

					<tr <c:if test="${uid != 'zyz'}">style="display:none"</c:if>>
                    	<td>hql模板:</td>
                    	<td><textarea name="hqlTemplate" <c:if test="${hiveAdminJob.jobType != '99'}">readonly="readonly" </c:if> rows="10" cols="70">${hiveAdminJob.hqlTemplate}</textarea></td>
                    </tr>

					<tr>
						<td>统计开始:</td>
						<td><input type="text" name="statBeginTime" class="Wdate"
							style="width: 170px" value="${hiveAdminJob.statBeginTime}"
							onClick="WdatePicker({dateFmt:'yyyy-MM-dd'});" /></td>
					</tr>
					<tr>
						<td>统计结束:</td>
						<td><input type="text" name="statEndTime" class="Wdate"
							style="width: 170px" value="${hiveAdminJob.statEndTime}"
							onClick="WdatePicker();" /></td>
					</tr>
					<tr>
						<td>任务启动时间:</td>
						<td><input type="text" name="jobStartTime" class="Wdate"
							style="width: 170px" value="${hiveAdminJob.jobStartTime}"
							onClick="WdatePicker({dateFmt:'yyyy-MM-dd H:mm:ss'});" /></td>
					</tr>
					<tr>
						<td><input type="hidden" name="action" value="modify" /></td>
						<td><button
								class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only "
								type="submit">
								<span class="ui-button-text ui-corner-all">提交修改</span>
							</button></td>
					</tr>
				</tbody>
			</table>
		</fieldset>
	</form>
</body>
</html>