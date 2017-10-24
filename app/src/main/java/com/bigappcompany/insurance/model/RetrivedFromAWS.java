package com.bigappcompany.insurance.model;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 13 Oct 2017 at 1:07 PM
 */

public class RetrivedFromAWS {
	private String foldername,path;
	public RetrivedFromAWS(String foldername, String path){
		
		this.foldername = foldername;
		this.path = path;
	}
	
	public String getFoldername() {
		return foldername;
	}
	
	public void setFoldername(String foldername) {
		this.foldername = foldername;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
}
