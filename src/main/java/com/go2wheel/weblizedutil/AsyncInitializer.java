package com.go2wheel.weblizedutil;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class AsyncInitializer {

	@Autowired
	private ConfigFileWatcher configFileWatcher;
	
	@PostConstruct
	public void post() {
		configFileWatcher.watch();
	}
}
