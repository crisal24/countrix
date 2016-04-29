package com.anserran.countries.view.screens;

import com.anserran.countries.C;
import com.anserran.countries.control.Controller;
import com.anserran.countries.model.questions.ImageQuestion;
import com.anserran.countries.model.questions.Question;
import com.anserran.countries.model.questions.TextQuestion;
import com.anserran.countries.view.DecreasingBar;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import es.eucm.i18n.I18N;

public class QuestionScreen extends Table {

	private Controller controller;

	private I18N i18N;

	private Container<Actor> questionContainer;

	private Container<Actor> answerContainer;

	private Label questionLabel;

	private Image questionImage;

	private TextureRegionDrawable drawable;

	private Array<TextButton> answersButtons;

	private Table optionsTable;

	private Array<Integer> tmpOptions = new Array<Integer>();

	private Label correctLabel;

	private Label incorrectLabel;

	private DecreasingBar timeBar;

	private int correct;

	private int incorrect;

	private Array<Question> questions = new Array<Question>();

	private float nextQuestionTime;

	private boolean nextQuestion = true;

	private Question currentQuestion;

	public QuestionScreen(Controller c) {
		this.controller = c;
		i18N = c.getAssets().getI18N();
		setFillParent(true);
		add(timeBar = new DecreasingBar(c.getAssets().getSkin(), C.STYLE_TIME))
				.expandX().fill().height(c.getAssets().dpToPx(16));
		row();
		Table points = new Table();
		points.add(correctLabel = new Label("0", c.getAssets().getSkin(),
				C.STYLE_CORRECT));
		points.add().expandX();
		points.add(incorrectLabel = new Label("0", c.getAssets().getSkin(),
				C.STYLE_INCORRECT));
		add(points).expandX().fillX();
		row();
		add(questionContainer = new Container<Actor>()).expand().fill()
				.getActor().fill().pad(c.getAssets().dpToPx(24));
		row();
		add(answerContainer = new Container<Actor>()).expandX().fill()
				.getActor().fill().pad(c.getAssets().dpToPx(24));

		answersButtons = new Array<TextButton>();
		optionsTable = new Table();
		CheckListener checkListener = new CheckListener();
		for (int i = 0; i < C.MAX_OPTIONS; i++) {
			TextButton optionButton = new TextButton("", c.getAssets()
					.getSkin());
			optionsTable.add(optionButton).growX().center()
					.padTop(c.getAssets().dpToPx(24));
			optionsTable.row();
			answersButtons.add(optionButton);
			optionButton.addListener(checkListener);
		}
		questionLabel = new Label("", c.getAssets().getSkin(), C.STYLE_TITLE);
		questionImage = new Image(drawable = new TextureRegionDrawable(),
				Scaling.fit);
		questionLabel.setWrap(true);
		questionLabel.setAlignment(Align.center);

	}

	@Override
	protected void setParent(Group parent) {
		super.setParent(parent);
		correct = 0;
		incorrect = 0;
		setVisible(false);
	}

	public void addQuestions(Array<Question> questions) {
		this.questions.addAll(questions);
	}

	@Override
	public void act(float delta) {
		setVisible(true);
		super.act(delta);
		timeBar.setValue(controller.getTime() / C.ROUND_TIME);
		if (nextQuestion) {
			nextQuestionTime -= delta;
			if (nextQuestionTime < 0 && questions.size > 0) {
				setQuestion(currentQuestion = questions.removeIndex(0));
				nextQuestion = false;
				answerContainer.setTouchable(Touchable.enabled);
			}
		}
	}

	private void setQuestion(Question question) {
		setVisible(true);
		if (question instanceof TextQuestion) {
			TextQuestion textQuestion = (TextQuestion) question;
			if (question instanceof ImageQuestion) {
				TextureAtlas atlas = ((ImageQuestion) question).getAtlas();
				drawable.setRegion(atlas.findRegion(textQuestion
						.getQuestionKey()));
				questionContainer.setActor(questionImage);
				questionImage.invalidate();
			} else {
				questionLabel.setText(i18N.m(textQuestion.getQuestionKey(),
						textQuestion.getQuestionParameter()));
				questionContainer.setActor(questionLabel);
			}

			tmpOptions.clear();
			for (int i = 0; i < textQuestion.getAnswers().length; i++) {
				tmpOptions.add(i);
			}
			tmpOptions.shuffle();
			int i = 0;
			for (TextButton option : answersButtons) {
				option.setStyle(controller.getAssets().getSkin()
						.get(TextButtonStyle.class));
				if (i < tmpOptions.size) {
					option.setVisible(true);
					String answerKey = textQuestion.getAnswers()[tmpOptions
							.get(i)];
					option.setUserObject(tmpOptions.get(i));
					option.setText(i18N.m(answerKey));
				} else {
					option.setVisible(false);
					option.setUserObject(false);
				}
				i++;
			}
			answerContainer.setActor(optionsTable);
		}
	}

	public Array<Question> getQuestions() {
		return questions;
	}

	public class CheckListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if (controller.answer(currentQuestion, (Integer) event
					.getListenerActor().getUserObject())) {
				((TextButton) event.getListenerActor()).setStyle(controller
						.getAssets().getSkin()
						.get(C.STYLE_CORRECT, TextButtonStyle.class));
				correct++;
				correctLabel.setText(correct + "");
			} else {
				((TextButton) event.getListenerActor()).setStyle(controller
						.getAssets().getSkin()
						.get(C.STYLE_INCORRECT, TextButtonStyle.class));
				incorrect++;
				incorrectLabel.setText(incorrect + "");
			}
			nextQuestionTime = C.FEEDBACK_TIME;
			nextQuestion = true;
			answerContainer.setTouchable(Touchable.disabled);
		}
	}
}
