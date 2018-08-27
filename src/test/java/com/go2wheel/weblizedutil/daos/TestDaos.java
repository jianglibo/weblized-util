package com.go2wheel.weblizedutil.daos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.text.ParseException;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import com.go2wheel.weblizedutil.SpringBaseFort;
import com.go2wheel.weblizedutil.model.ReusableCron;
import com.go2wheel.weblizedutil.repository.ReusableCronRepository;
import com.go2wheel.weblizedutil.service.ReusableCronDbService;

public class TestDaos extends SpringBaseFort {
	
	@Autowired
	private ReusableCronDbService reusableCronDbService;
	
	
	@Autowired
	private ReusableCronRepository reusableCronRepository;
	
	@Before
	public void b() {
		reusableCronRepository.findAll().stream().forEach(recron -> reusableCronRepository.delete(recron));
	}
	

	

	
	@Test(expected = ConstraintViolationException.class)
	public void tCronExpressionEmpty() {
		long num  = reusableCronRepository.count();
		assertThat(num, equalTo(0L));
		ReusableCron ma = new ReusableCron("", "你好");
		ma = reusableCronDbService.save(ma);
		num  = reusableCronRepository.count();
		assertThat(num, equalTo(1L));
		assertThat(ma.getId(), greaterThan(0));
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void tCronExpressionNull() {
		long num  = reusableCronRepository.count();
		assertThat(num, equalTo(0L));
		ReusableCron ma = new ReusableCron(null, "你好");
		ma = reusableCronDbService.save(ma);
		num  = reusableCronRepository.count();
		assertThat(num, equalTo(1L));
		assertThat(ma.getId(), greaterThan(0));
	}

	@Test(expected = ConstraintViolationException.class)
	public void tCronNoValidate() {
		long num  = reusableCronRepository.count();
		assertThat(num, equalTo(0L));
		ReusableCron ma = new ReusableCron("hello", "你好");
		ma = reusableCronDbService.save(ma);
		num  = reusableCronRepository.count();
		assertThat(num, equalTo(1L));
		assertThat(ma.getId(), greaterThan(0));
	}
	
	@Test()
	public void tCronValidate() throws ParseException {
		long num  = reusableCronRepository.count();
		assertThat(num, equalTo(0L));
		ReusableCron ma = new ReusableCron("0 30 6,12 * * ?", "hello");
		ma = reusableCronDbService.save(ma);
	}
	
	@Test(expected = DuplicateKeyException.class)
	public void tCronDuplicate() throws ParseException {
		long num  = reusableCronRepository.count();
		assertThat(num, equalTo(0L));
		ReusableCron ma = new ReusableCron("0 30 6,12 * * ?", "hello");
		ma = reusableCronDbService.save(ma);
		
		ma = new ReusableCron("0 30 6,12 * * ?", "hello");
		ma = reusableCronDbService.save(ma);

	}


}
