package com.go2wheel.weblizedutil.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import com.go2wheel.weblizedutil.SpringBaseTWithWeb;

public class TestComboController extends SpringBaseTWithWeb {


	@Test
	public void testCombo() throws Exception {

		MvcResult result = this.mockMvc.perform(get("/combo/1.82.3?/t/hello.js")).andReturn();

		this.mockMvc.perform(asyncDispatch(result)).andExpect(status().isOk())
				.andExpect(header().string("Content-Type", ComboController.APPLICATION_JS))
				.andExpect(content().string("hello js."));
	}

	@Test
	public void testComboEmpty() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/combo/1.82.3")).andReturn();
		this.mockMvc.perform(asyncDispatch(result)).andExpect(status().is(HttpStatus.NOT_FOUND.value()))
				.andExpect(content().string("empty file list."));
	}

	@Test
	public void testComboMixed() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/combo/1.82.3?/t/hello.js&/t/hello.css")).andReturn();
		this.mockMvc.perform(asyncDispatch(result)).andExpect(status().is(HttpStatus.NOT_FOUND.value()))
				.andExpect(content().string("mixed file type."));
	}
	
	@Test
	public void testComboMissing() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/combo/1.82.3?/t/hello.js&/t/hello1.js")).andReturn();
		this.mockMvc.perform(asyncDispatch(result)).andExpect(status().is(HttpStatus.NOT_FOUND.value()))
				.andExpect(content().string("some files not exists."));
	}


	@Test
	public void testImg() throws Exception {
		this.mockMvc.perform(get("/img/layout-icon.jpg")).andExpect(status().isOk());
	}

	@Test
	public void tResourcePtn() throws IOException {
		Resource[] jsrs = wac.getResources("classpath:public/**/*.js");
		Resource[] csrs = wac.getResources("classpath:public/**/*.css");

		assertThat(jsrs.length, greaterThan(27));
		assertThat(csrs.length, greaterThan(96));
	}

}
