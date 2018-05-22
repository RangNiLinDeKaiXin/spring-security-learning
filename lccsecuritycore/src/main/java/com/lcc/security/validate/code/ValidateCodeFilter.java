package com.lcc.security.validate.code;

import com.lcc.security.properties.SecurityProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: lcc
 * @Date: 2018-05-22
 **/

public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
	/**
	 * 验证码校验失败处理器
	 */
	private AuthenticationFailureHandler authenticationFailureHandler;
	/**
	 * 系统配置信息
	 */
	private SecurityProperties securityProperties;
	/**
	 * 验证请求url与配置的url是否匹配的工具类
	 */
	private AntPathMatcher antPathMatcher = new AntPathMatcher();
	private final SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

	private Set<String> urls = new HashSet<>();

	public AuthenticationFailureHandler getAuthenticationFailureHandler() {
		return authenticationFailureHandler;
	}

	public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

	/**
	 * 初始化要拦截的url配置信息
	 */
	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		if (StringUtils.hasText(securityProperties.getValidateCodeProperties().getImageCode().getUrl())) {
			String[] configUrls = org.apache.commons.lang.StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getValidateCodeProperties().getImageCode().getUrl(), ",");
			for (String configUrl : configUrls) {
				urls.add(configUrl);
			}
		}
		urls.add("/authentication/form");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		boolean action = false;
		for (String url : urls) {
			if (antPathMatcher.match(url, httpServletRequest.getRequestURI())) {
				action = true;
			}
		}
		if (action) {
			try {
				validate(new ServletWebRequest(httpServletRequest));

			} catch (ValidateCodeException e) {
				authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
				return;
			}

		}
		filterChain.doFilter(httpServletRequest, httpServletResponse);

	}

	private void validate(ServletWebRequest servletWebRequest) {
		ImageCode imageCode = null;
		String code = null;
		try {
			imageCode = (ImageCode) sessionStrategy.getAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
			code = ServletRequestUtils.getRequiredStringParameter(servletWebRequest.getRequest(), "imageCode");
		} catch (ServletRequestBindingException e) {
			throw new ValidateCodeException("验证码不存在");
		}
		if (!StringUtils.hasText(code)) {
			throw new ValidateCodeException("验证码不能为空");
		}

		if (imageCode == null) {
			throw new ValidateCodeException("验证码不存在");
		}

		if (imageCode.isExpire()) {
			sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
			throw new ValidateCodeException("验证码已过期");
		}
		if (!imageCode.getCode().equals(code)) {
			throw new ValidateCodeException("验证码不正确");
		}
		sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);

	}
}
