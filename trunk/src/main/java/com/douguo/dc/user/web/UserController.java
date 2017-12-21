package com.douguo.dc.user.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.douguo.dc.common.web.BaseController;
import com.douguo.dc.model.AppModel;
import com.douguo.dc.model.Pager;
import com.douguo.dc.service.OverviewService;
import com.douguo.dc.user.model.User;
import com.douguo.dc.user.model.UserFunction;
import com.douguo.dc.user.service.UserFunctionService;
import com.douguo.dc.user.service.UserMenuService;
import com.douguo.dc.user.service.UserService;
import com.douguo.dc.user.utils.MD5Util;
import com.douguo.dc.user.utils.UserInfoUtil;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;
import com.douguo.dc.util.JavaSendMail;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

    private final static Log logger = LogFactory.getLog(UserController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private OverviewService overviewService;

	@Autowired
	private UserMenuService userMenuService;
	
	@Autowired
	private UserFunctionService userFunctionService ;

	/**
	 * 事件列表页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param userID
	 * @param pwd
	 * @return
	 */
	@RequestMapping(value = "/login")
	public String dashboard(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			@RequestParam(value = "uid", required = false, defaultValue = "") String userID,
			@RequestParam(value = "pwd", required = false, defaultValue = "") String pwd) {

		logger.info(userID + " is login");

		if (userID != null && userID.trim().length() > 0 && pwd != null && pwd.length() > 0) {
			try {
				// String[] data =
				// con.queryRow("SELECT userID,type,groupID,userName FROM userinfo where userID=? ",
				// new String[] { userID });
				User user = userService.getUser(userID);
				String pass = user.getPass();
				pwd = MD5Util.MD5(pwd);
				if (user != null && pass.equals(pwd)) {

					List<Map<String, Object>> listFunction = userMenuService.queryUserFunction(userID);

					user.setFunctions(listFunction);

					request.getSession().setAttribute(UserInfoUtil.GLOBAL_SESSION_USR_CACHE_KEY, user);
					Pager<AppModel> pager = overviewService.getApps("NORMAL");
					request.getSession().setAttribute("apps", pager);
					
					// 设置登陆cookie
					userInfoUtil.setCookies(request, response, userID);

					return "/../../main";
				} else {
					return "/../../login";
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				return "/../../login";
			}
		} else {
			model.put("uid", userID);
			return "/../../login";
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param uid
	 * @param pass
	 * @return
	 */
	@RequestMapping(value = "/editPass")
	public String editPass(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			@RequestParam(value = "uid", required = false, defaultValue = "") String uid,
			@RequestParam(value = "pass", required = false, defaultValue = "") String pass) {
		// @ TODO 暂时不处理失败的情况
		User obj = (User) request.getSession().getAttribute(UserInfoUtil.GLOBAL_SESSION_USR_CACHE_KEY);
		boolean isOk = userService.updateUserPass(obj.getUid(), pass);
		return "/../../common/editPass";
	}

	@RequestMapping(value = "/manager")
	public String manager(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		String selID = request.getParameter("selID"); // 要查询的用户的ID
		model.put("selID", selID);
		return "/admin/userManage";
	}

	@RequestMapping(value = "/insert")
	public String insert(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String uid = request.getParameter("uid");
		if (uid != null && uid.trim().length() > 0) { // 判断添加用户ID是否为空
			User user = userService.getUser(uid);
			// 判断要添加的userID是否已经存在
			if (user == null) {
				user = new User();
				user.setUid(uid);
				user.setPass(request.getParameter("pass"));
				user.setUsername(request.getParameter("userName"));
				boolean isOk = userService.insert(user);
				if (isOk) {
					model.put("msg", "添加成功");
					
					//给新用户发送邮件
					String subject = "后台新用户开通通知" ;
					String to = uid+"@douguo.com" ;
					StringBuffer content = new StringBuffer("Hi~ 亲爱哒 <b>"+request.getParameter("userName")+"</b> , 终于等到你~</br>"
							+ "DataCenter数据中心已经成功给你开通了dc后台账户, "
							+ "账户名：<b>"+uid+"</b>  初始密码：<b>"+request.getParameter("pass")+"</b></br>"
							+ "快登陆看看吧：http://dc.douguo.net/</br>记得修改密码哦~") ;
					try {
						JavaSendMail.sendHtmlEmail(subject, to, content.toString()) ;
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else {
					model.put("msg", "添加失败");
				}

			} else
				model.put("msg", uid + "此用户ID已存在，请重新输入！");
		} else {
			model.put("msg", uid + "此用户ID不能为空");
		}
		return "/admin/userManage";
	}
	
	

	@RequestMapping(value = "/edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String uid = request.getParameter("uid");
		String username = request.getParameter("username");
		if (uid != null && uid.trim().length() > 0) {
			User user = userService.getUser(uid);
			String[][] re = new String[1][2];
			re[0][0] = uid;
			re[0][1] = username;

			if (null == user) {
				user = new User();
				user.setUid(uid);
				user.setPass(uid);
				user.setUsername(username);
				userService.insert(user);
			} else {
				user.setUid(uid);
				user.setUsername(username);
				userService.update(user);
			}

		} else {
			// out.println("用户id不能为空！");
		}

		return "/admin/userManage";
	}

	@RequestMapping(value = "/del")
	public String del(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		String uid = request.getParameter("uid");
		userService.del(uid);
		return "/admin/userManage";
	}

	@RequestMapping(value = "/queryJson")
	public String queryJson(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		String uid = request.getParameter("uid");

		String selID = request.getParameter("selID"); // 要查询的用户的ID
		if (selID.equals("null"))
			selID = null;

		List<User> listUser = userService.queryUserList(selID);
		String[][] arryFun = new String[listUser.size()][2];

		for (int i = 0; i < listUser.size(); i++) // 将查询出来的数据添加到要显示的结果集中
		{
			String tmpUid = listUser.get(i).getUid();
			String username = listUser.get(i).getUsername();
			arryFun[i] = new String[] {
					tmpUid,
					username,
					"<a href='/user/preEdit.do?uid=" + tmpUid + "' title='编辑' >编辑</a>",
					"<a href='/user/del.do?uid=" + tmpUid + "'>删除</a>",
					"<a href='#' onclick=\"showUrlInDialog('/user/preCopy.do?action=copy&uid=" + tmpUid
							+ "')\" title='拷贝' >拷贝(待完善)</a>",
					"<a href='/userfun/preManager.do?uid=" + tmpUid + "' title='授权' >授权</a>" };
		}

		String rowsLimit = request.getParameter("rows"); // 获取jqgrid没页要显示的行数
		if (null == rowsLimit) // 设置rowsLimit的默认值
			rowsLimit = "10";
		JQGridUtil t = new JQGridUtil();
		int nPage = 1; // jqgrid要显示的当前页面
		int records = listUser.size(); // 要显示的记录总行数
		int total = records / Integer.parseInt(rowsLimit) + 1; // 计算总的页数
		// 行数据
		List<Map> rows = new ArrayList<Map>();

		for (String[] axx : arryFun) {
			Map map = new HashMap();
			map.put("id", axx[0]);
			map.put("cell", axx);

			rows.add(map);
		}

		// rows
		GridPager<Map> gridPager = new GridPager<Map>(nPage, total, records, rows);
		t.toJson(gridPager, response);

		userService.del(uid);
		return "/admin/userManage";
	}

	/**
	 * 修改页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "/preEdit")
	public String preEdit(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			@RequestParam(value = "uid", required = false, defaultValue = "") String uid) {
		//
		User user = userService.getUser(uid);
		model.put("user", user);
		return "/admin/userManageEdit";
	}
	
	/**
	 * 修改页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/preEditPass")
	public String preEditPass(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//
		User user = (User)request.getSession().getAttribute(UserInfoUtil.GLOBAL_SESSION_USR_CACHE_KEY);
		//User user = userService.getUser(uid);
		model.put("user", user);
		return "/../../common/editPass";
	}

	/**
	 * 复制页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param uid
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/preCopy")
	public String preCopy(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			@RequestParam(value = "uid", required = false, defaultValue = "") String uid) throws IOException {

		model.put("uid", uid);
		return "/admin/userManageCopy";
	}

	/**
	 * 复制页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/copy")
	public void copy(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {
		
		List<UserFunction> userFromList = new ArrayList<UserFunction>() ;
		HashSet<String> userFromFunctionList = new HashSet<String>() ;
		List<UserFunction> userToList = new ArrayList<UserFunction>() ;
		HashSet<String> userToFunctionList = new HashSet<String>() ;
		HashSet<String> functionIdToDel = new HashSet<String>();
		List<String> userFunctionAfterDel = new ArrayList<String>() ;
		HashSet<UserFunction> ufParamSet = new HashSet<UserFunction>() ;
		HashSet<String> functionIdAll = new HashSet<String>();
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		String uidFrom = request.getParameter("uid");
		String uidTo = request.getParameter("uid_to");

		if (uidFrom == null || uidTo == null || uidFrom.trim().length() == 0 || 
			uidTo.trim().length() == 0 || uidFrom.equals(uidTo)) // check拷贝和被拷贝用户是否为空
		{
			response.getWriter()
			.println(
					"<div style=\"padding-left:50px;padding-right:50px\" ><div class='ui-state-highlight ui-corner-all' style='margin-top: 20px; padding: 0 .7em;'><p><span class='ui-icon ui-icon-info' style='float: left; margin-right: .3em;'></span>拷贝失败！输入不正确！</p></div></div>"
					);
		} else {
			
			User user = userService.getUser(uidFrom);
			User user_to = userService.getUser(uidTo);
			
			if (user != null && user_to !=null) // 判断拷贝、被拷贝的用户是否存在
			{
				
				userFromList = userFunctionService.queryUserFunctionByUid(uidFrom) ;
				userToList = userFunctionService.queryUserFunctionByUid(uidTo) ;
				
				for(UserFunction uf_from : userFromList){
					userFromFunctionList.add(uf_from.getFunctionId()) ;
				}
				
				for(UserFunction uf_to : userToList){
					userToFunctionList.add(uf_to.getFunctionId()) ;
				}
				
				for(String a : userFromFunctionList){
					for(String b : userToFunctionList){
						if(a.equals(b)){
							functionIdToDel.add(b);
						}
					}
				}
				//set转list
				List<String> temp_afterDel = new ArrayList<String>() ;
				temp_afterDel.addAll(userFromFunctionList) ;
				userFunctionAfterDel = temp_afterDel ;
				//移除相同权限
				if(functionIdToDel.size()>0){
					for(String c : functionIdToDel){
						userFunctionAfterDel.remove(c) ;
					}
				}
				
				if(userFunctionAfterDel.size()==0){
					response.getWriter()
					.println(
					"<div style=\"padding-left:50px;padding-right:50px\" ><div class='ui-state-highlight ui-corner-all' style='margin-top: 20px; padding: 0 .7em;'><p><span class='ui-icon ui-icon-info' style='float: left; margin-right: .3em;'></span>与被授权用户权限相同！</p></div></div>");
				} else {
					//uid'functionId存于list以适配方法
					for(String tempFid : userFunctionAfterDel){
						UserFunction ufParam = new UserFunction() ;
						ufParam.setUid(uidTo);
						ufParam.setFunctionId(tempFid);
						ufParamSet.add(ufParam) ;
					}
					
					//set转list
					List<UserFunction> temp = new ArrayList<UserFunction>() ;
					temp.addAll(ufParamSet) ;
					//执行sql
					userFunctionService.batchInsert(temp) ;
					
					response.getWriter()
					.println(
							"<div style=\"padding-left:50px;padding-right:50px\" ><div class='ui-state-highlight ui-corner-all' style='margin-top: 20px; padding: 0 .7em;'><p><span class='ui-icon ui-icon-info' style='float: left; margin-right: .3em;'></span>拷贝成功</p></div></div>");
				}
				
			} else {
				response.getWriter()
				.println(
						"<div style=\"padding-left:50px;padding-right:50px\" ><div class='ui-state-highlight ui-corner-all' style='margin-top: 20px; padding: 0 .7em;'><p><span class='ui-icon ui-icon-info' style='float: left; margin-right: .3em;'></span>"+ "用户 " + uidFrom+ " 或 " + uidTo + " 不存在！" +"</p></div></div>");
			}

		}
	}

}
