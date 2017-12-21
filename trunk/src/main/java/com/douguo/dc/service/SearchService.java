package com.douguo.dc.service;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

import com.douguo.dc.dao.SearchDao;
import com.douguo.dc.model.AppModel;
import com.douguo.dc.model.SearchemptyModel;
import com.douguo.dc.model.Pager;
import com.douguo.dc.util.JsonUtil;
import com.douguo.dc.util.StatKey;
@Repository("searchService")
public class SearchService {

	@Autowired
	private SearchDao SearchDao;
	public List<Map<String, Object>> getEmptyList(String startDate, Integer clienttypeid) {
		List<Map<String, Object>> keyRows = this.SearchDao.selectSearchEmptyList(startDate, clienttypeid);
		return  keyRows;
	}
}
