package com.douguo.crm.dao;

import com.common.base.common.Page;
import com.douguo.crm.model.VIPUserNew;
import com.douguo.dc.util.Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository("vipUserNewDao")
public class VIPUserNewDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final String querySQL = "select * from crm_vip_user_new where id=?";
    private final String querySQLUID = "select * from crm_vip_user_new where user_id=?";
    private final String queryVipUserNewListSQL = "SELECT * FROM crm_vip_user_new";
    private final String insertSQL = "INSERT INTO crm_vip_user_new (user_id, username,headicon,nickname,tag_id,principal,createtime) VALUES (?,?,?,?,?,?,?);";
    private final String updateTagSQL = "UPDATE crm_vip_user_new SET tag_id = ? WHERE id = ? ;";
    private final String updateSQL = "update crm_vip_user_new set user_id=?,username=?,headicon=?,nickname=?,tag_id=?,principal=?,createtime=? where id=?";
    private final String deleteSQL = "delete from crm_vip_user_new where id=?";

    /**
     * 获取新用户列表
     *
     * @return
     */
    public List<Map<String, Object>> queryVipUserNewListAll() {
        Util.showSQL(queryVipUserNewListSQL, new Object[]{});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryVipUserNewListSQL, new Object[]{});
        return rowlst;
    }

    /**
     * 获取用户列表
     *
     * @param userId
     * @param userName
     * @param nickName
     * @param tagId
     * @param principal
     * @return
     */
    public Page queryVipUserNew(String userId, String userName, String nickName, String tagId, String principal, int pageNo, int pageSize, String sort) {
        String querySQL = "SELECT * FROM crm_vip_user_new where 1=1 ";
        String countSQL = "SELECT count(1) FROM crm_vip_user_new where 1=1 ";
        StringBuffer filterSQL = new StringBuffer();
        //用户id
        if (StringUtils.isNotBlank(userId)) {
            filterSQL.append(" and user_id=" + userId);
        }
        //用户名称
        if (StringUtils.isNotBlank(userName)) {
            filterSQL.append(" and username='" + userName + "'");
        }
        //昵称
        if (StringUtils.isNotBlank(nickName)) {
            filterSQL.append(" and nickname='" + nickName + "'");
        }
        //标签
        if (StringUtils.isNotBlank(tagId)) {
            filterSQL.append(" and tag_id=" + tagId);
        }
        //负责人
        if (StringUtils.isNotBlank(principal)) {
            filterSQL.append(" and principal='" + principal + "'");
        }

        if (StringUtils.isNotBlank(sort)) {
            sort = " order by " + sort;
        }

        //
        Page page = null;
        int count = jdbcTemplate.queryForInt(countSQL + filterSQL.toString(), new Object[]{});

        if (count == 0) {
            page = new Page();
        } else {
            int start = (pageNo - 1) * pageSize;
            String strLimit = " limit " + start + "," + pageSize;
            String strSQL = querySQL + filterSQL.toString() + sort + strLimit;

            Util.showSQL(strSQL, new Object[]{});
            List<VIPUserNew> rowlst = jdbcTemplate.query(strSQL, new Object[]{}, new VIPUserNewRowMapper());
            page = new Page(start, count, pageSize, rowlst);
        }

        return page;
    }

    public List<VIPUserNew> getVipUserNew(String id) {
        Object[] params = new Object[]{id};
        List<VIPUserNew> list = jdbcTemplate.query(querySQL, params, new VIPUserNewRowMapper());
        return list;
    }

    public List<VIPUserNew> getVipUserNewByUID(String userId) {
        Object[] params = new Object[]{userId};
        List<VIPUserNew> list = jdbcTemplate.query(querySQLUID, params, new VIPUserNewRowMapper());
        return list;
    }

    public int insertVipUserNew(VIPUserNew vipUserNew) {
        Object[] parms = new Object[]{vipUserNew.getUserId(), vipUserNew.getUserName(), vipUserNew.getHeadIcon(), vipUserNew.getNickName(),
                vipUserNew.getTagId(), vipUserNew.getPrincipal(), vipUserNew.getCreatetime()};
        Util.showSQL(insertSQL, parms);
        return jdbcTemplate.update(insertSQL, parms);
    }

    /**
     * 更新用户Tag
     *
     * @param id
     * @param tag_id
     * @return
     */

    public int updateVipUserNewTag(int id, int tag_id) {
        Util.showSQL(updateTagSQL, new Object[]{tag_id, id});
        return jdbcTemplate.update(updateTagSQL, tag_id, id);
    }

    /**
     * 更新
     *
     * @return
     */
    public boolean update(VIPUserNew vipUserNew) {
        int n = jdbcTemplate.update(updateSQL, vipUserNew.getUserId(), vipUserNew.getUserName(), vipUserNew.getHeadIcon(), vipUserNew.getNickName(),
                vipUserNew.getTagId(), vipUserNew.getPrincipal(), vipUserNew.getCreatetime(), vipUserNew.getId());
        if (n > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getTotal(String tablename) {
        String totalSQL = "select count(*) from " + tablename;
        int total = 0;
        try {
            total = jdbcTemplate.queryForObject(totalSQL, Integer.class);
        } catch (Exception e) {
            total = 0;
        }
        return total;
    }

    public class VIPUserNewRowMapper implements RowMapper<VIPUserNew> {
        @Override
        public VIPUserNew mapRow(ResultSet rs, int rowNum) throws SQLException {
            VIPUserNew vipUserNew = new VIPUserNew();
            vipUserNew.setId(rs.getInt("id"));
            vipUserNew.setUserId(rs.getInt("user_id"));
            vipUserNew.setUserName(rs.getString("username"));
            vipUserNew.setHeadIcon(rs.getString("headicon"));
            vipUserNew.setNickName(rs.getString("nickname"));
            //
            vipUserNew.setTagId(rs.getInt("tag_id"));
            vipUserNew.setPrincipal(rs.getString("principal"));
            vipUserNew.setCreatetime(rs.getString("createtime"));

            return vipUserNew;
        }
    }

    public int delete(Integer id) {
        return jdbcTemplate.update(deleteSQL, id);
    }

}