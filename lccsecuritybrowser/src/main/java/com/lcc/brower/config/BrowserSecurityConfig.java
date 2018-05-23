package com.lcc.brower.config;

import com.lcc.security.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.lcc.security.properties.SecurityProperties;
import com.lcc.security.validate.code.SmsCodeFilter;
import com.lcc.security.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

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

	@Qualifier("dataSource")
	@Autowired
	private DataSource dataSource;

	@Qualifier("myUserDetailsService")
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

	//spring 加密解码 用来匹配密码
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//记住我 读写数据库
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		//启动建表
		//	tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
		validateCodeFilter.setAuthenticationFailureHandler(lccAuthenticationFailureHandler);
		validateCodeFilter.setSecurityProperties(securityProperties);
		validateCodeFilter.afterPropertiesSet();


		SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
		smsCodeFilter.setAuthenticationFailureHandler(lccAuthenticationFailureHandler);
		smsCodeFilter.setSecurityProperties(securityProperties);
		smsCodeFilter.afterPropertiesSet();
		http
				.addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
				//.httpBasic() 默认
				.formLogin()
				//	.loginPage("/lcc-signIn.html") //自定义登录页面
				.loginPage("/authentication/require")
				.loginProcessingUrl("/authentication/form") //为了让UsernamePasswordAuthenticationFilter 知道处理表单认证url 默认是/login
				.successHandler(lccAuthenticationSuccessHandler)
				.failureHandler(lccAuthenticationFailureHandler)
				.and()
				.rememberMe()
				.tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(securityProperties.getBrowserProperties().getRememberme())
				//取用户名做登录
				.userDetailsService(userDetailsService)
				.and()
				.authorizeRequests()
				//	.antMatchers("/lcc-signIn.html").permitAll()
				.antMatchers("/authentication/require",
						securityProperties.getBrowserProperties().getLoginPage(),
						"/code/*").permitAll()
				//任何请求都要认证
				.anyRequest()
				.authenticated()
				.and().csrf().disable()
				.apply(smsCodeAuthenticationSecurityConfig)
		;

	}
}
