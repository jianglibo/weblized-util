package com.go2wheel.weblizedutil.value;

public class PreDefinedCronPattern {
	
	private String name;
	private String iname;
	
	private String cronValue;
	
	public PreDefinedCronPattern(String name) {
		super();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public String getCronValue() {
		return cronValue;
	}
	public void setCronValue(String cronValue) {
		this.cronValue = cronValue;
	}
}
