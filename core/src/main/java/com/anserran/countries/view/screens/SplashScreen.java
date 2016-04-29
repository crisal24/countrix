package com.anserran.countries.view.screens;

import com.anserran.countries.C;
import com.anserran.countries.control.Controller;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import es.eucm.i18n.I18N;

public class SplashScreen extends Table {

	private Controller controller;

	private Label maxScore;

	public SplashScreen(final Controller c) {
		this.controller = c;
		setFillParent(true);
		I18N i18N = c.getAssets().getI18N();
		Skin skin = c.getAssets().getSkin();
		pad(c.getAssets().dpToPx(48));

		add().expandY();
		row();
		add(new Label(i18N.m("title"), skin, C.STYLE_TITLE)).center();
		row();
		add(
				maxScore = new Label(i18N.m("max_score", c.getPreferences()
						.getInteger("max_score", 0)), skin, C.STYLE_CORRECT))
				.center();
		add().expandY();
		row();
		add(new TextButton(i18N.m("start").toUpperCase(), skin)).expandX()
				.fill().getActor().addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						c.start();
					}
				});
		row();
		final CheckBox checkBox = new CheckBox("See xAPI statements", skin);
		checkBox.getImageCell().pad(c.getAssets().dpToPx(8));
		checkBox.setChecked(c.getPreferences().getBoolean("xapi", false));

		checkBox.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boolean checked = checkBox.isChecked();
				c.getPreferences().putBoolean("xapi", checked);
			}
		});

		add(checkBox).pad(c.getAssets().dpToPx(8));
	}

	@Override
	protected void setParent(Group parent) {
		super.setParent(parent);
		maxScore.setText(controller
				.getAssets()
				.getI18N()
				.m("max_score",
						controller.getPreferences().getInteger("max_score", 0)));
	}
}
