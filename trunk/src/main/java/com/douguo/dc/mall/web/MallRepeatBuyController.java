package com.douguo.dc.mall.web;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.douguo.dc.mall.common.MallConstants;
import com.douguo.dc.mall.service.MallRepeatBuyService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/mall/repeatbuy")
public class MallRepeatBuyController {
	@Autowired
	private MallRepeatBuyService mallRepeatBuyService;

	@RequestMapping(value = "/day30list", method = RequestMethod.GET)
	public String day30list(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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
		String statType = request.getParameter("statType");
		String repeatType = request.getParameter("repeatType");

		if(StringUtils.isBlank(repeatType)){
			repeatType = "";
		}
		
		if (null == statType || "".equals(statType)) {
			statType = MallConstants.MALL_REPEAT_BUY_COMMON;
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("statType", statType);
		model.put("repeatType", repeatType);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst;
		List<Map<String, Object>> rowSumList = new ArrayList<Map<String,Object>>();
		rowlst = mallRepeatBuyService.queryListByStatType(startDate, endDate, statType);

		//BigDecimal newsums = new BigDecimal(0);
		Map<Date, BigDecimal> mapSum = new HashMap<Date,BigDecimal>();
		for (Map<String, Object> map : rowlst) {
			Date statDate = (Date) map.get("statdate");
			String rType = (String) map.get("repeat_type");
			if ("0".equals(rType)) {
				BigDecimal newsums = (BigDecimal) map.get("repeat_value");
				mapSum.put(statDate,newsums);
			}
		}

		for (Map<String, Object> map : rowlst) {
			Date statDate = (Date) map.get("statdate");
			String rType = (String) map.get("repeat_type");
			BigDecimal rValue = (BigDecimal) map.get("repeat_value");
			BigDecimal nValue = rValue.multiply(new BigDecimal("100"));
			//
			BigDecimal ratio = new BigDecimal(0);
			BigDecimal curTotal = mapSum.get(statDate);
			if(curTotal == null) {
				curTotal = new BigDecimal(0);
			}
			if(curTotal.intValue() != 0){
				ratio = nValue.divide(curTotal, 1, BigDecimal.ROUND_HALF_UP);
			}
			
			map.put("curTotal", curTotal);
			map.put("ratio", ratio);
			//
			if(StringUtils.isBlank(repeatType)){
				if(!rType.equals("0")){
					rowSumList.add(map);
				}
			}else {
				if(rType.equals(repeatType)){
					rowSumList.add(map);
				}
			}
		}

		model.put("rowlst", rowSumList);

		return "/mall/repeat_buy/mall_30day_repeat_buy_list";
	}
}