package com.zyz.open.hiveadmin.dao;

import com.douguo.dc.util.Util;
import com.zyz.open.hiveadmin.model.HiveAdminJobTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository("hiveAdminJobTemplateDao")
public class HiveAdminJobTemplateDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String querySQL = "select * from dd_hiveadmin_job_template where id=?";
    private final String queryTemplateUidSQL = "select * from dd_hiveadmin_job_template where template_uid=?";
    private final String queryAllSQL = "SELECT * FROM dd_hiveadmin_job_template order by template_group,sort";
    private final String insertSQL = "insert into dd_hiveadmin_job_template (uid,show_list_title,show_var_title,template_name,template_uid,template_content,template_group,sort,template_desc,update_time,create_time) values (?,?,?,?,?,?,?,?,?,?,?)";
    private final String updateSQL = "update dd_hiveadmin_job_template set uid=?,show_list_title=?,show_var_title=?,template_name=?,template_uid=?,template_content=?,template_group=?,sort=?,template_desc=?,update_time=?,create_time=? where id=?";

    public List<Map<String, Object>> queryList() {
        String sql = "SELECT * FROM `dd_hiveadmin_job_template` ORDER BY template_group,sort";
        Util.showSQL(sql, new Object[]{});
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
        return rowlst;
    }

    public List<HiveAdminJobTemplate> getHiveAdminJobTemplate(String id) {
        Object[] params = new Object[]{id};
        List<HiveAdminJobTemplate> list = jdbcTemplate.query(querySQL, params, new HiveAdminJobTemplateRowMapper());
        return list;
    }

    public List<HiveAdminJobTemplate> getHiveAdminJobTemplateByUid(String templateUid) {
        Object[] params = new Object[]{templateUid};
        List<HiveAdminJobTemplate> list = jdbcTemplate.query(queryTemplateUidSQL, params, new HiveAdminJobTemplateRowMapper());
        return list;
    }

    /**
     * 查询qtype List
     *
     * @return
     */
    public List<HiveAdminJobTemplate> queryAll() {
        List<HiveAdminJobTemplate> list = jdbcTemplate.query(queryAllSQL, new HiveAdminJobTemplateRowMapper());
        return list;
    }

    /**
     * 新增
     *
     * @return
     */
    public boolean insert(HiveAdminJobTemplate template) {
        int n = jdbcTemplate.update(insertSQL, template.getUid(), template.getShowListTitle(),
                template.getShowVarTitle(), template.getTemplateName(), template.getTemplateUid(),
                template.getTemplateContent(), template.getTemplateGroup(), template.getSort(),
                template.getTemplateDesc(), template.getUpdateTime(), template.getCreateTime());
        if (n > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更新
     *
     * @return
     */
    public boolean update(HiveAdminJobTemplate template) {
        int n = jdbcTemplate.update(updateSQL, template.getUid(), template.getShowListTitle(),
                template.getShowVarTitle(), template.getTemplateName(), template.getTemplateUid(),
                template.getTemplateContent(), template.getTemplateGroup(), template.getSort(),
                template.getTemplateDesc(), template.getUpdateTime(), template.getCreateTime(), template.getId());
        if (n > 0) {
            return true;
        } else {
            return false;
        }
    }

    public class HiveAdminJobTemplateRowMapper implements RowMapper<HiveAdminJobTemplate> {
        @Override
        public HiveAdminJobTemplate mapRow(ResultSet rs, int rowNum) throws SQLException {
            HiveAdminJobTemplate obj = new HiveAdminJobTemplate();
            obj.setId(rs.getInt("id"));
            obj.setUid(rs.getString("uid"));
            obj.setShowListTitle(rs.getString("show_list_title"));
            obj.setShowVarTitle(rs.getString("show_var_title"));
            obj.setTemplateName(rs.getString("template_name"));
            obj.setTemplateUid(rs.getString("template_uid"));
            obj.setTemplateContent(rs.getString("template_content"));
            obj.setTemplateGroup(rs.getString("template_group"));
            obj.setSort(rs.getInt("sort"));
            obj.setTemplateDesc(rs.getString("template_desc"));
            obj.setUpdateTime(rs.getString("update_time"));
            obj.setCreateTime(rs.getString("create_time"));
            return obj;
        }
    }
}