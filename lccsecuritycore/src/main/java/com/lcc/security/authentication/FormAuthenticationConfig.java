package com.lcc.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


/**
 * 表单登录配置
 *
 * @author: lcc
 * @Date: 2018-05-24
 **/
@Component
public class FormAuthenticationConfig {

	@Autowired
	protected AuthenticationSuccessHandler lccAuthenticationSuccessHandler;

	@Autowired
	protected AuthenticationFailureHandler lccAuthenticationFailureHandler;

	public void configure(HttpSecurity http) throws Exception {
		http
				//.httpBasic() 默认
				.formLogin()
				//	.loginPage("/lcc-signIn.html") //自定义登录页面
				.loginPage("/authentication/require")
				.loginProcessingUrl("/authentication/form") //为了让UsernamePasswordAuthenticationFilter 知道处理表单认证url 默认是/login
				.successHandler(lccAuthenticationSuccessHandler)
				.failureHandler(lccAuthenticationFailureHandler);

	}

}

