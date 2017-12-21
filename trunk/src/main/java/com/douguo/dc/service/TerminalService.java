package com.douguo.dc.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.dao.TerminalDao;
import com.douguo.dc.model.DevicesModel;
import com.douguo.dc.util.StatKey;

@Repository("TerminalService")
public class TerminalService {

	private TerminalDao	terminalDao;

	@Autowired
	public void setTerminalDao(TerminalDao terminalDao) {
		this.terminalDao = terminalDao;
	}

	public String getDevicesChartData(String appId, String statDate, String time_type, String stats) {
		String stat_key_id = "";
		String dimension_type = "";

		if (stats.trim().equals("devices_devices_active_user") || stats.trim().equals("devices_resolutions_active_user") || stats.trim().equals("devices_versions_active_user") || stats.trim().equals("devices_network_active_user") || stats.trim().equals("devices_carriers_active_user") || stats.trim().equals("devices_location_active_user")) {
			stat_key_id = StatKey.TerminalDevicesUV;
		} else if (stats.trim().equals("devices_devices_launches") || stats.trim().equals("devices_resolutions_launches") || stats.trim().equals("devices_versions_launches") || stats.trim().equals("devices_network_launches") || stats.trim().equals("devices_carriers_launches") || stats.trim().equals("devices_location_launches")) {
			stat_key_id = StatKey.TerminalDevicesPV;
		} else {
			return null;
		}

		if (null != stats && (stats.trim().equals("devices_devices_active_user") || stats.trim().equals("devices_devices_launches"))) {
			dimension_type = "DEVICE";
		} else if (null != stats && (stats.trim().equals("devices_resolutions_active_user") || stats.trim().equals("devices_resolutions_launches"))) {
			dimension_type = "SCREEN";
		} else if (null != stats && (stats.trim().equals("devices_versions_active_user") || stats.trim().equals("devices_versions_launches"))) {
			dimension_type = "OS_VERSION";
		} else if (null != stats && (stats.trim().equals("devices_network_active_user") || stats.trim().equals("devices_network_launches"))) {
			dimension_type = "NETWORK_MODE";
		} else if (null != stats && (stats.trim().equals("devices_carriers_active_user") || stats.trim().equals("devices_carriers_launches"))) {
			dimension_type = "OPERATOR";
		} else if (null != stats && (stats.trim().equals("devices_location_active_user") || stats.trim().equals("devices_location_launches"))) {
			dimension_type = "LOCATION";
		} else {
			return null;
		}

		String name = statDate;
		String format = "{\"stats\":[{\"data\":[%s],\"name\":\"%s\"}],\"dates\":[%s],\"result\":\"%s\"}";
		StringBuilder data = new StringBuilder();
		StringBuilder dates = new StringBuilder();
		List<Map<String, Object>> rowlst = this.terminalDao.getDevicesChartData(appId, statDate, time_type, dimension_type, stat_key_id);
		for (Map<String, Object> map : rowlst) {
			String nm = String.valueOf(map.get("dimension_name"));
			if (null == nm)
				continue;
			nm = nm.replaceAll("\n", "");
			if (nm.trim().equals(""))
				continue;
			if(dimension_type.equals("LOCATION")){
				nm = terminalDao.getLocationName(nm.trim());
			}
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
			dates.append("\"").append(nm).append("\"").append(",");
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

	public String getDevicesTableData(int startRow, int pagesize, String appId, String statDate, String time_type, String stats) {
		String dimension_type = "";
		if (null != stats && stats.trim().indexOf("_devices_") > 0 ) {
			dimension_type = "DEVICE";
		} else if (null != stats && stats.trim().indexOf("_resolutions_") > 0 ) {
			dimension_type = "SCREEN";
		} else if (null != stats && stats.trim().indexOf("_versions_") > 0 ) {
			dimension_type = "OS_VERSION";
		} else if (null != stats && stats.trim().indexOf("_network_") > 0 ) {
			dimension_type = "NETWORK_MODE";
		} else if (null != stats && stats.trim().indexOf("_carriers_") > 0 ) {
			dimension_type = "OPERATOR";
		} else if (null != stats && stats.trim().indexOf("_location_") > 0 ) {
			dimension_type = "LOCATION";
		} else {
			return null;
		}

		String newUserTotal = this.terminalDao.getSumVal(appId, statDate, time_type, dimension_type, StatKey.TerminalDevicesUV);
		String launchTotal = this.terminalDao.getSumVal(appId, statDate, time_type, dimension_type, StatKey.TerminalDevicesPV);

		String total = this.terminalDao.getTotal(appId, statDate, time_type, dimension_type, StatKey.TerminalDevicesUV);

		Map<String, DevicesModel> devices = new LinkedHashMap<String, DevicesModel>();
		List<Map<String, Object>> rowlstUV = this.terminalDao.getDevicesTableData(startRow, pagesize, appId, statDate, time_type, dimension_type, StatKey.TerminalDevicesUV);
		for (Map<String, Object> map : rowlstUV) {
			String nm = String.valueOf(map.get("dimension_name"));
			if (null == nm)
				continue;
			nm = nm.replaceAll("\n", "");
			if (nm.trim().equals(""))
				continue;
			String user_value = String.valueOf(map.get("stat_value"));
			if (null == user_value) {
				user_value = "0";
			} else {
				user_value = user_value.replaceAll("\n", "");
				if (user_value.trim().equals("")) {
					user_value = "0";
				}
			}
			String user_per = this.getPercentage(user_value, newUserTotal, 2);
			DevicesModel dm = new DevicesModel();
			dm.setModel(nm);
			dm.setUser(user_value);
			dm.setUserPer(user_per);
			devices.put(nm, dm);
		}
		List<Map<String, Object>> rowlstPV = this.terminalDao.getDevicesTableData(startRow, pagesize, appId, statDate, time_type, dimension_type, StatKey.TerminalDevicesPV);
		for (Map<String, Object> map : rowlstPV) {
			String nm = String.valueOf(map.get("dimension_name"));
			if (null == nm)
				continue;
			nm = nm.replaceAll("\n", "");
			if (nm.trim().equals(""))
				continue;
			DevicesModel dm = devices.get(nm);
			if (null == dm)
				continue;

			String launch_value = String.valueOf(map.get("stat_value"));
			if (null == launch_value) {
				launch_value = "0";
			} else {
				launch_value = launch_value.replaceAll("\n", "");
				if (launch_value.trim().equals("")) {
					launch_value = "0";
				}
			}
			String launch_per = this.getPercentage(launch_value, launchTotal, 2);

			dm.setLaunch(launch_value);
			dm.setLaunchPer(launch_per);
		}

		String format = "{\"total\":%s,\"stats\":[%s],\"result\":\"success\"}";
		String subFormat = "{\"active_rate\":%s,\"active_data\":\"%s\",\"launch_rate\":%s,\"date\":\"%s\",\"launch_data\":\"%s\"}";
		StringBuilder stats_data = new StringBuilder();
		for (Map.Entry<String, DevicesModel> entry : devices.entrySet()) {
			DevicesModel dm = entry.getValue();
			String name = terminalDao.getLocationName(dm.getModel().trim());
			String temp = String.format(subFormat, dm.getUserPer(), dm.getUser(), dm.getLaunchPer(), name, dm.getLaunch());
			stats_data.append(temp).append(",");
		}

		String dataStr = "";
		if (0 != stats_data.length()) {
			dataStr = stats_data.substring(0, stats_data.length() - 1);
		}
		return String.format(format, total, dataStr);
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
}
