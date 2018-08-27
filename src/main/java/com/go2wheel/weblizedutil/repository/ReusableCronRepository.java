package com.go2wheel.weblizedutil.repository;

import com.go2wheel.weblizedutil.jooqschema.tables.records.ReuseableCronRecord;
import com.go2wheel.weblizedutil.model.ReusableCron;

public interface ReusableCronRepository extends RepositoryBase<ReuseableCronRecord, ReusableCron>{

	ReusableCron findByExpression(String expression);

}
