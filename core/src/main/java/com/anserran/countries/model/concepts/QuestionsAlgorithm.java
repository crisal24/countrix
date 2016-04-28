package com.anserran.countries.model.concepts;

import com.anserran.countries.C;
import com.anserran.countries.model.concepts.Concept.State;
import com.anserran.countries.model.questions.Question;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;
import java.util.Map;
import java.util.Random;

public class QuestionsAlgorithm {

	private Random random = new Random();

	private Comparator<Concept> comparator;

	private Array<Integer> offsets = new Array<Integer>();

	public QuestionsAlgorithm(Comparator<Concept> comparator) {
		this.comparator = comparator;
	}

	public Array<Question> generate(int count, int currentLevel, int maxLevel,
			Map<Integer, Array<Concept>> concepts) {
		Array<Question> questions = new Array<Question>();
		sortConcepts(concepts);
		offsets.clear();
		for (int i = 0; i < concepts.size(); i++) {
			offsets.add(i);
		}

		for (int i = 0; i < count; i++) {
			int level;
			switch (i % C.QUESTIONS_CYCLE) {
			case 0:
			case 3:
				level = currentLevel == 1 ? 1 : random
						.nextInt(currentLevel - 1) + 1;
				break;
			case 2:
			case 5:
				level = maxLevel == currentLevel ? maxLevel : random
						.nextInt(maxLevel - currentLevel + 1) + currentLevel;
				break;
			default:
				level = currentLevel;
				break;
			}
			int offset = offsets.removeIndex(level - 1);
			questions.add(concepts.get(level).get(offset).generateQuestion());
			offset++;
			offsets.insert(level - 1, offset);
		}
		return questions;
	}

	private void sortConcepts(Map<Integer, Array<Concept>> concepts) {
		for (Array<Concept> list : concepts.values()) {
			list.sort(comparator);
		}
	}

	public static class NaiveComparator implements Comparator<Concept> {

		@Override
		public int compare(Concept c1, Concept c2) {
			if (c1.getQueued() > c2.getQueued()) {
				return 1;
			} else if (c1.getQueued() < c2.getQueued()) {
				return -1;
			} else {
				return compareScore(c1, c2);
			}
		}

		protected int compareScore(Concept c1, Concept c2) {
			return c1.getPresented() - c2.getPresented();
		}
	}

	public static class InformedComparator extends NaiveComparator {

		@Override
		public int compareScore(Concept c1, Concept c2) {
			int result;
			State state1 = c1.getState();
			State state2 = c2.getState();
			int presented1 = c1.getPresented();
			int presented2 = c2.getPresented();
			if (state1 == state2) {
				if (state1 == State.NOT_PRESENTED || state2 == State.LEARNING) {
					result = presented1 - presented2;
				} else {
					result = (int) Math.signum(c1.getScore() - c2.getScore());
				}
			} else {
				result = state1.ordinal() - state2.ordinal();
			}
			return result;
		}
	}
}
