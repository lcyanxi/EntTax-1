package com.zyz.open.hiveadmin.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.douguo.dg.group.service.DgGroupService;
import com.zyz.open.hiveadmin.model.HiveAdminJobTemplate;
import com.zyz.open.hiveadmin.service.HiveAdminJobTemplateService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.douguo.dc.common.web.BaseController;
import com.douguo.dc.serverlog.service.ServerLogQtypeDictService;
import com.douguo.dc.user.service.UserService;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.JavaSendMail;
import com.douguo.dg.user.service.DgUserService;
import com.zyz.open.hiveadmin.common.HiveAdminJobConstants;
import com.zyz.open.hiveadmin.common.HiveAdminJobUtil;
import com.zyz.open.hiveadmin.model.HiveAdminJob;
import com.zyz.open.hiveadmin.service.HiveAdminJobOldAdmService;
import com.zyz.open.hiveadmin.service.HiveAdminJobService;

@Controller
@RequestMapping("/hiveadmin")
public class HiveAdminController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(HiveAdminController.class);

    @Autowired
    private HiveAdminJobService hiveAdminJobService;
    
    @Autowired
    private HiveAdminJobOldAdmService hiveAdminJobOldAdmService;

    @Autowired
    private HiveAdminJobTemplateService hiveAdminJobTemplateService;

    @Autowired
    private ServerLogQtypeDictService serverLogQtypeDictService;

    @Autowired
    private DgUserService dgUserService;

    @Autowired
    private DgGroupService dgGroupService;
    
    @Autowired
    private UserService userService ;

    private Map<String, String> mapGroups;

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
        
        //获取当前登陆用户ID
        String uid = getUser(request).getUid();
        
        List<HiveAdminJob> list = null;
        if (uid != null) {
        	if("zyz".equals(uid) || "zhangjianfei".equals(uid) || "lichang".equals(uid)){
        		list = hiveAdminJobService.queryAll();
        	}else {
        		list = hiveAdminJobService.queryListByUid(uid);
        	}
        }

        //
        int records = list.size();

        total = records / Integer.parseInt(rowsLimit) + 1; // 计算总的页数
        String[][] arryFun = new String[list.size()][];

        for (int i = 0; i < list.size(); i++) {
            HiveAdminJob haJob = list.get(i);
            
            String jobNameStr = haJob.getJobName() ;
            if(jobNameStr==null || "".equals(jobNameStr)){
            	jobNameStr = "未命名任务_" + haJob.getId() ;
            }
            
            String operate = new String() ;
            
            if(haJob.getStatus() == HiveAdminJobConstants.JobStatus.CHECK_DRAFT){ //待审核状态，发送邮件请求审核
            	
            	StringBuffer tempOperate = new StringBuffer() ;
            	
            	tempOperate.append("<a style=\"color:#F75000\" href='/hiveadmin/queryForCheck.do?jobId=" + haJob.getId()
                + "' title='申请执行' >申请执行</a>&nbsp;&nbsp;"
                + "<a href='/hiveadmin/preEdit.do?jobId=" + haJob.getId()
                + "' title='修改' >修改</a>&nbsp;&nbsp;"
                +"<a class=\"deleteJob\" style=\"color:#ADADAD\" data-running=\"" + haJob.getStatus()
                + "\" data-jid=\"" + haJob.getId() + "\" href='javascript:void(0);' title='删除' >删除</a>&nbsp;&nbsp;") ;
            	
            	//TODO 设置审核人
            	if("zhangjianfei".equals(uid) || "zyz".equals(uid)){
            		tempOperate.append("<a href='/hiveadmin/check.do?jobId=" + haJob.getId()
                    + "' title='通过审核' >通过审核</a>&nbsp;&nbsp;") ;
            	}
            	
            	operate = tempOperate.toString() ;
            	
            }
            
            if(haJob.getStatus() == HiveAdminJobConstants.JobStatus.CHECK_OK){ //审核通过显示执行按钮
            	operate = "<a class=\"dojob\" style=\"color:#CE3131\" data-running=\"" + haJob.getStatus()
                + "\" data-jid=\"" + haJob.getId()
                + "\" href='javascript:void(0);' title='执行任务'>执行</a>&nbsp;&nbsp;"
                +"<a href='/hiveadmin/preEdit.do?jobId=" + haJob.getId()
                + "' title='修改' >修改</a>&nbsp;&nbsp;"
                +"<a class=\"deleteJob\" style=\"color:#ADADAD\" data-running=\"" + haJob.getStatus()
                + "\" data-jid=\"" + haJob.getId() + "\" href='javascript:void(0);' title='删除' >删除</a>";
            }
            
            if(haJob.getStatus() == HiveAdminJobConstants.JobStatus.EXEC_SUCCESS){ //执行成功
            	operate = "<a class=\"dojob\" style=\"color:#CE3131\" data-running=\"" + haJob.getStatus()
                + "\" data-jid=\"" + haJob.getId()
                + "\" href='javascript:void(0);' title='执行任务'>执行</a>&nbsp;&nbsp;"
                +"<a href='/hiveadmin/preEdit.do?jobId=" + haJob.getId()
                + "' title='修改' >修改</a>&nbsp;&nbsp;"
                +"<a class=\"deleteJob\" style=\"color:#ADADAD\" data-running=\"" + haJob.getStatus()
                + "\" data-jid=\"" + haJob.getId() + "\" href='javascript:void(0);' title='删除' >删除</a>&nbsp;&nbsp;"
            	+ "<a class=\"viewstatdata\" data-running=\"" + haJob.getStatus() + "\" data-jid=\"" + haJob.getId()
                + "\" href='javascript:void(0);' title='查看'>查看结果</a>&nbsp;&nbsp;"
//              + "<a href='/hiveadmin/data/data-" + haJob.getId() + ".txt?time=" + System.currentTimeMillis()
//              + "' title='下载'>下载</a>&nbsp;&nbsp;"
	            + "<a href=\"/hiveadmin/exportExcel.do?id="+haJob.getId()+"&jobType="+ haJob.getJobType()+"&jobName="+ jobNameStr
	            + " \" title='导出Excel'>导出Excel</a>" ;
            }
            
            if(haJob.getStatus() == HiveAdminJobConstants.JobStatus.EXEC_FAILURE){ //执行失败
            	operate = "<a class=\"dojob\" style=\"color:#CE3131\" data-running=\"" + haJob.getStatus()
                + "\" data-jid=\"" + haJob.getId()
                + "\" href='javascript:void(0);' title='执行任务'>执行</a>&nbsp;&nbsp;"
                +"<a href='/hiveadmin/preEdit.do?jobId=" + haJob.getId()
                + "' title='修改' >修改</a>&nbsp;&nbsp;"
                +"<a class=\"deleteJob\" style=\"color:#ADADAD\" data-running=\"" + haJob.getStatus()
                + "\" data-jid=\"" + haJob.getId() + "\" href='javascript:void(0);' title='删除' >删除</a>&nbsp;&nbsp;";
            }

            arryFun[i] = new String[]{String.valueOf(haJob.getId()), haJob.getJobType(), jobNameStr,
                    haJob.getUid(), HiveAdminJobConstants.convertJobStatus(haJob.getStatus()),
                    haJob.getStatBeginTime().substring(0, 10), haJob.getStatEndTime().substring(0, 10),
                    haJob.getJobStartTime().substring(0, 19), operate};
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
     * 把执行结果导出到Excl供用户下载
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException 
     */
    @RequestMapping(value = "/exportExcel")
    public ModelAndView exportExcel(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {
    	
    	List<Map<String, Object>> rowlst = new ArrayList<Map<String, Object>>();
    	
    	String jobId = request.getParameter("id");
    	String jobType = request.getParameter("jobType") ;
    	String jobName = request.getParameter("jobName") ;
    	
    	// 读取数据文本内容
//    	File file = new File("C:/Users/Administrator/Desktop/img/test.txt");
    	File file = new File(HiveAdminJobConstants.JOB_WORK_DATA_DIR + "/data-" + jobId + ".txt");

    	if(file.length() != 0 && file.length() < HiveAdminJobConstants.MAX_TXT_LENGTH){
    		//文件读取-begin
    		BufferedReader reader = null;
    		try {
    			reader = new BufferedReader(new FileReader(file));
    			String line = null;
    			int n = 0;
    			// 一次读入一行，直到读入null为文件结束
    			while ((line = reader.readLine()) != null) {
    				String[] arryLine = line.split("\001");
    				Map<String, Object> map = new HashMap<String, Object>();
    				for (int i = 0; i < arryLine.length; i++) {
    					map.put(String.valueOf(i), arryLine[i]);
    				}
    				rowlst.add(map);
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    		} finally {
    			if (reader != null) {
    				try {
    					reader.close();
    				} catch (IOException e1) {
    				}
    			}
    		}
    		//文件读取-END
    	} else if(file.length() == 0){
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("0", "没有数据，请核对参数及模板") ;
    		rowlst.add(map) ;
    		jobName = "Error_dateNone" ;
    	} else {
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("0", "数据量过大，请联系dataCenter") ;
    		rowlst.add(map) ;
    		jobName = "Error_dateOutRange" ;
    	}
    	
        
        if(jobName==null || "".equals(jobName)){
        	jobName = "未命名" ;
        }
        
        //获取表头
        HiveAdminJobTemplate template = hiveAdminJobTemplateService.getHiveAdminJobTemplateByUid(jobType);
        List<String> colTitleList = new ArrayList<String>();
        if (template != null) {
            String strListTitle = template.getShowListTitle();
            String[] arryListTitle = strListTitle.split(",");
            for (int i = 0; i < arryListTitle.length; i++) {
                colTitleList.add(arryListTitle[i]);
            }
        }
        
        model.put("colTitleList", colTitleList) ;
        model.put("exclName", jobName) ;
        model.put("list", rowlst) ;
    	
    	return new ModelAndView(new HiveAdminStatDateExportExcelView(), model);
    }

    
    /**
     * job管理-页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/preList")
    public String preJobList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
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
            plat = "JOB";
        }

        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("plat", plat);
        return "/hive_admin/hive_admin_job_list";
    }

    /**
     * 用户行为平台管理-页面
     */
    @RequestMapping(value = "/userbehavior/preList")
    public String preUserBehaviorList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
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
            plat = "";
        }
        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("plat", plat);
        return "/hive_admin/user_behavior/user_behavior_query_list_frame";
    }

    /**
     * 焦点图数据统计-页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/preFocusList")
    public String preFocusList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
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
        String type = request.getParameter("type");
        if (StringUtils.isEmpty(type)) {
            type = "";
        }
        String focusId = request.getParameter("focusId");

        if (null == plat || "".equals(plat)) {
            plat = "FOCUS";
        }

        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("plat", plat);
        model.put("focusId", focusId);
        model.put("type", type);

        List<Map<String, Object>> rowlst = new ArrayList<Map<String, Object>>();
        System.out.println("focusId" + focusId);
        if (StringUtils.isNotEmpty(focusId)) {
            rowlst = hiveAdminJobOldAdmService.queryFocusList(type, startDate, endDate, focusId);

        }

        model.put("rowlst", rowlst);
        return "/hive_admin/hive_admin_focus_list";
    }

    /**
     * 广告数据统计-页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/preAdList")
    public String preAdList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
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
        String adId = request.getParameter("adId");

        if (null == plat || "".equals(plat)) {
            plat = "AD";
        }

        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("plat", plat);
        model.put("adId", adId);

        List<Map<String, Object>> rowlst = new ArrayList<Map<String, Object>>();
        System.out.println("adId" + adId);
        if (StringUtils.isNotEmpty(adId)) {
            rowlst = hiveAdminJobOldAdmService.queryAdList(startDate, endDate, adId);

        }

        model.put("rowlst", rowlst);
        return "/hive_admin/hive_admin_ad_list";
    }

    /**
     * job管理-frame页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/preListFrame")
    public String preListFrame(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
        List<HiveAdminJobTemplate> templateList = hiveAdminJobTemplateService.queryAll();
        
        if(request.getAttribute("dslMsg") != null){
        	model.put("dslMsg", request.getAttribute("dslMsg")) ;
        }
        if(request.getAttribute("dpMsg") != null){
        	model.put("dpMsg", request.getAttribute("dpMsg")) ;
        }
        if(request.getAttribute("dwpMsg") != null){
        	model.put("dwpMsg", request.getAttribute("dwpMsg")) ;
        }
        if(request.getAttribute("queryMailSuccessMsg") != null){ // 用户申请审核Msg
        	model.put("queryMailSuccessMsg", request.getAttribute("queryMailSuccessMsg")) ;
        }
        if(request.getAttribute("checkMailSuccessMsg") != null){ // 管理员审核成功Msg
        	model.put("checkMailSuccessMsg", request.getAttribute("checkMailSuccessMsg")) ;
        }

        model.put("templateList", templateList);

        return "/hive_admin/hive_admin_job_list_frame";
    }

    /**
     * 用户行为平台管理-frame页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/userbehavior/preListFrame")
    public String preUserBehaviorListFrame(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
        return "/hive_admin/user_behavior/user_behavior_query_list_frame";
    }

    /**
     * 管理页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/preTagList")
    public String preTagList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
        return "/hive_admin/hive_admin_tag_list";
    }

    @RequestMapping(value = "/execJob", method = RequestMethod.GET)
    public String execJob(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                          HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String jobId = request.getParameter("jobId");
        HiveAdminJob haJob = hiveAdminJobService.getHiveAdminJobById(jobId);
        // 设置任务为执行中状态
        haJob.setStatus(HiveAdminJobConstants.JobStatus.EXEC_RUNNING);
        hiveAdminJobService.update(haJob);

        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");

            String querySQL = haJob.getHql();
            //
            Connection con = DriverManager.getConnection("jdbc:hive2://localhost:10000/default", "", "");
            Statement stmt = con.createStatement();
            // stmt.executeQuery(dropSQL); // 执行删除语句
            // stmt.executeQuery(createSQL); // 执行建表语句
            // stmt.executeQuery(insterSQL); // 执行插入语句
            log.info("querySQL:" + querySQL);
            ResultSet res = stmt.executeQuery(querySQL); // 执行查询语句

            FileWriter fw = new FileWriter("/usr/local/goldmine/tomcat/default/webapps/ROOT/hiveadmin/data/data-"
                    + jobId + ".txt");
            ResultSetMetaData dd = res.getMetaData();
            int cc = dd.getColumnCount();
            // System.out.println("columnCount:" + cc);

            while (res.next()) {
                StringBuffer sbValue = new StringBuffer("");
                for (int i = 1; i <= cc; i++) {
                    sbValue.append(res.getString(i));
                    if (i != cc) {
                        sbValue.append("\001");
                    }
                }

                //System.out.println(sbValue);
                fw.write(sbValue + "\n");
            }

            fw.flush();
            fw.close();
            // 设置任务为成功状态
            haJob.setStatus(HiveAdminJobConstants.JobStatus.EXEC_SUCCESS);
            hiveAdminJobService.update(haJob);
        } catch (Exception e) {
            // 设置任务为失败状态
            haJob.setStatus(HiveAdminJobConstants.JobStatus.EXEC_FAILURE);
            hiveAdminJobService.update(haJob);
            e.printStackTrace();
        }

        return "/hive_admin/hive_admin_job_list_frame";
    }

    @RequestMapping(value = "/insert")
    public String insert(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException, ParseException {
        request.setCharacterEncoding("utf-8");

        HiveAdminJob hiveAdminJob = new HiveAdminJob();
        
        String jobName = request.getParameter("jobName");
        String jobType = request.getParameter("jobType");

        String queryType = request.getParameter("queryType");
        String jobTypeContent = request.getParameter("jobTypeContent");

        if (StringUtils.isNotBlank(jobTypeContent)) {
            jobTypeContent = StringUtils.trim(jobTypeContent);
        }

        String statBeginTime = request.getParameter("statBeginTime");
        String statEndTime = request.getParameter("statEndTime");
        String jobStartTime = request.getParameter("jobStartTime");

        if (StringUtils.isBlank(jobStartTime)) {
            jobStartTime = DateUtil.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss");
        }
        
        String curTime = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
        
        //判断statBeginTime、statEndTime、jobStartTime是否为空
        if(jobStartTime==null || "".equals(jobStartTime)){
        	jobStartTime = curTime ;
        }
        if(statEndTime==null || "".equals(statEndTime)){
        	statEndTime = curTime ;
        }
        if(statBeginTime==null || "".equals(statBeginTime)){
        	statBeginTime = curTime ;
        }
        
        String hql = buildHql(jobType, queryType, jobTypeContent, statBeginTime, statEndTime);
        String hqlTemplate = "";
        
        // flag : status是否已经改变
        boolean statusChangeFalg = false ;
        
        // 判断表名是否为日志表 & 时间跨度是否合适
        statusChangeFalg = HiveAdminJobUtil.tableDaysDiffCheck(hiveAdminJob,statBeginTime,statEndTime,hql,request) ;
        
        // 当不满足上述任何一种条件即status尚未改变时 设置：审核通过
        if(!statusChangeFalg){
        	hiveAdminJob.setStatus(HiveAdminJobConstants.JobStatus.CHECK_OK);
        }

        String uid = getUser(request).getUid();

        hiveAdminJob.setJobName(jobName);
        hiveAdminJob.setJobType(jobType);
        hiveAdminJob.setQueryType(queryType);
        hiveAdminJob.setJobTypeContent(jobTypeContent);
        hiveAdminJob.setStatBeginTime(statBeginTime);
        hiveAdminJob.setStatEndTime(statEndTime);
        hiveAdminJob.setJobStartTime(jobStartTime);
        hiveAdminJob.setUid(uid);
        hiveAdminJob.setHql(hql);
        hiveAdminJob.setHqlTemplate(hqlTemplate);
        hiveAdminJob.setUpdateTime(curTime);
        hiveAdminJob.setCreateTime(curTime);

        hiveAdminJobService.insert(hiveAdminJob);
        
        return "forward:/hiveadmin/preListFrame.do";
    }
    
    /**
     * 用户请求审核，发送邮件至 zhangjanfei@douguo.com , zhangyaozhou@douguo.com
     * @author JainfeiZhang
     * @param request
     * @param response
     * @param model
     * @return
     * @throws UnsupportedEncodingException
     * @throws ParseException
     */
    @RequestMapping(value = "/queryForCheck")
    public String queryForCheck(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException, ParseException {
		
    	//通过ID获取当前请求的JOB
    	String jobId = request.getParameter("jobId") ;
    	HiveAdminJob hiveAdminJob = hiveAdminJobService.getHiveAdminJobById(jobId) ;
    	
    	//获取当前登录用户
    	String uid = getUser(request).getUid();
    	
    	//通过uid查找用户名称
    	String userName = userService.getUser(uid).getUsername() ;
    	
		//给zjf/zyz发送请求审核邮件邮件
		String subject = "数据超市查询任务审核请求" ;
		String to = "zhangjianfei@douguo.com,zhangyaozhou@douguo.com" ;
		StringBuffer content = new StringBuffer(
				"用户： <b>"+userName+"</b></br>"
				+"JobID: <b>"+hiveAdminJob.getId()+"</b></br>"
				+"JobName: <b>"+hiveAdminJob.getJobName()+"</b></br>"
				+"JobType: <b>"+hiveAdminJob.getJobType()+"</b></br>"
				+"JobStatus: <b>"+HiveAdminJobConstants.convertJobStatus(hiveAdminJob.getStatus())+"</b></br>"
				+"创建时间： <b>"+hiveAdminJob.getCreateTime()+"</b></br>"
				+"创建人： <b>"+hiveAdminJob.getUid()+"</b></br>"
				+"开始统计时间： <b>"+hiveAdminJob.getStatBeginTime()+"</b></br>"
				+"结束统计时间： <b>"+hiveAdminJob.getStatEndTime()+"</b></br>"
				+"SQL：<fieldset>"+hiveAdminJob.getHql()+"</fieldset></br>"
				+"URL: http://dc.douguo.net/") ;
		try {
			JavaSendMail.sendHtmlEmail(subject, to, content.toString()) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 回显信息，提示邮件发送成功
		request.setAttribute("queryMailSuccessMsg", "申请邮件发送成功！");
		
    	return "forward:/hiveadmin/preListFrame.do" ;
    }
    
    /**
     * 管理员审核
     * @author JainfeiZhang
     * @param request
     * @param response
     * @param model+
     * @return
     * @throws UnsupportedEncodingException
     * @throws ParseException
     */
    @RequestMapping(value = "/check")
    public String check(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException, ParseException {
    	
    	//通过ID获取当前请求的JOB
    	String jobId = request.getParameter("jobId") ;
    	HiveAdminJob hiveAdminJob = hiveAdminJobService.getHiveAdminJobById(jobId) ;
    	
    	//设置审核通过
    	hiveAdminJob.setStatus(HiveAdminJobConstants.JobStatus.CHECK_OK);
    	
    	//更新
    	hiveAdminJobService.update(hiveAdminJob);
    	
    	//通过uid查找联系人
    	String uid = hiveAdminJob.getUid() ;
    	String userName = userService.getUser(uid).getUsername() ;
    			
		//给请求用户发送审核通过邮件
		String subject = "数据超市查询任务审核完成通知" ;
		String to = hiveAdminJob.getUid()+"@douguo.com" ;
		StringBuffer content = new StringBuffer(
				"Hello~ <b>"+userName+"</b></br>"
				+"DataCenter已经收到你的申请~ 现已通过审核, 任务正在执行中, 快去看看吧：http://dc.douguo.net/</br>"
				+"	任务名称: <b>"+hiveAdminJob.getJobName()+"</b></br>"
				+"	任务类别: <b>"+hiveAdminJob.getJobType()+"</b></br>"
				+"	创建时间： <b>"+hiveAdminJob.getCreateTime()+"</b></br>"
				+"	开始统计时间： <b>"+hiveAdminJob.getStatBeginTime()+"</b></br>"
				+"	结束统计时间： <b>"+hiveAdminJob.getStatEndTime()+"</b></br>"
				+"	审核状态: <b>"+HiveAdminJobConstants.convertJobStatus(hiveAdminJob.getStatus())+"</b></br>") ;
		try {
			JavaSendMail.sendHtmlEmail(subject, to, content.toString()) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 回显信息，提示邮件发送成功
		request.setAttribute("checkMailSuccessMsg", "审核邮件发送成功！");
    	
    	return "forward:/hiveadmin/preListFrame.do" ;
    }

    private String buildHql(String jobType, String queryType, String jobTypeContent, String statBeginTime,
                            String statEndTime) {
        String hql = "";
        jobTypeContent = jobTypeContent.replaceAll("\\?", "\\\\\\\\?");

        if (jobType.equals("1")) {// 主站pv 查询pv&ip
            statBeginTime = statBeginTime.replaceAll("-", "");
            statEndTime = statEndTime.replaceAll("-", "");

            //根据url内容，自动处理url截取
            jobTypeContent = jobTypeContent.replaceAll("http://www.douguo.com", "");
            jobTypeContent = jobTypeContent.replaceAll("http://m.douguo.com", "");
            //
            hql = "select p_day,count(*),count(distinct ip) from dh_pv where p_day>=" + statBeginTime
                    + " and p_day <= " + statEndTime + " and request rlike '" + jobTypeContent
                    + ".*' group by p_day " +
                    " order by p_day " +
                    " limit 1000";
        } else if (jobType.equals("2")) {
            statBeginTime = statBeginTime.replaceAll("-", "");
            statEndTime = statEndTime.replaceAll("-", "");

            //根据url内容，自动处理url截取
            jobTypeContent = jobTypeContent.replaceAll("http://www.douguo.com", "");
            jobTypeContent = jobTypeContent.replaceAll("http://m.douguo.com", "");
            //
            hql = "select p_day,count(*),count(distinct ip) from dh_wap_pv where p_day>=" + statBeginTime
                    + " and p_day <= " + statEndTime + " and request rlike '" + jobTypeContent
                    + ".*' group by p_day " +
                    " order by p_day " +
                    " limit 1000";
        } 
        
        else if (jobType.equals("99")) {
            hql = "";
        } else if (jobType.equals("999")) {// 用户行为数据查询
            hql = buildUserBehaviorSql(statBeginTime, statEndTime, queryType, jobTypeContent);
        } else {
            String statBeginPartition = statBeginTime.replaceAll("-", "");
            String statEndPartition = statEndTime.replaceAll("-", "");
            HiveAdminJobTemplate hajTemplate = hiveAdminJobTemplateService.getHiveAdminJobTemplateByUid(jobType);
            hql = hajTemplate.getTemplateContent();
            hql = hql.replaceAll("<<BEGIN_DATE>>", statBeginTime);
            hql = hql.replaceAll("<<END_DATE>>", statEndTime);
            hql = hql.replaceAll("<<BEGIN_PARTITION>>", statBeginPartition);
            hql = hql.replaceAll("<<END_PARTITION>>", statEndPartition);
            hql = hql.replaceAll("<<JOB_TYPE_CONTENT>>", jobTypeContent);
        }
        return hql;
    }

    private String namesToUids(String names) {
        String[] arryNames = names.split(",");
        StringBuffer sbUids = null;
        for (String nkName : arryNames) {
            Map<String, Object> map = dgUserService.getUserByNickName(nkName);
            Integer tmpUid = (Integer) map.get("user_id");
            if (tmpUid == null) {
                continue;
            }
            if (sbUids == null) {
                sbUids = new StringBuffer();
                sbUids.append(tmpUid);
            } else {
                sbUids.append("," + tmpUid);
            }
        }

        return sbUids.toString();
    }

    private String buildUserBehaviorSql(String statBeginTime, String statEndTime, String queryType,
                                        String jobTypeContent) {
        String statBeginPartition = statBeginTime.replaceAll("-", "");
        String statEndPartition = statEndTime.replaceAll("-", "");

        StringBuffer sqlUid = null;
        StringBuffer sqlImei = null;

        if ("3".equals(queryType)) {// nickName 转uid
            queryType = "2";
            jobTypeContent = namesToUids(jobTypeContent);
        }
        //
        String[] arryIds = jobTypeContent.split(",");
        for (String id : arryIds) {
            //
            if (sqlImei == null) {
                sqlImei = new StringBuffer("req[\"imei\"]='" + id + "'");
            } else {
                sqlImei.append(" or req[\"imei\"]='" + id + "'");
            }
            //
            if (sqlUid == null) {
                sqlUid = new StringBuffer("req[\"uid\"]='" + id + "'");
            } else {
                sqlUid.append(" or req[\"uid\"]='" + id + "'");
            }
        }

        StringBuffer sbHql = new StringBuffer();
        if ("1".equals(queryType)) {// imei
            // 如果查询包含今天
            if (isCurDate(statEndTime)) {
                sbHql.append("select nt.time,nt.uid,nt.qtype,nt.obj,nt.souc,nt.vers,nt.plat from (");
                sbHql.append("	select time,req[\"imei\"] as imei,req[\"qtype\"] as qtype,req[\"obj\"] as obj,req[\"souc\"] as souc,req[\"vers\"] as vers,plat ");
                sbHql.append(" 	from dh_server_log ");
                sbHql.append(" 	where p_day >=" + statBeginPartition + " and p_day<=" + statEndPartition);
                sbHql.append(" 		and (" + sqlImei + ") ");
                sbHql.append(" union all ");
                sbHql.append("	select time,req[\"imei\"] as imei,req[\"qtype\"] as qtype,req[\"obj\"] as obj,req[\"souc\"] as souc,req[\"vers\"] as vers,plat ");
                sbHql.append(" 	from dh_server_log_by_hour ");
                sbHql.append(" 	where (" + sqlImei + ") ");
                sbHql.append(") nt ");
                sbHql.append(" order by nt.imei,nt.time limit 1000000");
            } else {
                sbHql.append("select time,req[\"imei\"] as imei,req[\"qtype\"] as qtype,req[\"obj\"] as obj,req[\"souc\"] as souc,req[\"vers\"] as vers,plat ");
                sbHql.append(" from dh_server_log ");
                sbHql.append(" where p_day >=" + statBeginPartition + " and p_day<=" + statEndPartition + " and ("
                        + sqlImei + ") ");
                sbHql.append(" order by imei,time limit 1000000");

            }
        } else if ("2".equals(queryType)) {// uid
            // 如果查询包含今天
            if (isCurDate(statEndTime)) {
                sbHql.append("select nt.time,nt.uid,nt.qtype,nt.obj,nt.souc,nt.vers,nt.plat from (");
                sbHql.append("	select time,req[\"uid\"] as uid,req[\"qtype\"] as qtype,req[\"obj\"] as obj,req[\"souc\"] as souc,req[\"vers\"] as vers,plat ");
                sbHql.append("	from dh_server_log ");
                sbHql.append("	where p_day >=" + statBeginPartition + " and p_day<=" + statEndPartition);
                sbHql.append(" 		and (" + sqlUid + ")");
                sbHql.append(" union all ");
                sbHql.append("	select time,req[\"uid\"] as uid,req[\"qtype\"] as qtype,req[\"obj\"] as obj,req[\"souc\"] as souc,req[\"vers\"] as vers,plat ");
                sbHql.append("	from dh_server_log_by_hour ");
                sbHql.append(" 	where (" + sqlUid + ")");
                sbHql.append(") nt ");
                sbHql.append(" order by nt.uid,nt.time limit 1000000");
            } else {// 查询不包含今天
                sbHql.append("select time,req[\"uid\"] as uid,req[\"qtype\"] as qtype,req[\"obj\"] as obj,req[\"souc\"] as souc,req[\"vers\"] as vers,plat ");
                sbHql.append(" from dh_server_log ");
                sbHql.append(" where p_day >=" + statBeginPartition + " and p_day<=" + statEndPartition + " and ("
                        + sqlUid + ") ");
                sbHql.append(" order by uid,time limit 1000000");
            }
        } else if ("3".equals(queryType)) {// nickname
            String uid = getUid(jobTypeContent);
        }

        return sbHql.toString();
    }

    private boolean isCurDate(String statEndTime) {
        String curDate = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
        if (curDate.equals(statEndTime.substring(0, 10))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据nickname，转换为uid
     *
     * @param nickName
     * @return
     */
    private String getUid(String nickName) {
        return "";
    }

    /**
     * 获取焦点图类型-json数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/load_focustype", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String getChannels(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        //
        List<Map<String, Object>> rowlst = new ArrayList<Map<String, Object>>();
        //
        Map<String, Object> mapIndex = new HashMap<String, Object>();
        mapIndex.put("focustype_name", "首页焦点图");
        mapIndex.put("focustype_code", "INDEX");
        //
        Map<String, Object> mapHuodong = new HashMap<String, Object>();
        mapHuodong.put("focustype_name", "活动页焦点图");
        mapHuodong.put("focustype_code", "HUODONG");
        //
        rowlst.add(mapIndex);
        rowlst.add(mapHuodong);

        String format = "{\"total\":%s,\"data_groups\":[],\"datas\":[%s],\"result\":\"success\"}";
        if (null == rowlst) {
            String str = String.format(format, "0", "");
            return str;
        }

        int total = 0;
        StringBuilder datas = new StringBuilder();
        String formatData = "{\"is_shown\":true,\"name\":\"%s\",\"id\":\"%s\"}";
        for (Map<String, Object> map : rowlst) {
            String name = String.valueOf(map.get("focustype_name")).trim();
            String id = String.valueOf(map.get("focustype_code")).trim();
            if (null == name || null == id || id.trim().equals("")) {
                continue;
            }
            String str = String.format(formatData, name, id);
            datas.append(str).append(",");
            total++;
        }
        String datasStr = " ";
        if (0 != datas.length()) {
            datasStr = datas.substring(0, datas.length() - 1);
        }

        String ret = String.format(format, total, datasStr);
        return ret;
    }

    /**
     * 修改页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/preEdit")
    public String preEdit(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //

        String jobId = request.getParameter("jobId");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        HiveAdminJob hiveAdminJob = hiveAdminJobService.getHiveAdminJobById(jobId);
        hiveAdminJob.setStatBeginTime(hiveAdminJob.getStatBeginTime().substring(0, 10));
        hiveAdminJob.setStatEndTime(hiveAdminJob.getStatEndTime().substring(0, 10));
        hiveAdminJob.setJobStartTime(hiveAdminJob.getJobStartTime().substring(0, 19));
        String uid = getUser(request).getUid();

        //
        List<HiveAdminJobTemplate> templateList = hiveAdminJobTemplateService.queryAll();

        model.put("templateList", templateList);
        model.put("hiveAdminJob", hiveAdminJob);
        model.put("uid", uid);
        return "/hive_admin/hive_admin_job_modify";
    }

    @RequestMapping(value = "/update")
    public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");

        String jobId = request.getParameter("jobId");
        String jobName = request.getParameter("jobName");
        String jobType = request.getParameter("jobType");
        String uid = request.getParameter("uid");
        String queryType = request.getParameter("queryType");
        String hql = request.getParameter("hql");
        String hqlTemplate = request.getParameter("hqlTemplate");
        String jobTypeContent = request.getParameter("jobTypeContent");
        String statBeginTime = request.getParameter("statBeginTime");
        String statEndTime = request.getParameter("statEndTime");
        String jobStartTime = request.getParameter("jobStartTime");

        String curTime = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
        // 只有不等与99的的时候，hql才能取界面传递的 hql
        if (StringUtils.isNotEmpty(jobType) && !"99".equals(jobType)) {
            hql = buildHql(jobType, queryType, jobTypeContent, statBeginTime, statEndTime);
        } else {
            String statBeginPartition = statBeginTime.replaceAll("-", "");
            String statEndPartition = statEndTime.replaceAll("-", "");
            //兼容老代码，模板为空，hql保持不变
            if (StringUtils.isNotBlank(hqlTemplate)) {
                hql = hqlTemplate;
                hql = hql.replaceAll("<<BEGIN_DATE>>", statBeginTime);
                hql = hql.replaceAll("<<END_DATE>>", statEndTime);
                hql = hql.replaceAll("<<BEGIN_PARTITION>>", statBeginPartition);
                hql = hql.replaceAll("<<END_PARTITION>>", statEndPartition);
                hql = hql.replaceAll("<<JOB_TYPE_CONTENT>>", jobTypeContent);
            }
        }

        if (jobId != null && jobId.trim().length() > 0) {
            HiveAdminJob hiveAdminJob = hiveAdminJobService.getHiveAdminJobById(jobId);
            
            // flag : status是否已经改变
            boolean statusChangeFalg = false ;
            
            // 判断表名是否为日志表 & 时间跨度是否合适
            statusChangeFalg = HiveAdminJobUtil.tableDaysDiffCheck(hiveAdminJob,statBeginTime,statEndTime,hql,request) ;
            
            if (hiveAdminJob != null) {// update
            	
            	// 当不满足tableDaysDiffCheck任何一种条件即status尚未改变时 设置：审核通过
            	if(!statusChangeFalg){
            		//设置状态 :通过审核
            		hiveAdminJob.setStatus(HiveAdminJobConstants.JobStatus.CHECK_OK);
            	} else {
            		//设置状态 :待审核
            		hiveAdminJob.setStatus(HiveAdminJobConstants.JobStatus.CHECK_DRAFT);
            	}
            	
                hiveAdminJob.setJobName(jobName);
                hiveAdminJob.setUid(uid);
                hiveAdminJob.setJobType(jobType);
                hiveAdminJob.setQueryType(queryType);
                hiveAdminJob.setJobTypeContent(jobTypeContent);
                hiveAdminJob.setStatBeginTime(statBeginTime);
                hiveAdminJob.setStatEndTime(statEndTime);
                hiveAdminJob.setJobStartTime(jobStartTime);
                hiveAdminJob.setHql(hql);
                hiveAdminJob.setHqlTemplate(hqlTemplate);
                hiveAdminJob.setUpdateTime(curTime);
                
                hiveAdminJobService.update(hiveAdminJob);
            }
        }
        return "forward:/hiveadmin/preListFrame.do";
    }

    /**
     * 转换qtype中文名称
     *
     * @return
     */
    private String getQtypeName(String qtype) {
        Map<String, String> qtypeMap = new HashMap<String, String>();
        List<Map<String, Object>> rowList = serverLogQtypeDictService.queryList();
        for (Map<String, Object> map : rowList) {
            String tmpQtype = (String) map.get("qtype");
            String tmpQtypeName = (String) map.get("qtype_name");
            qtypeMap.put(tmpQtype, tmpQtypeName);
        }
        return qtypeMap.get(qtype);
    }

    /**
     * 转换圈子中文名称
     *
     * @return
     */
    private String getGroupName(String groupId) {

        if (mapGroups == null) {
            mapGroups = dgGroupService.getGroupsMap();
        }

        String groupName = mapGroups.get(groupId);
        if (groupName == null || "".equals(groupName)) {
            return groupId;
        } else {
            return groupName;
        }
    }

    /**
     * 转义objId
     *
     * @return
     */
    private String getObjId(String qtype, String objId) {
        if ("view_group_posts".equals(qtype) || "view_group_simpledetail".equals(qtype)) {
            return getGroupName(objId);
        } else {
            return objId;
        }
    }

    /**
     * 转换app显示名称
     *
     * @param client
     * @return
     */
    private String getAppName(String client) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("3", "IOS-豆果美食");
        map.put("4", "Android-豆果美食");

        return map.get(client);
    }

    @Deprecated
    @RequestMapping(value = "/viewRes", method = RequestMethod.GET)
    public String viewRes(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                          HttpServletRequest request, HttpServletResponse response, ModelMap model) {

        String jobId = request.getParameter("jobId");
        String jobType = request.getParameter("jobType");
        String rootDir = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println("root dir is :" + rootDir);
        //
        List<Map<String, Object>> rowlst = new ArrayList<Map<String, Object>>();
        //
        File file = new File(HiveAdminJobConstants.JOB_WORK_DATA_DIR + "/data-" + jobId + ".txt");
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            int n = 0;
            // 一次读入一行，直到读入null为文件结束
            while ((line = reader.readLine()) != null) {
                String[] arryLine = line.split(",");
                Map<String, Object> map = new HashMap<String, Object>();
                if (arryLine.length == 7) {
                    String qtype = arryLine[2];
                    String objId = arryLine[3];
                    if (HiveAdminJobConstants.excludeQtype(qtype)) {
                        continue;
                    }
                    map.put("time", arryLine[0]);
                    map.put("uid", arryLine[1]);
                    map.put("qtype", qtype);
                    map.put("qtypeName", getQtypeName(arryLine[2]));
                    map.put("objId", getObjId(qtype, objId));
                    map.put("souc", getAppName(arryLine[4]));
                    map.put("vers", arryLine[5]);
                    map.put("plat", arryLine[6]);
                }
                rowlst.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        model.put("rowlst", rowlst);
        return "/hive_admin/user_behavior/user_behavior_view";
    }

    @RequestMapping(value = "/viewStatData", method = RequestMethod.GET)
    public String viewStatData(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                               HttpServletRequest request, HttpServletResponse response, ModelMap model) {

        String jobId = request.getParameter("jobId");
        HiveAdminJob haJob = hiveAdminJobService.getHiveAdminJobById(jobId);
        String jobType = haJob.getJobType();
        String rootDir = Thread.currentThread().getContextClassLoader().getResource("").getPath();

        //
        List<Map<String, Object>> rowlst = new ArrayList<Map<String, Object>>();

        // 绘制表头
        HiveAdminJobTemplate template = hiveAdminJobTemplateService.getHiveAdminJobTemplateByUid(jobType);

        Map<String, Object> mapTitle = new HashMap<String, Object>();
        List<String> colTitleList = new ArrayList<String>();

        if (template != null) {

            String strListTitle = template.getShowListTitle();
            String[] arryListTitle = strListTitle.split(",");
            for (int i = 0; i < arryListTitle.length; i++) {
                mapTitle.put("col" + i, arryListTitle[i]);
                colTitleList.add(arryListTitle[i]);
            }
        } else {
            String strListTitle = "";
            if (jobType.equals("1")) {
                strListTitle = "日期,pv,uv";
            } else if (jobType.equals("2")) {
                strListTitle = "日期,曝光,点击";
            } else if (jobType.equals("3")) {
                strListTitle = "帖子id,小时,回复数,浏览数";
            } else if (jobType.equals("4")) {
                strListTitle = "日期,帖子id,回复数,用户数,pv,uv";
            } else if (jobType.equals("5")) {
                strListTitle = "日期,菜谱id,菜谱名称,pv,uv";
            } else if (jobType.equals("6")) {
                strListTitle = "日期,菜单id,菜单名称,pv,uv";
            }

            String[] arryListTitle = strListTitle.split(",");
            for (int i = 0; i < arryListTitle.length; i++) {
                mapTitle.put("col" + i, arryListTitle[i]);
                colTitleList.add(arryListTitle[i]);
            }
        }

//        File file = new File("C:/Users/Administrator/Desktop/img/test.rar");
        File file = new File(HiveAdminJobConstants.JOB_WORK_DATA_DIR + "/data-" + jobId + ".txt");
        BufferedReader reader = null;
        
    	if(file.length() != 0 && file.length() < HiveAdminJobConstants.MAX_TXT_LENGTH){
    		try {
    			System.out.println("以行为单位读取文件内容，一次读一整行：");
    			reader = new BufferedReader(new FileReader(file));
    			String line = null;
    			int n = 0;
    			// 一次读入一行，直到读入null为文件结束
    			while ((line = reader.readLine()) != null) {
    				String[] arryLine = line.split("\001");
    				Map<String, Object> map = new HashMap<String, Object>();
    				
    				for (int i = 0; i < arryLine.length; i++) {
    					map.put("col" + i, arryLine[i]);
    				}
    				rowlst.add(map);
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    		} finally {
    			if (reader != null) {
    				try {
    					reader.close();
    				} catch (IOException e1) {
    				}
    			}
    		}
    	} else if(file.length() == 0){
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("col0", "没有数据，请核对参数及模板") ;
    		rowlst.add(map) ;
    	} else {
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("col0", "文件过大,请联系dataCenter！") ;
    		rowlst.add(map) ;
    	}

        model.put("colTitleList", colTitleList);
        model.put("rowlst", rowlst);
        return "/hive_admin/hive_admin_job_view";
    }
    
    @RequestMapping(value = "/deleteJob", method = RequestMethod.GET)
    public String deleteJob(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                               HttpServletRequest request, HttpServletResponse response, ModelMap model) {

        String jobId = request.getParameter("jobId");
        
        hiveAdminJobService.delete(jobId);
        
        return "forward:/hiveadmin/preListFrame.do";
    }
}