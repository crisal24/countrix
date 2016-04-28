package com.anserran.countries.view.screens;

import com.anserran.countries.control.Controller;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LoadScreen extends Table {

	public LoadScreen(Controller c) {
		setFillParent(true);
		add(
				new Label(c.getAssets().getI18N().m("loading"), c.getAssets()
						.getSkin())).center();
	}
}
