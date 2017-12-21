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
          <fieldset>
              <legend style="font-weight: 900; font-size: 20px">用户画像</legend>

              <div><p style="font-weight: 800">Info</p></div>
                   <table class= "gridtable" style="margin-top: -5px">
                   <tr>
                         <td>
                          <label style="font-size: 16px">user_id:</label>
                          <input type="text" name="user_id" readonly="readonly" value="${user['user_id']}" />
                         </td>
                   </tr>
                       <tr>
                         <td style="padding-left: 25px">
                          <label style="font-size: 16px">username:</label>
                          <input type="text" name="username" readonly="readonly" value="${user['username']}" />
                         </td>
                       </tr>
                       <tr>
                         <td style="padding-left: 25px">
                          <label style="font-size: 16px">nickname:</label>
                          <input type="text" name="nickname" readonly="readonly" value="${user.nickname}" />
                         </td>
                       </tr>
                       <tr>
                         <td style="padding-left: 25px">
                          <label style="font-size: 16px">sex:</label>
                          <input type="text" name="sex" readonly="readonly" value="${user.sex}" />
                         </td>
                       </tr>
                       <tr>
                         <td style="padding-left: 25px">
                          <label style="font-size: 16px">mobile:</label>
                          <input type="text" name="mobile" value="${user.mobile}" />
                         </td>
                       </tr>
                       <tr>
                         <td style="padding-left: 25px">
                          <label style="font-size: 16px">email:</label>
                          <input type="text" name="email" value="${user.email}" />
                         </td>
                     </tr>
                     <tr>
                     <td>
                          <label style="font-size: 16px">origin_city:</label>
                          <input type="text" name="origin_city" value="${user.origin_city}" />
                         </td>
                     </tr>
                       <tr>
                         <td style="padding-left: 25px">
                          <label style="font-size: 16px">origin_province:</label>
                          <input type="text" name="origin_province" value="${user.origin_province}" />
                         </td>
                       </tr>
                       <tr>
                         <td style="padding-left: 25px">
                          <label style="font-size: 16px">cur_city:</label>
                          <input type="text" name="cur_city" value="${user.cur_city}" />
                         </td>
                       </tr>
                       <tr>
                        <td style="padding-left: 25px">
                         <label style="font-size: 16px">cur_province:</label>
                         <input type="text" name="cur_province" value="${user.cur_province}" />
                        </td>
                       </tr>
                       <tr>
                        <td style="padding-left: 25px">
                         <label style="font-size: 16px">purview:</label>
                         <input type="text" name="purview" value="${user.purview}" />
                        </td>
                       </tr>
                       <tr>
                        <td style="padding-left: 25px">
                         <label style="font-size: 16px">level:</label>
                         <input type="text" name="level" value="${user.level}" />
                        </td>
                        </tr>
                        <tr>
                         <td>
                          <label style="font-size: 16px">is_vip:</label>
                          <input type="text" name="is_vip" value="${user.is_vip}" />
                         </td>
                        </tr>
                       <tr>
                         <td style="padding-left: 25px">
                          <label style="font-size: 16px">source:</label>
                          <input type="text" name="source" value="${user.source}" />
                          </td>
                       </tr>
                       <tr>
                          <td style="padding-left: 25px">
                          <label style="font-size: 16px">channel:</label>
                           <input type="text" name="channel" value="${user.channel}" />
                          </td>
                       </tr>
                       <tr>
                          <td style="padding-left: 25px">
                          <label style="font-size: 16px">cate:</label>
                           <input type="text" name="cate" value="${user.cate}" />
                         </td>
                       </tr>
                       <tr>
                             <td style="padding-left: 25px">
                              <label style="font-size: 16px">registerdate:</label>
                             <input type="text" name="registerdate" value="${user.registerdate}" />
                             </td>
                       </tr>
                       <tr>
                                <td style="padding-left: 25px">
                                     <label style="font-size: 16px">registerip:</label>
                                     <input type="text" name="registerip" value="${user.registerip}" />
                                 </td>
                       </tr>
                       <tr>
                           <td>
                               <label style="font-size: 16px">birthday:</label>
                               <input type="text" name="birthday" value="${user.birthday}" />
                           </td>
                       </tr>
                       <tr>
                           <td style="padding-left: 25px">
                               <label style="font-size: 16px">description:</label>
                               <input type="text" name="description" value="${user.description}" />
                           </td>
                       </tr>
                       <tr>
                           <td style="padding-left: 25px">
                               <label style="font-size: 16px">fans_num:</label>
                               <input type="text" name="fans_num" value="${user.fans_num}" />
                           </td>
                       </tr>
                       <tr>
                           <td style="padding-left: 25px">
                               <label style="font-size: 16px">diaries_num:</label>
                               <input type="text" name="diaries_num" value="${user.diaries_num}" />
                           </td>
                       </tr>
                       <tr>
                           <td style="padding-left: 25px">
                               <label style="font-size: 16px">recipes_num:</label>
                               <input type="text" name="recipes_num" value="${user.recipes_num}" />
                           </td>
                       </tr>
                       <tr>
                           <td style="padding-left: 25px">
                               <label style="font-size: 16px">friends_num:</label>
                               <input type="text" name="friends_num" value="${user.friends_num}" />
                           </td>
                       </tr>
                       <tr>
                           <td>
                               <label style="font-size: 16px">caidans_num:</label>
                               <input type="text" name="caidans_num" value="${user.caidans_num}" />
                           </td>
                       </tr>
                       <tr>
                           <td style="padding-left: 25px">
                               <label style="font-size: 16px">dishes_num:</label>
                               <input type="text" name="dishes_num" value="${user.dishes_num}" />
                           </td>
                       </tr>
                       <tr>
                           <td style="padding-left: 25px">
                               <label style="font-size: 16px">cookcollect_num:</label>
                               <input type="text" name="cookcollect_num" value="${user.cookcollect_num}" />
                           </td>
                       </tr>
                       <tr>
                           <td style="padding-left: 25px">
                               <label style="font-size: 16px">integration_num:</label>
                               <input type="text" name="integration_num" value="${user.integration_num}" />
                           </td>
                       </tr>
                       <tr>
                           <td style="padding-left: 25px">
                               <label style="font-size: 16px">point:</label>
                               <input type="text" name="point" value="${user.point}" />
                           </td>
                       </tr>
                       <tr>
                           <td style="padding-left: 25px">
                               <label style="font-size: 16px">post_num:</label>
                               <input type="text" name="post_num" value="${user.post_num}" />
                           </td>
                       </tr>
                       <tr>
                           <td>
                               <label style="font-size: 16px">upload_contact_time:</label>
                               <input type="text" name="upload_contact_time" value="${user.upload_contact_time}" />
                           </td>
                       </tr>
                       <tr>
                           <td style="padding-left: 25px">
                               <label style="font-size: 16px">is_robot:</label>
                               <input type="text" name="is_robot" value="${user.is_robot}" />
                           </td>
                       </tr>

                    </table>
                    <br />

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
	</body>
</html>
