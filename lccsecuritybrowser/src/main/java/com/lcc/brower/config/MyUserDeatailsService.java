package com.lcc.brower.config;

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
 * @Date: 2018-05-21
 **/
@Slf4j
@Component
public class MyUserDeatailsService implements UserDetailsService {
//	@Autowired
//	private UserMapper userMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//根据用户名查找用户信息 userMapper.selectByusername
		log.info("登录用户名：{}",username);
		//AuthorityUtils.commaSeparatedStringToAuthorityList 把字符串转程 GrantedAuthority 对象
		//密码匹配spring 自己做 这里只用从数据库里读出来就好了
		String password=passwordEncoder.encode("123456");
		return new User(username,password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
	}
}
