package com.douguo.dc.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.douguo.dc.service.UserEngagementService;
import com.douguo.dc.util.StatKey;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/userengagement")
public class UserEngagementController {
	private UserEngagementService	userEngagementService;

	@Autowired
	public void setUserEngagementService(UserEngagementService userEngagementService) {
		this.userEngagementService = userEngagementService;
	}

	@RequestMapping(value = "/nav/{menu}")
	public String duration(@PathVariable String menu, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		String yestoday = DateUtil.getSpecifiedDayBefore(today, 1);
		model.put("start_date", yestoday.replaceAll("-", "."));
		model.put("end_date", yestoday.replaceAll("-", "."));

		String appId = request.getParameter("appId");
		if (null == appId || appId.equals("")) {
			appId = "1";
		}
		model.put("globalAppid", appId);
		if (null == menu)
			return null;
		if (menu.equals("duration")) {
			return "/userengagement/sessionLength";
		} else if (menu.equals("frequency")) {
			return "/userengagement/frequency";
		}
		return "redirect:/overview.do";
	}

	@RequestMapping(value = "/data/{data_tpye}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String loadChartData(@PathVariable String data_tpye, HttpServletRequest request, HttpServletResponse response) {
		String ret = new String("");
		if (null == data_tpye)
			return ret;

		String appId = request.getParameter("app_id");
		if (null == appId || appId.equalsIgnoreCase("")) {
			appId = "1";
		}

		String is_compared = request.getParameter("is_compared");
		if (null == is_compared || is_compared.trim().equalsIgnoreCase("")) {
			is_compared = "false";
		}
		String original_data_count = request.getParameter("original_data_count");
		if (null == original_data_count || original_data_count.trim().equalsIgnoreCase("")) {
			original_data_count = "0";
		}

		String stats = request.getParameter("stats");
		String time_unit = request.getParameter("time_unit");
		if (null == time_unit || time_unit.trim().equals("")) {
			return ret;
		}

		if (null == stats || stats.trim().equals("")) {
			return ret;
		}

		String startDate = request.getParameter("start_date");
		String endDate = request.getParameter("end_date");
		if (!startDate.equals(endDate)) {
			return ret;
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

		String channels = request.getParameter("channels[]");
		if (null != channels && !channels.equals("")) {
			try {
				channels = URLDecoder.decode(channels, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				channels = "";
				e.printStackTrace();
			}
		}

		String dimension_type = "";
		String dimension_name = "";
		if ((null != channels && null != versions) && (!channels.trim().equals("") && !versions.trim().equals(""))) {
			dimension_type = "VERSION_CHANNELID";
			dimension_name = versions + "_" + channels;
		} else if ((null == channels || channels.trim().equals("")) && (null != versions && !versions.trim().equals(""))) {
			dimension_type = "VERSION";
			dimension_name = versions;
		} else if ((null == versions || versions.trim().equals("")) && (null != channels && !channels.trim().equals(""))) {
			dimension_type = "CHANNELID";
			dimension_name = channels;
		} else {
			dimension_type = "ALL";
			dimension_name = "";
		}

		if (data_tpye.equals("load_chart_data")) {
			if (stats.equals("duration")) {
				ret = this.userEngagementService.getSessionLengthChart(time_unit, appId, startDate, dimension_type, dimension_name, StatKey.SessionLengthJson, is_compared);
			} else if (stats.equals("frequency")) {
				ret = this.userEngagementService.getSessionDailyChart(time_unit, appId, startDate, dimension_type, dimension_name, StatKey.SessionJson, is_compared);
			}
		} else if (data_tpye.equals("load_table_data")) {
			if (stats.equals("duration")) {
				ret = this.userEngagementService.getSessionLengthTable(time_unit, appId, startDate, dimension_type, dimension_name, StatKey.SessionLengthJson);
			} else if (stats.equals("frequency")) {
				ret = this.userEngagementService.getSessionDailyTable(time_unit, appId, startDate, dimension_type, dimension_name, StatKey.SessionJson, is_compared);
			}
		}

		return ret;
	}
}
