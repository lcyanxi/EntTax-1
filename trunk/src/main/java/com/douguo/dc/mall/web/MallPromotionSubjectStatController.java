package com.douguo.dc.mall.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

import com.douguo.dc.mall.service.AppTuanService;
import com.douguo.dc.mall.service.MallPromotionSubjectStatService;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/mall/promotion/subject")
public class MallPromotionSubjectStatController {
	@Autowired
	private MallPromotionSubjectStatService mallPromotionSubjectStatService;

	@Autowired
	private AppTuanService appTuanService;

	@RequestMapping(value = "/subjectList", method = RequestMethod.GET)
	public String subjectList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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

		String plat = request.getParameter("plat");
		String order = request.getParameter("order");

		if (null == plat || "".equals(plat)) {
			plat = "STORE";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("plat", plat);
		model.put("order", order);
		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst = mallPromotionSubjectStatService.queryList(startDate, endDate);

		// 优食汇PV相关数据
		// List<Map<String, Object>> tuanList =
		// appTuanService.queryList(startDate, endDate);
		// Map<String ,Map<String,Object>> mapTuan = new
		// HashMap<String,Map<String,Object>>();
		// for(Map<String,Object> map : tuanList){
		// String statDate = (String)map.get("statdate");
		// mapTuan.put(statDate, map);
		// }
		// 优食汇订单相关数据
		List<Map<String, Object>> orderList = appTuanService.queryOrderSumList(startDate, endDate, "");
		Map<Date, Map<String, Object>> mapOrder = new HashMap<Date, Map<String, Object>>();
		for (Map<String, Object> map : orderList) {
			Date statDate = (Date) map.get("statdate");
			mapOrder.put(statDate, map);
		}
		//
		// BigDecimal moneysSum = new BigDecimal(0);
		// //
		// for (Map<String, Object> map : rowlst) {
		// BigDecimal moneys = (BigDecimal) map.get("moneys");
		// moneysSum = moneysSum.add(moneys);
		// }
		// //
		for (Map<String, Object> map : rowlst) {
			Date statDate = (Date) map.get("statdate");
			Integer subjectGoodsNum = (Integer) map.get("goods_num");
			Integer subjectPv = (Integer) map.get("subject_pv");
			Integer subjectGoodsPv = (Integer) map.get("goods_pv");
			BigDecimal subjectMoneys = (BigDecimal) map.get("moneys");

			// 优食汇-订单相关
			Map<String, Object> oMap = mapOrder.get(statDate);
			Long yshListPv = (Long) oMap.get("list_pv");
			Integer yshListUv = (Integer) oMap.get("list_uv");
			Long yshDetailPv = (Long) oMap.get("detail_pv");
			Integer yshDetailUv = (Integer) oMap.get("detail_uv");
			Integer yshPays = (Integer) oMap.get("pays");
			Integer yshGoods = (Integer) oMap.get("goods");
			if (yshGoods == null)
				yshGoods = 0;
			BigDecimal yshMoneys = (BigDecimal) oMap.get("moneys");

			// 专题单品转化率 ： 专题单品销售数量 / 专题pv
			BigDecimal subjectPvPayRate = new BigDecimal(0);

			if (0 != subjectPv.intValue()) {
				BigDecimal dbGoodsNum = new BigDecimal(subjectGoodsNum * 100);
				subjectPvPayRate = dbGoodsNum.divide(new BigDecimal(subjectPv), 2, BigDecimal.ROUND_HALF_UP);
			}
			// 优食汇转化率 : 销售商品量 / 优食汇pv
			BigDecimal yshPvPayRate = new BigDecimal(0);
			if (0 != yshListPv.intValue()) {
				BigDecimal dbYshGoods = new BigDecimal(yshGoods * 100);
				yshPvPayRate = dbYshGoods.divide(new BigDecimal(yshListPv), 2, BigDecimal.ROUND_HALF_UP);
			}
			// PV占比
			BigDecimal subjectGoodsPvRate = new BigDecimal(0);
			if (0 != yshDetailPv.intValue()) {
				BigDecimal dbSubjectGoodsPvm = new BigDecimal(subjectGoodsPv * 100);
				subjectGoodsPvRate = dbSubjectGoodsPvm.divide(new BigDecimal(yshDetailPv), 2, BigDecimal.ROUND_HALF_UP);
			}
			// 销量占比
			BigDecimal subjectGoodsRate = new BigDecimal(0);
			if (0 != yshGoods.intValue()) {
				BigDecimal dbSubjectGoodsNum = new BigDecimal(subjectGoodsNum * 100);
				subjectGoodsRate = dbSubjectGoodsNum.divide(new BigDecimal(yshGoods), 2, BigDecimal.ROUND_HALF_UP);
			}
			// 销售金额占比 ： 专题销售金额/优食汇销售金额
			BigDecimal subjectMoneyRate = new BigDecimal(0);
			if (0 != yshMoneys.intValue()) {
				subjectMoneys = subjectMoneys.multiply(new BigDecimal(100));
				subjectMoneyRate = subjectMoneys.divide(yshMoneys, 2, BigDecimal.ROUND_HALF_UP);
			}

			map.put("ysh_list_pv", yshListPv);
			map.put("ysh_list_uv", yshListUv);
			map.put("ysh_detail_pv", yshDetailPv);
			map.put("ysh_detail_uv", yshDetailUv);
			map.put("ysh_pays", yshPays);
			map.put("ysh_goods", yshGoods);
			map.put("ysh_moneys", yshMoneys);
			//
			map.put("subjectPvPayRate", subjectPvPayRate);
			map.put("YshPvPayRate", yshPvPayRate);
			map.put("subjectGoodsPvRate", subjectGoodsPvRate);
			map.put("subjectGoodsRate", subjectGoodsRate);
			map.put("subjectMoneyRate", subjectMoneyRate);
		}
		model.put("rowlst", rowlst);
		return "/mall/promotion/subject/mall_promotion_subject_list";
	}
}