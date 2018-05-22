package com.lcc.security.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
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

/**
 * @author: lcc
 * @Date: 2018-05-22
 **/
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {
    @Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	private final SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();



	public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		if (StringUtils.pathEquals("/authentication/form", httpServletRequest.getRequestURI())) {
			try {
				validate(new ServletWebRequest(httpServletRequest));

			} catch (ValidateCodeException e) {
				authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
				return;
			}

		}
		filterChain.doFilter(httpServletRequest, httpServletResponse);

	}

	private void validate(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {
		ImageCode imageCode = (ImageCode) sessionStrategy.getAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
		String code = ServletRequestUtils.getRequiredStringParameter(servletWebRequest.getRequest(), "imageCode");
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
