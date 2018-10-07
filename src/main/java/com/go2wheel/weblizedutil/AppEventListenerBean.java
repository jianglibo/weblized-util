package com.go2wheel.weblizedutil;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.go2wheel.weblizedutil.exception.UnexpectlyCallMethodException;
import com.go2wheel.weblizedutil.model.UserAccount;
import com.go2wheel.weblizedutil.service.ReusableCronDbService;
import com.go2wheel.weblizedutil.service.UserAccountDbService;
import com.go2wheel.weblizedutil.value.CommonRoleNames;

@Component
public class AppEventListenerBean implements EnvironmentAware {
	
	@Autowired
	private ReusableCronDbService reuseableCronDbService;
	
	private Environment environment;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserAccountDbService userAccountDbService;

    private static final Logger logger = LoggerFactory.getLogger(AppEventListenerBean.class);
    
    /**
     *  @see ApplicationEvent
     *  
     * @param event
     */
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
    }
    
    @EventListener
    public void onApplicationStartedEvent(ApplicationStartedEvent event) throws IOException, UnexpectlyCallMethodException {
//    	ApplicationState.IS_PROD_MODE = !Arrays.stream(environment.getActiveProfiles()).anyMatch(p -> "dev".equals(p));
    	createInitUsers();
//    	logger.info("application on dev mode: " + ApplicationState.IS_PROD_MODE);
    	logger.info("onApplicationStartedEvent be called.");
    }
    
	private void createInitUsers() throws UnexpectlyCallMethodException {
		UserAccount ua = new UserAccount.UserAccountBuilder("jianglibo", "jianglibo@gmail.com", "123456")
				.withRoles(CommonRoleNames.ROLE_SUPERMAN)
				.build();
		try {
			userAccountDbService.createUser(ua, passwordEncoder);
		} catch (DuplicateKeyException e) {
		}
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
