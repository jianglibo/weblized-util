package com.go2wheel.weblizedutil.controller;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Date;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.go2wheel.weblizedutil.SpringBaseTWithWeb;

public class TestQuartzCronExpressionController extends SpringBaseTWithWeb {

	@Test
	public void testValidExpression() throws Exception {
		String url = QuartzCronExpressController.MAPPING_PATH + "/next";
		String ms = Date.from(Instant.ofEpochSecond(0L)).getTime() + "";
		String cron = "1 1 1 3/5 * ? *";
		mockMvc.perform(get(url).param("ms", ms).param("cron", cron).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().json("{data: [147661000]}", true));
	}

	@Test
	public void testInValidExpression() throws Exception {
		String url = QuartzCronExpressController.MAPPING_PATH + "/next";
		String ms = new Date().getTime() + "";
		String cron = "1 1 1 3/89 * ? *";
		mockMvc.perform(get(url).param("ms", ms).param("cron", cron).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().json("{message:\"Increment > 31 : 89\",errors:{_global:[]}}", true));
	}
	
	@Test
	public void testWrongParameter() throws Exception {
		String url = QuartzCronExpressController.MAPPING_PATH + "/next";
		String ms = "kku";
		String cron = "1 1 1 3/8 * ? *";
		mockMvc.perform(get(url).param("ms", ms).param("cron", cron).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is4xxClientError());

	}

}
