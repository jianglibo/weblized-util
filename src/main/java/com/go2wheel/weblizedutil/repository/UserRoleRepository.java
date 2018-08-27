package com.go2wheel.weblizedutil.repository;

import java.util.List;

import com.go2wheel.weblizedutil.jooqschema.tables.records.UserRoleRecord;
import com.go2wheel.weblizedutil.model.UserRole;

public interface UserRoleRepository extends RepositoryBase<UserRoleRecord, UserRole>{

	UserRole findByName(String name);
	
	List<UserRole> findByNames(List<String> rolenames);

}
