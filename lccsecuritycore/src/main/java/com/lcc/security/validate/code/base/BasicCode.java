package com.lcc.security.validate.code.base;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: lcc
 * @Date: 2018-05-22
 **/
@Data
public class BasicCode {
	private String code;
	private LocalDateTime expireTime;

	public BasicCode(String code, int expireInt) {
		this.code = code;
		this.expireTime = LocalDateTime.now().plusSeconds(expireInt);
	}

	public boolean isExpire() {
		if (LocalDateTime.now().compareTo(expireTime) > 0) {
			return true;
		}
		return false;
	}
}
