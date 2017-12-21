package com.douguo.dc.cook.web;

import java.text.ParseException;
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

import com.douguo.dc.cook.service.CookSumStatService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/cookSum")
public class CookSumStatController {
	
	@Autowired
	private CookSumStatService cookSumStatService;
	
	@RequestMapping(value = "/cookSumStat")
	public String showCookSum( HttpServletRequest request, HttpServletResponse response, ModelMap model) throws JSONException, ParseException  {
		
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
		
		List<Map<String, Object>> rowlist = cookSumStatService.queryCookSumStatList(startDate, endDate) ;
		
		model.put("startDate", startDate);
        model.put("endDate", endDate);
        
        List<Integer> seriesCooks = new ArrayList<Integer>();
        List<Integer> seriesCook_users = new ArrayList<Integer>();
        List<Integer> seriesFirst_cooks = new ArrayList<Integer>();
        List<Integer> seriesFirst_cook_users = new ArrayList<Integer>();
        List<Integer> seriesCook_comments = new ArrayList<Integer>();
        List<Integer> seriesCook_comment_users = new ArrayList<Integer>();
        List<Integer> seriesCook_comment_replys = new ArrayList<Integer>();
        List<Integer> seriesCook_comment_reply_users = new ArrayList<Integer>();
        List<Integer> seriesFirst_cook_comment_replys = new ArrayList<Integer>();
        List<Integer> seriesFirst_cook_comment_reply_users = new ArrayList<Integer>();
        List<Date> seriesStatdate = new ArrayList<Date>();
        List<Date> xAxis = new ArrayList<Date>();
        
        for (Map<String, Object> map : rowlist) {
        	seriesCooks.add((Integer) map.get("cooks"));
        	seriesCook_users.add((Integer) map.get("cook_users"));
        	seriesFirst_cooks.add((Integer) map.get("first_cooks"));
        	seriesFirst_cook_users.add((Integer) map.get("first_cook_users"));
        	seriesCook_comments.add((Integer) map.get("cook_comments"));
        	seriesCook_comment_users.add((Integer) map.get("cook_comment_users"));
        	seriesCook_comment_replys.add((Integer) map.get("cook_comment_replys"));
        	seriesCook_comment_reply_users.add((Integer) map.get("cook_comment_reply_users"));
        	seriesFirst_cook_comment_replys.add((Integer) map.get("first_cook_comment_replys"));
        	seriesFirst_cook_comment_reply_users.add((Integer) map.get("first_cook_comment_reply_users"));
        	seriesStatdate.add((Date) map.get("statdate"));
        	
            Date date =  (Date) map.get("statdate");
            if (!xAxis.contains(date)) {
                xAxis.add(date);
            }
        }

        JSONArray jsonArray = new JSONArray();
        JSONObject objCooks = buildJson("菜谱数", "line", seriesCooks);
        JSONObject objCook_users = buildJson("菜谱人数", "line", seriesCook_users);
        JSONObject objFirst_cooks = buildJson("首次上传菜谱数", "line", seriesFirst_cooks);
        JSONObject objFirst_cook_users = buildJson("首次上传菜谱人数", "line", seriesFirst_cook_users);
        JSONObject objCook_comments = buildJson("菜谱评论数", "line", seriesCook_comments);
        JSONObject objCook_comment_users = buildJson("菜谱评论人数", "line", seriesCook_comment_users);
        JSONObject objCook_comment_replys = buildJson("菜谱评论回复数", "line", seriesCook_comment_replys);
        JSONObject objCook_comment_reply_users = buildJson("菜谱评论回复人数", "line", seriesCook_comment_reply_users);
        JSONObject objFirst_cook_comment_replys = buildJson("新用户菜谱评论回复数", "line", seriesFirst_cook_comment_replys);
        JSONObject objFirst_cook_comment_reply_users = buildJson("新用户菜谱评论回复人数", "line", seriesFirst_cook_comment_reply_users);
        
        jsonArray.put(objCooks);
        jsonArray.put(objCook_users);
        jsonArray.put(objFirst_cooks);
        jsonArray.put(objFirst_cook_users);
        jsonArray.put(objCook_comments);
        jsonArray.put(objCook_comment_users);
        jsonArray.put(objCook_comment_replys);
        jsonArray.put(objCook_comment_reply_users);
        jsonArray.put(objFirst_cook_comment_replys);
        jsonArray.put(objFirst_cook_comment_reply_users);

        model.put("series", jsonArray.toString());        
        model.put("xAxis", buildJson("", "category", xAxis).toString());
        model.put("rowlst", rowlist);
        
		return "/cook/cookSumStat" ;
	}
	
	private static JSONObject buildJson(String name, String type, List<?> data) throws JSONException {
        JSONObject jsonObj = new JSONObject();

        if (StringUtils.isNotBlank(name)) {
            jsonObj.put("name", name);
        }
        jsonObj.put("type", type);
        jsonObj.put("data", data);
        return jsonObj;
    }
}