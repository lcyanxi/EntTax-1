package com.douguo.dc.user.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.douguo.dc.user.model.Function;
import com.douguo.dc.user.model.Menu;
import com.douguo.dc.user.model.User;
import com.douguo.dc.user.service.FunctionService;
import com.douguo.dc.user.service.MenuService;
import com.douguo.dc.user.service.UserMenuService;
import com.douguo.dc.user.service.UserService;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;

@Controller
@RequestMapping("/fun")
public class FunctionController {

	@Autowired
	private FunctionService functionService;

	@Autowired
	private MenuService menuService;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private String[] typedata = new String[] { "系统设置", "全网数据" };

	@RequestMapping(value = "/queryJson")
	public void queryJson(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {

		request.setCharacterEncoding("utf-8");
		String rowsLimit = request.getParameter("rows"); // 取出每一页显示的行数
		String sidx = request.getParameter("sidx"); // 取出排序的项
		String sord = request.getParameter("sord"); // 取出排序方式：升序，降序

		if (rowsLimit == null) // 设置每一页显示行数的默认值
		{
			rowsLimit = "10";
		}
		JQGridUtil t = new JQGridUtil();
		int nPage = 1; // 当前显示的页数
		int total = 1; // 要显示的总的页数，初始值为1
		// String selSql =
		// "SELECT functionID,functionName,uri,type,typeID FROM functionsinfo ";
		// String[][] data = con.query(selSql);

		List<Function> list = functionService.queryAll();

		// int records = data.length;
		int records = list.size();

		total = records / Integer.parseInt(rowsLimit) + 1; // 计算总的页数
		// String[][] arryFun = data;
		String[][] arryFun = new String[list.size()][];
		// for (int i = 0; i < data.length; i++) {
		// arryFun[i] = new String[] { data[i][0], data[i][1], data[i][2],
		// data[i][3], data[i][4],
		// "<a href='functioninfo_modify.jsp?functionID=" + data[i][0] +
		// "' title='修改' >修改</a>" };
		// }

		for (int i = 0; i < list.size(); i++) {
			Function fun = list.get(i);
			arryFun[i] = new String[] { fun.getFunctionId(), fun.getFunctionName(), fun.getUri(), fun.getType(),
					fun.getTypeId(),
					"<a href='/fun/preEdit.do?functionId=" + fun.getFunctionId() + "' title='修改' >修改</a>" };
		}

		// 行数据
		List<Map> rows = new ArrayList<Map>();
		for (String[] axx : arryFun) {
			Map map = new HashMap();
			map.put("id", "1");
			map.put("cell", axx);

			rows.add(map);
		}

		// rows
		GridPager<Map> gridPager = new GridPager<Map>(nPage, total, records, rows); // 将表格显示的配置初始化给GridPager
		t.toJson(gridPager, response); // 发送json数据

	}

	/**
	 * 管理页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param appId
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/preFun")
	public String preFun(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//
		return "/admin/functioninfo";
	}

	/**
	 * 修改页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param appId
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/preEdit")
	public String preEdit(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//

		String functionId = request.getParameter("functionId");
		Function fun = functionService.getFunction(functionId);
		model.put("fun", fun);
		return "/admin/functioninfoModify";
	}

	@RequestMapping(value = "/insert")
	public String insert(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String functionId = request.getParameter("functionId");
		String functionName = request.getParameter("functionName");
		String uri = request.getParameter("uri");
		String typeId = request.getParameter("typeId");
		String typeName = "";
		if (functionId != null && functionId.trim().length() > 0) {
			// 判断functionId是否重复
			// String sql =
			// "select count(1) from functionsinfo where functionID=?";
			// String[] data = con.queryRow(sql,new String[]{functionID});
			Function fun = functionService.getFunction(functionId);
			if (fun == null) {
				String[][] re = new String[1][5];
				re[0][0] = functionId;
				re[0][1] = functionName;
				re[0][2] = uri;
				re[0][4] = typeId;

				// 判断uri是否重复
				// String sqluri =
				// "select count(3) from functionsinfo where uri=?";
				// String[] datauri = con.queryRow(sqluri, new String[] {
				// re[0][2] });

				Function funUri = functionService.getFunctionByUri(uri);
				// if ("0".equals(datauri[0]) || re[0][2].equals("#")) {
				if (funUri == null || uri.equals("#")) {
					int index = Integer.parseInt(typeId);
					re[0][3] = typedata[index];
					typeName = typedata[index];
					// sql =
					// "insert into functionsinfo (functionID,functionName,uri,type,typeID) values (?,?,?,?,?)";
					// con.exec(sql, re);
					funUri = new Function();
					funUri.setFunctionId(functionId);
					funUri.setFunctionName(functionName);
					funUri.setUri(uri);
					funUri.setType(typeName);
					funUri.setTypeId(typeId);

					functionService.insert(funUri);
					// System.out.println(con);
					// String menu_sql =
					// "select max(menuID) as maxid from menu";
					// String[] maxid = con.queryRow(menu_sql);

					// int max_menuID = Integer.parseInt(maxid[0]) + 1;
					// String insert_menu_sql =
					// "insert into menu (menuID,parentID,functionID,level,sortID,visiable) values ('"
					// + max_menuID + "','0','" + functionID + "','1','1','0')";
					// con.exec(insert_menu_sql);
					Menu menu = new Menu();
					menu.setId(null);
					menu.setParentId("0");
					menu.setFunctionId(functionId);
					menu.setLevel("1");
					menu.setSort("1");
					menu.setVisiable("0");
					menuService.saveMenu(menu);
					// out.println("新增成功！");
				} else {
					// out.println("uri " + re[0][2] + "已经存在，请重新输入！");
				}
			} else {
				// out.println(functionId + "此functionID已存在，请重新输入！");
			}
		} else {
			// out.println("functionId不能为空！");
		}
		return "/admin/functioninfo";
	}

	@RequestMapping(value = "/update")
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String functionId = request.getParameter("functionId");
		String functionName = request.getParameter("functionName");
		String uri = request.getParameter("uri");
		Integer typeId = Integer.parseInt(request.getParameter("typeId"));

		if (functionId != null && functionId.trim().length() > 0) {
			Function fun = functionService.getFunction(functionId);

			if (fun == null) {
				fun = new Function();
				fun.setFunctionId(functionId);
				fun.setFunctionName(functionName);
				fun.setUri(uri);
				fun.setType(typedata[typeId]);
				fun.setTypeId(String.valueOf(typeId));
				//functionService.insert(fun);
				// out.println("新增成功！");
			} else {// update
				fun.setFunctionId(functionId);
				fun.setFunctionName(functionName);
				fun.setUri(uri);
				fun.setType(typedata[typeId]);
				fun.setTypeId(String.valueOf(typeId));
				functionService.update(fun);
				// out.println("修改成功！");
			}
		} else {
			// out.println("functionId不能为空！");
		}
		return "/admin/functioninfo";
	}
}
