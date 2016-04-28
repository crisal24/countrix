package com.anserran.countries.model.questions;

import com.anserran.countries.model.concepts.Concept;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ImageQuestion extends TextQuestion {

	private TextureAtlas atlas;

	public ImageQuestion(Concept concept, int type, int correctOption,
						 String imageId, TextureAtlas atlas, String... answers) {
		super(concept, type, correctOption, imageId, null, answers);
		this.atlas = atlas;
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}
}
