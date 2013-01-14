package com.siondream.freegemas.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.siondream.freegemas.PlatformResolver;

public class WebGLResolver implements PlatformResolver {

	@Override
	public String getDefaultLanguage() {
		return "en_UK";
	}
	
	public String format(String string, Object... args) {
		return "";
	}

	@Override
	public BitmapFont loadFont(String fntFile, String ttfFile, int size) {
		return new BitmapFont(Gdx.files.internal(fntFile), true);
	}
}
