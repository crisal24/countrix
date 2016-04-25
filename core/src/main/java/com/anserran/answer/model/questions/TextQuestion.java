package com.anserran.answer.model.questions;

import com.anserran.answer.model.concepts.Concept;

public class TextQuestion extends Question {

	private String questionKey;

	private String questionParameter;

	private String[] answers;

	public TextQuestion(Concept concept, int type, int correctOption,
			String questionKey, String questionParameter, String... answers) {
		super(concept, type, answers.length, correctOption);
		this.questionKey = questionKey;
		this.questionParameter = questionParameter;
		this.answers = answers;
	}

	public String getQuestionParameter() {
		return questionParameter;
	}

	public String getQuestionKey() {
		return questionKey;
	}

	public String[] getAnswers() {
		return answers;
	}
}
