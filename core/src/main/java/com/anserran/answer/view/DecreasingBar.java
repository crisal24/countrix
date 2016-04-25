package com.anserran.answer.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class DecreasingBar extends Widget {

	private float value;

	private float prevValue = 0;

	private float delay;

	private DecreasingBarStyle style;

	public DecreasingBar(DecreasingBarStyle style) {
		this.style = style;
	}

	public DecreasingBar(Skin skin) {
		this(skin.get(DecreasingBarStyle.class));
	}

	public DecreasingBar(Skin skin, String style) {
		this(skin.get(style, DecreasingBarStyle.class));
	}

	public void setValue(float value) {
		this.value = value;
		if (this.prevValue < value) {
			this.prevValue = value;
			delay = style.delay;
		}
	}

	@Override
	public void act(float delta) {
		if (delay >= 0) {
			delay -= delta;
		}
		if (delay <= 0 && prevValue > value) {
			prevValue -= delta / style.delayFactor;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		drawBar(batch, parentAlpha, style.backgroundColor, getWidth());
		drawBar(batch, parentAlpha, style.secondaryColor, getWidth()
				* prevValue);
		drawBar(batch, parentAlpha, style.primaryColor, getWidth() * value);
	}

	private void drawBar(Batch batch, float parentAlpha, Color color,
			float width) {
		batch.setColor(color);
		batch.getColor().a *= parentAlpha;
		style.drawable.draw(batch, getX(), getY(), width, getHeight());
	}

	public static class DecreasingBarStyle {

		public Drawable drawable;

		public Color primaryColor;

		public Color secondaryColor;

		public Color backgroundColor;

		public float delay = 0.0f;

		public float delayFactor = 1.0f;
	}
}
