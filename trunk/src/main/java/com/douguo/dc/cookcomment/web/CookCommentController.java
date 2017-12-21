package com.douguo.dc.cookcomment.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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

import com.douguo.dc.cookcomment.service.CookCommentStatService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/cookcomment")
public class CookCommentController {

	@Autowired
	private CookCommentStatService cookCommentStatService;

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
			startDate = DateUtil.getSpecifiedDayBefore(today, 1);
		}
		String endDate = request.getParameter("endDate");
		if (null == endDate || endDate.equals("")) {
			endDate = DateUtil.getSpecifiedDayBefore(today, 1);
		}

		String app = request.getParameter("app");
		String tab = request.getParameter("tab");

		if (null == app || "".equals(app)) {
			app = "3";
		}

		if (null == tab || "".equals(tab)) {
			tab = "CHANNEL";
		}

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst = cookCommentStatService.queryList(startDate, endDate, "comments");
		List<Map<String, Object>> rowlstNew = new ArrayList<Map<String, Object>>();
		// ---- 重构数据 begin -------
		Map<String, Map<String, Object>> newMap = new TreeMap<String, Map<String, Object>>();
		for (Map<String, Object> map : rowlst) {
			//
			// String strType = (String) map.get("type");
			// strType = getSourceName(strSource);
			Date date = (Date) map.get("statdate");
			String type = (String) map.get("type");
			Integer bdComments = (Integer) map.get("comments");

			String newKey = DateUtil.date2Str(date, "yyyy-MM-dd");
			if (newMap.containsKey(newKey)) {
				Map<String, Object> tMap = newMap.get(newKey);
				tMap.put(type, bdComments);
				//
				newMap.put(newKey, tMap);
			} else {
				Map<String, Object> tMap = new HashMap<String, Object>();
				tMap.put("statdate", date);
				// tMap.put("type", strSource);
				tMap.put(type, bdComments);
				//
				newMap.put(newKey, tMap);
			}
		}

		// map to list
		for (Entry<String, Map<String, Object>> entry : newMap.entrySet()) {
			Map<String, Object> mp = entry.getValue();
			Integer nTotal = (Integer) mp.get("total");
			Integer nPass = (Integer) mp.get("pass");
			Integer nGarbage = (Integer) mp.get("garbage");
			Integer nDayu = (Integer) mp.get("dayu");
			if (nTotal == null) {
				nTotal = new Integer(0);
			}
			//
			if (nPass == null) {
				nPass = new Integer(0);
			}
			//
			if (nGarbage == null) {
				nGarbage = new Integer(0);
			}
			//
			if (nDayu == null) {
				nDayu = new Integer(0);
			}
			//
			BigDecimal bdTotal = new BigDecimal(nTotal);
			BigDecimal bdDayu = new BigDecimal(nDayu * 100);
			//
			BigDecimal bdPass = new BigDecimal(nPass * 100);
			BigDecimal bdGarbage = new BigDecimal(nGarbage * 100);
			//
			BigDecimal bdPassRate = new BigDecimal(0);
			BigDecimal bdGarbageRate = new BigDecimal(0);
			BigDecimal bdDayuRate = new BigDecimal(0);

			if (nTotal > 0) {
				bdPassRate = bdPass.divide(bdTotal, 2, BigDecimal.ROUND_HALF_UP);
			}
			//
			if (nPass > 0) {
				bdGarbageRate = bdGarbage.divide(new BigDecimal(nPass), 2, BigDecimal.ROUND_HALF_UP);
			}
			//
			if (nDayu > 0) {
				if(nGarbage==0){
					bdDayuRate = new BigDecimal(0) ;
				} else {
					bdDayuRate = bdDayu.divide(new BigDecimal(nGarbage), 2, BigDecimal.ROUND_HALF_UP);
				}
			}
			

			mp.put("passRate", bdPassRate);
			mp.put("garbageRate", bdGarbageRate);
			mp.put("dayuRate", bdDayuRate);

			rowlstNew.add(mp);
		}

		// ------- 重构数据 end -------

		// ----构造图表数据 begin -------
		List<String> xAxis = new ArrayList<String>();
		Map<String, List<BigDecimal>> mapChart = new HashMap<String, List<BigDecimal>>();

		Map<String, BigDecimal> mapTmp = new HashMap<String, BigDecimal>();
		List<String> listType = new ArrayList<String>();
		//
		// listSource.add("1");
		// listSource.add("3");
		// listSource.add("4");

		//
		for (Map<String, Object> map : rowlstNew) {
//			String strType = (String) map.get("type");
			String strType = "dayuRate";
			Date date = (Date) map.get("statdate");
			String strDate = DateUtil.date2Str(date, "yyyy-MM-dd");
			BigDecimal bdRs = (BigDecimal) map.get("dayuRate");
			//
			mapTmp.put(strDate, bdRs);
			if (!listType.contains(strType) && listType.size() < 30) {
				listType.add(strType);
			}
			//
			if (!xAxis.contains(strDate)) {
				xAxis.add(strDate);
			}
		}
		//
		for (String souc : listType) {
			//
			for (String dDate : xAxis) {
				
				BigDecimal bdVal = mapTmp.get(dDate);
				if (bdVal == null) {
					bdVal = new BigDecimal(0);
				}
				List<BigDecimal> list;
				if (mapChart.containsKey(souc)) {
					list = mapChart.get(souc);
				} else {
					list = new ArrayList<BigDecimal>();
				}
				list.add(bdVal);
				mapChart.put(souc, list);
			}
		}

		//
		JSONArray jsonArray = new JSONArray();
		for (Entry<String, List<BigDecimal>> entry : mapChart.entrySet()) {
			String key = entry.getKey();
			List<BigDecimal> value = entry.getValue();
			JSONObject obj1 = buildJson(key, "line", value);
			jsonArray.put(obj1);
		}
		JSONObject selectJson = new JSONObject();
		for (int i = 3; i < listType.size(); i++) {
			selectJson.put(listType.get(i), false);
		}

		JSONObject legendJson = new JSONObject();
		legendJson.put("data", listType);
		legendJson.put("orient", "vertical");
		legendJson.put("selected", selectJson);
		legendJson.put("x", "left");
		legendJson.put("y", "top");
		model.put("legend", legendJson.toString());
		model.put("series", jsonArray.toString());
		model.put("xAxis", buildJson("", "category", xAxis).toString());
		// ----构造图表数据 end -------
		//
		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("app", app);
		model.put("tab", tab);

		model.put("rowlst", rowlstNew);
		return "/cookcomment/cook_comment_list";
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

	private String getSourceName(String sourceId) {
		if (sourceId.equals("1")) {
			return "网站";
		} else if (sourceId.equals("3")) {
			return "IOS";
		} else if (sourceId.equals("4")) {
			return "Android";
		} else {
			return "Other";
		}
	}
}