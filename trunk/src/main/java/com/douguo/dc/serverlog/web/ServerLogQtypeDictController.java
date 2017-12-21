package com.douguo.dc.serverlog.web;

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

import com.douguo.dc.serverlog.model.ServerLogQtypeDict;
import com.douguo.dc.serverlog.service.ServerLogQtypeDictService;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;

@Controller
@RequestMapping("/slog/qtypedict")
public class ServerLogQtypeDictController {

	@Autowired
	private ServerLogQtypeDictService serverLogQtypeDictService;

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

		List<ServerLogQtypeDict> list = serverLogQtypeDictService.queryAll();

		// int records = data.length;
		int records = list.size();

		total = records / Integer.parseInt(rowsLimit) + 1; // 计算总的页数
		String[][] arryFun = new String[list.size()][];

		for (int i = 0; i < list.size(); i++) {
			ServerLogQtypeDict fun = list.get(i);
			arryFun[i] = new String[] { String.valueOf(fun.getId()), String.valueOf(fun.getQtype()),
					String.valueOf(fun.getQtypeName()), String.valueOf(fun.getService()), String.valueOf(fun.getCreateTime()),
					String.valueOf(fun.getQdesc()),
					"<a href='/slog/qtypedict/preEdit.do?id=" + fun.getId() + "' title='修改' >修改</a>" };
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
		return "/serverlog/serverLogQtypeDictManager";
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
		ServerLogQtypeDict slogQtypeDict = serverLogQtypeDictService.getServerLogQtypeDict(id);
		model.put("slogQtypeDict", slogQtypeDict);
		return "/serverlog/serverLogQtypeDictModify";
	}

	@RequestMapping(value = "/insert")
	public String insert(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String qtype = request.getParameter("qtype");
		String qtypeName = request.getParameter("qtypeName");
		String service = request.getParameter("service");
		String createTime = request.getParameter("createTime");
		String desc = request.getParameter("qdesc");
		//
		ServerLogQtypeDict slogQtypeDict = new ServerLogQtypeDict();
		slogQtypeDict.setQtype(qtype);
		slogQtypeDict.setQtypeName(qtypeName);
		if (StringUtils.isNotBlank(service)) {
			slogQtypeDict.setService(service);
		}

		slogQtypeDict.setCreateTime(createTime);
		slogQtypeDict.setQdesc(desc);

		serverLogQtypeDictService.insert(slogQtypeDict);
		return "/serverlog/serverLogQtypeDictManager";
	}

	@RequestMapping(value = "/update")
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String id = request.getParameter("id");
		String qtype = request.getParameter("qtype");
		String qtypeName = request.getParameter("qtypeName");
		String service = request.getParameter("service");
		String createTime = request.getParameter("createTime");
		String qdesc = request.getParameter("qdesc");

		if (id != null && id.trim().length() > 0) {
			ServerLogQtypeDict slogQtypeDict = serverLogQtypeDictService.getServerLogQtypeDict(id);

			if (slogQtypeDict == null) {
				slogQtypeDict = new ServerLogQtypeDict();
				slogQtypeDict.setId(Integer.parseInt(id));
				slogQtypeDict.setQtype(qtype);
				slogQtypeDict.setQtypeName(qtypeName);
				slogQtypeDict.setService(service);
				slogQtypeDict.setCreateTime(createTime);
				slogQtypeDict.setQdesc(qdesc);
				serverLogQtypeDictService.insert(slogQtypeDict);
				// out.println("新增成功！");
			} else {// update
				slogQtypeDict.setId(Integer.parseInt(id));
				slogQtypeDict.setQtype(qtype);
				slogQtypeDict.setQtypeName(qtypeName);
				slogQtypeDict.setService(service);
				slogQtypeDict.setCreateTime(createTime);
				slogQtypeDict.setQdesc(qdesc);

				serverLogQtypeDictService.update(slogQtypeDict);
				// out.println("修改成功！");
			}
		} else {
			// out.println("functionId不能为空！");
		}
		return "/serverlog/serverLogQtypeDictManager";
	}
}
