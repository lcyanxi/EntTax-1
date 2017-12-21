package com.douguo.dc.taskmonitor.model;

/**
 * Created by lichang on 2017/11/2.
 */
public class Operator {
    private int id;
    private String name;
    private String userid;
    private String group;
    private String workdesc;
    private int cooks;
    private int dishs;
    private int posts;
    private int questions;
    private int caidans;
    private int cook_comments;
    private int dish_comments;
    private int dish_likes;
    private int post_replys;
    private int questions_replys;
    private int mall;
    private int live;


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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getWorkdesc() {
        return workdesc;
    }

    public void setWorkdesc(String workdesc) {
        this.workdesc = workdesc;
    }

    public int getCooks() {
        return cooks;
    }

    public void setCooks(int cooks) {
        this.cooks = cooks;
    }

    public int getDishs() {
        return dishs;
    }

    public void setDishs(int dishs) {
        this.dishs = dishs;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public int getCaidans() {
        return caidans;
    }

    public void setCaidans(int caidans) {
        this.caidans = caidans;
    }

    public int getCook_comments() {
        return cook_comments;
    }

    public void setCook_comments(int cook_comments) {
        this.cook_comments = cook_comments;
    }

    public int getDish_comments() {
        return dish_comments;
    }

    public void setDish_comments(int dish_comments) {
        this.dish_comments = dish_comments;
    }

    public int getDish_likes() {
        return dish_likes;
    }

    public void setDish_likes(int dish_likes) {
        this.dish_likes = dish_likes;
    }

    public int getPost_replys() {
        return post_replys;
    }

    public void setPost_replys(int post_replys) {
        this.post_replys = post_replys;
    }

    public int getQuestions_replys() {
        return questions_replys;
    }

    public void setQuestions_replys(int questions_replys) {
        this.questions_replys = questions_replys;
    }

    public int getMall() {
        return mall;
    }

    public void setMall(int mall) {
        this.mall = mall;
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userid='" + userid + '\'' +
                ", group='" + group + '\'' +
                ", workdesc='" + workdesc + '\'' +
                ", cooks=" + cooks +
                ", dishs=" + dishs +
                ", posts=" + posts +
                ", questions=" + questions +
                ", caidans=" + caidans +
                ", cook_comments=" + cook_comments +
                ", dish_comments=" + dish_comments +
                ", dish_likes=" + dish_likes +
                ", post_replys=" + post_replys +
                ", questions_replys=" + questions_replys +
                ", mall=" + mall +
                ", live=" + live +
                '}';
    }
}
