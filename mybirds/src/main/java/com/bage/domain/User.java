package com.bage.domain;

import java.io.Serializable;

/**
 * Created by bage on 2016/3/13.
 */
public class User implements Serializable {
    private int use_id; // 用户id
    private String use_phone;   // 用户手机号
    private String use_name;    // 用户名
    private String use_password;    // 用户密码
    private String use_sex; // 用户性别
    private String use_birthday;    // 用户生日
    private String use_imageurl;    // 用户头像URL
    private String use_mail;    // 用户邮箱
    private int use_positivecount;  // 用户点赞数
    private String use_introduction;    // 用户简介
    private String use_remark;  // 用户备注信息

    public User() {

    }

    public User(String use_phone, String use_name, String use_password, String use_sex, String use_birthday, String use_imageurl, String use_mail, int use_positivecount, String use_introduction, String use_remark) {
        this.use_phone = use_phone;
        this.use_name = use_name;
        this.use_password = use_password;
        this.use_sex = use_sex;
        this.use_birthday = use_birthday;
        this.use_imageurl = use_imageurl;
        this.use_mail = use_mail;
        this.use_positivecount = use_positivecount;
        this.use_introduction = use_introduction;
        this.use_remark = use_remark;
    }

    public User(int use_id, String use_phone, String use_name, String use_password, String use_sex, String use_birthday, String use_imageurl, String use_mail, int use_positivecount, String use_introduction, String use_remark) {
        this.use_id = use_id;
        this.use_phone = use_phone;
        this.use_name = use_name;
        this.use_password = use_password;
        this.use_sex = use_sex;
        this.use_birthday = use_birthday;
        this.use_imageurl = use_imageurl;
        this.use_mail = use_mail;
        this.use_positivecount = use_positivecount;
        this.use_introduction = use_introduction;
        this.use_remark = use_remark;
    }

    public int getUse_id() {
        return use_id;
    }

    public void setUse_id(int use_id) {
        this.use_id = use_id;
    }

    public String getUse_phone() {
        return use_phone;
    }

    public void setUse_phone(String use_phone) {
        this.use_phone = use_phone;
    }

    public String getUse_name() {
        return use_name;
    }

    public void setUse_name(String use_name) {
        this.use_name = use_name;
    }

    public String getUse_password() {
        return use_password;
    }

    public void setUse_password(String use_password) {
        this.use_password = use_password;
    }

    public String getUse_sex() {
        return use_sex;
    }

    public void setUse_sex(String use_sex) {
        this.use_sex = use_sex;
    }

    public String getUse_birthday() {
        return use_birthday;
    }

    public void setUse_birthday(String use_birthday) {
        this.use_birthday = use_birthday;
    }

    public String getUse_imageurl() {
        return use_imageurl;
    }

    public void setUse_imageurl(String use_imageurl) {
        this.use_imageurl = use_imageurl;
    }

    public String getUse_mail() {
        return use_mail;
    }

    public void setUse_mail(String use_mail) {
        this.use_mail = use_mail;
    }

    public int getUse_positivecount() {
        return use_positivecount;
    }

    public void setUse_positivecount(int use_positivecount) {
        this.use_positivecount = use_positivecount;
    }

    public String getUse_introduction() {
        return use_introduction;
    }

    public void setUse_introduction(String use_introduction) {
        this.use_introduction = use_introduction;
    }

    public String getUse_remark() {
        return use_remark;
    }

    public void setUse_remark(String use_remark) {
        this.use_remark = use_remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return use_id == user.use_id;

    }

    @Override
    public int hashCode() {
        return use_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "use_id=" + use_id +
                ", use_phone='" + use_phone + '\'' +
                ", use_name='" + use_name + '\'' +
                ", use_password='" + use_password + '\'' +
                ", use_sex='" + use_sex + '\'' +
                ", use_birthday='" + use_birthday + '\'' +
                ", use_imageurl='" + use_imageurl + '\'' +
                ", use_mail='" + use_mail + '\'' +
                ", use_positivecount=" + use_positivecount +
                ", use_introduction='" + use_introduction + '\'' +
                ", use_remark='" + use_remark + '\'' +
                '}';
    }
}
