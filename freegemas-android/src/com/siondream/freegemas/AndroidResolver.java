package com.siondream.freegemas;
import com.siondream.freegemas.PlatformResolver;


public class AndroidResolver implements PlatformResolver {

	public String getDefaultLanguage() {
		return java.util.Locale.getDefault().toString();
	}
}
