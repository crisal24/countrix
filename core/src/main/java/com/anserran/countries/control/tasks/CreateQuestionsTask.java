package com.anserran.countries.control.tasks;

import java.util.Map;

import com.anserran.countries.C;
import com.anserran.countries.model.concepts.Concept;
import com.anserran.countries.model.concepts.QuestionsAlgorithm;
import com.anserran.countries.model.concepts.QuestionsAlgorithm.NaiveComparator;
import com.anserran.countries.model.questions.Question;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.async.AsyncTask;

public class CreateQuestionsTask implements AsyncTask<Array<Question>> {

	private Map<Integer, Array<Concept>> concepts;

	private QuestionsAlgorithm algorithm;

	private int level;

	private int maxLevel;

	public CreateQuestionsTask(Map<Integer, Array<Concept>> concepts) {
		this.concepts = concepts;
		algorithm = new QuestionsAlgorithm(new NaiveComparator());
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	@Override
	public Array<Question> call() throws Exception {
		return algorithm.generate(C.QUESTIONS_CYCLE, level, maxLevel, concepts);
	}
}
