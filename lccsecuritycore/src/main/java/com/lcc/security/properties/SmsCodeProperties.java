package com.lcc.security.properties;

import lombok.Data;


/**
 * 图片验证码选项
 *
 * @author: lcc
 * @Date: 2018-05-22
 **/
@Data
public class SmsCodeProperties {

	private int length = 6;

	/**
	 * 过期时间
	 */
	private int expireIn = 60;


	private String url;

}
