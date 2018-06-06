package com.happynetwork.common.vo;

import java.io.Serializable;
import java.util.List;

public class LocalImageBean implements Serializable{
	private List<ImageBean> localImageList;
	private String groupName;
	public List<ImageBean> getLocalImageList() {
		return localImageList;
	}
	public void setLocalImageList(
			List<ImageBean> localImageList) {
		this.localImageList = localImageList;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@Override
	public String toString() {
		return "LocalImageBean [localImageList=" + localImageList
				+ ", groupName=" + groupName + "]";
	}
	
}
