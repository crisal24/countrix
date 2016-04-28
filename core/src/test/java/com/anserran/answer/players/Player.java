package com.anserran.answer.players;

import com.anserran.countries.model.questions.Question;

import java.util.Random;


public class Player {

	private Random random = new Random();

	public int choose(Question question) {
		return random.nextInt(question.getOptions());
	}
}
