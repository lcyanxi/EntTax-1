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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Menu配置管理</title>
	<link rel="stylesheet" type="text/css" media="screen" href="/jqgrid/themes/redmond/jquery-ui-1.8.1.custom.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="/jqgrid/themes/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="/jqgrid/themes/ui.multiselect.css" />
	<script src="/jqgrid/js/jquery.js" type="text/javascript"></script>
	<script src="/jqgrid/js/jquery-ui-1.8.1.custom.min.js" type="text/javascript"></script>
	<script src="/jqgrid/js/jquery.layout.js" type="text/javascript"></script>
	<script src="/jqgrid/js/i18n/grid.locale-en.js" type="text/javascript"></script>
	<script src="/jqgrid/js/ui.multiselect.js" type="text/javascript"></script>
	<script src="/jqgrid/js/jquery.jqGrid.js" type="text/javascript"></script>
	<script src="/jqgrid/js/jquery.tablednd.js" type="text/javascript"></script>
	<script src="/jqgrid/js/jquery.contextmenu.js" type="text/javascript"></script>
</head>
<%
	request.setCharacterEncoding("utf-8");
	String strType = (String)request.getParameter("menuType");
	String strExport = (String)request.getParameter("export");
	System.out.println("*******" + strType);

	//
	if(strType != null){
		
		List list = new ArrayList();

		try {
			 //构造查询sql语句
			
			if("edit".equals(strType)){
				String menuID = (String)request.getParameter("menuID");
				String parentID = (String)request.getParameter("parentID");
				String functionID = (String)request.getParameter("functionID");
				String level = (String)request.getParameter("level");
				String sortID = (String)request.getParameter("sortID");
				String visiable = (String)request.getParameter("visiable");

				String strDelSQL = "delete from menu where menuID=?";
				String[][] paramDel = new String[1][1];
				paramDel[0][0] = menuID;
				//int[] dataDel = con.exec(strDelSQL,paramDel);

				String strAddSQL = "insert into menu (menuID,parentID,functionID,level,sortID,visiable) values (?,?,?,?,?,?);";

				System.out.println("*******" + strAddSQL);

				String[][] re = new String[1][6];
				re[0][0] = menuID;
				re[0][1] = parentID;
				re[0][2] = functionID;
				re[0][3] = level;
				re[0][4] = sortID;
				re[0][5] = visiable;

				//int[] data = con.exec(strAddSQL,re);
			}else if("json".equals(strType)){
				//查询数据
				String strSQL = "select m.menuID,m.parentID,m.functionID,m.level,m.sortID,m.visiable,f.functionName from menu m,functionsinfo f where f.functionID=m.functionID  order by f.typeID,level,sortID;";
				//String[][] data = con.query(strSQL);

				if("true".equals(strExport)){
                                        response.setContentType("application/msexcel");
                                        response.setHeader("Content-disposition","inline; filename=reportMenuConfig.xls");
                                        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
					
					String sheetName = "报表";//sheet名
					String[] arryHeader = new String[] { "menuId", "parentId", "functionId", "level", "sort","visiable","functionName" };

					//Excel excel = new Excel();
					//ExcelSheet eSheet = excel.getSheet(sheetName);
					//eSheet.setHeader(arryHeader);
					//eSheet.addRecord(data);
					//excel.writeExcel(response.getOutputStream());
					return;
                }
				
				String rowsLimit = request.getParameter("rows");     //获取jqgrid没页要显示的行数
				int nPage = 1;//当前页数
				//int records = data.length;//录总行数
				//int total = records/Integer.parseInt(rowsLimit)+1;//总页数
				
				if(null == rowsLimit){//设置rowsLimit的默认值
					rowsLimit="10";
				}
				
				
				//行数据
				List<Map> rows = new ArrayList<Map>();
		
				//for(String[] arryT : data){
				//	Map map = new HashMap();
				//	map.put("id", arryT[0]);
				//	map.put("cell", arryT);
			
				//	rows.add(map);
				//}
				
				//rows
				//JQGridUtil t = new JQGridUtil();
				//GridPager<Map> gridPager = new GridPager<Map>( nPage, total, records, rows);

				//t.toJson(gridPager, response);
				System.out.println("======== json ");
			}
		} catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
					  
	  	//response.sendRedirect("/op/admin/menuConfig.jsp");
	}//end if 
