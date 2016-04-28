package com.anserran.countries;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class CountriesHtml extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig() {
		GwtApplicationConfiguration conf =  new GwtApplicationConfiguration(800, 700);
		return conf;
	}

	@Override
	public ApplicationListener createApplicationListener() {
		return new Countries();
	}
}