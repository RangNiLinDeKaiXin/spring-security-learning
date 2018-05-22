package com.lcc.security.validate.code;


import com.lcc.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
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
    @Autowired
	private ValidateCodeGenerator imageCodeGenerator;

	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();


	/**
	 * 创建验证码
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/image")
	public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ImageCode imageCode = imageCodeGenerator.generate(new ServletWebRequest(request));
		// 将图形验证码放入session中
		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
		ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
	}


}
