package com.douguo.dc.channel.web;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.douguo.dc.channel.service.ChannelCooperationService;
import com.douguo.dc.channel.model.ChannelCooperation;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/channel/cooperation")
public class ChannelCooperationController {

	@Autowired
	private ChannelCooperationService channelCooperationService;

	@RequestMapping(value = "/queryJson")
	public void queryJson(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {

		request.setCharacterEncoding("utf-8");
		String rowsLimit = request.getParameter("rows"); // 取出每一页显示的行数
		String sidx = request.getParameter("sidx"); // 取出排序的项
		String sord = request.getParameter("sord"); // 取出排序方式：升序，降序
		String level = request.getParameter("level"); // 
		//
		
		if (rowsLimit == null) // 设置每一页显示行数的默认值
		{
			rowsLimit = "10";
		}
		JQGridUtil t = new JQGridUtil();
		int nPage = 1; // 当前显示的页数
		int total = 1; // 要显示的总的页数，初始值为1
		
		if(StringUtils.isBlank(level)){
			level = "1";
		}
		
		List<ChannelCooperation> list = channelCooperationService.queryAll();
		
		// int records = data.length;
		int records = list.size();
		
		total = records / Integer.parseInt(rowsLimit) + 1; // 计算总的页数
		String[][] arryFun = new String[list.size()][];

		for (int i = 0; i < list.size(); i++) {
			ChannelCooperation fun = list.get(i);
			arryFun[i] = new String[] { String.valueOf(fun.getId()), fun.getCopName(), String.valueOf(fun.getSort()),
					fun.getCopDesc(), String.valueOf(fun.getCreateTime()),
					"<a href='/channel/cooperation/preEdit.do?id=" + fun.getId() + "' title='修改' >修改</a>" };
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
	 * 渠道大类-管理页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param appId
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/preList")
	public String preList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//
		return "/channel/cooperation/channel_cooperation_manager";
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
		//
		//获取类型list
		ChannelCooperation channelCooperation = channelCooperationService.getChannelCooperation(id);
		model.put("channelCooperation", channelCooperation);
		
		return "/channel/cooperation/channel_cooperation_modify";
	}

	@RequestMapping(value = "/insert")
	public String insert(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		
		String cooperationName = request.getParameter("cooperationName");
		String cooperationDesc = request.getParameter("cooperationDesc");
		String sort = request.getParameter("sort");
		String createTime = request.getParameter("createTime");
		
		ChannelCooperation channelCooperation = new ChannelCooperation();
		channelCooperation.setCopName(cooperationName);
		channelCooperation.setCopDesc(cooperationDesc);
		
		//排序
		if (StringUtils.isNotBlank(sort)) {
			int nSort = Integer.parseInt(sort);
			channelCooperation.setSort(nSort);
		}
		//创建时间
		if (StringUtils.isBlank(createTime)) {
			createTime = DateUtil.date2Str(new Date(), "yyyy-MM-dd");
			channelCooperation.setCreateTime(createTime);
		}
		
		channelCooperation.setCreateTime(createTime);

		channelCooperationService.insert(channelCooperation);
		return "redirect:/channel/cooperation/preList.do";
	}

	@RequestMapping(value = "/update")
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String id = request.getParameter("id");
		String cooperationName = request.getParameter("cooperationName");
		String cooperationDesc = request.getParameter("cooperationDesc");
		String sort = request.getParameter("sort");
		String createTime = request.getParameter("createTime");

		if (id != null && id.trim().length() > 0) {
			ChannelCooperation channelCooperation = channelCooperationService.getChannelCooperation(id);

			if (channelCooperation != null) {
				//排序
				if (StringUtils.isNotBlank(sort)) {
					int nSort = Integer.parseInt(sort);
					channelCooperation.setSort(nSort);
				}
				channelCooperation.setId(Integer.parseInt(id));
				channelCooperation.setCopName(cooperationName);
				channelCooperation.setCopDesc(cooperationDesc);
				channelCooperation.setCreateTime(createTime);

				channelCooperationService.update(channelCooperation);
				// out.println("修改成功！");
			}
		} else {
			// out.println("functionId不能为空！");
		}
		return "redirect:/channel/cooperation/preList.do";
	}
}