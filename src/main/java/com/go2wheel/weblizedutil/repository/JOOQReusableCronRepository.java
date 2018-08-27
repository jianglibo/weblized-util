package com.go2wheel.weblizedutil.repository;


import static com.go2wheel.weblizedutil.jooqschema.tables.ReuseableCron.REUSEABLE_CRON;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.go2wheel.weblizedutil.jooqschema.tables.records.ReuseableCronRecord;
import com.go2wheel.weblizedutil.model.ReusableCron;

@Repository
public class JOOQReusableCronRepository extends RepositoryBaseImpl<ReuseableCronRecord, ReusableCron> implements ReusableCronRepository {

	@Autowired
	protected JOOQReusableCronRepository(DSLContext jooq) {
		super(REUSEABLE_CRON, ReusableCron.class, jooq);
	}

	@Override
	public ReusableCron findByExpression(String expression) {
		return jooq.selectFrom(REUSEABLE_CRON).where(REUSEABLE_CRON.EXPRESSION.eq(expression)).fetchAnyInto(ReusableCron.class);
	}
}
