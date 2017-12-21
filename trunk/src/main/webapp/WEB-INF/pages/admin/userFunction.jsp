<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date,java.util.Map,java.util.HashMap" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.codehaus.jackson.*" %>
<%@ page import="org.codehaus.jackson.map.*" %>
<%@ page import="org.codehaus.jackson.map.JsonMappingException" %>
<%@ page import="org.codehaus.jackson.node.*" %>

<%
	request.setCharacterEncoding("utf-8");
	String strUserId = (String)request.getParameter("uid");
	String strTreeIds = (String)session.getAttribute("tree_ids");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
    <title>Big Tree</title>
    <link href="/wdtree/wdtree/css/tree.css" rel="stylesheet" type="text/css" />
    <!--link href="wdtree/wdtree/sample-css/page.css" rel="stylesheet" type="text/css" /-->
    <style type="text/css">
	    body {
		color:#000; /* MAIN BODY TEXT COLOR */
		font-family:"Lucida Grande","Lucida Sans Unicode",Arial,Verdana,sans-serif; /* MAIN BODY FONTS */
		}
		.demo{
	      float:left;
	      width:500px;
	    }

	    a.button{
	      font-size: 0.8em;
	      margin-right: 4px;
	    }
    </style>
    <meta charset="UTF-8" />
	<script src="/wdtree/wdtree/src/jquery.js" type="text/javascript"></script>
  	<script src="/wdtree/wdtree/src/Plugins/jquery.tree.js" type="text/javascript"></script>
  	<script src="/wdtree/wdtree/data/tree1.js" type="text/javascript"></script>
</head>
<body>
	
  <div style="padding:10px;"> 
	  <div class="demo">
	    <div style="margin-bottom:5px;">
	        <!--a class="button" href="javascript:void(0);" id="showchecked">Get Selected Nodes</a-->
	        <!--a class="button" href="javascript:void(0);" id="showcurrent">Get Current Node</a-->
	    </div>
	    <div style="border-bottom: #c3daf9 1px solid; border-left: #c3daf9 1px solid; width: 750px; height: 500px; overflow: auto; border-top: #c3daf9 1px solid; border-right: #c3daf9 1px solid;">
	    	<form id="treeForm" name="treeForm" action="/userfun/save.do" method="post">
	        <div id="tree">
	        	
	        </div>
	        <input type="hidden" id="tree_ids" name="tree_ids"/>
	        <input type="hidden" id="uid" name="uid" value="${uid}"/>
	        <br/> 
	        <input type="button" id="bt_save" name="bt_save" value="保存" />
	      </form>
	    </div>
	  </div>
  </div>

  <script type="text/javascript">
      var userAgent = window.navigator.userAgent.toLowerCase();
      $.browser.msie8 = $.browser.msie && /msie 8\.0/i.test(userAgent);
      $.browser.msie7 = $.browser.msie && /msie 7\.0/i.test(userAgent);
      $.browser.msie6 = !$.browser.msie8 && !$.browser.msie7 && $.browser.msie && /msie 6\.0/i.test(userAgent);
      
      //加载树
      function load() {
      	var o = {
          	showcheck: true
          	//onnodeclick:function(item){alert(item.text);}, preUserFun       
          };
          
      	
          //o.data = [treedata];
          o.data = [treedata];
          //o.data = [{"id":"0","text":"root","value":"86","showcheck":true,"complete":true,"isexpand":true,"checkstate":0,"hasChildren":true,"ChildNodes":[{"ChildNodes":null,"value":"1","id":"1","level":"1","url":null,"parentId":"0","menuId":"1","text":"1","showcheck":true,"complete":true,"isexpand":true,"checkstate":"0","hasChildren":false,"functionId":"1","sortId":"1","visiable":"1"}]}]
          //alert(JSON.stringify(treedata));
          
          //展示树形
          $("#tree").treeview(o);  
          
          //得到选中的节点值
          $("#showchecked").click(function(e){
              var s=$("#tree").getCheckedNodes();
              if(s !=null){
              	//alert(s.join(","));
              }else{
              	//alert("NULL");
              }
          });
          
           $("#showcurrent").click(function(e){
              var s=$("#tree").getCurrentNode();
              if(s !=null){
                  //alert(s.text);
              }
              else{
                  //alert("NULL");
              }
           });
           
           //保存按钮
          $("#bt_save").click(function(e){
              var s=$("#tree").getCheckedNodes(true);
              //var s=$("#tree").getTSNs();
              if(s !=null){
              	$("#tree_ids").val(s.join(","));
              }else{
              	alert("NULL");
              }
              $("#treeForm").submit();
          });
      }
      
      function uf_load(){
      	$.post("/menu/buildTree.do?uid=${uid}",{
           				Action: "post", 
           				Name: "lulu" },
					function (data, textStatus){
						// data 可以是 xmlDoc, jsonObj, html, text, 等等.
						//this; // 这个Ajax请求的选项配置信息，请参考jQuery.get()说到的this
						//alert(JSON.stringify(data));
						//alert(JSON.stringify(treedata));
							
						treedata["ChildNodes"] = data;
						//alert(JSON.stringify(treedata));
						load();
						//alert(data.result);
					},
					"json");
      	
          //alert(JSON.stringify(treedata));	
          var s=$("#tree").reflash("3,4");
      }
      
      if( $.browser.msie6){
          uf_load();
      }else{
          $(document).ready(uf_load);
      }
      
  </script>
</body>
</html>
