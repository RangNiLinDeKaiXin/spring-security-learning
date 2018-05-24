package com.lcc.security.validate.code.image;

import com.lcc.security.validate.code.base.BasicCode;
import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * @author: lcc
 * @Date: 2018-05-22
 **/
@Data
public class ImageCode extends BasicCode {
	private BufferedImage image;

	public ImageCode(BufferedImage image, String code, int expireInt) {
		super(code, expireInt);
		this.image = image;
	}

}
