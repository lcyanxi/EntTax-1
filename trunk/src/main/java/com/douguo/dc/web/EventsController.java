package com.douguo.dc.web;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.douguo.dc.service.EventsService;
import com.douguo.dc.util.JsonUtil;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/events")
public class EventsController {

	@Autowired
	private EventsService eventsService;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 事件列表页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param appId
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/dashboard")
	public String dashboard(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			@RequestParam(value = "appId", required = false, defaultValue = "4") String appId,
			@RequestParam(value = "version", required = false, defaultValue = "") String version) {
		if (null == appId || appId.equals("")) {
			appId = "4";
		}
		String verName = "";
		if ("".equals(version)) {
			verName = "全部版本";
		} else {
			verName = version;
		}
		String today = sdf.format(System.currentTimeMillis());
		String yesterday = DateUtil.getSpecifiedDayBefore(today, 1);
		model.put("globalAppid", appId);
		model.put("version", version);
		model.put("verName", verName);
		model.put("endDate", yesterday);

		return "/events/dashboard";
	}

	/**
	 * 获取事件列表 ajax请求
	 * 
	 * @param request
	 * @param response
	 * @param appId
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/dashboard/load_table_data", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String dashboardLoadTable(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "appId", required = false, defaultValue = "4") String appId,
			@RequestParam(value = "version", required = false, defaultValue = "") String version,
			@RequestParam(value = "stat_date", required = false, defaultValue = "") String statDate) {

		if (null == appId || appId.equals("")) {
			appId = "4";
		}
		List<Map<String, Object>> list = eventsService.getEventsListByDay(appId, version, statDate);
		return JsonUtil.getTableJson(list, list.size(), "success");
	}

	/**
	 * 获取版本下拉菜单 ajax请求
	 * 
	 * @param request
	 * @param response
	 * @param appId
	 * @return
	 */
	@RequestMapping(value = "/load_versions", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String loadVersion(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "appId", required = false, defaultValue = "4") String appId) {
		if (null == appId || appId.equals("")) {
			appId = "4";
		}
		List<Map<String, Object>> list = eventsService.queryVersions(appId);
		return JsonUtil.getListJson(list, list.size(), "success");
	}

	/**
	 * 获取事件下拉菜单 ajax请求
	 * 
	 * @param request
	 * @param response
	 * @param appId
	 * @return
	 */
	@RequestMapping(value = "/load_event_groups", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String loadEvents(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "appId", required = false, defaultValue = "4") String appId) {
		if (null == appId || appId.equals("")) {
			appId = "4";
		}
		List<Map<String, Object>> list = eventsService.queryEvents(appId);
		return JsonUtil.getListJson(list, list.size(), "success");
	}

	/**
	 * 事件趋势页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param appId
	 * @param id
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			@RequestParam(value = "appId", required = false, defaultValue = "4") String appId,
			@RequestParam(value = "id", required = false, defaultValue = "") String id,
			@RequestParam(value = "version", required = false, defaultValue = "") String version) {
		if (null == id || id.equals("")) {
			return "redirect:/events/dashboard.do?appId=" + appId;
		}
		if (null == appId || appId.equals("")) {
			appId = "4";
		}
		model.put("globalAppid", appId);
		model.put("version", version);
		model.put("id", id);

		String eventName = eventsService.getNameById(appId, id);
		String today = sdf.format(System.currentTimeMillis());
		String endDate = DateUtil.getSpecifiedDayBefore(today, 1);
		String startDate = DateUtil.getSpecifiedDayBefore(today, 7);
		model.put("eventName", id + "(" + eventName + ")");
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		return "/events/detail";
	}

	/**
	 * 事件趋势图
	 * 
	 * @param request
	 * @param response
	 * @param appId
	 * @param id
	 * @param version
	 * @param stat
	 * @param end_date
	 * @param start_date
	 * @return
	 */
	@RequestMapping(value = "/detail/load_table_data", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String dashDetailTable(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "appId", required = false, defaultValue = "4") String appId,
			@RequestParam(value = "id", required = false, defaultValue = "") String id,
			@RequestParam(value = "version", required = false, defaultValue = "") String version,
			@RequestParam(value = "end_date", required = false, defaultValue = "") String endDate,
			@RequestParam(value = "start_date", required = false, defaultValue = "") String startDate) {
		if (null == appId || appId.equals("")) {
			appId = "4";
		}
		return eventsService.getEventsTable(appId, id, version, startDate, endDate);
	}

	/**
	 * 事件趋势表
	 * @param request
	 * @param response
	 * @param appId
	 * @param id
	 * @param version
	 * @param stat
	 * @param endDate
	 * @param startDate
	 * @return
	 */
	@RequestMapping(value = "/detail/load_chart_data", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String dashDetailChart(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "appId", required = false, defaultValue = "4") String appId,
			@RequestParam(value = "id", required = false, defaultValue = "") String id,
			@RequestParam(value = "version", required = false, defaultValue = "") String version,
			@RequestParam(value = "stat", required = false, defaultValue = "") String stat,
			@RequestParam(value = "end_date", required = false, defaultValue = "") String endDate,
			@RequestParam(value = "start_date", required = false, defaultValue = "") String startDate) {

		if (null == appId || appId.equals("")) {
			appId = "4";
		}
		String ret = "";
		if ("count".equals(stat)) {
			ret = eventsService.getEventsCount(appId, id, version, startDate, endDate);
		} else if ("count_over_launch".equals(stat)) {
			ret = eventsService.getEventsPvRadio(appId, id, version, startDate, endDate);
		} else if ("device".equals(stat)) {
			ret = eventsService.getEventsUv(appId, id, version, startDate, endDate);
		} else if ("user_per_uv".equals(stat)) {
			ret = eventsService.getEventsUvRadio(appId, id, version, startDate, endDate);
		}
		return ret;
	}

}
