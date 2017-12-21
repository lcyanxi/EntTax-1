package com.douguo.dc.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douguo.dc.user.dao.MenuDao;
import com.douguo.dc.user.model.Menu;

@Repository("menuService")
public class MenuService {

	@Autowired
	private MenuDao menuDao;

	public boolean saveMenu(Menu menu) {
		return menuDao.saveMenu(menu);
	}
	
	public List<Map<String, Object>> queryMenu() {
		return menuDao.queryMenu();
	}
}
