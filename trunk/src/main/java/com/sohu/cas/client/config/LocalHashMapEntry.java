package com.sohu.cas.client.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * User: shunlongli
 * Date: 2010-3-19
 * Time: 12:16:26
 */
public class LocalHashMapEntry {

    @XmlAttribute
    private String key;

    @XmlAttribute
    private String value;

    @XmlTransient
    public String getKey(){
        return this.key;
    }

    @XmlTransient
    public String getValue(){
        return this.value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
