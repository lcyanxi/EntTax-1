package com.douguo.dc.dish.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("dishDao")
public class DishDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryDishTagList(String startDate, String endDate) {
        String sql = "SELECT statdate,tag_id,tag_name,dishs,users FROM `dd_dish_tag_stat` WHERE statdate BETWEEN ? AND ? ORDER BY statdate,dishs desc,tag_id";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    /**
     * 作品统计 - 按来源
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> queryDishListBySource(String startDate, String endDate) {
        String sql = "SELECT statdate,source,dishs,uids FROM `dd_dish_stat` WHERE statdate BETWEEN ? AND ? ORDER BY statdate,source";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    /**
     * 作品统计 - month
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> queryDishListByMonth(String startDate, String endDate) {
        String sql = "SELECT stat_year,stat_month,sum(dishs) as dishs FROM `dd_dish_stat` WHERE statdate BETWEEN ? AND ? group by stat_year,stat_month ORDER BY stat_year,stat_month";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    /**
     * 作品统计 - week
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> queryDishListByWeek(String startDate, String endDate) {
        String sql = "SELECT stat_year,stat_week,sum(dishs) as dishs FROM `dd_dish_stat` WHERE statdate BETWEEN ? AND ? group by stat_year,stat_week ORDER BY stat_year,stat_week";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    /**
     *
     */
    public List<Map<String, Object>> queryDishSumList(String startDate, String endDate) {
        //String sql = "SELECT id,dishs,dish_users,cook_dishs,cook_dish_users,other_dishs,other_dish_users,dish_favs,dish_fav_users,statdate FROM dd_dish_sum_stat WHERE statdate BETWEEN ? AND ? ORDER BY statdate";
        String sql = "SELECT * FROM dd_dish_sum_stat WHERE statdate BETWEEN ? AND ? ORDER BY statdate";
        Util.showSQL(sql, new Object[]{});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }
}