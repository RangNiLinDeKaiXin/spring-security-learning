package com.lcc.web.entity;

import lombok.Data;

/**
 * @author: lcc
 * @Date: 2018-05-21
 **/
@Data
public class User {
	private String username;
	private String password="123456";

	public User(String username) {
		this.username = username;
	}
}
