<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
    request.setCharacterEncoding("utf-8");    
    String rowsLimit=request.getParameter("rows");    //取出每一页显示的行数
    String sidx = request.getParameter("sidx");       //取出排序的项
    String sord = request.getParameter("sord");       //取出排序方式：升序，降序
    
            if(rowsLimit==null)                        //设置每一页显示行数的默认值
            {
               rowsLimit="10";
            }
            JQGridUtil t = new JQGridUtil();
            int nPage = 1;                            //当前显示的页数
            int total = 1;                            //要显示的总的页数，初始值为1
    String selSql = "SELECT functionID,functionName,uri,type,typeID FROM functionsinfo ";
    String[][] data = con.query(selSql);
    int records = data.length;
    total = records/Integer.parseInt(rowsLimit)+1;            //计算总的页数
    String[][] arryFun =data;
    for(int i=0;i<data.length;i++)
    {
      arryFun[i] = new String[] { data[i][0], data[i][1], data[i][2], data[i][3], data[i][4],
        "<a href='functioninfo_modify.jsp?functionID="+ data[i][0] +"' title='修改' >修改</a>"};
    }
                //行数据
		List<Map> rows = new ArrayList<Map>();	
		for(String[] axx : arryFun){
			Map map = new HashMap();
			map.put("id", "1");
			map.put("cell", axx);
			
			rows.add(map);
		}
		
		//rows
		GridPager<Map> gridPager = new GridPager<Map>( nPage, total, records, rows);   //将表格显示的配置初始化给GridPager
		t.toJson(gridPager, response);                                                //发送json数据

con.close();
%>


