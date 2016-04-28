package com.anserran.countries;

public interface C {

	String STYLE_TITLE = "title";
	String STYLE_QUESTION = "question";
	String STYLE_TIME = "time";
	String STYLE_CORRECT = "correct";
	String STYLE_INCORRECT = "incorrect";

	int MAX_OPTIONS = 3;

	/**
	 * Number of times a concept must be presented to consider that it has been
	 * fully presented
	 */
	int REPEAT_CONCEPT = 2;

	int QUESTIONS_CYCLE = 6;

	int GEOGRAPHY_LEVELS = 9;

	float ROUND_TIME = 30.0f;

	float ERROR_TIME = 5.0f;

	float FEEDBACK_TIME = 0.5f;
}
