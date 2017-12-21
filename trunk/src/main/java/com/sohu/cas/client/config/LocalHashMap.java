package com.sohu.cas.client.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * User: shunlongli
 * Date: 2010-3-19
 * Time: 12:11:27
 */
public class LocalHashMap<K,V> {
    @XmlElement(name = "property")
    private List<LocalHashMapEntry> properties = new ArrayList<LocalHashMapEntry>();

    @XmlTransient
    public List<LocalHashMapEntry> getProperties(){
        return this.properties;
    }
}
