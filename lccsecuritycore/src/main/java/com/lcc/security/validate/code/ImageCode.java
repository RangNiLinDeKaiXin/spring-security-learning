package com.lcc.security.validate.code;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @author: lcc
 * @Date: 2018-05-22
 **/
@Data
@AllArgsConstructor
public class ImageCode {
	private BufferedImage image;
	private String code;
	private LocalDateTime expireTime;

	public ImageCode(BufferedImage image, String code, int expireIn) {
		this.image = image;
		this.code = code;
		this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
	}
	public boolean isExpire(){
		if(LocalDateTime.now().compareTo(expireTime)>0) {
			return true;
		}
		return false;
	}
}
