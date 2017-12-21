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
                      <form method="post" name="functioninfo_modify" id="modify" action="/vipuser/manage/update.do" title='' style="width:650px;margin:0px;">
                          <fieldset>
                              <legend style="font-weight: 900; font-size: 20px">达人管理</legend>
                              
                              				<div><p style="font-weight: 800">基本信息</p></div>
                              			   <table class= "gridtable" style="margin-top: -5px">
                                           <tr>
                                                 <td>
                                                  <label style="font-size: 16px">ID:</label>
                                                  <input type="text" name="id" readonly="readonly" value="${vipUser.id}" />
                                                 </td>
                                                 <td style="padding-left: 25px">
                                                  <label style="font-size: 16px">用户ID:</label>
                                                  <input type="text" name="userId" readonly="readonly" value="${vipUser.userId}" />
                                                 </td>
                                                 <td style="padding-left: 25px">
                                                  <label style="font-size: 16px">用户名:</label>
                                                  <input type="text" name="userName" readonly="readonly" value="${vipUser.userName}" />
                                                 </td>
                                                 <td style="padding-left: 25px">
                                                  <label style="font-size: 16px">昵称:</label>
                                                  <input type="text" name="nickname" readonly="readonly" value="${vipUser.nickName}" />
                                                 </td>
                                                 <td style="padding-left: 25px">
                                                  <label style="font-size: 16px">真实姓名:</label>
                                                  <input type="text" name="realName" value="${vipUser.realName}" />
                                                 </td>
                                                 <td style="padding-left: 25px">
                                                  <label style="font-size: 16px">地址:</label>
                                                  <input type="text" name="address" value="${vipUser.address}" />
                                                 </td>
                                             </tr>
                                             
                                             <tr>
                                             <td>
                                                  <label style="font-size: 16px">省:</label>
                                                  <input type="text" name="province" value="${vipUser.province}" />
                                                 </td>
                                                 <td style="padding-left: 25px">
                                                  <label style="font-size: 16px">市:</label>
                                                  <input type="text" name="city" value="${vipUser.city}" />
                                                 </td>
                                                 <td style="padding-left: 25px">
                                                  <label style="font-size: 16px">微博地址:</label>
                                                  <input type="text" name="weiboUrl" value="${vipUser.weiboUrl}" />
                                                 </td>
                                                <td style="padding-left: 25px">
                                                 <label style="font-size: 16px">QQ群:</label>
                                                 <input type="text" name="qqGroup" value="${vipUser.qqGroup}" />
                                                </td>
                                                <td style="padding-left: 25px">
                                                 <label style="font-size: 16px">负责人:</label>
                                                 <input type="text" name="principal" value="${vipUser.principal}" />
                                                </td>
                                                <td style="padding-left: 25px">
                                                 <label style="font-size: 16px">性别:</label>
                                                 <input type="text" name="sex" value="${vipUser.sex}" />
                                                </td>
                                                </tr>
                                            	<tr>
                                            	 <td>
                                                  <label style="font-size: 16px">手机:</label>
                                                  <input type="text" name="mobile" value="${vipUser.mobile}" />
                                                 </td>
                                                 <td style="padding-left: 25px">
                                                  <label style="font-size: 16px">国家:</label>
                                                  <input type="text" name="country" value="${vipUser.country}" />
                                                  </td>
                                                  <td style="padding-left: 25px">
                                                  <label style="font-size: 16px">QQ:</label>
                                                   <input type="text" name="qq" value="${vipUser.qq}" />
                                                  </td>
                                                  <td style="padding-left: 25px">
                                                  <label style="font-size: 16px">微信:</label>
                                                 <input type="text" name="webchat" value="${vipUser.webchat}" />
                                                 </td>
                                            	 <td style="padding-left: 25px">
                                                  <label style="font-size: 16px">标签名称:</label>
                                                  	<c:if test='${vipUser.tagId==1}'>
                                                  		<input type="text" name="" value="核心用户" />
                                                  	</c:if>
                                                  	<c:if test='${vipUser.tagId==2}'>
                                                  		<input type="text" name="" value="菜谱达人" />
                                                  	</c:if>
                                                  	<c:if test='${vipUser.tagId==3}'>
                                                  		<input type="text" name="" value="圈圈达人" />
                                                  	</c:if>
                                                  	<c:if test='${vipUser.tagId==4}'>
                                                  		<input type="text" name="" value="马甲" />
                                                  	</c:if>
                                                  	<c:if test='${vipUser.tagId==5}'>
                                                  		<input type="text" name="" value="黑名单" />
                                                  	</c:if>
                                                  	<c:if test='${vipUser.tagId==0}'>
                                                  		<input type="text" name="" value="未分类" />
                                                  	</c:if>
                                                  	<!-- 
                                                    <select name="tagId" style="width: 173px" disabled="disabled">
                                                        <option <c:if test='${vipUser.tagId==1}'>selected</c:if> value='1'>核心用户</option>
                                                        <option <c:if test='${vipUser.tagId==2}'>selected</c:if> value='2'>菜谱达人</option>
                                                        <option <c:if test='${vipUser.tagId==3}'>selected</c:if> value='3'>圈圈达人</option>
                                                        <option <c:if test='${vipUser.tagId==4}'>selected</c:if> value='4'>马甲</option>
                                                        <option <c:if test='${vipUser.tagId==5}'>selected</c:if> value='5'>黑名单</option>
                                                        <option <c:if test='${vipUser.tagId==0}'>selected</c:if> value='0'>未分类</option>
                                                    </select>
                                                    -->
                                            	 </td>
                                            	 <!--  style="padding-left: 47px" -->
                                            	 <td style="padding-left: 25px">
                                                 <label style="font-size: 16px">生育情况:</label>
                                                 	<c:if test='${vipUser.hasChild=="YES"}'>
                                                  		<input type="text" name="" value="有孩子" />
                                                  	</c:if>
                                                  	<c:if test='${vipUser.hasChild=="NO"}'>
                                                  		<input type="text" name="" value="没孩子" />
                                                  	</c:if>
                                                 	<!-- 
                                                    <select id="hasChild" name="hasChild" style="width: 173px" disabled="disabled">
                                                        <option value="">全部</option>
                                                        <option <c:if test="${vipUser.hasChild=='YES'}" >selected</c:if> value="YES">有孩</option>
                                                        <option <c:if test="${vipUser.hasChild=='NO'}" >selected</c:if> value="NO">无孩</option>
                                                    </select>
                                                    -->
                                                </td>
                                            	</tr>
                                            </table>
                                            <br />
                                            
                                           <div><p style="font-weight: 800">擅长领域</p></div>
										   <table style="margin-top: -5px">
                                           <tr>
                                                    <label><input name="domain" <c:if test="${domain1==1}" >checked=checked</c:if> type="checkbox" value="1" disabled="disabled"/>烘焙</label>
                                                    <label><input name="domain" <c:if test="${domain2==1}" >checked=checked</c:if> type="checkbox" value="2" disabled="disabled"/>家常菜</label>
                                                    <label><input name="domain" <c:if test="${domain3==1}" >checked=checked</c:if> type="checkbox" value="3" disabled="disabled"/>创意菜</label>
                                                    <label><input name="domain" <c:if test="${domain4==1}" >checked=checked</c:if> type="checkbox" value="4" disabled="disabled"/>宝宝餐</label>
                                                    <label><input name="domain" <c:if test="${domain5==1}" >checked=checked</c:if> type="checkbox" value="5" disabled="disabled"/>早餐</label>
                                                    <label><input name="domain" <c:if test="${domain6==1}" >checked=checked</c:if> type="checkbox" value="6" disabled="disabled"/>西餐</label>
                                                    <label><input name="domain" <c:if test="${domain7==1}" >checked=checked</c:if> type="checkbox" value="7" disabled="disabled"/>全能</label>
                                                    <label><input name="domain" <c:if test="${domain8==1}" >checked=checked</c:if> type="checkbox" value="8" disabled="disabled"/>其他</label>
                                           </tr>
                                           </table>
                                           
                                           <br />
                                           
                                           <div><p style="font-weight: 800">参加活动</p></div>
                                           <table class= "gridtable"  style="margin-top: -5px">
                                           <tr align="left">
                                              <th>序号</th>
                                              <th>活动ID号</th>
                                              <th>活动主题</th>
                                              <th>活动地址</th>
                                           </tr>
                                           <c:forEach items="${vipUserHuodong}" var="item"  varStatus="status">
                                           <tr>
                                              <td>${status.index+1}</td>
                                              <td>${item.id}</td>
                                              <td>${item.title}</td>
                                              <td>${item.address}</td>
                                           </tr>
                                           </c:forEach>
                                           </table>
                                           
                                           <br />
                                           
                                           <div><p style="font-weight: 800">备注</p></div>
                                           <div style="margin-top: -5px">                                       
                                           <textarea name="userDesc" rows="10" cols="190" disabled="disabled">${vipUser.userDesc}</textarea>
                                           </div>
                                           
                                           <table>
                                           </tr>
                                           <tr>
	                                             <td>
	                                               <input type="hidden" name="action" value="modify" />
	                                               <input type="hidden" name="queryCondition" value="${queryCondition}" />
	                                             </td>
                                           </tr>
                                           </table>
                   </fieldset>
              </form>
	</body>
</html>
