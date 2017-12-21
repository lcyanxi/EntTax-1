package com.douguo.dc.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseBody;

import com.douguo.dc.model.SearchemptyModel;
import com.douguo.dc.model.AppModel;
import com.douguo.dc.model.Pager;
import com.douguo.dc.service.SearchService;
import com.douguo.dc.util.TypeUtil;
import com.douguo.dc.util.DateUtil;

@Controller
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private SearchService SearchService;
	
	
	private Map<Integer,String> getClienttype(){
		Map<Integer,String> clienttype = new HashMap<Integer,String>();
		clienttype.put(1,"网站"); 
		clienttype.put(2,"客户端"); 
		return clienttype;
	}
	private Map<Integer,String> getType(){
		Map<Integer,String> type = new HashMap<Integer,String>();
		type.put(1,"标签搜索"); 
		type.put(2,"食材搜索");
		type.put(3,"关键字搜索");
		return type;
	}

	public SearchController() {
		// TODO Auto-generated constructor stub
	}
	@RequestMapping(value = "/emptyList", method = RequestMethod.GET)
	public String common(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
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
		String Getclienttypeid = request.getParameter("clienttypeid");
		if (null == Getclienttypeid || Getclienttypeid.equals("")) {
			Getclienttypeid = "0";
		}
		Integer clienttypeid = Integer.valueOf(Getclienttypeid);
		System.out.println(clienttypeid+'-'+request.getParameter("clienttypeid"));
		model.put("globalAppid", appId);
		model.put("startDate", startDate);
		model.put("clienttypeid", clienttypeid);
		/** 获取app应用列表数据 */
		List<Map<String, Object>> rowlst = this.SearchService.getEmptyList(startDate, clienttypeid);
		Map<Integer, String> clienttype = this.getClienttype();
		//Map<Integer, String> type = (List<map<Integer, String>>)this.getType();
		//System.out.println(TypeUtil.typeToString("rowlst：",rowlst));
		request.getSession().setAttribute("rowlst", rowlst);
		request.getSession().setAttribute("clienttype", clienttype);
		//request.getSession().setAttribute("type", type);
		return "/search/emptyList";
	}
}
