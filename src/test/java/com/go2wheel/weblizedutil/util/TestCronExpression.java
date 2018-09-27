package com.go2wheel.weblizedutil.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.junit.Test;
import org.quartz.CronExpression;

public class TestCronExpression {
	
	/**
	 * every second.
	 * @throws ParseException
	 */
	@Test
	public void tAllAsterisk() throws ParseException {
		try {
			new CronExpression("* * * * * * *");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented"));
		}
	}
	
	@Test
	public void tTwoQuestAsterisk() throws ParseException {
		try {
			new CronExpression("* * * ? * ? *");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("can only be specified for Day-of-Month -OR- Day-of-Week"));
		}
	}
	

	@Test
	public void tYears() throws ParseException {
		CronExpression ce = new CronExpression("1 1 1 5 5 ? */5");
		
		LocalDateTime ldt = LocalDateTime.now().withYear(1980);
		
		Instant it = ldt.toInstant(ZoneOffset.ofHours(0));
		Date now = Date.from(it);
		
		for(int i = 0; i< 10; i++) {
			now = ce.getNextValidTimeAfter(now);
			assertThat(now.getYear() + 1900, equalTo(1980 + i * 5 + 5));
		}
		
		ce = new CronExpression("1 1 1 5 5 ? 0/5");
		now = Date.from(it);
		
		for(int i = 0; i< 10; i++) {
			now = ce.getNextValidTimeAfter(now);
			assertThat(now.getYear() + 1900, equalTo(1980 + i * 5 + 5));
		}


		
		
	}

	

	
	@Test
	public void tNoEnd() throws ParseException {
		try {
			new CronExpression("* * * 1/ * ? *");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("'/' must be followed by an integer"));
		}
	}
	
	@Test
	public void tNoBegin() throws ParseException {
		new CronExpression("* * * /2 * ? *");
	}
	
	
	@Test(expected = ParseException.class)
	public void tNoEndDash() throws ParseException {
		new CronExpression("* * * 1- * ? *");
	}
	
	@Test(expected=ParseException.class)
	public void tNoBeginDash() throws ParseException {
		new CronExpression("* * * -2 * ? *");
	}
	
	@Test
	public void tTwoQuestAsterisk1() throws ParseException {
		try {
			new CronExpression("* * * 1/2 * * *");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented"));
		}
	}
	
	/**
	 * every second.
	 * @throws ParseException
	 */
	@Test
	public void tAlAsterisk() throws ParseException {
		CronExpression ce = new CronExpression("* * * * * ? *");
		System.out.println(Instant.now());
		Date d = ce.getNextValidTimeAfter(Date.from(Instant.now()));
		System.out.println(d.toInstant());
	}
	
	@Test(expected=ParseException.class)
	public void tBigRange() throws ParseException {
		new CronExpression("0/70 * * * * ? *");
	}
	
	/**
	 * every second.
	 * @throws ParseException
	 */
	@Test
	public void tZeroRange() throws ParseException {
		new CronExpression("0/0 * * * * ? *");
	}
	
	
	/**
	 * In first minute of an hour fire for every second.
	 * @throws ParseException
	 */
	@Test
	public void mixRangeAndSingle() throws ParseException {
		new CronExpression("* 1,3-5,0/3,L * 1,3-5,1/3 * ? *");
	}
	
	/**
	 * Support for specifying multiple "nth" days is not implemented.
	 * @throws ParseException
	 */
	@Test(expected=ParseException.class)
	public void week2() throws ParseException {
		new CronExpression("* 1,3-5,0/3,L * ? * 6#1,6#3 *");
	}

	/**
	 * java.text.ParseException: '#' option is not valid here. (pos=1)
	 * @throws ParseException
	 */
	@Test(expected=ParseException.class)
	public void weekInmonth() throws ParseException {
		new CronExpression("* * * 6#2 * ? *");
	}
	
	/**
	 * java.text.ParseException: 'L' option is not valid here. (pos=1)
	 * @throws ParseException
	 */
	@Test(expected=ParseException.class)
	public void lastwdOfMonth() throws ParseException {
		new CronExpression("* * * 6L * ? *");
	}
	
	/**
	 * the last friday of the month
	 * @throws ParseException
	 */
	@Test
	public void lastwdOfMonth1() throws ParseException {
		new CronExpression("* * * ? * 6L *");
	}
	
	/**
	 * no
	 * @throws ParseException
	 */
	@Test(expected=ParseException.class)
	public void testMixNumberAndWeekName() throws ParseException {
		new CronExpression("* * * ? * 1-SAT *");
	}

	@Test
	public void testWeekDayStep() throws ParseException {
		new CronExpression("* * * ? * 1/2 *");
	}
	
	
	@Test
	public void testFiveSec() throws ParseException {
		CronExpression ce = new CronExpression("*/5 * * ? * * *");
		
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = ce.getNextValidTimeAfter(now);
		
		int mod = d.getSeconds() % 5;
		
		assertThat(mod, equalTo(0));
	}
	
	@Test
	public void testFiveSec1() throws ParseException {
		CronExpression ce = new CronExpression("0/5 * * ? * * *");
		
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = ce.getNextValidTimeAfter(now);
		
		int mod = d.getSeconds() % 5;
		
		assertThat(mod, equalTo(0));
	}
	
	@Test
	public void testFiveSec2() throws ParseException {
		CronExpression ce = new CronExpression("1/5 * * ? * * *");
		
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = ce.getNextValidTimeAfter(now);
		
		int mod = d.getSeconds() % 5;
		
		assertThat(mod, equalTo(1));
	}
	
	@Test
	public void testFiveSec33() throws ParseException {
		CronExpression ce = new CronExpression("3/5 * * ? * * *");
		
		Instant is = Instant.now();
		Date now = Date.from(is);
		
		Date d = now;
		
		for(int idx = 0; idx < 20; idx++) {
			d = ce.getNextValidTimeAfter(d);
			int mod = d.getSeconds() % 5;
			System.out.println(d.getSeconds());
			assertThat(mod, equalTo(3));
		}
	}
	
	
	@Test
	public void testFiveSec3() throws ParseException {
		CronExpression ce = new CronExpression("13/5 * * ? * * *");
		
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = ce.getNextValidTimeAfter(now);
		
		int mod = d.getSeconds() % 5;
		
		assertThat(mod, equalTo(3));
	}
	
	
	@Test
	public void testEveryDay() throws ParseException {
		CronExpression ce = new CronExpression("1 1 1 ? * * *");
		
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = ce.getNextValidTimeAfter(now);
		
		int t1 = now.getDate();
		int t2 = d.getDate();
		
		assertThat(t2, equalTo(t1 + 1));
	}
	
	@Test
	public void testEveryDay1() throws ParseException {
		CronExpression ce = new CronExpression("1 1 1 1/1 * ? *");
		
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = ce.getNextValidTimeAfter(now);
		Date d1 = ce.getNextValidTimeAfter(d);
		
		int t1 = d.getDate();
		int t2 = d1.getDate();
		
		assertThat(t2, equalTo(t1 + 1));
	}
	
	
	@Test
	public void tDate() throws ParseException {
		CronExpression ce = new CronExpression("1 1 1 1/3 * ? *");
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = now;
		for(int idx = 0; idx < 20; idx++) {
			d = ce.getNextValidTimeAfter(d);
			int i = d.getDate() % 3;
			System.out.println(d.getDate());
			assertThat(i, equalTo(1)); // 31 1	4 7, 
		}
	}
	
	@Test
	public void tDate1() throws ParseException {
		CronExpression ce = new CronExpression("1 1 1 27/3 * ? *");
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = now;

		for(int idx = 0; idx < 20; idx++) {
			d = ce.getNextValidTimeAfter(d);
			int i = d.getDate() % 3;
			System.out.println(d.getDate());
			assertThat(i, equalTo(0)); // 27, 30, 27, 30, 不会绕过来。 
		}
	}
	
	@Test
	public void tRange3() throws ParseException {
		CronExpression ce = new CronExpression("1 1 1 25-25 * ? *");
		
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = now;

		for(int idx = 0; idx < 20; idx++) {
			d = ce.getNextValidTimeAfter(d);
			System.out.println(d.getDate()); // only 25
 		}
	}
	
	@Test
	public void tRangeMinutes() throws ParseException {
		CronExpression ce = new CronExpression("1 1 20-3 * * ? *");
		
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = now;

		for(int idx = 0; idx < 20; idx++) {
			d = ce.getNextValidTimeAfter(d);
			System.out.println(d.getHours()); // 20,21,22,23,0,1,2,3
 		}
	}
	
	@Test
	public void tRange4() throws ParseException {
		CronExpression ce = new CronExpression("1 1 1 25-24 * ? *");
		
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = now;

		for(int idx = 0; idx < 20; idx++) {
			d = ce.getNextValidTimeAfter(d);
			System.out.println(d.getDate()); // only 25
 		}
	}
	
	
	@Test
	public void tRange() throws ParseException {
		CronExpression ce = new CronExpression("1 1 1 25-5 * ? *");
		
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = now;

		for(int idx = 0; idx < 20; idx++) {
			d = ce.getNextValidTimeAfter(d);
			System.out.println(d.getDate()); // round up, 25-31, 1-5
 		}
	}
	
	@Test
	public void tRange1() throws ParseException {
		CronExpression ce = new CronExpression("1 1 1 15-10 * ? *");
		
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = now;

		for(int idx = 0; idx < 40; idx++) {
			d = ce.getNextValidTimeAfter(d);
			System.out.println(d.getDate()); // round up. 15-31 and 1-10
 
		}
	}
	
	@Test
	public void tStep() throws ParseException {
		CronExpression ce = new CronExpression("1 1 1 25/3 * ? *");
		
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = now;

		for(int idx = 0; idx < 40; idx++) {
			d = ce.getNextValidTimeAfter(d);
			System.out.println(d.getDate()); // won't round up. 25,28,31。
 		}
	}
	
	@Test
	public void tStep2() throws ParseException {
		CronExpression ce = new CronExpression("1 55/6 1 * * ? *");
		
		Instant is = Instant.now();
		Date now = Date.from(is);
		Date d = now;

		for(int idx = 0; idx < 20; idx++) {
			d = ce.getNextValidTimeAfter(d);
			System.out.println(d.getMinutes()); // won't round up. 25,28,31。
 		}
	}

	
}
