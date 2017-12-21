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
                      <form method="post" name="functioninfo_modify" id="modify" action="/channel/manage/update.do" title='' style="width:550px;margin:0px;"> 
                          <fieldset> 
                              <legend>渠道管理</legend> 
                              <table> 
                                       <tbody> 
                                           <tr>
                                                 <td> Id:</td> 
                                                 <td>
                                                 	<input type="text" name="id" readonly="readonly" value="${channel.id}" />
                                                 </td> 
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> 渠道名称:</td> 
                                                 <td>
                                                 	<input type="text" name="channelName" value="${channel.channelName}" />
                                                 </td>
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> 渠道代码:</td> 
                                                 <td><input type="text" name="channelCode" value="${channel.channelCode}" /></td> 
                                           </tr> 
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                            <tr>
                                            	<td> 渠道合集:</td>
                                                <td><input type="text" name="channelTag" value="${channel.channelTag}" /></td>
                                           </tr>
                                           <tr>
                                           		<td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> 负责人:</td>
                                                 <td><input type="text" name="userName" value="${channel.userName}" /></td> 
                                           </tr>
                                           <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> 对接人:</td>
                                                 <td><input type="text" name="principal" value="${channel.principal}" /></td> 
                                           </tr>
                                            <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> 部门:</td>
                                                 <td><input type="text" name="principalDep" value="${channel.principalDep}" /></td> 
                                           </tr>
                                            <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> 联系方式:</td>
                                                 <td><input type="text" name="principalContact" value="${channel.principalContact}" /></td> 
                                           </tr>
                                            <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> 合作模式:</td>
                                                 <td><input type="text" name="channelCooperation" value="${channel.channelCooperation}" /></td> 
                                           </tr>
                                            <tr>
												<td>&nbsp;</td>
                                             </tr>
                                             <tr>
                                             	<td> 合作平台:</td>
                                                <td>
                                                	<input type="radio" name="channelPlat" value="android" <c:if test="${channel.channelPlat == 'android' || channel.channelPlat == ''}"> checked="checked" </c:if>/>android
                                                	<input type="radio" name="channelPlat" value="ios" <c:if test="${channel.channelPlat == 'ios'}">checked="checked" </c:if>  />ios
                                                </td>
                                            </tr>
                                            <tr>
                                                  <td>&nbsp;</td>
                                           </tr>
                                           <tr> 
                                                 <td> 合作进度:</td>
                                                 <td><input type="text" name="planDesc" value="${channel.planDesc}" /></td> 
                                           </tr>
                                           <tr>
                                                  <td><input type="hidden" name="action" value="modify" /></td>
                                                  <td></td> 
                                           </tr>
                                     </tbody> 
                           </table> 
                            <!-- -------------------- -->
                <br/>
			    <div class="selectList">
			    	渠道类别:
			        <select class="channelType1" name="channelType1">
			            <option>请选择</option>
			        </select>
			        <select class="channelType2" name="channelType2">
			            <option>请选择</option>
			        </select>
			        <select class="channelType3" name="channelType3">
			            <option>请选择</option>
			        </select>
			    </div>
			    <br/>
			    <script type="text/javascript">
			    $(function(){
			        $(".selectList").each(function(){
			            var url = "/channel/manage/channelTypeJson.do";
			            var areaJson;
			            var temp_html;
			            var chType1 = $(this).find(".channelType1");
			            var chType2 = $(this).find(".channelType2");
			            var chType3 = $(this).find(".channelType3");
			            //初始化省
			            var channelType1 = function(){
			                $.each(areaJson,function(i,channelType1){
			                    temp_html+="<option value='"+channelType1.ch1_id+"'>"+channelType1.ch1_name+"</option>";
			                });
			                chType1.html(temp_html);
			                //设置选中值
			                chType1.val("${channel.channelType1}");
			                channelType2();
			            };
			            //赋值市
			            var channelType2 = function(){
			                temp_html = ""; 
			                var n = chType1.get(0).selectedIndex;
			                $.each(areaJson[n].c,function(i,channelType2){
		                		temp_html+="<option value='"+channelType2.ch2_id+"'>"+channelType2.ch2_name+"</option>";	
			                });
			                chType2.html(temp_html);
			              	//设置选中值
			                chType2.val("${channel.channelType2}");
			                channelType3();
			            };
			            //赋值县
			            var channelType3 = function(){
			                temp_html = ""; 
			                var m = chType1.get(0).selectedIndex;
			                var n = chType2.get(0).selectedIndex;
			                if(typeof(areaJson[m].c[n].d) == "undefined"){
			                    chType3.css("display","none");
			                }else{
			                    chType3.css("display","inline");
			                    $.each(areaJson[m].c[n].d,function(i,channelType3){
			                        temp_html+="<option value='"+channelType3.ch3_id+"'>"+channelType3.ch3_name+"</option>";
			                    });
			                    //
			                    chType3.html(temp_html);
			                  	//设置选中值
				                chType3.val("${channel.channelType3}");
			                };
			            };
			            //选择省改变市
			            chType1.change(function(){
			                channelType2();
			            });
			            //选择市改变县
			            chType2.change(function(){
			                channelType3();
			            });
			            //获取json数据
			            $.getJSON(url,function(data){
			                areaJson = data;
			                channelType1();
			            });
			        });
			    });
			    
			    </script>
				<!-- -------------------- -->
				<button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only " type="submit">
                <span class="ui-button-text ui-corner-all">提交修改</span></button>
                   </fieldset> 
              </form>
	</body>
</html>