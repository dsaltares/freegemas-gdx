package com.siondream.freegemas;

public class DesktopResolver implements PlatformResolver {

	@Override
	public String getDefaultLanguage() {
		return java.util.Locale.getDefault().toString();
	}
	
	public String format(String string, Object... args) {
		return String.format(string, args);
	}
}
