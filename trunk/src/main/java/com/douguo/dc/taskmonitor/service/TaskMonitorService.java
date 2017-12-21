package com.douguo.dc.taskmonitor.service;

import com.douguo.dc.taskmonitor.dao.TaskMonitorDao;
import com.douguo.dc.taskmonitor.model.Operator;
import com.douguo.dc.taskmonitor.model.TaskMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lichang on 2017/10/23.
 */
@Repository("taskMonitorService")
public class TaskMonitorService {

    @Autowired
    private TaskMonitorDao taskMonitorDao;

    public Map queryAll(String starDate, String endDate) {
        Map map = new HashMap();
        List listValue = new ArrayList();
        List<Map<String, Object>> list = taskMonitorDao.queryAllListGroupName(starDate, endDate);
        for (Map<String, Object> maplist : list) {

            Timestamp statdate = (Timestamp) maplist.get("statdate");
            DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date = sdf.format(statdate);
            String name = (String) maplist.get("name");
            String id = (String) maplist.get("id");
            String aim_cook_num = (String) maplist.get("aim_cook_num");
            String cook_num = (String) maplist.get("cook_num");
            String cook_rate = (String) maplist.get("cook_rate");
            String aim_dish_num = (String) maplist.get("aim_dish_num");
            String dish_num = (String) maplist.get("dish_num");
            String dish_rate = (String) maplist.get("dish_rate");
            String aim_post_num = (String) maplist.get("aim_post_num");
            String post_num = (String) maplist.get("post_num");
            String post_rate = (String) maplist.get("post_rate");
            String aim_caidan_num = (String) maplist.get("aim_caidan_num");
            String caidan_num = (String) maplist.get("caidan_num");
            String caidan_rate = (String) maplist.get("caidan_rate");
            String aim_cookcomment_num = (String) maplist.get("aim_cookcomment_num");
            String cookcomment_num = (String) maplist.get("cookcomment_num");
            String cookcomment_rate = (String) maplist.get("cookcomment_rate");
            String aim_dishcomment_num = (String) maplist.get("aim_dishcomment_num");
            String dishcomment_num = (String) maplist.get("dishcomment_num");
            String dishcomment_rate = (String) maplist.get("dishcomment_rate");
            String aim_postcomment_num = (String) maplist.get("aim_postcomment_num");
            String postcomment_num = (String) maplist.get("postcomment_num");
            String postcomment_rate = (String) maplist.get("postcomment_rate");
            String aim_questioncomment_num = (String) maplist.get("aim_questioncomment_num");
            String questioncomment_num = (String) maplist.get("questioncomment_num");
            String questioncomment_rate = (String) maplist.get("questioncomment_rate");

            TaskMonitor taskMonitor = new TaskMonitor();

            taskMonitor.setStatdate(date);
            taskMonitor.setName(name);
            taskMonitor.setId(id);
            taskMonitor.setAim_cook_num(aim_cook_num);
            taskMonitor.setCook_num(cook_num);
            taskMonitor.setCook_rate(cook_rate);
            taskMonitor.setAim_dish_num(aim_dish_num);
            taskMonitor.setDish_num(dish_num);
            taskMonitor.setDish_rate(dish_rate);
            taskMonitor.setAim_post_num(aim_post_num);
            taskMonitor.setPost_num(post_num);
            taskMonitor.setPost_rate(post_rate);
            taskMonitor.setAim_caidan_num(aim_caidan_num);
            taskMonitor.setCaidan_num(caidan_num);
            taskMonitor.setCaidan_rate(caidan_rate);
            taskMonitor.setAim_cookcomment_num(aim_cookcomment_num);
            taskMonitor.setCookcomment_num(cookcomment_num);
            taskMonitor.setCookcomment_rate(cookcomment_rate);
            taskMonitor.setAim_dishcomment_num(aim_dishcomment_num);
            taskMonitor.setDishcomment_num(dishcomment_num);
            taskMonitor.setDishcomment_rate(dishcomment_rate);
            taskMonitor.setAim_postcomment_num(aim_postcomment_num);
            taskMonitor.setPostcomment_num(postcomment_num);
            taskMonitor.setPostcomment_rate(postcomment_rate);
            taskMonitor.setAim_questioncomment_num(aim_questioncomment_num);
            taskMonitor.setQuestioncomment_num(questioncomment_num);
            taskMonitor.setQuestioncomment_rate(questioncomment_rate);


            listValue.add(taskMonitor);
        }
        map.put("task", listValue);
        return map;
    }


    /**
     * 显示用户列表
     * @return
     */
    public Map queryOperatorList(String sql) {
        Map resultMap = new HashMap();

        List<Operator> list = new ArrayList<>();
        List<Map<String, Object>> mapList = taskMonitorDao.queryOperatorList(sql);

        for (Map<String, Object> map : mapList) {
            int id = (int) map.get("id");
            String name = (String) map.get("name");
            String user_id = (String) map.get("user_id");
            String group_in = (String) map.get("group_in");
            String workdesc = (String) map.get("workdesc");
            int cooks = (int) map.get("cooks");
            int dishs = (int) map.get("dishs");
            int posts = (int) map.get("posts");
            int questions = (int) map.get("questions");
            int caidans = (int) map.get("caidans");
            int cook_comments = (int) map.get("cook_comments");
            int dish_comments = (int) map.get("dish_comments");
            int dish_likes = (int) map.get("dish_likes");
            int post_replys = (int) map.get("post_replys");
            int questions_replys = (int) map.get("questions_replys");
            int mall = (int) map.get("mall");
            int live = (int) map.get("live");

            Operator operator = new Operator();
            operator.setId(id);
            operator.setName(name);
            operator.setUserid(user_id);
            operator.setGroup(group_in);
            operator.setWorkdesc(workdesc);
            operator.setCooks(cooks);
            operator.setDishs(dishs);
            operator.setPosts(posts);
            operator.setQuestions(questions);
            operator.setCaidans(caidans);
            operator.setCook_comments(cook_comments);
            operator.setDish_comments(dish_comments);
            operator.setDish_likes(dish_likes);
            operator.setPost_replys(post_replys);
            operator.setQuestions_replys(questions_replys);
            operator.setMall(mall);
            operator.setLive(live);

            list.add(operator);
        }

        resultMap.put("operator", list);
        return resultMap;
    }

    public int deleteUserById(int id,String sql){
        return taskMonitorDao.deleteUserById(id,sql);
    }

    public int addUser(Operator operator,String insertSQL){

        return taskMonitorDao.addUser(operator,insertSQL);
    }
    public  int update(Operator operator,String updateSQL){
        return taskMonitorDao.updateUser(operator,updateSQL);
    }





}
