package com.douguo.dc.util;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {

	public static String getTableJson(List stats, int total, String result) {
		Map<String, Object> map0 = new HashMap<String, Object>();
		map0.put("stats", stats);
		map0.put("total", total);
		map0.put("result", result);
		return JSON.toJSONString(map0);
	}

	public static String getChartJson(List dates, List stats, Boolean isCompared, String result,int total) {
		Map<String, Object> map0 = new HashMap<String, Object>();
		if (dates != null)
		    map0.put("dates", dates);
		if (stats != null)
		    map0.put("stats", stats);
		if (isCompared != null)
			map0.put("is_compared", isCompared);
		if(total>-1)
		    map0.put("total", total);
		map0.put("result", result);
		return JSON.toJSONString(map0);
	}
	
	public static String getListJson(List stats, int total, String result) {
		Map<String, Object> map0 = new HashMap<String, Object>();
		map0.put("datas", stats);
		map0.put("total", total);
		map0.put("result", result);
		return JSON.toJSONString(map0);
	}

	/**
	 * jsonz字符串转json对象
	 * @param desc
	 * @return com.alibaba.fastjson.JSONObject
	 * @author zhangjianfei
	 * @data 2017-04-29
     */
	public static com.alibaba.fastjson.JSONObject parseStrToJsonObj (String desc){
		com.alibaba.fastjson.JSONObject descJsonObj = JSON.parseObject(desc);
		return descJsonObj ;
	}
//
//	public static void main(String[] args) {
//		String desc = "{\"type\":\"logmail\",\"title\":'aaaa'}" ;
//		System.out.println(JsonUtil.parseStrToJsonObj(desc).get("ndays"));
//	}
}
