package com.douguo.dc.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.douguo.dc.model.ChannelDict;
import com.douguo.dc.service.ManageService;

@Controller
public class ManageController {

	@Autowired
	private ManageService manageService;

	@RequestMapping(value = "/manage/{menu}/index", method = RequestMethod.GET, produces = "application/html; charset=UTF-8")
	public String manageChannel(@PathVariable String menu, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		String appId = request.getParameter("appId");
		if (null == appId || appId.equals("")) {
			appId = "1";
		}
		model.put("globalAppid", appId);
		if (null == menu)
			return null;
		if (menu.trim().equals("channel")) {
			List<ChannelDict> productDicList = manageService.getChannelListByStatus(1);
			model.put("channels", productDicList);
			return "/manage/channel";
		} else if (menu.trim().equals("events")) {
			return "/manage/events";
		} else if (menu.trim().equals("events_list")) {
			return "/manage/events_list";
		} else if (menu.trim().equals("version")) {
			return "/manage/version";
		} else if (menu.trim().equals("version_list")) {
			return "/manage/version_list";
		} else if (menu.trim().equals("app")) {
			return "/manage/app";
		} else if (menu.trim().equals("app_list")) {
			return "/manage/app_list";
		}
		return "redirect:/overview.do";
	}

	@RequestMapping(value = "/manage/alone/channel/save")
	public String saveChannel(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");

		String name = (String) request.getParameter("name");
		String code = (String) request.getParameter("code");
		manageService.insertChannelDict(code, name);
		List<ChannelDict> productDicList = manageService.getChannelListByStatus(1);
		model.put("channels", productDicList);
		String appId = request.getParameter("appId");
		if (null == appId || appId.equals("")) {
			appId = "4";
		}
		model.put("globalAppid", appId);
		return "/manage/channel";
	}

	@RequestMapping(value = "/manage/alone/channel/delete", method = RequestMethod.GET, produces = "application/html; charset=UTF-8")
	public String deleteChannel(@RequestParam(value = "id", required = true) int id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		manageService.deleteChannelDict(id);
		String appId = request.getParameter("appId");
		if (null == appId || appId.equals("")) {
			appId = "1";
		}
		model.put("globalAppid", appId);
		return "/manage/channel";
	}

	@RequestMapping(value = "/manage/getapps", method = RequestMethod.POST)
	public void getApps(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String ret = this.manageService.getAppsByStatus("NORMAL");
		response.setContentType("application/json;charset=UTF-8");
		try {
			response.getWriter().write(ret);
		} catch (IOException e) {
		}
	}

