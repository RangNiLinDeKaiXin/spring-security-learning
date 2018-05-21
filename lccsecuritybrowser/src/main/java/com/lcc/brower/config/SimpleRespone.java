package com.lcc.brower.config;

import lombok.Data;

/**
 * @author: lcc
 * @Date: 2018-05-21
 **/
@Data
public class SimpleRespone {
	public SimpleRespone(Object content) {
		this.content = content;
	}
	private Object content;
}
