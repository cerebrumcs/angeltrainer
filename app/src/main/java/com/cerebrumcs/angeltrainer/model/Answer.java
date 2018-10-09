package com.cerebrumcs.angeltrainer.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Answer {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "text")
    private String answer;

    @ColumnInfo(name = "is_true")
    private boolean isTrue;

    @ColumnInfo(name = "question_id")
    private long questionId;

    public Answer(){}

    public Answer(String answer, boolean isTrue){
        this.answer = answer;
        this.isTrue = isTrue;
    }


    public long getId(){
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAnswer(){
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean isTrue) {
        this.isTrue = isTrue;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }
}

