package com.go2wheel.weblizedutil.ui;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public interface MainMenuItem extends Comparable<MainMenuItem> {
	String getName();
	String getPath();
	Integer getOrder();
	boolean isActive();
	boolean isGroupFirst();
	void alterState(String currentUrl);
	
	@Override
	default public int compareTo(MainMenuItem o) {
		return this.getOrder().compareTo(o.getOrder());
	}
	void setName(String name);
	String getGroupName();
	MainMenuItem customizeFromOrigin(Collection<? extends GrantedAuthority> gas);
	void setGroupFirst(boolean b);
	void setGroupName(String name);
}
