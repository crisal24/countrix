package com.anserran.answer.players;


import com.anserran.answer.model.questions.Question;

public class PerfectPlayer extends Player {

	public float error;

	public PerfectPlayer(float error) {
		this.error = error;
	}

	@Override
	public int choose(Question question) {
		if (Math.random() > error) {
			if (question.getCorrectOption() == 0) {
				return 1;
			} else {
				return question.getCorrectOption() - 1;
			}
		} else {
			return question.getCorrectOption();
		}
	}
}
