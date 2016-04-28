package com.anserran.countries.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import es.eucm.i18n.I18N;

public class Assets extends AssetManager {

	private Skin skin;

	private I18N i18N;

	private float dpPx;

	public Assets() {
		float ppcy = Gdx.graphics.getPpcY();

		float idealScale = ppcy / 48.0f;
		String scale;
		if (idealScale <= 0.5f) {
			scale = "0.5";
		} else if (idealScale <= 1.0f) {
			scale = "1.0";
		} else if (idealScale <= 2.0f) {
			scale = "2.0";
		} else {
			scale = "3.0";
		}

		i18N = new I18N();
		i18N.addMessages(Gdx.files.internal("i18n/en_US.properties")
				.readString());
		load("skin/" + scale + "/skin.json", Skin.class);
		finishLoading();
		skin = get("skin/" + scale + "/skin.json");
		dpPx = skin.getDrawable("blank").getMinWidth();
	}

	public boolean isDone() {
		return getQueuedAssets() == 0;
	}

	public Skin getSkin() {
		return skin;
	}

	public I18N getI18N() {
		return i18N;
	}

	public float dpToPx(float dp) {
		return (dp / 48.0f) * dpPx;
	}

}
