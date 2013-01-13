package com.siondream.freegemas.client;

import com.siondream.freegemas.Freegemas;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(960, 540);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new Freegemas();
	}
}