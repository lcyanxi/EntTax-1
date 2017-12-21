package com.douguo.uprofile.mobile.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.douguo.uprofile.mobile.model.MobileModel;

@Repository("mobileModelDao")
public class MobileModelDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String querySQL = "select * from dd_mobile_model where id=?";
	private final String queryAllSQL = "SELECT * FROM dd_mobile_model ";
	private final String insertSQL = "insert into dd_mobile_model(devi, brand, model, model_name,model_desc, consume_level ) values (?,?,?,?,?,?)";
	private final String updateSQL = "update dd_mobile_model set devi=?,brand=?,model=?, model_name=?, model_desc=?, consume_level=? where id=?";

	public List<MobileModel> getMobileModel(String mobilModelId) {
		Object[] params = new Object[] { mobilModelId };
		List<MobileModel> list = jdbcTemplate.query(querySQL, params, new MobileModelRowMapper());
		return list;
	}

	/**
	 * 查询qtype List
	 * 
	 * @return
	 */
	public List<MobileModel> queryAll() {
		List<MobileModel> list = jdbcTemplate.query(queryAllSQL, new MobileModelRowMapper());
		return list;
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public boolean insert(MobileModel mobileModel) {
		int n = jdbcTemplate.update(insertSQL, mobileModel.getDevi(), mobileModel.getBrand(),
				mobileModel.getModel(), mobileModel.getModelName(), mobileModel.getModelDesc(), mobileModel.getConsumeLevel());
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
	public boolean update(MobileModel mobileModel) {
		int n = jdbcTemplate.update(updateSQL, mobileModel.getDevi(), mobileModel.getBrand(),
				mobileModel.getModel(), mobileModel.getModelName(),mobileModel.getModelDesc(), mobileModel.getConsumeLevel(), mobileModel.getId());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public class MobileModelRowMapper implements RowMapper<MobileModel> {

		@Override
		public MobileModel mapRow(ResultSet rs, int rowNum) throws SQLException {
			MobileModel fun = new MobileModel();
			fun.setId(rs.getInt("id"));
			fun.setDevi(rs.getString("devi"));
			fun.setBrand(rs.getString("brand"));
			fun.setModel(rs.getString("model"));
			fun.setModelName(rs.getString("model_name"));
			fun.setModelDesc(rs.getString("model_desc"));
			fun.setConsumeLevel(rs.getString("consume_level"));
			return fun;
		}
	}
}