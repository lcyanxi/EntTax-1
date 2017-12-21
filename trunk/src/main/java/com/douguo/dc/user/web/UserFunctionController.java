package com.douguo.dc.user.web;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.douguo.dc.user.model.UserFunction;
import com.douguo.dc.user.service.MenuService;
import com.douguo.dc.user.service.UserFunctionService;
import com.douguo.dc.user.service.UserService;

@Controller
@RequestMapping("/userfun")
public class UserFunctionController {

	@Autowired
	private UserFunctionService userFunctionService;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 展示页页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param appId
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/preManager")
	public String preManager(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//
		String uid = request.getParameter("uid"); // 要查询的用户的ID
		model.put("uid", uid);
		return "/admin/userFunction";
	}

	@RequestMapping(value = "/save")
	public String save(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		request.setCharacterEncoding("utf-8");
		String strTreeIds = (String) request.getParameter("tree_ids");
		String strUserId = (String) request.getParameter("uid");

		System.out.println("*******" + strTreeIds);

		// List list = new ArrayList();

		try {

			// 先删除原来的权限
			userFunctionService.delByUid(strUserId);

			// if (strTreeIds != null && !"".equals(strTreeIds)) {
			// String[] arryTreeIds = strTreeIds.split(",");
			// String[][] re = new String[arryTreeIds.length][2];
			// for (int i = 0; i < arryTreeIds.length; i++) {
			// re[i][0] = strUserId;
			// re[i][1] = arryTreeIds[i];
			// }
			// int[] data = con.exec(strSQL, re);
			// }
			List<UserFunction> listUserFun = new ArrayList<UserFunction>();

			if (strTreeIds != null && !"".equals(strTreeIds)) {
				String[] arryTreeIds = strTreeIds.split(",");
				// String[][] re = new String[arryTreeIds.length][2];

				for (int i = 0; i < arryTreeIds.length; i++) {
					// re[i][0] = strUserId;
					// re[i][1] = arryTreeIds[i];

					UserFunction uf = new UserFunction();
					uf.setUid(strUserId);
					uf.setFunctionId(arryTreeIds[i]);
					listUserFun.add(uf);
				}
				boolean isOk = userFunctionService.batchInsert(listUserFun);
				// int[] data = con.exec(strSQL, re);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// response.sendRedirect("userFunction.jsp");
		// response.sendRedirect("userManage.jsp");

		// /////////

		return "/admin/userManage";
	}
	
	@RequestMapping(value = "/authorize")
	public String authorize(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		
		String authorize_funtion_id = request.getParameter("authorize_funtion_id") ;
		String authorize_funtion_id_toAdd = request.getParameter("authorize_funtion_id_toAdd") ;
		
		boolean a = userFunctionService.addUserFun(authorize_funtion_id, authorize_funtion_id_toAdd) ;
		
		return "/admin/userManage";
	}

}
