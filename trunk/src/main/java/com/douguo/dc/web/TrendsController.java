package com.douguo.dc.web;

import com.douguo.dc.applog.dao.AppUserNewDao;
import com.douguo.dc.service.TrendsService;
import com.douguo.dc.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/trends")
public class TrendsController {
    private TrendsService trendsService;

    @Autowired
    private AppUserNewDao appUserNewDao;

    @Autowired
    public void setTrendsService(TrendsService trendsService) {
        this.trendsService = trendsService;
    }

    /**
     * 新增用户页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/newuser")
    public String installation(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        /** 设置页面访问新增用户的起始时间 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String start_date = DateUtil.getSpecifiedDayBefore(today, 6);
        String end_date = today;
        start_date = start_date.replaceAll("-", ".");
        end_date = end_date.replaceAll("-", ".");
        model.put("start_date", start_date);
        model.put("end_date", end_date);

        String appId = request.getParameter("appId");
        if (null == appId || appId.equals("")) {
            appId = "4";
        }
        model.put("globalAppid", appId);
        return "/app_trend/new_user";
    }

    @RequestMapping(value = "/newuserdetail", method = RequestMethod.GET)
    public String appDauList(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                             HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        if (null == appId || appId.equals("")) {
            appId = "4";
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

        String app = request.getParameter("app");
        String channel = request.getParameter("channel");
        String vers = request.getParameter("vers");

        if (null == app || "".equals(app)) {
            app = "4";
        }

        model.put("globalAppid", appId);
        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("app", app);

        /** 获取app应用列表数据 */
        List<Map<String, Object>> rowlist = appUserNewDao.queryChannelList(startDate, endDate, appId);
        // List<Map<String, Object>> rowlst =
        // appDauService.queryChannelListByApp(startDate, endDate,
        // app,channel,vers);

