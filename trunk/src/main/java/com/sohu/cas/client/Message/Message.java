package com.sohu.cas.client.Message;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: harry
 * Date: 2010-4-7
 * Time: 16:30:31
 * To change this template use File | Settings | File Templates.
 */
public class Message {
    public static final int SUCCESS = 0;
    public static final int NOMATCH = 1;
    public static final int INVALID_ACCESSKEY = 2;
    public static final int ERROR = 1000;

    private int type;
    private Date time;
    private String content;

    public Message(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public Message(int type, Date time, String content) {
        this.type = type;
        this.time = time;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
