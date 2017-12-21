package com.sohu.cas.client.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: zichengxiong
 * Date: 2010-3-29
 * Time: 11:53:30
 */

@XmlRootElement(name = "AuthConfig")
public class AuthConfig {
    @XmlElement
    private String proxy;

    @XmlJavaTypeAdapter(HashMapAdapter.class)
    private HashMap<String, String> properties;

    public void setProxy(String proxy){
        this.proxy = proxy;
    }

    public void setProperties(HashMap<String, String> properties){
        this.properties = properties;
    }

    @XmlTransient
    public HashMap<String, String> getProperties()
    {
        return this.properties;
    }

    @XmlTransient
    public String getProxy(){
        return this.proxy;
    }
}
