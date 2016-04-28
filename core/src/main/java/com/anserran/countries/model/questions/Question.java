package com.anserran.countries.model.questions;

import com.anserran.countries.model.concepts.Concept;

public class Question {

	private Concept concept;

	private int type;

	private int options;

	private int correctOption;

	public Question(Concept concept, int type, int options, int correctOption) {
		this.concept = concept;
		this.type = type;
		this.options = options;
		this.correctOption = correctOption;
	}

	public Concept getConcept() {
		return concept;
	}

	public int getOptions() {
		return options;
	}

	public int getCorrectOption() {
		return correctOption;
	}

	public int getType() {
		return type;
	}

	public boolean answer(int option) {
		boolean correct = option == correctOption;
		concept.answer(type, correct);
		return correct;
	}
}
