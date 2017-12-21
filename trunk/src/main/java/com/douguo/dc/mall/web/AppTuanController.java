package com.douguo.dc.mall.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.douguo.dc.util.BigDecimalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.douguo.dc.channel.web.ChannelSumExcelView;
import com.douguo.dc.mall.service.AppTuanService;
import com.douguo.dc.util.DateUtil;
import com.douguo.dg.mall.service.DgGoodsService;

@Controller
@RequestMapping("/apptuan")
public class AppTuanController {
	@Autowired
	private AppTuanService appTuanService;

	@Autowired
	private DgGoodsService dgGoodsService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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

		if (null == plat || "".equals(plat)) {
			plat = "CHART";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("plat", plat);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst = appTuanService.queryList(startDate, endDate);

		model.put("rowlst", rowlst);
		return "/app_tuan/app_tuan_list";
	}

	@RequestMapping(value = "/mapList", method = RequestMethod.GET)
	public String mapList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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

		if (null == plat || "".equals(plat)) {
			plat = "CHART";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("plat", plat);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst;
		List<Map<String, Object>> newrowlst = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> rowSumList = null;

		rowlst = appTuanService.queryList(startDate, endDate);
		rowSumList = appTuanService.queryList(startDate, endDate);

		Map<String, BigDecimal> mapSums = new HashMap<String, BigDecimal>();

		// for (Map<String, Object> map : rowSumList) {
		// Date date = (Date) map.get("statdate");
		// String strDate = "";
		// if (date != null) {
		// strDate = (String) date.toString();
		// }
		//
		// String strPlat = (String) map.get("plat");
		// String strQType = (String) map.get("qtype");
		// BigDecimal sums = (BigDecimal) map.get("ntimes");
		// String strKey = strDate + "_" + strPlat + "_" + strQType;
		// if (!mapSums.containsKey(strKey)) {
		// mapSums.put(strKey, sums);
		// }
		// }
		//
		// for (Map<String, Object> map : rowlst) {
		// Date date = (Date) map.get("statdate");
		// String strDate = "";
		// if (date != null) {
		// strDate = (String) date.toString();
		// }
		// String strPlat = (String) map.get("plat");
		// String strQType = (String) map.get("qtype");
		// BigDecimal times = (BigDecimal) map.get("times");
		// String strKey = strDate + "_" + strPlat + "_" + strQType;
		//
		// BigDecimal newsums = mapSums.get(strKey);
		// BigDecimal newtimes = times.multiply(new BigDecimal("100"));
		// BigDecimal ratio = newtimes.divide(newsums, 1,
		// BigDecimal.ROUND_HALF_UP);
		// map.put("ratio", ratio);
		// }

		model.put("rowlst", rowlst);
		return "/app_tuan/app_tuan_list";
	}

