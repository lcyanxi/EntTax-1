package com.douguo.dc.dish.web;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.douguo.dc.dish.service.DishService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/dishtag")
public class DishTagController {
	@Autowired
	private DishService dishService;

	private Map<Integer, String> getSource() {

		Map<Integer, String> sourceMap = new HashMap<Integer, String>();
		sourceMap.put(0, "网站");
		sourceMap.put(3, "IOS豆果美食");
		sourceMap.put(4, "Andriod豆果美食");
		return sourceMap;
	}

	private Map<String, String> getLevelName() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "A+");
		map.put("2", "A");
		map.put("3", "B");
		map.put("4", "C");
		map.put("5", "D");
		map.put("6", "E");
		map.put("red", "红旗");
		map.put("del", "删除");

		return map;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		if (null == appId || appId.equals("")) {
			appId = "1";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(System.currentTimeMillis());
		String startDate = request.getParameter("startDate");
		if (null == startDate || startDate.equals("")) {
			startDate = DateUtil.getSpecifiedDayBefore(today, 6);
		}
		String endDate = request.getParameter("endDate");
		if (null == endDate || endDate.equals("")) {
			endDate = DateUtil.getSpecifiedDayBefore(today, 1);
		}

		Integer source = 0;
		String sSource = request.getParameter("source");

		if (null != sSource && !"".equals(sSource)) {
			source = Integer.parseInt(sSource);
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("source", source);

		/** 获取app应用列表数据 */
		Map<Integer, String> sourceMap = this.getSource();
		Map<String, String> dishNameMap = this.getLevelName();
		// provinceMap.get("11");
		List<Map<String, Object>> rowlst = dishService.queryDishTagList(startDate, endDate);
		//作品数
		Map<String, String> rowMapDishs = new HashMap<String, String>();
		//人数
		Map<String, String> rowMapUsers = new HashMap<String, String>();
		//tagName转换
		Map<String, String> rowMapTagName = new HashMap<String, String>();
		Set<String> tableLine = new LinkedHashSet<String>();
		Set<String> tableColumn = new LinkedHashSet<String>();
		//作品总计
		Integer dishsSum = 0;
		//用户数总计
		Integer usersSum = 0;
		String tmpDate = "";
		for (Map<String, Object> map : rowlst) {

			Long tagId = (Long) map.get("tag_id");
			String tagName = (String) map.get("tag_name");

			String statdate = map.get("statdate").toString();
			Integer dishs = (Integer) map.get("dishs");
			Integer users = (Integer) map.get("users");
			if (dishs == null) {
				dishs = 0;
			}
			
			if (users == null) {
				users = 0;
			}

			tableLine.add(statdate);
			tableColumn.add(String.valueOf(tagId));
			rowMapTagName.put(String.valueOf(tagId), tagName);
			
			if(source == 0){
				rowMapDishs.put(String.valueOf(tagId) + '-' + statdate, String.valueOf(dishs));
				
				if ("".equals(tmpDate)) {
					dishsSum += dishs;
				} else if (tmpDate.equals(statdate)) {
					dishsSum += dishs;
				} else {
					dishsSum = dishs;
				}
				rowMapDishs.put("总计" + '-' + statdate, String.valueOf(dishsSum));
				tmpDate = statdate;
				request.getSession().setAttribute("rowMapDishs", rowMapDishs);
			}else{
				rowMapUsers.put(String.valueOf(tagId) + '-' + statdate, String.valueOf(users));
				if ("".equals(tmpDate)) {
					usersSum += users;
				} else if (tmpDate.equals(statdate)) {
					usersSum += users;
				} else {
					usersSum = users;
				}
				rowMapUsers.put("总计" + '-' + statdate, String.valueOf(usersSum));
				tmpDate = statdate;
				request.getSession().setAttribute("rowMapUsers", rowMapUsers);
			}
			
		}
		//
		tableColumn.add("总计");
		
		request.getSession().setAttribute("rowMapTagName", rowMapTagName);
		request.getSession().setAttribute("tableLine", tableLine);
		request.getSession().setAttribute("tableColumn", tableColumn);
		if(source == 0){
			return "/dish/dishTagList";
		}else{
			return "/dish/dishTagUserList";
		}
		
	}
}