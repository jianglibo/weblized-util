package com.go2wheel.weblizedutil.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.go2wheel.weblizedutil.jooqschema.tables.records.BigObRecord;
import com.go2wheel.weblizedutil.model.BigOb;
import com.go2wheel.weblizedutil.repository.BigObRepository;

@Service
@Validated
public class BigObDbService extends DbServiceBase<BigObRecord, BigOb> {
	
	public BigObDbService(BigObRepository repo) {
		super(repo);
	}

	public BigOb findByName(String name) {
		return ((BigObRepository)repo).findByName(name);
	}
}
