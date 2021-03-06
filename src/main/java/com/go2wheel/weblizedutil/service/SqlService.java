package com.go2wheel.weblizedutil.service;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.TableImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.go2wheel.weblizedutil.repository.RepositoryBaseImpl;

@Service
public class SqlService {

	@Autowired
	private TableDiscovery tableDiscovery;
	
	@Autowired
	private DSLContext jooq;
	
	public String select(String tableName, int limit) {
		Table<?> ti = tableDiscovery.getTable(tableName);
		Field<?> fi = ti.field(RepositoryBaseImpl.CREATED_AT_FIELD_NAME);
		Result<?> ro;
		if (fi == null) {
			ro = jooq.selectFrom(ti).limit(limit).fetch();
		} else {
			ro = jooq.selectFrom(ti).orderBy(fi.desc()).limit(limit).fetch();
		}
		return ro.toString();
	}
	
	public int delete(String tableName, int id) {
		TableImpl<?> ti = tableDiscovery.getTable(tableName);
		Field<Integer> fi = (Field<Integer>) ti.field(RepositoryBaseImpl.ID_FIELD_NAME);
		int i = jooq.deleteFrom(ti).where(fi.eq(id)).execute();
		return i;
	}

}
