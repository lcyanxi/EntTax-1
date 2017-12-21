package com.douguo.dc.cookcollect.web;

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

import com.douguo.dc.cookcollect.service.CookCollectService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/cookcollect")
public class CookCollectController {
	@Autowired
	private CookCollectService cookCollectService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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

		String qtype = request.getParameter("qtype");
		String app = request.getParameter("app");

		if (null == qtype || "".equals(qtype)) {
			qtype = "view_cookcollect_detail";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("qtype", qtype);
		model.put("app", app);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst = cookCollectService.querySumList(startDate, endDate);

		model.put("rowlst", rowlst);
		return "/cookcollect/cookcollect_list";
	}

	@RequestMapping(value = "/detaillist", method = RequestMethod.GET)
	public String detailList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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

		String app = request.getParameter("app");
		String qtype = request.getParameter("qtype");
		String cookName = request.getParameter("cookName");

		if (null == qtype || "".equals(qtype)) {
			qtype = "";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("cookName", cookName);
		model.put("app", app);
		model.put("qtype", qtype);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst = cookCollectService.queryCookNameList(startDate, endDate, cookName);

		model.put("rowlst", rowlst);
		return "/cookcollect/cookcollect_list_detail";
	}
}