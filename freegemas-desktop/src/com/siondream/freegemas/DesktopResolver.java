package com.siondream.freegemas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class DesktopResolver implements PlatformResolver {

	@Override
	public String getDefaultLanguage() {
		return java.util.Locale.getDefault().toString();
	}
	
	public String format(String string, Object... args) {
		return String.format(string, args);
	}
	
	@Override
	public BitmapFont loadFont(String fntFile, String ttfFile, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(ttfFile));
		BitmapFont font = generator.generateFont(size, FreeTypeFontGenerator.DEFAULT_CHARS, true);
		generator.dispose();
		return font;
	}
}
