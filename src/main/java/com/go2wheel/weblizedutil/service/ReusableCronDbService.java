package com.go2wheel.weblizedutil.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.go2wheel.weblizedutil.jooqschema.tables.records.ReuseableCronRecord;
import com.go2wheel.weblizedutil.model.ReusableCron;
import com.go2wheel.weblizedutil.repository.ReusableCronRepository;

@Service
@Validated
public class ReusableCronDbService extends DbServiceBase<ReuseableCronRecord, ReusableCron> {

	public ReusableCronDbService(ReusableCronRepository reusableCronRepository) {
		super(reusableCronRepository);
	}

	public ReusableCron findByExpression(String expression) {
		return ((ReusableCronRepository)repo).findByExpression(expression);
	}
	
}
