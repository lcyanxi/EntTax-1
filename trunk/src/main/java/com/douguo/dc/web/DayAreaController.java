package com.douguo.dc.web;

import java.math.BigDecimal;
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

import com.douguo.dc.service.DayAreaService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/dayArea")
public class DayAreaController {
	@Autowired
	private DayAreaService dayAreaService;

	private Map<Integer, String> getSource() {

		Map<Integer, String> sourceMap = new HashMap<Integer, String>();
		sourceMap.put(0, "网站");
		sourceMap.put(3, "IOS豆果美食");
		sourceMap.put(4, "Andriod豆果美食");
		return sourceMap;
	}

	private Map<String, String> getProvince() {
		Map<String, String> provinceMap = new HashMap<String, String>();
		provinceMap.put("Beijing", "北京");
		provinceMap.put("Shanghai", "上海");
		provinceMap.put("Tianjin", "天津");
		provinceMap.put("Chongqing", "重庆");
		provinceMap.put("Guangdong", "广东");
		provinceMap.put("Fujian", "福建");
		provinceMap.put("Zhejiang", "浙江");
		provinceMap.put("Jiangsu", "江苏");
		provinceMap.put("Shandong", "山东");
		provinceMap.put("Liaoning", "辽宁");
		provinceMap.put("Jiangxi", "江西");
		provinceMap.put("Sichuan", "四川");
		provinceMap.put("Shaanxi", "陕西");
		provinceMap.put("Shanxi", "陕西");

		provinceMap.put("Hubei", "湖北");
		provinceMap.put("Henan", "河南");
		provinceMap.put("Hebei", "河北");
		provinceMap.put("Shanxi", "山西");
		provinceMap.put("Nei Mongol", "内蒙古");
		provinceMap.put("Jilin", "吉林");
		provinceMap.put("Heilongjiang", "黑龙江");
		provinceMap.put("Anhui", "安徽");
		provinceMap.put("Hunan", "湖南");
		provinceMap.put("Guangxi", "广西");
		provinceMap.put("Hainan", "海南");
		provinceMap.put("Yunnan", "云南");
		provinceMap.put("Guizhou", "贵州");
		provinceMap.put("Xizang", "西藏");
		provinceMap.put("Gansu", "甘肃");
		provinceMap.put("Ningxia", "宁夏");
		provinceMap.put("Qinghai", "青海");
		provinceMap.put("Xinjiang", "新疆");

		provinceMap.put("Hong Kong", "香港");
		// provinceMap.put(33,"澳门");
		// provinceMap.put(35,"台湾");
		// provinceMap.put(3118,"海外");
		provinceMap.put("null", "未知");
		provinceMap.put("NULL", "未知");

		return provinceMap;
	}

	@RequestMapping(value = "/recipeList", method = RequestMethod.GET)
	public String recipseList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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
//		String type = request.getParameter("type");
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
		Map<String, String> provinceMap = this.getProvince();
		// provinceMap.get("11");
		List<Map<String, Object>> rowlst = dayAreaService.queryProvinceList(startDate, endDate, type, source);
		Map<String, String> rowlst3 = new HashMap<String, String>();
		Set<String> tableLine = new LinkedHashSet<String>();
		// Set<String> tableColumn = new HashSet<String>();
		// Set<String> tableColumn = new TreeSet<String>();
		Set<String> tableColumn = new LinkedHashSet<String>();
		for (Map<String, Object> map : rowlst) {

			String province = (String) map.get("province");
			if(province != null){
				province = province.trim();
			}
			// 香港的是null
			if (province.equals("null")) {
				province = (String) map.get("city");
			}
			String province_name = provinceMap.get(province);
			if (province_name == null) {
				province_name = province;
			}
			String statdate = map.get("statdate").toString();
			BigDecimal num = (BigDecimal) map.get("pnum");
			BigDecimal usernum = (BigDecimal) map.get("pusernum");
			if (num == null) {
				num = new BigDecimal("0");
			}
			if (usernum == null) {
				usernum = new BigDecimal("0");
			}
			tableLine.add(statdate);
			tableColumn.add(province_name);
			rowlst3.put(province_name + '-' + statdate, num.intValue() + "(" + usernum.intValue() + ")");
		}
		request.getSession().setAttribute("rowlst2", rowlst3);
		request.getSession().setAttribute("tableLine", tableLine);
		request.getSession().setAttribute("tableColumn", tableColumn);
		return "/dayarea/recipeList";
	}
	
	@RequestMapping(value = "/dishList", method = RequestMethod.GET)
	public String dishList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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
