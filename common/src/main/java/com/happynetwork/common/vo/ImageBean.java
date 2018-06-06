package com.happynetwork.common.vo;

import java.io.Serializable;

public class ImageBean implements Serializable{
	private String pic;// 本地图片路径
	private String picFileId;  //图片ID
	private int position;//记录在adapter的位置
	private boolean isChoice;// 是否选中 true选中 false未选中
	private int headId;
	private String uid;
	public String getPath() {
		return pic;
	}
	public void setPath(String pic) {
		this.pic = pic;
	}
	public String getPicFileId() {
		return picFileId;
	}
	public void setPicFileId(String picFileId) {
		this.picFileId = picFileId;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public boolean isChoice() {
		return isChoice;
	}
	public void setChoice(boolean isChoice) {
		this.isChoice = isChoice;
	}
	public int getHeadId() {
		return headId;
	}
	public void setHeadId(int headId) {
		this.headId = headId;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

}
