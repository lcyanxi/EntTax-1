package com.douguo.dg.group.service;

import com.douguo.dg.group.dao.DgGroupDao;
import com.douguo.dg.user.dao.DgUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("dgGroupService")
public class DgGroupService {

	@Autowired
	private DgGroupDao dgGroupDao;


	public Map<String, Object> getGroupInfo(String gId) {
		List<Map<String, Object>> keyRows = dgGroupDao.getGroupInfo(gId);
		if(keyRows.size() > 0){
			return keyRows.get(0);
		}else{
			return null;
		}
	}

	public List<Map<String,Object>> getGroupList(){
		return dgGroupDao.getGroupList();
	}

    public Map<String, String> getGroupsMap() {
        //
        Map<String, String> mapGroup = new HashMap<String, String>();
        List<Map<String, Object>> list = dgGroupDao.getGroupList();

        for (Map<String, Object> map : list) {
            Integer groupId = (Integer) map.get("id");
            String groupName = (String) map.get("name");
            mapGroup.put(String.valueOf(groupId), groupName);
        }

        return mapGroup;
    }
}