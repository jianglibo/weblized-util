package com.go2wheel.weblizedutil;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.go2wheel.weblizedutil.event.ServerSwitchEvent;
import com.go2wheel.weblizedutil.value.FacadeResult;

@Component
public class ApplicationState {

	public static final String APPLICATION_STATE_PERSIST_FILE = "application-state.yml";
	
	public static boolean IS_PROD_MODE = false;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	private Locale local = Locale.CHINESE;
	
	private FacadeResult<?> facadeResult;

	private CommandStepState step = CommandStepState.INIT_START;
	
	@Value("${expectit.echo}")
	private boolean expectitEcho;
	
	public static enum CommandStepState {
		INIT_START, WAITING_SELECT, BOX_SELECTED
	}
	
	public void fireSwitchEvent() {
		ServerSwitchEvent sce = new ServerSwitchEvent(this);
		applicationEventPublisher.publishEvent(sce);
	}

	public CommandStepState getStep() {
		return step;
	}

	public void setStep(CommandStepState step) {
		this.step = step;
	}

	public Locale getLocal() {
		return local;
	}

	public void setLocal(Locale local) {
		this.local = local;
	}

	public FacadeResult<?> getFacadeResult() {
		return facadeResult;
	}

	public void setFacadeResult(FacadeResult<?> facadeResult) {
		this.facadeResult = facadeResult;
	}
}
