package com.sohu.cas.client.config;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;

/**
 * User: Administrator
 * Date: 2010-3-19
 * Time: 12:03:19
 */
public class HashMapAdapter extends XmlAdapter<LocalHashMap<String, String>, HashMap<String, String>>{
    @Override
    public HashMap<String, String> unmarshal(LocalHashMap<String, String> v) throws Exception {
		HashMap<String, String> hashMap = new HashMap<String, String>();
        for(LocalHashMapEntry entry: v.getProperties()){
            hashMap.put(entry.getKey(), entry.getValue());
        }

        return hashMap;
    }

    @Override
    public LocalHashMap<String, String> marshal(HashMap<String, String> v) throws Exception {
        LocalHashMap<String, String> localHashMap = new LocalHashMap<String, String>();
        for(String key:v.keySet()){
            LocalHashMapEntry localHashMapEntry = new LocalHashMapEntry();
            localHashMapEntry.setKey(key);
            localHashMapEntry.setValue(v.get(key));
            localHashMap.getProperties().add(localHashMapEntry);
        }

        return localHashMap;
    }
}
