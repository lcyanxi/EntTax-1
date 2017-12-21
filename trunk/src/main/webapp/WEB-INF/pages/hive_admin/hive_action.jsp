<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date,java.util.Map,java.util.HashMap" %>
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.codehaus.jackson.*" %>
<%@ page import="org.codehaus.jackson.map.*" %>
<%@ page import="org.codehaus.jackson.map.JsonMappingException" %>
<%@ page import="org.codehaus.jackson.node.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page
    import="java.io.File,
    java.io.FileWriter,
    java.io.IOException,
    java.sql.Connection,
    java.sql.DriverManager,
    java.sql.ResultSet,
    java.sql.SQLException,
    java.sql.Statement"%>
 
 
<%
    Class.forName("org.apache.hive.jdbc.HiveDriver");
    String table = request.getParameter("table");
    String querySQL = "select * from " + table + " limit 10";
    Connection con = DriverManager.getConnection(
            "jdbc:hive2://localhost:10000/default", "", "");
    Statement stmt = con.createStatement();
    //      stmt.executeQuery(dropSQL); // 执行删除语句
    //      stmt.executeQuery(createSQL); // 执行建表语句
    //      stmt.executeQuery(insterSQL); // 执行插入语句
    ResultSet res = stmt.executeQuery(querySQL); // 执行查询语句
 
    FileWriter fw = new FileWriter("/home/dg-hadoop/hive-admin-test.txt");
    while (res.next()) {
        System.out.println("name:\t" + res.getString(1) + "\tid:\t"
                + res.getString(2) + "\tsex:\t" + res.getString(3));
        fw.write("name:\t" + res.getString(1) + "\tid:\t"
                + res.getString(2) + "\tsex:\t" + res.getString(3)
                + "\n");
    }
    fw.flush();
    fw.close();
%>
 
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Insert title here</title>
    </head>
    <body>
 
    </body>
</html>
