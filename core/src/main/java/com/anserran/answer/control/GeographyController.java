package com.anserran.answer.control;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

public class GeographyController {

	private Array<String> continents;

	private Map<String, Array<String>> countriesByContinent;

	private String[] randomContinents = new String[2];

	private String[] randomCountries = new String[2];

	private TextureAtlas textureAtlas;

	public GeographyController() {
		continents = new Array<String>(new String[] { "Europe", "America",
				"Asia", "Oceania", "Africa" });
		countriesByContinent = new HashMap<String, Array<String>>();
	}

	public void addCountry(String country, String continent) {
		Array<String> countries = countriesByContinent.get(continent);
		if (countries == null) {
			countries = new Array<String>();
			countriesByContinent.put(continent, countries);
		}

		if (!countries.contains(country, false)) {
			countries.add(country);
		}
	}

	public String[] getRandomContinents(String continent) {
		continents.shuffle();
		int index = 0;
		int j = 0;
		while (j < 2) {
			while (continent.equals(continents.get(index))) {
				index++;
			}
			randomContinents[j] = continents.get(index);
			index++;
			j++;
		}
		return randomContinents;
	}

	public String[] getRandomCountries(String continent, String country) {

		if (continent.contains("Eur")) {
			continent = "Europe";
		}

		Array<String> countries = countriesByContinent.get(continent);
		countries.shuffle();
		int index = 0;
		int j = 0;
		while (j < 2) {
			while (country.equals(countries.get(index))) {
				index++;
			}
			randomCountries[j] = countries.get(index);
			index++;
			j++;
		}
		return randomCountries;
	}

	public void setTextureAtlas(TextureAtlas textureAtlas) {
		this.textureAtlas = textureAtlas;
	}

	public TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}
}
