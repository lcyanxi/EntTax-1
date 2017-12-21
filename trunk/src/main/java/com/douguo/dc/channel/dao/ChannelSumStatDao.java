package com.douguo.dc.channel.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.douguo.dc.util.Util;

@Repository("channelSumStatDao")
public class ChannelSumStatDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 渠道汇总监控-按日
     *
     * @param startDate
     * @param endDate
     * @param order
     * @return
     */
    public List<Map<String, Object>> queryChannelSumList(String startDate, String endDate, String... app) {
        return queryChannelSumList(startDate, endDate, "", app);
    }

    /**
     * 渠道汇总监控-按日
     *
     * @param startDate
     * @param endDate
     * @param channelType1
     * @param app
     * @return
     */
    public List<Map<String, Object>> queryChannelSumList(String startDate, String endDate, String channelType1, String... app) {

        String appSql = "";
        for (int i = 0; i < app.length; i++) {
            if (appSql.equals("")) {
                appSql = " app=" + app[i];
            } else {
                appSql += " or app=" + app[i];
            }
        }

        String sql = "select dau.statdate,dau.app,dacd.channel_name,dau.channel,dau.daus,newuser.news,retain1.userlast1,retain7.userlast7," +
                "       dacd.channel_type_1,dacd.channel_type_2,dacd.channel_type_3,dacd.channel_tag,dacd.user_name,dacd.channel_cooperation,dacd.channel_plat  "
                + "from ("
                + "		select statdate,app,channel,sum(uid) as daus "
                + "		from dd_app_dau_stat where statdate>=? and statdate<=? and ( " + appSql + ") "
                + "		group by statdate,app,binary channel"
                + ") dau left outer join ("
                + "		select statdate,client as app,dimension_name as channel,stat_value as news  "
                + "		from dd_app_collection_stat "
                + "		where stat_key_id=4 and time_type='TODAY' and dimension_type='CHANNEL' and statdate>=? and statdate<=?  and (client=3 or client=4)"
                + ") newuser on (dau.statdate=newuser.statdate and binary dau.channel=newuser.channel and dau.app=newuser.app) left outer join ("
                + "		select t.statdate,t.client as app,t.dimension_name as channel,t.stat_value as userlast1 "
                + "		from dd_app_user_retained_stat t "
                + "		where t.stat_key_id=18 and t.statdate>=? and t.statdate<=? and t.dimension_type='CHANNEL' and time_type='LAST1' and (t.client=3 or t.client=4)"
                + ") retain1 on (dau.statdate=retain1.statdate and binary dau.channel=retain1.channel and dau.app=retain1.app) left outer join ("
                + "		select t.statdate,t.client as app,t.dimension_name as channel,t.stat_value as userlast7 "
                + "		from dd_app_user_retained_stat t "
                + "		where t.stat_key_id=18 and t.statdate>=? and t.statdate<=? and t.dimension_type='CHANNEL' and time_type='LAST7' and (t.client=3 or t.client=4)"
                + ") retain7 on (dau.statdate=retain7.statdate and binary dau.channel=retain7.channel and dau.app=retain7.app) "
                + " left outer join dd_app_channel_dict dacd on binary dau.channel=dacd.channel_code ";

        if (StringUtils.isNotBlank(channelType1)) {
            sql += " where dacd.channel_type_1=" + channelType1;
        }
        sql += " order by dau.statdate,newuser.news desc";

        Util.showSQL(sql,
                new Object[]{startDate, endDate, startDate, endDate, startDate, endDate, startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate, startDate,
                endDate, startDate, endDate, startDate, endDate});
        return rowlst;
    }

    /**
     * 渠道汇总监控-按渠道汇总
     *
     * @param startDate
     * @param endDate
     * @param userName
     * @param channelPlat
     * @param channelType1
     * @param channelType2
     * @param channelType3
     * @param channelTag
     * @param app
     * @return
     */
    public List<Map<String, Object>> queryChannelSum(String startDate, String endDate, String userName, String channelPlat, String channelType1, String channelType2, String channelType3, String channelTag, String... app) {

        String appSql = "";
        for (int i = 0; i < app.length; i++) {
            if (appSql.equals("")) {
                appSql = " app=" + app[i];
            } else {
                appSql += " or app=" + app[i];
            }
        }

        String clientSql = "";
        for (int i = 0; i < app.length; i++) {
            if (clientSql.equals("")) {
                clientSql = " client =" + app[i];
            } else {
                clientSql += " or client =" + app[i];
            }
        }


        String sql = "select dacd.channel_name,dau.channel,dau.daus,newuser.news,newuser.avg_news,retain1.userlast1,retain7.userlast7,dacd.channel_type_1,dacd.channel_type_2,dacd.channel_tag  "
                + "from ("
                + "		select channel,sum(uid) as daus "
                + "		from dd_app_dau_stat "
                + "       where statdate>=? and statdate<=? and ( " + appSql + ") "
                + "		group by binary channel"
                + ") dau left outer join ("
                + "		select dimension_name as channel,sum(stat_value) as news,avg(stat_value) as avg_news  "
                + "		from dd_app_collection_stat "
                + "		where stat_key_id=4 and time_type='TODAY' and dimension_type='CHANNEL' and statdate>=? and statdate<=?  "
                + "         and (" + clientSql + ") "
                + "     group by  dimension_name"
                + ") newuser on (binary dau.channel=newuser.channel) left outer join ("
                + "		select t.dimension_name as channel,sum(t.stat_value) as userlast1 "
                + "		from dd_app_user_retained_stat t "
                + "		where t.stat_key_id=18 and t.statdate>=? and t.statdate<=? and t.dimension_type='CHANNEL' and time_type='LAST1' "
                + "         and ( " + clientSql + ")"
                + "     group by t.dimension_name"
                + ") retain1 on (binary dau.channel=retain1.channel) left outer join ("
                + "		select t.dimension_name as channel,sum(t.stat_value) as userlast7 "
                + "		from dd_app_user_retained_stat t "
                + "		where t.stat_key_id=18 and t.statdate>=? and t.statdate<=? and t.dimension_type='CHANNEL' and time_type='LAST7' "
                + "         and (" + clientSql + ")"
                + "     group by t.dimension_name"
                + ") retain7 on (binary dau.channel=retain7.channel) "
                + " left outer join dd_app_channel_dict dacd on binary dau.channel=dacd.channel_code "
                + " where 1=1 ";

        //String userName, String channelPlat, String channelType1, String channelType2, String channelType3, String channelTag

        if (StringUtils.isNotBlank(userName)) {
            sql += " and dacd.user_name='" + userName + "'";
        }

        if (StringUtils.isNotBlank(channelPlat)) {
            sql += " and dacd.channel_plat=" + channelPlat;
        }

        if (StringUtils.isNotBlank(channelType1)) {
            sql += " and dacd.channel_type_1=" + channelType1;
        }

        if (StringUtils.isNotBlank(channelType2)) {
            sql += " and dacd.channel_type_2=" + channelType2;
        }

        if (StringUtils.isNotBlank(channelType3)) {
            sql += " and dacd.channel_type_3=" + channelType3;
        }

        if (StringUtils.isNotBlank(channelTag)) {
            sql += " and dacd.channel_tag=" + channelTag;
        }

        sql += " order by newuser.news desc";

        Util.showSQL(sql, new Object[]{startDate, endDate, startDate, endDate, startDate, endDate, startDate, endDate});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, new Object[]{startDate, endDate, startDate, endDate, startDate, endDate, startDate, endDate});
        return rowlst;
    }

    /**
     * 大类统计
     *
     * @param startDate
     * @param endDate
     * @param app
     * @return
     */
    public List<Map<String, Object>> queryChannelListType1(String startDate, String endDate, String... app) {
        String sql = "select dimension_name as channel,stat_value as uid,client,statdate " +
                "	from dd_app_collection_stat " +
                "	where statdate>=? and statdate<=? and time_type='TODAY' and dimension_type='CHANNEL' AND stat_key_id=4 ";

        String appSql = "";
        for (int i = 0; i < app.length; i++) {
            if (appSql.equals("")) {
                appSql = " client=" + app[i];
            } else {
                appSql += " or client=" + app[i];
            }
        }

        sql = " select dacs.statdate,dacd.channel_type_1 as type1,sum(uid) as uid " +
                "from (" +
                "		select dimension_name as channel,stat_value as uid,client,statdate " +
                "		from dd_app_collection_stat " +
                "		where time_type='TODAY' and dimension_type='CHANNEL' AND stat_key_id=4 " +
                "			and statdate>=? and statdate<=? ";
        if (StringUtils.isNotBlank(appSql)) {
            sql += " AND (" + appSql + ") ";
        }
        sql += ") dacs join dd_app_channel_dict dacd on dacs.channel= dacd.channel_code ";


        List<String> listParm = new ArrayList<String>();
        listParm.add(startDate);
        listParm.add(endDate);


        if (StringUtils.isNotBlank(appSql)) {
            sql += " AND (" + appSql + ") ";
        }

        sql += " group by dacs.statdate,dacd.channel_type_1 order by statdate,uid desc;";

        Util.showSQL(sql, listParm.toArray());
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, listParm.toArray());
        return rowlst;
    }

    /**
     * 类别统计
     *
     * @param startDate
     * @param endDate
     * @param channelType1
     * @param app
     * @return
     */
    public List<Map<String, Object>> queryChannelListType2(String startDate, String endDate, String channelType1, String... app) {
        String sql = "select dimension_name as channel,stat_value as uid,client,statdate " +
                "	from dd_app_collection_stat " +
                "	where statdate>=? and statdate<=? and time_type='TODAY' and dimension_type='CHANNEL' AND stat_key_id=4 ";

        sql = " select dacs.statdate,dacd.channel_type_2 as type2,sum(uid) as uid " +
                "from (" +
                "		select dimension_name as channel,stat_value as uid,client,statdate " +
                "		from dd_app_collection_stat " +
                "		where time_type='TODAY' and dimension_type='CHANNEL' AND stat_key_id=4 " +
                "			and statdate>=? and statdate<=? " +
                ") dacs left outer join dd_app_channel_dict dacd on dacs.channel= dacd.channel_code " +
                " where dacd.channel_type_1=? ";


        List<String> listParm = new ArrayList<String>();
        listParm.add(startDate);
        listParm.add(endDate);
        listParm.add(channelType1);

        String appSql = "";
        for (int i = 0; i < app.length; i++) {
            if (appSql.equals("")) {
                appSql = " client=" + app[i];
            } else {
                appSql += " or client=" + app[i];
            }
        }

        if (StringUtils.isNotBlank(appSql)) {
            sql += " AND (" + appSql + ") ";
        }

        sql += " group by dacs.statdate,dacd.channel_type_2 order by statdate,uid desc;";

        Util.showSQL(sql, listParm.toArray());
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql, listParm.toArray());
        return rowlst;
    }
}