package com.acit.dc.util;

import java.util.Properties;

public class ImageType {

	private static Properties imageType = new Properties();
	static {
		imageType.put("image/bmp", "bmp");
		imageType.put("image/gif", "gif");
		imageType.put("image/jpeg", "jpeg");
		imageType.put("image/jpg", "jpg");
		imageType.put("image/png", "png");
		imageType.put("image/tif", "tiff");
		imageType.put("image/tiff", "tiff");
	}

	public static String getImageType(String imgType) {
		return imageType.getProperty(imgType);
	}
}
