package com.go2wheel.weblizedutil.repository;


import static com.go2wheel.weblizedutil.jooqschema.tables.UserRole.USER_ROLE;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.go2wheel.weblizedutil.jooqschema.tables.records.UserRoleRecord;
import com.go2wheel.weblizedutil.model.UserRole;

@Repository
public class JOOQUserRoleRepository extends RepositoryBaseImpl<UserRoleRecord, UserRole> implements UserRoleRepository {

	@Autowired
	protected JOOQUserRoleRepository(DSLContext jooq) {
		super(USER_ROLE, UserRole.class, jooq);
	}

	@Override
	public UserRole findByName(String name) {
		return jooq.selectFrom(USER_ROLE).where(USER_ROLE.ROLE.eq(name)).fetchOneInto(UserRole.class);
	}
	
	public List<UserRole> findByNames(List<String> rolenames) {
		return jooq.selectFrom(USER_ROLE).where(USER_ROLE.ROLE.in(rolenames)).fetchInto(UserRole.class);
	}

}
