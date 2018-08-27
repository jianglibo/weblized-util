package com.go2wheel.weblizedutil.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.go2wheel.weblizedutil.jooqschema.tables.records.KeyValueRecord;
import com.go2wheel.weblizedutil.model.KeyValue;
import com.go2wheel.weblizedutil.repository.KeyValueRepository;
import com.go2wheel.weblizedutil.value.KeyValueProperties;

@Service
@Validated
public class KeyValueDbService extends DbServiceBase<KeyValueRecord, KeyValue> {

	public KeyValueDbService(KeyValueRepository repo) {
		super(repo);
	}
	
	public List<String> getListString(String key) {
		return null;
	}

	public KeyValue findOneByKey(String... keys) {
		String key = String.join(".", keys);
		return ((KeyValueRepository)repo).findOneByKey(key);
	}
	
	public List<KeyValue> findManyByKeyPrefix(String... keys) {
		String key = String.join(".", keys);
		return ((KeyValueRepository)repo).findManyByKeyPrefix(key); 
	}

	public KeyValue create(String key, String value) {
		KeyValue kv = new KeyValue(key, value);
		return save(kv);
	}

	public KeyValueProperties getPropertiesByPrefix(String... keys) {
		String prefix = String.join(".", keys);
		return new KeyValueProperties(findManyByKeyPrefix(prefix), prefix);
	}
}
