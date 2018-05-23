package com.lcc.security.properties;

import lombok.Data;


/**
 * 验证码配置
 *
 * @author: lcc
 * @Date: 2018-05-22
 **/
@Data
public class ValidateCodeProperties {

	/**
	 * 图片验证码选项
	 */
	private ImageCodeProperties imageCode = new ImageCodeProperties();

	/**
	 * 短信验证码选项
	 */
	private SmsCodeProperties smsCode = new SmsCodeProperties();


}
