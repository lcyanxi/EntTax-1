package com.douguo.dc.pv.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.douguo.dc.pv.service.PvService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/pv")
public class PvController {
	@Autowired
	private PvService pvService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws JSONException {
		if (null == appId || appId.equals("")) {
			appId = "1";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(System.currentTimeMillis());
		String startDate = request.getParameter("startDate");
		if (null == startDate || startDate.equals("")) {
			startDate = DateUtil.getSpecifiedDayBefore(today, 7);
		}
		String endDate = request.getParameter("endDate");
		if (null == endDate || endDate.equals("")) {
			endDate = DateUtil.getSpecifiedDayBefore(today, 1);
		}

		String type = request.getParameter("type");
		String tab = request.getParameter("tab");

		if (null == tab || "".equals(tab)) {
			tab = "ALL";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("type", type);
		model.put("tab", tab);

		/** 获取type应用列表数据 */
		List<Map<String, Object>> rowlst;
		if (null == type || "".equals(type)) {
			rowlst = pvService.querySumList(startDate, endDate);
		} else {
			 rowlst = pvService.querySumListByType(startDate, endDate, type);
		}

		List<BigDecimal> seriesWap = new ArrayList<BigDecimal>();
		List<BigDecimal> seriesWww = new ArrayList<BigDecimal>();
		List<Date> xAxis = new ArrayList<Date>();
		for (Map<String, Object> map : rowlst) {
			String siteType = (String) map.get("type");
			if (siteType.equals("wap")) {
				seriesWap.add((BigDecimal) map.get("pv"));
			} else if (siteType.equals("www")) {
				seriesWww.add((BigDecimal) map.get("pv"));
			}
			Date date = (Date) map.get("statdate");
			if (!xAxis.contains(date)) {
				xAxis.add(date);
			}
		}

		JSONArray jsonArray = new JSONArray();
		JSONObject obj1 = buildJson("WapPV数", "line", seriesWap);
		JSONObject obj2 = buildJson("WwwPV数", "line", seriesWww);
		jsonArray.put(obj1);
		jsonArray.put(obj2);

		model.put("series", jsonArray.toString());
		model.put("xAxis", buildJson("", "category", xAxis).toString());
		model.put("rowlst", rowlst);
		return "/pv/pv_list";
	}

	public static void main(String[] args) {
		String name = "test";
		String type = "bar";
		List<String> data = new ArrayList<String>();
		data.add("12");
		data.add("15");
		data.add("16");
		data.add("17");
		try {
			JSONObject tmp = PvController.buildJson(name, type, data);
			JSONObject tmp2 = PvController.buildJson(name, type, data);
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(tmp);
			jsonArray.put(tmp2);
			System.out.println(jsonArray.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static JSONObject buildJson(String name, String type, List data) throws JSONException {
		JSONObject jsonObj = new JSONObject();

		if (StringUtils.isNotBlank(name)) {
			jsonObj.put("name", name);
		}
		jsonObj.put("type", type);
		jsonObj.put("data", data);
		return jsonObj;
	}
}