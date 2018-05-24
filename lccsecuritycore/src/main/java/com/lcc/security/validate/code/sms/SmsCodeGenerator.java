package com.lcc.security.validate.code.sms;

import com.lcc.security.properties.SecurityProperties;
import com.lcc.security.validate.code.base.BasicCode;
import com.lcc.security.validate.code.base.ValidateCodeGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author: lcc
 * @Date: 2018-05-22
 **/
@Component
public class SmsCodeGenerator implements ValidateCodeGenerator {
	@Autowired
	private SecurityProperties securityProperties;
	@Override
	public BasicCode generate(ServletWebRequest request) {
		String code = RandomStringUtils.random(securityProperties.getValidateCodeProperties().getSmsCode().getLength());
		code="12345";
		return new BasicCode(code,securityProperties.getValidateCodeProperties().getSmsCode().getExpireIn());
	}
}
