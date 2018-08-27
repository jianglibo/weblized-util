package com.go2wheel.weblizedutil.security;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.go2wheel.weblizedutil.SpringBaseFort;
import com.go2wheel.weblizedutil.exception.UnexpectlyCallMethodException;
import com.go2wheel.weblizedutil.model.UserAccount;
import com.go2wheel.weblizedutil.service.UserAccountDbService;

public class TestUser extends SpringBaseFort {
	
	@Autowired
	private UserAccountDbService userAccountDbService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	public void tCreateUser() throws UnexpectlyCallMethodException {
		clearDb();
		String plainPassword = "akukccc";
		UserAccount ua = new UserAccount.UserAccountBuilder("jianglibo", "jianglibo@gmail.com", plainPassword).withRoles("admin", "superuser").build();
		ua = userAccountDbService.createUser(ua, passwordEncoder);
		
		assertThat(ua.getMobile().length(), greaterThan(10));
		assertNotEquals(ua.getPassword(), plainPassword);
		
		assertThat(ua.getAuthorities().size(), equalTo(2));
		
		UserAccount fromdb = userAccountDbService.findByUsernameOrEmailOrMobile("jianglibo");
		assertThat(fromdb.getAuthorities().size(), equalTo(2));
	}

}
