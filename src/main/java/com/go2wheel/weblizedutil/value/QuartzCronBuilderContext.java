package com.go2wheel.weblizedutil.value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuartzCronBuilderContext {
	
	private String allTemplate;
	
	private String specifiedTemplate;
	
	private List<QuartzCronField> cronFields;
	
	private List<PreDefinedCronPattern> patterns;
	
	private int maxValueNumber;
	
	private String nextTimeLabel;
	
	private String nextTimeUrl;
	
	public Map<String, Object> asMap() {
		Map<String, Object> mp = new HashMap<>();
		mp.put("allTemplate", getAllTemplate());
		mp.put("specifiedTemplate", getSpecifiedTemplate());
		mp.put("cronFields", getCronFields());
		mp.put("patterns", getPatterns());
		return mp;
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

	public List<QuartzCronField> getCronFields() {
		return cronFields;
	}

	public void setCronFields(List<QuartzCronField> cronFields) {
		this.cronFields = cronFields;
	}

	public List<PreDefinedCronPattern> getPatterns() {
		return patterns;
	}

	public void setPatterns(List<PreDefinedCronPattern> patterns) {
		this.patterns = patterns;
	}

	public int getMaxValueNumber() {
		return maxValueNumber;
	}

	public void setMaxValueNumber(int maxValueNumber) {
		this.maxValueNumber = maxValueNumber;
	}

	public String getNextTimeLabel() {
		return nextTimeLabel;
	}

	public void setNextTimeLabel(String nextTimeLabel) {
		this.nextTimeLabel = nextTimeLabel;
	}

	public String getNextTimeUrl() {
		return nextTimeUrl;
	}

	public void setNextTimeUrl(String nextTimeUrl) {
		this.nextTimeUrl = nextTimeUrl;
	}

}
