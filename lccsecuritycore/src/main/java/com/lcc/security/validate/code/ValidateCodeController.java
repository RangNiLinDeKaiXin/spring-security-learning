package com.lcc.security.validate.code;


import com.lcc.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: lcc
 * @Date: 2018-05-22
 **/
@RestController
@RequestMapping("/code")
public class ValidateCodeController {
	/**
	 * 验证码的key
	 */
	public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";
	@Autowired
	private SecurityProperties securityProperties;

	@Qualifier("imageCodeGenerator")
    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    @Qualifier("smsCodeGenerator")
    @Autowired
    private ValidateCodeGenerator smsCodeGenerator;
	@Autowired
	private SmsCodeSender smsCodeSender;

	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();


	/**
	 * 创建验证码
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/image")
	public void getImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ImageCode imageCode = (ImageCode) imageCodeGenerator.generate(new ServletWebRequest(request));
		// 将图形验证码放入session中
		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
		ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
	}

	/**
	 * 创建验证码
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/sms")
	public void getSmsCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BasicCode smsCode = smsCodeGenerator.generate(new ServletWebRequest(request));
		//这里放到session ，可以选择放入数据库 或者redis
		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, smsCode);

		//从请求中获取手机号
		try {
			String mobile = ServletRequestUtils.getRequiredStringParameter(request, "mobile");
			smsCodeSender.send(mobile, smsCode.getCode());

		} catch (ServletRequestBindingException e) {
			throw new ValidateCodeException("手机号获取失败");
		}

	}

}
