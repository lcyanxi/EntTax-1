package com.zyz.open.hiveadmin.web;

import com.douguo.dc.common.web.BaseController;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;
import com.douguo.dc.util.DateUtil;
import com.zyz.open.hiveadmin.model.HiveAdminJobTemplate;
import com.zyz.open.hiveadmin.service.HiveAdminJobTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("/hiveadmin/tplt")
public class HiveAdminJobTemplateController extends BaseController {

    @Autowired
    private HiveAdminJobTemplateService hiveAdminJobTemplateService;

    @RequestMapping(value = "/queryJson")
    public void queryJson(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {

        request.setCharacterEncoding("utf-8");
        String rowsLimit = request.getParameter("rows"); // 取出每一页显示的行数
        String sidx = request.getParameter("sidx"); // 取出排序的项
        String sord = request.getParameter("sord"); // 取出排序方式：升序，降序
        String level = request.getParameter("level"); //
        //

        if (rowsLimit == null) // 设置每一页显示行数的默认值
        {
            rowsLimit = "10";
        }
        JQGridUtil t = new JQGridUtil();
        int nPage = 1; // 当前显示的页数
        int total = 1; // 要显示的总的页数，初始值为1

        if (StringUtils.isBlank(level)) {
            level = "1";
        }

        List<HiveAdminJobTemplate> list = hiveAdminJobTemplateService.queryAll();

        // int records = data.length;
        int records = list.size();

        total = records / Integer.parseInt(rowsLimit) + 1; // 计算总的页数
        String[][] arryFun = new String[list.size()][];

        for (int i = 0; i < list.size(); i++) {
            HiveAdminJobTemplate fun = list.get(i);
            arryFun[i] = new String[]{String.valueOf(fun.getId()), fun.getTemplateUid(), fun.getTemplateName(), fun.getTemplateGroup(), String.valueOf(fun.getSort()),
                    fun.getTemplateDesc(), String.valueOf(fun.getCreateTime()),
                    "<a href='/hiveadmin/tplt/preEdit.do?id=" + fun.getId() + "' title='修改' >修改</a>"};
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
     * 模板管理-管理页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/preList")
    public String preList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //
        return "/hive_admin/template/hive_admin_job_template_manager";
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
        String id = request.getParameter("id");
        //
        //获取类型list
        HiveAdminJobTemplate tplt = hiveAdminJobTemplateService.getHiveAdminJobTemplate(id);
        model.put("template", tplt);

        return "/hive_admin/template/hive_admin_job_template_modify";
    }

    @RequestMapping(value = "/insert")
    public String insert(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");

        String showListTitle = request.getParameter("showListTitle");
        String showVarTitle = request.getParameter("showVarTitle");
        String templateName = request.getParameter("templateName");
        String templateUid = request.getParameter("templateUid");
        String templateContent = request.getParameter("templateContent");
        String templateGroup = request.getParameter("templateGroup");
        String sort = request.getParameter("sort");
        String templateDesc = request.getParameter("templateDesc");
        String uid = getUser(request).getUid();
        String createTime = DateUtil.date2Str(new Date(), "yyyy-MM-dd");
        int nSort = 0;

        //排序
        if (StringUtils.isNotBlank(sort)) {
            nSort = Integer.parseInt(sort);
        }

        HiveAdminJobTemplate tplt = new HiveAdminJobTemplate();

        tplt.setUid(uid);
        tplt.setShowListTitle(showListTitle);
        tplt.setShowVarTitle(showVarTitle);
        tplt.setTemplateName(templateName);
        tplt.setTemplateUid(templateUid);
        tplt.setTemplateContent(templateContent);
        tplt.setTemplateGroup(templateGroup);
        tplt.setSort(nSort);
        tplt.setTemplateDesc(templateDesc);
        tplt.setUpdateTime(createTime);
        tplt.setCreateTime(createTime);

        hiveAdminJobTemplateService.insert(tplt);
        return "redirect:/hiveadmin/tplt/preList.do";
    }

    @RequestMapping(value = "/update")
    public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");

        String id = request.getParameter("id");
        String showListTitle = request.getParameter("showListTitle");
        String showVarTitle = request.getParameter("showVarTitle");
        String templateName = request.getParameter("templateName");
        String templateUid = request.getParameter("templateUid");
        String templateContent = request.getParameter("templateContent");
        String templateGroup = request.getParameter("templateGroup");
        String sort = request.getParameter("sort");
        String templateDesc = request.getParameter("templateDesc");
        String uid = getUser(request).getUid();
        String updateTime = DateUtil.date2Str(new Date(), "yyyy-MM-dd");
        int nSort = 0;

        //排序
        if (StringUtils.isNotBlank(sort)) {
            nSort = Integer.parseInt(sort);
        }

        if (id != null && id.trim().length() > 0) {
            HiveAdminJobTemplate tplt = hiveAdminJobTemplateService.getHiveAdminJobTemplate(id);

            if (tplt != null) {
                //排序
                if (StringUtils.isNotBlank(sort)) {
                    nSort = Integer.parseInt(sort);
                }

                tplt.setUid(uid);
                tplt.setShowListTitle(showListTitle);
                tplt.setShowVarTitle(showVarTitle);
                tplt.setTemplateName(templateName);
                tplt.setTemplateUid(templateUid);
                tplt.setTemplateContent(templateContent);
                tplt.setTemplateGroup(templateGroup);
                tplt.setSort(nSort);
                tplt.setTemplateDesc(templateDesc);
                tplt.setUpdateTime(updateTime);

                hiveAdminJobTemplateService.update(tplt);
            }
        }
        return "redirect:/hiveadmin/tplt/preList.do";
    }
}