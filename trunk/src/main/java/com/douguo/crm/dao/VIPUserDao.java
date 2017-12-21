package com.douguo.crm.dao;

import com.common.base.common.Page;
import com.douguo.crm.model.VIPUser;
import com.douguo.crm.model.VIPUserHuodong;
import com.douguo.dc.util.Util;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Repository("vipUserDao")
public class VIPUserDao {

    private static final Log loger = LogFactory.getLog(VIPUserDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final String querySQL = "select * from crm_vip_user where id=?";
    private final String querySQLUID = "select * from crm_vip_user where user_id=?";
    private final String queryVipUserListSQL = "SELECT * FROM crm_vip_user";
    private final String insertSQL = "INSERT INTO crm_vip_user (user_id, username,headicon,nickname,real_name,address,mobile,country,province,city,weibo_url,tag_id,tag_name,qq_group,principal,createtime,sex,qq,webchat,has_child,domain,user_desc) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
    private final String updateTagSQL = "UPDATE crm_vip_user SET tag_id = ? WHERE id = ? ;";
    private final String updateSQL = "update crm_vip_user set user_id=?,username=?,headicon=?,nickname=?,real_name=?,address=?,mobile=?,country=?,province=?,city=?,weibo_url=?,tag_id=?,tag_name=?,qq_group=?,principal=?,createtime=?,sex=?,qq=?,webchat=?,has_child=?,domain=?,user_desc=? where id=?";
    private final String deleteSQL = "DELETE FROM crm_vip_user WHERE id = ?" ;
    
    /**
     * 获取用户列表
     *
     * @return
     */
    public List<Map<String, Object>> queryVipUserList() {
        Util.showSQL(queryVipUserListSQL, new Object[]{});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(queryVipUserListSQL, new Object[]{});
        return rowlst;
    }

    /**
     * 获取用户列表
     *
     * @param userId
     * @param userName
     * @param nickName
     * @param province
     * @param city
     * @param tagId
     * @param principal
     * @return
     */
    public Page queryVipUser(String userId, String userName, String nickName, String province, String city, String tagId, String principal, String hasChild, String domain, int pageNo, int pageSize, String sort) {
        String querySQL = "SELECT * FROM crm_vip_user where 1=1 ";
        String countSQL = "select count(*) FROM crm_vip_user where 1=1 ";
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
        //省
        if (StringUtils.isNotBlank(province)) {
            filterSQL.append(" and province='" + province + "'");
        }
        //市
        if (StringUtils.isNotBlank(city)) {
            filterSQL.append(" and city='" + city + "'");
        }
        //标签
        if (StringUtils.isNotBlank(tagId)) {
            filterSQL.append(" and tag_id=" + tagId);
        }
        //负责人
        if (StringUtils.isNotBlank(principal)) {
            filterSQL.append(" and principal='" + principal + "'");
        }
        //生育状况
        if (StringUtils.isNotBlank(hasChild)) {
            filterSQL.append(" and has_child='" + hasChild + "'");
        }
        //擅长领域
        if (StringUtils.isNotBlank(domain) && !"0".equals(domain)) {
            filterSQL.append(" and domain & '" + domain + "' > 0 ");
        }

        if (StringUtils.isNotBlank(sort)) {
            sort = " order by " + sort;
        }

        //
        Page page = null;
        try {
            int count = jdbcTemplate.queryForInt(countSQL + filterSQL.toString(), new Object[]{});

            if (count == 0) {
                page = new Page();
            } else {
                int start = (pageNo - 1) * pageSize;
                String strLimit = " limit " + start + "," + pageSize;
                String strSQL = querySQL + filterSQL.toString() + sort + strLimit;
                Util.showSQL(strSQL, new Object[]{});
                List<VIPUser> rowlst = jdbcTemplate.query(strSQL, new Object[]{}, new VIPUserRowMapper());

                page = new Page(start, count, pageSize, rowlst);
            }
        } catch (Exception e) {
            loger.error(e.getMessage());
        }
        return page;
    }

    public List<VIPUser> getVipUser(String id) {
        Object[] params = new Object[]{id};
        List<VIPUser> list = jdbcTemplate.query(querySQL, params, new VIPUserRowMapper());
        return list;
    }

    public List<VIPUser> getVipUserByUID(String userId) {
        Object[] params = new Object[]{userId};
        List<VIPUser> list = jdbcTemplate.query(querySQLUID, params, new VIPUserRowMapper());
        return list;
    }

    public int insertVipUser(VIPUser vipUser) {
        Object[] parms = new Object[]{vipUser.getUserId(), vipUser.getUserName(), vipUser.getHeadIcon(), vipUser.getNickName(),
                vipUser.getRealName(), vipUser.getAddress(), vipUser.getMobile(), vipUser.getCountry(), vipUser.getProvince(),
                vipUser.getCity(), vipUser.getWeiboUrl(), vipUser.getTagId(), vipUser.getTagName(), vipUser.getQqGroup(),
                vipUser.getPrincipal(), vipUser.getCreatetime(), vipUser.getSex(), vipUser.getQq(), vipUser.getWebchat(),
                vipUser.getHasChild(), vipUser.getDomain(), vipUser.getUserDesc()};
        Util.showSQL(insertSQL, parms);
        return jdbcTemplate.update(insertSQL, parms);
    }
    
    public int deleteVipUser(String id){
    	Object[] params = new Object[]{id};
    	return jdbcTemplate.update(deleteSQL, params) ;
    }

    /**
     * 更新用户Tag
     *
     * @param id
     * @param tag_id
     * @return
     */

    public int updateVipUserTag(int id, int tag_id) {
        Util.showSQL(updateTagSQL, new Object[]{tag_id, id});
        return jdbcTemplate.update(updateTagSQL, tag_id, id);
    }

    /**
     * 更新
     *
     * @return
     */
    public boolean update(VIPUser vipUser) {
        int n = jdbcTemplate.update(updateSQL, vipUser.getUserId(), vipUser.getUserName(), vipUser.getHeadIcon(), vipUser.getNickName(), vipUser.getRealName(),
                vipUser.getAddress(), vipUser.getMobile(), vipUser.getCountry(), vipUser.getProvince(), vipUser.getCity(),
                vipUser.getWeiboUrl(), vipUser.getTagId(), vipUser.getTagName(), vipUser.getQqGroup(), vipUser.getPrincipal(),
                vipUser.getCreatetime(), vipUser.getSex(), vipUser.getQq(), vipUser.getWebchat(), vipUser.getHasChild(), vipUser.getDomain(), vipUser.getUserDesc(), vipUser.getId());
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

    public class VIPUserRowMapper implements RowMapper<VIPUser> {
        @Override
        public VIPUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            VIPUser vipUser = new VIPUser();
            vipUser.setId(rs.getInt("id"));
            vipUser.setUserId(rs.getInt("user_id"));
            vipUser.setUserName(rs.getString("username"));
            vipUser.setHeadIcon(rs.getString("headicon"));
            vipUser.setNickName(rs.getString("nickname"));
            //
            vipUser.setRealName(rs.getString("real_name"));
            vipUser.setAddress(rs.getString("address"));
            vipUser.setMobile(rs.getString("mobile"));
            vipUser.setCountry(rs.getString("country"));
            vipUser.setProvince(rs.getString("province"));
            //
            vipUser.setCity(rs.getString("city"));
            vipUser.setWeiboUrl(rs.getString("weibo_url"));
            vipUser.setTagId(rs.getInt("tag_id"));
            vipUser.setTagName(rs.getString("tag_name"));
            vipUser.setQqGroup(rs.getString("qq_group"));
            //
            vipUser.setPrincipal(rs.getString("principal"));
            vipUser.setCreatetime(rs.getString("createtime"));
            //
            vipUser.setSex(rs.getString("sex"));
            vipUser.setQq(rs.getString("qq"));
            vipUser.setWebchat(rs.getString("webchat"));
            vipUser.setHasChild(rs.getString("has_child"));
            vipUser.setDomain(rs.getString("domain"));
            vipUser.setUserDesc(rs.getString("user_desc"));

            return vipUser;
        }
    }


}