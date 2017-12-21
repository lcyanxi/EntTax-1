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

import com.douguo.dc.service.AizhanRankService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/aizhan")
public class AizhanRankController {
	@Autowired
	private AizhanRankService aizhanRankService;

	@RequestMapping(value = "/ranklist", method = RequestMethod.GET)
	public String recipseList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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

		Integer source = 0;
		String sSource = request.getParameter("source");
		 String type = request.getParameter("type");
//		String type = "cook";

		if (null == type || "".equals(type)) {
			type = "nokeyword";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("type", type);
		

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst = aizhanRankService.queryListByType(startDate, endDate, type);
//		Map<String, String> rowlst3 = new HashMap<String, String>();
//		Set<String> tableLine = new LinkedHashSet<String>();
//		Set<String> tableColumn = new LinkedHashSet<String>();
//		for (Map<String, Object> map : rowlst) {
//
//			String province = (String) map.get("province");
//			if (province != null) {
//				province = province.trim();
//			}
//			// 香港的是null
//			if (province.equals("null")) {
//				province = (String) map.get("city");
//			}
//			String province_name = province;
//			if (province_name == null) {
//				province_name = province;
//			}
//			String statdate = map.get("statdate").toString();
//			BigDecimal num = (BigDecimal) map.get("pnum");
//			BigDecimal usernum = (BigDecimal) map.get("pusernum");
//			if (num == null) {
//				num = new BigDecimal("0");
//			}
//			if (usernum == null) {
//				usernum = new BigDecimal("0");
//			}
//			tableLine.add(statdate);
//			tableColumn.add(province_name);
//			rowlst3.put(province_name + '-' + statdate, num.intValue() + "(" + usernum.intValue() + ")");
//		}
		model.put("rowlst", rowlst);
//		request.getSession().setAttribute("rowlst", rowlst);
//		request.getSession().setAttribute("tableLine", tableLine);
//		request.getSession().setAttribute("tableColumn", tableColumn);
		return "/aizhan/aizhanRankList";
	}
}
