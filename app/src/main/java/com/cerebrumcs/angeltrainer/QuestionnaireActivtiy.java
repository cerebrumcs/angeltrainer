package com.cerebrumcs.angeltrainer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cerebrumcs.angeltrainer.handler.AnswerHandler;
import com.cerebrumcs.angeltrainer.handler.QuestionnaireLoader;
import com.cerebrumcs.angeltrainer.model.Questionnaire;
import com.cerebrumcs.angeltrainer.model.QuestionnaireMode;


public class QuestionnaireActivtiy extends AppCompatActivity {

    public void init(Questionnaire questionnaire){

        TextView questionTextView = (TextView)findViewById(R.id.txtQuestion);
        CheckBox answerCheckBox1 = (CheckBox)findViewById(R.id.ckbAnswer1);
        CheckBox answerCheckBox2 = (CheckBox)findViewById(R.id.ckbAnswer2);
        CheckBox answerCheckBox3 = (CheckBox)findViewById(R.id.ckbAnswer3);

        AnswerHandler answerHandler = new AnswerHandler(questionnaire, questionTextView, answerCheckBox1, answerCheckBox2, answerCheckBox3);

        questionnaire.askNextQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        int selection = this.getIntent().getIntExtra("selection", 0);
        boolean random = this.getIntent().getBooleanExtra("random", false);

        Questionnaire questionnaire = QuestionnaireLoader.load(selection, QuestionnaireMode.getMode(random, selection));
        init(questionnaire);

    }


}
