package com.douguo.dc.web;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douguo.dc.model.AppModel;
import com.douguo.dc.model.Pager;
import com.douguo.dc.service.OverviewService;
import com.douguo.dc.util.DateUtil;

@Controller
public class OverviewController {

	@Autowired
	private OverviewService OverviewService;

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@RequestMapping(value = "/overview", method = RequestMethod.GET)
	public String Overview(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		if (null == appId || appId.equals("")) {
			appId = "1";
		}
		model.put("globalAppid", appId);
		model.put("globalAppid", appId);
		/** 获取app应用列表数据 */
		Pager<AppModel> apps = this.OverviewService.getApps("NORMAL");
		request.getSession().setAttribute("apps", apps);
		return "/overview/overview";
	}

	@RequestMapping(value = "/json/reports/load_chart_data", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String loadChartData(HttpServletRequest request, HttpServletResponse response) {
		String stats = request.getParameter("stats");
		String appId = request.getParameter("app_id");
		if (null == appId || appId.equalsIgnoreCase("")) {
			appId = "1";
		}
		String ret = "";
		String yesterday = sdf.format(System.currentTimeMillis() - 86400000);
		String TwoDaysAgo = sdf.format(System.currentTimeMillis() - 172800000);
		if (stats.contains("hours")) {
			String date = request.getParameter("date");
			if (null == date || date.equalsIgnoreCase("")) {
				date = sdf.format(System.currentTimeMillis());
			}
			if ("index_hours_active".equalsIgnoreCase(stats)) {
				ret = OverviewService.getActive(appId, date);
			} else if ("index_hours_launches".equalsIgnoreCase(stats)) {
				ret = OverviewService.getLaunches(appId, date);
			}

		} else {
			String startDate = request.getParameter("start_date");
			String endDate = request.getParameter("end_date");
			if (null == startDate || null == endDate || startDate.equalsIgnoreCase("") || endDate.equalsIgnoreCase("")) {
				startDate = DateUtil.getSpecifiedDayBefore(yesterday, 6);
				endDate = yesterday;
			} else {
				startDate = DateUtil.getSpecifiedDayBefore(startDate, 1);
				endDate = DateUtil.getSpecifiedDayBefore(endDate, 1);
			}

			if (stats.equalsIgnoreCase("index_installation")) {
				ret = OverviewService.getInstallation(appId, startDate, endDate);
			} else if (stats.equalsIgnoreCase("index_accumulation")) {
				ret = OverviewService.getAccumulation(appId, startDate, endDate);
			} else if (stats.equalsIgnoreCase("index_activeUser")) {
				ret = OverviewService.getActiveUser(appId, startDate, endDate);
			} else if (stats.equalsIgnoreCase("index_installationRate")) {
				try {
					ret = OverviewService.getInstallationRate(appId, startDate, endDate);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (stats.equalsIgnoreCase("index_launch")) {
				ret = OverviewService.getLaunch(appId, startDate, endDate);
			} else if (stats.equalsIgnoreCase("index_avgDuration")) {
				ret = OverviewService.getAvgDuration(appId, startDate, endDate);
			} else if (stats.equalsIgnoreCase("index_versions")) {
				String metric = request.getParameter("metric");
				ret = OverviewService.getTopByDim(appId, "VERSION", metric, TwoDaysAgo, yesterday);
			} else if (stats.equalsIgnoreCase("index_channels")) {
				String metric = request.getParameter("metric");
				ret = OverviewService.getTopByDim(appId, "CHANNEL", metric, TwoDaysAgo, yesterday);
			}
		}
		response.setHeader("Content-Type", "application/json; charset=utf-8");
		return ret;
	}

	@RequestMapping(value = "/json/reports/load_table_data", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String loadTableData(HttpServletRequest request, HttpServletResponse response) {
		String stats = request.getParameter("stats");
		String appId = request.getParameter("app_id");
		if (null == appId || appId.equalsIgnoreCase("")) {
			appId = "1";
		}
		String ret = "";
		if (stats.equalsIgnoreCase("index_today")) {
			ret = OverviewService.getIndexToday(appId);
		} else if (stats.equalsIgnoreCase("index_details")) {
			String today = sdf.format(System.currentTimeMillis());
			String startDate = request.getParameter("start_date");
			String endDate = request.getParameter("end_date");
			if (null == startDate || null == endDate || startDate.equalsIgnoreCase("") || endDate.equalsIgnoreCase("")) {
				startDate = DateUtil.getSpecifiedDayBefore(today, 7);
				endDate = DateUtil.getSpecifiedDayBefore(today, 1);
			} else {
				startDate = DateUtil.getSpecifiedDayBefore(startDate, 1);
				endDate = DateUtil.getSpecifiedDayBefore(endDate, 1);
			}
			ret = OverviewService.getTrendDetails(appId, startDate, endDate);
		} else if (stats.equalsIgnoreCase("index_scale")) {
			String date = sdf.format(System.currentTimeMillis());
			ret = OverviewService.getScale(appId, date);
		} else if (stats.equalsIgnoreCase("index_summary")) {
			String date = sdf.format(System.currentTimeMillis());
			ret = OverviewService.getSummary(appId, date);
		}
		response.setHeader("Content-Type", "application/json; charset=utf-8");
		return ret;

	}

}
