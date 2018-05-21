package com.lcc.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author: lcc
 * @Date: 2018-05-21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	@Test
	public void whenQuerySuccess() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/user/list")
				.param("username","xiaohong")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				//返回状态为200
				.andExpect(MockMvcResultMatchers.status().isOk())
				//返回一个集合 集合长度为3  语法https://github.com/json-path/JsonPath
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3));
	}
}
