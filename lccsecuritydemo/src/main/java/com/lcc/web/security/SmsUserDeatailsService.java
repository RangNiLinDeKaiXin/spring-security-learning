package com.lcc.brower.config.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * UserDetailsService获取用户信息
 * @author: lcc
 * @Date: 2018-05-23
 **/
@Slf4j
@Component("smsUserDeatailsService")
public class SmsUserDeatailsService implements UserDetailsService {
//	@Autowired
//	private UserMapper userMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
		//根据用户名查找用户信息 userMapper.selectByusername
		log.info("登录手机号：{}",mobile);
		//AuthorityUtils.commaSeparatedStringToAuthorityList 把字符串转程 GrantedAuthority 对象
		//密码匹配spring 自己做 这里只用从数据库里读出来就好了
		String password=passwordEncoder.encode("123456");
		return new User(mobile,password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
	}
}
