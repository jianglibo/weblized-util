package com.go2wheel.weblizedutil.repository;


import static com.go2wheel.weblizedutil.jooqschema.tables.KeyValue.KEY_VALUE;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.go2wheel.weblizedutil.jooqschema.tables.records.KeyValueRecord;
import com.go2wheel.weblizedutil.model.KeyValue;

@Repository
public class JOOQKeyValueRepository extends RepositoryBaseImpl<KeyValueRecord, KeyValue> implements KeyValueRepository {

	@Autowired
	protected JOOQKeyValueRepository(DSLContext jooq) {
		super(KEY_VALUE, KeyValue.class, jooq);
	}

	@Override
	public KeyValue findOneByKey(String key) {
		return jooq.selectFrom(KEY_VALUE).where(KEY_VALUE.ITEM_KEY.eq(key)).fetchOneInto(KeyValue.class);
	}

	@Override
	public List<KeyValue> findManyByKeyPrefix(String key) {
		return jooq.selectFrom(KEY_VALUE).where(KEY_VALUE.ITEM_KEY.startsWith(key)).fetchInto(KeyValue.class);
	}

}
