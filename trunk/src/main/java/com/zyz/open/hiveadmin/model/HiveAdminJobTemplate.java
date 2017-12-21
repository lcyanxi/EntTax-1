package com.zyz.open.hiveadmin.model;

import java.io.Serializable;

public class HiveAdminJobTemplate implements Serializable {

    /**
     *
     */
    private int id;
    private String uid;
    private String showListTitle;
    private String showVarTitle;
    private String templateName;
    private String templateUid;
    private String templateContent;
    private String templateGroup;
    private int sort;
    private String templateDesc;
    private String updateTime;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getShowListTitle() {
        return showListTitle;
    }

    public void setShowListTitle(String showListTitle) {
        this.showListTitle = showListTitle;
    }

    public String getShowVarTitle() {
        return showVarTitle;
    }

    public void setShowVarTitle(String showVarTitle) {
        this.showVarTitle = showVarTitle;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateUid() {
        return templateUid;
    }

    public void setTemplateUid(String templateUid) {
        this.templateUid = templateUid;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getTemplateGroup() {
        return templateGroup;
    }

    public void setTemplateGroup(String templateGroup) {
        this.templateGroup = templateGroup;
    }

    public String getTemplateDesc() {
        return templateDesc;
    }

    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}