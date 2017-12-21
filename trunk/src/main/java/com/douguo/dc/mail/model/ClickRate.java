package com.douguo.dc.mail.model;

/**
 * Created by lichang on 2017/10/25.
 */
public class ClickRate {
    private String statdate;
    private String qtype;
    private int click;
    private int exposure;
    private Double click_rate;

    public String getStatdate() {
        return statdate;
    }

    public void setStatdate(String statdate) {
        this.statdate = statdate;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getExposure() {
        return exposure;
    }

    public void setExposure(int exposure) {
        this.exposure = exposure;
    }

    public Double getClick_rate() {
        return click_rate;
    }

    public void setClick_rate(Double click_rate) {
        this.click_rate = click_rate;
    }
}
