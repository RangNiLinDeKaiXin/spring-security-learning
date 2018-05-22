package com.lcc.brower.config;

import com.lcc.security.properties.SecurityProperties;
import com.lcc.security.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 浏览器配置
 *
 * @author: lcc
 * @Date: 2018-05-21
 **/
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private SecurityProperties securityProperties;
	@Autowired
	private AuthenticationSuccessHandler lccAuthenticationSuccessHandler;
	@Autowired
	private AuthenticationFailureHandler lccAuthenticationFailureHandler;



	//spring 加密解码 用来匹配密码
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
		validateCodeFilter.setAuthenticationFailureHandler(lccAuthenticationFailureHandler);
		validateCodeFilter.setSecurityProperties(securityProperties);
		validateCodeFilter.afterPropertiesSet();
		http
				.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
				//.httpBasic() 默认
				.formLogin()
				//	.loginPage("/lcc-signIn.html") //自定义登录页面
				.loginPage("/authentication/require")
				.loginProcessingUrl("/authentication/form") //为了让UsernamePasswordAuthenticationFilter 知道处理表单认证url 默认是/login
				.successHandler(lccAuthenticationSuccessHandler)
				.failureHandler(lccAuthenticationFailureHandler)
				.and()
				.authorizeRequests()
				//	.antMatchers("/lcc-signIn.html").permitAll()
				.antMatchers("/authentication/require",
						securityProperties.getBrowserProperties().getLoginPage(),
						"/code/image").permitAll()
				//任何请求都要认证
				.anyRequest()
				.authenticated()
				.and().csrf().disable();
	}
}
