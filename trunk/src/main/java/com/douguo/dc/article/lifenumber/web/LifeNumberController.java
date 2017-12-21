package com.douguo.dc.article.lifenumber.web;

import com.douguo.dc.article.lifenumber.service.LifeNumberService;
import com.douguo.dc.article.lifenumber.util.EchartsJsonParseUtil;
import com.douguo.dc.util.DateUtil;
import com.douguo.dc.util.MathUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/article/lifenumber")
public class LifeNumberController {

    @Autowired
    private LifeNumberService lifeNumberService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "appId", required = false, defaultValue = "1") String appId,
                       HttpServletRequest request, HttpServletResponse response, ModelMap model) throws JSONException {
        if (null == appId || appId.equals("")) {
            appId = "1";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis());
        String startDate = request.getParameter("startDate");
        if (null == startDate || startDate.equals("")) {
            startDate = DateUtil.getSpecifiedDayBefore(today, 15);
        }
        String endDate = request.getParameter("endDate");
        if (null == endDate || endDate.equals("")) {
            endDate = DateUtil.getSpecifiedDayBefore(today, 1);
        }

        model.put("startDate", startDate);
        model.put("endDate", endDate);

        // 获取数据源
        List<Map<String, Object>> rowlst = lifeNumberService.queryLifeNumberList(startDate, endDate);

        /**
         * 表格数据构造 BEGIN
         */
//        model.put("rowlst", rowlrowst);

        /**
         * Echarts 图表构造 BEGIN
         */
        // echarts 数据构造
        List<Integer> seriesArticles = new ArrayList<Integer>();
        List<Integer> seriesArticlesUpload = new ArrayList<Integer>();
        List<Integer> seriesArticlesUserUpload = new ArrayList<Integer>();
        List<Integer> seriesArticlesGrap = new ArrayList<Integer>();
        List<Integer> seriesArticlesQuality = new ArrayList<Integer>();
        List<String> seriesArticlesQualityRate = new ArrayList<String>();

        List<Integer> seriesArticlesUsers = new ArrayList<Integer>();
        List<Integer> seriesArticlesApplyUsers = new ArrayList<Integer>();
        List<Integer> seriesArticlesInviteUsers = new ArrayList<Integer>();

        List<Integer> seriesArticlesPv = new ArrayList<Integer>();
        List<Integer> seriesArticlesUv = new ArrayList<Integer>();

        List<Integer> seriesArticlesAuthorFollows = new ArrayList<Integer>();
        List<Integer> seriesArticlesAuthorFavs = new ArrayList<Integer>();
        List<Integer> seriesArticlesAuthorCmmts = new ArrayList<Integer>();

        // x轴 日期坐标list
        List<Date> xAxis = new ArrayList<Date>();

        // 数据遍历转换
        for (Map<String, Object> map : rowlst) {
            seriesArticles.add((Integer) map.get("articles"));
            seriesArticlesUpload.add((Integer) map.get("articles_upload"));
            seriesArticlesUserUpload.add((Integer) map.get("articles_user_upload"));
            seriesArticlesGrap.add((Integer) map.get("articles_grab"));
            seriesArticlesQuality.add((Integer) map.get("quality_articles"));
            // 计算优质文章占比
            int quality_num = (Integer) map.get("quality_articles");
            int all_num = (Integer) map.get("articles");
            String rate = MathUtil.parseIntDivisionStr(quality_num, all_num);

            // 计算值插入list
            seriesArticlesQualityRate.add(rate);

            // 为数据源加入比例
            map.put("quality_rate",rate);

            seriesArticlesUsers.add((Integer) map.get("all_users"));
            seriesArticlesApplyUsers.add((Integer) map.get("apply_all_users"));
            seriesArticlesInviteUsers.add((Integer) map.get("invite_all_users"));

            seriesArticlesPv.add((Integer) map.get("article_views_pv"));
            seriesArticlesUv.add((Integer) map.get("article_views_uv"));

            seriesArticlesAuthorFollows.add((Integer) map.get("article_user_follows"));
            seriesArticlesAuthorFavs.add((Integer) map.get("article_user_favs"));
            seriesArticlesAuthorCmmts.add((Integer) map.get("article_user_cmmts"));

            Date date = (Date) map.get("statdate");
            if (!xAxis.contains(date)) {
                xAxis.add(date);
            }
        }

        // json 数据转换
        JSONArray jsonArray = new JSONArray();
        JSONObject objArticles = EchartsJsonParseUtil.buildJson("新增文章数", "line", seriesArticles);
        JSONObject objArticlesUpload = EchartsJsonParseUtil.buildJson("运营上传文章数", "line", seriesArticlesUpload);
        JSONObject objArticlesUserUpload = EchartsJsonParseUtil.buildJson("用户上传文章数", "line", seriesArticlesUserUpload);
        JSONObject objArticlesGrap = EchartsJsonParseUtil.buildJson("爬取文章数", "line", seriesArticlesGrap);
        JSONObject objArticlesQuality = EchartsJsonParseUtil.buildJson("优质文章数", "line", seriesArticlesQuality);
        JSONObject objArticlesQualityRate = EchartsJsonParseUtil.buildJson("优质文章占比", "line", seriesArticlesQualityRate);

        JSONObject objArticlesUsers = EchartsJsonParseUtil.buildJson("累计作者数", "line", seriesArticlesUsers);
        JSONObject objArticlesApplyUsers = EchartsJsonParseUtil.buildJson("累计申请入驻数", "line", seriesArticlesApplyUsers);
        JSONObject objArticlesInviteUsers = EchartsJsonParseUtil.buildJson("累计邀请入驻数", "line", seriesArticlesInviteUsers);

        JSONObject objArticlesPv = EchartsJsonParseUtil.buildJson("文章浏览量", "line", seriesArticlesPv);
        JSONObject objArticlesUv = EchartsJsonParseUtil.buildJson("文章浏览UV", "line", seriesArticlesUv);

        JSONObject objArticlesAuthorFollows = EchartsJsonParseUtil.buildJson("文章内关注数", "line", seriesArticlesAuthorFollows);
        JSONObject objArticlesAuthorFavs = EchartsJsonParseUtil.buildJson("文章点赞数", "line", seriesArticlesAuthorFavs);
        JSONObject objArticlesAuthorCmmts = EchartsJsonParseUtil.buildJson("文章评论数", "line", seriesArticlesAuthorCmmts);

        JSONObject objStatDate = EchartsJsonParseUtil.buildJson("","category", xAxis);

        jsonArray.put(objArticles);
        jsonArray.put(objArticlesUpload);
        jsonArray.put(objArticlesUserUpload);
        jsonArray.put(objArticlesGrap);
        jsonArray.put(objArticlesQuality);
        jsonArray.put(objArticlesQualityRate);

//        jsonArray.put(objArticlesUsers);
        jsonArray.put(objArticlesApplyUsers);
//        jsonArray.put(objArticlesInviteUsers);

        jsonArray.put(objArticlesPv);
        jsonArray.put(objArticlesUv);

        jsonArray.put(objArticlesAuthorFollows);
        jsonArray.put(objArticlesAuthorFavs);
        jsonArray.put(objArticlesAuthorCmmts);

        model.put("series", jsonArray.toString());
        model.put("xAxis", objStatDate.toString());

        model.put("rowlst", rowlst);

        model.put("rate",seriesArticlesQualityRate);
        //图表end

        return "/article/life_number/life_number_list";

    }


}