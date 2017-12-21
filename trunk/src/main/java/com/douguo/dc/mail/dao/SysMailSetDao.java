package com.douguo.dc.mail.dao;

import com.douguo.dc.mail.common.MailConstants;
import com.douguo.dc.mail.model.SysMailSet;
import com.douguo.dc.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("sysMailSetDao")
public class SysMailSetDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String querySQL = "select * from dd_sys_mail_set where id=?";
	private final String queryAllSQL = "SELECT * FROM dd_sys_mail_set order by send_time";
	private final String queryTimeMailSQL = "SELECT * FROM dd_sys_mail_set where send_type <> '" + MailConstants.MAIL_SEND_TYPE_ABANDONED + "'";
	private final String insertSQL = "insert into dd_sys_mail_set(mail_type,subject,mail_to,send_type,send_date,send_time,mail_desc,mail_config) values (?,?,?,?,?,?,?,?)";
	private final String updateSQL = "update dd_sys_mail_set set mail_type=?,subject=?,mail_to=?,send_type=?,send_date=?,send_time=?,`mail_desc`=?,mail_config=? where id=?";
	private final String updateMailToSQL = "update dd_sys_mail_set set mail_to=? where id=?";

	public List<SysMailSet> queryListByMailType(String mailType) {
		String sql = "SELECT * FROM `dd_sys_mail_set` WHERE mail_type= ? ORDER BY mail_type";
		Util.showSQL(sql, new Object[] { mailType });
		List<SysMailSet> rowlst = jdbcTemplate.query(sql, new Object[] { mailType },new SysMailSetRowMapper());
		return rowlst;
	}
	
	public List<SysMailSet> getSysMailSet(String id) {
		Object[] params = new Object[] { id };
		List<SysMailSet> list = jdbcTemplate.query(querySQL, params, new SysMailSetRowMapper());
		return list;
	}

	/**
	 * 查询List
	 * 
	 * @return
	 */
	public List<SysMailSet> queryAll() {
		List<SysMailSet> list = jdbcTemplate.query(queryAllSQL, new SysMailSetRowMapper());
		return list;
	}
	
	/**
	 * 查询定时List
	 * 
	 * @return
	 */
	public List<SysMailSet> queryTimeMail() {
		List<SysMailSet> list = jdbcTemplate.query(queryTimeMailSQL, new SysMailSetRowMapper());
		return list;
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public boolean insert(SysMailSet sysMailSet) {
		int n = jdbcTemplate.update(insertSQL, sysMailSet.getMailType(), sysMailSet.getSubject(),
				sysMailSet.getMailTo(), sysMailSet.getSendType(), sysMailSet.getSendDate(), sysMailSet.getSendTime(),
				sysMailSet.getDesc(),sysMailSet.getConfig());
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
	public boolean update(SysMailSet sysMailSet) {
		int n = jdbcTemplate.update(updateSQL, sysMailSet.getMailType(), sysMailSet.getSubject(),
				sysMailSet.getMailTo(), sysMailSet.getSendType(), sysMailSet.getSendDate(), sysMailSet.getSendTime(),
				sysMailSet.getDesc(),sysMailSet.getConfig(), sysMailSet.getId());
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 更新收件人
	 * 
	 * @return
	 */
	public boolean updateMailReceiver(String recriverStr, String id) {
		int n = jdbcTemplate.update(updateMailToSQL, recriverStr,id);
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public class SysMailSetRowMapper implements RowMapper<SysMailSet> {

		@Override
		public SysMailSet mapRow(ResultSet rs, int rowNum) throws SQLException {
			SysMailSet obj = new SysMailSet();
			obj.setId(rs.getInt("id"));
			obj.setMailType(rs.getString("mail_type"));
			obj.setSubject(rs.getString("subject"));
			obj.setMailTo(rs.getString("mail_to"));
			obj.setSendType(rs.getString("send_type"));
			obj.setSendDate(rs.getString("send_date"));
			obj.setSendTime(rs.getString("send_time"));
			obj.setDesc(rs.getString("mail_desc"));
			obj.setConfig(rs.getString("mail_config"));
			return obj;
		}
	}
}
