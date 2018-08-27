package com.go2wheel.weblizedutil.util;

import java.util.concurrent.TimeUnit;

public class TplUtil {
	
	public String formatSize(Long fileSize) {
		return StringUtil.formatSize(fileSize);
	}
	
	public String getFormatedTimeCosts(long timeCost) {
		return StringUtil.getTimeCost(timeCost);
	}
	
	public String getFormatedTimeCosts(long timeCost, TimeUnit unit) {
		return StringUtil.getTimeCost(timeCost, unit);
	}
}
