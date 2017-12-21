package com.douguo.dc.service;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.dao.OverviewDao;
import com.douguo.dc.model.AppModel;
import com.douguo.dc.model.Pager;
import com.douguo.dc.util.JsonUtil;
import com.douguo.dc.util.StatKey;
import com.douguo.dc.util.DateUtil;

@Repository("overviewService")
public class OverviewService {

	private final static Log LOG = LogFactory.getLog(OverviewService.class);
	NumberFormat format = NumberFormat.getPercentInstance(); // 获取格式化类实例
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	Comparator<String> localComparator = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {

			return -o1.compareTo(o2);
		}
	};

	@Autowired
	private OverviewDao OverviewDao;

	public Pager<AppModel> getApps(String status) {
		List<Map<String, Object>> appRows = this.OverviewDao.queryAppModel(status);

		List<AppModel> appList = new ArrayList<AppModel>();
		for (Map<String, Object> row : appRows) {
			AppModel app = new AppModel();
			app.setId((Integer) row.get("id"));
			app.setName((String) row.get("name"));
			app.setKey((String) row.get("key"));
			app.setUser_id((Integer) row.get("user_id"));
			app.setStatus((String) row.get("status"));
			appList.add(app);
		}

		Pager<AppModel> apps = new Pager<AppModel>();
		apps.setDatas(appList);
		apps.setSize(appList.size());
		return apps;
	}

	public String getIndexToday(String appId) {

		String TwoDaysAgo = sdf.format(System.currentTimeMillis() - 172800000);
		format.setMinimumFractionDigits(2);
		String yesterday = sdf.format(System.currentTimeMillis() - 86400000);
		Map<String, String> statMaps = new HashMap<String, String>();
		statMaps.put(StatKey.SessionCount, "launch"); // 启动次数
		statMaps.put(StatKey.NewUserCount, "install");// 新增用户
		statMaps.put(StatKey.UserCount, "active");// 活跃用户
		statMaps.put(StatKey.MeanSessionTime, "average"); // 使用时长

		List<Map<String, Object>> rowlstTwoDaysAgo = OverviewDao.queryValueInDay(appId, "TODAY", "ALL", TwoDaysAgo);
		List<Map<String, Object>> rowlstYesterday = OverviewDao.queryValueInDay(appId, "TODAY", "ALL", yesterday);

		List<Map<String, String>> localList = new ArrayList<Map<String, String>>();

		Map<String, String> mapYesterday = new HashMap<String, String>();
		mapYesterday.put("date", "昨日");

		for (Map<String, Object> map : rowlstYesterday) {
			String stat = map.get("stat_value").toString().replace("\n", "");
			String statKeyId = map.get("stat_key_id").toString();
			mapYesterday.put(statMaps.get(statKeyId), stat);
		}

		double installYesterday;
		double activeYesterday;
		try {
			installYesterday = Double.valueOf(mapYesterday.get("install"));
		} catch (Exception e) {
			installYesterday = 0.0;
			mapYesterday.put("install", "0");
		}

		try {
			activeYesterday = Double.valueOf(mapYesterday.get("active"));
		} catch (Exception e) {
			activeYesterday = 0.0;
			mapYesterday.put("active", "0");
		}

		if (activeYesterday == 0.0 || installYesterday == 0.0) {
			mapYesterday.put("rate", "-");
		} else {
			mapYesterday.put("rate", format.format(installYesterday / activeYesterday));
		}

		Map<String, String> mapTwoDaysAgo = new HashMap<String, String>();
		mapTwoDaysAgo.put("date", "前日");
		for (Map<String, Object> map : rowlstTwoDaysAgo) {
			String stat = map.get("stat_value").toString().replace("\n", "");
			String statKeyId = map.get("stat_key_id").toString();
			mapTwoDaysAgo.put(statMaps.get(statKeyId), stat);
		}
		double installToday;
		double activeToday;
		try {
			installToday = Double.valueOf(mapTwoDaysAgo.get("install"));
		} catch (Exception e) {
			installToday = 0.0;
			mapTwoDaysAgo.put("install", "0");
		}

		try {
			activeToday = Double.valueOf(mapTwoDaysAgo.get("active"));
		} catch (Exception e) {
			activeToday = 0.0;
			mapTwoDaysAgo.put("active", "0");
		}

		if (activeToday == 0.0 || installToday == 0.0) {
			mapTwoDaysAgo.put("rate", "-");
		} else {
			mapTwoDaysAgo.put("rate", format.format(installToday / activeToday));
		}

		// 累计用户
		List<Map<String, Object>> rowlstSum = OverviewDao.queryDay2Day(appId, StatKey.UserCount, "ALL", "ALL",
				TwoDaysAgo, yesterday);

		for (Map<String, Object> map : rowlstSum) {
			String stat = map.get("stat_value").toString().replace("\n", "");
			String statTime = map.get("stat_time").toString();
			if (statTime.equalsIgnoreCase(TwoDaysAgo)) {
				mapTwoDaysAgo.put("sum", stat);
			} else {
				mapYesterday.put("sum", stat);
			}
		}

		localList.add(mapYesterday);
		localList.add(mapTwoDaysAgo);
		int total = 2;
		String result = "success";

		return JsonUtil.getTableJson(localList, total, result);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	// 整体趋势数据明细
	public String getTrendDetails(String appId, String startDate, String endDate) {
		int total;
		try {
			total = (int) (sdf.parse(endDate).getTime() - sdf.parse(startDate).getTime()) / (24 * 60 * 60 * 1000) + 1;
		} catch (Exception e) {
			total = 0;
		}
		Map<String, String> statMaps = new HashMap<String, String>();
		statMaps.put("accumulations", StatKey.UserCount); // 累计用户
		statMaps.put("launch_data", StatKey.SessionCount);// 当天启动用户
		statMaps.put("active_data", StatKey.UserCount);// 活跃用户
		statMaps.put("duration_data", StatKey.MeanSessionTime); // 使用时长
		statMaps.put("install_data", StatKey.NewUserCount);// 新增用户
		List<Map<String, Object>> rowlst;
		Iterator iter = statMaps.entrySet().iterator();
		Map<String, Map<String, String>> allMap = new TreeMap<String, Map<String, String>>(localComparator);
		String tempStartDate = startDate;
		String tempEndDate = startDate;
		String key;
		String stats;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			key = (String) entry.getKey();
			stats = (String) entry.getValue();
			if (key.equalsIgnoreCase("accumulations")) {
				rowlst = OverviewDao.queryDay2Day(appId, stats, "ALL", "ALL", startDate, endDate);
			} else {
				rowlst = OverviewDao.queryDay2Day(appId, stats, "TODAY", "ALL", startDate, endDate);
			}

			Date dateBegin;
			Date dateEnd;
			long daySize = 0;
			for (Map<String, Object> map : rowlst) {
				try {
					dateBegin = sdf.parse(tempStartDate);
					tempEndDate = map.get("stat_time").toString();
					dateEnd = sdf.parse(tempEndDate);
					daySize = (dateEnd.getTime() - dateBegin.getTime()) / (24 * 60 * 60 * 1000);
				} catch (Exception e) {
				}
				String stat;
				String localTime;
				Map<String, String> localMap;
				for (long i = 0; i < daySize; i++) {
					stat = "-";
					localTime = DateUtil.getSpecifiedDayAfter(tempStartDate, (int) i);
					if (allMap.containsKey(localTime)) {
						localMap = allMap.get(localTime);
						localMap.put(key, stat);
					} else {
						localMap = new HashMap<String, String>();
						localMap.put(key, stat);
						allMap.put(localTime, localMap);
					}
				}

				stat = map.get("stat_value").toString().replace("\n", "");
				localTime = map.get("stat_time").toString();

				if (allMap.containsKey(localTime)) {
					localMap = allMap.get(localTime);
					localMap.put(key, stat);
				} else {
					localMap = new HashMap<String, String>();
					localMap.put(key, stat);
					allMap.put(localTime, localMap);
				}
				tempStartDate = DateUtil.getSpecifiedDayAfter(tempStartDate, (int) daySize + 1);
			}
			tempStartDate = startDate;
		}
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Iterator iterAll = allMap.entrySet().iterator();
		Double install_data;
		Double active_data;
		while (iterAll.hasNext()) {
			Map.Entry entry = (Map.Entry) iterAll.next();
			Map<String, String> val = (Map<String, String>) entry.getValue();
			val.put("date", (String) entry.getKey());
			try {
				install_data = Double.valueOf(val.get("install_data").toString());
				active_data = Double.valueOf(val.get("active_data").toString());
				val.put("install_rate", format.format(install_data / active_data));
			} catch (Exception e) {
				val.put("install_rate", "-");
			}
			data.add(val);
		}
		String ret = JsonUtil.getChartJson(null, data, false, "success", total);
		return ret;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	// 整体趋势中的某一个
	private String getTrendSingle(String appId, String stats, String time_type, String dimension_type,
			String startDate, String endDate) throws Exception {
		String name = startDate + "到 " + endDate;
		int sumSize = (int) ((sdf.parse(endDate).getTime() - sdf.parse(startDate).getTime()) / (24 * 60 * 60 * 1000)) + 1;

		List<Map<String, Object>> rowlst = OverviewDao.queryDay2Day(appId, stats, time_type, dimension_type, startDate,
				endDate);
		List<String> dates = new ArrayList<String>();
		List<Integer> data = new ArrayList<Integer>();
		Date dateBegin;
		Date dateEnd;
		for (Map<String, Object> map : rowlst) {
			dateBegin = sdf.parse(startDate);
			endDate = map.get("stat_time").toString();
			dateEnd = sdf.parse(endDate);
			long daySize = (dateEnd.getTime() - dateBegin.getTime()) / (24 * 60 * 60 * 1000);

			for (long i = 0; i < daySize; i++) {
				dates.add(DateUtil.getSpecifiedDayAfter(startDate, (int) i));
				data.add(Integer.valueOf("0"));
			}
			dates.add(endDate);
			data.add(Integer.valueOf(map.get("stat_value").toString().replace("\n", "")));
			startDate = DateUtil.getSpecifiedDayAfter(startDate, (int) daySize + 1);
		}
		int lastDateSize = sumSize - dates.size();
		for (int i = 0; i < lastDateSize; i++) {
			dates.add(startDate);
			data.add(0);
			startDate = DateUtil.getSpecifiedDayAfter(startDate, 1);
		}

		Map map0 = new HashMap();
		map0.put("name", name);
		map0.put("data", data);
		List<Map> statList = new ArrayList<Map>();
		statList.add(map0);
		String ret = JsonUtil.getChartJson(dates, statList, false, "success", -1);
		return ret;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	// 整体趋势中的活跃用户构成
	public String getInstallationRate(String appId, String startDate, String endDate) throws Exception {
		String tempStart = startDate;

		int sumSize = (int) ((sdf.parse(endDate).getTime() - sdf.parse(startDate).getTime()) / (24 * 60 * 60 * 1000)) + 1;

		List<Map<String, Object>> rowlstInstallation = OverviewDao.queryDay2Day(appId, StatKey.NewUserCount, "TODAY",
				"ALL", startDate, endDate); // 新增用户
		List<Map<String, Object>> rowlstActiveUser = OverviewDao.queryDay2Day(appId, StatKey.UserCount, "TODAY", "ALL",
				startDate, endDate); // 活跃用户
		List<String> dates = new ArrayList<String>();
		List<Integer> dataInstallation = new ArrayList<Integer>();
		List<Integer> dataNewUser = new ArrayList<Integer>();
		Map mapNewUser = new HashMap();
		mapNewUser.put("name", "新用户");

		Date dateBegin;
		Date dateEnd;

		for (Map<String, Object> map : rowlstInstallation) {
			dateBegin = sdf.parse(startDate);
			endDate = map.get("stat_time").toString();
			dateEnd = sdf.parse(endDate);
			long daySize = (dateEnd.getTime() - dateBegin.getTime()) / (24 * 60 * 60 * 1000);

			for (long i = 0; i < daySize; i++) {
				dates.add(DateUtil.getSpecifiedDayAfter(startDate, (int) i));
				dataInstallation.add(Integer.valueOf("0"));
			}
			dates.add(map.get("stat_time").toString());
			dataInstallation.add(Integer.valueOf(map.get("stat_value").toString().replace("\n", "")));
			startDate = DateUtil.getSpecifiedDayAfter(startDate, (int) daySize + 1);
		}

		List<Integer> dataOldUser = new ArrayList<Integer>();
		Map mapOldUser = new HashMap();
		mapOldUser.put("name", "老用户");

		startDate = tempStart;
		for (Map<String, Object> map : rowlstActiveUser) {
			dateBegin = sdf.parse(startDate);
			endDate = map.get("stat_time").toString();
			dateEnd = sdf.parse(endDate);
			long daySize = (dateEnd.getTime() - dateBegin.getTime()) / (24 * 60 * 60 * 1000);

			for (long i = 0; i < daySize; i++) {
				dataOldUser.add(Integer.valueOf("0"));
				dataNewUser.add(0);
			}

			int tempSize = dataOldUser.size();
			dataOldUser.add(Integer.valueOf(map.get("stat_value").toString().replace("\n", ""))
					- dataInstallation.get(tempSize));
			startDate = DateUtil.getSpecifiedDayAfter(startDate, (int) daySize + 1);
		}

		int lastDateSize = sumSize - dates.size();
		for (int i = 0; i < lastDateSize; i++) {
			dates.add(startDate);
			dataInstallation.add(0);
			dataOldUser.add(0);
			startDate = DateUtil.getSpecifiedDayAfter(startDate, 1);
		}

		mapNewUser.put("data", dataInstallation);
		mapOldUser.put("data", dataOldUser);

		List<Map> statList = new ArrayList<Map>();
		statList.add(mapNewUser);
		statList.add(mapOldUser);
		String ret = JsonUtil.getChartJson(dates, statList, false, "success", -1);
		return ret;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	// 某一维度在某一天中的时段分析
	private String getHoursByDay(String appId, String stats, String date) {
		String[] hours = new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
				"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };
		List<String> dates = Arrays.asList(hours);
		List<Map> statList = new ArrayList<Map>();

		String[] tempDates = date.split(",");
		for (int i = 0; i < tempDates.length; i++) {
			if (!tempDates[i].equalsIgnoreCase("")) {
				List<Map<String, Object>> rowlst = OverviewDao.queryHour2Hour(appId, stats, "ALL", tempDates[i]);
				@SuppressWarnings("serial")
				ArrayList<Integer> data = new ArrayList<Integer>(24) {
				};
				for (int j = 0; j < 24; j++) {
					data.add(0);
				}
				for (Map<String, Object> map : rowlst) {
					int hour = Integer.valueOf(map.get("hour").toString());
					data.remove(hour);
					data.add(hour, Integer.valueOf(map.get("stat_value").toString().replace("\n", "")));
				}
				Map map0 = new HashMap();
				map0.put("data", data);
				map0.put("visible", "true");
				map0.put("name", tempDates[i]);
				statList.add(map0);
			}
		}
		String ret = JsonUtil.getChartJson(dates, statList, false, "success", -1);
		return ret;
	}

	// 某一天趋势中的新增用户,只提供查询单独某一天的数据
	public String getActive(String appId, String date) {
		return getHoursByDay(appId, StatKey.UserCount, date);
	}

	// 某一天趋势中的启动次数,只提供查询单独某一天的数据
	public String getLaunches(String appId, String date) {
		return getHoursByDay(appId, StatKey.SessionCount, date);
	}

	// 整体趋势中的新增用户
	public String getInstallation(String appId, String startDate, String endDate) {
		try {
			return getTrendSingle(appId, StatKey.NewUserCount, "TODAY", "ALL", startDate, endDate);
		} catch (Exception e) {
			return "";
		}
	}

	// 整体趋势中的累计用户
	public String getAccumulation(String appId, String startDate, String endDate) {
		try {
			return getTrendSingle(appId, StatKey.UserCount, "ALL", "ALL", startDate, endDate);
		} catch (Exception e) {
			LOG.error("", e);
			return "";
		}
	}

	// 整体趋势中的活跃用户
	public String getActiveUser(String appId, String startDate, String endDate) {
		try {
			return getTrendSingle(appId, StatKey.UserCount, "TODAY", "ALL", startDate, endDate);
		} catch (Exception e) {
			LOG.error("", e);
			return "";
		}
	}

	// 整体趋势中的启动次数
	public String getLaunch(String appId, String startDate, String endDate) {
		try {
			return getTrendSingle(appId, StatKey.SessionCount, "TODAY", "ALL", startDate, endDate);
		} catch (Exception e) {
			LOG.error("", e);
			return "";
		}
	}

	// 整体趋势中的单次使用时长
	public String getAvgDuration(String appId, String startDate, String endDate) {
		try {
			return getTrendSingle(appId, StatKey.MeanSessionTime, "TODAY", "ALL", startDate, endDate);
		} catch (Exception e) {
			LOG.error("", e);
			return "";
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getScale(String appId, String date) {
		format.setMinimumFractionDigits(2);
		List<Map<String, Object>> rowlst = OverviewDao.queryDay2Day(appId, StatKey.UserCount, "ALL", "ALL", date, date);
		List<Map<String, Object>> rowlst7 = OverviewDao.queryDay2Day(appId, StatKey.UserCount, "LAST7", "ALL", date,
				date);
		List<Map<String, Object>> rowlst30 = OverviewDao.queryDay2Day(appId, StatKey.UserCount, "LAST30", "ALL", date,
				date);
		List<Map> statList = new ArrayList<Map>();
		double all = 0.0;
		double all7 = 0.0;
		double all30 = 0.0;
		for (Map<String, Object> map : rowlst) {
			Map map0 = new HashMap();
			map0.put("date", "累计用户");
			all = Double.valueOf(map.get("stat_value").toString().replace("\n", ""));
			map0.put("data", all);
			statList.add(map0);
		}
		for (Map<String, Object> map : rowlst7) {
			Map map0 = new HashMap();
			map0.put("date", "过去7天活跃用户 (%)");
			all7 = Double.valueOf(map.get("stat_value").toString().replace("\n", ""));
			map0.put("data", all7 + " (" + format.format(all7 / all) + ")");
			statList.add(map0);
		}
		for (Map<String, Object> map : rowlst30) {
			Map map0 = new HashMap();
			map0.put("date", "过去30天活跃用户 (%)");
			all30 = Double.valueOf(map.get("stat_value").toString().replace("\n", ""));
			map0.put("data", all30 + " (" + format.format(all30 / all) + ")");
			statList.add(map0);
		}
		String ret = JsonUtil.getChartJson(null, statList, false, "success", 3);
		return ret;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getSummary(String appId, String date) {
		format.setMinimumFractionDigits(2);
		List<Map<String, Object>> rowlst = OverviewDao.queryDay2Day(appId, StatKey.SessionCount, "ALL", "ALL", date,
				date);
		List<Map<String, Object>> rowlstUserCount = OverviewDao.queryDay2Day(appId, StatKey.UserCount, "ALL", "ALL",
				date, date);
		List<Map<String, Object>> rowlst7 = OverviewDao.queryDay2Day(appId, StatKey.MeanSessionTime, "LAST7", "ALL",
				date, date);
		List<Map<String, Object>> rowlst30 = OverviewDao.queryDay2Day(appId, StatKey.MeanSessionTime, "LAST30", "ALL",
				date, date);
		List<Map> statList = new ArrayList<Map>();
		for (Map<String, Object> map : rowlst) {
			Map map0 = new HashMap();
			map0.put("date", "累计启动");
			map0.put("data", map.get("stat_value").toString().replace("\n", ""));
			for (Map<String, Object> mapLocal : rowlstUserCount) {
				int allSessionCount = Integer.valueOf(map0.get("data").toString());
				int allUserCount = Integer.valueOf(mapLocal.get("stat_value").toString().replace("\n", ""));
				map0.put("data", allSessionCount + "(" + allUserCount / allSessionCount + ")");
			}
			statList.add(map0);
		}
		for (Map<String, Object> map : rowlst7) {
			Map map0 = new HashMap();
			map0.put("date", "过去7天平均单次使用时长");
			map0.put("data", map.get("stat_value").toString().replace("\n", ""));
			statList.add(map0);
		}
		for (Map<String, Object> map : rowlst30) {
			Map map0 = new HashMap();
			map0.put("date", "过去30天平均单次使用时长");
			map0.put("data", map.get("stat_value").toString().replace("\n", ""));
			statList.add(map0);
		}
		String ret = JsonUtil.getChartJson(null, statList, null, "success", 3);
		return ret;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	// 此方法中，所有标示为today的数据，其实都是前天的数据。
	public String getTopByDim(String appId, String dim, String metric, String startDate, String endDate) {
		format.setMinimumFractionDigits(2);
		String stats;
		String timetype;
		if (metric.equalsIgnoreCase("install")) {
			stats = StatKey.NewUserCount;
			timetype = "TODAY";
		} else if (metric.equalsIgnoreCase("active")) {
			stats = StatKey.UserCount;
			timetype = "TODAY";
		} else {
			stats = StatKey.UserCount;
			timetype = "ALL";
		}
		List<Map<String, Object>> rowlst = OverviewDao.queryTopByDim(appId, timetype, dim, stats, startDate, endDate);
		List<Map> statList = new ArrayList<Map>();
		List<String> dates = new ArrayList<String>();
		Map<String, String> tempMap = new TreeMap<String, String>(localComparator);
		String tempKey = "";
		double TodayAll = 0.0;
		double YesterdayAll = 0.0;
		String tempValue = "";
		for (Map<String, Object> map : rowlst) {
			tempKey = map.get("dimension_name").toString();
			tempValue = map.get("stat_value").toString().replace("\n", "");
			if (tempMap.containsKey(tempKey)) {
				tempMap.put(tempKey, tempMap.get(tempKey) + "&&" + "today" + "@#" + tempValue);
				try {
					TodayAll += Double.valueOf(tempValue);
				} catch (Exception e) {
					TodayAll += 0.0;
				}
			} else {
				tempMap.put(tempKey, "yesterday" + "@#" + tempValue);
				try {
					YesterdayAll += Double.valueOf(tempValue);
				} catch (Exception e) {
					YesterdayAll += 0.0;
				}
			}
		}
		Map mapToday = new HashMap();
		mapToday.put("index", 2);
		mapToday.put("visible", true);
		mapToday.put("name", "前日");
		Map mapYesterday = new HashMap();
		mapYesterday.put("index", 1);
		mapYesterday.put("visible", true);
		mapYesterday.put("name", "昨日");
		Iterator iterAll = tempMap.entrySet().iterator();
		List<Map> statListToday = new ArrayList<Map>();
		List<Map> statListYesterday = new ArrayList<Map>();
		double yesterdayY;
		double todayY;

		HashMap<String, String> qudaoDic = getQudaoDic();

		while (iterAll.hasNext()) {
			Map mapTodayValue = new HashMap();
			Map mapYesterdayValue = new HashMap();
			Map.Entry entry = (Map.Entry) iterAll.next();
			if (dim.equalsIgnoreCase("CHANNEL")) {
				if (null != qudaoDic && qudaoDic.containsKey((String) entry.getKey())) {
					dates.add(qudaoDic.get((String) entry.getKey()));
				} else {
					dates.add((String) entry.getKey());
				}
			} else {
				dates.add((String) entry.getKey());
			}

			String[] val = ((String) entry.getValue()).split("&&");
			try {
				todayY = Double.valueOf(val[1].split("@#")[1]);
			} catch (Exception e) {
				todayY = 0.0;
			}

			try {
				yesterdayY = Double.valueOf(val[0].split("@#")[1]);
			} catch (Exception e) {
				yesterdayY = 0.0;
			}
			mapTodayValue.put("name", format.format(todayY / TodayAll));
			mapTodayValue.put("y", todayY);
			mapYesterdayValue.put("name", format.format(yesterdayY / YesterdayAll));
			mapYesterdayValue.put("y", yesterdayY);
			statListToday.add(mapTodayValue);
			statListYesterday.add(mapYesterdayValue);
		}
		mapToday.put("data", statListToday);
		mapYesterday.put("data", statListYesterday);
		statList.add(mapToday);
		statList.add(mapYesterday);
		String ret = JsonUtil.getChartJson(dates, statList, null, "success", -1);
		return ret;
	}

	private HashMap<String, String> getQudaoDic() {

		List<Map<String, Object>> rowlst = OverviewDao.queryQudaoDic(1);
		HashMap<String, String> qudaoDic = new HashMap<String, String>();
		for (Map<String, Object> map : rowlst) {
			qudaoDic.put(map.get("qudao_code").toString(), map.get("qudao_name").toString().replace("\n", ""));
		}
		return qudaoDic;
	}

}
