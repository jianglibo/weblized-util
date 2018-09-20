package com.go2wheel.weblizedutil.util;

import java.text.ParseException;

import org.junit.Test;
import org.quartz.CronExpression;

public class TestCronExpression {
	
	/**
	 * every second.
	 * @throws ParseException
	 */
	@Test(expected=ParseException.class)
	public void tAllAsterisk() throws ParseException {
		new CronExpression("* * * * * * *");
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

}
