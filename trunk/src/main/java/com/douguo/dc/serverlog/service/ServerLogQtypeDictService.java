package com.douguo.dc.serverlog.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.serverlog.dao.ServerLogQtypeDictDao;
import com.douguo.dc.serverlog.model.ServerLogQtypeDict;

@Repository("serverLogQtypeDictService")
public class ServerLogQtypeDictService {

	@Autowired
	private ServerLogQtypeDictDao serverLogQtypeDictDao;

	public List<Map<String, Object>> queryListByService(String service) {
		List<Map<String, Object>> keyRows = serverLogQtypeDictDao.queryListByService(service);
		return keyRows;
	}

	public List<Map<String, Object>> queryList() {
		List<Map<String, Object>> keyRows = serverLogQtypeDictDao.queryList();
		return keyRows;
	}

	public ServerLogQtypeDict getServerLogQtypeDict(String serverLogQtypeDictId) {
		List<ServerLogQtypeDict> list = serverLogQtypeDictDao.getServerLogQtypeDict(serverLogQtypeDictId);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<ServerLogQtypeDict> queryAll() {
		return serverLogQtypeDictDao.queryAll();
	}

	/**
	 * 新增
	 * 
	 * @param fun
	 * @return
	 */
	public boolean insert(ServerLogQtypeDict serverLogQtypeDict) {
		return serverLogQtypeDictDao.insert(serverLogQtypeDict);
	}

	/**
	 * 更新
	 * 
	 * @param fun
	 * @return
	 */
	public boolean update(ServerLogQtypeDict serverLogQtypeDict) {
		return serverLogQtypeDictDao.update(serverLogQtypeDict);
	}
}