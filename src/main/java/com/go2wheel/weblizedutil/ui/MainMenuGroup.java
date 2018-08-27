package com.go2wheel.weblizedutil.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;

public class MainMenuGroup implements Comparable<MainMenuGroup> {
	
	private String name;
	
	private Integer order = 0;
	
	private List<MainMenuItem> items = new ArrayList<>();
	
	private Set<String> gas = new HashSet<>();
	
	public MainMenuGroup() {}
	
	public MainMenuGroup(String name, String...gas) {
		this.name = name;
		this.gas = Stream.of(gas).collect(Collectors.toSet());
	}
	
	public MainMenuGroup customizeFromOrigin(Collection<? extends GrantedAuthority> gas) {
		MainMenuGroup nmg = new MainMenuGroup();
		nmg.setName(getName());
		nmg.setOrder(getOrder());
		nmg.setItems(getItems().stream().map(it -> it.customizeFromOrigin(gas)).collect(Collectors.toList()));
		return nmg;
	}

	@Override
	public int compareTo(MainMenuGroup o) {
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

	public List<MainMenuItem> getItems() {
		return items;
	}

	public void setItems(List<MainMenuItem> items) {
		this.items = items;
	}
	
	

}
