package com.douguo.dc.applog.web;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.douguo.dc.applog.model.AppLogClickDict;
import com.douguo.dc.applog.service.AppLogClickDictService;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;

@Controller
@RequestMapping("/appclickdict")
public class AppLogClickDictController {

	@Autowired
	private AppLogClickDictService appLogClickDictService;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

		List<AppLogClickDict> list = appLogClickDictService.queryAll();

		// int records = data.length;
		int records = list.size();

		total = records / Integer.parseInt(rowsLimit) + 1; // 计算总的页数
		String[][] arryFun = new String[list.size()][];

		for (int i = 0; i < list.size(); i++) {
			AppLogClickDict fun = list.get(i);
			arryFun[i] = new String[] { String.valueOf(fun.getId()), String.valueOf(fun.getPage()),
					String.valueOf(fun.getView()), String.valueOf(fun.getPosition()), String.valueOf(fun.getTitle()),
					String.valueOf(fun.getTitleDesc()),
					"<a href='/appclickdict/preEdit.do?id=" + fun.getId() + "' title='修改' >修改</a>" };
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
	@RequestMapping(value = "/preList")
	public String preFun(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//
		return "/app_click_dict/appLogClickDictManager";
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

		String id = request.getParameter("id");
		AppLogClickDict appLogClickDict = appLogClickDictService.getAppLogClickDict(id);
		model.put("appLogClickDict", appLogClickDict);
		return "/app_click_dict/appLogClickDictModify";
	}

	@RequestMapping(value = "/insert")
	public String insert(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String page = request.getParameter("page");
		String view = request.getParameter("view");
		String position = request.getParameter("position");
		String title = request.getParameter("title");
		String titleDesc = request.getParameter("titleDesc");
		//
		AppLogClickDict appLogClickDict = new AppLogClickDict();
		appLogClickDict.setPage(Integer.parseInt(page));
		appLogClickDict.setView(Integer.parseInt(view));
		if (StringUtils.isNotBlank(position)) {
			appLogClickDict.setPosition(Integer.parseInt(position));
		}

		appLogClickDict.setTitle(title);
		appLogClickDict.setTitleDesc(titleDesc);

		appLogClickDictService.insert(appLogClickDict);
		return "/app_click_dict/appLogClickDictManager";
	}

	@RequestMapping(value = "/update")
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String id = request.getParameter("id");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer view = Integer.parseInt(request.getParameter("view"));
		Integer position = Integer.parseInt(request.getParameter("position"));
		String title = request.getParameter("title");
		String titleDesc = request.getParameter("titleDesc");

		if (id != null && id.trim().length() > 0) {
			AppLogClickDict appLogClickDict = appLogClickDictService.getAppLogClickDict(id);

			if (appLogClickDict == null) {
				appLogClickDict = new AppLogClickDict();
				appLogClickDict.setId(Integer.parseInt(id));
				appLogClickDict.setPage(page);
				appLogClickDict.setView(view);
				appLogClickDict.setPosition(position);
				appLogClickDict.setTitle(title);
				appLogClickDict.setTitleDesc(titleDesc);
				appLogClickDictService.insert(appLogClickDict);
				// out.println("新增成功！");
			} else {// update
				appLogClickDict.setId(Integer.parseInt(id));
				appLogClickDict.setPage(page);
				appLogClickDict.setView(view);
				appLogClickDict.setPosition(position);
				appLogClickDict.setTitle(title);
				appLogClickDict.setTitleDesc(titleDesc);

				appLogClickDictService.update(appLogClickDict);
				// out.println("修改成功！");
			}
		} else {
			// out.println("functionId不能为空！");
		}
		return "/app_click_dict/appLogClickDictManager";
	}
}
