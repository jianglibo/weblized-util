package com.go2wheel.weblizedutil.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.go2wheel.weblizedutil.jooqschema.tables.records.JobLogRecord;
import com.go2wheel.weblizedutil.model.JobLog;
import com.go2wheel.weblizedutil.repository.JobLogRepository;

@Service
@Validated
public class JobLogDbService extends DbServiceBase<JobLogRecord, JobLog> {
	
	public JobLogDbService(JobLogRepository repo) {
		super(repo);
	}

	
	
}
