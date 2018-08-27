package com.go2wheel.weblizedutil.event;

import org.springframework.context.ApplicationEvent;

public class ServerSwitchEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServerSwitchEvent(Object source) {
		super(source);
	}
	

}
