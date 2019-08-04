package com.bage.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bage on 2016/3/13.
 */
public class Event implements Serializable {

	public static final int DATATYPE_PURETEXT = 0;
	public static final int DATATYPE_PICTURE = 1;
	public static final int DATATYPE_AUDIO = 2;

	public static final String dataType_desc_bird = "鸟";
	public static final String dataType_desc_cicada = "蝉";

	public static final int dataType_bird_text = 0; // 鸟，文本
	public static final int dataType_bird_pic = 1; // 鸟，图片
	public static final int dataType_bird_voice = 2; // 鸟，语音

	public static final int dataType_cicada_text = 100;
	public static final int dataType_cicada_pic = 101;
	public static final int dataType_cicada_voice = 102;

	private int eve_id; // 事件id
	private int use_id; // 用户id
	private int eve_count;// 事件中鸟的数量
	private int eve_positivecount;// 事件的点赞数
	private String eve_question; // 事件问题
	private String eve_time; // 事件发布时间
	private float eve_longitude; // 事件的位置经度
	private float eve_latitude; // 事件的位置纬度
	private int eve_datatype; // 数据类型
	private String eve_puretext; // 纯文本
	private String eve_audiourl; // 音频文件的url
	private String eve_pictureurl; // 图文件的URL
	private String eve_description; // 事件的问题
	private String eve_remark; // 事件的备注

	public Event() {

	}

	public Event(int use_id, int eve_count, int eve_positivecount, String eve_question, String eve_time,
			float eve_longitude, float eve_latitude, int eve_datatype, String eve_puretext, String eve_audiourl,
			String eve_pictureurl, String eve_description, String eve_remark) {
		super();
		this.use_id = use_id;
		this.eve_count = eve_count;
		this.eve_positivecount = eve_positivecount;
		this.eve_question = eve_question;
		this.eve_time = eve_time;
		this.eve_longitude = eve_longitude;
		this.eve_latitude = eve_latitude;
		this.eve_datatype = eve_datatype;
		this.eve_puretext = eve_puretext;
		this.eve_audiourl = eve_audiourl;
		this.eve_pictureurl = eve_pictureurl;
		this.eve_description = eve_description;
		this.eve_remark = eve_remark;
	}

	public Event(int eve_id, int use_id, int eve_count, int eve_positivecount, String eve_question, String eve_time,
			float eve_longitude, float eve_latitude, int eve_datatype, String eve_puretext, String eve_audiourl,
			String eve_pictureurl, String eve_description, String eve_remark) {
		super();
		this.eve_id = eve_id;
		this.use_id = use_id;
		this.eve_count = eve_count;
		this.eve_positivecount = eve_positivecount;
		this.eve_question = eve_question;
		this.eve_time = eve_time;
		this.eve_longitude = eve_longitude;
		this.eve_latitude = eve_latitude;
		this.eve_datatype = eve_datatype;
		this.eve_puretext = eve_puretext;
		this.eve_audiourl = eve_audiourl;
		this.eve_pictureurl = eve_pictureurl;
		this.eve_description = eve_description;
		this.eve_remark = eve_remark;
	}

	public Event(int eve_id, int use_id, String eve_question, String eve_time,
				 float eve_longitude, float eve_latitude, int eve_datatype, String eve_puretext, String eve_audiourl,
				 String eve_pictureurl, String eve_description, String eve_remark) {
		super();
		this.eve_id = eve_id;
		this.use_id = use_id;
		this.eve_count = eve_count;
		this.eve_positivecount = eve_positivecount;
		this.eve_question = eve_question;
		this.eve_time = eve_time;
		this.eve_longitude = eve_longitude;
		this.eve_latitude = eve_latitude;
		this.eve_datatype = eve_datatype;
		this.eve_puretext = eve_puretext;
		this.eve_audiourl = eve_audiourl;
		this.eve_pictureurl = eve_pictureurl;
		this.eve_description = eve_description;
		this.eve_remark = eve_remark;
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

	public int getEve_count() {
		return eve_count;
	}

	public void setEve_count(int eve_count) {
		this.eve_count = eve_count;
	}

	public int getEve_positivecount() {
		return eve_positivecount;
	}

	public void setEve_positivecount(int eve_positivecount) {
		this.eve_positivecount = eve_positivecount;
	}

	public String getEve_question() {
		return eve_question;
	}

	public void setEve_question(String eve_question) {
		this.eve_question = eve_question;
	}

	public String getEve_time() {
		return eve_time;
	}

	public void setEve_time(String eve_time) {
		this.eve_time = eve_time;
	}

	public float getEve_longitude() {
		return eve_longitude;
	}

	public void setEve_longitude(float eve_longitude) {
		this.eve_longitude = eve_longitude;
	}

	public float getEve_latitude() {
		return eve_latitude;
	}

	public void setEve_latitude(float eve_latitude) {
		this.eve_latitude = eve_latitude;
	}

	public int getEve_datatype() {
		return eve_datatype;
	}

	public void setEve_datatype(int eve_datatype) {
		this.eve_datatype = eve_datatype;
	}

	public String getEve_puretext() {
		return eve_puretext;
	}

	public void setEve_puretext(String eve_puretext) {
		this.eve_puretext = eve_puretext;
	}

	public String getEve_audiourl() {
		return eve_audiourl;
	}

	public void setEve_audiourl(String eve_audiourl) {
		this.eve_audiourl = eve_audiourl;
	}

	public String getEve_pictureurl() {
		return eve_pictureurl;
	}

	public void setEve_pictureurl(String eve_pictureurl) {
		this.eve_pictureurl = eve_pictureurl;
	}

	public String getEve_description() {
		return eve_description;
	}

	public void setEve_description(String eve_description) {
		this.eve_description = eve_description;
	}

	public String getEve_remark() {
		return eve_remark;
	}

	public void setEve_remark(String eve_remark) {
		this.eve_remark = eve_remark;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + eve_id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (eve_id != other.eve_id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Event [eve_id=" + eve_id + ", use_id=" + use_id + ", eve_count=" + eve_count + ", eve_positivecount="
				+ eve_positivecount + ", eve_question=" + eve_question + ", eve_time=" + eve_time + ", eve_longitude="
				+ eve_longitude + ", eve_latitude=" + eve_latitude + ", eve_datatype=" + eve_datatype
				+ ", eve_puretext=" + eve_puretext + ", eve_audiourl=" + eve_audiourl + ", eve_pictureurl="
				+ eve_pictureurl + ", eve_description=" + eve_description + ", eve_remark=" + eve_remark + "]";
	}

}
