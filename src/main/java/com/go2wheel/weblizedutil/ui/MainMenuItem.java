package com.go2wheel.weblizedutil.ui;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class MainMenuItem implements Comparable<MainMenuItem>, Cloneable {
	
	private String name;
	
	private Integer order = 0;
	
	private String path;
	/**
	 * groupName is a necessary, Cause there need a way for controller to define which group it belongs to.
	 */
	private String groupName;
	
	private boolean active = false;
	
	private boolean groupFirst = false;
	
	private String[] forRoles; 
	
	public MainMenuItem(String groupName, String name, String path, Integer order, String...forRoles) {
		this.groupName = groupName;
		this.name = name;
		this.path = path;
		this.order = order;
		this.setForRoles(forRoles);
	}
	
	public MainMenuItem() {
	}

	public MainMenuItem customizeFromOrigin(Collection<? extends GrantedAuthority> gas) {
		MainMenuItem mi = new MainMenuItem();
		mi.setName(getName());
		mi.setPath(getPath());
		mi.setOrder(getOrder());
		return mi;
	}

	@Override
	public int compareTo(MainMenuItem o) {
		return this.getOrder().compareTo(o.getOrder());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isGroupFirst() {
		return groupFirst;
	}

	public void setGroupFirst(boolean groupFirst) {
		this.groupFirst = groupFirst;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String[] getForRoles() {
		return forRoles;
	}

	public void setForRoles(String[] forRoles) {
		this.forRoles = forRoles;
	}

}
