package com.douguo.dc.taskmonitor.web;

import com.douguo.dc.taskmonitor.model.Operator;
import com.douguo.dc.taskmonitor.service.TaskMonitorService;
import com.douguo.dc.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lichang on 2017/10/23.
 */
@Controller
@RequestMapping("/task")
public class TaskMonitorController {

    @Autowired
    private TaskMonitorService taskMonitorService;

    @RequestMapping(value = "/queryList", method = RequestMethod.GET)
    public String toTastMonitorPage(HttpServletRequest request, ModelMap model) {
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

        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("data", taskMonitorService.queryAll(startDate, endDate));

        return "/taskmonitor/task_monitor_stat";
    }


    @RequestMapping(value = "/queryOperateList", method = RequestMethod.GET)
    public String toTaskOpertorPage(HttpServletRequest request, ModelMap model) {

            String sql = "select * from dd_operate_user_id_dict";
            String resultPage="/taskmonitor/user_operate";


        model.put("data", taskMonitorService.queryOperatorList(sql));
        return resultPage;
    }


    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    @ResponseBody
    public Map deleteOperatorDataById(HttpServletRequest request, @RequestParam(value = "id") int id) {
        Map map = new HashMap();


          String  sql = "DELETE FROM  dd_operate_user_id_dict WHERE id=?";

        int result = taskMonitorService.deleteUserById(id, sql);
        String message = "";
        int status = 0;
        if (result > 0) {
            message = "删除成功";
            status = 1;
        } else {
            message = "删除失败";
        }
        map.put("message", message);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public Map addOperatorUser(HttpServletRequest request, @RequestParam(value = "name") String name, @RequestParam(value = "userid") String userid,
                               @RequestParam(value = "group") String group, @RequestParam(value = "workdesc") String workdesc,
                               @RequestParam(value = "cooks") int cooks, @RequestParam(value = "dishs") int dishs,
                               @RequestParam(value = "posts") int posts, @RequestParam(value = "questions") int questions,
                               @RequestParam(value = "caidans") int caidans, @RequestParam(value = "cook_comments") int cook_comments,
                               @RequestParam(value = "dish_comments") int dish_comments, @RequestParam(value = "dish_likes") int dish_likes,
                               @RequestParam(value = "post_replys") int post_replys, @RequestParam(value = "questions_replys") int questions_replys,
                               @RequestParam(value = "mall") int mall, @RequestParam(value = "live") int live) {

        String insertSQL = "insert into dd_operate_user_id_dict(user_id,name,group_in,workdesc,cooks,dishs,posts,questions,caidans," +
                    "cook_comments,dish_comments,dish_likes,post_replys,questions_replys,mall,live) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        Operator operator = new Operator();

        operator.setLive(live);
        operator.setMall(mall);
        operator.setQuestions_replys(questions_replys);
        operator.setQuestions(questions);
        operator.setPost_replys(post_replys);
        operator.setDish_likes(dish_likes);
        operator.setCook_comments(cook_comments);
        operator.setDishs(dishs);
        operator.setWorkdesc(workdesc);
        operator.setName(name);
        operator.setDish_comments(dish_comments);
        operator.setCaidans(caidans);
        operator.setPosts(posts);
        operator.setCooks(cooks);
        operator.setGroup(group);
        operator.setUserid(userid);


        int result = taskMonitorService.addUser(operator, insertSQL);
        String message = "";
        int status = 0;
        if (result > 0) {
            message = "添加成功";
            status = 1;
        } else {
            message = "添加失败";
        }
        Map map = new HashMap();

        map.put("message", message);
        map.put("status", status);
        return map;
    }


    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ResponseBody
    public Map updateOpertorUser(HttpServletRequest request,@RequestParam(value = "name") String name, @RequestParam(value = "userid") String userid,
                                 @RequestParam(value = "group") String group, @RequestParam(value = "workdesc") String workdesc,
                                 @RequestParam(value = "cooks") int cooks, @RequestParam(value = "dishs") int dishs,
                                 @RequestParam(value = "posts") int posts, @RequestParam(value = "questions") int questions,
                                 @RequestParam(value = "caidans") int caidans, @RequestParam(value = "cook_comments") int cook_comments,
                                 @RequestParam(value = "dish_comments") int dish_comments, @RequestParam(value = "dish_likes") int dish_likes,
                                 @RequestParam(value = "post_replys") int post_replys, @RequestParam(value = "questions_replys") int questions_replys,
                                 @RequestParam(value = "mall") int mall, @RequestParam(value = "live") int live,
                                 @RequestParam(value = "id") int id) {

        String updateSQL = "update dd_operate_user_id_dict set user_id=?,name=?,group_in=?,workdesc=?,cooks=?,dishs=?,posts=?,questions=?," +
                    "caidans=?,cook_comments=?,dish_comments=?,dish_likes=?,post_replys=?,questions_replys=?,mall=?,live=? where id=? ";


        Operator operator = new Operator();

        operator.setLive(live);
        operator.setMall(mall);
        operator.setQuestions_replys(questions_replys);
        operator.setQuestions(questions);
        operator.setPost_replys(post_replys);
        operator.setDish_likes(dish_likes);
        operator.setCook_comments(cook_comments);
        operator.setDishs(dishs);
        operator.setWorkdesc(workdesc);
        operator.setName(name);
        operator.setDish_comments(dish_comments);
        operator.setCaidans(caidans);
        operator.setPosts(posts);
        operator.setCooks(cooks);
        operator.setGroup(group);
        operator.setUserid(userid);
        operator.setId(id);


        int result = taskMonitorService.update(operator, updateSQL);
        String message = "";
        int status = 0;
        if (result > 0) {
            message = "更新成功";
            status = 1;
        } else {
            message = "更新失败，请重试";
        }
        Map map = new HashMap();

        map.put("message", message);
        map.put("status", status);
        return map;
    }



    @RequestMapping(value = "/queryOperateListWeek", method = RequestMethod.GET)
    public String toTaskOpertorPageWeek(HttpServletRequest request, ModelMap model) {


          String   sql = "select * from dd_operate_user_id_week_dict";
           String resultPage="/taskmonitor/user_operate_week";


        model.put("data", taskMonitorService.queryOperatorList(sql));
        return resultPage;
    }

    @RequestMapping(value = "/deleteUserWeek", method = RequestMethod.GET)
    @ResponseBody
    public Map deleteOperatorDataByIdWeek(HttpServletRequest request, @RequestParam(value = "id") int id) {
        Map map = new HashMap();

        String sql = "DELETE FROM  dd_operate_user_id_week_dict WHERE id=?";

        int result = taskMonitorService.deleteUserById(id, sql);
        String message = "";
        int status = 0;
        if (result > 0) {
            message = "删除成功";
            status = 1;
        } else {
            message = "删除失败";
        }
        map.put("message", message);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = "/addUserWeek", method = RequestMethod.POST)
    @ResponseBody
    public Map addOperatorUserWeek(HttpServletRequest request, @RequestParam(value = "name") String name, @RequestParam(value = "userid") String userid,
                               @RequestParam(value = "group") String group, @RequestParam(value = "workdesc") String workdesc,
                               @RequestParam(value = "cooks") int cooks, @RequestParam(value = "dishs") int dishs,
                               @RequestParam(value = "posts") int posts, @RequestParam(value = "questions") int questions,
                               @RequestParam(value = "caidans") int caidans, @RequestParam(value = "cook_comments") int cook_comments,
                               @RequestParam(value = "dish_comments") int dish_comments, @RequestParam(value = "dish_likes") int dish_likes,
                               @RequestParam(value = "post_replys") int post_replys, @RequestParam(value = "questions_replys") int questions_replys,
                               @RequestParam(value = "mall") int mall, @RequestParam(value = "live") int live) {

        String type = request.getParameter("type");
        String insertSQL = "insert into dd_operate_user_id_week_dict(user_id,name,group_in,workdesc,cooks,dishs,posts,questions,caidans," +
                    "cook_comments,dish_comments,dish_likes,post_replys,questions_replys,mall,live) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        Operator operator = new Operator();

        operator.setLive(live);
        operator.setMall(mall);
        operator.setQuestions_replys(questions_replys);
        operator.setQuestions(questions);
        operator.setPost_replys(post_replys);
        operator.setDish_likes(dish_likes);
        operator.setCook_comments(cook_comments);
        operator.setDishs(dishs);
        operator.setWorkdesc(workdesc);
        operator.setName(name);
        operator.setDish_comments(dish_comments);
        operator.setCaidans(caidans);
        operator.setPosts(posts);
        operator.setCooks(cooks);
        operator.setGroup(group);
        operator.setUserid(userid);


        int result = taskMonitorService.addUser(operator, insertSQL);
        String message = "";
        int status = 0;
        if (result > 0) {
            message = "添加成功";
            status = 1;
        } else {
            message = "添加失败";
        }
        Map map = new HashMap();

        map.put("message", message);
        map.put("status", status);
        return map;
    }


    @RequestMapping(value = "/updateUserWeek", method = RequestMethod.POST)
    @ResponseBody
    public Map updateOpertorUserWeek(HttpServletRequest request,@RequestParam(value = "name") String name, @RequestParam(value = "userid") String userid,
                                 @RequestParam(value = "group") String group, @RequestParam(value = "workdesc") String workdesc,
                                 @RequestParam(value = "cooks") int cooks, @RequestParam(value = "dishs") int dishs,
                                 @RequestParam(value = "posts") int posts, @RequestParam(value = "questions") int questions,
                                 @RequestParam(value = "caidans") int caidans, @RequestParam(value = "cook_comments") int cook_comments,
                                 @RequestParam(value = "dish_comments") int dish_comments, @RequestParam(value = "dish_likes") int dish_likes,
                                 @RequestParam(value = "post_replys") int post_replys, @RequestParam(value = "questions_replys") int questions_replys,
                                 @RequestParam(value = "mall") int mall, @RequestParam(value = "live") int live,
                                 @RequestParam(value = "id") int id) {

        String type = request.getParameter("type");
        String updateSQL = "update dd_operate_user_id_week_dict set user_id=?,name=?,group_in=?,workdesc=?,cooks=?,dishs=?,posts=?,questions=?," +
                    "caidans=?,cook_comments=?,dish_comments=?,dish_likes=?,post_replys=?,questions_replys=?,mall=?,live=? where id=? ";


        Operator operator = new Operator();

        operator.setLive(live);
        operator.setMall(mall);
        operator.setQuestions_replys(questions_replys);
        operator.setQuestions(questions);
        operator.setPost_replys(post_replys);
        operator.setDish_likes(dish_likes);
        operator.setCook_comments(cook_comments);
        operator.setDishs(dishs);
        operator.setWorkdesc(workdesc);
        operator.setName(name);
        operator.setDish_comments(dish_comments);
        operator.setCaidans(caidans);
        operator.setPosts(posts);
        operator.setCooks(cooks);
        operator.setGroup(group);
        operator.setUserid(userid);
        operator.setId(id);


        int result = taskMonitorService.update(operator, updateSQL);
        String message = "";
        int status = 0;
        if (result > 0) {
            message = "更新成功";
            status = 1;
        } else {
            message = "更新失败，请重试";
        }
        Map map = new HashMap();

        map.put("message", message);
        map.put("status", status);
        return map;
    }

}
