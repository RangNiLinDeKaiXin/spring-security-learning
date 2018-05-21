package com.lcc.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: lcc
 * @Date: 2018-05-21
 **/
@Data
@ConfigurationProperties(prefix = "lcc.security")
public class SecurityProperties {
 private BrowserProperties browserProperties =new BrowserProperties();
}
