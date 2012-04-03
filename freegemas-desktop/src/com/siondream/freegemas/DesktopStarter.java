package com.siondream.freegemas;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopStarter {
	public static void main (String[] args) {
		new LwjglApplication(new Freegemas(),
							 "Freegemas",
							 1280,
							 720,
							 false);
	}
}
