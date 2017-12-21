package com.zyz.open.hiveadmin.service;

import com.zyz.open.hiveadmin.dao.HiveAdminJobDao;
import com.zyz.open.hiveadmin.dao.HiveAdminJobTemplateDao;
import com.zyz.open.hiveadmin.model.HiveAdminJobTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("HiveAdminJobTemplateService")
public class HiveAdminJobTemplateService {

	@Autowired
	private HiveAdminJobTemplateDao hiveAdminJobTemplateDao;

	public List<Map<String, Object>> queryList() {
		List<Map<String, Object>> keyRows = hiveAdminJobTemplateDao.queryList();
		return keyRows;
	}

	public HiveAdminJobTemplate getHiveAdminJobTemplate(String id) {
		List<HiveAdminJobTemplate> list = hiveAdminJobTemplateDao.getHiveAdminJobTemplate(id);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

    public HiveAdminJobTemplate getHiveAdminJobTemplateByUid(String templateUid) {
        List<HiveAdminJobTemplate> list = hiveAdminJobTemplateDao.getHiveAdminJobTemplateByUid(templateUid);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }


	public List<HiveAdminJobTemplate> queryAll() {
		return hiveAdminJobTemplateDao.queryAll();
	}

	/**
	 * 新增
	 * 
	 * @param hiveAdminJobTemplate
	 * @return
	 */
	public boolean insert(HiveAdminJobTemplate hiveAdminJobTemplate) {
		return hiveAdminJobTemplateDao.insert(hiveAdminJobTemplate);
	}

	/**
	 * 更新
	 * 
	 * @param hiveAdminJobTemplate
	 * @return
	 */
	public boolean update(HiveAdminJobTemplate hiveAdminJobTemplate) {
		return hiveAdminJobTemplateDao.update(hiveAdminJobTemplate);
	}
}