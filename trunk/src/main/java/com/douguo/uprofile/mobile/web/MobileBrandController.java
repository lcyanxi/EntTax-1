package com.douguo.uprofile.mobile.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;
import com.douguo.uprofile.mobile.model.MobileBrand;
import com.douguo.uprofile.mobile.service.MobileBrandService;

@Controller
@RequestMapping("/mobile/brand")
public class MobileBrandController {

	@Autowired
	private MobileBrandService mobileBrandService;

	@RequestMapping(value = "/queryJson")
	public void queryJson(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {

		request.setCharacterEncoding("utf-8");
		String rowsLimit = request.getParameter("rows"); // 取出每一页显示的行数
		String sidx = request.getParameter("sidx"); // 取出排序的项
		String sord = request.getParameter("sord"); // 取出排序方式：升序，降序

		if (rowsLimit == null) // 设置每一页显示行数的默认值
		{
			rowsLimit = "10";
		}
		JQGridUtil t = new JQGridUtil();
		int nPage = 1; // 当前显示的页数
		int total = 1; // 要显示的总的页数，初始值为1

		List<MobileBrand> list = mobileBrandService.queryAll();
		
		// int records = data.length;
		int records = list.size();

		total = records / Integer.parseInt(rowsLimit) + 1; // 计算总的页数
		String[][] arryFun = new String[list.size()][];

		for (int i = 0; i < list.size(); i++) {
			MobileBrand fun = list.get(i);
			arryFun[i] = new String[] { String.valueOf(fun.getId()), fun.getBrandName(), fun.getBrandEnName(),
					 fun.getBrandDesc(), fun.getCompany(), String.valueOf(fun.getSortNo()),
					"<a href='/mobile/brand/preEdit.do?id=" + fun.getId() + "' title='修改' >修改</a>" };
		}

		// 行数据
		List<Map> rows = new ArrayList<Map>();
		for (String[] axx : arryFun) {
			Map map = new HashMap();
			map.put("id", "1");
			map.put("cell", axx);

			rows.add(map);
		}

		// rows
		GridPager<Map> gridPager = new GridPager<Map>(nPage, total, records, rows); // 将表格显示的配置初始化给GridPager
		t.toJson(gridPager, response); // 发送json数据

	}

	/**
	 * 管理页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param appId
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/preList")
	public String preList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "/mobile/brand/mobile_brand_manager";
	}

	/**
	 * 修改页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param appId
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/preEdit")
	public String preEdit(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String id = request.getParameter("id");
		MobileBrand mobileBrand = mobileBrandService.getMobileBrand(id) ;
		model.put("mobileBrand", mobileBrand);
		return "/mobile/brand/mobile_brand_modify";
	}

	@RequestMapping(value = "/insert")
	public String insert(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		
		MobileBrand mobileBrand = new MobileBrand() ;
		
		String brandName = request.getParameter("brandName") ;
		String brandEnName = request.getParameter("brandEnName") ;
		Integer sortNo = Integer.valueOf(request.getParameter("sortNo")) ;
		String brandDesc = request.getParameter("brandDesc") ;
		String company = request.getParameter("company") ;
		
		mobileBrand.setBrandName(brandName);
		mobileBrand.setBrandEnName(brandEnName);
		mobileBrand.setSortNo(sortNo);
		mobileBrand.setBrandDesc(brandDesc);
		mobileBrand.setCompany(company);

		mobileBrandService.insert(mobileBrand);
		
		return "/mobile/brand/mobile_brand_manager";
	}

	@RequestMapping(value = "/update")
	public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String id = request.getParameter("id");
		String brandName = request.getParameter("brandName") ;
		String brandEnName = request.getParameter("brandEnName") ;
		Integer sortNo = Integer.valueOf(request.getParameter("sortNo")) ;
		String brandDesc = request.getParameter("brandDesc") ;
		String company = request.getParameter("company") ;

		if (id != null && id.trim().length() > 0) {
			MobileBrand mobileBrand = mobileBrandService.getMobileBrand(id);

			mobileBrand.setId(Integer.valueOf(id));
				
			mobileBrand.setBrandName(brandName);
			mobileBrand.setBrandEnName(brandEnName);
			mobileBrand.setSortNo(sortNo);
			mobileBrand.setBrandDesc(brandDesc);
			mobileBrand.setCompany(company);

			mobileBrandService.update(mobileBrand);
			
			return "/mobile/brand/mobile_brand_manager";
		} else {
			return "/mobile/brand/mobile_brand_modify";
		}
	}
}