package com.douguo.uprofile.mobile.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.uprofile.mobile.model.MobileBrand;

@Repository("mobileBrandDao")
public class MobileBrandDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String querySQL = "select * from dd_mobile_brand where id=?";
	private final String queryAllSQL = "SELECT * FROM dd_mobile_brand ";
	private final String insertSQL = "insert into dd_mobile_brand(brand_name,brand_en_name,sort_no,brand_desc,company) values (?,?,?,?,?)";
	private final String updateSQL = "update dd_mobile_brand set brand_name=?,brand_en_name=?,sort_no=?,brand_desc=?,company=? where id=?";

	public List<MobileBrand> getMobileBrand(String mobileBrandId) {
		Object[] params = new Object[] { mobileBrandId };
		List<MobileBrand> list = jdbcTemplate.query(querySQL, params, new MobileBrandRowMapper());
		return list;
	}

	/**
	 * 查询qtype List
	 * 
	 * @return
	 */
	public List<MobileBrand> queryAll() {
		List<MobileBrand> list = jdbcTemplate.query(queryAllSQL, new MobileBrandRowMapper());
		return list;
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public boolean insert(MobileBrand mobileBrand) {
		int n = jdbcTemplate.update(insertSQL, mobileBrand.getBrandName(), mobileBrand.getBrandEnName(),
				mobileBrand.getSortNo(), mobileBrand.getBrandDesc(), mobileBrand.getCompany());
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
	public boolean update(MobileBrand mobileBrand) {
		int n = jdbcTemplate.update(updateSQL, mobileBrand.getBrandName(), mobileBrand.getBrandEnName(),
				mobileBrand.getSortNo(), mobileBrand.getBrandDesc(), mobileBrand.getCompany(), mobileBrand.getId());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public class MobileBrandRowMapper implements RowMapper<MobileBrand> {

		@Override
		public MobileBrand mapRow(ResultSet rs, int rowNum) throws SQLException {
			MobileBrand fun = new MobileBrand();
			fun.setId(rs.getInt("id"));
			fun.setBrandName(rs.getString("brand_name"));
			fun.setBrandEnName(rs.getString("brand_en_name"));
			fun.setSortNo(rs.getInt("sort_no"));
			fun.setBrandDesc(rs.getString("brand_desc"));
			fun.setCompany(rs.getString("company"));
			return fun;
		}
	}
}