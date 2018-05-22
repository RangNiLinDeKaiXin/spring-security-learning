package com.lcc.security.properties;

import lombok.Data;


/**
 * 图片验证码选项
 *
 * @author: lcc
 * @Date: 2018-05-22
 **/
@Data
public class ImageCodeProperties {

	private int length = 4;

	/**
	 * 过期时间
	 */
	private int expireIn = 60;

	/**
	 * 验证码的宽
	 */
	private int width = 67;

	/**
	 * 验证码的高
	 */
	private int height = 23;

	private String url;

}
