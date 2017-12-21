package com.douguo.dc.cookusertag.dao;

import com.douguo.dc.cookusertag.model.Categories;
import com.douguo.dc.cookusertag.model.CookTag;
import com.douguo.dc.cookusertag.model.CookUserTag;
import com.douguo.dc.cookusertag.model.Ingredients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository("cookUserTagDao")
public class CookUserTagDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<CookUserTag> queryAllUserTagList() {
        String sql = "SELECT * FROM `dd_tag` WHERE flag >=1";
        List<CookUserTag> list = jdbcTemplate.query(sql, new CookUserTagRowMapper());
        return list;
    }

    public List<Categories> queryAllCategoriesList() {
        //String sql = "SELECT * from dg_categories WHERE level>=0 and flag>=0";
        String sql = "SELECT * from dg_categories WHERE level>=0";
        List<Categories> rowlst = jdbcTemplate.query(sql, new CategoriesRowMapper());
        return rowlst;
    }

    public List<Ingredients> queryAllIngredientsList() {
        //String sql = "SELECT * from dg_ingredients WHERE level>=0 and flag>=0";
        String sql = "SELECT * from dg_ingredients WHERE level>=0";
        List<Ingredients> rowlst = jdbcTemplate.query(sql, new IngredientsRowMapper());
        return rowlst;
    }

    public List<CookUserTag> getCookUserTag(String cookUserTagId) {
        String sql = "select * from dd_tag where flag>0 and id=?";
        Object[] params = new Object[] { cookUserTagId };
        List<CookUserTag> list = jdbcTemplate.query(sql, params, new CookUserTagRowMapper());
        return list;
    }

    public boolean insertCookUserTag(CookUserTag cookUserTag) {
        String sql = "insert into dd_tag(tag_id,tag_name,flag) values (?,?,?)";
        int n = jdbcTemplate.update(sql, cookUserTag.getTagId(), cookUserTag.getTagName(), cookUserTag.getFlag());
        if (n > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean insertCookTag(CookTag cookTag) {
        String sql = "insert into dd_cook_tag(user_tag_id,ingre_categ_id,flag) values (?,?,?)";
        int n = jdbcTemplate.update(sql, cookTag.getUser_tag_id(), cookTag.getIngre_categ_id(), cookTag.getFlag());
        if (n > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteCookTag(String id,String ingre_categ_id,String flag) {
        String sql = "DELETE from dd_cook_tag WHERE user_tag_id=? and ingre_categ_id=? and flag=?";
        System.out.println("SQL:" + sql);
        Object[] params = new Object[] { id,ingre_categ_id,flag };
        int n = jdbcTemplate.update(sql, params);
        if (n > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<CookTag> queryAllCookTag(String id,String flag) {
        String sql = "select * from dd_cook_tag where flag>0 and user_tag_id=? and flag=?";
        Object[] params = new Object[] { id,flag };
        List<CookTag> rowlist = jdbcTemplate.query(sql, params ,new CookTagRowMapper());
        return rowlist;
    }

    public List<Map<String,Object>>  queryAllDgIngredients(){
	    String sql="select id,name,parentid,flag,synonum,path from dg_ingredients";
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
        return  rowlst;
    }
    public List<Map<String,Object>>  queryAllDgCategories(){
        String sql="select id,name,parentid,flag, synonym AS synonum ,path from dg_categories";
        List<Map<String, Object>> rowlst = jdbcTemplate.queryForList(sql);
        return  rowlst;
    }

//    public boolean insert(MobileBrand mobileBrand) {
//        int n = jdbcTemplate.update(insertSQL, mobileBrand.getBrandName(), mobileBrand.getBrandEnName(),
//                mobileBrand.getSortNo(), mobileBrand.getBrandDesc(), mobileBrand.getCompany());
//        if (n > 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * 更新
//     *
//     * @return
//     */
//    public boolean update(MobileBrand mobileBrand) {
//        int n = jdbcTemplate.update(updateSQL, mobileBrand.getBrandName(), mobileBrand.getBrandEnName(),
//                mobileBrand.getSortNo(), mobileBrand.getBrandDesc(), mobileBrand.getCompany(), mobileBrand.getId());
//        if (n > 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    public class CookUserTagRowMapper implements RowMapper<CookUserTag> {
        @Override
        public CookUserTag mapRow(ResultSet rs, int rowNum) throws SQLException {
            CookUserTag fun = new CookUserTag();
            fun.setId(rs.getInt("id"));
            fun.setTagId(rs.getInt("tag_id"));
            fun.setTagName(rs.getString("tag_name"));
            fun.setFlag(rs.getInt("flag"));
            return fun;
        }
    }

    public class CategoriesRowMapper implements RowMapper<Categories> {
        @Override
        public Categories mapRow(ResultSet rs, int rowNum) throws SQLException {
            Categories fun = new Categories();
            fun.setId(rs.getInt("id"));
            fun.setName(rs.getString("name"));
            fun.setUname(rs.getString("uname"));
            fun.setIsparent(rs.getInt("isparent"));
            fun.setParentid(rs.getInt("parentid"));
            fun.setLevel(rs.getInt("level"));
            fun.setPath(rs.getString("path"));
            fun.setSortnum(rs.getInt("sortnum"));
            fun.setImage(rs.getString("image"));
            fun.setFlag(rs.getInt("flag"));
            fun.setIsshow(rs.getInt("isshow"));
            fun.setCreatetime(rs.getString("createtime"));
            fun.setUpdatetime(rs.getString("updatetime"));
            return fun;
        }
    }

    public class IngredientsRowMapper implements RowMapper<Ingredients> {
        @Override
        public Ingredients mapRow(ResultSet rs, int rowNum) throws SQLException {
            Ingredients fun = new Ingredients();
            fun.setId(rs.getInt("id"));
            fun.setName(rs.getString("name"));
            fun.setUname(rs.getString("uname"));
            fun.setIsparent(rs.getInt("isparent"));
            fun.setParentid(rs.getInt("parentid"));
            fun.setLevel(rs.getInt("level"));
            fun.setPath(rs.getString("path"));
            fun.setSortnum(rs.getInt("sortnum"));
            fun.setImage(rs.getString("image"));
            fun.setFlag(rs.getInt("flag"));
            fun.setIsshow(rs.getInt("isshow"));
            fun.setSynonum(rs.getString("synonum"));
            fun.setCreatetime(rs.getString("createtime"));
            fun.setUpdatetime(rs.getString("updatetime"));
            return fun;
        }
    }

    public class CookTagRowMapper implements RowMapper<CookTag> {
        @Override
        public CookTag mapRow(ResultSet rs, int rowNum) throws SQLException {
            CookTag fun = new CookTag();
            fun.setId(rs.getInt("id"));
            fun.setUser_tag_id(rs.getString("user_tag_id"));
            fun.setIngre_categ_id(rs.getString("ingre_categ_id"));
            fun.setFlag(rs.getInt("flag"));
            return fun;
        }
    }

}