//		String type = request.getParameter("type");
		String type = "dish";

		if (null != sSource && !"".equals(sSource)) {
			source = Integer.parseInt(sSource);
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("source", source);

		/** 获取app应用列表数据 */
		Map<Integer, String> sourceMap = this.getSource();
		Map<String, String> provinceMap = this.getProvince();
		// provinceMap.get("11");
		List<Map<String, Object>> rowlst = dayAreaService.queryProvinceList(startDate, endDate, type, source);
		Map<String, String> rowlst3 = new HashMap<String, String>();
		Set<String> tableLine = new LinkedHashSet<String>();
		// Set<String> tableColumn = new HashSet<String>();
		// Set<String> tableColumn = new TreeSet<String>();
		Set<String> tableColumn = new LinkedHashSet<String>();
		for (Map<String, Object> map : rowlst) {

			String province = (String) map.get("province");
			if(province != null){
				province = province.trim();
			}
			// 香港的是null
			if (province.equals("null")) {
				province = (String) map.get("city");
			}
			String province_name = provinceMap.get(province);
			if (province_name == null) {
				province_name = province;
			}
			String statdate = map.get("statdate").toString();
			BigDecimal num = (BigDecimal) map.get("pnum");
			BigDecimal usernum = (BigDecimal) map.get("pusernum");
			if (num == null) {
				num = new BigDecimal("0");
			}
			if (usernum == null) {
				usernum = new BigDecimal("0");
			}
			tableLine.add(statdate);
			tableColumn.add(province_name);
			rowlst3.put(province_name + '-' + statdate, num.intValue() + "(" + usernum.intValue() + ")");
		}
		request.getSession().setAttribute("rowlst2", rowlst3);
		request.getSession().setAttribute("tableLine", tableLine);
		request.getSession().setAttribute("tableColumn", tableColumn);
		return "/dayarea/dishList";
	}
	
	@RequestMapping(value = "/commentList", method = RequestMethod.GET)
	public String commentList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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
//		String type = request.getParameter("type");
		String type = "comment";

		if (null != sSource && !"".equals(sSource)) {
			source = Integer.parseInt(sSource);
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("source", source);

		/** 获取app应用列表数据 */
		Map<Integer, String> sourceMap = this.getSource();
		Map<String, String> provinceMap = this.getProvince();
		// provinceMap.get("11");
		List<Map<String, Object>> rowlst = dayAreaService.queryProvinceListLikeType(startDate, endDate, type, source);
		Map<String, String> rowlst3 = new HashMap<String, String>();
		Set<String> tableLine = new LinkedHashSet<String>();
		// Set<String> tableColumn = new HashSet<String>();
		// Set<String> tableColumn = new TreeSet<String>();
		Set<String> tableColumn = new LinkedHashSet<String>();
		for (Map<String, Object> map : rowlst) {

			String province = (String) map.get("province");
			if(province != null){
				province = province.trim();
			}
			// 香港的是null
			if (province.equals("null")) {
				province = (String) map.get("city");
			}
			String province_name = provinceMap.get(province);
			if (province_name == null) {
				province_name = province;
			}
			String statdate = map.get("statdate").toString();
			BigDecimal num = (BigDecimal) map.get("pnum");
			BigDecimal usernum = (BigDecimal) map.get("pusernum");
			if (num == null) {
				num = new BigDecimal("0");
			}
			if (usernum == null) {
				usernum = new BigDecimal("0");
			}
			tableLine.add(statdate);
			tableColumn.add(province_name);
			rowlst3.put(province_name + '-' + statdate, num.intValue() + "(" + usernum.intValue() + ")");
		}
		request.getSession().setAttribute("rowlst2", rowlst3);
		request.getSession().setAttribute("tableLine", tableLine);
		request.getSession().setAttribute("tableColumn", tableColumn);
		return "/dayarea/commentList";
	}
	
	@RequestMapping(value = "/favList", method = RequestMethod.GET)
	public String favList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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
//		String type = request.getParameter("type");
		String type = "fav";

		if (null != sSource && !"".equals(sSource)) {
			source = Integer.parseInt(sSource);
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("source", source);

		/** 获取app应用列表数据 */
		Map<Integer, String> sourceMap = this.getSource();
		Map<String, String> provinceMap = this.getProvince();
		// provinceMap.get("11");
		List<Map<String, Object>> rowlst = dayAreaService.queryProvinceListLikeType(startDate, endDate, type, source);
		Map<String, String> rowlst3 = new HashMap<String, String>();
		Set<String> tableLine = new LinkedHashSet<String>();
		// Set<String> tableColumn = new HashSet<String>();
		// Set<String> tableColumn = new TreeSet<String>();
		Set<String> tableColumn = new LinkedHashSet<String>();
		for (Map<String, Object> map : rowlst) {

			String province = (String) map.get("province");
			if(province != null){
				province = province.trim();
			}
			// 香港的是null
			if (province.equals("null")) {
				province = (String) map.get("city");
			}
			String province_name = provinceMap.get(province);
			if (province_name == null) {
				province_name = province;
			}
			String statdate = map.get("statdate").toString();
			BigDecimal num = (BigDecimal) map.get("pnum");
			BigDecimal usernum = (BigDecimal) map.get("pusernum");
			if (num == null) {
				num = new BigDecimal("0");
			}
			if (usernum == null) {
				usernum = new BigDecimal("0");
			}
			tableLine.add(statdate);
			tableColumn.add(province_name);
			rowlst3.put(province_name + '-' + statdate, num.intValue() + "(" + usernum.intValue() + ")");
		}
		request.getSession().setAttribute("rowlst2", rowlst3);
		request.getSession().setAttribute("tableLine", tableLine);
		request.getSession().setAttribute("tableColumn", tableColumn);
		return "/dayarea/favList";
	}
	

}
