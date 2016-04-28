package com.anserran.answer;

import com.anserran.countries.C;
import com.anserran.countries.model.concepts.Concept;
import com.anserran.countries.model.concepts.Concept.State;
import com.anserran.countries.model.concepts.QuestionsAlgorithm;
import com.anserran.countries.model.concepts.QuestionsAlgorithm.InformedComparator;
import com.anserran.countries.model.concepts.QuestionsAlgorithm.NaiveComparator;
import com.anserran.countries.model.questions.Question;
import com.anserran.answer.players.MemoryPlayer;
import com.anserran.answer.players.Player;
import com.badlogic.gdx.utils.Array;
import org.junit.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConceptTest {

	static int N_CONCEPTS = 25;
	static int N_LEVELS = 5;

	@Test
	public void test() {
		Array<Float> diffs = new Array<Float>();
		for (int i = 0; i < 100; i++) {
			float learned1 = testGame(new MemoryPlayer(0.f),
					new NaiveComparator(), "Memory 75% - Naive");
			float learned2 = testGame(new MemoryPlayer(0.f),
					new InformedComparator(), "Memory 75% - Informed");
			diffs.add(learned2 - learned1);
		}
		float mean = 0;
		for (Float f : diffs) {
			mean += f;
		}
		mean /= diffs.size;
		System.out.println(mean * 100 + "%");
		assertTrue(mean > 0.0f);
	}

	public float testGame(Player player, Comparator<Concept> comparator,
						  String label) {
		QuestionsAlgorithm naive = new QuestionsAlgorithm(comparator);
		int maxLevel = N_LEVELS;

		Map<Integer, Array<Concept>> conceptsMap = new HashMap<Integer, Array<Concept>>();
		int k = 0;
		for (int i = 1; i <= maxLevel; i++) {
			Array<Concept> concepts = new Array<Concept>();
			conceptsMap.put(i, concepts);
			for (int j = 0; j < N_CONCEPTS; j++) {
				concepts.add(new TestConcept(k++ + "", i));
			}
		}

		int currentLevel = 1;
		int xp = 0;
		int levelBarrier = N_CONCEPTS * TestConcept.QUESTIONES_TYPE
				* C.REPEAT_CONCEPT;

		int questionsToAnswer = Math.round(levelBarrier * N_LEVELS);

		while (questionsToAnswer > 0) {
			Array<Question> questions = naive.generate(C.QUESTIONS_CYCLE,
					currentLevel, maxLevel, conceptsMap);
			for (Question question : questions) {
				questionsToAnswer--;
				int option = player.choose(question);
				if (question.answer(option)) {
					xp++;
					currentLevel = Math.min(maxLevel,
							(int) (Math.floor((float) xp / levelBarrier) + 1));
				}
			}
		}

		int conceptsMastered = 0;
		int conceptsOK = 0;
		int conceptsNotLearned = 0;
		int conceptsNotPresented = 0;

		for (Array<Concept> list : conceptsMap.values()) {
			for (Concept concept : list) {
				if (concept.getState() == State.NOT_PRESENTED
						|| concept.getState() == State.LEARNING) {
					conceptsNotPresented++;
				} else if (concept.getScore() > 0.8f) {
					conceptsMastered++;
				} else if (concept.getScore() > 0.5f) {
					conceptsOK++;
				} else {
					conceptsNotLearned++;
				}
			}
		}

		float totalConcepts = N_CONCEPTS * N_LEVELS;
		return (conceptsMastered + conceptsOK) / totalConcepts;
	}

	@Test
	public void testSerialize() {
		TestConcept concept = new TestConcept("id", 1);
		for (int i = 0; i < 100; i++) {
			concept.answer(0, i % 3 == 0);
		}

		String data = concept.serialize();

		TestConcept concept1 = new TestConcept("id2", 1);
		concept1.deserialize(data);

		assertEquals(data, concept1.serialize());
		assertEquals(concept.getPresented(), concept1.getPresented());
		assertEquals(concept.getPresented(), 100);
		assertEquals(concept.getCorrect(), 34);
		assertEquals(concept.getIncorrect(), 66);
	}

}