        model.put("rowlst", rowlist);
        return "/app_trend/new_user_detail_list";
    }

    /**
     * 活跃用户页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/activeuser")
    public String active(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        /** 设置页面访问活跃用户的起始时间 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String start_date = DateUtil.getSpecifiedDayBefore(today, 6);
        String end_date = today;
        start_date = start_date.replaceAll("-", ".");
        end_date = end_date.replaceAll("-", ".");
        model.put("start_date", start_date);
        model.put("end_date", end_date);

        String appId = request.getParameter("appId");
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        model.put("globalAppid", appId);
        return "/app_trend/active_user";
    }

    /**
     * 用户留存率页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/retention", method = RequestMethod.GET)
    public String retention(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        /** 设置页面访问用户留存率的起始时间 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String start_date = DateUtil.getSpecifiedDayBefore(today, 6);
        String end_date = today;
        start_date = start_date.replaceAll("-", ".");
        end_date = end_date.replaceAll("-", ".");
        model.put("start_date", start_date);
        model.put("end_date", end_date);
        String appId = request.getParameter("appId");
        String client = request.getParameter("client");
        if (null == appId || appId.equals("")) {
            appId = "4";
        }

        if (StringUtils.isNotEmpty(client)) {
            client = "4";
        }

        model.put("globalAppid", appId);
        model.put("client", client);
        /** 返回到用户留存页面 */
        return "/app_trend/user_retention";
    }

    /**
     * 用户原始渠道-留存率页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/originRetention", method = RequestMethod.GET)
    public String originRetention(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        /** 设置页面访问用户留存率的起始时间 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String start_date = DateUtil.getSpecifiedDayBefore(today, 6);
        String end_date = today;
        start_date = start_date.replaceAll("-", ".");
        end_date = end_date.replaceAll("-", ".");
        model.put("start_date", start_date);
        model.put("end_date", end_date);
        String appId = request.getParameter("appId");
        String client = request.getParameter("client");
        if (null == appId || appId.equals("")) {
            appId = "4";
        }

        if (StringUtils.isNotEmpty(client)) {
            client = "4";
        }

        model.put("globalAppid", appId);
        model.put("client", client);
        /** 返回到用户留存页面 */
        return "/app_trend/origin_user_retention";
    }

    /**
     * 电商用户-周留存率页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/mall_user_week_retention", method = RequestMethod.GET)
    public String mallUserWeekRetention(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        /** 设置页面访问用户留存率的起始时间 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String start_date = DateUtil.getSpecifiedDayBefore(today, 6);
        String end_date = today;
        start_date = start_date.replaceAll("-", ".");
        end_date = end_date.replaceAll("-", ".");
        model.put("start_date", start_date);
        model.put("end_date", end_date);
        String appId = request.getParameter("appId");
        String client = request.getParameter("client");
        if (null == appId || appId.equals("")) {
            appId = "4";
        }

        if (StringUtils.isNotEmpty(client)) {
            client = "4";
        }

        model.put("globalAppid", appId);
        model.put("client", client);
        
        System.out.println("Here=============================");
        
        /** 返回到用户留存页面 */
        return "/app_trend/mall_user_week_retention";
    }

    /**
     * 启动次数页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/launch")
    public String launch(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        /** 设置页面访问启动次数的起始时间 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String start_date = DateUtil.getSpecifiedDayBefore(today, 6);
        String end_date = today;
        start_date = start_date.replaceAll("-", ".");
        end_date = end_date.replaceAll("-", ".");
        model.put("start_date", start_date);
        model.put("end_date", end_date);

        String appId = request.getParameter("appId");
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        model.put("globalAppid", appId);
        return "/app_trend/launch_times";
    }

    /**
     * 获取版本信息列表json数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/load_versions", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String getVersions(HttpServletRequest request, HttpServletResponse response) {
        String appId = request.getParameter("app_id");
        if (null == appId || appId.equalsIgnoreCase("")) {
            appId = "1";
        }

        response.setHeader("Content-Type", "application/json; charset=utf-8");
        String ret = this.trendsService.getVersions(appId);
        return ret;
    }

    /**
     * 获取渠道列表json数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/load_channels", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String getChannels(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        String ret = this.trendsService.getChannels();
        return ret;
    }

    /**
     * 加载图表数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/load_chart_data", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String loadChartData(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        String ret = "";

        String appId = request.getParameter("app_id");
        if (null == appId || appId.equalsIgnoreCase("")) {
            appId = "4";
        }

        String is_compared = request.getParameter("is_compared");
        if (null == is_compared || is_compared.trim().equalsIgnoreCase("")) {
            is_compared = "false";
        }
        String original_data_count = request.getParameter("original_data_count");
        if (null == original_data_count || original_data_count.trim().equalsIgnoreCase("")) {
            original_data_count = "0";
        }

        String stats = request.getParameter("stats");
        String time_unit = request.getParameter("time_unit");
        if (null == time_unit || time_unit.trim().equals("")) {
            return ret;
        }

        if (null == stats || stats.trim().equals("")) {
            return ret;
        }

        String endDate = request.getParameter("end_date");
        String startDate = request.getParameter("start_date");

        String channels = request.getParameter("channels[]");
        if (null != channels && !channels.equals("")) {
            try {
                channels = URLDecoder.decode(channels, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                channels = "";
                e.printStackTrace();
            }
        }
        String versions = request.getParameter("versions[]");
        if (null != versions && !versions.equals("")) {
            try {
                versions = URLDecoder.decode(versions, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                versions = "";
                e.printStackTrace();
            }
        }

        String dimension_type = "";
        String dimension_name = "";
        if ((null != channels && null != versions) && (!channels.trim().equals("") && !versions.trim().equals(""))) {
            dimension_type = "CHANNELID_VERSION";
            dimension_name = channels + "_" + versions;
        } else if ((null == channels || channels.trim().equals(""))
                && (null != versions && !versions.trim().equals(""))) {
            dimension_type = "VERSION";
            dimension_name = versions;
        } else if ((null == versions || versions.trim().equals(""))
                && (null != channels && !channels.trim().equals(""))) {
            // dimension_type = "CHANNELID";
            dimension_type = "CHANNEL";
            dimension_name = channels;
        } else {
            dimension_type = "ALL";
            dimension_name = "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (time_unit.trim().equalsIgnoreCase("daily")) {
            String time_type = "TODAY";
            if (null == startDate || null == endDate || startDate.trim().equals("") || endDate.trim().equals("")) {
                String today = sdf.format(System.currentTimeMillis());
                startDate = DateUtil.getSpecifiedDayBefore(today, 7);
                endDate = DateUtil.getSpecifiedDayBefore(today, 1);
            } else {
                startDate = DateUtil.getSpecifiedDayBefore(startDate, 1);
                endDate = DateUtil.getSpecifiedDayBefore(endDate, 1);
            }
            if (stats.equalsIgnoreCase("installations")) {//新增用
                startDate = DateUtil.getSpecifiedDayAfter(startDate, 1);
//				endDate = DateUtil.getSpecifiedDayAfter(endDate, 1);
                ret = trendsService.getNewUserTrends(appId, is_compared, endDate, startDate, time_unit, time_type,
                        dimension_type, dimension_name);
            } else if (stats.equalsIgnoreCase("active_users")) {
                ret = trendsService.getActiveUserTrends(appId, is_compared, endDate, startDate, time_unit, time_type,
                        dimension_type, dimension_name);
            } else if (stats.equalsIgnoreCase("launches")) {
                ret = trendsService.getLaunchesTrends(appId, is_compared, endDate, startDate, time_unit, time_type,
                        dimension_type, dimension_name);
            }
        } else if (time_unit.trim().equalsIgnoreCase("hourly")) {
            // String time_type = "HOUR";
            if (stats.equalsIgnoreCase("installations")) {
                /** 新增用户是按天统计的，无小时数据 */
            } else if (stats.equalsIgnoreCase("active_users")) {
                //
            } else if (stats.equalsIgnoreCase("launches")) {
                //
            }
        } else if (time_unit.trim().equalsIgnoreCase("weekly")) {
            String time_type = "TODAY";
            if (null == startDate || null == endDate || startDate.trim().equals("") || endDate.trim().equals("")) {
                String today = sdf.format(System.currentTimeMillis());
                startDate = DateUtil.getSpecifiedDayBefore(today, 7);
                endDate = DateUtil.getSpecifiedDayBefore(today, 1);
            }
            if (stats.equalsIgnoreCase("installations")) {
                ret = trendsService.getNewUserTrendsWeek(appId, endDate, startDate, time_unit, time_type,
                        dimension_type, dimension_name);
            }
        } else if (time_unit.trim().equalsIgnoreCase("monthly")) {
            String time_type = "TODAY";
            if (null == startDate || null == endDate || startDate.trim().equals("") || endDate.trim().equals("")) {
                String today = sdf.format(System.currentTimeMillis());
                startDate = DateUtil.getSpecifiedDayBefore(today, 31);
                endDate = DateUtil.getSpecifiedDayBefore(today, 1);
            }
            if (stats.equalsIgnoreCase("installations")) {
                ret = trendsService.getNewUserTrendsMonth(appId, endDate, startDate, time_unit, time_type,
                        dimension_type, dimension_name);
            }

        }
        return ret;
    }

    /**
     * 加载表格数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/load_table_data", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String loadTableData(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        String ret = "";

        String page = request.getParameter("page");
        String appId = request.getParameter("app_id");
        if (null == appId || appId.equalsIgnoreCase("")) {
            appId = "4";
        }

        // String client = request.getParameter("client");
        // if (null == client || client.equalsIgnoreCase("")) {
        // client = "4";
        // }
        String stats = request.getParameter("stats");
        String time_unit = request.getParameter("time_unit");
        if (null == time_unit || time_unit.trim().equals("")) {
            return ret;
        }

        if (null == stats || stats.trim().equals("")) {
            return ret;
        }
        
        //TODO
        System.out.println("stats===============: "+stats);
        System.out.println("time_unit===============: "+time_unit);

        String endDate = request.getParameter("end_date");
        String startDate = request.getParameter("start_date");

        //TODO
        System.out.println("startDate===============: "+startDate);
        System.out.println("endDate===============: "+endDate);
        
        String channels = request.getParameter("channels[]");
        if (null != channels && channels.equals("")) {
            try {
                channels = URLDecoder.decode(channels, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                channels = "";
                e.printStackTrace();
            }
        }
        String versions = request.getParameter("versions[]");
        if (null != versions && versions.equals("")) {
            try {
                versions = URLDecoder.decode(versions, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                versions = "";
                e.printStackTrace();
            }
        }

        String dimension_type = "";
        String dimension_name = "";
        if ((null != channels && null != versions) && (!channels.trim().equals("") && !versions.trim().equals(""))) {
            dimension_type = "CHANNELID_VERSION";
            dimension_name = channels + "_" + versions;
        } else if ((null == channels || channels.trim().equals(""))
                && (null != versions && !versions.trim().equals(""))) {
            dimension_type = "VERSION";
            dimension_name = versions;
        } else if ((null == versions || versions.trim().equals(""))
                && (null != channels && !channels.trim().equals(""))) {
            // dimension_type = "CHANNELID";
            dimension_type = "CHANNEL";
            dimension_name = channels;
        } else {
            dimension_type = "ALL";
            dimension_name = "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (time_unit.trim().equalsIgnoreCase("daily")) {
            if (null == startDate || null == endDate || startDate.trim().equals("") || endDate.trim().equals("")) {
                String today = sdf.format(System.currentTimeMillis());
                startDate = DateUtil.getSpecifiedDayBefore(today, 7);
                endDate = DateUtil.getSpecifiedDayBefore(today, 1);
            } else {
                startDate = DateUtil.getSpecifiedDayBefore(startDate, 1);
                endDate = DateUtil.getSpecifiedDayBefore(endDate, 1);
            }
            String time_type = "TODAY";
            if (stats.equalsIgnoreCase("retentions")) {
                if (startDate.equals(endDate)) {
                    startDate = DateUtil.getSpecifiedDayBefore(endDate, 7);
                }
                startDate = DateUtil.getSpecifiedDayBefore(startDate, 1);
                endDate = DateUtil.getSpecifiedDayBefore(endDate, 1);
                ret = this.trendsService.getUserRetentions(appId, endDate, startDate, page, time_unit, time_type,
                        dimension_type, dimension_name);
            } else if (stats.equalsIgnoreCase("origin_retentions")) {
                if (startDate.equals(endDate)) {
                    startDate = DateUtil.getSpecifiedDayBefore(endDate, 7);
                }
                startDate = DateUtil.getSpecifiedDayBefore(startDate, 1);
                endDate = DateUtil.getSpecifiedDayBefore(endDate, 1);
                ret = this.trendsService.getOriginUserRetentions(appId, endDate, startDate, page, time_unit, time_type,
                        dimension_type, dimension_name);
            } else if (stats.equalsIgnoreCase("mall_user_week_order_retentions")) {
                if (startDate.equals(endDate)) {
                    startDate = DateUtil.getSpecifiedDayBefore(endDate, 7);
                }
                startDate = DateUtil.getSpecifiedDayBefore(startDate, 1);
//				endDate = DateUtil.getSpecifiedDayBefore(endDate, 1);
                ret = this.trendsService.getMallUserWeekRetentions(appId, endDate, startDate, page, time_unit,
                        time_type, dimension_type, dimension_name);
            } else if (stats.equalsIgnoreCase("installations")) {
                startDate = DateUtil.getSpecifiedDayAfter(startDate, 1);
//				endDate = DateUtil.getSpecifiedDayAfter(endDate, 1);
                ret = this.trendsService.getNewUserDetails(appId, endDate, startDate, page, time_unit, time_type,
                        dimension_type, dimension_name);
            } else if (stats.equalsIgnoreCase("active_users")) {
                ret = this.trendsService.getActiveUserDetails(appId, endDate, startDate, page, time_unit, time_type,
                        dimension_type, dimension_name);
            } else if (stats.equalsIgnoreCase("launches")) {
                ret = this.trendsService.getLaunchesDetails(appId, endDate, startDate, page, time_unit, time_type,
                        dimension_type, dimension_name);
            }
        } else if (time_unit.trim().equalsIgnoreCase("hourly")) {

        } else if (time_unit.trim().equalsIgnoreCase("weekly")) {
            String time_type = "TODAY";
            if (stats.equalsIgnoreCase("installations")) {
                ret = this.trendsService.getNewUserDetailsWeek(appId, endDate, startDate, page, time_unit, time_type,
                        dimension_type, dimension_name);
            } else if (stats.equalsIgnoreCase("mall_user_week_order_retentions")) {
                if (startDate.equals(endDate)) {
                    startDate = DateUtil.getSpecifiedDayBefore(endDate, 7);
                }
                startDate = DateUtil.getSpecifiedDayBefore(startDate, 1);
                endDate = DateUtil.getSpecifiedDayBefore(endDate, 1);
                ret = this.trendsService.getMallUserWeekRetentions(appId, endDate, startDate, page, time_unit,
                        time_type, dimension_type, dimension_name);
            } else if (stats.equalsIgnoreCase("retentions")) {
                if (startDate.equals(endDate)) {
                    startDate = DateUtil.getSpecifiedDayBefore(endDate, 7);
                }
                startDate = DateUtil.getSpecifiedDayBefore(startDate, 1);
                endDate = DateUtil.getSpecifiedDayBefore(endDate, 1);
                ret = this.trendsService.getUserRetentions(appId, endDate, startDate, page, time_unit, time_type,
                        dimension_type, dimension_name);
            }
        } else if (time_unit.trim().equalsIgnoreCase("monthly")) {
        	
            String time_type = "TODAY";
            if (null == startDate || null == endDate || startDate.trim().equals("") || endDate.trim().equals("")) {
                String today = sdf.format(System.currentTimeMillis());
                startDate = DateUtil.getSpecifiedDayBefore(today, 31);
                endDate = DateUtil.getSpecifiedDayBefore(today, 1);
            }
            if (stats.equalsIgnoreCase("installations")) {
                ret = this.trendsService.getNewUserDetailsMonth(appId, endDate, startDate, page, time_unit, time_type,
                        dimension_type, dimension_name);
            }
            if (stats.equalsIgnoreCase("retentions")) {
                ret = this.trendsService.getUserRetentionsMonth(appId, endDate, startDate, page, time_unit, time_type,
                        dimension_type, dimension_name);
            }
        }

        System.out.println("ret in Controller:=============================="+ret);
        
        return ret;
    }


    /**
     * 新增注册用户来源统计
     * @param appId
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/register_user_type", method = RequestMethod.GET)
    public String  registeUserType(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                               HttpServletRequest request, ModelMap model){
        System.out.println("---------------------"+appId);
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String startDate = request.getParameter("startDate");
        if (null == startDate || startDate.equals("")) {
            startDate = DateUtil.getSpecifiedDayBefore(today, 6);
        }
        String endDate = request.getParameter("endDate");
        if (null == endDate || endDate.equals("")) {
            endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        }

        String type = request.getParameter("type");
        if (StringUtils.isBlank(type)) {
            type = "type";
        }

        model.put("globalAppid", appId);
        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("type", type);

        Map map=  trendsService.queryRegisterUserList(startDate,endDate);

        model.put("userData",map);
        return "/app_trend/new_register_user";
    }


    /**
     * 新增注册用户日统计
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/register_user", method = RequestMethod.GET)
    public String  registeDayUser(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                               HttpServletRequest request, ModelMap model){
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String startDate = request.getParameter("startDate");
        if (null == startDate || startDate.equals("")) {
            startDate = DateUtil.getSpecifiedDayBefore(today, 6);
        }
        String endDate = request.getParameter("endDate");
        if (null == endDate || endDate.equals("")) {
            endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        }

        String type = request.getParameter("type");
        if (StringUtils.isBlank(type)) {
            type = "source";
        }

        model.put("globalAppid", appId);
        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("type", type);

        Map map=  trendsService.queryRegisterDayUserList(startDate,endDate);

        model.put("userData",map);
        return "/app_trend/register_day_user";
    }

    /**
     * 注册用户周统计
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/register_week_user", method = RequestMethod.GET)
    public String  registeWeekUser(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                                   HttpServletRequest request, ModelMap model){
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String startDate = request.getParameter("startDate");
        if (null == startDate || startDate.equals("")) {
            startDate = DateUtil.getSpecifiedDayBefore(today, 7);
        }
        String endDate = request.getParameter("endDate");
        if (null == endDate || endDate.equals("")) {
            endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        }

        String type = request.getParameter("type");

        if (null == type || "".equals(type)) {
            type = "week";
        }

        model.put("globalAppid", appId);
        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("type", type);

        Map map=  trendsService.queryRegisterWeekUserList(startDate,endDate);
        model.put("userData",map);
        return "/app_trend/register_week_user";
    }

    /**
     * 注册用户月统计
     * @param appId
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/register_month_user", method = RequestMethod.GET)
    public String  registeMonthUser(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                                    HttpServletRequest request, ModelMap model){
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String startDate = request.getParameter("startDate");
        if (null == startDate || startDate.equals("")) {
            startDate = DateUtil.getSpecifiedDayBefore(today, 7);
        }
        String endDate = request.getParameter("endDate");
        if (null == endDate || endDate.equals("")) {
            endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        }

        String type = request.getParameter("type");

        if (null == type || "".equals(type)) {
            type = "month";
        }

        model.put("globalAppid", appId);
        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("type", type);
        Map map=  trendsService.queryRegisterMonthUserList(startDate,endDate);

        model.put("userData",map);
        return "/app_trend/register_month_user";
    }



}
