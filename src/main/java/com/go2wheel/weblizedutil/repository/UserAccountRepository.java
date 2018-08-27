package com.go2wheel.weblizedutil.repository;

import java.util.List;

import com.go2wheel.weblizedutil.jooqschema.tables.records.UserAccountRecord;
import com.go2wheel.weblizedutil.model.UserAccount;
import com.go2wheel.weblizedutil.model.UserRole;

public interface UserAccountRepository extends RepositoryBase<UserAccountRecord, UserAccount>{
	UserAccount findByEmail(String email);
	UserAccount findByMobile(String mobile);
	UserAccount findByName(String name);
	List<UserAccount> findLikeName(String partOfName);
	UserAccount findByUsernameOrEmailOrMobile(String usernameOrEmailOrMobile);
	
	UserAccount saveRolesAndReturn(UserAccount saved, List<UserRole> roles);
}
