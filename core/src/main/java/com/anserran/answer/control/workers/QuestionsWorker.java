package com.anserran.answer.control.workers;

import com.anserran.answer.C;
import com.anserran.answer.control.Controller;
import com.anserran.answer.control.Worker;
import com.anserran.answer.control.tasks.CreateQuestionsTask;
import com.anserran.answer.control.tasks.LoadConceptsTask;
import com.anserran.answer.model.concepts.Concept;
import com.anserran.answer.model.questions.Question;
import com.badlogic.gdx.assets.AssetLoaderParameters.LoadedCallback;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader.TextureAtlasParameter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;

import java.util.Map;

public class QuestionsWorker implements Worker {

	private Controller controller;

	private final AsyncExecutor asyncExecutor;

	private AsyncResult<Map<Integer, Array<Concept>>> conceptsResult;

	private CreateQuestionsTask questionsTask;

	private AsyncResult<Array<Question>> questionsResult;

	public QuestionsWorker(Controller controller) {
		this.controller = controller;
		asyncExecutor = new AsyncExecutor(5);
	}

	@Override
	public void init() {
		final LoadConceptsTask loadConcepts = new LoadConceptsTask(
				controller.getPreferences());
		TextureAtlasParameter parameters = new TextureAtlasParameter();
		parameters.loadedCallback = new LoadedCallback() {
			@Override
			public void finishedLoading(AssetManager assetManager,
					String fileName, Class type) {
				loadConcepts.setTextureAtlas(assetManager.get(fileName,
						TextureAtlas.class));
			}
		};
		controller.getAssets().load("flags/flags.atlas", TextureAtlas.class,
				parameters);
		conceptsResult = asyncExecutor.submit(loadConcepts);
	}

	@Override
	public void act() {
		if (controller.getAssets().isDone() && conceptsResult != null
				&& conceptsResult.isDone()) {
			questionsTask = new CreateQuestionsTask(conceptsResult.get());
			questionsTask.setLevel(1);
			questionsTask.setMaxLevel(C.GEOGRAPHY_LEVELS);
			conceptsResult = null;
			questionsResult = asyncExecutor.submit(questionsTask);
		}

		if (questionsResult != null && questionsResult.isDone()) {
			controller.addQuestions(questionsResult.get());
			questionsResult = null;
		}

		if (conceptsResult == null && questionsResult == null
				&& controller.getQuestions().size < C.QUESTIONS_CYCLE) {
			questionsTask.setLevel(1);
			questionsTask.setMaxLevel(C.GEOGRAPHY_LEVELS);
			questionsResult = asyncExecutor.submit(questionsTask);
		}
	}
}
