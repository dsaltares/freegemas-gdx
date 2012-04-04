package com.siondream.freegemas;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class DesktopStarter {
	public static void main (String[] args) {
		new JoglApplication(new Freegemas(),
							"Freegemas",
							1280,
							720,
							true);
	}
}
