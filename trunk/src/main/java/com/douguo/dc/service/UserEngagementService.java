package com.douguo.dc.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.dao.UserEngagementDao;

@Repository("UserEngagementService")
public class UserEngagementService {
	private UserEngagementDao	userEngagementDao;

	@Autowired
	public void setUserEngagementDao(UserEngagementDao userEngagementDao) {
		this.userEngagementDao = userEngagementDao;
	}

	public String getSessionDailyChart(String time_unit, String appId, String statdate, String dimension_type, String dimension_name, String statKeyId, String is_compared) {
		String format = "{\"stats\":[{\"data\":[%s],\"name\":\"%s\"}],\"dates\":[%s],\"is_compared\":%s,\"summary\":{\"value\":\"%s\",\"name\":\"%s\"},\"result\":\"%s\"}";
		/** 用户数总数 */
		int total = 0;
		String sideBar = "用户数总数";
		String name = statdate.substring(5);

		StringBuilder data = new StringBuilder();
		StringBuilder dates = new StringBuilder();
		List<Map<String, Object>> rowlst = this.userEngagementDao.querySessionDaily(appId, statdate, dimension_type, dimension_name, statKeyId);
		for (Map<String, Object> map : rowlst) {
			String value = String.valueOf(map.get("stat_value"));
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				}
			}
			total += Integer.valueOf(value);
		}
		String totalStr = String.valueOf(total);
		for (Map<String, Object> map : rowlst) {
			String time_type = String.valueOf(map.get("time_type"));
			if (null == time_type)
				continue;
			time_type = time_type.replaceAll("\n", "");
			if (time_type.trim().equals(""))
				continue;
			String key = this.getFrequencyXName(time_type);

			if (null == key || key.trim().equals(""))
				continue;
			String value = String.valueOf(map.get("stat_value"));
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				} else {
					value = this.getPercentage(value, totalStr, 2);
				}
			}
			dates.append("\"").append(key).append("\"").append(",");
			data.append(value).append(",");
		}

		String dataStr = "";
		if (0 != data.length()) {
			dataStr = data.substring(0, data.length() - 1);
		}
		String datesStr = "";
		if (0 != dates.length()) {
			datesStr = dates.substring(0, dates.length() - 1);
		}
		String ret = String.format(format, dataStr, name, datesStr, is_compared, totalStr, sideBar, "success");
		return ret;
	}

	public String getSessionDailyTable(String time_unit, String appId, String statdate, String dimension_type, String dimension_name, String statKeyId, String is_compared) {
		String format = "{\"total\":%s,\"stats\":[%s],\"result\":\"%s\"}";
		String subFormat = "{\"num\":%s,\"key\":\"%s\",\"percent\":%s}";
		/** 用户数总数 */
		int total = 0;
		StringBuilder stats = new StringBuilder();
		List<Map<String, Object>> rowlst = this.userEngagementDao.querySessionDaily(appId, statdate, dimension_type, dimension_name, statKeyId);
		for (Map<String, Object> map : rowlst) {
			String value = String.valueOf(map.get("stat_value"));
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				}
			}
			total += Integer.valueOf(value);
		}
		String totalStr = String.valueOf(total);
		int index = 0;
		for (Map<String, Object> map : rowlst) {
			String time_type = String.valueOf(map.get("time_type"));
			if (null == time_type)
				continue;
			time_type = time_type.replaceAll("\n", "");
			if (time_type.trim().equals(""))
				continue;
			String key = this.getFrequencyXName(time_type);

			if (null == key || key.trim().equals(""))
				continue;
			String value = String.valueOf(map.get("stat_value"));
			String percent = "";
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				} else {
					percent = this.getPercentage(value, totalStr, 2);
				}
			}
			String sub = String.format(subFormat, value, key, percent);
			stats.append(sub).append(",");
			index++;
		}

		String statsStr = "";
		if (0 != stats.length()) {
			statsStr = stats.substring(0, stats.length() - 1);
		}
		String ret = String.format(format, index, statsStr, "success");
		return ret;
	}

	public String getSessionLengthChart(String time_unit, String appId, String statdate, String dimension_type, String dimension_name, String statKeyId, String is_compared) {
		if (time_unit.equals("daily_per_launch")) {
			return this.handleChartPer(appId, statdate, dimension_type, dimension_name, statKeyId, is_compared);
		} else if (time_unit.equals("daily")) {
			return this.handleChartDaily(appId, statdate, dimension_type, dimension_name, statKeyId, is_compared);
		}
		return null;
	}

	public String getSessionLengthTable(String time_unit, String appId, String statdate, String dimension_type, String dimension_name, String statKeyId) {
		if (time_unit.equals("daily_per_launch")) {
			return this.handleTablePer(appId, statdate, dimension_type, dimension_name, statKeyId);
		} else if (time_unit.equals("daily")) {
			return this.handleTableDaily(appId, statdate, dimension_type, dimension_name, statKeyId);
		}
		return null;
	}

	private String handleTablePer(String appId, String statdate, String dimension_type, String dimension_name, String statKeyId) {
		String format = "{\"total\":%s,\"stats\":[%s],\"result\":\"%s\"}";
		String subFormat = "{\"num\":%s,\"key\":\"%s\",\"percent\":%s}";
		/** 启动次数总和 */
		int total = 0;
		StringBuilder stats = new StringBuilder();
		List<Map<String, Object>> rowlst = this.userEngagementDao.querySessionLength(appId, statdate, dimension_type, dimension_name, statKeyId);
		for (Map<String, Object> map : rowlst) {
			String value = String.valueOf(map.get("stat_value"));
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				}
			}
			total += Integer.valueOf(value);
		}
		String totalStr = String.valueOf(total);
		int index = 0;
		for (Map<String, Object> map : rowlst) {
			String time_type = String.valueOf(map.get("time_type"));
			if (null == time_type)
				continue;
			time_type = time_type.replaceAll("\n", "");
			if (time_type.trim().equals(""))
				continue;
			String key = this.getXName(time_type);

			if (null == key || key.trim().equals(""))
				continue;
			String value = String.valueOf(map.get("stat_value"));
			String percent = "";
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				} else {
					percent = this.getPercentage(value, totalStr, 2);
				}
			}
			String sub = String.format(subFormat, value, key, percent);
			stats.append(sub).append(",");
			index++;
		}

		String statsStr = "";
		if (0 != stats.length()) {
			statsStr = stats.substring(0, stats.length() - 1);
		}
		String ret = String.format(format, index, statsStr, "success");
		return ret;
	}

	private String handleChartPer(String appId, String statdate, String dimension_type, String dimension_name, String statKeyId, String is_compared) {
		String format = "{\"stats\":[{\"data\":[%s],\"name\":\"%s\"}],\"dates\":[%s],\"is_compared\":%s,\"summary\":{\"value\":\"%s\",\"name\":\"%s\"},\"result\":\"success\"}";
		/** 启动次数总和 */
		int total = 0;
		String siteBar = "总启动次数";
		String name = statdate.substring(5);

		StringBuilder data = new StringBuilder();
		StringBuilder dates = new StringBuilder();
		List<Map<String, Object>> rowlst = this.userEngagementDao.querySessionLength(appId, statdate, dimension_type, dimension_name, statKeyId);
		for (Map<String, Object> map : rowlst) {
			String value = String.valueOf(map.get("stat_value"));
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				}
			}
			total += Integer.valueOf(value);
		}
		String totalStr = String.valueOf(total);

		for (Map<String, Object> map : rowlst) {
			String time_type = String.valueOf(map.get("time_type"));
			if (null == time_type)
				continue;
			time_type = time_type.replaceAll("\n", "");
			if (time_type.trim().equals(""))
				continue;
			String key = this.getXName(time_type);

			if (null == key || key.trim().equals(""))
				continue;
			String value = String.valueOf(map.get("stat_value"));
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				} else {
					value = this.getPercentage(value, totalStr, 2);
				}
			}
			dates.append("\"").append(key).append("\"").append(",");
			data.append(value).append(",");
		}

		String dataStr = "";
		if (0 != data.length()) {
			dataStr = data.substring(0, data.length() - 1);
		}
		String datesStr = "";
		if (0 != dates.length()) {
			datesStr = dates.substring(0, dates.length() - 1);
		}
		String ret = String.format(format, dataStr, name, datesStr, is_compared, totalStr, siteBar);
		return ret;
	}

	private String handleTableDaily(String appId, String statdate, String dimension_type, String dimension_name, String statKeyId) {
		String format = "{\"total\":%s,\"stats\":[%s],\"result\":\"%s\"}";
		String subFormat = "{\"num\":%s,\"key\":\"%s\",\"percent\":%s}";
		/** 用户总数 */
		int total = 0;
		StringBuilder stats = new StringBuilder();
		List<Map<String, Object>> rowlst = this.userEngagementDao.querySessionLengthDaily(appId, statdate, dimension_type, dimension_name, statKeyId);
		for (Map<String, Object> map : rowlst) {
			String value = String.valueOf(map.get("stat_value"));
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				}
			}
			total += Integer.valueOf(value);
		}
		String totalStr = String.valueOf(total);
		int index = 0;
		for (Map<String, Object> map : rowlst) {
			String time_type = String.valueOf(map.get("time_type"));
			if (null == time_type)
				continue;
			time_type = time_type.replaceAll("\n", "");
			if (time_type.trim().equals(""))
				continue;
			String key = this.getXNameDaily(time_type);

			if (null == key || key.trim().equals(""))
				continue;
			String value = String.valueOf(map.get("stat_value"));
			String percent = "";
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				} else {
					percent = this.getPercentage(value, totalStr, 2);
				}
			}
			String sub = String.format(subFormat, value, key, percent);
			stats.append(sub).append(",");
			index++;
		}

		String statsStr = "";
		if (0 != stats.length()) {
			statsStr = stats.substring(0, stats.length() - 1);
		}
		String ret = String.format(format, index, statsStr, "success");
		return ret;
	}

	private String handleChartDaily(String appId, String statdate, String dimension_type, String dimension_name, String statKeyId, String is_compared) {
		String format = "{\"stats\":[{\"data\":[%s],\"name\":\"%s\"}],\"dates\":[%s],\"is_compared\":%s,\"summary\":{\"value\":\"%s\",\"name\":\"%s\"},\"result\":\"%s\"}";
		/** 用户数总数 */
		int total = 0;
		String sideBar = "用户数总数";
		String name = statdate.substring(5);

		StringBuilder data = new StringBuilder();
		StringBuilder dates = new StringBuilder();
		List<Map<String, Object>> rowlst = this.userEngagementDao.querySessionLengthDaily(appId, statdate, dimension_type, dimension_name, statKeyId);
		for (Map<String, Object> map : rowlst) {
			String value = String.valueOf(map.get("stat_value"));
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				}
			}
			total += Integer.valueOf(value);
		}
		String totalStr = String.valueOf(total);
		for (Map<String, Object> map : rowlst) {
			String time_type = String.valueOf(map.get("time_type"));
			if (null == time_type)
				continue;
			time_type = time_type.replaceAll("\n", "");
			if (time_type.trim().equals(""))
				continue;
			String key = this.getXNameDaily(time_type);

			if (null == key || key.trim().equals(""))
				continue;
			String value = String.valueOf(map.get("stat_value"));
			if (null == value) {
				value = "0";
			} else {
				value = value.replaceAll("\n", "");
				if (value.trim().equals("")) {
					value = "0";
				} else {
					value = this.getPercentage(value, totalStr, 2);
				}
			}
			dates.append("\"").append(key).append("\"").append(",");
			data.append(value).append(",");
		}

		String dataStr = "";
		if (0 != data.length()) {
			dataStr = data.substring(0, data.length() - 1);
		}
		String datesStr = "";
		if (0 != dates.length()) {
			datesStr = dates.substring(0, dates.length() - 1);
		}
		String ret = String.format(format, dataStr, name, datesStr, is_compared, totalStr, sideBar, "success");
		return ret;
	}

	private String getPercentage(String sub, String total, int scale) {
		if (null == total || total.trim().equals("0") || total.trim().equals("") || null == sub || sub.trim().equals("0") || sub.trim().equals("")) {
			return "";
		}
		if (scale < 0) {
			scale = 2;
		}
		BigDecimal divisor = new BigDecimal(sub);
		BigDecimal dividend = new BigDecimal(total);
		double d = divisor.divide(dividend, scale + 2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;

		BigDecimal result = new BigDecimal(Double.toString(d));
		BigDecimal one = new BigDecimal("1");
		String ret = result.divide(one, scale, BigDecimal.ROUND_HALF_UP).toString();
		return ret;
	}

	private String getXName(String key) {
		if (key.equals("SINGLE_1_S1-3")) {
			return "1-3 秒";
		} else if (key.equals("SINGLE_2_S4-10")) {
			return "4-10 秒";
		} else if (key.equals("SINGLE_3_S11-30")) {
			return "11-30 秒";
		} else if (key.equals("SINGLE_4_S31-60")) {
			return "31-60 秒";
		} else if (key.equals("SINGLE_5_M1-3")) {
			return "1-3 分";
		} else if (key.equals("SINGLE_6_M3-10")) {
			return "3-10 分";
		} else if (key.equals("SINGLE_7_M10-30")) {
			return "10-30 分";
		} else if (key.equals("SINGLE_8_M30+")) {
			return "30 分~";
		}
		return null;
	}

	private String getXNameDaily(String key) {
		if (key.equals("DAILY_1_S1-3")) {
			return "1-3 秒";
		} else if (key.equals("DAILY_2_S4-10")) {
			return "4-10 秒";
		} else if (key.equals("DAILY_3_S11-30")) {
			return "11-30 秒";
		} else if (key.equals("DAILY_4_S31-60")) {
			return "31-60 秒";
		} else if (key.equals("DAILY_5_M1-3")) {
			return "1-3 分";
		} else if (key.equals("DAILY_6_M3-10")) {
			return "3-10 分";
		} else if (key.equals("DAILY_7_M10-30")) {
			return "10-30 分";
		} else if (key.equals("DAILY_8_M30+")) {
			return "30 分~";
		}
		return null;
	}

	private String getFrequencyXName(String key) {
		if (key.equals("LAUNCH_1_1-2")) {
			return "1-2";
		} else if (key.equals("LAUNCH_2_3-5")) {
			return "3-5";
		} else if (key.equals("LAUNCH_3_6-9")) {
			return "6-9";
		} else if (key.equals("LAUNCH_4_10-19")) {
			return "10-19";
		} else if (key.equals("LAUNCH_5_20-49")) {
			return "20-49";
		} else if (key.equals("LAUNCH_6_50+")) {
			return "50+";
		}
		return null;
	}
}