	/**
	 * 导出汇总信息 - excel
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param appId
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/exportProductExcel")
	public ModelAndView exportProductExcel(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//
		String order = request.getParameter("order");
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
		String goodsIds = request.getParameter("goodsIds");
		//
		List<Map<String, Object>> rowlst ;
		if(StringUtils.isBlank(goodsIds)){
			rowlst = appTuanService.queryProductList(startDate, endDate, order);
		}else{
			rowlst = appTuanService.queryProductList(startDate, endDate, goodsIds, order);
		}
		
		// 处理数据
		setData(rowlst);

		model.put("list", rowlst);

		return new ModelAndView(new MallProductStatExcelView(), model);
	}
	
	
	/**
	 * 导出单个商品信息信息 - excel
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param appId
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/exportSingleProductExcel")
	public ModelAndView exportSingleProductExcel(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//
		String tuanId = request.getParameter("tuan_id") ;
		String order = request.getParameter("order");
		String startDate = request.getParameter("startDate") ;
		String endDate = request.getParameter("endDate") ;
		
		List<Map<String, Object>> rowlst = appTuanService.querySingleProductList(startDate, endDate, tuanId, order);
		for (Map<String, Object> map : rowlst) {
			// Integer clicks = (Integer) map.get("clicks");
			Integer uids = (Integer) map.get("uids");

			Integer orders = (Integer) map.get("orders");
			Integer pays = (Integer) map.get("pays");
			BigDecimal orderRate = new BigDecimal(0);
			BigDecimal payRate = new BigDecimal(0);

			if (0 != uids) {
				BigDecimal bdOrders = new BigDecimal(orders * 100);
				// BigDecimal bdClicks = new BigDecimal(clicks);
				BigDecimal bdUids = new BigDecimal(uids);
				orderRate = bdOrders.divide(bdUids, 2, BigDecimal.ROUND_HALF_UP);
			}

			if (0 != pays && orders != 0) {
				BigDecimal bdPays = new BigDecimal(pays * 100);
				BigDecimal bdOrders = new BigDecimal(orders);
				payRate = bdPays.divide(bdOrders, BigDecimal.ROUND_HALF_UP);
			}

			map.put("orderRate", orderRate);
			map.put("payRate", payRate);
		}
		model.put("rowlst", rowlst);

		return new ModelAndView(new MallSingleProductStatExcelView(), model);
	}
	

	@RequestMapping(value = "/productList", method = RequestMethod.GET)
	public String productList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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
		String goodsIds = request.getParameter("goodsIds");
		String order = request.getParameter("order");

		if (null == plat || "".equals(plat)) {
			plat = "PRODUCT";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("goodsIds", goodsIds);
		model.put("plat", plat);
		model.put("order", order);
		System.out.println("order:" + order);
		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst ;
		if(StringUtils.isBlank(goodsIds)){
			rowlst = appTuanService.queryProductList(startDate, endDate, order);
		}else{
			rowlst = appTuanService.queryProductList(startDate, endDate, goodsIds, order);
		}

		// 处理数据
		setData(rowlst);

		model.put("rowlst", rowlst);
		return "/app_tuan/app_tuan_product_list";
	}

	private void setData(List<Map<String, Object>> rowlst) {
		// 商品名称
		List<Map<String, Object>> listGoods = dgGoodsService.getCustomerGoodsList();
		Map<Long, Map<String, Object>> mapGoods = new HashMap<Long, Map<String, Object>>();
		for (Map<String, Object> map : listGoods) {
			Integer gId = (Integer) map.get("id");
			mapGoods.put(Long.parseLong(gId.toString()), map);
		}
		//
		for (Map<String, Object> map : rowlst) {
			// Integer clicks = (Integer) map.get("clicks");
			BigDecimal uids = (BigDecimal) map.get("uids");
			BigDecimal orders = (BigDecimal) map.get("orders");
			BigDecimal pays = (BigDecimal) map.get("pays");
			BigDecimal orderRate = new BigDecimal(0);
			BigDecimal payRate = new BigDecimal(0);
			Long goodId = (Long) map.get("tuan_id");

			if (0 != uids.intValue()) {
				BigDecimal bdOrders = orders.multiply(new BigDecimal(100));
				// BigDecimal bdClicks = new BigDecimal(clicks);
				orderRate = bdOrders.divide(uids, 2, BigDecimal.ROUND_HALF_UP);
			}

			if (0 != pays.intValue() && orders.intValue() != 0) {
				BigDecimal bdPays = pays.multiply(new BigDecimal(100));
				payRate = bdPays.divide(orders, BigDecimal.ROUND_HALF_UP);
			}

			map.put("orderRate", orderRate);
			map.put("payRate", payRate);
			// 展示项：分类名、商家名称等
			String fCateName = "";
			String cateName = "";
			Integer storeId = 0;
			String storeName = "";
			Date sellFlagTime = null;
			BigDecimal dbMarketPrice = new BigDecimal(0);
			BigDecimal dbPrice = new BigDecimal(0);
			Integer marketPrice = 0;
			Integer price = 0;
			//
			Map<String, Object> tMap = mapGoods.get(goodId);
			if (tMap != null) {
				fCateName = (String) tMap.get("f_cate_name");
				cateName = (String) tMap.get("cate_name");
				storeId = (Integer) tMap.get("user_id");
				storeName = (String) tMap.get("store_name");
				marketPrice = (Integer) tMap.get("market_price");
				if (marketPrice != 0) {
					dbMarketPrice = new BigDecimal(marketPrice);
					dbMarketPrice = dbMarketPrice.divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
				}

				sellFlagTime = (Date) tMap.get("sellflagtime");
				price = (Integer) tMap.get("price");
				if (price != 0) {
					dbPrice = new BigDecimal(price);
					dbPrice = dbPrice.divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
				}
			}

			map.put("fCateName", fCateName);
			map.put("cateName", cateName);
			map.put("storeId", storeId);
			map.put("storeName", storeName);
			map.put("marketPrice", dbMarketPrice);
			map.put("sellFlagTime", sellFlagTime);
			map.put("price", dbPrice);
		}
	}

	@RequestMapping(value = "/productListOld", method = RequestMethod.GET)
	public String productList_old(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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
			plat = "PRODUCT";
		}

		//
		List<Map<String, Object>> listGoods = dgGoodsService.getCustomerGoodsList();
		Map<Long, Map<String, Object>> mapGoods = new HashMap<Long, Map<String, Object>>();
		for (Map<String, Object> map : listGoods) {
			Integer gId = (Integer) map.get("id");
			mapGoods.put(Long.parseLong(gId.toString()), map);
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("plat", plat);
		model.put("order", order);
		System.out.println("order:" + order);
		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst = appTuanService.queryProductList(startDate, endDate, order);
		for (Map<String, Object> map : rowlst) {
			// Integer clicks = (Integer) map.get("clicks");
			BigDecimal uids = (BigDecimal) map.get("uids");
			BigDecimal orders = (BigDecimal) map.get("orders");
			BigDecimal pays = (BigDecimal) map.get("pays");
			BigDecimal orderRate = new BigDecimal(0);
			BigDecimal payRate = new BigDecimal(0);
			Long goodId = (Long) map.get("tuan_id");

			if (0 != uids.intValue()) {
				BigDecimal bdOrders = orders.multiply(new BigDecimal(100));
				// BigDecimal bdClicks = new BigDecimal(clicks);
				orderRate = bdOrders.divide(uids, 2, BigDecimal.ROUND_HALF_UP);
			}

			if (0 != pays.intValue() && orders.intValue() != 0) {
				BigDecimal bdPays = pays.multiply(new BigDecimal(100));
				payRate = bdPays.divide(orders, BigDecimal.ROUND_HALF_UP);
			}

			map.put("orderRate", orderRate);
			map.put("payRate", payRate);
			// 展示项：分类名、商家名称等
			String fCateName = "";
			String cateName = "";
			Integer storeId = 0;
			String storeName = "";
			Integer marketPrice = 0;
			Date sellFlagTime = null;
			Integer price = 0;
			//
			Map<String, Object> tMap = mapGoods.get(goodId);
			if (tMap != null) {
				fCateName = (String) tMap.get("f_cate_name");
				cateName = (String) tMap.get("cate_name");
				storeId = (Integer) tMap.get("user_id");
				storeName = (String) tMap.get("store_name");
				marketPrice = (Integer) tMap.get("market_price");
				if (marketPrice != 0) {
					marketPrice = marketPrice / 100;
				}

				sellFlagTime = (Date) tMap.get("sellflagtime");
				price = (Integer) tMap.get("price");
				if (price != 0) {
					price = price / 100;
				}
			}

			map.put("fCateName", fCateName);
			map.put("cateName", cateName);
			map.put("storeId", storeId);
			map.put("storeName", storeName);
			map.put("marketPrice", marketPrice);
			map.put("sellFlagTime", sellFlagTime);
			map.put("price", price);
		}
		model.put("rowlst", rowlst);
		return "/app_tuan/app_tuan_product_list";
	}

	/**
	 * 商品点击统计
	 *
	 * @param appId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/singleProductList", method = RequestMethod.GET)
	public String singleProductList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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
		String tuanId = request.getParameter("tuanId");

		if (null == plat || "".equals(plat)) {
			plat = "PRODUCT";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("plat", plat);
		model.put("order", order);
		model.put("tuanId", tuanId);
		System.out.println("order:" + order);
		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst = appTuanService.querySingleProductList(startDate, endDate, tuanId, order);
		for (Map<String, Object> map : rowlst) {
			// Integer clicks = (Integer) map.get("clicks");
			Integer uids = (Integer) map.get("uids");

			Integer orders = (Integer) map.get("orders");
			Integer pays = (Integer) map.get("pays");
			BigDecimal orderRate = new BigDecimal(0);
			BigDecimal payRate = new BigDecimal(0);

			if (0 != uids) {
				BigDecimal bdOrders = new BigDecimal(orders * 100);
				// BigDecimal bdClicks = new BigDecimal(clicks);
				BigDecimal bdUids = new BigDecimal(uids);
				orderRate = bdOrders.divide(bdUids, 2, BigDecimal.ROUND_HALF_UP);
			}

			if (0 != pays && orders != 0) {
				BigDecimal bdPays = new BigDecimal(pays * 100);
				BigDecimal bdOrders = new BigDecimal(orders);
				payRate = bdPays.divide(bdOrders, BigDecimal.ROUND_HALF_UP);
			}

			map.put("orderRate", orderRate);
			map.put("payRate", payRate);
		}
		model.put("rowlst", rowlst);
		return "/app_tuan/app_tuan_single_product_list";
	}

	@RequestMapping(value = "/mallOrderDayStatList", method = RequestMethod.GET)
	public String mallOrderDayStatList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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

		if (null == plat || "".equals(plat)) {
			plat = "CHART";
		}

		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		model.put("plat", plat);

		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst = appTuanService.queryOrderSumList(startDate, endDate, "");

        for(Map<String,Object> map : rowlst){
            Integer detailUv = (Integer) map.get("detail_uv");
            Integer orders = (Integer) map.get("orders");
            Integer pays = (Integer) map.get("pays");
            BigDecimal dMoneys = (BigDecimal) map.get("moneys");
            Integer orderUV = (Integer) map.get("order_uv");
            BigDecimal dConpons = (BigDecimal) map.get("conpons");
            BigDecimal dSubsidys = (BigDecimal) map.get("subsidys");
            BigDecimal dComMoneys = (BigDecimal) map.get("com_moneys");

            BigDecimal payRate = new BigDecimal(0);
            BigDecimal conversionRate = new BigDecimal(0);// 成交单转化率
            BigDecimal dCustomerPrice = new BigDecimal(0);//客单价

            if (0 != pays) {
                BigDecimal bdPays = new BigDecimal(pays * 100);
                BigDecimal bdOrders = new BigDecimal(orders);
                payRate = bdPays.divide(bdOrders, BigDecimal.ROUND_HALF_UP);
            }
            //

            if (0 != detailUv.intValue()) {
                BigDecimal bdPays = new BigDecimal(pays * 100);
                //
                BigDecimal bdDetailUv = new BigDecimal(detailUv);
                conversionRate = bdPays.divide(bdDetailUv, 2, BigDecimal.ROUND_HALF_UP);
            }

            //
            try {
                dMoneys = dMoneys.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                e.printStackTrace();
                dMoneys = new BigDecimal(0);
            }
            //
            try {
                dConpons = dConpons.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                e.printStackTrace();
                dConpons = new BigDecimal(0);
            }
            //
            try {
                dSubsidys = dSubsidys.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                e.printStackTrace();
                dSubsidys = new BigDecimal(0);
            }
            //
            //
            if (0 != orderUV) {
                BigDecimal bdOrderUV = new BigDecimal(orderUV);
                //
                dCustomerPrice = dMoneys.divide(bdOrderUV, 2, BigDecimal.ROUND_HALF_UP);
            }

            //
            try {
                dComMoneys = dComMoneys.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                e.printStackTrace();
                dComMoneys = new BigDecimal(0);
            }

            map.put("moneys",dMoneys);
            map.put("payRate",payRate);
            map.put("conversionRate",conversionRate);
            map.put("customerPrice",dCustomerPrice);
            map.put("subsidys",dSubsidys);
            map.put("com_moneys",dComMoneys);
            map.put("conpons",dConpons);
        }


		model.put("rowlst", rowlst);
		return "/mall/order_sum/mall_order_day_stat_list";
	}
}