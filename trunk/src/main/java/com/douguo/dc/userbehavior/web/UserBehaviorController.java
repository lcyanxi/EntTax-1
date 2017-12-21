package com.douguo.dc.userbehavior.web;

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

import com.douguo.dc.userbehavior.service.UserBehaviorService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/user/behavior")
public class UserBehaviorController {
	@Autowired
	private UserBehaviorService userBehaviorService;

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
			qtype = "";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("qtype", qtype);
		model.put("app", app);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst;
		if (null == qtype || "".equals(qtype)) {
			rowlst = userBehaviorService.querySumList(startDate, endDate);
		} else {
			rowlst = userBehaviorService.querySumListByQtype(startDate, endDate, qtype);
		}

		model.put("rowlst", rowlst);
		return "/user_behavior/user_behavior_list";
	}

	@RequestMapping(value = "/keyword/detaillist", method = RequestMethod.GET)
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
		String keyword = request.getParameter("keyword");

		if (null == qtype || "".equals(qtype)) {
			qtype = "";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("keyword", keyword);
		model.put("app", app);
		model.put("qtype", qtype);
		List<Map<String, Object>> rowlst;
		/** 获取app应用列表数据 */
		if(qtype.equals("")){
			rowlst = userBehaviorService.queryKeywordList(startDate, endDate, keyword);
		}else{
			rowlst = userBehaviorService.queryKeywordListByQtype(startDate, endDate, keyword,qtype);
		}
		

		model.put("rowlst", rowlst);
		return "/user_behavior/user_behavior_list_detail";
	}
}