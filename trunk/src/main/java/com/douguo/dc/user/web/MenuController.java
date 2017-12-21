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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.douguo.dc.user.model.Menu;
import com.douguo.dc.user.model.User;
import com.douguo.dc.user.service.MenuService;
import com.douguo.dc.user.service.UserMenuService;
import com.douguo.dc.user.utils.Tree;
import com.douguo.dc.user.utils.UserInfoUtil;
import com.douguo.dc.user.utils.export.excel.Excel;
import com.douguo.dc.user.utils.export.excel.ExcelSheet;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;

@Controller
@RequestMapping("/menu")
public class MenuController {

	private final static Log logger = LogFactory.getLog(MenuController.class);

	@Autowired
	private UserMenuService userMenuService;

	@Autowired
	private MenuService menuService;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@RequestMapping(value = "/menuConfig")
	public String menuConfig(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		return "/admin/menuConfig";
	}

	@RequestMapping(value = "/menuJson")
	public String menuJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");  
		String strExport = (String) request.getParameter("export");
		// 查询数据
		String strSQL = "select m.menuID,m.parentID,m.functionID,m.level,m.sortID,m.visiable,f.functionName from menu m,functionsinfo f where f.functionID=m.functionID  order by f.typeID,level,sortID;";

		List<Map<String, Object>> listData = menuService.queryMenu();

		if ("true".equals(strExport)) {
			response.setContentType("application/msexcel");
			response.setHeader("Content-disposition", "inline; filename=reportMenuConfig.xls");
			response.getWriter().println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");

			String sheetName = "报表";// sheet名
			String[] arryHeader = new String[] { "menuID", "parentID", "functionID", "level", "sortID", "visiable",
					"functionName" };

			Excel excel = new Excel();
			ExcelSheet eSheet = excel.getSheet(sheetName);
			eSheet.setHeader(arryHeader);
			// eSheet.addRecord(data);
			excel.writeExcel(response.getOutputStream());
			return null;
		}

		String rowsLimit = request.getParameter("rows"); // 获取jqgrid没页要显示的行数
		int nPage = 1;// 当前页数

		int records = listData.size();// 录总行数
		int total = records / Integer.parseInt(rowsLimit) + 1;// 总页数

		if (null == rowsLimit) {// 设置rowsLimit的默认值
			rowsLimit = "10";
		}

		// 行数据
		List<Map> rows = new ArrayList<Map>();

		for (Map<String, Object> mapMenu : listData) {
			Map map = new HashMap();
			map.put("id", String.valueOf(mapMenu.get("id")));
			String[] argCell = new String[7];
			argCell[0] = String.valueOf(mapMenu.get("id"));
			argCell[1] = String.valueOf(mapMenu.get("parent_id"));
			argCell[2] = String.valueOf(mapMenu.get("function_id"));
			argCell[3] = String.valueOf(mapMenu.get("level"));
			argCell[4] = String.valueOf(mapMenu.get("sort"));
			argCell[5] = String.valueOf(mapMenu.get("visiable"));
			argCell[6] = String.valueOf(mapMenu.get("function_name"));
			map.put("cell", argCell);

			rows.add(map);
		}

		// rows
		JQGridUtil t = new JQGridUtil();
		GridPager<Map> gridPager = new GridPager<Map>(nPage, total, records, rows);

		t.toJson(gridPager, response);

		return "/admin/menuConfig";
	}

	@RequestMapping(value = "/saveMenu")
	public String saveMenu(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String menuId = (String) request.getParameter("menuId");
		String parentId = (String) request.getParameter("parentId");
		String functionId = (String) request.getParameter("functionId");
		String level = (String) request.getParameter("level");
		String sort = (String) request.getParameter("sort");
		String visiable = (String) request.getParameter("visiable");
		Menu menu = new Menu();
		menu.setId(menuId);
		menu.setParentId(parentId);
		menu.setFunctionId(functionId);
		menu.setLevel(level);
		menu.setSort(sort);
		menu.setVisiable(visiable);

		menuService.saveMenu(menu);
		return "/admin/menuConfig";
	}

	/**
	 * 事件列表页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/buildTree")
	public String buildTree(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		response.setContentType("text/html;charset=UTF-8");  
		 String strUserId = (String) request.getParameter("uid");
		User user = (User) request.getSession().getAttribute(UserInfoUtil.GLOBAL_SESSION_USR_CACHE_KEY);
		String curUserID = "";
		if (user != null) {
			curUserID = user.getUid();
		}

		String strType = (String) request.getParameter("type");

		String[][] dataMenu = null;
		String[][] dataUserFunction = null;
		try {

			List<Map<String, Object>> listUserMenu = userMenuService.queryUserMenu(curUserID, strType);

			dataMenu = new String[listUserMenu.size()][8];
			for (int i = 0; i < listUserMenu.size(); i++) {
				Map<String, Object> map = listUserMenu.get(i);
				dataMenu[i][0] = String.valueOf(map.get("id"));
				dataMenu[i][1] = String.valueOf(map.get("parent_id"));
				dataMenu[i][2] = (String) map.get("function_id");
				dataMenu[i][3] = String.valueOf(map.get("level"));
				dataMenu[i][4] = String.valueOf(map.get("sort"));
				dataMenu[i][5] = String.valueOf(map.get("visiable"));
				dataMenu[i][6] = (String) map.get("function_name");
				dataMenu[i][7] = (String) map.get("uri");
			}
			//获得需授权用户的权限信息
			List<Map<String, Object>> listUserFunction = userMenuService.queryUserFunction(strUserId);

			//
			List<String> listIds = new ArrayList<String>();
			for (Map<String, Object> map : listUserFunction) {
				listIds.add(String.valueOf(map.get("function_id")));
			}

			Tree tree = new Tree();
			tree.toJson(dataMenu, listIds, response);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}