package com.siondream.freegemas.client;

import com.siondream.freegemas.PlatformResolver;

public class WebGLResolver implements PlatformResolver {

	@Override
	public String getDefaultLanguage() {
		return "en_UK";
	}
	
	public String format(String string, Object... args) {
		return "";
	}
}
