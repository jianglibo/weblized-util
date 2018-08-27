package com.go2wheel.weblizedutil.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.go2wheel.weblizedutil.jooqschema.tables.records.UserRoleRecord;
import com.go2wheel.weblizedutil.model.UserRole;
import com.go2wheel.weblizedutil.repository.UserRoleRepository;

@Service
@Validated
public class UserRoleDbService extends DbServiceBase<UserRoleRecord, UserRole> {
	
	public UserRoleDbService(UserRoleRepository repo) {
		super(repo);
	}

	public UserRole findByName(String name) {
		return ((UserRoleRepository)repo).findByName(name);
	}

	public List<UserRole> findByNames(List<String> rolenames) {
		return ((UserRoleRepository)repo).findByNames(rolenames);
	}

}
