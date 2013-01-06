package com.siondream.freegemas;
import com.siondream.freegemas.PlatformResolver;


public class DesktopResolver implements PlatformResolver {

	@Override
	public String getDefaultLanguage() {
		return java.util.Locale.getDefault().toString();
	}
}
