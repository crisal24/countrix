package com.anserran.countries.control;

import com.anserran.countries.C;
import com.anserran.countries.assets.Assets;
import com.anserran.countries.control.workers.QuestionsWorker;
import com.anserran.countries.model.questions.ImageQuestion;
import com.anserran.countries.model.questions.Question;
import com.anserran.countries.model.questions.TextQuestion;
import com.anserran.countries.view.screens.LoadScreen;
import com.anserran.countries.view.screens.QuestionScreen;
import com.anserran.countries.view.screens.SplashScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import es.eucm.gleaner.tracker.XAPITracker;
import es.eucm.gleaner.tracker.XAPITracker.Alternative;
import es.eucm.gleaner.tracker.XAPITracker.Completable;
import es.eucm.gleaner.tracker.storage.NetStorage;
import es.eucm.gleaner.viewer.TraceViewer;

import java.util.HashMap;
import java.util.Map;

public class Controller {

	private Preferences preferences;

	private final QuestionScreen questionsScreen;
	private Group root;

	private Assets assets;

	private Map<Class, Actor> screens;

	private Worker worker;

	private Class currentScreen;

	private float time;

	private int score;

	private XAPITracker tracker;

	private TraceViewer traceViewer;

	public Controller(Group root) {
		this.root = root;
		preferences = Gdx.app.getPreferences("concepts");
		assets = new Assets();
		screens = new HashMap<Class, Actor>();
		addScreen(new SplashScreen(this));
		addScreen(questionsScreen = new QuestionScreen(this));
		addScreen(new LoadScreen(this));

		ObjectMap<String, Object> config = new Json().fromJson(ObjectMap.class,
				Gdx.files.internal("config.json"));

		if (config.containsKey("endpoint")
				&& config.containsKey("trackingCode")) {
			NetStorage netStorage = new NetStorage(Gdx.net, config.get(
					"endpoint").toString(), config.get("trackingCode")
					.toString());
			netStorage.setAuthorization("a:");
			tracker = new XAPITracker(netStorage, 1);

			if (config.get("showStatements", false) == Boolean.TRUE) {
				traceViewer = new TraceViewer(assets.getSkin(), tracker);
				traceViewer.setFillParent(true);
			}
		}
	}

	private void addScreen(Actor screen) {
		screens.put(screen.getClass(), screen);
	}

	private <T extends Actor> void setScreen(Class<T> screenClass) {

		if (tracker != null) {
			if (currentScreen == null) {
				tracker.initialized("_session", Completable.SERIOUS_GAME);
			}

			if (screenClass == QuestionScreen.class) {
				tracker.initialized("Questions", Completable.LEVEL);
			}
		}

		if (currentScreen != screenClass) {
			if (tracker != null && currentScreen == QuestionScreen.class) {
				tracker.completed("Questions", Completable.LEVEL);
			}

			root.clearChildren();
			root.addActor(screens.get(screenClass));
			this.currentScreen = screenClass;
			if (traceViewer != null
					&& getPreferences().getBoolean("xapi", false)) {
				root.addActor(traceViewer);
			}
		}
	}

	public Assets getAssets() {
		return assets;
	}

	public void splash() {
		setScreen(SplashScreen.class);
	}

	public void start() {
		setScreen(LoadScreen.class);
		worker = new QuestionsWorker(this);
		worker.init();
	}

	public void act(float delta) {
		if (tracker != null) {
			tracker.update(delta);
		}
		assets.update(250);
		if (worker != null) {
			worker.act();
		}

		if (currentScreen == QuestionScreen.class) {
			time -= delta;
			if (time <= 0.0f) {
				int maxScore = preferences.getInteger("max_score");
				if (score > maxScore) {
					preferences.putInteger("max_score", score);
				}
				score = 0;
				preferences.flush();
				setScreen(SplashScreen.class);
			}
		}
	}

	public void addQuestions(Array<Question> questions) {
		if (currentScreen != QuestionScreen.class) {
			setScreen(QuestionScreen.class);
			time = C.ROUND_TIME;
		}
		questionsScreen.addQuestions(questions);
	}

	public boolean answer(Question question, int option) {
		boolean correct = question.answer(option);
		String id = question.getConcept().getId();
		preferences.putString(id, question.getConcept().serialize());

		TextQuestion textQuestion = (TextQuestion) question;

		if (textQuestion instanceof ImageQuestion) {
			id += "Flag";
		} else if (textQuestion.getQuestionKey().equals("is_capital")) {
			id += "IsCapitalOf";
		} else if (textQuestion.getQuestionKey().equals("capital_of")) {
			id += "Capital";
		} else if (textQuestion.getQuestionKey().equals("continent_of")) {
			id += "Continent";
		}

		if (!correct) {
			time -= C.ERROR_TIME;
		} else {
			score++;
		}

		if (tracker != null) {
			tracker.setScore(score);
			tracker.selected(id, Alternative.QUESTION,
					textQuestion.getAnswers()[option]);

		}

		return correct;
	}

	public void dispose() {
		if (tracker != null) {
			tracker.close();
		}
		preferences.flush();
		assets.dispose();
	}

	public Array<Question> getQuestions() {
		return questionsScreen.getQuestions();
	}

	public Preferences getPreferences() {
		return preferences;
	}

	public float getTime() {
		return time;
	}
}