%>
<body>
	<table id="tb_menu"></table>
	<div id="pager_menu"></div>

	<div>
		<br>
		<form name="addForm" action="/menu/saveMenu.do" method="post">
			<input type="hidden" value="edit" id="menuType" name="menuType">
			<table>
				<tr>
					<td>menuID：	</td>
					<td><input type="text" name="menuId" id="menuId" value=""/></td>
					<td>&nbsp;&nbsp;parentId：</td>
					<td>
						<input type="text" name="parentId" id="parentId" value=""/>
					</td>
<%--					<td>&nbsp;&nbsp;所属菜单：</td>
					<td>
						<select id="parentId" name="parentId" >
							<option value="0">菜单</option>
							<option value="95">用户行为统计</option>
							<option value="154">crm-达人管理</option>
							<option value="61">系统设置</option>
							<option value="74">渠道管理</option>
							<option value="44">豆果-数据自助超市</option>
							<option value="70">留存分析</option>
							<option value="114">圈圈统计</option>
							<option value="62">电商统计</option>
							<option value="87">APP渠道统计</option>
							<option value="63">APP统计</option>
							<option value="127">网站统计</option>
							<option value="83">菜谱统计</option>
							<option value="84">作品统计</option>
							<option value="37">地区分布</option>
							<option value="236">任务监控</option>
							<option value="247">文章统计</option>
						</select>
					</td>--%>
				</tr>
				<tr>
					<td>functionID：</td>
					<td><input type="text" readonly="readonly" name="functionId" id="functionId" value=""/></td>
					<td>&nbsp;&nbsp;层级:		</td>
					<!-- <input type="text" name="level" id="level" value=""/> -->
					<td>
						<select id="level" name="level" >
							<option value="1">一级分类</option>
							<option value="2">二级分类</option>
							<option value="3">三级分类</option>
							<option value="4">四级分类</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>sort：	</td>
					<td><input type="text" name="sort" id="sort" value=""/></td>
					<td>&nbsp;&nbsp;是否显示：</td>
					<td>
						<select id="visiable" name="visiable" >
							<option value="1">显示</option>
							<option value="0">不显示</option>
						</select><br/>
					</td>
				</tr>
			</table>
			<button class="submit" type="submit">提交修改</button>
		</form>
	</div>
	<script>
		jQuery("#tb_menu").jqGrid({
			url:'/menu/menuJson.do?menuType=json',
			datatype: "json",
			colNames:['menuId','parentId', 'functionId', 'level','sort','visiable','functionName'],
			colModel:[
				{name:'menuId',index:'menuId', width:55,align:"left",sortable:true},
				{name:'parentId',index:'parentId', width:90, align:"left",sortable:true},
				{name:'functionId',index:'functionId', width:100, align:"left",sortable:false},
				{name:'level',index:'level', width:80, align:"center",sortable:true},
				{name:'sort',index:'sort', width:80, align:"center",sortable:true},		
				{name:'visiable',index:'visiable', width:80,align:"center",sortable:false},		
				{name:'functionName',index:'functionName', width:150, align:"left", sortable:false}
			],
			height:237,
			width:877,
			rowNum:500,//设置初始显示数据的行数
			rowList:[100,500,1000],
			pager: '#pager_menu',
			sortname: 'level,sort',
			viewrecords: true,
			sortorder: "asc",
			loadonce: true,
			caption:"Menu配置管理",
			onSelectRow: function(id,status){ 
				var curRow = jQuery('#tb_menu').getRowData(id);
				jQuery("#menuId").val(curRow['menuId']);
				jQuery("#parentId").val(curRow['parentId']);
				jQuery("#functionId").val(curRow['functionId']);
				jQuery("#level").val(curRow['level']);
				jQuery("#sort").val(curRow['sort']);
				jQuery("#visiable").val(curRow['visiable']);
			}
		});
		jQuery("#tb_menu").jqGrid('navGrid','#pager_menu',{edit:false,edittext:'编辑',add:false,addtext:'增加',del:false,deltext:'删除',search:true,searchtext:'查询'});

		// add custom button to export the data to excel
		jQuery("#tb_menu").jqGrid('navButtonAdd','#pager_menu',{
			caption:"excel",
			onClickButton : function () { 
				jQuery("#tb_menu").jqGrid('excelExport',{"url":"menuConfig.jsp?menuType=json&export=true"});
			} 
		});

		$(window).bind('load', function() {
			uf_resizeGrid();
                 });
                $(window).bind('resize', function() {
			uf_resizeGrid();
                 });
		function uf_resizeGrid(){
			$("#tb_menu").setGridWidth($(window).width()*0.90);
			$("#tb_menu").setGridHeight($(window).height()*0.65);
		}
	</script>
</body>
</html>
