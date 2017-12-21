package com.douguo.dc.serverlog.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.douguo.dc.serverlog.service.ServerlogService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/slog")
public class ServerlogController {
	@Autowired
	private ServerlogService serverlogService;

	@RequestMapping(value = "/timelist", method = RequestMethod.GET)
	public String recipseList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {
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

		Integer source = 0;
		String sSource = request.getParameter("source");
		String plat = request.getParameter("plat");

		if (null == plat || "".equals(plat)) {
			plat = "API";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("plat", plat);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst;
		List<Map<String, Object>> newrowlst = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> rowSumList = null;
		if (null == plat || "".equals(plat)) {
			rowlst = serverlogService.queryList(startDate, endDate);
		} else {
			rowlst = serverlogService.queryListByPlat(startDate, endDate, plat);
			rowSumList = serverlogService.querySumListByPlat(startDate, endDate, plat);
		}
		
		Map<String,BigDecimal> mapSums = new HashMap<String,BigDecimal>();
		
		for(Map<String,Object> map : rowSumList){
			Date date = (Date)map.get("statdate");
			String strDate = "";
			if(date != null){
				strDate = (String)date.toString();
			}
			
			String strPlat = (String)map.get("plat");
			String strQType = (String)map.get("qtype");
			BigDecimal sums = (BigDecimal)map.get("ntimes");
			String strKey = strDate + "_" + strPlat + "_" + strQType;
			if(!mapSums.containsKey(strKey)){
				mapSums.put(strKey, sums);
			}
		}

		for(Map<String,Object> map : rowlst){
			Date date = (Date)map.get("statdate");
			String strDate = "";
			if(date != null){
				strDate = (String)date.toString();
			}
			String strPlat = (String)map.get("plat");
			String strQType = (String)map.get("qtype");
			BigDecimal times = (BigDecimal)map.get("times");
			String strKey = strDate + "_" + strPlat + "_" + strQType;
			
			BigDecimal newsums = mapSums.get(strKey);
			BigDecimal newtimes = times.multiply(new BigDecimal("100"));
			BigDecimal ratio = newtimes.divide(newsums,1, BigDecimal.ROUND_HALF_UP);
			map.put("ratio",ratio);
		}
		
		model.put("rowlst", rowlst);
		return "/serverlog/serverlogList";
	}
}
