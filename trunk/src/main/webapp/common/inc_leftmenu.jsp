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
	String strUserId = (String)request.getParameter("user_id");
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
	      width:50px;
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
	  <div class="demo" style="height:100%">
	    <div style="margin-bottom:1px;">
	        <!--a class="button" href="javascript:void(0);" id="showchecked">Get Selected Nodes</a-->
	        <!--a class="button" href="javascript:void(0);" id="showcurrent">Get Current Node</a-->
	    </div>
	    <div id="menuDiv" name="menuDiv" style="border-bottom: #c3daf9 1px solid; border-left: #c3daf9 1px solid; width: 257px;height:417px; overflow: auto; border-top: #c3daf9 1px solid; border-right: #c3daf9 1px solid;">
	        <div id="tree">
	        	
	        </div>
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
          	showcheck: false,
          	onnodeclick:function(item){
			
			if(typeof(item.uri) != "undefined" && item.uri != "#"){
				alert("sdf");
				//top.frames["main"].location.replace(item.uri);
				document.location=item.uri;
			}
			//alert(item.uri);
		}
          };
      	
          o.data = [treedata];
          
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
                  alert(s.text);
              }
              else{
                  alert("NULL");
              }
           });
      }

      function uf_changeMenuSize(){
        var h = $(document).height(); //获取当前窗口可视操作区域高度
        $("#menuDiv").height(h*0.8);
      }

	$(window).bind('resize', function() {
		uf_changeMenuSize();
                  });
      
      function uf_load(){
      	//$.post("../common/buildTree_json.jsp?type=menu",{
      	$.post("/menu/buildTree.do?type=menu",{
           				Action: "post", 
           				Name: "lulu" },
					function (data, textStatus){
						//this; // 这个Ajax请求的选项配置信息，请参考jQuery.get()说到的this
						treedata["ChildNodes"] = data;
						load();
					},
					"json");
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
