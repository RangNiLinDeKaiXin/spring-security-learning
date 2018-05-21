package com.lcc.security.properties;

import lombok.Data;

/**
 * @author: lcc
 * @Date: 2018-05-21
 **/
@Data
public class BrowserProperties {
	//登录页面
	private String loginPage="/lcc-signIn.html";
	//配置处理方式
    private LoginType loginType=LoginType.JSON;
}
