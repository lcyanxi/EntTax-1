package com.douguo.dc.mall.web;

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

import com.douguo.dc.mall.service.MallSourceStatService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/mall/source")
public class MallSourceController {
	
	@Autowired
	private MallSourceStatService mallSourceStatService;

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
		String channel = request.getParameter("channel");
		String vers = request.getParameter("vers");

		if (null == app || "".equals(app)) {
			app = "3";
		}

		if (null == tab || "".equals(tab)) {
			tab = "CHANNEL";
		}

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst = mallSourceStatService.queryMallSourceList(startDate, endDate, "source");
		List<Map<String, Object>> rowlstNew = new ArrayList<Map<String, Object>>();  
		// ---- 重构数据 begin -------
		Map<String,Map<String,Object>> newMap = new TreeMap<String,Map<String,Object>>();
		for (Map<String, Object> map : rowlst) {
			//
			String strSource = (String) map.get("source");
			strSource = getSourceName(strSource);
			Date date = (Date) map.get("statdate");
			String qtype = (String) map.get("qtype");
			Integer bdPv = (Integer) map.get("pv");
			
			String newKey = date + strSource ; 
			if(newMap.containsKey(newKey)){
				Map<String, Object> tMap = newMap.get(newKey);
				tMap.put(qtype, bdPv);
				//
				newMap.put(newKey, tMap);
			}else{
				Map<String, Object> tMap = new HashMap<String,Object>();
				tMap.put("statdate", date);
				tMap.put("source", strSource);
				tMap.put(qtype, bdPv);
				//
				newMap.put(newKey, tMap);
			}
		}
		
		//map to list
		for(Entry<String, Map<String,Object>> entry : newMap.entrySet()){
			Map<String,Object> mp = entry.getValue();
			Integer nDetail = (Integer)mp.get("view_tuan_detail");
			Integer nOrders = (Integer)mp.get("orders");
			Integer nPays = (Integer)mp.get("pays");
			if(nDetail == null){
				nDetail = new Integer(0);
			}
			//
			if(nOrders == null){
				nOrders = new Integer(0);
			}
			//
			if(nPays == null){
				nPays = new Integer(0);
			}
			//
			BigDecimal bdDetails = new BigDecimal(nDetail);
			BigDecimal bdOrders = new BigDecimal(nOrders * 100);
			BigDecimal bdPays = new BigDecimal(nPays * 100);
			//
			BigDecimal dbOrderRate = new BigDecimal(0);
			BigDecimal dbPayRate = new BigDecimal(0);
			
			if(nDetail > 0){
				dbOrderRate = bdOrders.divide(bdDetails,2, BigDecimal.ROUND_HALF_UP);
			}
			if(nOrders > 0){
				dbPayRate = bdPays.divide(new BigDecimal(nOrders), BigDecimal.ROUND_HALF_UP);
			}
			
			mp.put("orderRate", dbOrderRate);
			mp.put("payRate", dbPayRate);
			
			rowlstNew.add(mp);
		}
		
		// ------- 重构数据 end -------
		
		
		// ----构造图表数据 begin -------
		List<Date> xAxis = new ArrayList<Date>();
		Map<String, List<BigDecimal>> mapChart = new HashMap<String, List<BigDecimal>>();

		Map<String, BigDecimal> mapTmp = new HashMap<String, BigDecimal>();
		List<String> listSource = new ArrayList<String>();
		//
//		listSource.add("1");
//		listSource.add("3");
//		listSource.add("4");
		
		//
		for (Map<String, Object> map : rowlstNew) {
			String strSource = (String) map.get("source");
			Date date = (Date) map.get("statdate");
			BigDecimal bdRs = (BigDecimal) map.get("payRate");

			mapTmp.put(strSource + date, bdRs);
			if (!listSource.contains(strSource) && listSource.size() < 30) {
				listSource.add(strSource);
			}

			if (!xAxis.contains(date)) {
				xAxis.add(date);
			}
		}

		for (String souc : listSource) {
			//
			for (Date dDate : xAxis) {
				BigDecimal bdVal = mapTmp.get(souc + dDate);
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
		for (int i = 4; i < listSource.size(); i++) {
			selectJson.put(listSource.get(i), false);
		}

		JSONObject legendJson = new JSONObject();
		legendJson.put("data", listSource);
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
		return "/mall/source/mall_source_list";
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
	
	private String getSourceName(String sourceId){
		if(sourceId.equals("0")){
			return "网站";
		}else if(sourceId.equals("815")){
			return "WAP";
		}else if(sourceId.equals("3")){
			return "IOS";
		}else if(sourceId.equals("4")){
			return "Android";
		}else{
			return sourceId;
		}
	}
}