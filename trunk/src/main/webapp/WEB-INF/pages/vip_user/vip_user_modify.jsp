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
                      <form method="post" name="functioninfo_modify" id="modify" action="/vipuser/manage/update.do" title='' style="width:650px;margin:0px;">
                          <fieldset> 
                              <legend>达人管理</legend>
                              <table> 
                                       <tbody> 
                                           <tr>
                                                 <td align="right"> Id:</td>
                                                 <td>
                                                 	<input type="text" name="id" readonly="readonly" value="${vipUser.id}" />
                                                 </td> 
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td align="right"> 用户id:</td>
                                                 <td>
                                                 	<input type="text" name="userId" readonly="readonly" value="${vipUser.userId}" />
                                                 </td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td align="right"> 用户名:</td>
                                                 <td><input type="text" name="userName" readonly="readonly" value="${vipUser.userName}" /></td>
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                            <tr>
                                            	<td align="right"> 昵称:</td>
                                                <td><input type="text" name="nickname" readonly="readonly" value="${vipUser.nickName}" /></td>
                                           </tr>
                                           <tr>
                                           		<td align="right">&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td align="right"> 真实名称:</td>
                                                 <td><input type="text" name="realName" value="${vipUser.realName}" /></td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td align="right"> 地址:</td>
                                                 <td><input type="text" name="address" value="${vipUser.address}" /></td>
                                           </tr>
                                            <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td align="right"> 手机:</td>
                                                 <td><input type="text" name="mobile" value="${vipUser.mobile}" /></td>
                                           </tr>
                                            <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td align="right"> 国家:</td>
                                                 <td><input type="text" name="country" value="${vipUser.country}" /></td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td align="right"> 省:</td>
                                                 <td><input type="text" name="province" value="${vipUser.province}" /></td>
                                           </tr>
                                           <tr>
                                            	<td>&nbsp;</td>
											</tr>
                                           <tr>
                                                 <td align="right"> 市:</td>
                                                 <td><input type="text" name="city" value="${vipUser.city}" /></td>
                                           </tr>
                                           <tr>
                                                <td>&nbsp;</td>
                                           </tr>
                                            <tr>
                                                <td align="right"> 微博地址:</td>
                                                <td><input type="text" name="weiboUrl" value="${vipUser.weiboUrl}" /></td>
                                           </tr>
                                            <tr>
                                                <td>&nbsp;</td>
                                           	</tr>
                                            <tr>
                                                <td align="right"> 标签名称:</td>
                                                <td>
                                                    <select name="tagId">
                                                        <option <c:if test='${vipUser.tagId==1}'>selected</c:if> value='1'>核心用户</option>
                                                        <option <c:if test='${vipUser.tagId==2}'>selected</c:if> value='2'>菜谱达人</option>
                                                        <option <c:if test='${vipUser.tagId==3}'>selected</c:if> value='3'>圈圈达人</option>
                                                        <option <c:if test='${vipUser.tagId==4}'>selected</c:if> value='4'>马甲</option>
                                                        <option <c:if test='${vipUser.tagId==5}'>selected</c:if> value='5'>黑名单</option>
                                                        <option <c:if test='${vipUser.tagId==0}'>selected</c:if> value='0'>未分类</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>&nbsp;</td>
                                           	</tr>
                                            <tr>
                                                <td align="right"> qq群:</td>
                                                <td><input type="text" name="qqGroup" value="${vipUser.qqGroup}" /></td>
                                           </tr>
                                            <tr>
                                                <td>&nbsp;</td>
                                           	</tr>
                                            <tr>
                                                <td align="right"> 负责人:</td>
                                                <td><input type="text" name="principal" value="${vipUser.principal}" /></td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                            <tr>
                                                <td align="right"> 性别:</td>
                                                <td><input type="text" name="sex" value="${vipUser.sex}" /></td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr>
                                                <td align="right"> qq:</td>
                                                <td><input type="text" name="qq" value="${vipUser.qq}" /></td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr>
                                                <td align="right"> 微信:</td>
                                                <td><input type="text" name="webchat" value="${vipUser.webchat}" /></td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr>
                                                <td align="right"> 生育情况:</td>
                                                <td>
                                                    <select id="hasChild" name="hasChild">
                                                        <option value="">全部</option>
                                                        <option <c:if test="${vipUser.hasChild=='YES'}" >selected</c:if> value="YES">有孩</option>
                                                        <option <c:if test="${vipUser.hasChild=='NO'}" >selected</c:if> value="NO">无孩</option>
                                                    </select>&nbsp;
                                                </td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr>
                                                <td align="right"> 擅长领域:</td>
                                                <td>
                                                    <label><input name="domain" <c:if test="${domain1==1}" >checked=checked</c:if> type="checkbox" value="1" />烘焙</label>
                                                    <label><input name="domain" <c:if test="${domain2==1}" >checked=checked</c:if> type="checkbox" value="2" />家常菜</label>
                                                    <label><input name="domain" <c:if test="${domain3==1}" >checked=checked</c:if> type="checkbox" value="3" />创意菜</label>
                                                    <label><input name="domain" <c:if test="${domain4==1}" >checked=checked</c:if> type="checkbox" value="4" />宝宝餐</label>
                                                    <label><input name="domain" <c:if test="${domain5==1}" >checked=checked</c:if> type="checkbox" value="5" />早餐</label>
                                                    <label><input name="domain" <c:if test="${domain6==1}" >checked=checked</c:if> type="checkbox" value="6" />西餐</label>
                                                    <label><input name="domain" <c:if test="${domain7==1}" >checked=checked</c:if> type="checkbox" value="7" />全能</label>
                                                    <label><input name="domain" <c:if test="${domain8==1}" >checked=checked</c:if> type="checkbox" value="8" />其他</label>
                                                </td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <table>
	                                           <tr>
	                                           		参加活动：
	                                           </tr>
	                                           <tr>
	                                           		<td align="center">序号</td>
	                                           		<td>活动ID号</td>
	                                           		<td>活动主题</td>
	                                           		<td>活动地址</td>
	                                           </tr>
	                                           <c:forEach items="${vipUserHuodong}" var="item"  varStatus="status">
	                                           <tr>
	                                           		<td align="center">${status.index+1}</td>
	                                           		<td>${item.id}</td>
	                                           		<td>${item.title}</td>
	                                           		<td>${item.address}</td>
	                                           </tr>
	                                           </c:forEach>
                                           </table>
                                           </br>
                                           <tr>
                                                <td align="right"> 备注:</td>
                                                <td>
                                                    <textarea name="userDesc" align="left" rows="10" cols="70">${vipUser.userDesc}</textarea>
                                                </td>
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