package com.douguo.dc.mall.dao;

import com.douguo.dc.util.Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("mallPositionStatDao")
public class MallPositionStatDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 返回指定日期内的 position 信息
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> queryList(String startDate, String endDate) {
        String sql = "SELECT * " +
                "   FROM `dd_mall_position_stat` " +
                "   WHERE statdate BETWEEN ? AND ? " +
                "   ORDER BY statdate";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> queryListSumWithPageView(String startDate, String endDate, String order) {

        String sql = "SELECT page,view,sum(clicks) as clicks,sum(product_pv) as product_pv,sum(product_uv) as product_uv,"
                + "		sum(goods) as goods,sum(pays) as pays,sum(pay_uids) as pay_uids,sum(moneys) as moneys "
                + " FROM `dd_mall_position_stat` "
                + "	WHERE statdate BETWEEN ? AND ?  "
                + "	group by statdate,page,view";
        if (StringUtils.isNotEmpty(order)) {
            sql += " order by " + order + " desc";
        }

        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate
                .queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }
}
