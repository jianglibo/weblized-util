package com.go2wheel.weblizedutil.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.go2wheel.weblizedutil.job.JobBaseFort;
import com.go2wheel.weblizedutil.model.BigOb;

public class TestBigObDbService extends JobBaseFort {

	@Test
	public void tCreate() {
		List<BigOb> bigobs = bigObDbService.findAll();
		clearDb();
		
		bigobs = bigObDbService.findAll();
		
		assertTrue(bigobs.size() == 0);
		
		BigOb bo = new BigOb();
		bo.setContent("hhhhhhhhhhhhhhhhhhhhhh".getBytes());
		bo.setCreatedAt(new Date());
		bo.setDescription("description");
		bo.setName("hello");
		bo = bigObDbService.save(bo);
		assertThat(bo.getId(), greaterThan(99));
		
		int oid = bo.getId();
		
		bo = bigObDbService.findByName(bo.getName());
		
		assertThat(bo.getId(), equalTo(oid));
	}

}
