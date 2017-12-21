<%@page import="com.douguo.dc.util.HttpClientUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page
	import="java.io.File,java.util.Map,java.util.HashMap,
    java.io.FileWriter,
    java.io.IOException,
    java.sql.Connection,
    java.sql.DriverManager,
    java.sql.ResultSet,
    java.sql.SQLException,
    java.sql.Statement"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jqGrid Demos</title>
<style>
html,body {
	margin: 0; /* Remove body margin/padding */
	padding: 0;
	font-size: 75%;
}
table{border-left: 1px solid #666; border-bottom: 1px solid #666;}
td{border-right:1px solid #666;border-top: 1px solid #666;}
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
</head>
<body>

	<%
		request.setCharacterEncoding("utf8");
		Class.forName("com.mysql.jdbc.Driver");
		String table = request.getParameter("table");
		String querySQL = "select tag from dg_tag where flag='0' and cate_id > 0";
		//String jdbc = "jdbc:mysql://192.168.1.233:3306/dg2010?useUnicode=true&amp;characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
		//String user = "hadoop";
		//String pwd = "hadoop@2014)(*";

		String jdbc = "jdbc:mysql://192.168.1.206:3306/dg2010?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
		String user = "root";
		String pwd = "aaaaaa";
		Connection con = DriverManager.getConnection(jdbc, user, pwd);
		Statement stmt = con.createStatement();
		//      stmt.executeQuery(dropSQL); // 执行删除语句
		//      stmt.executeQuery(createSQL); // 执行建表语句
		//      stmt.executeQuery(insterSQL); // 执行插入语句
		ResultSet res = stmt.executeQuery(querySQL); // 执行查询语句

		//FileWriter fw = new FileWriter("/home/dg-hadoop/hive-admin-test.txt");
		FileWriter fw = new FileWriter("/Users/zyz/test.txt");
	%>
	<table width="700" border="0" cellspacing="0" cellpadding="0" >

		<%
			int i = 0;
			while (res.next()) {
				i++;
				String tag = res.getString(1);
				String counts = "10";
				String url = "http://acp.douguo.net/toolbak/getTagCount?";
				Map<String, String> params = new HashMap<String, String>();
				params.put("key", tag);
				
				String value = HttpClientUtil.doGetRequest(url, params);
				Thread.sleep(100);
				System.out.println(i);
		%>
		<tr>
			<td><%=i%></td>
			<td><%=tag%></td>
			<td><%=value%></td>
		</tr>
		<%
			//out.println(tag + "," + counts + "\n");
				fw.write(tag + "," + counts + "\n");
			}
			fw.flush();
			fw.close();
		%>
	</table>
	

</body>
</html>


