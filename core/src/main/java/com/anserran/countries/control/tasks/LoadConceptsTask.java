package com.anserran.countries.control.tasks;

import java.util.HashMap;
import java.util.Map;

import com.anserran.countries.C;
import com.anserran.countries.control.GeographyController;
import com.anserran.countries.model.concepts.Concept;
import com.anserran.countries.model.concepts.GeographyConcept;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.async.AsyncTask;

public class LoadConceptsTask implements
		AsyncTask<Map<Integer, Array<Concept>>> {

	private GeographyController controller = new GeographyController();

	private Preferences preferences;

	public LoadConceptsTask(Preferences preferences) {
		this.preferences = preferences;
	}

	public void setTextureAtlas(TextureAtlas atlas) {
		controller.setTextureAtlas(atlas);
	}

	@Override
	public Map<Integer, Array<Concept>> call() throws Exception {

		String[] lines = Gdx.files.internal("data/geography.csv").readString()
				.split("\n");

		Map<Integer, Array<Concept>> map = new HashMap<Integer, Array<Concept>>();

		int j = 0;
		for (String line : lines) {
			int level = (int) ((j / (lines.length / (float) C.GEOGRAPHY_LEVELS)) + 1);
			String[] split = line.split(",");
			String countryId = split[0];
			String countryName = split[1];
			String continent = split[2];
			String capital = split[3];
			String city1 = split[4];
			String city2 = split[5];
			Array<String> cities = new Array<String>();
			for (int i = 6; i < split.length; i++) {
				cities.add(split[i]);
			}
			controller.addCountry(countryName, continent);
			GeographyConcept concept = new GeographyConcept(controller,
					countryId, level, countryName, continent, capital, city1,
					city2);

			String data = preferences.getString(concept.getId(), "");
			if (!data.isEmpty()) {
				concept.deserialize(data);
			}

			Array<Concept> concepts = map.get(level);
			if (concepts == null) {
				concepts = new Array<Concept>();
				map.put(level, concepts);
			}
			concepts.add(concept);

			j++;
		}

		for (Array<Concept> concepts : map.values()) {
			concepts.shuffle();
		}

		return map;
	}
}
