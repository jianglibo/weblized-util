package com.go2wheel.weblizedutil.controller;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.go2wheel.weblizedutil.SpringBaseTWithWeb;

public class TestKeyValueController extends SpringBaseTWithWeb {

	@Test
	public void testModelAttribute() throws Exception {
		String url = KeyValuesController.MAPPING_PATH;
		
		mockMvc.perform(delete(url).param("ids", "133").accept(MediaType.TEXT_PLAIN)).andExpect(status().isOk())
				.andExpect(content()
						.string(containsString("class=\"pure-menu-item menu-item-divided pure-menu-selected\"")))
				.andExpect(content().string(not(containsString("<header>"))))
				.andExpect(content().string(containsString("Index Page")));
	}

}
