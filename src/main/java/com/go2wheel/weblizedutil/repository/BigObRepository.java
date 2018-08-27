package com.go2wheel.weblizedutil.repository;

import com.go2wheel.weblizedutil.jooqschema.tables.records.BigObRecord;
import com.go2wheel.weblizedutil.model.BigOb;

public interface BigObRepository extends RepositoryBase<BigObRecord, BigOb>{

	BigOb findByName(String name);

}
