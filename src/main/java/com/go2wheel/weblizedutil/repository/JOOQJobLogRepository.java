package com.go2wheel.weblizedutil.repository;


import static com.go2wheel.weblizedutil.jooqschema.tables.JobLog.JOB_LOG;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.go2wheel.weblizedutil.jooqschema.tables.records.JobLogRecord;
import com.go2wheel.weblizedutil.model.JobLog;

@Repository
public class JOOQJobLogRepository extends RepositoryBaseImpl<JobLogRecord, JobLog> implements JobLogRepository {

	@Autowired
	protected JOOQJobLogRepository(DSLContext jooq) {
		super(JOB_LOG, JobLog.class, jooq);
	}

}
