<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.qunar.grid.GridPager" %>
<%@ page import="com.qunar.grid.JQGridUtil" %>
<%@ page import ="com.susing.EasyConnection,com.susing.sql.ConnectionManager" %>


<%
    EasyConnection con = ConnectionManager.currentManager().getConnection("logstatDB");
    request.setCharacterEncoding("utf-8"); 
    
    String selID = request.getParameter("selID");  //要查询的用户的ID
    if(selID.equals("null"))
        selID=null;
    String selSql = "SELECT userID,username FROM usersinfo ";  
    if(selID!=null && !selID.equals(""))           //是否要查询某个用户
    {
       selSql += " where userID like '%"+selID+"%'";
    }
    String[][] data = con.query(selSql);
    String[][] arryFun =data;
    for(int i=0;i<data.length;i++)                 //将查询出来的数据添加到要显示的结果集中
    {
      arryFun[i] = new String[] { data[i][0], data[i][1], "<a href='userManage_edit.jsp?userID="+ data[i][0] +"' title='编辑' >编辑</a>",
        "<a href='userManage.jsp?action=del&id="+data[i][0]+"'>删除</a>",
        "<a href='#' onclick=\"showUrlInDialog('userManage_copy.jsp?action=copy&userID_cpfrom="+data[i][0]+"')\" title='拷贝' >拷贝</a>", 
        "<a href='userFunction.jsp?user_id="+ data[i][0] +"' title='授权' >授权</a>" };
    }
		
		String rowsLimit = request.getParameter("rows");     //获取jqgrid没页要显示的行数
                if(null==rowsLimit)                                  //设置rowsLimit的默认值
                    rowsLimit="10";
                JQGridUtil t = new JQGridUtil();
		int nPage = 1;                                       //jqgrid要显示的当前页面
		int records = arryFun.length;                        //要显示的记录总行数
		int total = records/Integer.parseInt(rowsLimit)+1;   //计算总的页数
		//行数据
		List<Map> rows = new ArrayList<Map>();
		
		for(String[] axx : arryFun){
			Map map = new HashMap();
			map.put("id", "1");
			map.put("cell", axx);
			
			rows.add(map);
		}
		
		//rows
		GridPager<Map> gridPager = new GridPager<Map>( nPage, total, records, rows);
		t.toJson(gridPager, response);

con.close();
%>


