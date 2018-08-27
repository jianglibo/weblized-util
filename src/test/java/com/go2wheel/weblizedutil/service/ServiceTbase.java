package com.go2wheel.weblizedutil.service;

import com.go2wheel.weblizedutil.SpringBaseFort;
import com.go2wheel.weblizedutil.model.UserAccount;

public class ServiceTbase extends SpringBaseFort {

	protected UserAccount createAUser() {
		UserAccount ua = new UserAccount.UserAccountBuilder("ab", "a@b.c", "ajjy").build();
		return userAccountDbService.save(ua);
	}
	
}
