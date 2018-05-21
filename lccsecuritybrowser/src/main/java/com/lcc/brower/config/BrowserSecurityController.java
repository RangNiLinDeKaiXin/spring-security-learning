package com.lcc.brower.config;

import com.lcc.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: lcc
 * @Date: 2018-05-21
 **/
@Slf4j
@RestController
public class BrowserSecurityController {
	@Autowired
	private SecurityProperties securityProperties;
	//spring 把请求的url 缓存到session里
	private RequestCache requestCache = new HttpSessionRequestCache();

	//spring  跳转url
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	/**
	 * 当需要身份认证时 跳转到这
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/authentication/require")
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public SimpleRespone requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//获取请求url
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest != null) {
			String url = savedRequest.getRedirectUrl();
			log.info("引发跳转url：{}", url);
			if (StringUtils.endsWithIgnoreCase(url, "html")) {
				redirectStrategy.sendRedirect(request, response, securityProperties.getBrowserProperties().getLoginPage());
			}

		}
		return new SimpleRespone("访问的服务需要身份认证，请引导用户到登录页");
	}
}
