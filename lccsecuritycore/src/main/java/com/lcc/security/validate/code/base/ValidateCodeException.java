package com.lcc.security.validate.code.base;

import org.springframework.security.core.AuthenticationException;


/**
 * 验证码异常
 * 继承身份验证异常的基类
 *
 * @author: lcc
 * @Date: 2018-05-22
 **/
public class ValidateCodeException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	/**
	 * 实现一个父类的构造方法
	 *
	 * @param msg
	 */
	public ValidateCodeException(String msg) {
		super(msg);
	}
}
