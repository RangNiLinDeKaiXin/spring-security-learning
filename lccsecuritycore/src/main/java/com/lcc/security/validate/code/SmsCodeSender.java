package com.lcc.security.validate.code;

/**
 * @author: lcc
 * @Date: 2018-05-22
 **/
public interface SmsCodeSender {
	void send(String mobile,String code);
}
