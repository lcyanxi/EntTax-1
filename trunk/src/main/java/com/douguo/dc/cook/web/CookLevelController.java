package com.douguo.dc.cook.web;

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

import com.douguo.dc.cook.service.CookService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/cooklevel")
public class CookLevelController {
	@Autowired
	private CookService cookService;

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

	@RequestMapping(value = "/cllist", method = RequestMethod.GET)
	public String clList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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
		// String type = request.getParameter("type");
		String type = "cook";

		if (null != sSource && !"".equals(sSource)) {
			source = Integer.parseInt(sSource);
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("source", source);

		/** 获取app应用列表数据 */
		Map<Integer, String> sourceMap = this.getSource();
		Map<String, String> levelMap = this.getLevelName();
		// provinceMap.get("11");
		List<Map<String, Object>> rowlst = cookService.queryCookLevelList(startDate, endDate);
		Map<String, String> rowlst3 = new HashMap<String, String>();
		Set<String> tableLine = new LinkedHashSet<String>();
		// Set<String> tableColumn = new HashSet<String>();
		// Set<String> tableColumn = new TreeSet<String>();
		Set<String> tableColumn = new LinkedHashSet<String>();
		Integer cooksSum = 0;
		String tmpDate = "";
		for (Map<String, Object> map : rowlst) {

			String level = (String) map.get("level");
			if (level != null) {
				level = level.trim();
			}

			String level_name = levelMap.get(level);
			
			if(level_name == null){
				continue;
			}

			String statdate = map.get("statdate").toString();
			Integer cooks = (Integer) map.get("cooks");
			if (cooks == null) {
				cooks = 0;
			}
			
			tableLine.add(statdate);
			tableColumn.add(level_name);
			
			
			rowlst3.put(level_name + '-' + statdate, String.valueOf(cooks));
			if("".equals(tmpDate)){
				cooksSum += cooks;
			}else if(tmpDate.equals(statdate)){
				cooksSum += cooks;
			}else{
				cooksSum = cooks;
			}
			rowlst3.put("总计" + '-' + statdate, String.valueOf(cooksSum));
			tmpDate = statdate;
		}
		//
		tableColumn.add("总计");
		
		request.getSession().setAttribute("rowlst2", rowlst3);
		request.getSession().setAttribute("tableLine", tableLine);
		request.getSession().setAttribute("tableColumn", tableColumn);
		return "/cook/cookLevelList";
	}
}