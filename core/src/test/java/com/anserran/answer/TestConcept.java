package com.anserran.answer;

import com.anserran.countries.model.concepts.Concept;
import com.anserran.countries.model.questions.Question;

import java.util.Random;


public class TestConcept extends Concept {

	public static int OPTIONS = 3;

	public static int QUESTIONES_TYPE = 4;

	private Random random = new Random();

	public TestConcept(String id, int level) {
		super(id, level);
	}

	@Override
	public Question buildQuestion(int type) {
		return new Question(this, type, OPTIONS, random.nextInt(OPTIONS));
	}

	@Override
	public int getQuestionsType() {
		return QUESTIONES_TYPE;
	}
}
