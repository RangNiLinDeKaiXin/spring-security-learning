package com.lcc.security.validate.code;

import org.springframework.web.context.request.ServletWebRequest;


/**
 * 验证码接口
 * @author: lcc
 * @Date: 2018-05-22
 **/
public interface ValidateCodeGenerator {
    /**
     * 图形验证码实现方法接口
     * @param request
     * @return
     */
    ImageCode generate(ServletWebRequest request);

}