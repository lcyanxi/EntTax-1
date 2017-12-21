package com.douguo.dc.web;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douguo.dc.service.TerminalService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/terminal")
public class TerminalController {
	private TerminalService	terminalService;

	@Autowired
	public void setTerminalService(TerminalService terminalService) {
		this.terminalService = terminalService;
	}

	@RequestMapping(value = "/device", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		/** 设置页面访问新增用户的起始时间 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(System.currentTimeMillis());
		String start_date = DateUtil.getSpecifiedDayBefore(today, 1);
		String end_date = DateUtil.getSpecifiedDayBefore(today, 1);
		start_date = start_date.replaceAll("-", ".");
		end_date = end_date.replaceAll("-", ".");
		model.put("start_date", start_date);
		model.put("end_date", end_date);
		String appId = request.getParameter("appId");
		if (null == appId || appId.equals("")) {
			appId = "1";
		}
		model.put("globalAppid", appId);

		String which = request.getParameter("which");
		if (null == which || which.equals("devices")) {
			return "/terminal/devices";
		}
		if (null != which && which.equals("resolutions")) {
			return "/terminal/resolutions";
		}
		if (null != which && which.equals("system")) {
			return "/terminal/system";
		}
		if (null != which && which.equals("network")) {
			return "/terminal/network";
		}
		if (null != which && which.equals("carriers")) {
			return "/terminal/carriers";
		}
		if (null != which && which.equals("location")) {
			return "/terminal/location";
		}

		return "redirect:/overview.do";
	}

	@RequestMapping(value = "/load_chart_data", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String loadChartData(HttpServletRequest request, HttpServletResponse response) {
		String ret = "";

		String appId = request.getParameter("app_id");
		if (null == appId || appId.equalsIgnoreCase("")) {
			appId = "1";
		}

		String stats = request.getParameter("stats");
		if (null == stats || stats.trim().equals("")) {
			return ret;
		}

		String time_unit = request.getParameter("time_unit");
		if (null == time_unit || time_unit.trim().equals("")) {
			return ret;
		}

		String endDate = request.getParameter("end_date");
		String startDate = request.getParameter("start_date");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String statDate = "";
		if (time_unit.trim().equalsIgnoreCase("daily")) {
			if (null == startDate || null == endDate || startDate.trim().equals("") || endDate.trim().equals("")) {
				String today = sdf.format(System.currentTimeMillis());
				statDate = DateUtil.getSpecifiedDayBefore(today, 1);
			} else {
				statDate = startDate;
			}
			String time_type = "TODAY";
			ret = this.terminalService.getDevicesChartData(appId, statDate, time_type, stats);
		}
		return ret;
	}

	@RequestMapping(value = "/load_table_data", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String loadTableData(HttpServletRequest request, HttpServletResponse response) {
		String ret = "";
		String pageNo = request.getParameter("page");
		int pagesize = 20;
		int page_no = (pageNo == null || pageNo.equals("")) ? 1 : Integer.parseInt(pageNo);
		int startRow = (page_no - 1) * pagesize;

		String appId = request.getParameter("app_id");
		if (null == appId || appId.equalsIgnoreCase("")) {
			appId = "1";
		}

		String stats = request.getParameter("stats");
		if (null == stats || stats.trim().equals("")) {
			return ret;
		}

		String time_unit = request.getParameter("time_unit");
		if (null == time_unit || time_unit.trim().equals("")) {
			return ret;
		}

		String endDate = request.getParameter("end_date");
		String startDate = request.getParameter("start_date");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String statDate = "";
		if (time_unit.trim().equalsIgnoreCase("daily")) {
			if (null == startDate || null == endDate || startDate.trim().equals("") || endDate.trim().equals("")) {
				String today = sdf.format(System.currentTimeMillis());
				statDate = DateUtil.getSpecifiedDayBefore(today, 1);
			} else {
				statDate = startDate;
			}
			String time_type = "TODAY";
			ret = this.terminalService.getDevicesTableData(startRow, pagesize, appId, statDate, time_type, stats);
		}
		return ret;
	}
}