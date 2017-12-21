package com.douguo.dc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.dao.ErrorAnalysisDao;
import com.douguo.dc.util.StatKey;

@Repository("ErrorAnalysisService")
public class ErrorAnalysisService {

	private ErrorAnalysisDao	errorAnalysisDao;

	@Autowired
	public void setErrorAnalysisDao(ErrorAnalysisDao errorAnalysisDao) {
		this.errorAnalysisDao = errorAnalysisDao;
	}

	public String getData(String appId, String startDate, String endDate, String time_type, String dimension_type, String dimension_name, String stat_key_id, String stats) {
		if (stats.equals("error_count")) {
			return this.getChartData(appId, startDate, endDate, time_type, dimension_type, dimension_name, stat_key_id);
		} else if (stats.equals("error_list")) {
			return this.getTableData(appId, startDate, endDate, time_type, dimension_type, dimension_name, stat_key_id);
		} else {
			return null;
		}
	}

	private String getChartData(String appId, String startDate, String endDate, String time_type, String dimension_type, String dimension_name, String stat_key_id) {
		String format = "{\"stats\":[{\"data\":[%s],\"name\":\"%s\"}],\"dates\":[%s],\"result\":\"%s\"}";

		String name = "总错误数";
		StringBuilder data = new StringBuilder();
		StringBuilder dates = new StringBuilder();
		List<Map<String, Object>> rowlst = this.errorAnalysisDao.getData(appId, startDate, endDate, time_type, dimension_type, dimension_name, stat_key_id, "asc");
		for (Map<String, Object> map : rowlst) {
			String stat_time = String.valueOf(map.get("stat_time"));
			stat_time = stat_time.replaceAll("\n", "");
			if (stat_time.trim().equals(""))
				continue;

			String value = String.valueOf(map.get("stat_value"));
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				}
			}
			data.append(value).append(",");
			dates.append("\"").append(stat_time).append("\"").append(",");
		}
		String dataStr = "";
		if (0 != data.length()) {
			dataStr = data.substring(0, data.length() - 1);
		}
		String datesStr = "";
		if (0 != dates.length()) {
			datesStr = dates.substring(0, dates.length() - 1);
		}

		return String.format(format, dataStr, name, datesStr, "success");
	}

	private String getTableData(String appId, String startDate, String endDate, String time_type, String dimension_type, String dimension_name, String stat_key_id) {
		String formatStr = "{\"total\":%s,\"stats\":[%s],\"result\":\"%s\"}";
		String formatData = "{\"version\":\"%s\",\"stat_time\":\"%s\",\"error_count\":%s,\"launch_count\":%s}";

		List<Map<String, Object>> rowlstLaunch = this.errorAnalysisDao.getData(appId, startDate, endDate, time_type, "ALL", "", StatKey.SessionCount, "desc");
		Map<String, String> launch = new HashMap<String, String>();
		for (Map<String, Object> map : rowlstLaunch) {
			String stat_time = String.valueOf(map.get("stat_time"));
			stat_time = stat_time.replaceAll("\n", "");
			if (stat_time.trim().equals(""))
				continue;
			String value = String.valueOf(map.get("stat_value"));
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				}
			}
			launch.put(stat_time, value);
		}

		StringBuilder data = new StringBuilder();
		List<Map<String, Object>> rowlstError = this.errorAnalysisDao.getData(appId, startDate, endDate, time_type, dimension_type, dimension_name, stat_key_id, "desc");
		int index = 0;
		for (Map<String, Object> map : rowlstError) {
			String stat_time = String.valueOf(map.get("stat_time"));
			stat_time = stat_time.replaceAll("\n", "");
			if (stat_time.trim().equals(""))
				continue;
			String lauch_value = launch.get(stat_time);
			if (null == lauch_value || lauch_value.equals(""))
				lauch_value = "0";
			String value = String.valueOf(map.get("stat_value"));
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				}
			}
			String version = (dimension_name.equals("")) ? "ALL" : dimension_name;
			data.append(String.format(formatData, version, stat_time, value, lauch_value)).append(",");
			index++;
		}
		String dataStr = "";
		if (0 != data.length()) {
			dataStr = data.substring(0, data.length() - 1);
		}
		return String.format(formatStr, index, dataStr, "success");
	}
}
