package com.lcc.security.validate.code;

import com.lcc.security.properties.SecurityProperties;
import com.lcc.security.validate.code.image.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author: lcc
 * @Date: 2018-05-22
 **/
@Component("imageCodeSecurityConfig")
public class ImageCodeSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private AuthenticationSuccessHandler lccAuthenticationSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler lccAuthenticationFailureHandler;
	@Qualifier("myUserDetailsService")
	@Autowired
	private UserDetailsService userDeatailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;


	@Override
	public void configure(HttpSecurity http) throws Exception {
		ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
		validateCodeFilter.setAuthenticationFailureHandler(lccAuthenticationFailureHandler);
		validateCodeFilter.setSecurityProperties(securityProperties);
		validateCodeFilter.afterPropertiesSet();
        //默认为DaoAuthenticationProvider   需要设置默认的 userDeatailsService 与 默认的 passwordEncoder   注意AbstractUserDetailsAuthenticationProvider类的 additionalAuthenticationChecks 检查用户名密码
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDeatailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

		http
				// 将过滤器添加到UsernamePasswordAuthenticationFilter后面
				.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
				.authenticationProvider(daoAuthenticationProvider)
		;

	}

}