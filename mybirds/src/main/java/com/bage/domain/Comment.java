package com.bage.domain;

import java.io.Serializable;

/**
 * 评论对象的定义
 */
public class Comment implements Serializable {

    private int com_id; // 评论id
    private int eve_id; // 事件id
    private int use_id; // 用户id
    private String com_content; // 评论内容
    private int com_answer; // 回答（整型的）
    private String com_time;    // 评论时间
    private int com_positivecount;  // 点赞数
    private int com_negativecount;  // 点菜数

    public Comment() {
    }

    public Comment(int eve_id, int use_id, String com_content, int com_answer, String com_time, int com_positivecount, int com_negativecount) {
        this.eve_id = eve_id;
        this.use_id = use_id;
        this.com_content = com_content;
        this.com_answer = com_answer;
        this.com_time = com_time;
        this.com_positivecount = com_positivecount;
        this.com_negativecount = com_negativecount;
    }

    public Comment(int com_id, int eve_id, int use_id, String com_content, int com_answer, String com_time, int com_positivecount, int com_negativecount) {
        this.com_id = com_id;
        this.eve_id = eve_id;
        this.use_id = use_id;
        this.com_content = com_content;
        this.com_answer = com_answer;
        this.com_time = com_time;
        this.com_positivecount = com_positivecount;
        this.com_negativecount = com_negativecount;
    }

    public int getCom_id() {
        return com_id;
    }

    public void setCom_id(int com_id) {
        this.com_id = com_id;
    }

    public int getEve_id() {
        return eve_id;
    }

    public void setEve_id(int eve_id) {
        this.eve_id = eve_id;
    }

    public int getUse_id() {
        return use_id;
    }

    public void setUse_id(int use_id) {
        this.use_id = use_id;
    }

    public String getCom_content() {
        return com_content;
    }

    public void setCom_content(String com_content) {
        this.com_content = com_content;
    }

    public int getCom_answer() {
        return com_answer;
    }

    public void setCom_answer(int com_answer) {
        this.com_answer = com_answer;
    }

    public String getCom_time() {
        return com_time;
    }

    public void setCom_time(String com_time) {
        this.com_time = com_time;
    }

    public int getCom_positivecount() {
        return com_positivecount;
    }

    public void setCom_positivecount(int com_positivecount) {
        this.com_positivecount = com_positivecount;
    }

    public int getCom_negativecount() {
        return com_negativecount;
    }

    public void setCom_negativecount(int com_negativecount) {
        this.com_negativecount = com_negativecount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        return com_id == comment.com_id;

    }

    @Override
    public int hashCode() {
        return com_id;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "com_id=" + com_id +
                ", eve_id=" + eve_id +
                ", use_id=" + use_id +
                ", com_content='" + com_content + '\'' +
                ", com_answer=" + com_answer +
                ", com_time='" + com_time + '\'' +
                ", com_positivecount=" + com_positivecount +
                ", com_negativecount=" + com_negativecount +
                '}';
    }
}
