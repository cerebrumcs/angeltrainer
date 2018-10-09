package com.cerebrumcs.angeltrainer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Questionnaire {

    public static final String ANSWERED_PROPERTY = "ANSWERED_PROPERTY";
    public static String CURRENT_QUESTION_PROPERTY = "CURRENT_QUESTION_PROPERTY";

    private List<QuestionWithAnswer> questions = new LinkedList<>();
    private QuestionWithAnswer currentQuestion = null;
    private Answer currentAnswer = null;
    private int index;
    private int category;
    private int mode;

    ///////// PROPERTY CHANGE SUPPORT /////////

    private PropertyChangeSupport pts = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener){
        pts.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener){
        pts.removePropertyChangeListener(listener);
    }

    ////////////////////////////////////////

    public Questionnaire(List<QuestionWithAnswer> questions){
        this.questions = questions;
    }

    public Questionnaire(List<QuestionWithAnswer> questions, int startIndex){
        this.questions = questions;
        this.index = startIndex;
    }

    public QuestionWithAnswer askNextQuestion(){
        QuestionWithAnswer lastQuestion = this.currentQuestion;
        this.currentQuestion = questions.get(index);
        index = index + 1 % questions.size();
        pts.firePropertyChange(CURRENT_QUESTION_PROPERTY, lastQuestion, currentQuestion);

        return this.currentQuestion;
    }

    public QuestionWithAnswer getCurrentQuestion() {
        return currentQuestion;
    }

    public void doAswer(Answer answer) {
        Answer lastAnswer = this.currentAnswer;
        this.currentAnswer = answer;
        this.pts.firePropertyChange(ANSWERED_PROPERTY, lastAnswer, currentAnswer);
    }


    public Answer getCurrentAnswer() {
        return currentAnswer;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode){
        this.mode = mode;
    }
}
