package com.douguo.dc.mall.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import com.douguo.dc.mall.service.MallStoreStatService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/mall/store")
public class MallStoreStatController {
	@Autowired
	private MallStoreStatService mallStoreStatService;

	@RequestMapping(value = "/storeList", method = RequestMethod.GET)
	public String storeList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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

		String plat = request.getParameter("plat");
		String order = request.getParameter("order");

		if (null == plat || "".equals(plat)) {
			plat = "STORE";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("plat", plat);
		model.put("order", order);
		System.out.println("order:" + order);
		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst = mallStoreStatService.queryStoreSumList(startDate, endDate, order);
		BigDecimal moneysSum = new BigDecimal(0);
		//
		for (Map<String, Object> map : rowlst) {
			BigDecimal moneys = (BigDecimal) map.get("moneys");
			moneysSum = moneysSum.add(moneys);
		}
		//
		for (Map<String, Object> map : rowlst) {
			BigDecimal moneys = (BigDecimal) map.get("moneys");
			BigDecimal moneysRate = new BigDecimal(0);

			if (0 != moneysSum.intValue()) {
				moneys = moneys.multiply(new BigDecimal(100));
				moneysRate = moneys.divide(moneysSum, 2, BigDecimal.ROUND_HALF_UP);
			}

			map.put("moneysRate", moneysRate);
		}
		model.put("rowlst", rowlst);
		return "/mall/store/mall_store_list";
	}

	@RequestMapping(value = "/singleStoreList", method = RequestMethod.GET)
	public String singleStoreList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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

		String plat = request.getParameter("plat");
		String order = request.getParameter("order");
		String storeId = request.getParameter("storeId");

		if (null == plat || "".equals(plat)) {
			plat = "STORE";
		}
		
		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("plat", plat);
		model.put("order", order);
		model.put("storeId", storeId);
		System.out.println("order:" + order);
		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst = mallStoreStatService.querySingleStoreSumList(startDate, endDate, storeId, order);

		model.put("rowlst", rowlst);
		return "/mall/store/mall_single_store_list";
	}
}