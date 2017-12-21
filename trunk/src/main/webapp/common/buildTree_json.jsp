<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="com.douguo.dc.user.model.User"%>
<%@ page import="java.sql.PreparedStatement"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="com.susing.sql.ConnectionManager"%>
<%@ page import="com.susing.EasyConnection"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date,java.util.Map,java.util.HashMap"%>
<%@ page import="javax.sql.DataSource"%>
<%@ page import="javax.naming.Context"%>
<%@ page import="javax.naming.InitialContext"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Map.Entry"%>
<%@ page import="org.codehaus.jackson.*"%>
<%@ page import="org.codehaus.jackson.map.*"%>
<%@ page import="org.codehaus.jackson.map.JsonMappingException"%>
<%@ page import="org.codehaus.jackson.node.*"%>
<%@ page import="com.douguo.dc.user.utils.Tree"%>

<%
	request.setCharacterEncoding("utf-8");

	String strUserId = (String) request.getParameter("user_id");
	String strType = (String) request.getParameter("type");

	EasyConnection con = null;
	String[][] dataMenu = null;
	String[][] dataUserFunction = null;
	try {
		// get connection
		con = ConnectionManager.currentManager().getConnection("logstatDB");

		// sql
		String strMenuSQL = "select m.menuID,m.parentID,m.functionID,m.level,m.sortID,m.visiable,f.functionName,f.uri from menu m,functionsinfo f where f.functionID=m.functionID";
		if (strType != null && "menu".equals(strType)) {
			User user = (User) session.getAttribute("loginUser");
			String curUserID = "";
			if (user != null) {
				curUserID = user.getUserid();
			}
			strMenuSQL += " and m.visiable = 1 and f.functionID in(select functionID from userfunctions where userID = '"
					+ curUserID + "')";
		}
		strMenuSQL += " order by level,sortID";
		//strMenuSQL += " order by f.typeID,level,sortID";
		String strUserFunctionSQL = "select `userID`,`functionID` from `userfunctions` where userID = '"
				+ strUserId + "'";

		//System.out.println(strMenuSQL);
		//System.out.println(strUserFunctionSQL);  

		// search data
		dataMenu = con.query(strMenuSQL);
		dataUserFunction = con.query(strUserFunctionSQL);

		//
		List<String> listIds = new ArrayList<String>();
		for (String[] arryRow : dataUserFunction) {
			listIds.add(arryRow[1]);
		}

		Tree tree = new Tree();
		tree.toJson(dataMenu, listIds, response);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		try {
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
%>
