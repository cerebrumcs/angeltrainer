package com.cerebrumcs.angeltrainer.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.LinkedList;
import java.util.List;


public class QuestionWithAnswer {

    @Embedded
    private Question question;

    @Relation(parentColumn = "id", entityColumn = "question_id")
    List<Answer> answers = new LinkedList<>();

    public QuestionWithAnswer(){}

    public QuestionWithAnswer(Question question, List<Answer> answers){
        this.question = question;
        this.answers = answers;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer){
        if(!answers.contains(answer)){
            answers.add(answer);
        }
    }

    public Answer getTrueAnswer() {
        Answer trueAnswer = null;
        for(Answer answer : answers){
            if(answer.isTrue()){
                trueAnswer = answer;
            }
        }
        return trueAnswer;
    }
}
