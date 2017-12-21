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

import com.douguo.dc.channel.service.ChannelTypeService;
import com.douguo.dc.channel.model.ChannelType;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/channel/type")
public class ChannelTypeController {

	@Autowired
	private ChannelTypeService channelTypeService;

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
		
		List<ChannelType> list = channelTypeService.queryListByLevel(level);
		
		// int records = data.length;
		int records = list.size();
		
		total = records / Integer.parseInt(rowsLimit) + 1; // 计算总的页数
		String[][] arryFun = new String[list.size()][];

		for (int i = 0; i < list.size(); i++) {
			ChannelType fun = list.get(i);
			arryFun[i] = new String[] { String.valueOf(fun.getId()), fun.getTypeName(), String.valueOf(fun.getSort()),
					fun.getTypeDesc(), String.valueOf(fun.getCreateTime()),
					"<a href='/channel/type/preEdit.do?id=" + fun.getId() + "&level="+fun.getLevel() + "' title='修改' >修改</a>" };
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
	@RequestMapping(value = "/preList/1")
	public String preList1(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//
		String level = request.getParameter("level");
		if(StringUtils.isBlank(level)){
			level = "1";
		}
		//设置类型
		model.put("level", level);
		
		return "/channel/type/channel_type_manager_1";
	}
	
	/**
	 * 渠道类别-管理页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param appId
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/preList/2")
	public String preList2(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//
		String level = request.getParameter("level");
		if(StringUtils.isBlank(level)){
			level = "2";
		}
		//获取大类list
		List<ChannelType> list = channelTypeService.queryListByLevel("1");
		
		//设置类型
		model.put("level", level);
		model.put("parentList", list);
		
		return "/channel/type/channel_type_manager_2";
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
	@RequestMapping(value = "/preList/3")
	public String preList3(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//
		String level = request.getParameter("level");
		if(StringUtils.isBlank(level)){
			level = "3";
		}
		//获取类型list
		List<ChannelType> list = channelTypeService.queryListByLevel("2");
				
		//设置类型
		model.put("level", level);
		model.put("parentList", list);
		
		return "/channel/type/channel_type_manager_3";
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
		String level = request.getParameter("level");
		//
		//获取类型list
		int nLevel = Integer.parseInt(level);
		if(nLevel > 1){
			List<ChannelType> list = channelTypeService.queryListByLevel(String.valueOf(nLevel - 1));
			model.put("parentList", list);
		}
		
		//
		ChannelType channelType = channelTypeService.getChannelType(id);
		model.put("channelType", channelType);
		model.put("level", level);
		
		return "/channel/type/channel_type_modify";
	}

	@RequestMapping(value = "/insert")
	public String insert(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		
		String parentId = request.getParameter("parentId");
		String typeName = request.getParameter("typeName");
		String typeDesc = request.getParameter("typeDesc");
		String level = request.getParameter("level");
		String sort = request.getParameter("sort");
		String createTime = request.getParameter("createTime");
		
		//级别
		ChannelType channelType = new ChannelType();
		channelType.setTypeName(typeName);
		channelType.setTypeDesc(typeDesc);
		
		int nParentId = 0;
		if (StringUtils.isNotBlank(parentId)) {
			nParentId = Integer.parseInt(parentId);
		}
		channelType.setParentId(nParentId);
		
		//
		if (StringUtils.isNotBlank(level)) {
			int nLevel = Integer.parseInt(level);
			channelType.setLevel(nLevel);
		}
		//排序
		if (StringUtils.isNotBlank(sort)) {
			int nSort = Integer.parseInt(sort);
			channelType.setSort(nSort);
		}
		//创建时间
		if (StringUtils.isBlank(createTime)) {
			createTime = DateUtil.date2Str(new Date(), "yyyy-MM-dd");
			channelType.setCreateTime(createTime);
		}
		
		channelType.setCreateTime(createTime);
		

		channelTypeService.insert(channelType);
		return "redirect:/channel/type/preList/"+level+".do";
	}

	@RequestMapping(value = "/update")
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String id = request.getParameter("id");
		String parentId = request.getParameter("parentId");
		String typeName = request.getParameter("typeName");
		String typeDesc = request.getParameter("typeDesc");
		String level = request.getParameter("level");
		String sort = request.getParameter("sort");
		String createTime = request.getParameter("createTime");

		if (id != null && id.trim().length() > 0) {
			ChannelType channelType = channelTypeService.getChannelType(id);

			if (channelType != null) {
				//
				if (StringUtils.isNotBlank(level)) {
					int nLevel = Integer.parseInt(level);
					channelType.setLevel(nLevel);
				}
				//排序
				if (StringUtils.isNotBlank(sort)) {
					int nSort = Integer.parseInt(sort);
					channelType.setSort(nSort);
				}
				if(StringUtils.isBlank(parentId)){
					parentId = "0";
				}
				channelType.setId(Integer.parseInt(id));
				channelType.setParentId(Integer.parseInt(parentId));
				channelType.setTypeName(typeName);
				channelType.setTypeDesc(typeDesc);
				channelType.setCreateTime(createTime);

				channelTypeService.update(channelType);
				// out.println("修改成功！");
			}
		} else {
			// out.println("functionId不能为空！");
		}
		return "redirect:/channel/type/preList/" + level + ".do";
	}
}