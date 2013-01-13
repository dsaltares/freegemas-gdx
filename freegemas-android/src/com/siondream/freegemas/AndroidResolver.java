package com.siondream.freegemas;

public class AndroidResolver implements PlatformResolver {
	
	public String getDefaultLanguage() {
		return java.util.Locale.getDefault().toString();
	}
	
	public String format(String string, Object... args) {
		return String.format(string, args);
	}
}
