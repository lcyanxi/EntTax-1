package com.douguo.dc.applog.web;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.douguo.dc.applog.service.AppClickService;
import com.douguo.dc.applog.service.AppLogClickDictService;
import com.douguo.dc.applog.service.AppTypeDictService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/appclick")
public class AppClickController {
	@Autowired
	private AppClickService appClickService;

	@Autowired
	private AppTypeDictService appTypeDictService;
	@Autowired
	private AppLogClickDictService appLogClickDictService;

	@RequestMapping(value = "/xsplist", method = RequestMethod.GET)
	public String xspList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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

		String type = request.getParameter("type");
		String data1 = request.getParameter("data1");
		String data3 = request.getParameter("data3");

		if (null == type || "".equals(type)) {
			type = "38";
		}

		Map<String, String> mapTypeDict = getAppTypeDict(type);

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("type", type);
		model.put("mapTypeDict", mapTypeDict);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst;
		if (type.equals("38")) {
			if (data1 == null || "".equals(data1)) {
				data1 = "0";
			}
			rowlst = appClickService.queryListData3(startDate, endDate, type, data1);
		} else {
			rowlst = appClickService.queryList(startDate, endDate, type, data1);
		}

		model.put("rowlst", rowlst);
		return "/app_click/xspList";
	}

	/**
	 * 豆果美食点击统计
	 * 
	 * @param appId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dglist", method = RequestMethod.GET)
	public String dgList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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

		String page = request.getParameter("page");
		String view = request.getParameter("view");
		String position = request.getParameter("position");

		if (null == page || "".equals(page)) {
			page = "1";
		}

		Map<String, String> mapClickDict = getAppLogClickDict(page, false);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst;

		if (StringUtils.isNotBlank(view)) {
			rowlst = appClickService.queryPVList(startDate, endDate, page, view);

			// rowlst = appClickService.queryPVList(startDate, endDate, page);
			// mapClickDict = getAppLogClickDict(page, false);
			// }
			// else if (position == null || "".equals(position)) {
			// rowlst = appClickService.queryPVList(startDate, endDate, page,
			// view);
			// mapClickDict = getAppLogClickDict(page, false);
		} else {
			rowlst = appClickService.queryPVList(startDate, endDate, page);
			// mapClickDict = getAppLogClickDict(page, false);
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("page", page);
		model.put("view", view);
		model.put("mapClickDict", mapClickDict);
		model.put("rowlst", rowlst);
		return "/app_click/dgList";
	}

	/**
	 * 豆果美食点击统计-位置
	 * 
	 * @param appId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dgpolist", method = RequestMethod.GET)
	public String dgPoList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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

		String page = request.getParameter("page");
		String view = request.getParameter("view");
		String position = request.getParameter("position");

		if (null == page || "".equals(page)) {
			page = "1";
		}

		Map<String, String> mapClickDict = getAppLogClickDict(page, true);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst;

		if (StringUtils.isNotBlank(position)) {
			rowlst = appClickService.queryPVPoList(startDate, endDate, page, view, position);

			// rowlst = appClickService.queryPVList(startDate, endDate, page);
			// mapClickDict = getAppLogClickDict(page, false);
			// }
			// else if (position == null || "".equals(position)) {
			// rowlst = appClickService.queryPVList(startDate, endDate, page,
			// view);
			// mapClickDict = getAppLogClickDict(page, false);
		} else {
			rowlst = appClickService.queryPVPoList(startDate, endDate, page, view);
			// mapClickDict = getAppLogClickDict(page, false);
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("page", page);
		model.put("view", view);
		model.put("position", position);
		model.put("mapClickDict", mapClickDict);
		model.put("rowlst", rowlst);
		return "/app_click/dgPoList";
	}

	private Map<String, String> getAppTypeDict(String type) {
		List<Map<String, Object>> appTypeList = appTypeDictService.queryListByType(type);

		Map<String, String> mapTypeDict = new HashMap<String, String>();
		if ((appTypeList == null) || (appTypeList.size() == 0)) {
			return mapTypeDict;
		}

		for (Map<String, Object> map : appTypeList) {
			String tmpType = String.valueOf(map.get("type"));
			String tmpData = String.valueOf(map.get("data"));
			String tmpVal = String.valueOf(map.get("val"));
			String tmpName = (String) map.get("name");
			mapTypeDict.put(tmpType + "_" + tmpData + "_" + tmpVal, tmpName);
		}

		return mapTypeDict;
	}

	/**
	 * 新版530点击日志字典
	 * 
	 * @param type
	 * @param isPO
	 *            是否生成position的对应关系
	 * @return
	 */
	private Map<String, String> getAppLogClickDict(String page, boolean isPO) {
		List<Map<String, Object>> appClickDictList = appLogClickDictService.queryListByPage(page);

		Map<String, String> mapClickDict = new HashMap<String, String>();
		if ((appClickDictList == null) || (appClickDictList.size() == 0)) {
			return mapClickDict;
		}

		for (Map<String, Object> map : appClickDictList) {
			String tmpPage = String.valueOf(map.get("page"));
			String tmpView = String.valueOf(map.get("view"));
			String tmpPO = String.valueOf(map.get("position"));
			String tmpTitle = (String) map.get("title");
			if (isPO) {

				mapClickDict.put(tmpPage + "_" + tmpView + "_" + tmpPO, tmpTitle);
			} else {
				String key = tmpPage + "_" + tmpView;
				if (!mapClickDict.containsKey(key)) {
					mapClickDict.put(key, tmpTitle);
				}

			}
		}

		return mapClickDict;
	}
}
