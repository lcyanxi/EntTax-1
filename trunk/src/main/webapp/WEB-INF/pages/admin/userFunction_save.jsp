<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import ="com.susing.EasyConnection,com.susing.sql.ConnectionManager" %>
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
	String strTreeIds = (String)request.getParameter("tree_ids");
	String strUserId = (String)request.getParameter("user_id");
	if(strUserId == null || "".equals(strUserId)){
		
	}
	
	System.out.println("*******" + strTreeIds);

	EasyConnection con = null;
        List list = new ArrayList();

        try {
	        con = ConnectionManager.currentManager().getConnection("logstatDB");
		
		String strDelSQL = "delete from userfunctions where userID=?;";
		con.exec(strDelSQL,new String []{strUserId});
                //

		String strSQL = "insert into userfunctions (userID,functionID) values (?,?);";
                System.out.println("*******" + strSQL);

		if(strTreeIds != null && !"".equals(strTreeIds)){
			String [] arryTreeIds = strTreeIds.split(",");
			String[][] re = new String[arryTreeIds.length][2];
			for(int i = 0; i < arryTreeIds.length; i++){
				re[i][0]=strUserId;
				re[i][1]=arryTreeIds[i];
			}
			int[] data = con.exec(strSQL,re);
		}

        } catch (Exception e) {
                e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }	

	//response.sendRedirect("userFunction.jsp");
	response.sendRedirect("userManage.jsp");
%>
