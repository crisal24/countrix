package com.anserran.answer.desktop;

import com.anserran.countries.Countries;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 450;
		config.height = 800;
		new LwjglApplication(new Countries(arg.length > 0 && "debug".equals(arg[0])), config);
	}
}
