package com.lcc.web.code;

import com.lcc.security.validate.code.ImageCode;
import com.lcc.security.validate.code.ValidateCodeGenerator;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 自定义验证码生成器
 */


//@Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {

    /**
     * 新的验证码生成逻辑
     * @param request
     * @return
     */
    @Override
    public ImageCode generate(ServletWebRequest request) {
        System.out.println("更高级的图形验证码生成代码");
        return null;
    }
}
