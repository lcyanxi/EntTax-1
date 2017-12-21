package com.sohu.cas.client.core;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Boolean isActive(){
        return isActive;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private int id = -1;
    private String name = null;
    private String fullName = null;
    private String desc = null;
    private List<Group> groups = null;
    private String email = null;
    private String phone = null;
    private String mobile = null;
    private String address = null;
    private String param = null;
    private Boolean isActive = false;


    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.id < 0) return false;
        if (!(obj instanceof User)) return false;
        return (((User) obj).getId() == this.id);
    }

//    public int hashCode() {
//        return id < 0 ? super.hashCode() : com.sohu.cms.util.Util.buildHashCode(id, com.sohu.cms.ItemInfo.USER_TYPE);
//    }
}
