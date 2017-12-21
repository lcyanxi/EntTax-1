package com.douguo.dc.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.user.dao.FunctionDao;
import com.douguo.dc.user.model.Function;

@Repository("functionService")
public class FunctionService {

	@Autowired
	private FunctionDao functionDao;

	public List<Function> queryAll() {
		return functionDao.queryAll();
	}

	public Function getFunction(String functionId) {
		List<Function> list = functionDao.getFunctionByFunId(functionId);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public Function getFunctionByUri(String uri) {
		List<Function> list = functionDao.getFunctionByUri(uri);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 新增
	 * 
	 * @param fun
	 * @return
	 */
	public boolean insert(Function fun) {
		return functionDao.insert(fun);
	}

	/**
	 * 更新
	 * 
	 * @param fun
	 * @return
	 */
	public boolean update(Function fun) {
		return functionDao.update(fun);
	}
}
