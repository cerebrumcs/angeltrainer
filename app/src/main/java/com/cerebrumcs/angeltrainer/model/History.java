package com.cerebrumcs.angeltrainer.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class History {

    @PrimaryKey(autoGenerate = true)
    long id;

    @ColumnInfo(name = "question_id")
    long questionId;

    @ColumnInfo(name = "answer_id")
    long answerId;

    @ColumnInfo(name = "timestamp")
    long timestamp;

    @ColumnInfo(name = "mode")
    int mode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public int getMode(){
        return this.mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }
}
