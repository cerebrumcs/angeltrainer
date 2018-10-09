package com.cerebrumcs.angeltrainer.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Question {

    @PrimaryKey(autoGenerate = true)
    long id;

    @ColumnInfo(name = "text")
    private String question;

    @ColumnInfo(name = "category_id")
    private long categoryId;

    public Question(){}

    public Question(String question, long categoryId){
        this.question = question;
        this.categoryId = categoryId;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }



    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}

