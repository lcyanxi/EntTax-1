package com.douguo.dc.serverlog.model;

public class ServerLogSrctypeDict {
    private int id;
    private String srctype;
    private String srctypeName;
    private String service;
    private String createTime;
    private String updateTime;
    private String sdesc;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrctype() {
        return srctype;
    }

    public void setSrctype(String srctype) {
        this.srctype = srctype;
    }

    public String getSrctypeName() {
        return srctypeName;
    }

    public void setSrctypeName(String srctypeName) {
        this.srctypeName = srctypeName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSdesc() {
        return sdesc;
    }

    public void setSdesc(String sdesc) {
        this.sdesc = sdesc;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ServerLogSrctypeDict{" +
                "id=" + id +
                ", srctype='" + srctype + '\'' +
                ", srctypeName='" + srctypeName + '\'' +
                ", service='" + service + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", sdesc='" + sdesc + '\'' +
                '}';
    }
}
