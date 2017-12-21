package com.douguo.uprofile.mobile.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.uprofile.mobile.dao.MobileModelDao;
import com.douguo.uprofile.mobile.model.MobileModel;

@Repository("mobileModelService")
public class MobileModelService {

	@Autowired
	private MobileModelDao mobileModelDao;

	public MobileModel getMobileModel(String mobileModelId) {
		List<MobileModel> list = mobileModelDao.getMobileModel(mobileModelId);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 查询所有数据
	 * @return
	 */
	public List<MobileModel> queryAll() {
		return mobileModelDao.queryAll();
	}

	/**
	 * 新增
	 * @return
	 */
	public boolean insert(MobileModel mobileModel) {
		return mobileModelDao.insert(mobileModel);
	}

	/**
	 * 更新
	 * @return
	 */
	public boolean update(MobileModel mobileModel) {
		return mobileModelDao.update(mobileModel);
	}
}