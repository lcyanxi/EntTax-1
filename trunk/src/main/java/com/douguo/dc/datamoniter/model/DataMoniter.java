package com.douguo.dc.datamoniter.model;

import java.io.Serializable;

public class DataMoniter implements Serializable {

	private String type;
	private String name;
	private int docs;
	private String statdate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDocs() {
        return docs;
    }

    public void setDocs(int docs) {
        this.docs = docs;
    }

    public String getStatdate() {
        return statdate;
    }

    public void setStatdate(String statdate) {
        this.statdate = statdate;
    }
}
