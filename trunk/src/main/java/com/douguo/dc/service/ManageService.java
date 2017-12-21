package com.douguo.dc.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.dao.ManageDao;
import com.douguo.dc.model.ChannelDict;
import com.douguo.dc.util.DateUtil;

@Repository("ManageService")
public class ManageService {

	@Autowired
	private ManageDao	ManageDao;

	public List<ChannelDict> getChannelListByStatus(int status) {
		List<ChannelDict> list = new ArrayList<ChannelDict>();
		List<Map<String, Object>> rowlst = ManageDao.queryChannelDict(status);
		if (null == rowlst)
			return null;
		for (Map<String, Object> map : rowlst) {
			ChannelDict channelDict = new ChannelDict();
			channelDict.setId(Integer.parseInt(map.get("id").toString()));
			channelDict.setChannelCode(String.valueOf(map.get("channel_code")));
			channelDict.setChannelName(String.valueOf(map.get("channel_name")));
			list.add(channelDict);
		}
		return list;
	}

	public int insertChannelDict(String code, String name) {
		return ManageDao.insertChannelDict(code, name);
	}

	public int deleteChannelDict(int id) {
		return ManageDao.updateChannelDict(id);
	}

	public String getEventsListByStatus(int status, String sortname, String sortorder, int startRow, int page_no, int pagesize, String qtype, String query) {
		String format = "{\"page\":%s,\"total\":%s, \"rows\":[%s]}";
		String formatSub = "{\"id\":%s,\"cell\":[\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"]}";
		/* Get total rows */
		int totalRows = this.ManageDao.getTotal("dd_event_dic");
		List<Map<String, Object>> rowlst = ManageDao.queryEventsDictPage(status, sortname, sortorder, startRow, pagesize, qtype, query);
		if (null == rowlst)
			return null;
		StringBuffer rows = new StringBuffer();
		for (Map<String, Object> map : rowlst) {
			int id = Integer.parseInt(map.get("id").toString());
			String event_code = String.valueOf(map.get("event_code"));
			String event_name = String.valueOf(map.get("event_name"));
			int app_id = Integer.parseInt(map.get("app_id").toString());
			int stat = Integer.parseInt(map.get("status").toString());
			String temp = String.format(formatSub, id, id, event_code, event_name, app_id, stat);
			rows.append(temp).append(",");
		}

		String dataStr = "";
		if (0 != rows.length()) {
			dataStr = rows.substring(0, rows.length() - 1);
		}
		return String.format(format, page_no, totalRows, dataStr);
	}

	public String getAppList(String sortname, String sortorder, int startRow, int page_no, int pagesize, String qtype, String query) {
		String format = "{\"page\":%s,\"total\":%s, \"rows\":[%s]}";
		String formatSub = "{\"id\":%s,\"cell\":[\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"]}";

		int totalRows = this.ManageDao.getTotal("app");
		List<Map<String, Object>> rowlst = ManageDao.queryAppDictPage(sortname, sortorder, startRow, pagesize, qtype, query);
		if (null == rowlst)
			return null;
		StringBuffer rows = new StringBuffer();
		for (Map<String, Object> map : rowlst) {
			int id = Integer.parseInt(map.get("id").toString());
			String name = String.valueOf(map.get("name"));
			String key = String.valueOf(map.get("key"));
			int user_id = Integer.parseInt(map.get("user_id").toString());
			Timestamp t1 = (Timestamp) map.get("create_time");
			String create_time = DateUtil.timestamp2Str(t1, "yyyy-MM-dd HH:mm:ss");

			Timestamp t2 = (Timestamp) map.get("update_time");
			String update_time = DateUtil.timestamp2Str(t2, "yyyy-MM-dd HH:mm:ss");
			if (null == update_time) {
				update_time = "null";
			}

			String temp = String.format(formatSub, id, id, name, key, user_id, create_time, update_time);
			rows.append(temp).append(",");
		}

		String dataStr = "";
		if (0 != rows.length()) {
			dataStr = rows.substring(0, rows.length() - 1);
		}
		return String.format(format, page_no, totalRows, dataStr);
	}

	public String getVersionListByStatus(int status, String sortname, String sortorder, int startRow, int page_no, int pagesize, String qtype, String query) {
		String format = "{\"page\":%s,\"total\":%s, \"rows\":[%s]}";
		String formatSub = "{\"id\":%s,\"cell\":[\"%s\",\"%s\",\"%s\",\"%s\"]}";
		/* Get total rows */
		int totalRows = this.ManageDao.getTotal("dd_app_version_dict");
		List<Map<String, Object>> rowlst = ManageDao.queryVersionDictPage(status, sortname, sortorder, startRow, pagesize, qtype, query);
		if (null == rowlst)
			return null;
		StringBuffer rows = new StringBuffer();
		for (Map<String, Object> map : rowlst) {
			int id = Integer.parseInt(map.get("id").toString());
			String app_id = String.valueOf(map.get("app_id"));
			String app_version = String.valueOf(map.get("app_version"));
			int stat = Integer.parseInt(map.get("status").toString());
			String temp = String.format(formatSub, id, id, app_id, app_version, stat);
			rows.append(temp).append(",");
		}

		String dataStr = "";
		if (0 != rows.length()) {
			dataStr = rows.substring(0, rows.length() - 1);
		}
		return String.format(format, page_no, totalRows, dataStr);
	}

	public String getAppsByStatus(String status) {
		String format = "[%s]";
		String formatSub = "{\"id\":%s,\"name\":\"%s\"}";
		List<Map<String, Object>> rowlst = ManageDao.queryAppsDict(status);
		if (null == rowlst)
			return null;
		StringBuffer rows = new StringBuffer();
		for (Map<String, Object> map : rowlst) {
			int id = Integer.parseInt(map.get("id").toString());
			String name = String.valueOf(map.get("name"));
			String temp = String.format(formatSub, id, name);
			rows.append(temp).append(",");
		}

		String dataStr = "";
		if (0 != rows.length()) {
			dataStr = rows.substring(0, rows.length() - 1);
		}
		return String.format(format, dataStr);
	}

	public String addEventDict(String event_name, String event_code, String app_id, String status) {
		return this.ManageDao.insertEventDict(event_name, event_code, app_id, status);
	}

	public String updateEventDict(String id, String event_name, String event_code, String app_id, String status) {
		return this.ManageDao.updateEventDict(id, event_name, event_code, app_id, status);
	}

	public boolean deleteEventByIds(String ids) {
		return this.ManageDao.deleteEventByIds(ids);
	}
	
	public boolean deleteAppByIds(String ids) {
		return this.ManageDao.deleteAppByIds(ids);
	}

	public String addVersionDict(String app_id, String app_version, String status) {
		return this.ManageDao.insertVersionDict(app_id, app_version, status);
	}

	public boolean deleteVersionByIds(String ids) {
		return this.ManageDao.deleteVersionByIds(ids);
	}

	public String updateVersionDict(String idStr, String app_id, String app_version, String status) {
		return this.ManageDao.updateVersionDict(idStr, app_id, app_version, status);
	}

	public String addAppDict(String name, String key, String user_id) {
		return this.ManageDao.insertAppDict(name, key, user_id);
	}

	public String updateAppDic(String idStr, String name, String key, String user_id) {
		return this.ManageDao.updateAppDict(idStr, name, key, user_id);
	}
}
