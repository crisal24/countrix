package com.anserran.answer.players;

import java.util.HashMap;
import java.util.Random;

import com.anserran.answer.model.questions.Question;


public class MemoryPlayer extends Player {

	private HashMap<String, Integer> memoryMap = new HashMap<String, Integer>();

	private float memory;

	private Random random = new Random();

	public MemoryPlayer(float memory) {
		this.memory = memory;
	}

	@Override
	public int choose(Question question) {
		if (memoryMap.containsKey(question.getConcept().getId()
				+ question.getType())
				&& Math.random() < memory) {
			return question.getCorrectOption();
		} else {
			memoryMap.put(question.getConcept().getId() + question.getType(),
					question.getCorrectOption());
			return random.nextInt(question.getOptions());
		}
	}
}