	@RequestMapping(value = "/manage/{id}/{operate}", method = RequestMethod.POST)
	public void operate(@PathVariable String id, @PathVariable String operate, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (id.trim().equals("events")) {
			if (operate.trim().equals("list")) {
				/* Get the params */
				String sortname = request.getParameter("sortname");
				String sortorder = request.getParameter("sortorder");
				String rp = request.getParameter("rp");
				String pageNo = request.getParameter("page");

				/** 获取查询条件 */
				String qtype = request.getParameter("qtype");
				String query = request.getParameter("query");

				sortname = (sortname == null || sortname.equals("")) ? "event_code" : sortname;
				sortorder = (sortorder == null || sortorder.equals("")) ? "asc" : sortorder;
				int pagesize = (rp == null || rp.equals("")) ? 15 : Integer.parseInt(rp);
				int page_no = (pageNo == null || pageNo.equals("")) ? 1 : Integer.parseInt(pageNo);
				int startRow = (page_no - 1) * pagesize;

				String ret = this.manageService.getEventsListByStatus(1, sortname, sortorder, startRow, page_no,
						pagesize, qtype, query);

				response.setContentType("application/json;charset=UTF-8");
				try {
					response.getWriter().write(ret);
					response.flushBuffer();
				} catch (IOException e) {
				}
			} else if (operate.trim().equals("add")) {
				String JSON_SUCCESS = "{\"success\":\"true\"}";
				String JSON_ERROR = "{\"success\":\"false\",\"msg\":\"%s\"}";

				String event_name = request.getParameter("event_name");
				String event_code = request.getParameter("event_code");
				String app_id = request.getParameter("app_id");
				String status = request.getParameter("status");
                String is_mult = request.getParameter("is_mult");

				String msg = "";
				if (null == event_name || event_name.trim().equals("")) {
					msg += "事件名称为空，请输入正确事件名称！";
				}
				if (null == event_code || event_code.trim().equals("")) {
					msg += "事件代码为空，请输入正确事件代码！";
				}
				if (null == app_id || app_id.trim().equals("")) {
					msg += "事件所属应用为空，请选择正确事件所属应用！";
				}
				if (null == status || status.trim().equals("")) {
					msg += "事件状态为空，请选择正确事件状态！";
				}

				String ret = "";
				if (msg.equals("")) {
					//特殊处理：豆果 ios & android
					if("1".equals(is_mult)){
                        String newAppId = app_id;
                        if("3".equals(app_id)){
                            newAppId = "4";
                        }else if ("4".equals(app_id)){
                            newAppId = "3";
                        }
                        ret = this.manageService.addEventDict(event_name, event_code, newAppId, status);
					}

					ret = this.manageService.addEventDict(event_name, event_code, app_id, status);
				} else {
					ret = msg;
				}
				response.setContentType("application/json;charset=UTF-8");
				try {
					if (null != ret && ret.equals("success")) {
						response.getWriter().write(JSON_SUCCESS);
						response.flushBuffer();
					} else {
						response.getWriter().write(String.format(JSON_ERROR, ret));
						response.flushBuffer();
					}
				} catch (IOException e) {
				}
			} else if (operate.trim().equals("update")) {
				String JSON_SUCCESS = "{\"success\":\"true\"}";
				String JSON_ERROR = "{\"success\":\"false\",\"msg\":\"%s\"}";

				String idStr = request.getParameter("id");
				String event_name = request.getParameter("event_name");
				String event_code = request.getParameter("event_code");
				String app_id = request.getParameter("app_id");
				String status = request.getParameter("status");

				String msg = "";
				if (null == idStr || idStr.trim().equals("")) {
					msg += "获取事件ID失败，请重试！";
				}
				if (null == event_name || event_name.trim().equals("")) {
					msg += "事件名称为空，请输入正确事件名称！";
				}
				if (null == event_code || event_code.trim().equals("")) {
					msg += "事件代码为空，请输入正确事件代码！";
				}
				if (null == app_id || app_id.trim().equals("")) {
					msg += "事件所属应用为空，请选择正确事件所属应用！";
				}
				if (null == status || status.trim().equals("")) {
					msg += "事件状态为空，请选择正确事件状态！";
				}

				String ret = "";
				if (msg.equals("")) {
					ret = this.manageService.updateEventDict(idStr, event_name, event_code, app_id, status);
				} else {
					ret = msg;
				}
				response.setContentType("application/json;charset=UTF-8");
				try {
					if (null != ret && ret.equals("success")) {
						response.getWriter().write(JSON_SUCCESS);
						response.flushBuffer();
					} else {
						response.getWriter().write(String.format(JSON_ERROR, ret));
						response.flushBuffer();
					}
				} catch (IOException e) {
				}
			} else if (operate.trim().equals("delete")) {
				String JSON_SUCCESS = "{\"success\":\"true\"}";
				String JSON_ERROR = "{\"success\":\"false\",\"msg\":\"删除失败,后台异常\"}";
				String ids = request.getParameter("ids");

				response.setContentType("application/json;charset=UTF-8");
				try {
					if (null == ids || ids.trim().equals("")) {
						response.getWriter().write(JSON_SUCCESS);
					} else {
						boolean ret = this.manageService.deleteEventByIds(ids);
						if (ret) {
							response.getWriter().write(JSON_SUCCESS);
							response.flushBuffer();
						} else {
							response.getWriter().write(JSON_ERROR);
							response.flushBuffer();
						}
					}
				} catch (IOException e) {
				}
			}
		} else if (id.trim().equals("version")) {
			if (operate.trim().equals("list")) {
				/* Get the params */
				String sortname = request.getParameter("sortname");
				String sortorder = request.getParameter("sortorder");
				String rp = request.getParameter("rp");
				String pageNo = request.getParameter("page");

				/** 获取查询条件 */
				String qtype = request.getParameter("qtype");
				String query = request.getParameter("query");

				sortname = (sortname == null || sortname.equals("")) ? "event_code" : sortname;
				sortorder = (sortorder == null || sortorder.equals("")) ? "asc" : sortorder;
				int pagesize = (rp == null || rp.equals("")) ? 10 : Integer.parseInt(rp);
				int page_no = (pageNo == null || pageNo.equals("")) ? 1 : Integer.parseInt(pageNo);
				int startRow = (page_no - 1) * pagesize;

				String ret = this.manageService.getVersionListByStatus(1, sortname, sortorder, startRow, page_no,
						pagesize, qtype, query);

				response.setContentType("application/json;charset=UTF-8");
				try {
					response.getWriter().write(ret);
					response.flushBuffer();
				} catch (IOException e) {
				}
			} else if (operate.trim().equals("add")) {
				String JSON_SUCCESS = "{\"success\":\"true\"}";
				String JSON_ERROR = "{\"success\":\"false\",\"msg\":\"%s\"}";

				String app_id = request.getParameter("app_id");
				String app_version = request.getParameter("app_version");
				String status = request.getParameter("status");

				String msg = "";
				if (null == app_id || app_id.trim().equals("")) {
					msg += "应用名称为空，请输入正确应用名称！";
				}
				if (null == app_version || app_version.trim().equals("")) {
					msg += "版本号为空，请输入正确版本号！";
				}
				if (null == status || status.trim().equals("")) {
					msg += "版本状态为空，请选择正确版本状态！";
				}

				String ret = "";
				if (msg.equals("")) {
					ret = this.manageService.addVersionDict(app_id, app_version, status);
				} else {
					ret = msg;
				}
				response.setContentType("application/json;charset=UTF-8");
				try {
					if (null != ret && ret.equals("success")) {
						response.getWriter().write(JSON_SUCCESS);
						response.flushBuffer();
					} else {
						response.getWriter().write(String.format(JSON_ERROR, ret));
						response.flushBuffer();
					}
				} catch (IOException e) {
				}
			} else if (operate.trim().equals("update")) {
				String JSON_SUCCESS = "{\"success\":\"true\"}";
				String JSON_ERROR = "{\"success\":\"false\",\"msg\":\"%s\"}";

				String idStr = request.getParameter("id");
				String app_id = request.getParameter("app_id");
				String app_version = request.getParameter("app_version");
				String status = request.getParameter("status");

				String msg = "";
				if (null == idStr || idStr.trim().equals("")) {
					msg += "获取事件ID失败，请重试！";
				}
				if (null == app_id || app_id.trim().equals("")) {
					msg += "应用ID为空，请选择正确应用！";
				}
				if (null == app_version || app_version.trim().equals("")) {
					msg += "版本号为空，请输入正确版本号！";
				}
				if (null == status || status.trim().equals("")) {
					msg += "版本状态为空，请选择正确版本状态！";
				}

				String ret = "";
				if (msg.equals("")) {
					ret = this.manageService.updateVersionDict(idStr, app_id, app_version, status);
				} else {
					ret = msg;
				}
				response.setContentType("application/json;charset=UTF-8");
				try {
					if (null != ret && ret.equals("success")) {
						response.getWriter().write(JSON_SUCCESS);
						response.flushBuffer();
					} else {
						response.getWriter().write(String.format(JSON_ERROR, ret));
						response.flushBuffer();
					}
				} catch (IOException e) {
				}
			} else if (operate.trim().equals("delete")) {
				String JSON_SUCCESS = "{\"success\":\"true\"}";
				String JSON_ERROR = "{\"success\":\"false\",\"msg\":\"删除失败,后台异常\"}";
				String ids = request.getParameter("ids");

				response.setContentType("application/json;charset=UTF-8");
				try {
					if (null == ids || ids.trim().equals("")) {
						response.getWriter().write(JSON_SUCCESS);
					} else {
						boolean ret = this.manageService.deleteVersionByIds(ids);
						if (ret) {
							response.getWriter().write(JSON_SUCCESS);
							response.flushBuffer();
						} else {
							response.getWriter().write(JSON_ERROR);
							response.flushBuffer();
						}
					}
				} catch (IOException e) {
				}
			}
		} else if (id.trim().equals("app")) {
			if (operate.trim().equals("list")) {
				String sortname = request.getParameter("sortname");
				String sortorder = request.getParameter("sortorder");
				String rp = request.getParameter("rp");
				String pageNo = request.getParameter("page");

				/** 获取查询条件 */
				String qtype = request.getParameter("qtype");
				String query = request.getParameter("query");

				sortname = (sortname == null || sortname.equals("")) ? "id" : sortname;
				sortorder = (sortorder == null || sortorder.equals("")) ? "asc" : sortorder;
				int pagesize = (rp == null || rp.equals("")) ? 15 : Integer.parseInt(rp);
				int page_no = (pageNo == null || pageNo.equals("")) ? 1 : Integer.parseInt(pageNo);
				int startRow = (page_no - 1) * pagesize;

				String ret = this.manageService.getAppList(sortname, sortorder, startRow, page_no, pagesize, qtype,
						query);
				response.setContentType("application/json;charset=UTF-8");
				try {
					response.getWriter().write(ret);
					response.flushBuffer();
				} catch (IOException e) {
				}
			} else if (operate.trim().equals("add")) {
				String JSON_SUCCESS = "{\"success\":\"true\"}";
				String JSON_ERROR = "{\"success\":\"false\",\"msg\":\"%s\"}";

				String name = request.getParameter("name");
				String key = request.getParameter("key");
				String user_id = request.getParameter("user_id");

				String msg = "";
				if (null == name || name.trim().equals("")) {
					msg += "应用名称为空，请输入正确应用名称！";
				}
				if (null == key || key.trim().equals("")) {
					msg += "key为空，请输入正确key！";
				}
				if (null == user_id || user_id.trim().equals("")) {
					msg += "USER_ID为空，请输入正确的USER_ID！";
				}

				String ret = "";
				if (msg.equals("")) {
					ret = this.manageService.addAppDict(name, key, user_id);
				} else {
					ret = msg;
				}
				response.setContentType("application/json;charset=UTF-8");
				try {
					if (null != ret && ret.equals("success")) {
						response.getWriter().write(JSON_SUCCESS);
						response.flushBuffer();
					} else {
						response.getWriter().write(String.format(JSON_ERROR, ret));
						response.flushBuffer();
					}
				} catch (IOException e) {
				}
			} else if (operate.trim().equals("update")) {
				String JSON_SUCCESS = "{\"success\":\"true\"}";
				String JSON_ERROR = "{\"success\":\"false\",\"msg\":\"%s\"}";

				String idStr = request.getParameter("id");
				String name = request.getParameter("name");
				String key = request.getParameter("key");
				String user_id = request.getParameter("user_id");

				String msg = "";
				if (null == idStr || idStr.trim().equals("")) {
					msg += "获取应用ID失败，请重试！";
				}
				if (null == name || name.trim().equals("")) {
					msg += "应用名称为空，请输入正确应用名称！";
				}
				if (null == key || key.trim().equals("")) {
					msg += "key为空，请输入正确key！";
				}
				if (null == user_id || user_id.trim().equals("")) {
					msg += "USER_ID为空，请输入正确的USER_ID！";
				}

				String ret = "";
				if (msg.equals("")) {
					ret = this.manageService.updateAppDic(idStr, name, key, user_id);
				} else {
					ret = msg;
				}
				response.setContentType("application/json;charset=UTF-8");
				try {
					if (null != ret && ret.equals("success")) {
						response.getWriter().write(JSON_SUCCESS);
						response.flushBuffer();
					} else {
						response.getWriter().write(String.format(JSON_ERROR, ret));
						response.flushBuffer();
					}
				} catch (IOException e) {
				}
			} else if (operate.trim().equals("delete")) {
				String JSON_SUCCESS = "{\"success\":\"true\"}";
				String JSON_ERROR = "{\"success\":\"false\",\"msg\":\"删除失败,后台异常\"}";
				String ids = request.getParameter("ids");

				response.setContentType("application/json;charset=UTF-8");
				try {
					if (null == ids || ids.trim().equals("")) {
						response.getWriter().write(JSON_SUCCESS);
					} else {
						boolean ret = this.manageService.deleteAppByIds(ids);
						if (ret) {
							response.getWriter().write(JSON_SUCCESS);
							response.flushBuffer();
						} else {
							response.getWriter().write(JSON_ERROR);
							response.flushBuffer();
						}
					}
				} catch (IOException e) {
				}
			}
		} else {

		}
	}
}