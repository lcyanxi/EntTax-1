package com.douguo.dc.mall.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.douguo.dc.util.Util;

@Repository("appTuanDao")
public class AppTuanDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryList(String startDate, String endDate) {
        String sql = "SELECT statdate,list_pv,list_uv,detail_pv,detail_uv,order_pv,pay_sign_pv,pay_success_pv FROM `dd_app_tuan_stat` WHERE statdate BETWEEN ? AND ? ORDER BY statdate";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> queryListSum(String startDate, String endDate) {
        String sql = "SELECT sum(list_pv) as list_sum,sum(list_uv) as list_uv_sum,sum(detail_pv) as detail_sum,sum(detail_uv) as detail_uv_sum,sum(order_pv) as order_sum,sum(pay_sign_pv) as pay_sign_sum,sum(pay_success_pv) as pay_success_sum FROM `dd_app_tuan_stat` WHERE statdate BETWEEN ? AND ? ";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    private List<Map<String, Object>> queryTableList(String tableName, String startDate, String endDate, String order) {
        String sql = "SELECT * FROM " + tableName + " WHERE statdate BETWEEN ? AND ? " + " ORDER BY statdate";

        if (StringUtils.isNotEmpty(order)) {
            sql += "," + order + " desc";
        }

        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> queryProductList(String startDate, String endDate, String order) {
        if (StringUtils.isEmpty(order)) {
            order = "clicks";
        }

        String sql = "SELECT tuan_id,max(tuan_name) as tuan_name,sum(clicks) as clicks,sum(uids) as uids,sum(orders) as orders," +
                " sum(pays) as pays,sum(pay_uids) as pay_uids,sum(goods) as goods,sum(moneys) as moneys " +
                " FROM `dd_app_tuan_product_stat` WHERE statdate BETWEEN ? AND ? group by tuan_id ";
        if (StringUtils.isNotEmpty(order)) {
            sql += " order by " + order + " desc";
        }

        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> queryProductList(String startDate, String endDate, String goodsIds, String order) {
        if (StringUtils.isEmpty(order)) {
            order = "clicks";
        }

        String sql = "SELECT tuan_id,max(tuan_name) as tuan_name,sum(clicks) as clicks,sum(uids) as uids,sum(orders) as orders,"
                + "		sum(pays) as pays,sum(pay_uids) as pay_uids,sum(goods) as goods,sum(moneys) as moneys "
                + " FROM `dd_app_tuan_product_stat` "
                + "	WHERE statdate BETWEEN ? AND ? and tuan_id in (" + goodsIds + ")"
                + "	group by tuan_id ";
        if (StringUtils.isNotEmpty(order)) {
            sql += " order by " + order + " desc";
        }

        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate
                .queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> querySingleProductList(String startDate, String endDate, String tuanId,
                                                            String order) {
        if (StringUtils.isEmpty(order)) {
            order = "clicks";
        }

        String sql = "SELECT * FROM `dd_app_tuan_product_stat` WHERE statdate BETWEEN ? AND ? and tuan_id = ?"
                + " ORDER BY statdate," + order + " desc";

        if (StringUtils.isNotEmpty(order)) {
            sql += "," + order + " desc";
        }

        Util.showSQL(sql, new Object[]{startDate, endDate, tuanId});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate, tuanId});
        return rowlst;
    }

    public List<Map<String, Object>> queryOrderSumList(String startDate, String endDate, String order) {
        if (StringUtils.isEmpty(order)) {
            order = "dtoss.moneys";
        }

        String sql = "SELECT * FROM `dd_app_tuan_order_sum_stat` dtoss join `dd_app_tuan_stat` dts on dtoss.statdate=dts.statdate WHERE dtoss.statdate BETWEEN ? AND ? "
                + " ORDER BY dtoss.statdate," + order + " desc";
        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> queryOrderTimeDistributeList(String startDate, String endDate, String order) {
        String sql = "SELECT * FROM dd_app_tuan_order_time_distribute_stat WHERE statdate BETWEEN ? AND ? "
                + " ORDER BY statdate";

        if (StringUtils.isNotEmpty(order)) {
            sql += "," + order;
        }

        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> queryOrderPayTypeList(String startDate, String endDate, String order) {
        String sql = "SELECT statdate,pay_type,sum(pays) as pays,sum(moneys) as moneys FROM  dd_app_tuan_order_pay_type_stat  WHERE statdate BETWEEN ? AND ? "
                + " group by pay_type " + " ORDER BY statdate";

        if (StringUtils.isNotEmpty(order)) {
            sql += "," + order + " desc";
        }

        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

    public List<Map<String, Object>> queryOrderSourceList(String startDate, String endDate, String order) {
        String sql = "SELECT statdate,source,sum(pays) as pays,sum(moneys) as moneys FROM  dd_app_tuan_order_pay_type_stat  WHERE statdate BETWEEN ? AND ? "
                + " group by source " + " ORDER BY statdate";

        if (StringUtils.isNotEmpty(order)) {
            sql += "," + order + " desc";
        }

        Util.showSQL(sql, new Object[]{startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate});
        return rowlst;
    }

	/*
     * public List<Map<String, Object>> queryProductOrderList(String startDate,
	 * String endDate,String order) { if(StringUtils.isEmpty(order)){ order =
	 * "dtp.clicks"; }else{ order = "dto." + order; } String sql =
	 * "SELECT dtp.statdate,dtp.tuan_id,dtp.tuan_name,dtp.clicks,dto.orders,dto.pays "
	 * +
	 * " FROM `dd_app_tuan_product_stat` dtp left join `dd_app_tuan_product_order_stat` dto"
	 * + " on (dtp.tuan_id=dto.tuan_id and dtp.statdate=dto.statdate)" +
	 * " WHERE dtp.statdate BETWEEN ? AND ? " + " ORDER BY dtp.statdate," +
	 * order + " desc"; Util.showSQL(sql, new Object[] { startDate, endDate });
	 * List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new
	 * Object[] { startDate, endDate }); return rowlst; }
	 */
}
