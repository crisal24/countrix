package com.anserran.answer.model.concepts;

import com.anserran.answer.C;
import com.anserran.answer.model.questions.Question;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Concept {

	public enum State {
		NOT_PRESENTED, LEARNING, PRESENTED
	}

	private String id;

	private int level;

	private int presented;

	private int queued = 0;

	private int correct;

	private int incorrect;

	private Array<Boolean> lastTries;

	private Map<Integer, Integer> presentedTypes;

	private Map<Integer, Integer> correctTypes;

	private Map<Integer, Integer> incorrectTypes;

	private State state = State.NOT_PRESENTED;

	/**
	 * Score for the concept, from 0.0 to 1.0
	 */
	private float score;

	public Concept(String id, int level) {
		this.id = id;
		this.level = level;
		lastTries = new Array<Boolean>();
		correctTypes = new HashMap<Integer, Integer>();
		incorrectTypes = new HashMap<Integer, Integer>();
		presentedTypes = new HashMap<Integer, Integer>();
		for (int i = 0; i < getQuestionsType(); i++) {
			correctTypes.put(i, 0);
			incorrectTypes.put(i, 0);
			presentedTypes.put(i, 0);
		}
	}

	public String getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public State getState() {
		return state;
	}

	public int getQueued() {
		return queued;
	}

	public float getScore() {
		return score;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPresented() {
		return presented;
	}

	public int getCorrect() {
		return correct;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}

	public int getIncorrect() {
		return incorrect;
	}

	public void setIncorrect(int incorrect) {
		this.incorrect = incorrect;
	}

	public Question generateQuestion() {
		queued++;
		int type = 0;
		int count = 0;
		for (Entry<Integer, Integer> entry : presentedTypes.entrySet()) {
			if (entry.getValue() < count) {
				type = entry.getKey();
			} else if (entry.getValue() == count) {
				type = Math.random() > 0.5f ? type : entry.getKey();
			}
		}
		return buildQuestion(type);
	}

	protected abstract Question buildQuestion(int type);

	public abstract int getQuestionsType();

	private void updateState() {
		state = calculateSate(presented);
	}

	private State calculateSate(int presented) {
		if (presented == 0) {
			return State.NOT_PRESENTED;
		} else if (presented < getQuestionsType() * C.REPEAT_CONCEPT) {
			return State.LEARNING;
		} else {
			return State.PRESENTED;
		}
	}

	private void updateScore() {
		int count = 0;
		for (Boolean b : lastTries) {
			if (b) {
				count++;
			}
		}
		score = count / ((float) C.REPEAT_CONCEPT * getQuestionsType());
	}

	private void addTry(boolean correct) {
		if (lastTries.size >= C.REPEAT_CONCEPT * getQuestionsType()) {
			lastTries.removeIndex(0);
		}
		lastTries.add(correct);
	}

	public void answer(int questionType, boolean correct) {
		queued--;
		presented++;
		addTry(correct);

		if (correct) {
			this.correct++;
			correctTypes.put(questionType, correctTypes.get(questionType) + 1);
		} else {
			this.incorrect++;
			incorrectTypes.put(questionType,
					incorrectTypes.get(questionType) + 1);
		}
		presentedTypes.put(questionType, presentedTypes.get(questionType) + 1);
		updateScore();
		updateState();
	}

	public String serialize() {
		String result = correct + ";" + incorrect + ";";
		for (Boolean b : lastTries) {
			result += (b ? "t" : "f") + ",";
		}
		result += ";";
		for (Entry<Integer, Integer> entry : correctTypes.entrySet()) {
			result += entry.getKey() + "=" + entry.getValue() + ",";
		}
		result += ";";
		for (Entry<Integer, Integer> entry : incorrectTypes.entrySet()) {
			result += entry.getKey() + "=" + entry.getValue() + ",";
		}
		return result;
	}

	public void deserialize(String data) {
		String split[] = data.split(";");
		correct = Integer.parseInt(split[0]);
		incorrect = Integer.parseInt(split[1]);
		presented = correct + incorrect;
		for (String s : split[2].split(",")) {
			addTry(s.equals("t"));
		}

		for (String entry : split[3].split(",")) {
			String entrySplit[] = entry.split("=");
			correctTypes.put(Integer.parseInt(entrySplit[0]),
					Integer.parseInt(entrySplit[1]));
			presentedTypes.put(Integer.parseInt(entrySplit[0]),
					Integer.parseInt(entrySplit[1]));
		}

		for (String entry : split[4].split(",")) {
			String entrySplit[] = entry.split("=");
			incorrectTypes.put(Integer.parseInt(entrySplit[0]),
					Integer.parseInt(entrySplit[1]));
			int value = presentedTypes.get(Integer.parseInt(entrySplit[0]));
			presentedTypes.put(Integer.parseInt(entrySplit[0]),
					Integer.parseInt(entrySplit[1]) + value);
		}

	}

}
