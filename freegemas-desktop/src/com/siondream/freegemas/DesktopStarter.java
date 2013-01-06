package com.siondream.freegemas;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopStarter {
	public static void main (String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Freegemas";
		cfg.useGL20 = true;
		cfg.width = 1280;
		cfg.height = 720;
		
		Freegemas.setPlatformResolver(new DesktopResolver());
		
		new LwjglApplication(new Freegemas(), cfg);
	}
}
