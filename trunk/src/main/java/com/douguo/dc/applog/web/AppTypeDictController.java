package com.douguo.dc.applog.web;

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

import com.douguo.dc.applog.model.AppTypeDict;
import com.douguo.dc.applog.service.AppTypeDictService;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;

@Controller
@RequestMapping("/apptypedict")
public class AppTypeDictController {

	@Autowired
	private AppTypeDictService appTypeDictService;

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

		List<AppTypeDict> list = appTypeDictService.queryAll();

		// int records = data.length;
		int records = list.size();

		total = records / Integer.parseInt(rowsLimit) + 1; // 计算总的页数
		String[][] arryFun = new String[list.size()][];

		for (int i = 0; i < list.size(); i++) {
			AppTypeDict fun = list.get(i);
			arryFun[i] = new String[] { String.valueOf(fun.getId()), String.valueOf(fun.getType()),
					String.valueOf(fun.getData()), String.valueOf(fun.getVal()), String.valueOf(fun.getName()),
					String.valueOf(fun.getValDesc()),
					"<a href='/apptypedict/preEdit.do?id=" + fun.getId() + "' title='修改' >修改</a>" };
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
		return "/apptypedict/appTypeDictManager";
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
		AppTypeDict appTypeDict = appTypeDictService.getAppTypeDictById(id);
		model.put("appTypeDict", appTypeDict);
		return "/apptypedict/appTypeDictModify";
	}

	@RequestMapping(value = "/insert")
	public String insert(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String data = request.getParameter("data");
		String val = request.getParameter("val");
		String name = request.getParameter("name");
		String valDesc = request.getParameter("valDesc");

		if (id != null && id.trim().length() > 0) {
			// 判断Id是否重复
			AppTypeDict fun = appTypeDictService.getAppTypeDictById(id);
			if (fun == null) {
				AppTypeDict appTypeDict = new AppTypeDict();

				appTypeDict.setId(Integer.parseInt(id));
				appTypeDict.setType(Integer.parseInt(type));
				appTypeDict.setData(Integer.parseInt(data));
				appTypeDict.setVal(Integer.parseInt(val));
				appTypeDict.setName(name);
				appTypeDict.setValDesc(valDesc);

				appTypeDictService.insert(appTypeDict);
				// out.println("新增成功！");
			} else {
				// out.println(Id + "此ID已存在，请重新输入！");
			}
		} else {
			// out.println("Id不能为空！");
		}
		return "/apptypedict/appTypeDictManager";
	}

	@RequestMapping(value = "/update")
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String id = request.getParameter("id");
		Integer type = Integer.parseInt(request.getParameter("type"));
		Integer data = Integer.parseInt(request.getParameter("data"));
		Integer val = Integer.parseInt(request.getParameter("val"));
		String name = request.getParameter("name");
		String valDesc = request.getParameter("valDesc");

		if (id != null && id.trim().length() > 0) {
			AppTypeDict appTypeDict = appTypeDictService.getAppTypeDictById(id);

			if (appTypeDict == null) {
				appTypeDict = new AppTypeDict();
				appTypeDict.setId(Integer.parseInt(id));
				appTypeDict.setType(type);
				appTypeDict.setData(data);
				appTypeDict.setVal(val);
				appTypeDict.setName(name);
				appTypeDict.setValDesc(valDesc);
				appTypeDictService.insert(appTypeDict);
				// out.println("新增成功！");
			} else {// update
				appTypeDict.setId(Integer.parseInt(id));
				appTypeDict.setType(type);
				appTypeDict.setData(data);
				appTypeDict.setVal(val);
				appTypeDict.setName(name);
				appTypeDict.setValDesc(valDesc);

				appTypeDictService.update(appTypeDict);
				// out.println("修改成功！");
			}
		} else {
			// out.println("functionId不能为空！");
		}
		return "/apptypedict/appTypeDictManager";
	}
}
