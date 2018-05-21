package com.lcc.brower.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcc.security.properties.LoginType;
import com.lcc.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 *
 * @author: lcc
 * @Date: 2018-05-21
 **/
@Slf4j
@Component("lccAuthenticationFailureHandler")
// implements AuthenticationFailureHandler

//SimpleUrlAuthenticationFailureHandler 默认处理器
public class LccAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException ex) throws IOException, ServletException {
		log.info("登录失败");
		if (LoginType.JSON.equals(securityProperties.getBrowserProperties().getLoginType())) {
			httpServletResponse.setContentType("application/json;charset=UTF-8");
			ObjectMapper objectMapper = new ObjectMapper();
			httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			httpServletResponse.getWriter().write(objectMapper.writeValueAsString(ex.getMessage()));

		} else {
			super.onAuthenticationFailure(httpServletRequest, httpServletResponse, ex);
		}

	}
}
