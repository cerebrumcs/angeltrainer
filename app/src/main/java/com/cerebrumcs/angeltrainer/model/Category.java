package com.cerebrumcs.angeltrainer.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;

import java.util.LinkedList;
import java.util.List;

import com.cerebrumcs.angeltrainer.handler.QuestionnaireSelectionEventHandler;

@Entity
public class Category {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;


    public Category(){}

    public Category(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

