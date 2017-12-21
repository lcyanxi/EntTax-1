package com.douguo.dc.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douguo.dc.service.ErrorAnalysisService;
import com.douguo.dc.util.StatKey;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/erroranalysis")
public class ErrorAnalysisController {
	private ErrorAnalysisService	errorAnalysisService;

	@Autowired
	public void setErrorAnalysisService(ErrorAnalysisService errorAnalysisService) {
		this.errorAnalysisService = errorAnalysisService;
	}

	@RequestMapping(value = "/errors", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		/** 设置页面访问的起始时间 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(System.currentTimeMillis());
		String start_date = DateUtil.getSpecifiedDayBefore(today, 6);
		String end_date = today;
		start_date = start_date.replaceAll("-", ".");
		end_date = end_date.replaceAll("-", ".");
		model.put("start_date", start_date);
		model.put("end_date", end_date);
		String appId = request.getParameter("appId");
		if (null == appId || appId.equals("")) {
			appId = "1";
		}
		model.put("globalAppid", appId);
		return "/erroranalysis/errors";
	}

	@RequestMapping(value = "/load_data", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String loadChartData(HttpServletRequest request, HttpServletResponse response) {
		String appId = request.getParameter("app_id");
		if (null == appId || appId.equalsIgnoreCase("")) {
			appId = "1";
		}

		String stats = request.getParameter("stats");
		if (null == stats || stats.trim().equals("")) {
			return null;
		}

		String startDate = request.getParameter("start_date");
		String endDate = request.getParameter("end_date");
		if (null == startDate || null == endDate || startDate.trim().equals("") || endDate.trim().equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String today = sdf.format(System.currentTimeMillis());
			startDate = DateUtil.getSpecifiedDayBefore(today, 7);
			endDate = DateUtil.getSpecifiedDayBefore(today, 1);
		} else {
			startDate = DateUtil.getSpecifiedDayBefore(startDate, 1);
			endDate = DateUtil.getSpecifiedDayBefore(endDate, 1);
		}
		
		String versions = request.getParameter("versions[]");
		if (null != versions && !versions.equals("")) {
			try {
				versions = URLDecoder.decode(versions, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				versions = "";
				e.printStackTrace();
			}
		}
		String dimension_name = versions;
		String dimension_type = "APP_ERROR";
		String time_type = "TODAY";
		String stat_key_id = StatKey.FailureCount;

		return this.errorAnalysisService.getData(appId, startDate, endDate, time_type, dimension_type, dimension_name, stat_key_id, stats);
	}
}