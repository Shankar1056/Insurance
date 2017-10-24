package com.bigappcompany.insurance.model;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 27 Sep 2017 at 2:04 PM
 */

public class DetailsPreviousPolicyModel {
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	private String id,name;
	
	public DetailsPreviousPolicyModel(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
}
