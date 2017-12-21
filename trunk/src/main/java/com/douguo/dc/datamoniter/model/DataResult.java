package com.douguo.dc.datamoniter.model;

import java.io.Serializable;

public class DataResult implements Serializable {

	private String index;
	private String clusterStatus;
	private String historyDateCounts;
	private String type;
    private Integer isNormal;
    private String lastUpdateDate;
    private String lastUpdateDocs;
    private String indexCreatedDate;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getClusterStatus() {
        return clusterStatus;
    }

    public void setClusterStatus(String clusterStatus) {
        this.clusterStatus = clusterStatus;
    }

    public String getHistoryDateCounts() {
        return historyDateCounts;
    }

    public void setHistoryDateCounts(String historyDateCounts) {
        this.historyDateCounts = historyDateCounts;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIsNormal() {
        return isNormal;
    }

    public void setIsNormal(Integer isNormal) {
        this.isNormal = isNormal;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateDocs() {
        return lastUpdateDocs;
    }

    public void setLastUpdateDocs(String lastUpdateDocs) {
        this.lastUpdateDocs = lastUpdateDocs;
    }

    public String getIndexCreatedDate() {
        return indexCreatedDate;
    }

    public void setIndexCreatedDate(String indexCreatedDate) {
        this.indexCreatedDate = indexCreatedDate;
    }
}
