package com.lcc.security.validate.code.sms;

/**
 * @author: lcc
 * @Date: 2018-05-22
 **/
public class DefaultSmsCodeSender implements SmsCodeSender {
	@Override
	public void send(String mobile, String code) {
		System.out.println("向"+mobile+"发送"+code);
	}
}
