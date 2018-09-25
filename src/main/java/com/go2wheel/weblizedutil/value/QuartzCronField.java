package com.go2wheel.weblizedutil.value;

import java.util.List;

public class QuartzCronField {
	
	private String name;
	
	private String iname;
	
	private String uname;
	
	private boolean mandantory;
	
	private String allTemplate;
	
	private String specifiedTemplate;
	
	private String allowedValues;
	
	private String allowedSpecialCharacters;
	
	private List<String> examples;

	public QuartzCronField(String name, boolean mandantory, String allowedValues, String allowedSpecialCharacters) {
		super();
		this.name = name;
		this.mandantory = mandantory;
		this.allowedValues = allowedValues;
		this.allowedSpecialCharacters = allowedSpecialCharacters;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMandantory() {
		return mandantory;
	}

	public void setMandantory(boolean mandantory) {
		this.mandantory = mandantory;
	}

	public String getAllowedValues() {
		return allowedValues;
	}

	public void setAllowedValues(String allowedValues) {
		this.allowedValues = allowedValues;
	}

	public String getAllowedSpecialCharacters() {
		return allowedSpecialCharacters;
	}

	public void setAllowedSpecialCharacters(String allowedSpecialCharacters) {
		this.allowedSpecialCharacters = allowedSpecialCharacters;
	}

	public List<String> getExamples() {
		return examples;
	}

	public void setExamples(List<String> examples) {
		this.examples = examples;
	}

	public String getIname() {
		return iname;
	}

	public void setIname(String iname) {
		this.iname = iname;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getAllTemplate() {
		return allTemplate;
	}

	public void setAllTemplate(String allTemplate) {
		this.allTemplate = allTemplate;
	}

	public String getSpecifiedTemplate() {
		return specifiedTemplate;
	}

	public void setSpecifiedTemplate(String specifiedTemplate) {
		this.specifiedTemplate = specifiedTemplate;
	}

}
