package com.lcc.brower.config;

import com.lcc.security.authentication.FormAuthenticationConfig;
import com.lcc.security.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.lcc.security.properties.SecurityProperties;
import com.lcc.security.validate.code.ImageCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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

	@Autowired
	private ImageCodeSecurityConfig imageCodeSecurityConfig;

	@Autowired
	private FormAuthenticationConfig formAuthenticationConfig;


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
		formAuthenticationConfig.configure(http);
		http
				// 应用验证码安全配置
				.apply(imageCodeSecurityConfig)
				.and()
				// 应用短信验证码认证安全配置
				.apply(smsCodeAuthenticationSecurityConfig)
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
		;

	}
}
