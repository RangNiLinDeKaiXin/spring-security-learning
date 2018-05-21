package com.lcc.brower.authentication;

import com.lcc.security.properties.LoginType;
import com.lcc.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义用户登录成功处理
 *
 * @author: lcc
 * @Date: 2018-05-21
 **/
@Slf4j
@Component("lccAuthenticationSuccessHandler")
//implements AuthenticationSuccessHandler
public class LccAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
		log.info("登录成功");
		if (LoginType.JSON.equals(securityProperties.getBrowserProperties().getLoginType())) {
			httpServletResponse.setContentType("application/json;charset=UTF-8");
			ObjectMapper objectMapper = new ObjectMapper();
			httpServletResponse.getWriter().write(objectMapper.writeValueAsString(authentication));

		} else {
			super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
		}

	}
}
