package com.anserran.answer;

import com.anserran.answer.control.Controller;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Countries implements ApplicationListener {

	private boolean debug;

	private boolean drawDebug = false;

	private Controller controller;

	private Stage stage;

	public Countries() {
		this(false);
	}

	public Countries(boolean debug) {
		this.debug = debug;
	}

	@Override
	public void create() {
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		if (debug) {
			stage.addListener(new InputListener() {
				@Override
				public boolean keyDown(InputEvent event, int keycode) {
					switch (keycode) {
					case Keys.D:
						drawDebug = !drawDebug;
						stage.setDebugAll(drawDebug);
						break;

					}
					return super.keyDown(event, keycode);
				}
			});
		}
		controller = new Controller(stage.getRoot());
		controller.splash();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render() {
		controller.act(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		controller.dispose();
		stage.dispose();
	}
}
