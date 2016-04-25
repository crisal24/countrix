package com.anserran.answer.model.concepts;

import com.anserran.answer.control.GeographyController;
import com.anserran.answer.model.questions.ImageQuestion;
import com.anserran.answer.model.questions.Question;
import com.anserran.answer.model.questions.TextQuestion;

public class GeographyConcept extends Concept {

	private GeographyController controller;

	public static final int CAPITAL_OF = 0, CONTINENT = 1, IS_CAPITAL = 2,
			FLAG = 3;

	private String country;

	private String continent;

	private String capital;

	private String city1;

	private String city2;

	public GeographyConcept(GeographyController controller, String id,
			int level, String country, String continent, String capital,
			String city1, String city2) {
		super(id, level);
		this.controller = controller;
		this.country = country;
		this.continent = continent;
		this.capital = capital;
		this.city1 = city1;
		this.city2 = city2;
	}

	@Override
	protected Question buildQuestion(int type) {
		if (getId().equals("Turkey") && type == CONTINENT) {
			type = CAPITAL_OF;
		}
		switch (type) {
		case CAPITAL_OF:
			return new TextQuestion(this, 0, 0, "capital_of", country, capital,
					city1, city2);
		case CONTINENT:
			String[] continents = controller.getRandomContinents(continent);
			return new TextQuestion(this, 0, 0, "continent_of", country,
					continent, continents[0], continents[1]);
		case IS_CAPITAL:
			String[] countries = controller.getRandomCountries(continent,
					country);
			return new TextQuestion(this, 0, 0, "is_capital", capital, country,
					countries[0], countries[1]);
		case FLAG:
			countries = controller.getRandomCountries(continent, country);
			return new ImageQuestion(this, 0, 0, getId(),
					controller.getTextureAtlas(), country, countries[0],
					countries[1]);
		}
		return null;
	}

	@Override
	public int getQuestionsType() {
		return 4;
	}
}
