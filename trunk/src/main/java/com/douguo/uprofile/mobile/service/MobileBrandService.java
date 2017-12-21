package com.douguo.uprofile.mobile.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.uprofile.mobile.dao.MobileBrandDao;
import com.douguo.uprofile.mobile.model.MobileBrand;

@Repository("mobileBrandService")
public class MobileBrandService {

	@Autowired
	private MobileBrandDao mobileBrandDao;

	public MobileBrand getMobileBrand(String mobileBrandId) {
		List<MobileBrand> list = mobileBrandDao.getMobileBrand(mobileBrandId);
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
	public List<MobileBrand> queryAll() {
		return mobileBrandDao.queryAll();
	}

	/**
	 * 新增
	 * @return
	 */
	public boolean insert(MobileBrand mobileBrand) {
		return mobileBrandDao.insert(mobileBrand);
	}

	/**
	 * 更新
	 * @return
	 */
	public boolean update(MobileBrand mobileBrand) {
		return mobileBrandDao.update(mobileBrand);
	}
}