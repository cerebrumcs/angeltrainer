package com.cerebrumcs.angeltrainer.handler;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cerebrumcs.angeltrainer.QuestionnaireActivtiy;
import com.cerebrumcs.angeltrainer.database.DatabaseUtils;
import com.cerebrumcs.angeltrainer.model.Answer;
import com.cerebrumcs.angeltrainer.model.History;
import com.cerebrumcs.angeltrainer.model.QuestionWithAnswer;
import com.cerebrumcs.angeltrainer.model.Questionnaire;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

public class AnswerHandler implements PropertyChangeListener, View.OnClickListener {

    private Questionnaire questionnaire;

    private TextView txt;
    private final CheckBox cb1;
    private final CheckBox cb2;
    private final CheckBox cb3;

    private BiMap<CheckBox, Answer> checkboxBinding = HashBiMap.create();

    public AnswerHandler(Questionnaire questionnaire, TextView txt, CheckBox cb1, CheckBox cb2, CheckBox cb3) {

        this.txt = txt;
        this.questionnaire = questionnaire;
        this.cb1 = cb1;
        this.cb2 = cb2;
        this.cb3 = cb3;
        this.questionnaire.addPropertyChangeListener(this);

        cb1.setOnClickListener(this);
        cb2.setOnClickListener(this);
        cb3.setOnClickListener(this);
    }

    public void bind(Answer... answers) {
        checkboxBinding.forcePut(cb1, answers[0]);
        checkboxBinding.forcePut(cb2, answers[1]);
        checkboxBinding.forcePut(cb3, answers[2]);
    }

    public void disableCheckboxes(){
        for (CheckBox cb : checkboxBinding.keySet()){
            cb.setEnabled(false);
        }
    }

    public void resetCheckboxes(){
        for (CheckBox cb : checkboxBinding.keySet()){
            cb.setChecked(false);
            cb.setBackgroundColor(Color.TRANSPARENT);
            cb.setEnabled(true);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == Questionnaire.CURRENT_QUESTION_PROPERTY) {
            resetCheckboxes();
            QuestionWithAnswer question = (QuestionWithAnswer) evt.getNewValue();
            List<Answer> answers = question.getAnswers();
            bind(answers.get(0), answers.get(1), answers.get(2));

            txt.setText(question.getQuestion().getQuestion());

            for (Map.Entry<CheckBox, Answer> entry : checkboxBinding.entrySet()) {
                entry.getKey().setText(entry.getValue().getAnswer());
            }


        } else if (evt.getPropertyName() == Questionnaire.ANSWERED_PROPERTY) {

            QuestionWithAnswer question = questionnaire.getCurrentQuestion();

            final Answer answer = questionnaire.getCurrentAnswer();
            final Answer trueAnswer = question.getTrueAnswer();

            final QuestionnaireActivtiy activity = (QuestionnaireActivtiy) cb1.getContext();

            final CheckBox cbAnswer = checkboxBinding.inverse().get(answer);
            final CheckBox cbTrue = checkboxBinding.inverse().get(trueAnswer);

            // Create and store history object

            History history = new History();
            history.setMode(this.questionnaire.getMode());
            history.setTimestamp(System.currentTimeMillis());
            history.setQuestionId(this.questionnaire.getCurrentQuestion().getQuestion().getId());
            history.setAnswerId(this.questionnaire.getCurrentAnswer().getId());

            disableCheckboxes();
            cbTrue.setBackgroundColor(Color.GREEN);
            if (answer.getId() != trueAnswer.getId()) {
                cbAnswer.setBackgroundColor(Color.RED);
            }

            // Store History
            DatabaseUtils.getDb().historyDao().addHistory(history);

            Runnable r = new Runnable(){
                @Override
                public void run() {
                    questionnaire.askNextQuestion();
                }
            };

            new Handler().postDelayed(r, 2000);
        }

    }

    @Override
    public void onClick(View v) {
        CheckBox cb = (CheckBox) v;
        Answer answer = checkboxBinding.get(cb);
        questionnaire.doAswer(answer);
    }
}
