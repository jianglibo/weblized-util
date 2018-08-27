package com.go2wheel.weblizedutil.repository;

import java.util.List;

import com.go2wheel.weblizedutil.jooqschema.tables.records.KeyValueRecord;
import com.go2wheel.weblizedutil.model.KeyValue;

public interface KeyValueRepository extends RepositoryBase<KeyValueRecord, KeyValue>{

	KeyValue findOneByKey(String key);
	
	/**
	 * When find by 'a.b', it returns a.b.c = 1 and a.b.d = 2 etc. 
	 * @param keyPrefix
	 * @return
	 */
	List<KeyValue> findManyByKeyPrefix(String keyPrefix);

}
